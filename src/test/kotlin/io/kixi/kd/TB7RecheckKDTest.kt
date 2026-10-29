package io.kixi.kd

import io.kixi.Ki
import io.kixi.Measure
import io.kixi.Type
import io.kixi.Vector
import io.kixi.kd.schema.KDS
import io.kixi.uom.Currency
import io.kixi.uom.Quantity
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal as Dec
import java.util.Random
import io.kixi.uom.Unit as KiUnit

/**
 * TB7 re-check (2026-10-29), Ki.KD: what Ki.Core writes for a measure or a vector, KD reads
 * back equal (every registered unit, every number type, in every position a value takes),
 * and KDS Measure and Vector.
 */
class TB7RecheckKDTest : FunSpec({

    val units: List<KiUnit> = KiUnit.allUnits().filter { it !is Currency }.distinct()

    fun report(name: String, problems: List<String>) {
        if (problems.isNotEmpty()) println("[$name] ${problems.size} problems; first: ${problems.take(10).joinToString(" | ")}")
        withClue(problems.take(10).joinToString("\n")) { problems.size shouldBe 0 }
    }

    fun number(rnd: Random, type: Int, nonNegative: Boolean): Number {
        val mant = rnd.nextInt(2000) - if (nonNegative) 0 else 1000
        return when (type) {
            0 -> mant
            1 -> mant.toLong() * (if (rnd.nextBoolean()) 1L else 1_000_000_000L)
            2 -> Dec.valueOf(mant.toLong(), rnd.nextInt(4)).toFloat()
            3 -> Dec.valueOf(mant.toLong(), rnd.nextInt(9) - 2).toDouble()
            else -> Dec.valueOf(mant.toLong(), rnd.nextInt(8) - 2)
        }
    }

    fun randomMeasure(rnd: Random, unit: KiUnit?): Measure {
        val type = rnd.nextInt(5)
        val v = number(rnd, type, false)
        val u = number(rnd, type, true)
        return if (unit == null) Measure.of(v, u) else Measure.of(Quantity(v, unit), Quantity(u, unit))
    }

    test("K1 every measure Ki.Core writes reads back equal, alone, in an attribute, a list, a map, before a comment") {
        val rnd = Random(41)
        val problems = ArrayList<String>()
        var n = 0
        for (unit in units + listOf<KiUnit?>(null)) repeat(25) {
            val m = randomMeasure(rnd, unit)
            val text = m.toString()
            n++
            val forms = listOf(
                "t $text" to { t: Tag -> t.values.single() },
                "t a=$text b=1" to { t: Tag -> t["a"] },
                "t [$text, $text]" to { t: Tag -> (t.values.single() as List<*>)[1] },
                "t [k=$text]" to { t: Tag -> (t.values.single() as Map<*, *>)["k"] },
                "t $text # note" to { t: Tag -> t.values.single() },
                "t $text// note" to { t: Tag -> t.values.single() },
                "t ${text.replace("±", "+-")}" to { t: Tag -> t.values.single() },
            )
            for ((doc, get) in forms) {
                try {
                    val back = get(KD.read(doc))
                    if (back != m) problems += "$doc -> $back (${back?.let { it::class.simpleName }})"
                } catch (e: Exception) { problems += "$doc: ${e.message?.lineSequence()?.first()}" }
            }
            // and the tag a measure is in, written and read again
            try {
                val t = KD.read("t a=$text")
                if (KD.read(t.toString()) != t) problems += "tag round trip ${t}"
            } catch (e: Exception) { problems += "tag $text: ${e.message?.lineSequence()?.first()}" }
        }
        println("[K1] $n measures x 8 forms")
        report("K1", problems)
    }

    test("K2 measures of time, written as Ki.Core writes them and as people do (F: 12±1min:i)") {
        val problems = ArrayList<String>()
        fun u(s: String) = KiUnit.getUnit(s)!!
        val cases = listOf(
            Measure.of(Quantity(12, u("min")), Quantity(1, u("min"))), Measure.of(Quantity(Dec(12), u("min")), Quantity(Dec(1), u("min"))),
            Measure.of(Quantity(5L, u("s")), Quantity(1L, u("s"))), Measure.of(Quantity(1.5, u("h")), Quantity(0.25, u("h"))),
            Measure.of(Quantity(2, u("day")), Quantity(1, u("day"))), Measure.of(Quantity(Dec("2.5"), u("ms")), Quantity(Dec("0.5"), u("ms"))),
            Measure.of(Quantity(30, u("ns")), Quantity(2, u("ns"))), Measure.of(Quantity(30f, u("µs")), Quantity(2f, u("µs"))),
        )
        for (m in cases) {
            val text = m.toString()
            try { val back = KD.read("t $text").values.single(); if (back != m) problems += "$text -> $back" } catch (e: Exception) { problems += "$text: ${e.message?.lineSequence()?.first()}" }
        }
        for (text in listOf("12±1min", "5±1s", "1.5±0.25h", "2±1day", "12min±30s")) {
            try { val back = KD.read("t $text").values.single(); if (back != Measure.parse(text)) problems += "$text -> $back" } catch (e: Exception) { problems += "$text: ${e.message?.lineSequence()?.first()}" }
        }
        report("K2", problems)
    }

    test("K3 big whole numbers: measures and Dec, Double and Float vectors; Int and Long still refuse") {
        val problems = ArrayList<String>()
        val cm = KiUnit.getUnit("cm")!!
        val ms = listOf(
            Measure.of(Quantity(Dec("-1E+20"), cm), Quantity(Dec("1E+19"), cm)), Measure.of(Dec("123456789012345678901234567890"), Dec.ONE),
            Measure.of(Dec("-123456789012345678901234567890"), Dec("1E+25")), -Measure.of(Long.MIN_VALUE, 1L),
            -Measure.of(Quantity(Long.MIN_VALUE, cm), Quantity(1L, cm)),
        )
        for (m in ms) try { val b = KD.read("t $m").values.single(); if (b != m) problems += "$m -> $b" } catch (e: Exception) { problems += "$m: ${e.message?.lineSequence()?.first()}" }
        val vs = listOf(Vector.ofDecs(Dec("-1E+19"), Dec("1E+30")), Vector.ofDecs(Dec("-99999999999999999999.5")),
            Vector.ofDoubles(1e20, -1e300), Vector.ofFloats(1e20f, -3e38f))
        for (v in vs) try { val b = KD.read("t $v").values.single(); if (b != v) problems += "$v -> $b" } catch (e: Exception) { problems += "$v: ${e.message?.lineSequence()?.first()}" }
        for (bad in listOf(".vec<Int> { 100000000000000000000 }", ".vec<Long> { -100000000000000000000 }", ".vec<Float> { 1${"0".repeat(40)} }")) {
            try { KD.read("t $bad"); problems += "$bad was read" } catch (e: KDParseException) { } catch (e: Exception) { problems += "$bad: ${e::class.simpleName} ${e.message}" }
        }
        report("K3", problems)
    }

    test("K4 every vector Ki.Core writes reads back equal; KD and Vector.parse agree") {
        val rnd = Random(42)
        val problems = ArrayList<String>()
        val types = listOf(Type.Int, Type.Long, Type.Float, Type.Double, Type.Dec)
        repeat(1500) {
            val t = types[rnd.nextInt(5)]
            val xs: List<Number> = List(rnd.nextInt(6)) {
                val mant = rnd.nextInt(2000) - 1000
                when (t) {
                    Type.Int -> mant
                    Type.Long -> mant * 10_000_000_000L
                    Type.Float -> if (rnd.nextInt(15) == 0) -0.0f else Dec.valueOf(mant.toLong(), rnd.nextInt(14) - 4).toFloat()
                    Type.Double -> if (rnd.nextInt(15) == 0) -0.0 else Dec.valueOf(mant.toLong(), rnd.nextInt(30) - 10).toDouble()
                    else -> Dec.valueOf(mant.toLong(), rnd.nextInt(40) - 25)
                }
            }
            val v = Vector.of(t, xs)
            val text = v.toString()
            try {
                val k = KD.read("t $text").values.single()
                if (k != v) problems += "$text -> $k"
                if (k != Vector.parse(text)) problems += "KD and Vector.parse differ on $text"
            } catch (e: Exception) { problems += "$text: ${e.message?.lineSequence()?.first()}" }
        }
        report("K4", problems)
    }

    test("K5 KD and Vector.parse read the same text the same way (commas, comments, suffixes, signs)") {
        val problems = ArrayList<String>()
        for (text in listOf(".vec<Int> { 1, 2,3 }", ".vec<Int> { 1 /* x */ 2 }", ".vec<Int> {\n 1 # c\n 2 // d\n}", ".vec<Long> { 1L 2 }",
            ".vec<Double> { 1d -2.5 +3 }", ".vec<Float> { -0.0 1f }", ".vec<Dec> { 1.50bd -2 }", ".vec<Double> { 1e3 -1.5E-3 }", ".vec<Int> { +5 -0 }")) {
            val kd = try { KD.read("t $text").values.single() } catch (e: Exception) { "KD error: ${e.message?.lineSequence()?.first()}" }
            val core = try { Vector.parse(text) } catch (e: Exception) { "Vector.parse error: ${e.message}" }
            if (kd != core) problems += "$text: KD $kd, Vector.parse $core"
        }
        report("K5", problems)
    }

    test("K6 KDS: enum of measures across dimensions is false, not an error; unit and dimension; bounds refused; Any") {
        val problems = ArrayList<String>()
        val s = KDS.compile("schema T kds=1 { tag m occurs=0..1 { value type=Measure enum=[5±1cm 3±1] }; tag a occurs=0..1 { value type=Any } }")
        try { if (s.validate("m 5±1g").isValid) problems += "5±1g in enum [5±1cm 3±1]" } catch (e: Exception) { problems += "enum across dimensions: ${e::class.simpleName} ${e.message}" }
        try { if (!s.validate("m 50±10mm").isValid) problems += "50±10mm not in enum [5±1cm]" } catch (e: Exception) { problems += "enum: ${e.message}" }
        try { if (!s.validate("a 5±1cm").isValid || !s.validate("a .vec<Int> { 1 }").isValid) problems += "type=Any refuses a measure or vector" } catch (e: Exception) { problems += "Any: ${e.message}" }
        try { KDS.compile("schema T kds=1 { tag m { value type=Measure min=1±0 } }"); problems += "min on a Measure compiled" } catch (e: Exception) { }
        report("K6", problems)
    }

    test("K7 measures written with quantity scientific notation, and in grids") {
        val problems = ArrayList<String>()
        for (text in listOf("5.5e(-7)m±1e(-8)m", "5.5en7m±1en8m")) {   // 5.5e(-7) alone is not a KD number: the e(…) form belongs to quantities
            try { val b = KD.read("t $text").values.single(); if (b !is Measure) problems += "$text -> $b" } catch (e: Exception) { problems += "$text: ${e.message?.lineSequence()?.first()}" }
        }
        try {
            val r = io.kixi.Range(Measure.parse("1.0±0.1"), Measure.parse("2.0±0.1"))
            val b = KD.read("t $r").values.single()
            if (b != r) problems += "range $r -> $b"
        } catch (e: Exception) { problems += "range of measures: ${e.message?.lineSequence()?.first()}" }
        try {
            val g = KD.read("t .grid( 5±1 6±1cm\n 7±1 8±1 )").values.single() as io.kixi.Grid<*>
            if (g[0, 0] != Measure.parse("5±1")) problems += "grid cell ${g[0, 0]}"
        } catch (e: Exception) { problems += "grid: ${e.message?.lineSequence()?.first()}" }
        report("K7", problems)
    }
})
