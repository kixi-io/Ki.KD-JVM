package io.kixi.kd

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow

/**
 * Tests for SnipOptions configuration class.
 */
class SnipOptionsTest : FunSpec({

    // ========================================================================
    // Default Values
    // ========================================================================

    context("default values") {
        test("default options have conservative security settings") {
            val options = SnipOptions()
            options.allowRemoteUrls shouldBe false
            options.allowAbsolutePaths shouldBe true
            options.urlTimeoutMs shouldBe 10_000
            options.maxSnipDepth shouldBe 50
            options.warnOnInsecureUrls shouldBe true
            options.requireHttps shouldBe false
            options.cacheResolvedSnips shouldBe true
        }

        test("DEFAULT constant matches default constructor") {
            SnipOptions.DEFAULT shouldBe SnipOptions()
        }
    }

    // ========================================================================
    // Preset Configurations
    // ========================================================================

    context("preset configurations") {
        test("PERMISSIVE allows remote URLs") {
            val options = SnipOptions.PERMISSIVE
            options.allowRemoteUrls shouldBe true
            options.allowAbsolutePaths shouldBe true
            options.maxSnipDepth shouldBe 100
        }

        test("STRICT disables remote URLs and absolute paths") {
            val options = SnipOptions.STRICT
            options.allowRemoteUrls shouldBe false
            options.allowAbsolutePaths shouldBe false
            options.requireHttps shouldBe true
            options.maxSnipDepth shouldBe 20
        }

        test("SECURE_REMOTE allows URLs but requires HTTPS") {
            val options = SnipOptions.SECURE_REMOTE
            options.allowRemoteUrls shouldBe true
            options.requireHttps shouldBe true
            options.allowAbsolutePaths shouldBe false
        }
    }

    // ========================================================================
    // Builder Methods
    // ========================================================================

    context("builder methods") {
        test("withRemoteUrls enables remote URLs") {
            val options = SnipOptions().withRemoteUrls()
            options.allowRemoteUrls shouldBe true
        }

        test("withRemoteUrls can disable") {
            val options = SnipOptions.PERMISSIVE.withRemoteUrls(false)
            options.allowRemoteUrls shouldBe false
        }

        test("withAbsolutePaths toggles setting") {
            val options = SnipOptions().withAbsolutePaths(false)
            options.allowAbsolutePaths shouldBe false
        }

        test("withTimeout sets timeout") {
            val options = SnipOptions().withTimeout(5000)
            options.urlTimeoutMs shouldBe 5000
        }

        test("withMaxDepth sets max depth") {
            val options = SnipOptions().withMaxDepth(25)
            options.maxSnipDepth shouldBe 25
        }

        test("requireHttps enables HTTPS requirement") {
            val options = SnipOptions().requireHttps()
            options.requireHttps shouldBe true
        }

        test("chaining builder methods") {
            val options = SnipOptions()
                .withRemoteUrls()
                .withTimeout(3000)
                .requireHttps()

            options.allowRemoteUrls shouldBe true
            options.urlTimeoutMs shouldBe 3000
            options.requireHttps shouldBe true
        }
    }

    // ========================================================================
    // Validation
    // ========================================================================

    context("validation") {
        test("negative timeout throws") {
            shouldThrow<IllegalArgumentException> {
                SnipOptions(urlTimeoutMs = -1)
            }
        }

        test("zero timeout throws") {
            shouldThrow<IllegalArgumentException> {
                SnipOptions(urlTimeoutMs = 0)
            }
        }

        test("negative max depth throws") {
            shouldThrow<IllegalArgumentException> {
                SnipOptions(maxSnipDepth = -1)
            }
        }

        test("zero max depth throws") {
            shouldThrow<IllegalArgumentException> {
                SnipOptions(maxSnipDepth = 0)
            }
        }

        test("positive values are accepted") {
            val options = SnipOptions(urlTimeoutMs = 1, maxSnipDepth = 1)
            options.urlTimeoutMs shouldBe 1
            options.maxSnipDepth shouldBe 1
        }
    }

    // ========================================================================
    // Data Class Behavior
    // ========================================================================

    context("data class behavior") {
        test("copy preserves values") {
            val original = SnipOptions(
                allowRemoteUrls = true,
                urlTimeoutMs = 5000
            )
            val copied = original.copy()
            copied shouldBe original
        }

        test("copy with changes") {
            val original = SnipOptions()
            val modified = original.copy(allowRemoteUrls = true)

            original.allowRemoteUrls shouldBe false
            modified.allowRemoteUrls shouldBe true
        }

        test("equality works correctly") {
            val options1 = SnipOptions(allowRemoteUrls = true, maxSnipDepth = 30)
            val options2 = SnipOptions(allowRemoteUrls = true, maxSnipDepth = 30)
            options1 shouldBe options2
        }

        test("different options not equal") {
            val options1 = SnipOptions(allowRemoteUrls = true)
            val options2 = SnipOptions(allowRemoteUrls = false)
            options1 shouldNotBe options2
        }
    }
})