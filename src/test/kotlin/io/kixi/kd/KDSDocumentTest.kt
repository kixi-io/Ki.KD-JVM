@file:Suppress("DEPRECATION")

package io.kixi.kd

import io.kixi.kd.schema.KDS
import io.kixi.kd.schema.KDSchemaException
import io.kixi.kd.schema.KDValidationException
import io.kixi.kd.schema.ValidationOptions
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import java.nio.file.Files
import java.nio.file.Path

/**
 * Loading KD documents with [KD.load]: plain files, the `.schema(path)`
 * directive, inline schema tags, [LoadOptions], and the [KDS.read] alias.
 */
class KDSDocumentTest : FunSpec({

    val dir: Path = Files.createTempDirectory("kds-doc-test")
    afterSpec { dir.toFile().deleteRecursively() }

    fun write(name: String, text: String): Path {
        val p = dir.resolve(name)
        Files.createDirectories(p.parent)
        Files.writeString(p, text)
        return p
    }

    val assaySchema = """
        schema Assay kds=1 {
            tag assay occurs=1.._ {
                attribute id type=String required=true pattern=@"HTS-\d{4}"
                children {
                    tag measurement occurs=1.._ {
                        attribute well type=String required=true pattern=@"[A-P]\d{1,2}"
                        value type=Number min=0
                    }
                    tag instrument occurs=0..1 { attribute model type=String required=true }
                }
            }
        }
    """.trimIndent()

    context("SchemaRef literal") {
        test("parses bare and quoted paths and implies the .kds extension") {
            SchemaRef.parse(".schema(assay)").normalizedPath shouldBe "assay.kds"
            SchemaRef.parse(".schema(schemas/assay.kds)").normalizedPath shouldBe "schemas/assay.kds"
            SchemaRef.parse(".schema(\"plate reads\")").path shouldBe "plate reads"
            SchemaRef.parse(".schema(../v2/assay)").resolve(Path.of("/data/runs")) shouldBe Path.of("/data/v2/assay.kds")
        }
        test("rejects empty and malformed directives") {
            shouldThrow<Exception> { SchemaRef.parse(".schema()") }
            shouldThrow<Exception> { SchemaRef.parse(".schema(a, b)") }
            SchemaRef.isLiteral(".snip(x)") shouldBe false
            SchemaRef.isLiteral(" .schema(x) ") shouldBe true
        }
        test("plain parsing records the directive without loading anything") {
            val tags = KD.readDocument(".schema(assay)\nassay id=\"HTS-0001\"")
            tags.size shouldBe 2
            tags[0].schemaDirective shouldBe SchemaRef("assay")
            tags[0].values.single() shouldBe ".schema(assay)"
            tags[1].name shouldBe "assay"
        }
        test("a quoted string that looks like a directive stays a string") {
            KD.readDocument("\".schema(assay)\"").single().schemaDirective shouldBe null
        }
    }

    context("external schema via .schema(path)") {
        write("assay.kds", assaySchema)
        write("shared/instrument.kd", "instrument model=\"Cytation 5\"")

        test("valid document, snips resolved, declaration tag removed") {
            val file = write("run-valid.kd", """
                .schema(assay)
                assay id="HTS-0042" {
                    .snip(shared/instrument)
                    measurement 0.82 well=A1
                    measurement 1.07 well=B1
                }
            """.trimIndent())
            val doc = KD.load(file.toFile())
            doc.isValid shouldBe true
            doc.hasSchema shouldBe true
            doc.schema!!.name shouldBe "Assay"
            doc.tags.size shouldBe 1
            doc.root.name shouldBe "assay"
            doc.root.getChild("instrument")!!["model"] shouldBe "Cytation 5"
            doc.report() shouldBe "Assay: valid"
        }

        test("invalid document returns issues and report() lists them") {
            val file = write("run-invalid.kd", """
                .schema(assay)
                assay id="HTS-42" {
                    measurement -0.5 well=Q1
                    note "undeclared"
                }
            """.trimIndent())
            val doc = KDS.read(file.toFile())
            doc.isValid shouldBe false
            doc.issues.map { it.code } shouldBe listOf("PATTERN", "PATTERN", "VALUE_BELOW_MIN", "UNEXPECTED_TAG")
            doc.report() shouldStartWith "Assay: 4 issue(s)"
            doc.report() shouldContain "VALUE_BELOW_MIN     /assay[0]/measurement[0]/values[0]"
            doc.report() shouldContain "[expected 0, actual -0.5]"
            shouldThrow<KDValidationException> { doc.requireValid() }
        }

        test("relative path resolves against the document's folder, not the working directory") {
            write("nested/deep/assay.kds", assaySchema)
            val file = write("nested/deep/run.kd", ".schema(assay)\nassay id=\"HTS-0001\" { measurement 1 well=A1 }")
            KDS.read(file.toFile()).isValid shouldBe true
        }

        test("missing schema file is a schema error") {
            val file = write("missing.kd", ".schema(schemas/not-here)\nassay id=\"HTS-0001\"")
            val e = shouldThrow<KDSchemaException> { KDS.read(file.toFile()) }
            e.message shouldContain "Schema file not found"
            e.schemaPath shouldBe "/tags[0]"
        }

        test("a broken schema file is a schema error with the schema path") {
            write("broken.kds", "schema Broken kds=1 { tag a { value type=Typo } }")
            val file = write("uses-broken.kd", ".schema(broken)\na 1")
            val e = shouldThrow<KDSchemaException> { KDS.read(file.toFile()) }
            e.schemaPath shouldBe "/schema/tag[0]/value[0]"
        }

        test("text input needs a base path to resolve a directive") {
            shouldThrow<KDSchemaException> { KDS.read(".schema(assay)\nassay id=\"HTS-0001\"") }
            KDS.read(".schema(assay)\nassay id=\"HTS-0001\" { measurement 1 well=A1 }", dir).isValid shouldBe true
        }

        test("ValidationOptions are honoured") {
            val file = write("many.kd", ".schema(assay)\nassay id=x { measurement -1 well=Z; measurement -2 well=Z }")
            val doc = KDS.read(file.toFile(), ValidationOptions(maxIssues = 2))
            doc.issues.size shouldBe 2
            doc.validation!!.truncated shouldBe true
        }
    }

    context("inline schema as the first tag") {
        test("valid document") {
            val doc = KDS.read("""
                schema Reagents kds=1 {
                    tag reagent occurs=1.._ {
                        attribute name type=String required=true
                        attribute volume type=Quantity dimension=Volume max=10ℓ
                    }
                }
                reagent name="Trypsin" volume=250mℓ
                reagent name="DMEM" volume=2ℓ
            """.trimIndent())
            doc.isValid shouldBe true
            doc.schema!!.name shouldBe "Reagents"
            doc.tags.map { it["name"] } shouldBe listOf("Trypsin", "DMEM")
            doc.root.name shouldBe "root"
            doc.root.children.size shouldBe 2
        }

        test("invalid document") {
            val doc = KDS.read("""
                schema Reagents kds=1 {
                    tag reagent { attribute name type=String required=true }
                }
                reagent volume=1ℓ
            """.trimIndent())
            doc.issues.map { it.code } shouldBe listOf("MISSING_ATTRIBUTE", "UNEXPECTED_ATTRIBUTE")
        }

        test("a broken inline schema is a schema error, data is never validated") {
            val e = shouldThrow<KDSchemaException> {
                KDS.read("schema Broken kds=1 { tag a { value type=Int min=10 max=1 } }\na 5")
            }
            e.message shouldContain "Empty bound interval"
        }

        test("the schema tag must carry kds=1 to count as a schema") {
            // Without kds=, a tag named schema is ordinary data: it loads as a plain document.
            val doc = KD.load("schema Foo { tag a }\na 1")
            doc.hasSchema shouldBe false
            doc.tags.map { it.name } shouldBe listOf("schema", "a")
            shouldThrow<KDSchemaException> { KDS.read("schema Foo { tag a }\na 1") }
        }
    }

    context("plain documents: schemas are optional") {
        test("a document without a declaration loads as data") {
            val doc = KD.load("person name=\"Alice\" age=30")
            doc.hasSchema shouldBe false
            doc.schema.shouldBeNull()
            doc.validation.shouldBeNull()
            doc.isValid shouldBe true
            doc.issues shouldBe emptyList()
            doc.root["name"] shouldBe "Alice"
            doc.report() shouldBe "no schema"
            doc.requireValid() shouldBe doc
        }
        test("several top-level tags give a synthetic root, like KD.read") {
            val doc = KD.load("a 1\nb 2")
            doc.tags.size shouldBe 2
            doc.root.name shouldBe "root"
            doc.root.children.map { it.name } shouldBe listOf("a", "b")
        }
        test("an empty document is empty, not an error") {
            val doc = KD.load("")
            doc.tags shouldBe emptyList()
            doc.root.name shouldBe "root"
        }
        test("snips resolve from a file without any schema") {
            write("shared/instrument.kd", "instrument model=\"Cytation 5\"")
            val file = write("plain.kd", "run { .snip(shared/instrument) }")
            KD.load(file.toFile()).root.getChild("instrument")!!["model"] shouldBe "Cytation 5"
        }
        test("requireSchema turns a missing declaration into a schema error") {
            val e = shouldThrow<KDSchemaException> { KD.load("assay id=\"HTS-0001\"", null, LoadOptions.SCHEMA_REQUIRED) }
            e.message shouldContain "Expected a .schema(path) directive or a schema tag"
            shouldThrow<KDSchemaException> { KDS.read("assay id=\"HTS-0001\"") }
            shouldThrow<KDSchemaException> { KDS.read("") }
        }
        test("resolveSnips=false leaves directives in place for the schema to report") {
            write("assay.kds", assaySchema)
            write("shared/instrument.kd", "instrument model=\"Cytation 5\"")
            val file = write("unresolved.kd", ".schema(assay)\nassay id=\"HTS-0001\" { .snip(shared/instrument); measurement 1 well=A1 }")
            KD.load(file.toFile(), LoadOptions(resolveSnips = false)).issues.single().code shouldBe "UNRESOLVED_SNIP"
        }
        test("deprecated readWithSnips and parseWithSnips still work and match load().root") {
            write("shared/instrument.kd", "instrument model=\"Cytation 5\"")
            val file = write("legacy.kd", "run { .snip(shared/instrument) }")
            KD.readWithSnips(file.toFile()).toString() shouldBe KD.load(file.toFile()).root.toString()
            KD.parseWithSnips("run { .snip(shared/instrument) }", dir).toString() shouldBe
                    KD.load("run { .snip(shared/instrument) }", dir).root.toString()
        }
    }

    context("placement rules") {
        test("directive after data is rejected") {
            write("assay.kds", assaySchema)
            val file = write("late.kd", "assay id=\"HTS-0001\" { measurement 1 well=A1 }\n.schema(assay)")
            shouldThrow<KDSchemaException> { KDS.read(file.toFile()) }
        }
        test("directive nested in data is rejected") {
            write("assay.kds", assaySchema)
            val file = write("nested.kd", ".schema(assay)\nassay id=\"HTS-0001\" { .schema(assay); measurement 1 well=A1 }")
            shouldThrow<KDSchemaException> { KDS.read(file.toFile()) }
        }
        test("directive sharing its tag with other content is rejected") {
            write("assay.kds", assaySchema)
            val file = write("shared-tag.kd", ".schema(assay) extra=true\nassay id=\"HTS-0001\"")
            shouldThrow<KDSchemaException> { KDS.read(file.toFile()) }
        }
        test("plain schema.validate reports a stray directive instead of applying it") {
            val schema = KDS.compile("schema S kds=1 { tag a { value type=Int } }")
            val result = schema.validate(".schema(assay)\na 1")
            result.issues.single().code shouldBe "MISPLACED_SCHEMA_DIRECTIVE"
        }
    }
})
