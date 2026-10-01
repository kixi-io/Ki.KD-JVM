package io.kixi.kd

import io.kixi.Call
import io.kixi.Grid
import io.kixi.NSID
import io.kixi.kd.schema.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class KDSSchemaTest : FunSpec({
    fun schema(body: String) = KDS.compile("schema Test kds=1 { $body }")
    fun invalidSchema(body: String) { shouldThrow<KDSchemaException> { schema(body) } }
    fun code(schema: KDSchema, data: String, expected: String) {
        schema.validate(data).issues.any { it.code == expected } shouldBe true
    }

    test("bare strings quoted strings and raw strings compile equivalently") {
        for (name in listOf("status", "\"status\"", "@\"status\"")) {
            val s = schema("tag $name occurs=1 { value type=String enum=[planned running complete] }")
            s.validate("status running").isValid shouldBe true
            code(s, "status unknown", "ENUM")
        }
    }
    test("raw regex preserves backslashes") {
        val s = schema("""tag code { value type=String pattern=@"\d+\.\d+" }""")
        s.validate("code \"12.34\"").isValid shouldBe true
        code(s, "code \"x12.34x\"", "PATTERN")
    }
    test("reserved names must be quoted") {
        invalidSchema("tag true")
        schema("tag \"true\" occurs=0").validate("").isValid shouldBe true
    }
    test("closed defaults and required tag") {
        val s = schema("tag sample")
        s.validate("sample").isValid shouldBe true
        code(s, "", "OCCURRENCE")
        code(s, "sample; sample", "OCCURRENCE")
        code(s, "other", "UNEXPECTED_TAG")
        code(s, "sample 1", "UNEXPECTED_VALUE")
        code(s, "sample x=1", "UNEXPECTED_ATTRIBUTE")
        code(s, "sample { child }", "UNEXPECTED_TAG")
    }
    test("attributes distinguish optional missing and nil") {
        val s = schema("tag sample { attribute x type=Int required=true nullable=true; attribute y type=String }")
        s.validate("sample x=nil").isValid shouldBe true
        code(s, "sample", "MISSING_ATTRIBUTE")
        code(s, "sample x=1 y=nil", "NIL_NOT_ALLOWED")
    }
    test("strict numeric types do not coerce") {
        val s = schema("tag sample { value type=Int }")
        s.validate("sample 1").isValid shouldBe true
        for (v in listOf("1L", "1.0", "\"1\"")) code(s, "sample $v", "TYPE")
        schema("tag sample { values type=Number minCount=1 }").validate("sample 1 2L 3.0 4.0bd").isValid shouldBe true
    }
    test("nullable Any and nil type") {
        code(schema("tag x { value type=Any }"), "x nil", "NIL_NOT_ALLOWED")
        schema("tag x { value type=Any nullable=true }").validate("x nil").isValid shouldBe true
        schema("tag x { value type=\"nil\" }").validate("x nil").isValid shouldBe true
        code(schema("tag x { value type=\"nil\" }"), "x 1", "TYPE")
    }
    test("positional tuples and optional trailing positions") {
        val s = schema("tag p { value type=String; value type=Int required=false }")
        s.validate("p hello").isValid shouldBe true
        s.validate("p hello 2").isValid shouldBe true
        code(s, "p", "MISSING_VALUE")
        code(s, "p hello 2 3", "UNEXPECTED_VALUE")
        invalidSchema("tag p { value required=false; value }")
    }
    test("homogeneous sequences and collection counts have separate meaning") {
        val s = schema("tag p { values type=List minCount=2 maxCount=3 { items type=Int } }")
        s.validate("p [1] [2 3 4 5]").isValid shouldBe true
        code(s, "p [1]", "COUNT_BELOW_MIN")
        code(s, "p [1] [2] [3] [4]", "COUNT_ABOVE_MAX")
        invalidSchema("tag p { values; value }")
    }
    test("occurrence ranges count each sibling name independently") {
        val s = schema("tag a occurs=0..1; tag b occurs=1.._")
        s.validate("b; a; b").isValid shouldBe true
        code(s, "a; a; b", "OCCURRENCE")
        for (v in listOf("-1", "2..1", "0..<2", "1.5", "nil")) invalidSchema("tag a occurs=$v")
    }
    test("namespace match is exact") {
        val s = schema("tag a namespace=lab { attribute x namespace=lab type=Int required=true }")
        s.validate("lab:a lab:x=1").isValid shouldBe true
        code(s, "a lab:x=1", "UNEXPECTED_TAG")
        code(s, "lab:a x=1", "MISSING_ATTRIBUTE")
    }
    test("anonymous property tags are not flattened") {
        val s = schema("tag \"\" occurs=1.._ { attribute host type=String required=true }")
        s.validate("host=localhost\nhost=example").isValid shouldBe true
        code(s, "port=80", "MISSING_ATTRIBUTE")
    }
    test("open structural scopes are local") {
        val s = schema("tag a additionalAttributes=true additionalValues=true { children additionalChildren=true { tag b } }")
        s.validate("a 1 x=2 { b; c 3 }").isValid shouldBe true
        code(s, "a { b 1 }", "UNEXPECTED_VALUE")
        val open = KDS.compile("schema Open kds=1 additionalChildren=true { tag a }")
        open.validate("a; b 1").isValid shouldBe true
    }
    test("annotations are open by default and can be constrained") {
        schema("tag a").validate("@extra(1) a").isValid shouldBe true
        val s = schema("tag a additionalAnnotations=false { annotation meta { value type=Int } }")
        s.validate("@meta(2) a").isValid shouldBe true
        code(s, "a", "OCCURRENCE")
        code(s, "@meta(no) a", "TYPE")
        code(s, "@meta(2) @other a", "UNEXPECTED_ANNOTATION")
    }
    test("bounds preserve large integer and decimal precision") {
        val s = schema("tag n { value type=Number min=9007199254740993L max=9007199254740994L }")
        s.validate("n 9007199254740993L").isValid shouldBe true
        code(s, "n 9007199254740992L", "VALUE_BELOW_MIN")
        code(s, "n 9007199254740995L", "VALUE_ABOVE_MAX")
        val d = schema("tag n { value type=Dec min=0.10000000000000000001bd }")
        code(d, "n 0.1bd", "VALUE_BELOW_MIN")
    }
    test("exclusive bounds and nonfinite values") {
        val s = schema("tag n { value type=Number exclusiveMin=0 exclusiveMax=1 }")
        s.validate("n 0.5").isValid shouldBe true
        code(s, "n 0", "VALUE_BELOW_MIN")
        code(s, "n 1", "VALUE_ABOVE_MAX")
        code(s, "n NaN", "INCOMPARABLE_BOUND")
        code(s, "n Infinity", "INCOMPARABLE_BOUND")
    }
    test("strings count Unicode code points and patterns match whole string") {
        val s = schema("tag s { value type=String minLength=1 maxLength=1 }")
        s.validate("s \"😀\"").isValid shouldBe true
        code(s, "s \"\"", "STRING_TOO_SHORT")
        code(s, "s ab", "STRING_TOO_LONG")
    }
    test("quantity bounds convert compatible units") {
        val s = schema("tag length { value type=Quantity dimension=Length min=1cm max=10cm }")
        s.validate("length 50mm").isValid shouldBe true
        code(s, "length 5kg", "DIMENSION")
        code(s, "length 0.5cm", "VALUE_BELOW_MIN")
        code(s, "length 1m", "VALUE_ABOVE_MAX")
    }
    test("temperature bounds include offset conversion") {
        val s = schema("tag temp { value type=Quantity dimension=Temperature min=0°C max=100°C }")
        s.validate("temp 32°F").isValid shouldBe true
        s.validate("temp 212°F").isValid shouldBe true
        code(s, "temp 31°F", "VALUE_BELOW_MIN")
    }
    test("unit restriction and currencies never exchange") {
        val s = schema("tag price { value type=Quantity dimension=Currency unit=USD min=1USD }")
        s.validate("price 2USD").isValid shouldBe true
        code(s, "price 2EUR", "UNIT")
        val noUnit = schema("tag price { value type=Quantity min=1USD }")
        code(noUnit, "price 2EUR", "INCOMPARABLE_BOUND")
        invalidSchema("tag price { value type=Quantity min=1USD max=2EUR }")
    }
    test("recursive list map and range item constraints") {
        val s = schema("tag m { value type=Map minCount=1 { keys type=String; items type=List { items type=Int } } }")
        s.validate("m [a=[1 2]]").isValid shouldBe true
        code(s, "m [a=[1 bad]]", "TYPE")
        val range = schema("tag r { value type=Range { items type=Int } }")
        range.validate("r 1.._").isValid shouldBe true
        code(range, "r 1.0..2.0", "TYPE")
    }
    test("grid shape and individual cells") {
        val s = schema("tag plate { value type=Grid rows=2 columns=2 { items type=Number nullable=true } }")
        val tag = Tag("plate").apply { values.add(Grid.fromRows(listOf(listOf<Any?>(1, null), listOf<Any?>(2.0, 3)))) }
        s.validate(tag).isValid shouldBe true
        val bad = Tag("plate").apply { values.add(Grid.fromRows(listOf(listOf<Any?>(1, "bad")))) }
        val result = s.validate(bad)
        result.issues.any { it.code == "GRID_ROWS" } shouldBe true
        result.issues.any { it.code == "TYPE" && it.documentPath.endsWith("cells[0,1]") } shouldBe true
    }
    test("enum equality is numeric and structural after type validation") {
        schema("tag n { value type=Number enum=[1 2] }").validate("n 1.0").isValid shouldBe true
        schema("tag n { value type=List enum=[[1 2]] }").validate("n [1.0 2L]").isValid shouldBe true
        schema("tag q { value type=Quantity enum=[1cm] }").validate("q 10mm").isValid shouldBe true
        code(schema("tag n { value type=Int enum=[1] }"), "n 1.0", "TYPE")
    }
    test("nil enum membership is explicit") {
        val s = schema("tag x { value type=String nullable=true enum=[ok nil] }")
        s.validate("x nil").isValid shouldBe true
        code(schema("tag x { value type=String nullable=true enum=[ok] }"), "x nil", "ENUM")
    }
    test("references resolve forward and preserve placement") {
        val s = schema("""
            definitions {
                tagType Measurement { attribute well ref=Identifier required=true; value type=Number min=0 }
                valueType Identifier type=String minLength=1
            }
            tag measurement ref=Measurement occurs=1.._
        """.trimIndent())
        s.validate("measurement 2 well=A1; measurement 3 well=A2").isValid shouldBe true
        code(s, "measurement -1 well=A1", "VALUE_BELOW_MIN")
    }
    test("references cannot override definitions") {
        invalidSchema("definitions { valueType X type=Int }; tag a { value ref=X min=0 }")
        invalidSchema("definitions { tagType X }; tag a ref=X { value }")
    }
    test("unknown cyclic and unused invalid definitions fail compilation") {
        invalidSchema("tag a ref=Missing")
        invalidSchema("definitions { valueType A ref=B; valueType B ref=A }")
        invalidSchema("definitions { tagType A { children { tag a ref=A } } }")
        invalidSchema("definitions { valueType Unused type=Typo }")
    }
    test("unknown vocabulary duplicates and contradictory constraints fail") {
        for (body in listOf(
            "tag a typo=true", "tag a { attr x }", "tag a; tag a", "tag a { attribute x; attribute x }",
            "tag a { value type=String min=1 }", "tag a { value type=Int pattern=abc }",
            "tag a { value type=Int min=3 max=2 }", "tag a { value type=Int min=1 exclusiveMin=2 }",
            "tag a { value type=Number min=1 exclusiveMax=1 }", "tag a { value type=String pattern=\"[\" }",
            "tag a { value type=Int enum=[] }", "tag a { value type=Int enum=[bad] }",
            "tag a { value type=Int minCount=2 }", "tag a { value type=String rows=2 }",
            "tag a { value type=Quantity dimension=Typo }", "tag a { value type=Quantity unit=Typo }",
            "tag a { value type=Quantity dimension=Length min=1kg }", "tag a { value type=List { keys } }",
            "tag a { children; children }", "tag a additionalChildren=true { children }"
        )) invalidSchema(body)
    }
    test("schema header and version are checked") {
        for (text in listOf("", "schema X", "schema X kds=2", "schema X kds=1; schema Y kds=1", "wrong X kds=1"))
            shouldThrow<KDSchemaException> { KDS.compile(text) }
    }
    test("documents preserve actual root tags and top level cardinality") {
        KD.readDocument("").size shouldBe 0
        KD.readDocument("// empty").size shouldBe 0
        KD.readDocument("root { a }").single().name shouldBe "root"
        KD.readDocument("a; b").map { it.name } shouldBe listOf("a", "b")
        KD.read("").name shouldBe "root"
        KD.read("a").name shouldBe "a"
        KD.read("a; b").children.size shouldBe 2
        val s = schema("tag root { children { tag a } }")
        s.validate("root { a }").isValid shouldBe true
        s.validate(KD.read("root { a }")).isValid shouldBe true
        code(s, "a", "UNEXPECTED_TAG")
    }
    test("snips are never resolved and quoted snip text remains a string") {
        val s = schema("tag \"\" { value type=String }")
        code(s, ".snip(\"missing.kd\")", "UNRESOLVED_SNIP")
        s.validate("\".snip(missing.kd)\"").isValid shouldBe true
    }
    test("calls remain data and are accepted by Call and Any") {
        for (type in listOf("Call", "Any")) {
            val s = schema("tag a { value type=$type }")
            s.validate("a dangerous(1)").isValid shouldBe true
        }
    }
    test("validation is read only") {
        val s = schema("tag a { value type=Quantity dimension=Length min=1cm }")
        val tag = KD.read("a 50mm")
        val before = tag.toString()
        s.validate(tag).isValid shouldBe true
        tag.toString() shouldBe before
    }
    test("diagnostics include data and schema paths and requireValid throws") {
        val r = schema("tag a { attribute x type=Int required=true }").validate("a x=bad")
        r.issues.single().documentPath shouldBe "/a[0]/attributes/x"
        r.issues.single().schemaPath shouldBe "/schema/tag[0]/attribute[0]"
        shouldThrow<KDValidationException> { r.requireValid() }
    }
    test("issue cap and invalid graph limits") {
        val s = schema("tag a occurs=0.._ { value type=Int }")
        val capped = s.validate("a bad; a bad; a bad", ValidationOptions(maxIssues=1))
        capped.issues.size shouldBe 1
        capped.truncated shouldBe true
        val cyclic = Tag("a"); cyclic.children.add(cyclic)
        s.validate(cyclic).issues.single().code shouldBe "CYCLIC_DOCUMENT"
        val deep = Tag("a").apply { children.add(Tag("b").apply { children.add(Tag("c")) }) }
        s.validate(deep, ValidationOptions(maxDepth=1)).issues.single().code shouldBe "DEPTH_LIMIT"
    }
    test("collection cycles are detected even through Any") {
        val list = mutableListOf<Any?>(); list.add(list)
        val t = Tag("a").apply { values.add(list) }
        schema("tag a { value type=Any }").validate(t).issues.single().code shouldBe "CYCLIC_DOCUMENT"
    }
    test("schema can validate its bundled examples") {
        val schemaText = javaClass.getResource("/kds/assay.kds")!!.readText()
        val data = javaClass.getResource("/kds/assay.kd")!!.readText()
        KDS.compile(schemaText).validate(data).isValid shouldBe true
    }
})
