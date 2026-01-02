package io.kixi.kd

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.assertions.throwables.shouldThrow
import java.nio.file.Files
import java.nio.file.Path

/**
 * Tests for multiline KD documents containing snip directives.
 *
 * These tests demonstrate real-world usage of `.snip()` in KD files,
 * using KD.readWithSnips() to parse and resolve snips.
 *
 * Note: Since KDParser doesn't natively recognize `.snip()` as a literal type,
 * snips are written as quoted strings: `".snip(path)"` which creates an
 * anonymous tag with a string value that readWithSnips() detects and resolves.
 */
class SnipMultiFileTest : FunSpec({

    fun cleanup(tempDir: Path) {
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.deleteIfExists(it) }
    }

    // ========================================================================
    // Basic Multi-Snip Documents
    // ========================================================================

    context("multiline KD with multiple snips") {

        test("layout with header, sidebar, and footer snips") {
            val tempDir = Files.createTempDirectory("layout-test")
            try {
                val componentsDir = Files.createDirectory(tempDir.resolve("components"))

                // components/header.kd
                Files.writeString(componentsDir.resolve("header.kd"), """
                    header class="site-header" {
                        logo src="/img/logo.png" alt="MyApp"
                        nav {
                            link href="/" "Home"
                            link href="/about" "About"
                            link href="/contact" "Contact"
                        }
                    }
                """.trimIndent())

                // components/sidebar.kd
                Files.writeString(componentsDir.resolve("sidebar.kd"), """
                    sidebar id="main-nav" {
                        section title="Menu" {
                            item icon="home" "Dashboard"
                            item icon="settings" "Settings"
                            item icon="profile" "Profile"
                        }
                    }
                """.trimIndent())

                // components/footer.kd
                Files.writeString(componentsDir.resolve("footer.kd"), """
                    footer class="site-footer" {
                        copyright "© 2024 MyApp Inc."
                        links {
                            link href="/privacy" "Privacy"
                            link href="/terms" "Terms"
                        }
                    }
                """.trimIndent())

                // Main layout file with multiple snips
                Files.writeString(tempDir.resolve("layout.kd"), """
                    layout name="default" {
                        ".snip(components/header)"
                        
                        main class="content" {
                            ".snip(components/sidebar)"
                            
                            content id="page-content" {
                                placeholder "Content goes here"
                            }
                        }
                        
                        ".snip(components/footer)"
                    }
                """.trimIndent())

                // First parse without snip resolution to show the unresolved structure
                val unresolvedDoc = KD.read(tempDir.resolve("layout.kd").toFile())

                println("\n=== Layout with Header, Sidebar, Footer ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                // Parse with snip resolution
                val doc = KD.readWithSnips(tempDir.resolve("layout.kd"))

                println("\n--- AFTER (Resolved) ---")
                println(doc)

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "layout"
                doc["name"] shouldBe "default"

                // Should have: header, main, footer
                doc.children shouldHaveSize 3

                // Verify header was snipped in
                val header = doc.children[0]
                header.nsid.name shouldBe "header"
                header["class"] shouldBe "site-header"
                header.children shouldHaveSize 2  // logo, nav

                // Verify main structure with nested sidebar snip
                val main = doc.children[1]
                main.nsid.name shouldBe "main"
                main.children shouldHaveSize 2  // sidebar, content

                val sidebar = main.children[0]
                sidebar.nsid.name shouldBe "sidebar"
                sidebar["id"] shouldBe "main-nav"

                val content = main.children[1]
                content.nsid.name shouldBe "content"

                // Verify footer was snipped in
                val footer = doc.children[2]
                footer.nsid.name shouldBe "footer"
                footer["class"] shouldBe "site-footer"

            } finally {
                cleanup(tempDir)
            }
        }

        test("config file with multiple module snips") {
            val tempDir = Files.createTempDirectory("config-test")
            try {
                val modulesDir = Files.createDirectory(tempDir.resolve("modules"))

                // modules/database.kd
                Files.writeString(modulesDir.resolve("database.kd"), """
                    database {
                        host "db.example.com"
                        port 5432
                        pool_size 10
                        timeout 30s
                    }
                """.trimIndent())

                // modules/cache.kd
                Files.writeString(modulesDir.resolve("cache.kd"), """
                    cache {
                        backend "redis"
                        host "redis.example.com"
                        port 6379
                        ttl 3600
                    }
                """.trimIndent())

                // modules/logging.kd
                Files.writeString(modulesDir.resolve("logging.kd"), """
                    logging {
                        level "info"
                        format "json"
                        output "stdout"
                    }
                """.trimIndent())

                // Main config assembling all modules
                Files.writeString(tempDir.resolve("production.kd"), """
                    config env="production" version="2.0" {
                        ".snip(modules/database)"
                        ".snip(modules/cache)"
                        ".snip(modules/logging)"
                        
                        server {
                            host "0.0.0.0"
                            port 443
                            ssl enabled=true
                        }
                        
                        features {
                            debug false
                            metrics true
                        }
                    }
                """.trimIndent())

                // First parse without snip resolution
                val unresolvedDoc = KD.read(tempDir.resolve("production.kd").toFile())

                println("\n=== Config with Module Snips ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                val doc = KD.readWithSnips(tempDir.resolve("production.kd"))

                println("\n--- AFTER (Resolved) ---")
                println(doc)

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "config"
                doc["env"] shouldBe "production"

                // Should have: database, cache, logging, server, features
                doc.children shouldHaveSize 5

                doc.children[0].nsid.name shouldBe "database"
                doc.children[1].nsid.name shouldBe "cache"
                doc.children[2].nsid.name shouldBe "logging"
                doc.children[3].nsid.name shouldBe "server"
                doc.children[4].nsid.name shouldBe "features"

                // Verify database content
                val db = doc.children[0]
                db.children.find { it.nsid.name == "host" }?.values?.get(0) shouldBe "db.example.com"
                db.children.find { it.nsid.name == "port" }?.values?.get(0) shouldBe 5432

            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Nested Snips (Snips within Snips)
    // ========================================================================

    context("nested snips - components containing other components") {

        test("three-level component nesting") {
            val tempDir = Files.createTempDirectory("nested-test")
            try {
                val uiDir = Files.createDirectory(tempDir.resolve("ui"))

                // Level 3: Primitive icon component
                Files.writeString(uiDir.resolve("icon.kd"), """
                    icon {
                        svg viewBox="0 0 24 24" fill="currentColor" {
                            path d="M12 2L2 22h20L12 2z"
                        }
                    }
                """.trimIndent())

                // Level 2: Button uses icon
                Files.writeString(uiDir.resolve("button.kd"), """
                    button class="btn" {
                        ".snip(icon)"
                        span class="label" "Click Me"
                    }
                """.trimIndent())

                // Level 1: Form uses button
                Files.writeString(uiDir.resolve("form.kd"), """
                    form action="/submit" method="POST" {
                        input type="text" name="username" placeholder="Username"
                        input type="password" name="password" placeholder="Password"
                        ".snip(button)"
                    }
                """.trimIndent())

                // Level 0: Page uses form
                Files.writeString(tempDir.resolve("login.kd"), """
                    page title="Login" {
                        h1 "Sign In"
                        ".snip(ui/form)"
                        p class="footer" "Forgot password?"
                    }
                """.trimIndent())

                // First parse without snip resolution
                val unresolvedDoc = KD.read(tempDir.resolve("login.kd").toFile())

                println("\n=== Three-Level Component Nesting (Page → Form → Button → Icon) ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                val doc = KD.readWithSnips(tempDir.resolve("login.kd"))

                println("\n--- AFTER (Resolved) ---")
                println(doc)

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "page"
                doc["title"] shouldBe "Login"
                doc.children shouldHaveSize 3  // h1, form, p

                // Verify form was snipped in
                val form = doc.children[1]
                form.nsid.name shouldBe "form"
                form["action"] shouldBe "/submit"
                form.children shouldHaveSize 3  // input, input, button

                // Verify button was snipped into form
                val button = form.children[2]
                button.nsid.name shouldBe "button"
                button["class"] shouldBe "btn"
                button.children shouldHaveSize 2  // icon, span

                // Verify icon was snipped into button
                val icon = button.children[0]
                icon.nsid.name shouldBe "icon"
                icon.children[0].nsid.name shouldBe "svg"

            } finally {
                cleanup(tempDir)
            }
        }

        test("shared component used at multiple nesting levels") {
            val tempDir = Files.createTempDirectory("shared-nested-test")
            try {
                // Shared divider component
                Files.writeString(tempDir.resolve("divider.kd"), """
                    hr class="divider"
                """.trimIndent())

                // Section that uses divider
                Files.writeString(tempDir.resolve("section.kd"), """
                    section {
                        h2 "Section Title"
                        p "Section content here"
                        ".snip(divider)"
                    }
                """.trimIndent())

                // Page uses both section (which has divider) and divider directly
                Files.writeString(tempDir.resolve("page.kd"), """
                    article {
                        header {
                            h1 "Article Title"
                        }
                        ".snip(divider)"
                        ".snip(section)"
                        ".snip(section)"
                        ".snip(divider)"
                        footer {
                            p "The End"
                        }
                    }
                """.trimIndent())

                // First parse without snip resolution
                val unresolvedDoc = KD.read(tempDir.resolve("page.kd").toFile())

                println("\n=== Shared Component (Divider) Used at Multiple Levels ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                val doc = KD.readWithSnips(tempDir.resolve("page.kd"))

                println("\n--- AFTER (Resolved) ---")
                println(doc)

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "article"
                // header, hr, section, section, hr, footer
                doc.children shouldHaveSize 6

                doc.children[0].nsid.name shouldBe "header"
                doc.children[1].nsid.name shouldBe "hr"  // first divider
                doc.children[2].nsid.name shouldBe "section"
                doc.children[3].nsid.name shouldBe "section"
                doc.children[4].nsid.name shouldBe "hr"  // second divider
                doc.children[5].nsid.name shouldBe "footer"

                // Each section should also have a divider inside
                val section1 = doc.children[2]
                section1.children shouldHaveSize 3  // h2, p, hr
                section1.children[2].nsid.name shouldBe "hr"

            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Expand Mode
    // ========================================================================

    context("expand mode - inserting children without wrapper") {

        test("expand inserts children directly") {
            val tempDir = Files.createTempDirectory("expand-test")
            try {
                // Meta tags wrapped in a container
                Files.writeString(tempDir.resolve("meta.kd"), """
                    meta_tags {
                        meta charset="utf-8"
                        meta name="viewport" content="width=device-width"
                        meta name="author" content="MyApp Team"
                    }
                """.trimIndent())

                // Head uses expand to insert meta tags directly
                Files.writeString(tempDir.resolve("page.kd"), """
                    html {
                        head {
                            ".snip(meta, expand=true)"
                            title "My Page"
                            link rel="stylesheet" href="/css/app.css"
                        }
                        body {
                            h1 "Hello"
                        }
                    }
                """.trimIndent())

                // First parse without snip resolution
                val unresolvedDoc = KD.read(tempDir.resolve("page.kd").toFile())

                println("\n=== Expand Mode (Meta Tags Inserted Directly) ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                val doc = KD.readWithSnips(tempDir.resolve("page.kd"))

                println("\n--- AFTER (Resolved) ---")
                println(doc)

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "html"
                val head = doc.children[0]
                head.nsid.name shouldBe "head"

                // Should have: meta, meta, meta (expanded), title, link
                head.children shouldHaveSize 5

                // First three should be meta tags (no wrapper)
                head.children[0].nsid.name shouldBe "meta"
                head.children[0]["charset"] shouldBe "utf-8"
                head.children[1].nsid.name shouldBe "meta"
                head.children[1]["name"] shouldBe "viewport"
                head.children[2].nsid.name shouldBe "meta"
                head.children[2]["name"] shouldBe "author"

                // Then title and link
                head.children[3].nsid.name shouldBe "title"
                head.children[4].nsid.name shouldBe "link"

            } finally {
                cleanup(tempDir)
            }
        }

        test("mixed expand and non-expand snips") {
            val tempDir = Files.createTempDirectory("mixed-expand-test")
            try {
                Files.writeString(tempDir.resolve("items.kd"), """
                    items_wrapper {
                        item "One"
                        item "Two"
                        item "Three"
                    }
                """.trimIndent())

                Files.writeString(tempDir.resolve("container.kd"), """
                    container class="box" {
                        title "Container"
                    }
                """.trimIndent())

                Files.writeString(tempDir.resolve("main.kd"), """
                    main {
                        ".snip(container)"
                        list {
                            ".snip(items, expand=true)"
                        }
                    }
                """.trimIndent())

                // First parse without snip resolution
                val unresolvedDoc = KD.read(tempDir.resolve("main.kd").toFile())

                println("\n=== Mixed Expand and Non-Expand Snips ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                val doc = KD.readWithSnips(tempDir.resolve("main.kd"))

                println("\n--- AFTER (Resolved) ---")
                println(doc)

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "main"
                doc.children shouldHaveSize 2  // container, list

                // Container is NOT expanded - wrapper included
                val container = doc.children[0]
                container.nsid.name shouldBe "container"
                container["class"] shouldBe "box"

                // List has items expanded directly inside
                val list = doc.children[1]
                list.nsid.name shouldBe "list"
                list.children shouldHaveSize 3  // item, item, item (no wrapper)
                list.children.all { it.nsid.name == "item" } shouldBe true

            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Circular Reference Detection
    // ========================================================================

    context("circular reference detection") {

        test("self-referencing file throws") {
            val tempDir = Files.createTempDirectory("self-ref-test")
            try {
                Files.writeString(tempDir.resolve("recursive.kd"), """
                    recursive {
                        value "I reference myself"
                        ".snip(recursive)"
                    }
                """.trimIndent())

                shouldThrow<SnipCircularReferenceException> {
                    KD.readWithSnips(tempDir.resolve("recursive.kd"))
                }

            } finally {
                cleanup(tempDir)
            }
        }

        test("mutual reference (A -> B -> A) throws") {
            val tempDir = Files.createTempDirectory("mutual-ref-test")
            try {
                Files.writeString(tempDir.resolve("a.kd"), """
                    component_a {
                        name "A"
                        ".snip(b)"
                    }
                """.trimIndent())

                Files.writeString(tempDir.resolve("b.kd"), """
                    component_b {
                        name "B"
                        ".snip(a)"
                    }
                """.trimIndent())

                shouldThrow<SnipCircularReferenceException> {
                    KD.readWithSnips(tempDir.resolve("a.kd"))
                }

            } finally {
                cleanup(tempDir)
            }
        }

        test("three-way circular (A -> B -> C -> A) throws") {
            val tempDir = Files.createTempDirectory("three-way-ref-test")
            try {
                Files.writeString(tempDir.resolve("a.kd"), """
                    a { ".snip(b)" }
                """.trimIndent())

                Files.writeString(tempDir.resolve("b.kd"), """
                    b { ".snip(c)" }
                """.trimIndent())

                Files.writeString(tempDir.resolve("c.kd"), """
                    c { ".snip(a)" }
                """.trimIndent())

                shouldThrow<SnipCircularReferenceException> {
                    KD.readWithSnips(tempDir.resolve("a.kd"))
                }

            } finally {
                cleanup(tempDir)
            }
        }

        test("diamond pattern is NOT circular and succeeds") {
            val tempDir = Files.createTempDirectory("diamond-test")
            try {
                // D is used by both B and C
                Files.writeString(tempDir.resolve("d.kd"), """
                    shared value="from D"
                """.trimIndent())

                Files.writeString(tempDir.resolve("b.kd"), """
                    b {
                        ".snip(d)"
                    }
                """.trimIndent())

                Files.writeString(tempDir.resolve("c.kd"), """
                    c {
                        ".snip(d)"
                    }
                """.trimIndent())

                // A uses both B and C (diamond pattern)
                Files.writeString(tempDir.resolve("a.kd"), """
                    a {
                        ".snip(b)"
                        ".snip(c)"
                    }
                """.trimIndent())

                // First parse without snip resolution
                val unresolvedDoc = KD.read(tempDir.resolve("a.kd").toFile())

                println("\n=== Diamond Pattern (Not Circular - Should Succeed) ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                // Should NOT throw - diamond is not circular
                val doc = KD.readWithSnips(tempDir.resolve("a.kd"))

                println("\n--- AFTER (Resolved) ---")
                println(doc)

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "a"
                doc.children shouldHaveSize 2  // b, c

                // Both b and c should contain "shared"
                doc.children[0].nsid.name shouldBe "b"
                doc.children[0].children[0].nsid.name shouldBe "shared"

                doc.children[1].nsid.name shouldBe "c"
                doc.children[1].children[0].nsid.name shouldBe "shared"

            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Error Handling
    // ========================================================================

    context("error handling") {

        test("missing snip file throws SnipPathNotFoundException") {
            val tempDir = Files.createTempDirectory("missing-file-test")
            try {
                Files.writeString(tempDir.resolve("main.kd"), """
                    main {
                        ".snip(does-not-exist)"
                    }
                """.trimIndent())

                val exception = shouldThrow<SnipPathNotFoundException> {
                    KD.readWithSnips(tempDir.resolve("main.kd"))
                }

                exception.snipPath shouldBe "does-not-exist"

            } finally {
                cleanup(tempDir)
            }
        }

        test("remote URL blocked by default throws SnipSecurityException") {
            val tempDir = Files.createTempDirectory("url-blocked-test")
            try {
                Files.writeString(tempDir.resolve("main.kd"), """
                    main {
                        ".snip(https://example.com/component.kd)"
                    }
                """.trimIndent())

                shouldThrow<SnipSecurityException> {
                    KD.readWithSnips(tempDir.resolve("main.kd"))
                }

            } finally {
                cleanup(tempDir)
            }
        }
    }

    // ========================================================================
    // Caching
    // ========================================================================

    context("caching behavior") {

        test("same component snipped multiple times is cached") {
            val tempDir = Files.createTempDirectory("cache-test")
            try {
                Files.writeString(tempDir.resolve("item.kd"), """
                    item class="cached" {
                        content "I am cached"
                    }
                """.trimIndent())

                Files.writeString(tempDir.resolve("main.kd"), """
                    main {
                        ".snip(item)"
                        ".snip(item)"
                        ".snip(item)"
                    }
                """.trimIndent())

                // First parse without snip resolution
                val unresolvedDoc = KD.read(tempDir.resolve("main.kd").toFile())

                println("\n=== Caching (Same Component Snipped 3x) ===")
                println("--- BEFORE (Unresolved) ---")
                println(unresolvedDoc.unresolvedSnipString())

                val resolver = SnipResolver(SnipOptions(cacheResolvedSnips = true))
                val doc = KD.readWithSnips(tempDir.resolve("main.kd"), resolver)

                println("\n--- AFTER (Resolved) ---")
                println(doc)
                println("Cached paths: ${resolver.getCachedPaths()}")

                // Should only have one cached entry
                resolver.getCachedPaths() shouldHaveSize 1

                // When KD parses a single root tag, doc IS that tag directly
                doc.nsid.name shouldBe "main"
                doc.children shouldHaveSize 3
                doc.children.all { it.nsid.name == "item" } shouldBe true

            } finally {
                cleanup(tempDir)
            }
        }
    }
})