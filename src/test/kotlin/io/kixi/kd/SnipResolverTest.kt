package io.kixi.kd

import io.kixi.KiException
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.assertions.throwables.shouldThrow
import java.nio.file.Files
import java.nio.file.Path

/**
 * Tests for SnipResolver covering:
 * - File resolution
 * - Caching behavior
 * - Circular reference detection
 * - Depth limiting
 * - Security constraint enforcement
 */
class SnipResolverTest : FunSpec({

    // ========================================================================
    // Helper Functions
    // ========================================================================

    /**
     * Creates a temporary directory with KD files for testing.
     */
    fun createTestFiles(): Path {
        val tempDir = Files.createTempDirectory("snip-test")

        // Main file references component
        Files.writeString(tempDir.resolve("main.kd"), """
            main {
                title "Test App"
            }
        """.trimIndent())

        // Component file
        Files.writeString(tempDir.resolve("component.kd"), """
            component {
                button "Click me"
            }
        """.trimIndent())

        // Nested directory
        val subDir = Files.createDirectory(tempDir.resolve("components"))
        Files.writeString(subDir.resolve("widget.kd"), """
            widget {
                label "Widget Label"
            }
        """.trimIndent())

        // For circular reference testing
        Files.writeString(tempDir.resolve("a.kd"), """
            a_tag value="from a"
        """.trimIndent())

        Files.writeString(tempDir.resolve("b.kd"), """
            b_tag value="from b"
        """.trimIndent())

        return tempDir
    }

    /**
     * Cleans up temporary test directory.
     */
    fun cleanup(tempDir: Path) {
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.deleteIfExists(it) }
    }

    // ========================================================================
    // Basic Resolution
    // ========================================================================

    context("basic resolution") {
        test("resolve simple file") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()
                val snip = Snip("main")

                val result = resolver.resolve(
                    snip, tempDir, emptyList(), 0, 1, 1
                )

                result shouldHaveSize 1
                result[0].nsid.name shouldBe "main"
            } finally {
                cleanup(tempDir)
            }
        }

        test("resolve file in subdirectory") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()
                val snip = Snip("components/widget")

                val result = resolver.resolve(
                    snip, tempDir, emptyList(), 0, 1, 1
                )

                result shouldHaveSize 1
                result[0].nsid.name shouldBe "widget"
            } finally {
                cleanup(tempDir)
            }
        }

        test("resolve with expand=true returns children") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()
                val snip = Snip("component", expand = true)

                val result = resolver.resolve(
                    snip, tempDir, emptyList(), 0, 1, 1
                )

                result shouldHaveSize 1
                result[0].nsid.name shouldBe "button"
            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Caching
    // ========================================================================

    context("caching") {
        test("same file is cached") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver(SnipOptions(cacheResolvedSnips = true))
                val snip = Snip("main")

                // First resolution
                resolver.resolve(snip, tempDir, emptyList(), 0, 1, 1)
                resolver.getCachedPaths() shouldHaveSize 1

                // Second resolution uses cache
                resolver.resolve(snip, tempDir, emptyList(), 0, 1, 1)
                resolver.getCachedPaths() shouldHaveSize 1
            } finally {
                cleanup(tempDir)
            }
        }

        test("different files are cached separately") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()

                resolver.resolve(Snip("main"), tempDir, emptyList(), 0, 1, 1)
                resolver.resolve(Snip("component"), tempDir, emptyList(), 0, 1, 1)

                resolver.getCachedPaths() shouldHaveSize 2
            } finally {
                cleanup(tempDir)
            }
        }

        test("cache can be cleared") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()

                resolver.resolve(Snip("main"), tempDir, emptyList(), 0, 1, 1)
                resolver.getCachedPaths() shouldHaveSize 1

                resolver.clearCache()
                resolver.getCachedPaths().shouldBeEmpty()
            } finally {
                cleanup(tempDir)
            }
        }

        test("caching can be disabled") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver(SnipOptions(cacheResolvedSnips = false))

                resolver.resolve(Snip("main"), tempDir, emptyList(), 0, 1, 1)
                resolver.getCachedPaths().shouldBeEmpty()
            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Path Not Found
    // ========================================================================

    context("path not found") {
        test("missing file throws SnipPathNotFoundException") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()
                val snip = Snip("nonexistent")

                val exception = shouldThrow<SnipPathNotFoundException> {
                    resolver.resolve(snip, tempDir, emptyList(), 0, 1, 1)
                }

                exception.snipPath shouldBe "nonexistent"
            } finally {
                cleanup(tempDir)
            }
        }

        test("missing nested file throws") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()
                val snip = Snip("components/missing")

                shouldThrow<SnipPathNotFoundException> {
                    resolver.resolve(snip, tempDir, emptyList(), 0, 1, 1)
                }
            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Circular Reference Detection
    // ========================================================================

    context("circular reference detection") {
        test("resolution stack is properly maintained") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()

                resolver.getResolutionStack().shouldBeEmpty()

                resolver.resolve(Snip("main"), tempDir, emptyList(), 0, 1, 1)

                // Stack should be empty after successful resolution
                resolver.getResolutionStack().shouldBeEmpty()
            } finally {
                cleanup(tempDir)
            }
        }

        test("resolution stack is cleared after exception") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()

                resolver.getResolutionStack().shouldBeEmpty()

                // This will throw because file doesn't exist
                try {
                    resolver.resolve(Snip("nonexistent"), tempDir, emptyList(), 0, 1, 1)
                } catch (e: SnipPathNotFoundException) {
                    // Expected
                }

                // Stack should still be empty after exception
                resolver.getResolutionStack().shouldBeEmpty()
            } finally {
                cleanup(tempDir)
            }
        }

        // Note: Full circular reference detection testing requires nested snip expansion
        // which is handled by the KDParser. The resolver correctly detects circular
        // references when the same resolved path appears on its internal stack during
        // nested resolution. The `currentChain` parameter is only used for building
        // error messages, not for detection. Integration tests in KDParserSnipTest
        // verify the complete circular reference detection behavior.
    }

    // ========================================================================
    // Depth Limiting
    // ========================================================================

    context("depth limiting") {
        test("exceeding max depth throws") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver(SnipOptions(maxSnipDepth = 5))

                val exception = shouldThrow<SnipDepthExceededException> {
                    resolver.resolve(
                        Snip("main"),
                        tempDir,
                        emptyList(),
                        5,  // At max depth
                        1, 1
                    )
                }

                exception.currentDepth shouldBe 5
                exception.maxDepth shouldBe 5
            } finally {
                cleanup(tempDir)
            }
        }

        test("depth below max is allowed") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver(SnipOptions(maxSnipDepth = 5))

                // Should not throw at depth 4
                resolver.resolve(
                    Snip("main"),
                    tempDir,
                    emptyList(),
                    4,
                    1, 1
                )
            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Security Constraints
    // ========================================================================

    context("security constraints") {
        test("remote URL throws when disabled") {
            val resolver = SnipResolver(SnipOptions(allowRemoteUrls = false))
            val snip = Snip("https://example.com/file")

            val exception = shouldThrow<SnipSecurityException> {
                resolver.resolve(snip, Path.of("."), emptyList(), 0, 1, 1)
            }

            exception.snipPath shouldBe "https://example.com/file"
            exception.reason shouldBe "Remote URLs are not allowed"
        }

        test("HTTP URL throws when HTTPS required") {
            val resolver = SnipResolver(SnipOptions(
                allowRemoteUrls = true,
                requireHttps = true
            ))
            val snip = Snip("http://example.com/file")

            shouldThrow<SnipSecurityException> {
                resolver.resolve(snip, Path.of("."), emptyList(), 0, 1, 1)
            }
        }

        test("absolute path throws when disabled") {
            val resolver = SnipResolver(SnipOptions(allowAbsolutePaths = false))
            val snip = Snip("/etc/config")

            shouldThrow<SnipSecurityException> {
                resolver.resolve(snip, Path.of("."), emptyList(), 0, 1, 1)
            }
        }

        test("HTTPS URL allowed when required") {
            val resolver = SnipResolver(SnipOptions(
                allowRemoteUrls = true,
                requireHttps = true
            ))
            val snip = Snip("https://example.com/file")

            // Will throw SnipIOException or SnipPathNotFoundException, not SnipSecurityException
            // All snip exceptions inherit from KiException
            val exception = shouldThrow<KiException> {
                resolver.resolve(snip, Path.of("."), emptyList(), 0, 1, 1)
            }

            // Should not be a security exception
            (exception is SnipSecurityException) shouldBe false
        }
    }

    // ========================================================================
    // Factory Methods
    // ========================================================================

    context("factory methods") {
        test("create returns default resolver") {
            val resolver = SnipResolver.create()
            resolver.options shouldBe SnipOptions.DEFAULT
        }

        test("createWithRemoteUrls allows URLs") {
            val resolver = SnipResolver.createWithRemoteUrls()
            resolver.options.allowRemoteUrls shouldBe true
        }

        test("createStrict has strict options") {
            val resolver = SnipResolver.createStrict()
            resolver.options.allowRemoteUrls shouldBe false
            resolver.options.allowAbsolutePaths shouldBe false
        }
    }

    // ========================================================================
    // Reset Behavior
    // ========================================================================

    context("reset behavior") {
        test("reset clears cache and stack") {
            val tempDir = createTestFiles()
            try {
                val resolver = SnipResolver()

                resolver.resolve(Snip("main"), tempDir, emptyList(), 0, 1, 1)
                resolver.getCachedPaths() shouldHaveSize 1

                resolver.reset()

                resolver.getCachedPaths().shouldBeEmpty()
                resolver.getResolutionStack().shouldBeEmpty()
            } finally {
                cleanup(tempDir)
            }
        }
    }
})