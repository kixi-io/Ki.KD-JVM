package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StructTest {

    @Test fun testGrid() {
        val intGrid = KD.read("""
            1 2 3
            4 5 6
        """.trimIndent()).getChildrenValues<Int>()

        assertEquals("""
            [1, 2, 3]
            [4, 5, 6]
        """.trimIndent(), "${intGrid[0]}\n${intGrid[1]}")
    }

    @Test fun testStructsFromFile() {
        val root = KD.readResource("structure_tests.kd")
        log(root)
        val mapTag = root.findChild("fancyMap")!!
        assertEquals("[a, b, c]", (mapTag.getAttribute("map") as Map<*,*>)
                ["chars"].toString())
    }
}