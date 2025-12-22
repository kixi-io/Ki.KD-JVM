package io.kixi.kd

import io.kixi.text.ParseException
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldBeEmpty
import java.math.BigDecimal as Dec
import java.net.URL

/**
 * Comprehensive tests for KDParser covering:
 * - Tag structure (annotations, namespaces, values, attributes, children)
 * - String literals (simple, raw, block, backtick)
 * - Char literals
 * - Int literals (decimal, hex, binary)
 * - Long literals
 * - Float literals
 * - Double literals
 * - Dec (Dec) literals
 * - Bool literals
 * - URL literals
 */
class KDParserTest : FunSpec({

    val parser = KDParser()

    // ========================================================================
    // Empty and Basic Tag Structure
    // ========================================================================

    context("empty content") {
        test("empty string returns root tag") {
            val tag = parser.parse("")
            tag.nsid.name shouldBe "root"
            tag.children.shouldBeEmpty()
        }

        test("whitespace only returns root tag") {
            val tag = parser.parse("   \n\t\n   ")
            tag.nsid.name shouldBe "root"
            tag.children.shouldBeEmpty()
        }

        test("comment only returns root tag") {
            val tag = parser.parse("# just a comment")
            tag.nsid.name shouldBe "root"
            tag.children.shouldBeEmpty()
        }
    }

    context("simple named tags") {
        test("tag with name only") {
            val tag = parser.parse("my_tag")
            tag.nsid.name shouldBe "my_tag"
            tag.values.shouldBeEmpty()
            tag.attributes.shouldBeEmpty()
        }

        test("tag with namespace") {
            val tag = parser.parse("my_ns:my_tag")
            tag.nsid.name shouldBe "my_tag"
            tag.nsid.namespace shouldBe "my_ns"
        }

        test("tag with underscore in name") {
            val tag = parser.parse("my_cool_tag")
            tag.nsid.name shouldBe "my_cool_tag"
        }

        test("tag with dollar sign in name") {
            val tag = parser.parse("\$special")
            tag.nsid.name shouldBe "\$special"
        }

        test("tag starting with underscore") {
            val tag = parser.parse("_private")
            tag.nsid.name shouldBe "_private"
        }

        test("tag with unicode letters") {
            val tag = parser.parse("日本語")
            tag.nsid.name shouldBe "日本語"
        }
    }

    context("multiple top-level tags") {
        test("multiple tags wrapped in root") {
            val tag = parser.parse("""
                tag1
                tag2
                tag3
            """.trimIndent())

            tag.nsid.name shouldBe "root"
            tag.children shouldHaveSize 3
            tag.children[0].nsid.name shouldBe "tag1"
            tag.children[1].nsid.name shouldBe "tag2"
            tag.children[2].nsid.name shouldBe "tag3"
        }

        test("tags separated by semicolons on same line") {
            val tag = parser.parse("tag1; tag2; tag3")
            tag.nsid.name shouldBe "root"
            tag.children shouldHaveSize 3
        }
    }

    // ========================================================================
    // Anonymous Tags
    // ========================================================================

    context("anonymous tags") {
        test("anonymous tag with string value") {
            val root = parser.parse("""
                greetings {
                    "hello" language="English"
                }
            """.trimIndent())

            val greetings = root
            greetings.nsid.name shouldBe "greetings"
            greetings.children shouldHaveSize 1

            val anon = greetings.children[0]
            anon.isAnonymous() shouldBe true
            anon.value shouldBe "hello"
            anon["language"] shouldBe "English"
        }

        test("multiple anonymous tags") {
            val root = parser.parse("""
                files {
                    "/folder1/file.txt"
                    "/file2.txt"
                }
            """.trimIndent())

            root.children shouldHaveSize 2
            root.children[0].isAnonymous() shouldBe true
            root.children[0].value shouldBe "/folder1/file.txt"
            root.children[1].value shouldBe "/file2.txt"
        }
    }

    // ========================================================================
    // Tag with Values
    // ========================================================================

    context("tags with values") {
        test("tag with single string value") {
            val tag = parser.parse("""book "Lord of the Rings"""")
            tag.nsid.name shouldBe "book"
            tag.values shouldHaveSize 1
            tag.value shouldBe "Lord of the Rings"
        }

        test("tag with multiple values") {
            val tag = parser.parse("nums 3 5 7 11")
            tag.nsid.name shouldBe "nums"
            tag.values shouldHaveSize 4
            tag.values shouldContainExactly listOf(3, 5, 7, 11)
        }

        test("tag with mixed type values") {
            val tag = parser.parse("""mixed "hello" 42 true 3.14""")
            tag.values shouldHaveSize 4
            tag.values[0] shouldBe "hello"
            tag.values[1] shouldBe 42
            tag.values[2] shouldBe true
            tag.values[3] shouldBe 3.14
        }
    }

    // ========================================================================
    // Tag with Attributes
    // ========================================================================

    context("tags with attributes") {
        test("tag with single attribute") {
            val tag = parser.parse("pet type=\"dog\"")
            tag.nsid.name shouldBe "pet"
            tag["type"] shouldBe "dog"
        }

        test("tag with multiple attributes") {
            val tag = parser.parse("pets chihuahua=\"small\" dalmation=\"hyper\" mastiff=\"big\"")
            tag["chihuahua"] shouldBe "small"
            tag["dalmation"] shouldBe "hyper"
            tag["mastiff"] shouldBe "big"
        }

        test("tag with namespaced attribute") {
            val tag = parser.parse("person name:first=\"John\" name:last=\"Doe\"")
            // Namespaced attributes are stored with proper namespace and name
            tag.attributes.size shouldBe 2
            // Access via NSID to verify namespace parsing
            tag["first", "name"] shouldBe "John"
            tag["last", "name"] shouldBe "Doe"
        }

        test("tag with values and attributes") {
            val tag = parser.parse("""person "John" "Doe" age=30 active=true""")
            tag.values shouldHaveSize 2
            tag.values[0] shouldBe "John"
            tag.values[1] shouldBe "Doe"
            tag["age"] shouldBe 30
            tag["active"] shouldBe true
        }
    }

    // ========================================================================
    // Tag with Children
    // ========================================================================

    context("tags with children") {
        test("simple parent-child structure") {
            val tag = parser.parse("""
                parent {
                    child1
                    child2
                }
            """.trimIndent())

            tag.nsid.name shouldBe "parent"
            tag.children shouldHaveSize 2
            tag.children[0].nsid.name shouldBe "child1"
            tag.children[1].nsid.name shouldBe "child2"
        }

        test("nested children") {
            val tag = parser.parse("""
                grandparent {
                    parent {
                        child
                    }
                }
            """.trimIndent())

            tag.nsid.name shouldBe "grandparent"
            tag.children shouldHaveSize 1
            tag.children[0].nsid.name shouldBe "parent"
            tag.children[0].children shouldHaveSize 1
            tag.children[0].children[0].nsid.name shouldBe "child"
        }

        test("deep nesting") {
            val tag = parser.parse("""
                plants {
                    trees {
                        deciduous {
                            elm
                            oak
                        }
                    }
                }
            """.trimIndent())

            val trees = tag.getChild("trees")
            trees shouldNotBe null
            val deciduous = trees?.getChild("deciduous")
            deciduous shouldNotBe null
            deciduous?.children?.shouldHaveSize(2)
        }

        test("children with values and attributes") {
            val tag = parser.parse("""
                folder "myFiles" color="yellow" {
                    file "readme.txt" size=1024
                }
            """.trimIndent())

            tag.value shouldBe "myFiles"
            tag["color"] shouldBe "yellow"
            tag.children shouldHaveSize 1
            tag.children[0].value shouldBe "readme.txt"
            tag.children[0]["size"] shouldBe 1024
        }
    }

    // ========================================================================
    // Matrix / Grid Pattern
    // ========================================================================

    context("matrix pattern") {
        test("integer matrix") {
            val tag = parser.parse("""
                matrix {
                    1 2 3
                    4 5 6
                    7 8 9
                }
            """.trimIndent())

            tag.nsid.name shouldBe "matrix"
            tag.children shouldHaveSize 3

            val rows = tag.getChildrenValues<Int>()
            rows shouldHaveSize 3
            rows[0] shouldContainExactly listOf(1, 2, 3)
            rows[1] shouldContainExactly listOf(4, 5, 6)
            rows[2] shouldContainExactly listOf(7, 8, 9)
        }
    }

    // ========================================================================
    // Annotations
    // ========================================================================

    context("annotations") {
        test("simple annotation") {
            val tag = parser.parse("""
                @Test
                myTag "data"
            """.trimIndent())

            tag.nsid.name shouldBe "myTag"
            tag.annotations shouldHaveSize 1
            tag.annotations[0].nsid.name shouldBe "Test"
        }

        test("annotation with single value") {
            val tag = parser.parse("""
                @Test(true)
                myTag
            """.trimIndent())

            tag.annotations shouldHaveSize 1
            tag.annotations[0].value shouldBe true
        }

        test("annotation with values and attributes") {
            val tag = parser.parse("""
                @Test(true log="output.txt")
                myTag
            """.trimIndent())

            tag.annotations shouldHaveSize 1
            tag.annotations[0].value shouldBe true
            tag.annotations[0]["log"] shouldBe "output.txt"
        }

        test("multiple annotations") {
            val tag = parser.parse("""
                @UI(framework="HTML5")
                @Test
                myTag
            """.trimIndent())

            tag.annotations shouldHaveSize 2
            tag.annotations[0].nsid.name shouldBe "UI"
            tag.annotations[0]["framework"] shouldBe "HTML5"
            tag.annotations[1].nsid.name shouldBe "Test"
        }

        test("annotation on same line") {
            val tag = parser.parse("@Personal favorite_books")

            tag.nsid.name shouldBe "favorite_books"
            tag.annotations shouldHaveSize 1
            tag.annotations[0].nsid.name shouldBe "Personal"
        }

        test("namespaced annotation") {
            val tag = parser.parse("""
                @io:Serializable
                myTag
            """.trimIndent())

            tag.annotations[0].nsid.namespace shouldBe "io"
            tag.annotations[0].nsid.name shouldBe "Serializable"
        }
    }

    // ========================================================================
    // Comments
    // ========================================================================

    context("comments") {
        test("hash comment") {
            val tag = parser.parse("""
                # This is a comment
                myTag "value"
            """.trimIndent())

            tag.nsid.name shouldBe "myTag"
            tag.value shouldBe "value"
        }

        test("double-slash comment") {
            val tag = parser.parse("""
                // This is a comment
                myTag "value"
            """.trimIndent())

            tag.nsid.name shouldBe "myTag"
        }

        test("inline comment") {
            val tag = parser.parse("""myTag "value" # inline comment""")
            tag.value shouldBe "value"
        }

        test("block comment") {
            val tag = parser.parse("""
                /* This is a
                   block comment */
                myTag "value"
            """.trimIndent())

            tag.nsid.name shouldBe "myTag"
        }

        test("nested block comments") {
            val tag = parser.parse("""
                /* outer /* nested */ still outer */
                myTag
            """.trimIndent())

            tag.nsid.name shouldBe "myTag"
        }

        test("comment between values") {
            val tag = parser.parse("""myTag 1 /* skip */ 2 3""")
            tag.values shouldContainExactly listOf(1, 2, 3)
        }
    }

    // ========================================================================
    // 1. String Literals
    // ========================================================================

    context("string literals") {
        context("simple strings") {
            test("basic string") {
                val tag = parser.parse("""str "hello world"""")
                tag.value shouldBe "hello world"
            }

            test("empty string") {
                val tag = parser.parse("""str """"")
                tag.value shouldBe ""
            }

            test("unicode string") {
                val tag = parser.parse("""str "ท้องฟ้า"""")
                tag.value shouldBe "ท้องฟ้า"
            }

            test("string with escape sequences") {
                val tag = parser.parse("""str "line1\nline2\ttab"""")
                tag.value shouldBe "line1\nline2\ttab"
            }

            test("string with escaped quote") {
                val tag = parser.parse("""str "say \"hello\""""")
                tag.value shouldBe "say \"hello\""
            }

            test("string with escaped backslash") {
                val tag = parser.parse("""str "path\\to\\file"""")
                tag.value shouldBe "path\\to\\file"
            }

            test("string with carriage return escape") {
                val tag = parser.parse("""str "before\rafter"""")
                tag.value shouldBe "before\rafter"
            }

            test("string with unicode escape") {
                val tag = parser.parse("""str "A is \u0041"""")
                tag.value shouldBe "A is A"
            }
        }

        context("raw strings") {
            test("basic raw string") {
                val tag = parser.parse("""str @"C:\files\readme.md"""")
                tag.value shouldBe "C:\\files\\readme.md"
            }

            test("raw string preserves escapes") {
                val tag = parser.parse("""str @"whitespace:\t\n"""")
                tag.value shouldBe "whitespace:\\t\\n"
            }
        }

        context("block strings") {
            test("basic block string") {
                val tag = parser.parse("""
                    text ${'"'}${'"'}${'"'}
                    Hello
                    World
                    ${'"'}${'"'}${'"'}
                """.trimIndent())

                tag.value shouldBe "Hello\nWorld"
            }

            test("block string preserves internal newlines") {
                val tag = parser.parse("text \"\"\"line1\nline2\nline3\"\"\"")
                val value = tag.value as String
                value.lines().size shouldBe 3
            }
        }

        context("backtick strings") {
            test("single backtick raw string") {
                val tag = parser.parse("str `no escapes \\n here`")
                tag.value shouldBe "no escapes \\n here"
            }

            test("triple backtick multiline") {
                val tag = parser.parse("str ```line1\nline2```")
                (tag.value as String).contains("\n") shouldBe true
            }
        }
    }

    // ========================================================================
    // 2. Char Literals
    // ========================================================================

    context("char literals") {
        test("basic char") {
            val tag = parser.parse("ch 'A'")
            tag.value shouldBe 'A'
        }

        test("unicode char") {
            val tag = parser.parse("ch '桜'")
            tag.value shouldBe '桜'
        }

        test("cyrillic char") {
            val tag = parser.parse("ch 'Ж'")
            tag.value shouldBe 'Ж'
        }

        test("escaped newline char") {
            val tag = parser.parse("ch '\\n'")
            tag.value shouldBe '\n'
        }

        test("escaped tab char") {
            val tag = parser.parse("ch '\\t'")
            tag.value shouldBe '\t'
        }

        test("escaped single quote") {
            val tag = parser.parse("ch '\\''")
            tag.value shouldBe '\''
        }

        test("escaped backslash") {
            val tag = parser.parse("ch '\\\\'")
            tag.value shouldBe '\\'
        }

        test("space char") {
            val tag = parser.parse("ch ' '")
            tag.value shouldBe ' '
        }

        test("digit char") {
            val tag = parser.parse("ch '5'")
            tag.value shouldBe '5'
        }

        test("unicode escape in char") {
            val tag = parser.parse("ch '\\u0041'")
            tag.value shouldBe 'A'
        }
    }

    // ========================================================================
    // 3. Int Literals
    // ========================================================================

    context("int literals") {
        test("zero") {
            val tag = parser.parse("num 0")
            tag.value shouldBe 0
            tag.value.shouldBeInstanceOf<Int>()
        }

        test("positive int") {
            val tag = parser.parse("num 123")
            tag.value shouldBe 123
        }

        test("negative int") {
            val tag = parser.parse("num -456")
            tag.value shouldBe -456
        }

        test("max int") {
            val tag = parser.parse("num ${Int.MAX_VALUE}")
            tag.value shouldBe Int.MAX_VALUE
        }

        test("min int") {
            val tag = parser.parse("num ${Int.MIN_VALUE}")
            tag.value shouldBe Int.MIN_VALUE
        }

        test("int with underscores") {
            val tag = parser.parse("num 1_000_000")
            tag.value shouldBe 1_000_000
        }

        test("hex int") {
            val tag = parser.parse("num 0xFF")
            tag.value shouldBe 255
        }

        test("hex int uppercase") {
            val tag = parser.parse("num 0XAB")
            tag.value shouldBe 0xAB
        }

        test("hex int with underscores") {
            val tag = parser.parse("num 0xFF_FF")
            tag.value shouldBe 0xFFFF
        }

        test("binary int") {
            val tag = parser.parse("num 0b1010")
            tag.value shouldBe 10
        }

        test("binary int with underscores") {
            val tag = parser.parse("num 0b1111_0000")
            tag.value shouldBe 0b11110000
        }

        test("positive sign int") {
            val tag = parser.parse("num +42")
            tag.value shouldBe 42
        }
    }

    // ========================================================================
    // 4. Long Literals
    // ========================================================================

    context("long literals") {
        test("basic long") {
            val tag = parser.parse("num 123L")
            tag.value shouldBe 123L
            tag.value.shouldBeInstanceOf<Long>()
        }

        test("large long") {
            val tag = parser.parse("num 9999999999L")
            tag.value shouldBe 9999999999L
        }

        test("negative long") {
            val tag = parser.parse("num -123L")
            tag.value shouldBe -123L
        }

        test("max long") {
            val tag = parser.parse("num ${Long.MAX_VALUE}L")
            tag.value shouldBe Long.MAX_VALUE
        }

        test("long with underscores") {
            val tag = parser.parse("num 1_000_000_000_000L")
            tag.value shouldBe 1_000_000_000_000L
        }

        test("hex long") {
            val tag = parser.parse("num 0xFFFFFFFFFL")
            tag.value shouldBe 0xFFFFFFFFF
        }

        test("auto promotion to long when int overflows") {
            // Value larger than Int.MAX_VALUE should become Long
            val tag = parser.parse("num 3000000000")
            tag.value.shouldBeInstanceOf<Long>()
            tag.value shouldBe 3000000000L
        }
    }

    // ========================================================================
    // 5. Float Literals
    // ========================================================================

    context("float literals") {
        test("basic float") {
            val tag = parser.parse("num 123.45f")
            tag.value shouldBe 123.45f
            tag.value.shouldBeInstanceOf<Float>()
        }

        test("float uppercase F") {
            val tag = parser.parse("num 123.45F")
            tag.value shouldBe 123.45f
        }

        test("negative float") {
            val tag = parser.parse("num -42.5f")
            tag.value shouldBe -42.5f
        }

        test("float with leading decimal") {
            val tag = parser.parse("num 0.5f")
            tag.value shouldBe 0.5f
        }

        test("integer as float") {
            val tag = parser.parse("num 100f")
            tag.value shouldBe 100f
        }

        test("float with exponent") {
            val tag = parser.parse("num 1.5e3f")
            tag.value shouldBe 1500f
        }

        test("float with negative exponent") {
            val tag = parser.parse("num 1.5e-2f")
            tag.value shouldBe 0.015f
        }
    }

    // ========================================================================
    // 6. Double Literals
    // ========================================================================

    context("double literals") {
        test("basic double (implicit)") {
            val tag = parser.parse("num 123.45")
            tag.value shouldBe 123.45
            tag.value.shouldBeInstanceOf<Double>()
        }

        test("explicit double with d suffix") {
            val tag = parser.parse("num 123d")
            tag.value shouldBe 123.0
        }

        test("explicit double with D suffix") {
            val tag = parser.parse("num 123D")
            tag.value shouldBe 123.0
        }

        test("negative double") {
            val tag = parser.parse("num -99.99")
            tag.value shouldBe -99.99
        }

        test("double with exponent") {
            val tag = parser.parse("num 1.5e10")
            tag.value shouldBe 1.5e10
        }

        test("double with uppercase E") {
            val tag = parser.parse("num 2.5E5")
            tag.value shouldBe 2.5e5
        }

        test("double with negative exponent") {
            val tag = parser.parse("num 3.14e-5")
            tag.value shouldBe 3.14e-5
        }

        test("double with underscores") {
            val tag = parser.parse("num 1_234.567_89")
            tag.value shouldBe 1234.56789
        }

        test("positive infinity") {
            val tag = parser.parse("num Infinity")
            tag.value shouldBe Double.POSITIVE_INFINITY
        }

        test("NaN") {
            val tag = parser.parse("num NaN")
            (tag.value as Double).isNaN() shouldBe true
        }
    }

    // ========================================================================
    // 7. Dec (Dec) Literals
    // ========================================================================

    context("Dec literals") {
        test("basic Dec lowercase") {
            val tag = parser.parse("num 123.45bd")
            tag.value.shouldBeInstanceOf<Dec>()
            tag.value shouldBe Dec("123.45")
        }

        test("basic Dec uppercase") {
            val tag = parser.parse("num 123.45BD")
            tag.value shouldBe Dec("123.45")
        }

        test("integer as Dec") {
            val tag = parser.parse("num 100bd")
            tag.value shouldBe Dec("100")
        }

        test("negative Dec") {
            val tag = parser.parse("num -999.99bd")
            tag.value shouldBe Dec("-999.99")
        }

        test("high precision Dec") {
            val tag = parser.parse("num 123.456789012345678901234567890bd")
            tag.value shouldBe Dec("123.456789012345678901234567890")
        }

        test("Dec with underscores") {
            val tag = parser.parse("num 1_000_000.50bd")
            tag.value shouldBe Dec("1000000.50")
        }

        test("small Dec") {
            val tag = parser.parse("num 0.00001bd")
            tag.value shouldBe Dec("0.00001")
        }
    }

    // ========================================================================
    // 8. Bool Literals
    // ========================================================================

    context("bool literals") {
        test("true") {
            val tag = parser.parse("flag true")
            tag.value shouldBe true
            tag.value.shouldBeInstanceOf<Boolean>()
        }

        test("false") {
            val tag = parser.parse("flag false")
            tag.value shouldBe false
        }

        test("on (SDL compatibility)") {
            val tag = parser.parse("flag on")
            tag.value shouldBe true
        }

        test("off (SDL compatibility)") {
            val tag = parser.parse("flag off")
            tag.value shouldBe false
        }

        test("multiple bool values") {
            val tag = parser.parse("flags true false true")
            tag.values shouldContainExactly listOf(true, false, true)
        }

        test("bool as attribute") {
            val tag = parser.parse("item active=true visible=false")
            tag["active"] shouldBe true
            tag["visible"] shouldBe false
        }
    }

    // ========================================================================
    // 9. URL Literals
    // ========================================================================

    context("URL literals") {
        test("https URL") {
            val tag = parser.parse("site <https://www.example.com>")
            tag.value.shouldBeInstanceOf<URL>()
            (tag.value as URL).toString() shouldBe "https://www.example.com"
        }

        test("http URL") {
            val tag = parser.parse("site <http://example.com/path>")
            (tag.value as URL).protocol shouldBe "http"
        }

        test("URL with path and query") {
            val tag = parser.parse("site <https://example.com/path?query=value>")
            val url = tag.value as URL
            url.path shouldBe "/path"
            url.query shouldBe "query=value"
        }

        test("URL with port") {
            val tag = parser.parse("site <http://localhost:8080/api>")
            val url = tag.value as URL
            url.port shouldBe 8080
        }

        test("file URL") {
            val tag = parser.parse("file <file:///home/user/doc.txt>")
            (tag.value as URL).protocol shouldBe "file"
        }

        test("ftp URL") {
            val tag = parser.parse("ftp <ftp://ftp.example.com/file.zip>")
            (tag.value as URL).protocol shouldBe "ftp"
        }

        test("URL as attribute") {
            val tag = parser.parse("resource link=<https://api.example.com>")
            tag["link"].shouldBeInstanceOf<URL>()
        }
    }

    // ========================================================================
    // Nil/Null
    // ========================================================================

    context("nil literals") {
        test("nil keyword") {
            val tag = parser.parse("empty nil")
            tag.value shouldBe null
        }

        test("null keyword") {
            val tag = parser.parse("empty null")
            tag.value shouldBe null
        }

        test("nil as attribute value") {
            val tag = parser.parse("item value=nil")
            tag["value"] shouldBe null
        }
    }

    // ========================================================================
    // Lists
    // ========================================================================

    context("list literals") {
        test("empty list") {
            val tag = parser.parse("items []")
            tag.value.shouldBeInstanceOf<List<*>>()
            (tag.value as List<*>).shouldBeEmpty()
        }

        test("integer list with spaces") {
            val tag = parser.parse("nums [1 2 3 4]")
            tag.value shouldBe listOf(1, 2, 3, 4)
        }

        test("integer list with commas") {
            val tag = parser.parse("nums [1, 2, 3, 4]")
            tag.value shouldBe listOf(1, 2, 3, 4)
        }

        test("string list") {
            val tag = parser.parse("""names ["Alice" "Bob" "Charlie"]""")
            tag.value shouldBe listOf("Alice", "Bob", "Charlie")
        }

        test("mixed type list") {
            val tag = parser.parse("""mixed [1 "two" true 4.0]""")
            val list = tag.value as List<*>
            list[0] shouldBe 1
            list[1] shouldBe "two"
            list[2] shouldBe true
            list[3] shouldBe 4.0
        }

        test("nested list") {
            val tag = parser.parse("matrix [[1 2] [3 4]]")
            val outer = tag.value as List<*>
            outer shouldHaveSize 2
            outer[0] shouldBe listOf(1, 2)
            outer[1] shouldBe listOf(3, 4)
        }
    }

    // ========================================================================
    // Maps
    // ========================================================================

    context("map literals") {
        test("string key map") {
            val tag = parser.parse("""config [name="test" value=42]""")
            val map = tag.value as Map<*, *>
            map["name"] shouldBe "test"
            map["value"] shouldBe 42
        }

        test("map with naked string keys") {
            val tag = parser.parse("[Spanish=\"hola\" Fijian=\"Bula\"]")
            val map = tag.values[0] as Map<*, *>
            map["Spanish"] shouldBe "hola"
            map["Fijian"] shouldBe "Bula"
        }

        test("map with commas") {
            val tag = parser.parse("[a=1, b=2, c=3]")
            val map = tag.values[0] as Map<*, *>
            map["a"] shouldBe 1
            map["b"] shouldBe 2
            map["c"] shouldBe 3
        }
    }

    // ========================================================================
    // Line Continuation
    // ========================================================================

    context("line continuation") {
        test("backslash continues line") {
            val tag = parser.parse("""
                longTag "value1" "value2" \
                    "value3" "value4"
            """.trimIndent())

            tag.nsid.name shouldBe "longTag"
            tag.values shouldHaveSize 4
        }
    }

    // ========================================================================
    // Complex Real-World Examples
    // ========================================================================

    context("complex examples") {
        test("book catalog") {
            val tag = parser.parse("""
                @Personal
                favorite_books {
                    book "The Hobbit" author="J. R. R. Tolkien" published=1937
                    book "Dune" author="Frank Herbert" published=1965
                }
            """.trimIndent())

            tag.nsid.name shouldBe "favorite_books"
            tag.annotations shouldHaveSize 1
            tag.annotations[0].nsid.name shouldBe "Personal"
            tag.children shouldHaveSize 2

            val hobbit = tag.children[0]
            hobbit.value shouldBe "The Hobbit"
            hobbit["author"] shouldBe "J. R. R. Tolkien"
            hobbit["published"] shouldBe 1937
        }

        test("configuration file") {
            val tag = parser.parse("""
                config {
                    database host="localhost" port=5432 ssl=true
                    cache size=1000 ttl=3600L
                    logging level="debug" file=<file:///var/log/app.log>
                }
            """.trimIndent())

            tag.nsid.name shouldBe "config"
            tag.children shouldHaveSize 3

            val db = tag.getChild("database")!!
            db["host"] shouldBe "localhost"
            db["port"] shouldBe 5432
            db["ssl"] shouldBe true

            val cache = tag.getChild("cache")!!
            cache["ttl"] shouldBe 3600L

            val logging = tag.getChild("logging")!!
            (logging["file"] as URL).protocol shouldBe "file"
        }

        test("nested structure with all features") {
            val tag = parser.parse("""
                @Version("1.0")
                application name="MyApp" {
                    @Required
                    settings {
                        theme "dark"
                        font_size 14
                        features [undo redo search]
                    }

                    users {
                        user "admin" role="admin" active=true
                        user "guest" role="viewer" active=false
                    }
                }
            """.trimIndent())

            tag.annotations shouldHaveSize 1
            tag.annotations[0].value shouldBe "1.0"

            val settings = tag.getChild("settings")!!
            settings.annotations shouldHaveSize 1
            settings.annotations[0].nsid.name shouldBe "Required"

            val users = tag.getChild("users")!!
            users.children shouldHaveSize 2
        }
    }

    // ========================================================================
    // Error Cases
    // ========================================================================

    context("error handling") {
        test("unterminated string throws exception") {
            shouldThrow<ParseException> {
                parser.parse("""str "unclosed""")
            }
        }

        test("unterminated block string throws exception") {
            shouldThrow<ParseException> {
                parser.parse("""str ${"\"\"\""}unclosed""")
            }
        }

        test("invalid number throws exception") {
            shouldThrow<ParseException> {
                parser.parse("num 12.34.56.78")  // Too many components for Version
            }
        }

        test("unterminated list throws exception") {
            shouldThrow<ParseException> {
                parser.parse("items [1 2 3")
            }
        }

        test("unterminated URL throws exception") {
            shouldThrow<ParseException> {
                parser.parse("url <https://example.com")
            }
        }
    }

    // ========================================================================
    // Edge Cases
    // ========================================================================

    context("edge cases") {
        test("tag name 'true' is treated as boolean value") {
            // When a boolean appears where a tag name would be expected,
            // it becomes an anonymous tag with that value
            val tag = parser.parse("true")
            // This creates an anonymous tag with value true
            tag.isAnonymous() shouldBe true
            tag.value shouldBe true
        }

        test("very long string") {
            val longStr = "a".repeat(10000)
            val tag = parser.parse("""str "$longStr"""")
            (tag.value as String).length shouldBe 10000
        }

        test("deeply nested structure") {
            val input = buildString {
                repeat(20) { append("a { ") }
                append("leaf")
                repeat(20) { append(" }") }
            }
            val tag = parser.parse(input)
            // Should parse without stack overflow
            tag.nsid.name shouldBe "a"
        }

        test("many children") {
            val input = buildString {
                appendLine("parent {")
                repeat(1000) { appendLine("    child$it") }
                appendLine("}")
            }
            val tag = parser.parse(input)
            tag.children shouldHaveSize 1000
        }

        test("whitespace variations") {
            val tag = parser.parse("tag\t\t\"value\"\t\tattr=42")
            tag.value shouldBe "value"
            tag["attr"] shouldBe 42
        }
    }
})