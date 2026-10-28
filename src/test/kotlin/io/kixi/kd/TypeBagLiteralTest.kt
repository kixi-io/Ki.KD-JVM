package io.kixi.kd

import io.kixi.Ki
import io.kixi.kd.schema.KDS
import io.kixi.kd.schema.KDSchemaException
import io.kixi.kd.tb.Seq
import io.kixi.kd.tb.Shape
import io.kixi.kd.tb.withSampleBags
import io.kixi.kd.Tag
import io.kixi.types.ExtLiteral
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * Dot literals of type bags (Ki type bags design, 3.1): read and written through the
 * bag when it is loaded, kept as written (ExtLiteral) when it is not.
 */
class TypeBagLiteralTest : FunSpec({

    // Written as KD writes it (values before attributes), so the text can be compared after a round trip.
    val doc = "@note(s=.seq(\"AC\"))\nsample .sq(\"GG\") probe=.seq(\"ACGT\") shape=.shape(circle) {\n    part s=.seq(\"T\")\n}"

    test("with the bag loaded, literals read as the bag's values and write back as literals") {
        withSampleBags {
            val t = KD.read(doc)
            t["probe"] shouldBe Seq("ACGT")
            t["shape"] shouldBe Shape("circle")
            t.values shouldBe listOf(Seq("GG"))
            t.children.single()["s"] shouldBe Seq("T")
            t.annotations.single().getAttribute<Any?>("s") shouldBe Seq("AC")
            // Written back with the first literal name, so the alias .sq becomes .seq.
            t.toString().replace("\r\n", "\n") shouldBe doc.replace(".sq(", ".seq(")
            KD.read(t.toString()) shouldBe t
            KD.read("t xs=[.seq(\"A\") .seq(\"C\")] m=[k=.shape(square)]")["xs"] shouldBe listOf(Seq("A"), Seq("C"))
        }
    }

    test("without the bag, the same document reads as ExtLiterals and writes back unchanged") {
        val t = KD.read(doc)
        t["probe"] shouldBe ExtLiteral(".seq(\"ACGT\")")
        t["probe"].shouldBeInstanceOf<ExtLiteral>().name shouldBe "seq"
        t.children.single()["s"] shouldBe ExtLiteral(".seq(\"T\")")
        t.toString().replace("\r\n", "\n") shouldBe doc
        KD.read(t.toString()) shouldBe t
        // Read without the bag, then with it: the text is the same, the values are not.
        withSampleBags { KD.read(t.toString())["probe"] shouldBe Seq("ACGT") }
    }

    test("an unknown literal's text is kept as written, inner spaces and nesting included") {
        val t = KD.read("t a=.foo( 1, \"x)\" , (2) ) b=.bar(\"\")")
        t["a"] shouldBe ExtLiteral(".foo( 1, \"x)\" , (2) )")
        t["b"] shouldBe ExtLiteral(".bar(\"\")")
        Ki.format(t["a"]) shouldBe ".foo( 1, \"x)\" , (2) )"
    }

    test("a literal the bag refuses is an error at the literal; type parameters are grid-only") {
        withSampleBags {
            shouldThrow<KDParseException> { KD.read("t s=.seq(\"AXG\")") }.message shouldContain "Invalid .seq literal: ParseException \"Not a Seq: AXG\""
            shouldThrow<KDParseException> { KD.read("t a=1\nt s=.seq(\n\"AXG\")") }.message shouldContain "line: 2 index: 5"
            shouldThrow<KDParseException> { KD.read("t s=.seq<Int>(\"A\")") }      // only .grid takes a type parameter
        }
        shouldThrow<KDParseException> { KD.read("t s=.foo<Int>(1)") }
        shouldThrow<KDParseException> { KD.read("t s=.foo(1") }
    }

    test("literal names ignore case, as .ID( does; the bag writes its own spelling") {
        withSampleBags {
            KD.read("t s=.SEQ(\"A\") q=.Sq(\"C\")").let { it["s"] shouldBe Seq("A"); it["q"] shouldBe Seq("C"); it.toString() shouldBe "t s=.seq(\"A\") q=.seq(\"C\")" }
        }
    }

    test("a grid of bag values or kept literals is untyped, so it writes back and reads again") {
        val text = ".grid { .seq(\"A\") .seq(\"C\") }"
        withSampleBags {
            val g = KD.read("t g=$text")["g"] as io.kixi.Grid<*>
            g.toList() shouldBe listOf(Seq("A"), Seq("C"))
            val again = KD.read(KD.read("t g=$text").toString())["g"] as io.kixi.Grid<*>
            again.toList() shouldBe listOf(Seq("A"), Seq("C"))
            g.toString() shouldContain ".grid<Any> {"
        }
        val kept = KD.read("t g=$text")
        KD.read(kept.toString()) shouldBe kept
        (kept["g"] as io.kixi.Grid<*>).toList() shouldBe listOf(ExtLiteral(".seq(\"A\")"), ExtLiteral(".seq(\"C\")"))
    }

    test("dot literals can be map keys") {
        withSampleBags { (KD.read("t m=[.seq(\"A\")=1 .seq(\"C\")=2]")["m"] as Map<*, *>).keys shouldBe setOf(Seq("A"), Seq("C")) }
        (KD.read("t m=[.foo(1)=x]")["m"] as Map<*, *>).keys.single() shouldBe ExtLiteral(".foo(1)")
    }

    test("a kept literal read with a capital letter is read by the bag later") {
        val t = KD.read("t s=.Seq(\"A\")")
        t["s"] shouldBe ExtLiteral(".Seq(\"A\")")
        withSampleBags { KD.read(t.toString())["s"] shouldBe Seq("A") }
    }

    test("positional annotation values and values in lists hold bag literals") {
        withSampleBags {
            val t = KD.read("@mark(.seq(\"A\") 2)\nt [.seq(\"C\") .shape(square)]")
            t.annotations.single().values shouldBe listOf(Seq("A"), 2)
            t.values.single() shouldBe listOf(Seq("C"), Shape("square"))
        }
    }

    test("built-in literals are not affected") {
        KD.read("t i=.id(01J9A0X5K2M7N3P8Q1R6T4V9AB)")["i"].shouldBeInstanceOf<io.kixi.Id>()
        KD.read("t g=.geo(1.0, 2.0)")["g"].shouldBeInstanceOf<io.kixi.GeoPoint>()
    }

    context("KDS") {

        val schema = """
            schema "s" kds=1 {
                tag sample {
                    attribute probe type=Seq
                    attribute any type=Any nullable=true
                }
            }
        """.trimIndent()

        test("a bag's type name validates the bag's values") {
            withSampleBags {
                val s = KDS.compile(schema)
                s.validate("sample probe=.seq(\"AC\") any=.shape(circle)").isValid.shouldBeTrue()
                val r = s.validate("sample probe=\"AC\"")
                r.isValid.shouldBeFalse()
                r.issues.single().message shouldContain "Expected Seq"
            }
        }

        test("without the bag, a strict compile refuses the name and a lenient one accepts only kept literals") {
            shouldThrow<KDSchemaException> { KDS.compile(schema) }.message shouldContain "Unknown type Seq"
            val s = KDS.compile(schema, allowUnloadedTypes = true)
            s.validate("sample probe=.seq(\"AC\") any=.foo(1)").isValid.shouldBeTrue()
            s.validate("sample probe=\"AC\"").isValid.shouldBeFalse()
            // Lenient is for names a bag could supply; other mistakes stay errors.
            shouldThrow<KDSchemaException> {
                KDS.compile("schema \"s\" kds=1 {\n tag t {\n attribute a type=lower\n }\n}", allowUnloadedTypes = true)
            }.message shouldContain "Unknown type lower"
            shouldThrow<KDSchemaException> {
                KDS.compile("schema \"s\" kds=1 {\n tag t {\n attribute a type=Ext\n }\n}", allowUnloadedTypes = true)
            }.message shouldContain "Unknown type Ext"
        }

        test("an enum of literals kept without the bag matches any kept literal with the same text") {
            val s = KDS.compile("schema \"s\" kds=1 {\n tag t {\n attribute p type=Seq enum=[.seq(\"A\") .seq(\"C\")]\n }\n}", allowUnloadedTypes = true)
            s.validate("t p=.seq(\"C\")").isValid.shouldBeTrue()
            s.validate("t p=.seq(\"G\")").isValid.shouldBeFalse()
            // A store's own subclass (RevDB's ExtValue) with the same text is the same value.
            val stored = object : ExtLiteral(".seq(\"A\")") {}
            s.validate(Tag("t").apply { setAttribute("p", value = stored) }).isValid.shouldBeTrue()
        }

        test("a loaded type also accepts a kept literal of its own name (a value a newer bag version wrote)") {
            withSampleBags {
                val s = KDS.compile(schema, allowUnloadedTypes = true)
                s.validate(Tag("sample").apply { setAttribute("probe", value = object : ExtLiteral(".seq(\"A\")") {}) }).isValid.shouldBeTrue()
                s.validate(Tag("sample").apply { setAttribute("probe", value = ExtLiteral(".shape(circle)")) }).isValid.shouldBeFalse()
                s.validate("sample probe=.shape(circle)").issues.single().message shouldContain "Expected Seq; found Shape"
            }
            KDS.compile(schema, allowUnloadedTypes = true).validate("sample probe=7").issues.single().message shouldContain "found Integer"
        }

        test("constraints, nullability and items on bag types") {
            withSampleBags {
                fun refuses(attr: String, what: String) =
                    shouldThrow<KDSchemaException> { KDS.compile("schema \"s\" kds=1 {\n tag t {\n attribute a $attr\n }\n}") }.message shouldContain what
                refuses("type=Seq min=1", "Bounds require a numeric type or Quantity")
                refuses("type=Seq pattern=\"A\"", "String constraints require type=String")
                refuses("type=Seq minCount=1", "Count constraints require List, Map, Grid, or Vector")
                val s = KDS.compile("schema \"s\" kds=1 {\n tag t {\n attribute a type=Seq nullable=true\n attribute l type=List {\n items type=Seq\n }\n }\n}")
                s.validate("t a=nil l=[.seq(\"A\") .seq(\"C\")]").isValid.shouldBeTrue()
                s.validate("t l=[.seq(\"A\") \"C\"]").isValid.shouldBeFalse()
            }
        }

        test("Any accepts a bag value KD can write, and refuses one it cannot") {
            withSampleBags {
                val s = KDS.compile(schema)
                s.validate(Tag("sample").apply { setAttribute("any", value = io.kixi.kd.tb.PairImpl(1, 2)) }).isValid.shouldBeFalse()
                s.validate(Tag("sample").apply { setAttribute("any", value = Seq("A")) }).isValid.shouldBeTrue()
            }
        }

        test("a document's own schema naming a bag type needs the bag (KD.load is strict)") {
            val text = "schema \"s\" kds=1 {\n tag t {\n attribute a type=Seq\n }\n}\nt a=.seq(\"A\")"
            shouldThrow<KDSchemaException> { KD.load(text) }.message shouldContain "Unknown type Seq"
            withSampleBags { KD.load(text).isValid.shouldBeTrue() }
        }

        test("Ext is not a schema type name") {
            shouldThrow<KDSchemaException> { KDS.compile("schema \"s\" kds=1 {\n tag t {\n attribute a type=Ext\n }\n}") }
        }
    }
})
