package io.kixi.kd

import io.kixi.*
import io.kixi.uom.Quantity
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.math.BigDecimal as Dec
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

/**
 * Typed grids and date ranges in KD text: every cell type survives a write and re-read
 * with its value, number type and element type; unsuffixed numbers take a typed grid's
 * numeric type; mismatches and unknown type names are errors; date and time values can
 * start a range.
 */
class TypedGridAndDateRangeTest : FunSpec({

    fun value(text: String): Any? = KD.read("t $text").values.single()
    fun grid(text: String): Grid<*> = value(text) as Grid<*>
    fun cells(g: Grid<*>): List<Any?> = (0 until g.height).flatMap { y -> (0 until g.width).map { x -> g[x, y] } }
    fun kiType(g: Grid<*>): String? = g.elementType?.let { Type.of(it)?.name }

    context("every cell type survives KD text with its value, number type and element type") {
        val cases = listOf(
            "Int" to "1 -2", "Long" to "1L 9999999999L", "Float" to "1.5f -2.25f", "Double" to "1.5 -2.25",
            "Dec" to "1.5bd 123456789012345678901234567890.123456789bd", "Bool" to "true false", "Char" to "'a' 'b'",
            "String" to "\"a b\" \"c\"", "Date" to "2020/1/1 2020/12/31", "LocalDateTime" to "2024/3/15@9:00 2024/3/15@17:30:15",
            "ZonedDateTime" to "2024/3/15@9:00-09:00 2024/3/15@9:00+01:00", "KiTZDateTime" to "2024/3/15@9:00-JP/JST 2024/3/15@10:00-US/PST",
            "Duration" to "1h 0:1:59.5", "Version" to "1.0.0 2.0.0-beta", "Quantity" to "10µM 3.3µM",
            "Id" to ".id(01J9A0X5K2M7N3P8Q1R6T4V9AB) .id(01J9A0X5K2M7N3P8Q1R6T4V9AC)", "Ref" to ".ref(01J9A0X5K2M7N3P8Q1R6T4V9AB) .ref(01J9A0X5K2M7N3P8Q1R6T4V9AC)",
            "URL" to "<https://kixi.io/a> <https://kixi.io/b>", "Email" to "a@kixi.io b@kixi.io", "GeoPoint" to ".geo(35.6762, 139.6503) .geo(-33.8688, 151.2093)",
        )
        for ((name, literals) in cases) {
            test(name) {
                for (source in listOf(".grid { $literals; $literals }", ".grid<$name> { $literals; $literals }", ".grid<$name?> { $literals; nil nil }")) {
                    val g = grid(source)
                    val text = g.toString()
                    withClue("$source written as\n$text") {
                        val back = grid(text)
                        kiType(back) shouldBe name
                        kiType(g) shouldBe name
                        back.elementNullable shouldBe g.elementNullable
                        back.width shouldBe g.width
                        back.height shouldBe g.height
                        cells(back) shouldBe cells(g)
                        cells(back).map { it?.javaClass } shouldBe cells(g).map { it?.javaClass }
                    }
                }
            }
        }
    }

    context("an unsuffixed number takes the typed grid's numeric type, exactly from its text") {
        test("Dec") {
            cells(grid(".grid<Dec> { 68.2 1 0x10 1_000.5 }")) shouldBe listOf(Dec("68.2"), Dec("1"), Dec("16"), Dec("1000.5"))
            // more digits than a Double holds
            cells(grid(".grid<Dec> { 123456789012345678901234567890.123456789 }")) shouldBe listOf(Dec("123456789012345678901234567890.123456789"))
        }
        test("Long, Double and Float") {
            cells(grid(".grid<Long> { 1 2 99999999999 }")) shouldBe listOf(1L, 2L, 99_999_999_999L)
            cells(grid(".grid<Double> { 1 2.5 }")) shouldBe listOf(1.0, 2.5)
            cells(grid(".grid<Float> { 1.5 2 0.1 }")) shouldBe listOf(1.5f, 2f, 0.1f)
        }
        test("Int takes whole numbers in range") {
            cells(grid(".grid<Int> { 1 2.0 -3 }")) shouldBe listOf(1, 2, -3)
        }
        test("suffixed numbers of the declared type, and nil in a nullable grid, are fine") {
            cells(grid(".grid<Dec?> { 1.5bd nil }")) shouldBe listOf(Dec("1.5"), null)
            cells(grid(".grid<Int?> { 1 - }")) shouldBe listOf(1, null)
            cells(grid(".grid<Long> { 5L 6 }")) shouldBe listOf(5L, 6L)
        }
        test("Number and Any keep each number's own type") {
            cells(grid(".grid<Number> { 1 2L 1.5 }")).map { it!!::class } shouldBe listOf(Int::class, Long::class, Double::class)
        }
        test("the v0.2 plate signal grid holds Decs") {
            cells(grid(".grid<Dec> { 68.2 66.7; 35.4 56.0 }")).all { it is Dec } shouldBe true
        }
    }

    context("mismatches and unknown type names are errors") {
        fun fails(text: String, message: String) {
            shouldThrow<KDParseException> { value(text) }.message shouldContain message
        }
        test("cells of another type") {
            fails(".grid<Int> { 1 \"a\" }", "'\"a\"' is String, not Int")
            fails(".grid<Int> { 5L }", "'5L' is Long, not Int")
            fails(".grid<Dec> { 1.5f }", "'1.5f' is Float, not Dec")
            fails(".grid<Date> { 1 }", "'1' is Int, not Date")
            fails(".grid<Quantity> { 10µM 5 }", "'5' is Int, not Quantity")
        }
        test("numbers an Int or Long cannot hold") {
            fails(".grid<Int> { 1.5 }", "'1.5' is not a whole number, so it cannot be Int")
            fails(".grid<Int> { 99999999999 }", "'99999999999' does not fit in Int")
            fails(".grid<Long> { 1.25 }", "'1.25' is not a whole number, so it cannot be Long")
        }
        test("nil in a non-nullable grid") {
            fails(".grid<Int> { 1 nil }", "'nil' in .grid<Int>: Grid<Int> cannot contain nil values (use .grid<Int?> for nullable)")
            fails(".grid<Int> { 1 - }", "'-' in .grid<Int>")
            fails(".grid<Ref> { nil }", "use .grid<Ref?> for nullable")
        }
        test("unknown type names") {
            fails(".grid<Itn> { 1 2 }", "Unknown grid element type 'Itn'")
        }
        test("type names older versions wrote still read") {
            kiType(grid(".grid<BigDecimal> { 1.5 }")) shouldBe "Dec"
            kiType(grid(".grid<Boolean> { true }")) shouldBe "Bool"
            kiType(grid(".grid<Character> { 'a' }")) shouldBe "Char"
            kiType(grid(".grid<LocalDate> { 2020/1/1 }")) shouldBe "Date"
            kiType(grid(".grid<Integer> { 1 }")) shouldBe "Int"
        }
    }

    context("date and time values can start a range") {
        val d1 = LocalDate.of(2020, 1, 1)
        val d2 = LocalDate.of(2020, 12, 31)
        test("Date, all four bounds and open ends") {
            value("2020/1/1..2020/12/31") shouldBe Range(d1, d2, Range.Bound.Inclusive)
            value("2020/1/1..<2021/1/1") shouldBe Range(d1, LocalDate.of(2021, 1, 1), Range.Bound.ExclusiveEnd)
            value("2020/1/1<..2020/12/31") shouldBe Range(d1, d2, Range.Bound.ExclusiveStart)
            value("2020/1/1<..<2020/12/31") shouldBe Range(d1, d2, Range.Bound.Exclusive)
            value("2020/1/1.._") shouldBe Range(d1, null, Range.Bound.Inclusive)
            value("_..2020/12/31") shouldBe Range(null, d2, Range.Bound.Inclusive)
            @Suppress("UNCHECKED_CAST")
            val r = value("2020/1/1..2020/12/31") as Range<LocalDate>
            (LocalDate.of(2020, 6, 1) in r) shouldBe true
            (LocalDate.of(2021, 1, 1) in r) shouldBe false
        }
        test("LocalDateTime, ZonedDateTime and KiTZDateTime") {
            value("2024/3/15@9:00..2024/3/15@17:00") shouldBe
                Range(LocalDateTime.of(2024, 3, 15, 9, 0), LocalDateTime.of(2024, 3, 15, 17, 0), Range.Bound.Inclusive)
            (value("2024/3/15@9:00-09:00..2024/3/15@17:00-09:00") as Range<*>).start shouldBe value("2024/3/15@9:00-09:00")
            val kitz = value("2024/3/15@9:00-JP/JST..<2024/3/15@17:00-JP/JST") as Range<*>
            kitz.start shouldBe KiTZDateTime(2024, 3, 15, 9, 0, 0, KiTZ.JP_JST)
            kitz.bound shouldBe Range.Bound.ExclusiveEnd
        }
        test("as attributes, and through text") {
            KD.read("assay period=2020/1/1..2020/12/31").attributes[NSID("period")] shouldBe Range(d1, d2, Range.Bound.Inclusive)
            for (text in listOf("2020/1/1..2020/12/31", "2020/1/1<..<2021/1/1", "2020/1/1.._", "2024/3/15@9:00..2024/3/15@17:00:30.5",
                    "2024/3/15@9:00-JP/JST..<2024/3/15@17:00-JP/JST")) {
                val r = value(text)
                withClue("$text written as $r") { value(r.toString()) shouldBe r }
            }
        }
    }
})
