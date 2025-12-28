// AttributeOnlyAnonTagsTest

package io.kixi.kd

import io.kixi.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldBeEmpty
import java.math.BigDecimal as Dec
import java.net.URL
import java.time.LocalDate

/**
 * Tests for attribute-only anonymous tags feature.
 *
 * This test suite verifies that KD supports anonymous tags that start with
 * attributes rather than values, enabling config-file style syntax:
 *
 * ```
 * host = "localhost"
 * port = 8080
 * debug = true
 * ```
 */
class AttributeOnlyAnonTagsTest : FunSpec({

    val parser = KDParser()

    // ========================================================================
    // Basic Attribute-Only Anonymous Tags
    // ========================================================================

    context("basic attribute-only anonymous tags") {

        test("single attribute-only anonymous tag with string value") {
            val tag = parser.parse("name = \"Jose\"")
            tag.isAnonymous() shouldBe true
            tag.values.shouldBeEmpty()
            tag["name"] shouldBe "Jose"
        }

        test("single attribute-only anonymous tag without spaces around =") {
            val tag = parser.parse("name=\"Jose\"")
            tag.isAnonymous() shouldBe true
            tag["name"] shouldBe "Jose"
        }

        test("single attribute-only anonymous tag with integer value") {
            val tag = parser.parse("age = 35")
            tag.isAnonymous() shouldBe true
            tag["age"] shouldBe 35
        }

        test("single attribute-only anonymous tag with boolean value") {
            val tag = parser.parse("enabled = true")
            tag.isAnonymous() shouldBe true
            tag["enabled"] shouldBe true
        }

        test("single attribute-only anonymous tag with float value") {
            val tag = parser.parse("ratio = 3.14f")
            tag.isAnonymous() shouldBe true
            tag["ratio"] shouldBe 3.14f
        }

        test("single attribute-only anonymous tag with decimal value") {
            val tag = parser.parse("price = 99.99bd")
            tag.isAnonymous() shouldBe true
            tag["price"].shouldBeInstanceOf<Dec>()
        }
    }

    // ========================================================================
    // Multiple Attribute-Only Anonymous Tags
    // ========================================================================

    context("multiple attribute-only anonymous tags") {

        test("two attribute-only anonymous tags create root with children") {
            val tag = parser.parse("""
                name = "Jose"
                age = 35
            """.trimIndent())

            tag.nsid.name shouldBe "root"
            tag.children shouldHaveSize 2

            tag.children[0].isAnonymous() shouldBe true
            tag.children[0]["name"] shouldBe "Jose"

            tag.children[1].isAnonymous() shouldBe true
            tag.children[1]["age"] shouldBe 35
        }

        test("config file style with multiple attributes") {
            val tag = parser.parse("""
                host = "localhost"
                port = 8080
                debug = true
                timeout = 30L
            """.trimIndent())

            tag.nsid.name shouldBe "root"
            tag.children shouldHaveSize 4

            tag.children[0]["host"] shouldBe "localhost"
            tag.children[1]["port"] shouldBe 8080
            tag.children[2]["debug"] shouldBe true
            tag.children[3]["timeout"] shouldBe 30L
        }

        test("mixed named tags and attribute-only anonymous tags") {
            val tag = parser.parse("""
                server {
                    host = "localhost"
                    port = 8080
                }
            """.trimIndent())

            tag.nsid.name shouldBe "server"
            tag.children shouldHaveSize 2

            tag.children[0].isAnonymous() shouldBe true
            tag.children[0]["host"] shouldBe "localhost"

            tag.children[1].isAnonymous() shouldBe true
            tag.children[1]["port"] shouldBe 8080
        }
    }

    // ========================================================================
    // Attribute-Only Anonymous Tags with Multiple Attributes
    // ========================================================================

    context("attribute-only anonymous tags with multiple attributes") {

        test("anonymous tag starting with attribute, followed by more attributes") {
            val tag = parser.parse("name=\"Jose\" age=35 active=true")
            tag.isAnonymous() shouldBe true
            tag.values.shouldBeEmpty()
            tag["name"] shouldBe "Jose"
            tag["age"] shouldBe 35
            tag["active"] shouldBe true
        }

        test("anonymous tag with attribute and children") {
            val tag = parser.parse("""
                name = "config" {
                    setting1 "value1"
                    setting2 "value2"
                }
            """.trimIndent())

            tag.isAnonymous() shouldBe true
            tag["name"] shouldBe "config"
            tag.children shouldHaveSize 2
            tag.children[0].nsid.name shouldBe "setting1"
            tag.children[1].nsid.name shouldBe "setting2"
        }
    }

    // ========================================================================
    // Complex Value Types in Attribute-Only Anonymous Tags
    // ========================================================================

    context("complex value types in attribute-only anonymous tags") {

        test("attribute-only anonymous tag with URL value") {
            val tag = parser.parse("website = <https://example.com>")
            tag.isAnonymous() shouldBe true
            tag["website"].shouldBeInstanceOf<URL>()
            (tag["website"] as URL).host shouldBe "example.com"
        }

        test("attribute-only anonymous tag with date value") {
            val tag = parser.parse("birthday = 1990/5/15")
            tag.isAnonymous() shouldBe true
            tag["birthday"].shouldBeInstanceOf<LocalDate>()
            (tag["birthday"] as LocalDate).year shouldBe 1990
        }

        test("attribute-only anonymous tag with list value") {
            val tag = parser.parse("numbers = [1, 2, 3, 4, 5]")
            tag.isAnonymous() shouldBe true
            tag["numbers"] shouldBe listOf(1, 2, 3, 4, 5)
        }

        test("attribute-only anonymous tag with map value") {
            val tag = parser.parse("mapping = [a=1, b=2, c=3]")
            tag.isAnonymous() shouldBe true
            val map = tag["mapping"] as Map<*, *>
            map["a"] shouldBe 1
            map["b"] shouldBe 2
            map["c"] shouldBe 3
        }

        test("attribute-only anonymous tag with nil value") {
            val tag = parser.parse("optional = nil")
            tag.isAnonymous() shouldBe true
            tag["optional"] shouldBe null
        }

        test("attribute-only anonymous tag with version value") {
            val tag = parser.parse("version = 2.1.0-beta")
            tag.isAnonymous() shouldBe true
            tag["version"].shouldBeInstanceOf<Version>()
            (tag["version"] as Version).major shouldBe 2
        }

        test("attribute-only anonymous tag with range value") {
            val tag = parser.parse("range = 1..100")
            tag.isAnonymous() shouldBe true
            tag["range"].shouldBeInstanceOf<Range<*>>()
        }
    }

    // ========================================================================
    // Namespaced Attributes in Anonymous Tags
    // ========================================================================

    context("namespaced attributes in attribute-only anonymous tags") {

        test("attribute-only anonymous tag with namespaced attribute") {
            val tag = parser.parse("db:host = \"localhost\"")
            tag.isAnonymous() shouldBe true
            tag["host", "db"] shouldBe "localhost"
        }

        test("multiple namespaced attributes") {
            val tag = parser.parse("db:host=\"localhost\" db:port=5432 cache:size=1000")
            tag.isAnonymous() shouldBe true
            tag["host", "db"] shouldBe "localhost"
            tag["port", "db"] shouldBe 5432
            tag["size", "cache"] shouldBe 1000
        }
    }

    // ========================================================================
    // Annotations with Attribute-Only Anonymous Tags
    // ========================================================================

    context("annotations with attribute-only anonymous tags") {

        test("annotated attribute-only anonymous tag") {
            val tag = parser.parse("""
                @Required
                name = "Jose"
            """.trimIndent())

            tag.isAnonymous() shouldBe true
            tag.annotations shouldHaveSize 1
            tag.annotations[0].nsid.name shouldBe "Required"
            tag["name"] shouldBe "Jose"
        }

        test("multiple annotations on attribute-only anonymous tag") {
            val tag = parser.parse("""
                @Validated
                @NotEmpty
                email = "jose@example.com"
            """.trimIndent())

            tag.isAnonymous() shouldBe true
            tag.annotations shouldHaveSize 2
            tag["email"] shouldBe "jose@example.com"
        }
    }

    // ========================================================================
    // Round-Trip (Parse -> toString -> Parse)
    // ========================================================================

    context("round-trip consistency") {

        test("single attribute-only anonymous tag round-trips correctly") {
            val original = "name=\"Jose\""
            val tag = parser.parse(original)
            val output = tag.toString()
            val reparsed = parser.parse(output)

            reparsed.isAnonymous() shouldBe true
            reparsed["name"] shouldBe "Jose"
        }

        test("multiple attribute anonymous tag round-trips correctly") {
            val original = "name=\"Jose\" age=35"
            val tag = parser.parse(original)
            val output = tag.toString()
            val reparsed = parser.parse(output)

            reparsed.isAnonymous() shouldBe true
            reparsed["name"] shouldBe "Jose"
            reparsed["age"] shouldBe 35
        }
    }

    // ========================================================================
    // Edge Cases
    // ========================================================================

    context("edge cases") {

        test("attribute with underscore in name") {
            val tag = parser.parse("my_config_value = 42")
            tag.isAnonymous() shouldBe true
            tag["my_config_value"] shouldBe 42
        }

        test("attribute with dollar sign in name") {
            val tag = parser.parse("\$price = 99")
            tag.isAnonymous() shouldBe true
            tag["\$price"] shouldBe 99
        }

        test("attribute with unicode name") {
            val tag = parser.parse("名前 = \"太郎\"")
            tag.isAnonymous() shouldBe true
            tag["名前"] shouldBe "太郎"
        }

        test("attribute-only anonymous tag on same line with semicolon separator") {
            val tag = parser.parse("a = 1; b = 2; c = 3")
            tag.nsid.name shouldBe "root"
            tag.children shouldHaveSize 3
            tag.children[0]["a"] shouldBe 1
            tag.children[1]["b"] shouldBe 2
            tag.children[2]["c"] shouldBe 3
        }

        test("distinguishes named tag from attribute-only anonymous tag") {
            // "name value" is a named tag
            val namedTag = parser.parse("name \"Jose\"")
            namedTag.nsid.name shouldBe "name"
            namedTag.value shouldBe "Jose"

            // "name=value" is an attribute-only anonymous tag
            val anonTag = parser.parse("name=\"Jose\"")
            anonTag.isAnonymous() shouldBe true
            anonTag["name"] shouldBe "Jose"
        }

        test("attribute with spaces around equals sign") {
            val tag = parser.parse("key   =   \"value\"")
            tag.isAnonymous() shouldBe true
            tag["key"] shouldBe "value"
        }

        test("empty string attribute value") {
            val tag = parser.parse("empty = \"\"")
            tag.isAnonymous() shouldBe true
            tag["empty"] shouldBe ""
        }
    }

    // ========================================================================
    // Real-World Config File Examples
    // ========================================================================

    context("real-world config file examples") {

        test("database configuration") {
            val tag = parser.parse("""
                database {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/mydb"
                    username = "admin"
                    password = "secret"
                    pool_size = 10
                    timeout = 30000L
                }
            """.trimIndent())

            tag.nsid.name shouldBe "database"
            tag.children shouldHaveSize 6

            tag.children[0]["driver"] shouldBe "org.postgresql.Driver"
            tag.children[4]["pool_size"] shouldBe 10
        }

        test("application settings") {
            val tag = parser.parse("""
                app_name = "MyApplication"
                version = 1.0.0
                debug = false
                
                logging {
                    level = "INFO"
                    file = "/var/log/app.log"
                    max_size = 10mb
                }
                
                features {
                    experimental = false
                    beta = true
                }
            """.trimIndent())

            tag.nsid.name shouldBe "root"

            // Find the app_name child
            val appName = tag.children.find { it["app_name"] != null }
            appName shouldNotBe null
            appName!!["app_name"] shouldBe "MyApplication"

            // Find the logging section
            val logging = tag.getChild("logging")
            logging shouldNotBe null
            logging!!.children.find { it["level"] != null }!!["level"] shouldBe "INFO"
        }

        test("simple key-value properties file style") {
            val tag = parser.parse("""
                # Database settings
                db_host = "localhost"
                db_port = 5432
                
                # Cache settings  
                cache_enabled = true
                cache_ttl = 3600
            """.trimIndent())

            tag.nsid.name shouldBe "root"
            tag.children shouldHaveSize 4
        }
    }

    // ========================================================================
    // Property Access Methods (getProperty, etc.)
    // ========================================================================

    context("getProperty method") {

        test("getProperty returns attribute value") {
            val config = parser.parse("""
                host = "localhost"
                port = 8080
                debug = true
            """.trimIndent())

            config.getProperty("host") shouldBe "localhost"
            config.getProperty("port") shouldBe 8080
            config.getProperty("debug") shouldBe true
        }

        test("getProperty returns nil value") {
            val config = parser.parse("""
                optional = nil
            """.trimIndent())

            config.getProperty("optional") shouldBe null
        }

        test("getProperty throws PropertyNotFoundException for missing key") {
            val config = parser.parse("""
                host = "localhost"
            """.trimIndent())

            val exception = shouldThrow<PropertyNotFoundException> {
                config.getProperty("missing")
            }
            exception.key shouldBe "missing"
            exception.message shouldBe "Property 'missing' not found"
        }

        test("getProperty with namespace") {
            val config = parser.parse("""
                db:host = "localhost"
                db:port = 5432
            """.trimIndent())

            config.getProperty("host", "db") shouldBe "localhost"
            config.getProperty("port", "db") shouldBe 5432
        }

        test("getProperty throws for missing namespaced key") {
            val config = parser.parse("""
                db:host = "localhost"
            """.trimIndent())

            val exception = shouldThrow<PropertyNotFoundException> {
                config.getProperty("host", "cache")
            }
            exception.namespace shouldBe "cache"
            exception.message shouldBe "Property 'cache:host' not found"
        }
    }

    context("getPropertyOrNull method") {

        test("getPropertyOrNull returns value when found") {
            val config = parser.parse("""
                host = "localhost"
            """.trimIndent())

            config.getPropertyOrNull("host") shouldBe "localhost"
        }

        test("getPropertyOrNull returns null when not found") {
            val config = parser.parse("""
                host = "localhost"
            """.trimIndent())

            config.getPropertyOrNull("missing") shouldBe null
        }

        test("getPropertyOrNull returns null for nil value") {
            val config = parser.parse("""
                optional = nil
            """.trimIndent())

            config.getPropertyOrNull("optional") shouldBe null
        }
    }

    context("hasProperty method") {

        test("hasProperty returns true for existing property") {
            val config = parser.parse("""
                host = "localhost"
            """.trimIndent())

            config.hasProperty("host") shouldBe true
        }

        test("hasProperty returns false for missing property") {
            val config = parser.parse("""
                host = "localhost"
            """.trimIndent())

            config.hasProperty("missing") shouldBe false
        }

        test("hasProperty returns true for nil-valued property") {
            val config = parser.parse("""
                optional = nil
            """.trimIndent())

            config.hasProperty("optional") shouldBe true
        }

        test("hasProperty with namespace") {
            val config = parser.parse("""
                db:host = "localhost"
            """.trimIndent())

            config.hasProperty("host", "db") shouldBe true
            config.hasProperty("host", "cache") shouldBe false
        }
    }

    context("getProperties and getPropertiesMap methods") {

        test("getPropertiesMap returns all properties") {
            val config = parser.parse("""
                host = "localhost"
                port = 8080
                debug = true
            """.trimIndent())

            val props = config.getPropertiesMap()
            props.size shouldBe 3
            props["host"] shouldBe "localhost"
            props["port"] shouldBe 8080
            props["debug"] shouldBe true
        }

        test("getPropertiesMap with nested sections") {
            val config = parser.parse("""
                app = "myapp"
                database {
                    host = "localhost"
                    port = 5432
                }
            """.trimIndent())

            // Root collects from its direct children only (not grandchildren)
            // The anonymous tag with app="myapp" is a direct child
            // The database tag is a direct child, but host/port are in database's children
            val rootProps = config.getPropertiesMap()
            rootProps["app"] shouldBe "myapp"
            rootProps.containsKey("host") shouldBe false  // host is in grandchild, not direct child

            // Database section has properties in its children
            val dbProps = config.getChild("database")!!.getPropertiesMap()
            dbProps["host"] shouldBe "localhost"
            dbProps["port"] shouldBe 5432
        }
    }
})