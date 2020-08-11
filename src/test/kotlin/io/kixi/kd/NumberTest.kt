package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

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
    fun testLongs() {}

    @Test
    fun testFloats() {}

    @Test
    fun testDoubles() {}

    @Test
    fun testDecimals() {}
}