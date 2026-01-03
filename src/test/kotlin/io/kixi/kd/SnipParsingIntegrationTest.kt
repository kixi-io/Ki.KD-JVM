package io.kixi.kd

import io.kixi.text.ParseException
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.assertions.throwables.shouldThrow
import java.nio.file.Files

/**
 * Integration tests for snip parsing through the complete pipeline:
 * Raw KD text → KDParser → Tag with snip value → KD.readWithSnips() → resolved content
 *
 * These tests ensure the parser properly recognizes .snip() literals and that
 * the full resolution pipeline works end-to-end.
 */
class SnipParsingIntegrationTest : FunSpec({

    val parser = KDParser()

    // ========================================================================
    // Phase 1: KDParser recognizes .snip() literals
    // ========================================================================

    context("KDParser .snip() literal recognition") {

        test("parse .snip() as a string value") {
            val tag = parser.parse("""
                config {
                    .snip(shared/database)
                }
            """.trimIndent())

            tag.name shouldBe "config"
            tag.children.size shouldBe 1

            val snipTag = tag.children[0]
            snipTag.isAnonymous().shouldBeTrue()
            snipTag.values.size shouldBe 1
            snipTag.values[0].shouldBeInstanceOf<String>()
            snipTag.values[0] shouldBe ".snip(shared/database)"
        }

        test("parse .snip() with expand parameter") {
            val tag = parser.parse(".snip(components/header, expand=true)")

            tag.isAnonymous().shouldBeTrue()
            tag.values[0] shouldBe ".snip(components/header, expand=true)"
        }

        test("parse .snip() with quoted path") {
            val tag = parser.parse(".snip(\"path with spaces/file\")")

            tag.values[0] shouldBe ".snip(\"path with spaces/file\")"
        }

        test("parse .snip() with URL") {
            val tag = parser.parse(".snip(https://example.com/components/button)")

            tag.values[0] shouldBe ".snip(https://example.com/components/button)"
        }

        test("parse multiple snips in a document") {
            val tag = parser.parse("""
                app {
                    .snip(shared/database)
                    .snip(shared/logging)
                    server { port 8080 }
                    .snip(shared/auth, expand=true)
                }
            """.trimIndent())

            tag.name shouldBe "app"
            tag.children.size shouldBe 4

            // First snip
            tag.children[0].values[0] shouldBe ".snip(shared/database)"

            // Second snip
            tag.children[1].values[0] shouldBe ".snip(shared/logging)"

            // Regular tag
            tag.children[2].name shouldBe "server"

            // Third snip with expand
            tag.children[3].values[0] shouldBe ".snip(shared/auth, expand=true)"
        }

        test("parsed snip values are recognized by Snip.isLiteral()") {
            val tag = parser.parse(".snip(some/path)")

            val value = tag.values[0]
            value.shouldBeInstanceOf<String>()
            Snip.isLiteral(value as String) shouldBe true
        }

        test("reject invalid snip syntax - empty path") {
            shouldThrow<ParseException> {
                parser.parse(".snip()")
            }
        }

        test("reject snip with invalid parameter") {
            shouldThrow<ParseException> {
                parser.parse(".snip(path, invalid=yes)")
            }
        }
    }

    // ========================================================================
    // Phase 2: Full KD.readWithSnips() pipeline integration
    // ========================================================================

    context("KD.readWithSnips() pipeline integration") {

        test("resolve snip from file") {
            val tempDir = Files.createTempDirectory("kd-snip-test")
            try {
                // Create the snipped file
                val sharedDir = Files.createDirectory(tempDir.resolve("shared"))
                val databaseFile = sharedDir.resolve("database.kd")
                Files.writeString(databaseFile, """
                    database {
                        host "localhost"
                        port 5432
                    }
                """.trimIndent())

                // Create the main file that snips the database config
                val mainFile = tempDir.resolve("main.kd")
                Files.writeString(mainFile, """
                    app {
                        .snip(shared/database)
                        server { port 8080 }
                    }
                """.trimIndent())

                // Parse with snip resolution
                val result = KD.readWithSnips(mainFile)

                result.name shouldBe "app"
                result.children.size shouldBe 2

                // First child should be the resolved database tag
                val dbTag = result.children[0]
                dbTag.name shouldBe "database"
                dbTag.children.size shouldBe 2

                // Second child should be the server tag
                result.children[1].name shouldBe "server"

            } finally {
                tempDir.toFile().deleteRecursively()
            }
        }

        test("handle expand=true") {
            val tempDir = Files.createTempDirectory("kd-snip-expand-test")
            try {
                // Create snipped file with root tag containing children
                val componentsDir = Files.createDirectory(tempDir.resolve("components"))
                val headerFile = componentsDir.resolve("header.kd")
                Files.writeString(headerFile, """
                    header {
                        logo "MyApp"
                        nav { item "Home"; item "About" }
                    }
                """.trimIndent())

                // Main file using expand=true
                val mainFile = tempDir.resolve("main.kd")
                Files.writeString(mainFile, """
                    page {
                        .snip(components/header, expand=true)
                    }
                """.trimIndent())

                val result = KD.readWithSnips(mainFile)

                result.name shouldBe "page"
                // With expand=true, children of header should be inserted directly
                // So we should have logo and nav as direct children of page
                result.children.size shouldBe 2
                result.children[0].name shouldBe "logo"
                result.children[1].name shouldBe "nav"

            } finally {
                tempDir.toFile().deleteRecursively()
            }
        }

        test("handle nested snips") {
            val tempDir = Files.createTempDirectory("kd-snip-nested-test")
            try {
                // Level 2: deepest file
                val level2Dir = Files.createDirectory(tempDir.resolve("level2"))
                Files.writeString(level2Dir.resolve("deep.kd"), """
                    deep { value 42 }
                """.trimIndent())

                // Level 1: middle file that snips level 2
                val level1Dir = Files.createDirectory(tempDir.resolve("level1"))
                Files.writeString(level1Dir.resolve("middle.kd"), """
                    middle {
                        .snip(../level2/deep)
                    }
                """.trimIndent())

                // Main file that snips level 1
                Files.writeString(tempDir.resolve("main.kd"), """
                    root {
                        .snip(level1/middle)
                    }
                """.trimIndent())

                val result = KD.readWithSnips(tempDir.resolve("main.kd"))

                result.name shouldBe "root"
                result.children.size shouldBe 1

                val middleTag = result.children[0]
                middleTag.name shouldBe "middle"
                middleTag.children.size shouldBe 1

                val deepTag = middleTag.children[0]
                deepTag.name shouldBe "deep"

            } finally {
                tempDir.toFile().deleteRecursively()
            }
        }

        test("detect circular references") {
            val tempDir = Files.createTempDirectory("kd-snip-circular-test")
            try {
                // File A snips File B
                Files.writeString(tempDir.resolve("a.kd"), """
                    a { .snip(b) }
                """.trimIndent())

                // File B snips File A (circular!)
                Files.writeString(tempDir.resolve("b.kd"), """
                    b { .snip(a) }
                """.trimIndent())

                shouldThrow<SnipCircularReferenceException> {
                    KD.readWithSnips(tempDir.resolve("a.kd"))
                }

            } finally {
                tempDir.toFile().deleteRecursively()
            }
        }

        test("throw on missing snip file") {
            val tempDir = Files.createTempDirectory("kd-snip-missing-test")
            try {
                Files.writeString(tempDir.resolve("main.kd"), """
                    app { .snip(nonexistent/file) }
                """.trimIndent())

                shouldThrow<SnipPathNotFoundException> {
                    KD.readWithSnips(tempDir.resolve("main.kd"))
                }

            } finally {
                tempDir.toFile().deleteRecursively()
            }
        }
    }

    // ========================================================================
    // Phase 3: Edge cases and regression prevention
    // ========================================================================

    context("edge cases and regression prevention") {

        test("snip coexists with other dot-literals") {
            val tag = parser.parse("""
                data {
                    location .geo(37.7749, -122.4194)
                    .snip(shared/config)
                    binary .blob(SGVsbG8gV29ybGQ=)
                    position .coordinate(x=10, y=20)
                }
            """.trimIndent())

            tag.children.size shouldBe 4
            tag.children[0].name shouldBe "location"
            tag.children[1].values[0] shouldBe ".snip(shared/config)"
            tag.children[2].name shouldBe "binary"
            tag.children[3].name shouldBe "position"
        }

        test("snip inside nested tags parses correctly") {
            val tag = parser.parse("""
                level1 {
                    level2 {
                        level3 {
                            .snip(deep/content)
                        }
                    }
                }
            """.trimIndent())

            val level3 = tag.children[0].children[0]
            level3.name shouldBe "level3"
            level3.children[0].values[0] shouldBe ".snip(deep/content)"
        }

        test("multiple snips on same line parse correctly") {
            val tag = parser.parse("""
                root {
                    .snip(a); .snip(b); .snip(c)
                }
            """.trimIndent())

            tag.children.size shouldBe 3
            tag.children[0].values[0] shouldBe ".snip(a)"
            tag.children[1].values[0] shouldBe ".snip(b)"
            tag.children[2].values[0] shouldBe ".snip(c)"
        }
    }
})