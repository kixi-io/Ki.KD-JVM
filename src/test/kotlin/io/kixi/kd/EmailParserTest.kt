package io.kixi.kd

import io.kixi.Email
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * Tests for Email literal parsing in KDParser.
 *
 * Email addresses are parsed as naked literals (without quotes or wrappers):
 * ```
 * user@domain.com
 * dan@leuck.org
 * ```
 */
class EmailParserTest : FunSpec({

    val parser = KDParser()

    // ========================================================================
    // Basic Email Parsing
    // ========================================================================

    context("Basic Email parsing") {

        test("simple email as tag value") {
            val tag = parser.parse("contact dan@leuck.org")
            tag.value shouldBe Email.of("dan@leuck.org")
        }

        test("email with subdomain") {
            val tag = parser.parse("email user@mail.example.com")
            tag.value shouldBe Email.of("user@mail.example.com")
        }

        test("email with underscores in local part") {
            val tag = parser.parse("email john_doe@example.com")
            tag.value shouldBe Email.of("john_doe@example.com")
        }

        test("email with numbers") {
            val tag = parser.parse("email user123@example456.com")
            tag.value shouldBe Email.of("user123@example456.com")
        }

        test("email with hyphen in domain") {
            val tag = parser.parse("email user@my-domain.com")
            tag.value shouldBe Email.of("user@my-domain.com")
        }
    }

    // ========================================================================
    // Anonymous Tags with Email Values
    // ========================================================================

    context("Anonymous tags with Email values") {

        test("email as anonymous tag value") {
            val tag = parser.parse("admin@company.org")
            tag.isAnonymous() shouldBe true
            tag.value shouldBe Email.of("admin@company.org")
        }

        test("multiple emails as anonymous tag values") {
            val tag = parser.parse("alice@example.com bob@example.com")
            tag.isAnonymous() shouldBe true
            tag.values.size shouldBe 2
            tag.values[0] shouldBe Email.of("alice@example.com")
            tag.values[1] shouldBe Email.of("bob@example.com")
        }
    }

    // ========================================================================
    // Email as Attribute Values
    // ========================================================================

    context("Email as attribute values") {

        test("email as attribute value") {
            val tag = parser.parse("user contact=dan@leuck.org")
            (tag.getAttribute("contact") as Email) shouldBe Email.of("dan@leuck.org")
        }

        test("multiple email attributes") {
            val tag = parser.parse("team lead=alice@example.com backup=bob@example.com")
            (tag.getAttribute("lead") as Email) shouldBe Email.of("alice@example.com")
            (tag.getAttribute("backup") as Email) shouldBe Email.of("bob@example.com")
        }

        test("mixed attributes including email") {
            val tag = parser.parse("person name=\"Dan\" email=dan@leuck.org age=42")
            (tag.getAttribute("name") as String) shouldBe "Dan"
            (tag.getAttribute("email") as Email) shouldBe Email.of("dan@leuck.org")
            (tag.getAttribute("age") as Int) shouldBe 42
        }
    }

    // ========================================================================
    // Email in Collections
    // ========================================================================

    context("Email in collections") {

        test("email in list") {
            val tag = parser.parse("contacts [alice@example.com, bob@example.com]")
            val list = tag.value as List<*>
            list.size shouldBe 2
            list[0] shouldBe Email.of("alice@example.com")
            list[1] shouldBe Email.of("bob@example.com")
        }

        test("email as map value") {
            val tag = parser.parse("directory [alice=alice@example.com, bob=bob@example.com]")
            val map = tag.value as Map<*, *>
            map["alice"] shouldBe Email.of("alice@example.com")
            map["bob"] shouldBe Email.of("bob@example.com")
        }
    }

    // ========================================================================
    // Email in Annotations
    // ========================================================================

    context("Email in annotations") {

        test("email as annotation value") {
            val tag = parser.parse("""
                @notify(admin@example.com)
                task "Important task"
            """.trimIndent())

            tag.annotations.size shouldBe 1
            tag.annotations[0].value shouldBe Email.of("admin@example.com")
        }

        test("email as annotation attribute") {
            val tag = parser.parse("""
                @author(email=dan@leuck.org)
                document "Spec"
            """.trimIndent())

            tag.annotations.size shouldBe 1
            (tag.annotations[0].getAttribute("email") as Email) shouldBe Email.of("dan@leuck.org")
        }
    }

    // ========================================================================
    // Email in Call Arguments
    // ========================================================================

    context("Email in Call arguments") {

        test("email as Call value") {
            val tag = parser.parse("recipients sendmail(alice@example.com, bob@example.com)")
            val call = tag.value
            call.shouldBeInstanceOf<io.kixi.Call>()
            call.values.size shouldBe 2
            call.values[0] shouldBe Email.of("alice@example.com")
            call.values[1] shouldBe Email.of("bob@example.com")
        }

        test("email as Call attribute") {
            val tag = parser.parse("action sendmail(to=alice@example.com, from=noreply@example.com)")
            val call = tag.value
            call.shouldBeInstanceOf<io.kixi.Call>()
            (call.getAttribute("to") as Email) shouldBe Email.of("alice@example.com")
            (call.getAttribute("from") as Email) shouldBe Email.of("noreply@example.com")
        }
    }

    // ========================================================================
    // Email in Children
    // ========================================================================

    context("Email in child tags") {

        test("email values in nested structure") {
            val tag = parser.parse("""
                contacts {
                    alice alice@example.com
                    bob bob@example.com
                }
            """.trimIndent())

            tag.children.size shouldBe 2
            tag.children[0].value shouldBe Email.of("alice@example.com")
            tag.children[1].value shouldBe Email.of("bob@example.com")
        }
    }

    // ========================================================================
    // Email Properties Verification
    // ========================================================================

    context("Email properties verification") {

        test("email localPart") {
            val tag = parser.parse("email dan@leuck.org")
            val email = tag.value as Email
            email.localPart shouldBe "dan"
        }

        test("email domain") {
            val tag = parser.parse("email dan@leuck.org")
            val email = tag.value as Email
            email.domain shouldBe "leuck.org"
        }

        test("email tld") {
            val tag = parser.parse("email user@example.com")
            val email = tag.value as Email
            email.tld shouldBe "com"
        }
    }

    // ========================================================================
    // Edge Cases and Disambiguation
    // ========================================================================

    context("Edge cases and disambiguation") {

        test("email followed by attribute") {
            val tag = parser.parse("contact admin@example.com verified=true")
            tag.value shouldBe Email.of("admin@example.com")
            (tag.getAttribute("verified") as Boolean) shouldBe true
        }

        test("email followed by another value") {
            val tag = parser.parse("values user@example.com 42 true")
            tag.values.size shouldBe 3
            tag.values[0] shouldBe Email.of("user@example.com")
            tag.values[1] shouldBe 42
            tag.values[2] shouldBe true
        }

        test("string containing @ is not parsed as email") {
            val tag = parser.parse("text \"not@an@email\"")
            tag.value shouldBe "not@an@email"
        }
    }

    // ========================================================================
    // Email Formatting (Round-trip)
    // ========================================================================

    context("Email formatting and round-trip") {

        test("Email toString produces valid KD") {
            val email = Email.of("dan@leuck.org")
            email.toString() shouldBe "dan@leuck.org"
        }

        test("round-trip email through parse and toString") {
            val original = "contact dan@leuck.org"
            val tag = parser.parse(original)
            val email = tag.value as Email

            // Parse the email string back
            val reparsed = parser.parse("contact ${email}")
            reparsed.value shouldBe email
        }
    }
})