package io.kixi.kd

import io.kixi.*
import io.kixi.kd.schema.KDS
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * Tests for the `.id(...)` and `.ref(...)` literals in KD, the `@node`
 * annotation convention that RevDB builds on them, and KDS `Id`/`Ref` types.
 */
class IdAndRefLiteralTest : StringSpec({

    val id1Text = "01J9A0X5K2M7N3P8Q1R6T4V9AB"
    val id2Text = "01J9A0X5K2M7N3P8Q1R6T4VACD"
    val id1 = Id.parse(id1Text)
    val id2 = Id.parse(id2Text)

    // =========================================================================
    // Values and attributes
    // =========================================================================

    "parse .id as a tag value" {
        val tag = KD.read("key .id($id1Text)")
        tag.value.shouldBeInstanceOf<Id>()
        tag.value shouldBe id1
    }

    "parse .ref as a tag value, with and without a collection" {
        val with = KD.read("link .ref(compounds/$id1Text)").value as Ref
        with.collection shouldBe "compounds"
        with.id shouldBe id1

        val without = KD.read("link .ref($id1Text)").value as Ref
        without.collection.shouldBeNull()
        without.id shouldBe id1
    }

    "parse .id and .ref as attribute values" {
        val tag = KD.read("""result key=.id($id1Text) compound=.ref(compounds/$id2Text)""")
        tag["key"] shouldBe id1
        tag["compound"] shouldBe Ref("compounds", id2)
        (tag["compound"] as Ref).collection shouldBe "compounds"
    }

    "parse lists of refs" {
        val tag = KD.read("paper cites=[.ref(papers/$id1Text) .ref($id2Text)]")
        val cites = tag["cites"] as List<*>
        cites.size shouldBe 2
        cites[0] shouldBe Ref("papers", id1)
        cites[1] shouldBe Ref(id2)
    }

    "parse a link tag whose first value is a ref" {
        val tag = KD.read("""
            plate barcode="PLT-00042" {
                derived_from .ref(files/$id1Text) version=1.4.2
            }
        """.trimIndent())
        val link = tag.getChild("derived_from")!!
        link.value shouldBe Ref("files", id1)
        link["version"] shouldBe Version.parse("1.4.2")
    }

    // =========================================================================
    // Grids
    // =========================================================================

    "parse .grid<Ref?> with nil cells" {
        val tag = KD.read("""
            layout .grid<Ref?> {
                .ref(compounds/$id1Text)  nil
                .ref($id2Text)            .ref(compounds/$id1Text)
            }
        """.trimIndent())
        val grid = tag.value as Grid<*>
        grid.elementType shouldBe Ref::class.java
        grid.elementNullable shouldBe true
        grid.width shouldBe 2
        grid.height shouldBe 2
        grid[0, 0] shouldBe Ref("compounds", id1)
        grid[1, 0].shouldBeNull()
        grid[0, 1] shouldBe Ref(id2)
    }

    "parse .grid<Id>" {
        val grid = KD.read("g .grid<Id> { .id($id1Text) .id($id2Text) }").value as Grid<*>
        grid.elementType shouldBe Id::class.java
        grid[1, 0] shouldBe id2
    }

    // =========================================================================
    // @node annotation
    // =========================================================================

    "parse the @node annotation with an id and bare label tokens" {
        val tag = KD.read("""
            @node(.id($id1Text) labels=[chebi:CHEBI_57514 kinase_inhibitor])
            compound name="Staurosporine" mw=466.5Da
        """.trimIndent())
        val anno = tag.annotations.single()
        anno.name shouldBe "node"
        anno.namespace shouldBe ""
        anno.value shouldBe id1
        anno["labels"] shouldBe listOf("chebi:CHEBI_57514", "kinase_inhibitor")
        tag["name"] shouldBe "Staurosporine"
    }

    "annotated inner tags keep their ids" {
        val tag = KD.read("""
            @node(.id($id1Text))
            paper doi="10.1000/xyz123" {
                @node(.id($id2Text) labels=[sec:methods])
                section heading="Methods"
                section heading="Unaddressed"
            }
        """.trimIndent())
        tag.annotations.single().value shouldBe id1
        val sections = tag.getChildren("section")
        sections[0].annotations.single().value shouldBe id2
        sections[1].annotations.isEmpty() shouldBe true
    }

    // =========================================================================
    // Round trip
    // =========================================================================

    "toString round trips ids, refs, grids and the annotation" {
        val source = """
            @node(.id($id1Text) labels=[chebi:CHEBI_57514 kinase_inhibitor])
            compound name="Staurosporine" assay=.ref(assays/$id2Text) refs=[.ref($id1Text) .ref(assays/$id2Text)] {
                derived_from .ref(plates/$id2Text) version=1.4.2
                layout .grid<Ref?> {
                    .ref(compounds/$id1Text)  nil
                    .ref($id2Text)            .ref(compounds/$id1Text)
                }
            }
        """.trimIndent()
        val tag = KD.read(source)
        val again = KD.read(tag.toString())
        again shouldBe tag
        again.annotations.single().value shouldBe id1
        again["assay"] shouldBe Ref("assays", id2)
        (again["assay"] as Ref).collection shouldBe "assays"
        (again.getChild("layout")!!.value as Grid<*>).elementType shouldBe Ref::class.java
    }

    // =========================================================================
    // Errors
    // =========================================================================

    "invalid id and ref literals are KD parse errors" {
        shouldThrow<KDParseException> { KD.read("x .id(zz)") }
        shouldThrow<KDParseException> { KD.read("x .id($id1Text$id1Text)") }
        shouldThrow<KDParseException> { KD.read("x .ref(9bad/$id1Text)") }
        shouldThrow<KDParseException> { KD.read("x .ref(a/b/$id1Text)") }
    }

    // =========================================================================
    // KDS
    // =========================================================================

    "KDS accepts Id and Ref as types" {
        val schema = KDS.compile("""
            schema Results kds=1 {
                tag result {
                    attribute compound type=Ref required=true
                    attribute key type=Id
                }
            }
        """.trimIndent())

        schema.validate("""
            @node(.id($id1Text))
            result compound=.ref(compounds/$id2Text) key=.id($id1Text)
        """.trimIndent()).isValid shouldBe true

        val bad = schema.validate("""result compound="compounds/$id2Text" key=5""")
        bad.isValid shouldBe false
        bad.issues.size shouldBe 2
        bad.issues.all { it.code == "TYPE" } shouldBe true
    }
})
