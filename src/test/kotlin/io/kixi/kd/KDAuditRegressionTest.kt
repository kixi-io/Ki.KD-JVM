package io.kixi.kd

import io.kixi.Call
import io.kixi.Grid
import io.kixi.Ki
import io.kixi.NSID
import io.kixi.text.ParseException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.nio.file.Files
import java.nio.file.Path
import java.net.InetSocketAddress
import com.sun.net.httpserver.HttpServer

private fun withDocuments(block: (Path) -> Unit) {
    val dir = Files.createTempDirectory("kd-audit-")
    try { block(dir) } finally {
        Files.walk(dir).use { paths ->
            paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
        }
    }
}

class KDAuditRegressionTest : StringSpec({
    "nil and null preserve positional values" {
        KD.read("sample 1 nil 2 null 3").values shouldBe listOf(1, null, 2, null, 3)
    }
    "nil survives annotation and call parameters" {
        KD.read("@meta(nil, 2) sample").annotations.single().values shouldBe listOf(null, 2)
        (KD.read("sample fn(nil, 2)").value as Call).values shouldBe listOf(null, 2)
    }
    "explicit nil attribute differs from missing value" {
        val tag = KD.read("sample x=nil")
        tag.attributes.containsKey(NSID("x")) shouldBe true
        tag.attributes[NSID("x")] shouldBe null
    }
    "nil remains valid in lists and maps" {
        KD.read("sample [nil, 2]").value shouldBe listOf(null, 2)
        KD.read("sample [x=nil]").value shouldBe mapOf("x" to null)
    }
    for ((index, input) in listOf(
        "sample x=", "sample x=}", "sample fn(x=)", "@meta(x=) sample",
        "sample {", "sample { child", "/* missing", "/* outer /* inner */",
        "@meta(", "@meta(1", "@meta", "sample [1", "sample [x=1",
        "sample fn(1", "sample .grid(1 !)", "sample .grid(1 :)",
        "sample .grid(1 .unknown)"
    ).withIndex()) {
        "malformed input $index is rejected" {
            shouldThrow<ParseException> { KD.read(input) }
        }
    }
    "closed structures and nested comments remain valid" {
        KD.read("/* outer /* inner */ */ sample { child }").children.single().nsid.name shouldBe "child"
        KD.read("@meta(nil) sample {}").annotations.single().values shouldBe listOf(null)
    }
    for (comment in listOf("# comment", "// comment")) {
        "line comment $comment preserves next tag" {
            KD.read("first 1 $comment\nsecond 2").children.map { it.nsid.name } shouldBe listOf("first", "second")
        }
    }
    "named searches agree with predicate searches at every depth" {
        val root = KD.read("root { hit 1; branch { middle { hit 2; ns:hit 3 } } }")
        root.findChild("hit") shouldBe root.getChild("hit")
        root.findChildren("hit").map { it.value } shouldBe listOf(1, 2)
        root.findChild("hit", "ns")?.value shouldBe 3
        root.findChildren("hit", "ns").map { it.value } shouldBe listOf(3)
        root.findChild("absent") shouldBe null
        root.findChildren("hit") shouldBe root.findChildren { it.nsid == NSID("hit") }
    }
    "root snips and snip-only included documents resolve recursively" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("outer.kd"), ".snip(inner)")
            Files.writeString(dir.resolve("inner.kd"), "leaf 42")
            KD.parseWithSnips(".snip(outer)", dir).nsid.name shouldBe "leaf"
            KD.parseWithSnips(".snip(outer)", dir).value shouldBe 42
        }
    }
    "relative nested snips use their containing directory" {
        withDocuments { dir ->
            Files.createDirectories(dir.resolve("parts"))
            Files.writeString(dir.resolve("parts/outer.kd"), "outer { .snip(inner) }")
            Files.writeString(dir.resolve("parts/inner.kd"), "leaf 42")
            KD.parseWithSnips(".snip(parts/outer)", dir).getChild("leaf")?.value shouldBe 42
        }
    }
    "expand resolves nested directives before extracting children" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("outer.kd"), "outer { .snip(inner) }")
            Files.writeString(dir.resolve("inner.kd"), "leaf 42")
            KD.parseWithSnips("root { .snip(outer, expand=true) }", dir)
                .getChild("leaf")?.value shouldBe 42
        }
    }
    "root expansion supports multiple and zero children" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("many.kd"), "outer { a; b }")
            Files.writeString(dir.resolve("empty.kd"), "outer")
            KD.parseWithSnips(".snip(many, expand=true)", dir).children.map { it.nsid.name } shouldBe listOf("a", "b")
            KD.parseWithSnips(".snip(empty, expand=true)", dir).children.size shouldBe 0
        }
    }
    "cached includes do not share mutable values or child trees" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("part.kd"), "part [1 2] { leaf 3 }")
            val root = KD.parseWithSnips("root { .snip(part); .snip(part) }", dir)
            val first = root.children[0]
            val second = root.children[1]
            (first === second) shouldBe false
            (first.value === second.value) shouldBe false
            first.children.single().values[0] = 99
            second.children.single().value shouldBe 3
        }
    }
    "reused resolver resets between documents" {
        withDocuments { dir ->
            val file = dir.resolve("part.kd")
            val resolver = SnipResolver()
            Files.writeString(file, "part 1")
            KD.parseWithSnips(".snip(part)", dir, resolver).value shouldBe 1
            Files.writeString(file, "part 2")
            KD.parseWithSnips(".snip(part)", dir, resolver).value shouldBe 2
        }
    }
    "quoted snips retain legacy resolution behavior" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("header.kd"), "header 42")
            val text = "root { \".snip(header)\" }"
            KD.read(text).children.single().value shouldBe ".snip(header)"
            KD.parseWithSnips(text, dir).children.single().nsid.name shouldBe "header"
            KD.parseWithSnips("\".snip(header)\"", dir).value shouldBe 42
        }
    }
    "quoted nested snips resolve and cached inclusions remain independent" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("part.kd"), "part { \".snip(header)\" }")
            Files.writeString(dir.resolve("header.kd"), "header 42")
            val root = KD.parseWithSnips("root { \".snip(part)\"; \".snip(part)\" }", dir)
            root.children.map { it.children.single().nsid.name } shouldBe listOf("header", "header")
            root.children[0].children.single().values[0] = 99
            root.children[1].children.single().value shouldBe 42
        }
    }
    "circular root includes throw and unwind the resolver stack" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("a.kd"), ".snip(b)")
            Files.writeString(dir.resolve("b.kd"), ".snip(a)")
            val resolver = SnipResolver()
            shouldThrow<SnipCircularReferenceException> { KD.readWithSnips(dir.resolve("a.kd"), resolver) }
            resolver.getResolutionStack() shouldBe emptyList()
        }
    }
    "depth limit counts include edges rather than ordinary tags" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("a.kd"), "a { b { .snip(c) } }")
            Files.writeString(dir.resolve("c.kd"), "c")
            shouldThrow<SnipDepthExceededException> {
                KD.parseWithSnips(".snip(a)", dir, SnipResolver(SnipOptions(maxSnipDepth = 1)))
            }
        }
    }
    "snipped parse errors retain a parse exception cause" {
        withDocuments { dir ->
            Files.writeString(dir.resolve("bad.kd"), "bad {")
            shouldThrow<SnipParseException> { KD.parseWithSnips(".snip(bad)", dir) }
        }
    }
    "timeout cannot overflow the HTTP timeout integer" {
        shouldThrow<IllegalArgumentException> { SnipOptions(urlTimeoutMs = Int.MAX_VALUE.toLong() + 1) }
    }
    "file URLs fail explicitly instead of a connection cast error" {
        withDocuments { dir ->
            shouldThrow<SnipSecurityException> {
                SnipResolver(SnipOptions.PERMISSIVE).resolve(Snip("file:///missing.kd"), dir, emptyList(), 0, 1, 1)
            }
        }
    }
    "file reading preserves UTF-8 text" {
        withDocuments { dir ->
            val file = dir.resolve("unicode.kd")
            Files.writeString(file, "greeting \"こんにちは\"")
            KD.read(file.toFile()).value shouldBe "こんにちは"
        }
    }
    "Core grid formatting can be parsed by KD" {
        val original = Grid.fromRows(listOf(listOf(1, 2), listOf(3, 4)))
        val parsed = KD.read("sample " + Ki.format(original)).value as Grid<*>
        parsed.width shouldBe 2
        parsed.height shouldBe 2
        parsed.data.toList() shouldBe listOf(1, 2, 3, 4)
    }
    "empty grids retain the original rejection behavior" {
        for (literal in listOf(".grid()", ".grid(   )", ".grid<Int>()", ".grid {}", ".grid { # comment\n }")) {
            shouldThrow<ParseException> { KD.read("sample $literal") }
        }
    }
    "grid row boundaries ignore nested value newlines" {
        val parsed = KD.read("sample .grid([1,\n2] 3; [4,\n5] 6)").value as Grid<*>
        parsed.width shouldBe 2
        parsed.height shouldBe 2
        parsed.data.toList() shouldBe listOf(listOf(1, 2), 3, listOf(4, 5), 6)
    }
    "grid comments and nil placeholders preserve rows" {
        val parsed = KD.read("sample .grid { 1 - # end\n 2 nil }").value as Grid<*>
        parsed.data.toList() shouldBe listOf(1, null, 2, null)
        parsed.width shouldBe 2
        parsed.height shouldBe 2
    }
    "unterminated brace grid is rejected" {
        shouldThrow<ParseException> { KD.read("sample .grid { 1 2") }
    }

    "snip paths round trip quotes and backslashes" {
        for (path in listOf("a b", "a\"b", "a\\b", "a\nb")) {
            Snip.parse(Snip(path, true).toString()) shouldBe Snip(path, true)
        }
    }
    "snip URLs append extension before query and fragment" {
        Snip("https://example.com/part?key=a.b#section").normalizedPath shouldBe
                "https://example.com/part.kd?key=a.b#section"
        Snip("https://example.com/part.kd?key=x").normalizedPath shouldBe
                "https://example.com/part.kd?key=x"
        Snip("https://example.com").normalizedPath shouldBe "https://example.com"
    }
    for ((i, literal) in listOf(
        ".snip(part, expand=true junk)", ".snip(part, junk expand=true)",
        ".snip(\"part\" expand=true)", ".snip(part,)", ".snip(part,,expand=true)"
    ).withIndex()) {
        "snip rejects malformed parameter $i" {
            shouldThrow<ParseException> { Snip.parse(literal) }
        }
    }
    "remote includes resolve against their URL and redirects are rejected" {
        val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
        server.createContext("/parts/outer.kd") { exchange ->
            val bytes = "outer { .snip(inner) }".toByteArray()
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.use { it.write(bytes) }
        }
        server.createContext("/parts/inner.kd") { exchange ->
            val bytes = "leaf 42".toByteArray()
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.use { it.write(bytes) }
        }
        server.createContext("/redirect.kd") { exchange ->
            exchange.responseHeaders.add("Location", "/parts/inner.kd")
            exchange.sendResponseHeaders(302, -1)
            exchange.close()
        }
        server.start()
        try {
            withDocuments { dir ->
                val base = "http://127.0.0.1:${server.address.port}"
                val resolver = SnipResolver(SnipOptions(allowRemoteUrls = true, warnOnInsecureUrls = false))
                KD.parseWithSnips(".snip($base/parts/outer)", dir, resolver)
                    .getChild("leaf")?.value shouldBe 42
                shouldThrow<SnipPathNotFoundException> {
                    KD.parseWithSnips(".snip($base/redirect)", dir, resolver)
                }
                shouldThrow<SnipSecurityException> {
                    KD.parseWithSnips(".snip($base/parts/outer)", dir)
                }
            }
        } finally { server.stop(0) }
    }

    "leading nil and boolean literals remain anonymous positional values" {
        KD.read("nil 1 null true false").values shouldBe listOf(null, 1, null, true, false)
        KD.read("true false 1").values shouldBe listOf(true, false, 1)
        KD.read("nil 1").nsid.isAnonymous shouldBe true
    }

})
