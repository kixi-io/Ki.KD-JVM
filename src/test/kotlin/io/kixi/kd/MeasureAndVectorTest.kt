package io.kixi.kd

import io.kixi.*
import io.kixi.kd.schema.KDS
import io.kixi.kd.schema.KDSchemaException
import io.kixi.uom.Quantity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import java.math.BigDecimal as Dec
import io.kixi.uom.Unit as KiUnit

/**
 * Measures (`0.0042±0.0006µM`) and vectors (`.vec<Float> { 1.0 2.5 }`) in KD text
 * (type bags design, TB7): read as Ki.Core reads them, written back as they read,
 * typed in KDS.
 */
class MeasureAndVectorTest : FunSpec({

    fun value(text: String): Any? = KD.read("t $text").values.single()
    fun roundTrip(v: Any?): Any? = KD.read(KD.read("t x").also { it.values[0] = v }.toString()).values.single()

    context("measures") {
        test("read as Measure.parse reads them, and write back") {
            for (text in listOf(
                "0.0042±0.0006µM", "0.512±0.008", "120±11", "5±1cm:i", "5.0±0.1cm:d", "1.5bd±0.1bd",
                "2L±1L", "0.5f±0.1f", "-3.25±0.5", "20±0.5°C", "-5±0.1cm",
            )) {
                withClue(text) {
                    val v = value(text)
                    v shouldBe Measure.parse(text)
                    KD.read("t $text").toString() shouldContain text
                    roundTrip(v) shouldBe v
                }
            }
        }

        test("+- is the ASCII spelling, and each side may have its own unit") {
            value("0.512+-0.008") shouldBe Measure.parse("0.512±0.008")
            value("5cm±1mm") shouldBe Measure.parse("5±0.1cm")
            value("5cm+-1mm") shouldBe Measure.parse("5±0.1cm")
        }

        test("measures among other values, in attributes, lists and maps") {
            val t = KD.read("""assay 0.512±0.008 ic50=0.0042±0.0006µM reps=[1±0.1 2±0.2] by=[a=3±1] n=5""")
            t.values.single() shouldBe Measure.parse("0.512±0.008")
            t["ic50"] shouldBe Measure.parse("0.0042±0.0006µM")
            t["reps"] shouldBe listOf(Measure.parse("1±0.1"), Measure.parse("2±0.2"))
            (t["by"] as Map<*, *>)["a"] shouldBe Measure.parse("3±1")
            t["n"] shouldBe 5
            KD.read(t.toString()) shouldBe t
        }

        test("numbers, ranges and versions are unchanged") {
            value("5") shouldBe 5
            value("-5") shouldBe -5
            value("1..5").shouldBeInstanceOf<Range<*>>()
            value("1.2.3") shouldBe Version.parse("1.2.3")
            value("5cm") shouldBe Quantity(Dec(5), KiUnit.cm)
            KD.read("t 5 -3").values shouldBe listOf(5, -3)
        }

        test("bad measures are errors") {
            for (bad in listOf("5±", "5±x", "5±-1", "5cm±1g", "5±2024/1/1", "5+-")) {
                withClue(bad) { shouldThrow<KDParseException> { KD.read("t $bad") } }
            }
        }
    }

    context("vectors") {
        test("read and write back with their element type") {
            for (text in listOf(
                ".vec<Float> { 1.0 2.5 3.1 }", ".vec<Int> { 3 1 4 1 5 }", ".vec<Long> { 9000000000 -1 }",
                ".vec<Double> { 0.1 1.0E-5 -2.5 }", ".vec<Dec> { 0.1 2 -3.25 }", ".vec<Int> { }",
            )) {
                withClue(text) {
                    val v = value(text)
                    v shouldBe Vector.parse(text)
                    (v as Vector<*>).toString() shouldBe text
                    roundTrip(v) shouldBe v
                }
            }
        }

        test("unsuffixed numbers take the declared type exactly; suffixed ones must be of it") {
            value(".vec<Dec> { 0.1 68.2 }") shouldBe Vector.ofDecs(Dec("0.1"), Dec("68.2"))
            value(".vec<Float> { 1 2.5 }") shouldBe Vector.ofFloats(1f, 2.5f)
            value(".vec<Long> { 1 2L }") shouldBe Vector.ofLongs(1, 2)
            value(".vec<Int> { 0xFF 1_000 }") shouldBe Vector.ofInts(255, 1000)
            shouldThrow<KDParseException> { value(".vec<Int> { 1.5 }") }.message shouldContain ".vec<Int>"
            shouldThrow<KDParseException> { value(".vec<Int> { 1L }") }
            shouldThrow<KDParseException> { value(".vec<Double> { 1bd }") }
        }

        test("lines, commas and comments separate the numbers") {
            val t = KD.read(
                """
                spectrum nm=.vec<Int> {
                    400, 410    # visible
                    420 /* the last */ 430 // done
                } absorbance=.vec<Double> { 0.1 0.2 0.3 0.4 }
                """.trimIndent()
            )
            t["nm"] shouldBe Vector.ofInts(400, 410, 420, 430)
            t["absorbance"] shouldBe Vector.ofDoubles(0.1, 0.2, 0.3, 0.4)
            KD.read(t.toString()) shouldBe t
        }

        test("bad vectors are errors") {
            for (bad in listOf(".vec<String> { }", ".vec<Int?> { 1 }", ".vec<Int> { \"a\" }", ".vec<Int> { 1 2",
                ".vec<Int> 1 2", ".vec<Quantity> { 1cm }", ".vec<Int> { nil }", ".vec<Int { 1 }")) {
                withClue(bad) { shouldThrow<KDParseException> { KD.read("t $bad") } }
            }
        }
    }

    context("KDS") {
        test("Measure and Vector are type names") {
            val s = KDS.compile("schema T kds=1 { tag m occurs=0..1 { value type=Measure }; tag v occurs=0..1 { value type=Vector } }")
            s.validate("m 0.512±0.008").isValid shouldBe true
            s.validate("m 0.512").isValid shouldBe false
            s.validate("v .vec<Int> { 1 2 }").isValid shouldBe true
            s.validate("v [1 2]").isValid shouldBe false
        }

        test("a measure's dimension and unit, a vector's count") {
            val s = KDS.compile(
                "schema T kds=1 { tag m occurs=0..1 { value type=Measure dimension=Concentration }; tag u occurs=0..1 { value type=Measure unit=cm }; " +
                    "tag v occurs=0..1 { value type=Vector minCount=2 maxCount=3 } }"
            )
            s.validate("m 0.0042±0.0006µM").isValid shouldBe true
            s.validate("m 5±1cm").issues.map { it.code } shouldBe listOf("DIMENSION")
            s.validate("m 5±1").issues.map { it.code } shouldBe listOf("DIMENSION")
            s.validate("u 5±1cm").isValid shouldBe true
            s.validate("u 50±10mm").issues.map { it.code } shouldBe listOf("UNIT")
            s.validate("v .vec<Int> { 1 2 }").isValid shouldBe true
            s.validate("v .vec<Int> { 1 }").isValid shouldBe false
            s.validate("v .vec<Int> { 1 2 3 4 }").isValid shouldBe false
            shouldThrow<KDSchemaException> { KDS.compile("schema T kds=1 { tag a { value type=Vector dimension=Length } }") }
        }
    }
})
