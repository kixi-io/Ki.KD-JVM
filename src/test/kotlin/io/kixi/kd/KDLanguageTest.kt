package io.kixi.kd

import io.kixi.kd.tb.Seq
import io.kixi.kd.tb.withSampleBags
import io.kixi.lang.EmbeddedLanguageException
import io.kixi.lang.EmbeddedLanguages
import io.kixi.lang.EmbeddedSource
import io.kixi.uom.Quantity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * KD as an embedded language (`lang KD { ... }`): the block is read by KDParser as a
 * KD file is, with the host's values put in where `$x`, `${expr}` or `$(expr)` stand.
 */
class KDLanguageTest : FunSpec({

    /** What a host does: scan `{ body }`, evaluate each interpolation (here, from [values]), read the block. */
    fun block(body: String, values: Map<String, Any?> = emptyMap(), stringify: ((Any?) -> String)? = null): Any? {
        val source = "{$body}"
        val scanned = KDLanguage.scan(source, 1)
        scanned.end shouldBe source.length - 1
        for (x in scanned.interpolations) require(x.key in values) { "no value for ${x.key}" }
        val src = EmbeddedSource(source.substring(1, scanned.end))
        return if (stringify == null) KDLanguage.evaluate(src, values) else KDLanguage.evaluate(src, values, stringify)
    }

    fun tag(body: String, values: Map<String, Any?> = emptyMap()): Tag = block(body, values) as Tag

    test("a block reads exactly as KD: bare map keys and bare words are strings") {
        val t = tag("\n  map1 [fee = 23, fi = 26, foe = 12, fum = 16]\n")
        t.name shouldBe "map1"
        t.values.single() shouldBe mapOf("fee" to 23, "fi" to 26, "foe" to 12, "fum" to 16)
        tag("t status=active")["status"] shouldBe "active"
        tag("t 18°C..20°C")
        tag("plate p=\"PL-1\" {\n    well A1 0.5\n}").children.single().values shouldBe listOf("A1", 0.5)
    }

    test("interpolations put values in as they are, wherever a value can stand") {
        val list = listOf(1, 2)
        val tricky = "a=1 { b }"                                  // KD-looking text stays a string value
        val t = tag("@note(\$who)\nrun \$n operator=\$who items=\$list m=[\$k=\${v}] x=[\$n \$(n)] s=\$tricky",
            mapOf("who" to "dan", "n" to 3, "list" to list, "k" to "key", "v" to 2.5, "tricky" to tricky))
        t.values shouldBe listOf(3)
        t["operator"] shouldBe "dan"
        t["items"] shouldBe list
        t["m"] shouldBe mapOf("key" to 2.5)
        t["x"] shouldBe listOf(3, 3)
        t["s"] shouldBe tricky
        t.annotations.single().values shouldBe listOf("dan")
        tag("\$who 1", mapOf("who" to "x")).values shouldBe listOf("x", 1)    // a line starting with a value is an anonymous tag, as in KD
    }

    test("inside strings, an interpolation puts its text in; raw strings, chars and comments are left alone") {
        val v = mapOf("last" to "Smith", "n" to 3)
        tag("t a=\"Dr. \$last\" b=\"\${n} items\" c=\"\\\$5 or \$5\" d=@\"\$last\" e=`\$last`", v).let {
            it["a"] shouldBe "Dr. Smith"
            it["b"] shouldBe "3 items"
            it["c"] shouldBe "\$5 or \$5"
            it["d"] shouldBe "\$last"
            it["e"] shouldBe "\$last"
        }
        tag("t \"\"\"Hi \$last\"\"\"", v).values.single() shouldBe "Hi Smith"
        tag("t \"tab\\t\$last\\n\"", v).values.single() shouldBe "tab\tSmith\n"
        // How a value reads in a string is the host's choice.
        (block("t \"n=\$n\"", v) { x -> "<$x>" } as Tag).values.single() shouldBe "n=<3>"
        // Comments and chars hold no interpolations.
        KDLanguage.scan("{t 1 # \$x\n // \${y}\n /* \$z */ c='\$'}", 1).interpolations shouldBe emptyList()
    }

    test("a dollar before a digit is KD's currency prefix") {
        tag("t price=\$5.25")["price"].shouldBeInstanceOf<Quantity<*>>().toString() shouldBe "\$5.25"
        KDLanguage.scan("{t \$5 \$x}", 1).interpolations.map { it.key } shouldBe listOf("x")
    }

    test("the scan finds the block's end past nested braces, strings and grids") {
        val source = "{a { b \"}\" } .grid { 1 2 } t=\"{\"} after"
        KDLanguage.scan(source, 1).end shouldBe source.indexOf("} after")
        shouldThrow<EmbeddedLanguageException> { KDLanguage.scan("{a { b }", 1) }.reason shouldContain "Unterminated KD block"
        shouldThrow<EmbeddedLanguageException> { KDLanguage.scan("{a \${x", 1) }.reason shouldContain "Unterminated interpolation"
    }

    test("several tags make a root tag, none an empty one, as KD.read does") {
        tag("a 1\nb 2").let { it.name shouldBe "root"; it.children.map { c -> c.name } shouldBe listOf("a", "b") }
        tag("").let { it.name shouldBe "root"; it.children shouldBe emptyList() }
    }

    test("a KD error is reported at its position in the block") {
        val e = shouldThrow<EmbeddedLanguageException> { tag("a 1\nb x=") }
        e.offset shouldBe "a 1\nb x=".length
        e.reason shouldContain "Expected value"
    }

    test("type bag literals read through their bags") {
        withSampleBags { tag("gene .seq(\"ACGT\") probe=.sq(\"GA\")").let { it.values shouldBe listOf(Seq("ACGT")); it["probe"] shouldBe Seq("GA") } }
    }

    test("a naked URL is one value: its // and # are not comments, and the line goes on after it") {
        tag("module http://kixi.io/w#top").values.single().toString() shouldBe "http://kixi.io/w#top"
        tag("module http://kixi.io/w version=\$v", mapOf("v" to 2))["version"] shouldBe 2
        KDLanguage.scan("{m http://x.io/\$y}", 1).let { it.end shouldBe 17; it.interpolations shouldBe emptyList() }
    }

    test("a dollar inside a word is part of the word, as in KD") {
        KDLanguage.scan("{t status=active\$x foo\$bar=1}", 1).interpolations shouldBe emptyList()
        tag("t status=active\$x")["status"] shouldBe "active\$x"
        tag("t \$a\$b \${a}b", mapOf("a" to 1, "b" to 2)).values shouldBe listOf(1, 2, 1, "b")
    }

    test("a value from the host can be a range's bound, on either side") {
        val v = mapOf("a" to 1, "b" to 5, "n" to null)
        tag("t \$a..\$b \$a..<9 1..\$b _..\$b \$a.._", v).values.map { it.toString() } shouldBe listOf("1..5", "1..<9", "1..5", "_..5", "1.._")
        shouldThrow<EmbeddedLanguageException> { tag("t 1..\$n", v) }.reason shouldContain "cannot be nil"
        shouldThrow<EmbeddedLanguageException> { tag("t \$n..5", v) }.reason shouldContain "cannot be nil"
    }

    test("nil from the host stands where a value can") {
        tag("t \$n a=\$n [\$n 1]", mapOf("n" to null)).let {
            it.values shouldBe listOf(null, listOf(null, 1)); it["a"] shouldBe null; it.hasAttribute("a") shouldBe true
        }
    }

    test("inside a dot literal the value's text goes in, for the literal to read") {
        tag("t g=.geo(\$lat, \$lng)", mapOf("lat" to 1.5, "lng" to 2.5))["g"].toString() shouldBe KD.read("t g=.geo(1.5, 2.5)")["g"].toString()
        withSampleBags { tag("t .seq(\$s) .sq(\"\${s}T\")", mapOf("s" to "ACG")).values shouldBe listOf(Seq("ACG"), Seq("ACGT")) }
    }

    test("comments nest and triple backticks hold single ones, as in KD") {
        tag("t 1 /* a /* b */ c } */ u=2")["u"] shouldBe 2
        tag("t ```a ` b```").values.single() shouldBe "a ` b"
    }

    test("block strings keep KD's meaning: text as written, the first and last newline dropped") {
        tag("t \"\"\"\n    one\\n\n    two \$x\n\"\"\"", mapOf("x" to 3)).values.single() shouldBe "    one\\n\n    two 3"
    }

    test("errors carry their position: a bad escape, an interpolated attribute name, a range of mixed types") {
        shouldThrow<EmbeddedLanguageException> { tag("a 1\nb \"x\\b\"") }.let { it.offset shouldBe 6; it.reason shouldContain "'\\b'" }
        shouldThrow<EmbeddedLanguageException> { tag("t \$k=1", mapOf("k" to "a")) }.reason shouldContain "name cannot be interpolated"
        shouldThrow<EmbeddedLanguageException> { tag("t 1..\$s", mapOf("s" to "z")) }.let { it.reason shouldContain "compatible types"; it.offset shouldBe 7 }
        // Read by KDParser (a host's scan would stop at it first), at the string's start.
        shouldThrow<EmbeddedLanguageException> { KDLanguage.evaluate(EmbeddedSource("t \"open"), emptyMap()) }
            .let { it.offset shouldBe 2; it.reason shouldBe "Unterminated string" }
    }

    test("any host finds KD by name") {
        val kd = EmbeddedLanguages.find("kd")!!
        kd.name shouldBe "KD"
        (kd.evaluate(EmbeddedSource("t a=\$x"), mapOf("x" to 7)) as Tag)["a"] shouldBe 7
    }
})
