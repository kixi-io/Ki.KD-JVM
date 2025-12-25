package io.kixi.kd

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

/**
 * Tests for raw string parsing to ensure @"..." syntax works correctly
 * and doesn't conflict with annotations (@name) or emails (user@domain).
 */
class RawStringTest : FunSpec({

    val parser = KDParser()

    context("Raw string parsing") {

        test("simple raw string as tag value") {
            val tag = parser.parse("str @\"This is text\"")
            tag.nsid.name shouldBe "str"
            tag.value shouldBe "This is text"
        }

        test("raw string with Windows path") {
            val tag = parser.parse("path @\"C:\\Users\\name\\file.txt\"")
            tag.nsid.name shouldBe "path"
            tag.value shouldBe "C:\\Users\\name\\file.txt"
        }

        test("raw string as anonymous tag value") {
            val tag = parser.parse("@\"This is a raw string\"")
            tag.isAnonymous() shouldBe true
            tag.value shouldBe "This is a raw string"
        }

        test("raw string with regex pattern") {
            val tag = parser.parse("regex @\"\\d+\\.\\d+\"")
            tag.value shouldBe "\\d+\\.\\d+"
        }

        test("raw string followed by attribute") {
            val tag = parser.parse("str @\"raw text\" enabled=true")
            tag.value shouldBe "raw text"
            (tag.getAttribute("enabled") as Boolean) shouldBe true
        }

        test("multiple raw strings as values") {
            val tag = parser.parse("paths @\"C:\\path1\" @\"C:\\path2\"")
            tag.values.size shouldBe 2
            tag.values[0] shouldBe "C:\\path1"
            tag.values[1] shouldBe "C:\\path2"
        }
    }

    context("Raw string vs annotation disambiguation") {

        test("annotation followed by tag with raw string value") {
            val tag = parser.parse("""
                @greeting
                msg @"Hello World"
            """.trimIndent())

            tag.annotations.size shouldBe 1
            tag.annotations[0].nsid.name shouldBe "greeting"
            tag.nsid.name shouldBe "msg"
            tag.value shouldBe "Hello World"
        }

        test("annotation with params followed by raw string") {
            val tag = parser.parse("""
                @test(enabled=true)
                path @"C:\files\readme.md"
            """.trimIndent())

            tag.annotations.size shouldBe 1
            tag.annotations[0].nsid.name shouldBe "test"
            tag.value shouldBe "C:\\files\\readme.md"
        }
    }

    context("Raw string vs email disambiguation") {

        test("email and raw string as separate values") {
            val tag = parser.parse("data user@example.com @\"raw text\"")
            tag.values.size shouldBe 2
            tag.values[0].toString() shouldBe "user@example.com"
            tag.values[1] shouldBe "raw text"
        }

        test("raw string followed by email") {
            val tag = parser.parse("data @\"text\" admin@example.com")
            tag.values.size shouldBe 2
            tag.values[0] shouldBe "text"
            tag.values[1].toString() shouldBe "admin@example.com"
        }
    }

    context("Raw block strings") {

        test("raw block string") {
            val tag = parser.parse("json @\"\"\"\n{\"key\": \"value\"}\n\"\"\"")
            tag.value shouldBe "{\"key\": \"value\"}"
        }
    }
})