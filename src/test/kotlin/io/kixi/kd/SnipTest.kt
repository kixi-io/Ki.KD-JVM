package io.kixi.kd

import io.kixi.text.ParseException
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.assertions.throwables.shouldThrow
import java.nio.file.Path

/**
 * Comprehensive tests for the Snip class covering:
 * - Literal parsing (quoted and unquoted paths)
 * - Path normalization and extension handling
 * - URL detection and security flags
 * - Expand parameter parsing
 * - Error handling
 * - toString formatting
 */
class SnipTest : FunSpec({

    // ========================================================================
    // Basic Literal Parsing
    // ========================================================================

    context("basic literal parsing") {
        test("simple unquoted path") {
            val snip = Snip.parse(".snip(path/to/file)")
            snip.path shouldBe "path/to/file"
            snip.expand shouldBe false
        }

        test("path with parent directory reference") {
            val snip = Snip.parse(".snip(../components/button)")
            snip.path shouldBe "../components/button"
        }

        test("simple filename without directory") {
            val snip = Snip.parse(".snip(config)")
            snip.path shouldBe "config"
        }

        test("path with explicit .kd extension") {
            val snip = Snip.parse(".snip(config.kd)")
            snip.path shouldBe "config.kd"
        }

        test("path with different extension") {
            val snip = Snip.parse(".snip(data.yaml)")
            snip.path shouldBe "data.yaml"
        }

        test("deeply nested path") {
            val snip = Snip.parse(".snip(a/b/c/d/e/f)")
            snip.path shouldBe "a/b/c/d/e/f"
        }

        test("path with underscores and hyphens") {
            val snip = Snip.parse(".snip(my-component_v2/widget)")
            snip.path shouldBe "my-component_v2/widget"
        }
    }

    // ========================================================================
    // Quoted Path Parsing
    // ========================================================================

    context("quoted path parsing") {
        test("quoted path with spaces") {
            val snip = Snip.parse(".snip(\"path with spaces/file name\")")
            snip.path shouldBe "path with spaces/file name"
        }

        test("quoted path with parentheses") {
            val snip = Snip.parse(".snip(\"path(with)/parens\")")
            snip.path shouldBe "path(with)/parens"
        }

        test("quoted simple path (unnecessary but valid)") {
            val snip = Snip.parse(".snip(\"simple\")")
            snip.path shouldBe "simple"
        }

        test("quoted path with special characters") {
            val snip = Snip.parse(".snip(\"file@name#special\")")
            snip.path shouldBe "file@name#special"
        }
    }

    // ========================================================================
    // Path Normalization
    // ========================================================================

    context("path normalization") {
        test("auto-append .kd when no extension") {
            val snip = Snip.parse(".snip(config)")
            snip.normalizedPath shouldBe "config.kd"
        }

        test("no auto-append when .kd present") {
            val snip = Snip.parse(".snip(config.kd)")
            snip.normalizedPath shouldBe "config.kd"
        }

        test("no auto-append when other extension present") {
            val snip = Snip.parse(".snip(data.yaml)")
            snip.normalizedPath shouldBe "data.yaml"
        }

        test("no auto-append for dotfile") {
            val snip = Snip.parse(".snip(.hidden)")
            snip.normalizedPath shouldBe ".hidden.kd"
        }

        test("extension detection with path") {
            val snip = Snip.parse(".snip(path/to/file)")
            snip.normalizedPath shouldBe "path/to/file.kd"
        }

        test("extension detection with path having extension") {
            val snip = Snip.parse(".snip(path/to/file.json)")
            snip.normalizedPath shouldBe "path/to/file.json"
        }
    }

    // ========================================================================
    // URL Detection
    // ========================================================================

    context("URL detection") {
        test("https URL is detected") {
            val snip = Snip.parse(".snip(https://example.com/component)")
            snip.isUrl shouldBe true
            snip.isSecure shouldBe true
        }

        test("http URL is detected") {
            val snip = Snip.parse(".snip(http://example.com/component)")
            snip.isUrl shouldBe true
            snip.isSecure shouldBe false
        }

        test("file URL is detected") {
            val snip = Snip.parse(".snip(file:///path/to/file)")
            snip.isUrl shouldBe true
            snip.isSecure shouldBe true
        }

        test("regular path is not URL") {
            val snip = Snip.parse(".snip(path/to/file)")
            snip.isUrl shouldBe false
            snip.isSecure shouldBe true  // Non-URLs are considered secure
        }

        test("path starting with http is not URL without colon-slash") {
            val snip = Snip.parse(".snip(httpfiles/data)")
            snip.isUrl shouldBe false
        }
    }

    // ========================================================================
    // Absolute Path Detection
    // ========================================================================

    context("absolute path detection") {
        test("Unix absolute path") {
            val snip = Snip.parse(".snip(/etc/config)")
            snip.isAbsolutePath shouldBe true
        }

        test("Windows absolute path with C drive") {
            val snip = Snip(".snip(C:/Users/config)")
            Snip("C:/Users/config").isAbsolutePath shouldBe true
        }

        test("relative path") {
            val snip = Snip.parse(".snip(path/to/file)")
            snip.isAbsolutePath shouldBe false
        }

        test("parent relative path") {
            val snip = Snip.parse(".snip(../sibling/file)")
            snip.isAbsolutePath shouldBe false
        }

        test("URL is not absolute path") {
            val snip = Snip.parse(".snip(https://example.com/file)")
            snip.isAbsolutePath shouldBe false
        }
    }

    // ========================================================================
    // Expand Parameter
    // ========================================================================

    context("expand parameter") {
        test("expand=true with unquoted path") {
            val snip = Snip.parse(".snip(path/to/file, expand=true)")
            snip.path shouldBe "path/to/file"
            snip.expand shouldBe true
        }

        test("expand=false with unquoted path") {
            val snip = Snip.parse(".snip(path/to/file, expand=false)")
            snip.path shouldBe "path/to/file"
            snip.expand shouldBe false
        }

        test("expand=true with quoted path") {
            val snip = Snip.parse(".snip(\"path with spaces\", expand=true)")
            snip.path shouldBe "path with spaces"
            snip.expand shouldBe true
        }

        test("expand with extra whitespace") {
            val snip = Snip.parse(".snip(path,   expand = true  )")
            snip.path shouldBe "path"
            snip.expand shouldBe true
        }

        test("expand case insensitive") {
            val snip = Snip.parse(".snip(path, expand=TRUE)")
            snip.expand shouldBe true
        }

        test("no expand parameter defaults to false") {
            val snip = Snip.parse(".snip(path)")
            snip.expand shouldBe false
        }
    }

    // ========================================================================
    // Path Resolution
    // ========================================================================

    context("path resolution") {
        test("resolve simple path") {
            val snip = Snip("components/button")
            val basePath = Path.of("/app/src")
            val resolved = snip.resolve(basePath)
            resolved.toString() shouldBe "/app/src/components/button.kd"
        }

        test("resolve parent path") {
            val snip = Snip("../shared/utils")
            val basePath = Path.of("/app/src/components")
            val resolved = snip.resolve(basePath)
            resolved.toString() shouldBe "/app/src/shared/utils.kd"
        }

        test("resolve with existing extension") {
            val snip = Snip("config.yaml")
            val basePath = Path.of("/app")
            val resolved = snip.resolve(basePath)
            resolved.toString() shouldBe "/app/config.yaml"
        }

        test("resolve throws for URL snip") {
            val snip = Snip("https://example.com/file")
            val basePath = Path.of("/app")
            shouldThrow<IllegalStateException> {
                snip.resolve(basePath)
            }
        }
    }

    // ========================================================================
    // toString Formatting
    // ========================================================================

    context("toString formatting") {
        test("simple path") {
            val snip = Snip("path/to/file")
            snip.toString() shouldBe ".snip(path/to/file)"
        }

        test("path with expand") {
            val snip = Snip("path/to/file", expand = true)
            snip.toString() shouldBe ".snip(path/to/file, expand=true)"
        }

        test("path needing quotes") {
            val snip = Snip("path with spaces")
            snip.toString() shouldBe ".snip(\"path with spaces\")"
        }

        test("path with expand needing quotes") {
            val snip = Snip("path with spaces", expand = true)
            snip.toString() shouldBe ".snip(\"path with spaces\", expand=true)"
        }

        test("URL path") {
            val snip = Snip("https://example.com/file")
            snip.toString() shouldBe ".snip(https://example.com/file)"
        }
    }

    // ========================================================================
    // Error Handling
    // ========================================================================

    context("error handling") {
        test("empty snip throws") {
            shouldThrow<ParseException> {
                Snip.parse(".snip()")
            }
        }

        test("missing closing paren throws") {
            shouldThrow<ParseException> {
                Snip.parse(".snip(path")
            }
        }

        test("missing opening throws") {
            shouldThrow<ParseException> {
                Snip.parse("snip(path)")
            }
        }

        test("invalid unquoted characters throws") {
            shouldThrow<ParseException> {
                Snip.parse(".snip(path with spaces)")
            }
        }

        test("unterminated quote throws") {
            shouldThrow<ParseException> {
                Snip.parse(".snip(\"unclosed)")
            }
        }

        test("invalid parameter throws") {
            shouldThrow<ParseException> {
                Snip.parse(".snip(path, invalid=xyz)")
            }
        }

        test("only whitespace in path throws") {
            shouldThrow<ParseException> {
                Snip.parse(".snip(   )")
            }
        }
    }

    // ========================================================================
    // isLiteral Check
    // ========================================================================

    context("isLiteral check") {
        test("valid snip literal") {
            Snip.isLiteral(".snip(path)") shouldBe true
        }

        test("snip with spaces") {
            Snip.isLiteral("  .snip(path)  ") shouldBe true
        }

        test("not a snip - missing dot") {
            Snip.isLiteral("snip(path)") shouldBe false
        }

        test("not a snip - different function") {
            Snip.isLiteral(".blob(data)") shouldBe false
        }

        test("not a snip - missing paren") {
            Snip.isLiteral(".snip path") shouldBe false
        }

        test("empty string") {
            Snip.isLiteral("") shouldBe false
        }
    }

    // ========================================================================
    // parseOrNull
    // ========================================================================

    context("parseOrNull") {
        test("valid literal returns Snip") {
            val snip = Snip.parseOrNull(".snip(path)")
            snip.shouldNotBeNull()
            snip.path shouldBe "path"
        }

        test("invalid literal returns null") {
            val snip = Snip.parseOrNull(".snip()")
            snip.shouldBeNull()
        }

        test("non-snip literal returns null") {
            val snip = Snip.parseOrNull(".blob(data)")
            snip.shouldBeNull()
        }
    }

    // ========================================================================
    // Data Class Equality
    // ========================================================================

    context("equality") {
        test("equal snips") {
            val snip1 = Snip("path/to/file", expand = true)
            val snip2 = Snip("path/to/file", expand = true)
            snip1 shouldBe snip2
        }

        test("different paths not equal") {
            val snip1 = Snip("path/a")
            val snip2 = Snip("path/b")
            snip1 shouldNotBe snip2
        }

        test("different expand not equal") {
            val snip1 = Snip("path", expand = true)
            val snip2 = Snip("path", expand = false)
            snip1 shouldNotBe snip2
        }

        test("hashCode consistent with equals") {
            val snip1 = Snip("path", expand = true)
            val snip2 = Snip("path", expand = true)
            snip1.hashCode() shouldBe snip2.hashCode()
        }
    }

    // ========================================================================
    // Edge Cases
    // ========================================================================

    context("edge cases") {
        test("path with multiple dots") {
            val snip = Snip.parse(".snip(file.name.with.dots)")
            snip.path shouldBe "file.name.with.dots"
            snip.normalizedPath shouldBe "file.name.with.dots"  // Has extension
        }

        test("very long path") {
            val longPath = "a/" + "b/".repeat(100) + "file"
            val snip = Snip.parse(".snip($longPath)")
            snip.path shouldBe longPath
        }

        test("path with numbers") {
            val snip = Snip.parse(".snip(v1/2023/file123)")
            snip.path shouldBe "v1/2023/file123"
        }

        test("single character path") {
            val snip = Snip.parse(".snip(x)")
            snip.path shouldBe "x"
        }

        test("quoted empty content after path") {
            val snip = Snip.parse(".snip(\"path\")")
            snip.path shouldBe "path"
            snip.expand shouldBe false
        }
    }
})