package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal as Decimal

class NumberTest {

    val root = KD.readResource("number_tests.kd")

    @Test
    fun testInts() {
        log(root)
        val ints = root.getChild("Int")!!
        assertEquals("[0, 5, -5]", ints.getChild("basic")!!.values.toString())
        assertEquals(5, ints.getChild("int4")!!.value) // 0b101
        assertEquals(35, ints.getChild("int5")!!.value) // 0x23
    }

    @Test
    fun testLongs() {
        val longs = root.getChild("Long")!!
        assertEquals("[0, 5, -5]", longs.getChild("basic")!!.values.toString())
        assertEquals(1000L, longs.getChild("long1")!!.value)
        assertEquals(1000L, longs.getChild("long2")!!.value) // 1_000L
        assertEquals(3904857398753453453,
                longs.getChild("long3")!!.value) // 1_000L
    }

    @Test
    fun testFloats() {
        val floats = root.getChild("Float")!!
        assertEquals("[0.2, 0.3, 0.521]", floats.getChild("basic")!!.values.toString())
        assertEquals(0.253958f, floats.getChild("float1")!!.value)
        assertEquals(0.253958f, floats.getChild("float2")!!.value) // 0.253_958f
        assertEquals(1.2345678E7f, floats.getChild("float3")!!.value)
    }

    @Test
    fun testDoubles() {
        val doubles = root.getChild("Double")!!
        assertEquals("[0.2, 0.3, 0.521]", doubles.getChild("basic")!!.values.toString())
        assertEquals(0.253958, doubles.getChild("double1")!!.value)
        assertEquals(0.253958, doubles.getChild("double2")!!.value) // 0.253_958f
        assertEquals(1.2345678E7, doubles.getChild("double3")!!.value)
    }

    @Test
    fun testDecimals() {
        val decimals = root.getChild("Decimal")!!
        assertEquals("[0.2, 0.3, 0.521]", decimals.getChild("basic")!!.values.toString())
        assertEquals(Decimal(0), decimals.getChild("decimal1")!!.value)
        assertEquals(Decimal("11.111111"), decimals.getChild("decimal2")!!.value) // 0.253_958f
        assertEquals(Decimal("143523.521535"), decimals.getChild("decimal3")!!.value)
        assertEquals(Decimal("234535.3453453453454345345341242343"), decimals.getChild("decimal4")!!.value)
        assertEquals(Decimal("982342462234.163932352"), decimals.getChild("decimal5")!!.value)
    }
}