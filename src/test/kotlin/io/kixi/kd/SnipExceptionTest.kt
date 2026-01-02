package io.kixi.kd

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kixi.KiException
import io.kixi.text.ParseException

class SnipExceptionTest : FunSpec({

    context("SnipPathNotFoundException") {
        test("inherits from KiException") {
            val ex = SnipPathNotFoundException("config/settings", "/home/user/config/settings.kd")
            ex.shouldBeInstanceOf<KiException>()
        }

        test("contains path info in message") {
            val ex = SnipPathNotFoundException("config/settings", "/home/user/config/settings.kd")
            ex.message shouldContain "config/settings"
            ex.message shouldContain "/home/user/config/settings.kd"
        }

        test("includes line and index when provided") {
            val ex = SnipPathNotFoundException("file", "/path/file.kd", line = 10, index = 5)
            ex.message shouldContain "line: 10"
            ex.message shouldContain "index: 5"
        }

        test("includes default suggestion") {
            val ex = SnipPathNotFoundException("file", "/path/file.kd")
            ex.suggestion.shouldNotBeNull()
            ex.message shouldContain "Suggestion:"
        }

        test("preserves cause") {
            val cause = java.io.FileNotFoundException("Not found")
            val ex = SnipPathNotFoundException("file", "/path/file.kd", cause = cause)
            ex.cause shouldBe cause
        }
    }

    context("SnipCircularReferenceException") {
        test("inherits from KiException") {
            val ex = SnipCircularReferenceException("a.kd", listOf("b.kd", "c.kd"))
            ex.shouldBeInstanceOf<KiException>()
        }

        test("shows circular chain in message") {
            val ex = SnipCircularReferenceException("a.kd", listOf("b.kd", "c.kd"))
            ex.message shouldContain "b.kd -> c.kd -> a.kd"
        }

        test("includes default suggestion") {
            val ex = SnipCircularReferenceException("a.kd", listOf("b.kd"))
            ex.suggestion.shouldNotBeNull()
            ex.message shouldContain "Suggestion:"
        }

        test("properties are accessible") {
            val chain = listOf("first.kd", "second.kd")
            val ex = SnipCircularReferenceException("third.kd", chain, line = 5, index = 10)
            ex.snipPath shouldBe "third.kd"
            ex.chain shouldBe chain
            ex.line shouldBe 5
            ex.index shouldBe 10
        }
    }

    context("SnipDepthExceededException") {
        test("inherits from KiException") {
            val ex = SnipDepthExceededException("deep/file", 51, 50)
            ex.shouldBeInstanceOf<KiException>()
        }

        test("shows depth info in message") {
            val ex = SnipDepthExceededException("deep/file", 51, 50)
            ex.message shouldContain "51"
            ex.message shouldContain "50"
        }

        test("includes default suggestion with max depth") {
            val ex = SnipDepthExceededException("file", 100, 50)
            ex.suggestion.shouldNotBeNull()
            ex.message shouldContain "Suggestion:"
            ex.message shouldContain "50"
        }

        test("properties are accessible") {
            val ex = SnipDepthExceededException("file", 25, 20)
            ex.currentDepth shouldBe 25
            ex.maxDepth shouldBe 20
        }
    }

    context("SnipSecurityException") {
        test("inherits from KiException") {
            val ex = SnipSecurityException("http://evil.com/file", "Remote URLs not allowed")
            ex.shouldBeInstanceOf<KiException>()
        }

        test("shows reason in message") {
            val ex = SnipSecurityException("path", "Test reason")
            ex.message shouldContain "Test reason"
        }

        test("remoteUrlNotAllowed factory") {
            val ex = SnipSecurityException.remoteUrlNotAllowed("http://example.com/file", 5, 10)
            ex.message shouldContain "Remote URLs are not allowed"
            ex.snipPath shouldBe "http://example.com/file"
            ex.line shouldBe 5
            ex.index shouldBe 10
            ex.suggestion.shouldNotBeNull()
        }

        test("httpsRequired factory") {
            val ex = SnipSecurityException.httpsRequired("http://example.com/file")
            ex.message shouldContain "HTTPS is required"
            ex.suggestion.shouldNotBeNull()
        }

        test("absolutePathNotAllowed factory") {
            val ex = SnipSecurityException.absolutePathNotAllowed("/etc/passwd")
            ex.message shouldContain "Absolute paths are not allowed"
            ex.suggestion.shouldNotBeNull()
        }
    }

    context("SnipTimeoutException") {
        test("inherits from KiException") {
            val ex = SnipTimeoutException("http://slow.com/file", 5000)
            ex.shouldBeInstanceOf<KiException>()
        }

        test("shows timeout in message") {
            val ex = SnipTimeoutException("http://slow.com/file", 10000)
            ex.message shouldContain "10000ms"
        }

        test("includes default suggestion with timeout value") {
            val ex = SnipTimeoutException("url", 5000)
            ex.suggestion.shouldNotBeNull()
            ex.message shouldContain "Suggestion:"
        }

        test("properties are accessible") {
            val ex = SnipTimeoutException("url", 3000, line = 1, index = 2)
            ex.timeoutMs shouldBe 3000
            ex.line shouldBe 1
            ex.index shouldBe 2
        }
    }

    context("SnipParseException") {
        test("inherits from KDParseException") {
            val parseError = KDParseException("Syntax error", 5, 10)
            val ex = SnipParseException("config.kd", parseError)
            ex.shouldBeInstanceOf<KDParseException>()
        }

        test("inherits from ParseException") {
            val parseError = KDParseException("Syntax error")
            val ex = SnipParseException("config.kd", parseError)
            ex.shouldBeInstanceOf<ParseException>()
        }

        test("inherits from KiException") {
            val parseError = KDParseException("Syntax error")
            val ex = SnipParseException("config.kd", parseError)
            ex.shouldBeInstanceOf<KiException>()
        }

        test("includes parse error in message") {
            val parseError = KDParseException("Unexpected token", 5, 10)
            val ex = SnipParseException("config.kd", parseError, snipLine = 1, snipIndex = 5)
            ex.message shouldContain "config.kd"
            ex.message shouldContain "Unexpected token"
        }

        test("includes snip location") {
            val parseError = KDParseException("Error")
            val ex = SnipParseException("file.kd", parseError, snipLine = 20, snipIndex = 15)
            ex.message shouldContain "snipLine: 20"
            ex.message shouldContain "snipIndex: 15"
        }

        test("includes default suggestion") {
            val parseError = KDParseException("Error")
            val ex = SnipParseException("file.kd", parseError)
            ex.suggestion.shouldNotBeNull()
        }

        test("properties are accessible") {
            val parseError = KDParseException("Test error", 10, 20)
            val ex = SnipParseException("test.kd", parseError, 5, 15)
            ex.snipPath shouldBe "test.kd"
            ex.parseError shouldBe parseError
            ex.snipLine shouldBe 5
            ex.snipIndex shouldBe 15
        }
    }

    context("SnipIOException") {
        test("inherits from KiException") {
            val ex = SnipIOException("file.kd")
            ex.shouldBeInstanceOf<KiException>()
        }

        test("includes path in message") {
            val ex = SnipIOException("config/settings.kd")
            ex.message shouldContain "config/settings.kd"
        }

        test("includes cause message") {
            val cause = java.io.IOException("Permission denied")
            val ex = SnipIOException("file.kd", cause = cause)
            ex.message shouldContain "Permission denied"
        }

        test("includes default suggestion") {
            val ex = SnipIOException("file.kd")
            ex.suggestion.shouldNotBeNull()
            ex.message shouldContain "Suggestion:"
        }

        test("includes line and index") {
            val ex = SnipIOException("file.kd", line = 10, index = 5)
            ex.message shouldContain "line: 10"
            ex.message shouldContain "index: 5"
        }
    }

    context("exception hierarchy") {
        test("all snip exceptions inherit from KiException") {
            val parseError = KDParseException("error")
            listOf(
                SnipPathNotFoundException("path", "resolved"),
                SnipCircularReferenceException("path", listOf("a", "b")),
                SnipDepthExceededException("path", 10, 5),
                SnipSecurityException("path", "reason"),
                SnipTimeoutException("path", 1000),
                SnipParseException("path", parseError),
                SnipIOException("path")
            ).forEach { ex ->
                ex.shouldBeInstanceOf<KiException>()
            }
        }

        test("only SnipParseException inherits from KDParseException") {
            val parseError = KDParseException("error")

            // SnipParseException should be a KDParseException
            SnipParseException("path", parseError).shouldBeInstanceOf<KDParseException>()

            // Others should NOT be KDParseException (just KiException)
            SnipPathNotFoundException("path", "resolved").shouldBeInstanceOf<KiException>()
            SnipCircularReferenceException("path", listOf("a")).shouldBeInstanceOf<KiException>()
            SnipDepthExceededException("path", 10, 5).shouldBeInstanceOf<KiException>()
            SnipSecurityException("path", "reason").shouldBeInstanceOf<KiException>()
            SnipTimeoutException("path", 1000).shouldBeInstanceOf<KiException>()
            SnipIOException("path").shouldBeInstanceOf<KiException>()
        }
    }
})