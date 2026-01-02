package io.kixi.kd

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kixi.KiException
import io.kixi.text.ParseException

class KDParseExceptionTest : FunSpec({

    context("KDParseException") {
        test("inherits from ParseException") {
            val ex = KDParseException("KD parse error")
            ex.shouldBeInstanceOf<ParseException>()
        }

        test("inherits from KiException") {
            val ex = KDParseException("KD parse error")
            ex.shouldBeInstanceOf<KiException>()
        }

        test("message without location info") {
            val ex = KDParseException("Unexpected token")
            ex.message shouldContain "Unexpected token"
        }

        test("message with line number") {
            val ex = KDParseException("Unexpected token", line = 5)
            ex.message shouldContain "line: 5"
        }

        test("message with index") {
            val ex = KDParseException("Unexpected token", index = 10)
            ex.message shouldContain "index: 10"
        }

        test("message with line and index") {
            val ex = KDParseException("Unexpected token", line = 5, index = 10)
            ex.message shouldContain "line: 5"
            ex.message shouldContain "index: 10"
        }

        test("message with suggestion") {
            val ex = KDParseException("Unexpected token", suggestion = "Check KD syntax")
            ex.message shouldContain "Suggestion: Check KD syntax"
        }

        test("message with cause") {
            val cause = NumberFormatException("Not a number")
            val ex = KDParseException("KD parse error", cause = cause)
            ex.cause shouldBe cause
            ex.message shouldContain "cause:"
        }

        test("backward compatibility - cause as 4th positional parameter") {
            val cause = IllegalArgumentException("Original")
            val ex = KDParseException("Error", -1, 5, cause)
            ex.cause shouldBe cause
            ex.message shouldContain "index: 5"
        }

        test("null suggestion is ignored") {
            val ex = KDParseException("Error", suggestion = null)
            ex.suggestion.shouldBeNull()
            ex.message shouldNotContain "Suggestion:"
        }

        test("all parameters") {
            val cause = RuntimeException("Root cause")
            val ex = KDParseException(
                "Invalid tag structure",
                line = 10,
                index = 25,
                cause = cause,
                suggestion = "Tags must have a name or start with a value"
            )
            ex.message shouldContain "Invalid tag structure"
            ex.message shouldContain "line: 10"
            ex.message shouldContain "index: 25"
            ex.message shouldContain "cause:"
            ex.message shouldContain "Suggestion: Tags must have a name or start with a value"
        }
    }

    context("KDParseException.line factory method") {
        test("creates exception without line number") {
            val ex = KDParseException.line("Error at position", index = 15)
            ex.message shouldContain "index: 15"
            ex.message shouldNotContain "line:"
        }

        test("with suggestion") {
            val ex = KDParseException.line("Error", index = 5, suggestion = "Fix the syntax")
            ex.message shouldContain "Suggestion: Fix the syntax"
        }

        test("with cause") {
            val cause = NumberFormatException("Bad number")
            val ex = KDParseException.line("Error", index = 5, cause = cause)
            ex.cause shouldBe cause
        }

        test("with cause and suggestion") {
            val cause = IllegalStateException("Invalid state")
            val ex = KDParseException.line(
                "Parsing failed",
                index = 20,
                cause = cause,
                suggestion = "Check input format"
            )
            ex.cause shouldBe cause
            ex.message shouldContain "index: 20"
            ex.message shouldContain "Suggestion: Check input format"
        }
    }

    context("exception hierarchy") {
        test("KDParseException is a KiException") {
            val ex = KDParseException("Error")
            ex.shouldBeInstanceOf<KiException>()
            ex.shouldBeInstanceOf<ParseException>()
            ex.shouldBeInstanceOf<RuntimeException>()
        }
    }
})