package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KDBasicTest {

    @Test fun testTagWithValuesAndAttributes() {
        assertEquals("myTag", KD.read("myTag").toString())
        assertEquals("myTag 123", KD.read("myTag 123").toString())
        assertEquals("myTag name=\"Tagaroo\"", KD.read("myTag name=\"Tagaroo\"").toString())
        assertEquals("myTag 123 name=\"Tagaroo\"", KD.read("myTag 123 name=\"Tagaroo\"").toString())
        assertEquals("personForID [253235162=\"Jess\"]",
                KD.read("personForID [253_23_5162=\"Jess\"]").toString())
    }

    @Test fun testDataStructures() {
        var list = Ki.format(
                listOf(
                        Ki.parseDuration("2s"),
                        listOf(Ki.parseDuration("3s"), Ki.parseDuration("4s"))
                )
        )
        assertEquals("[2s, [3s, 4s]]", list)

        var map = Ki.format(
                mapOf(
                        Pair("time", Ki.parseDuration("2s")),
                        Pair("date", Ki.parseLocalDate("1970/5/25"))
                )
        )
        assertEquals("[\"time\"=2s, \"date\"=1970/5/25]", map)
    }
}