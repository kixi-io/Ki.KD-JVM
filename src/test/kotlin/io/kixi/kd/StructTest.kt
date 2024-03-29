package io.kixi.kd

import io.kixi.core.Call
import io.kixi.core.Ki
import io.kixi.core.NSID
import io.kixi.core.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StructTest {

    @Test fun lists() {
        assertEquals(KD("[1]"), listOf<Int>(1))
        assertEquals(KD("[1, 2, 3]"), listOf<Int>(1,2,3))
        assertEquals(KD("[]"), listOf<Any>())
    }

    @Test fun maps() {
        assertEquals(KD("""[name="fred"]"""), mapOf<String, String>("name" to "fred"))
        assertEquals(KD("""[name="fred", age=52]"""), mapOf<String,Any>("name" to
                "fred", "age" to 52))
        assertEquals(KD("[=]"), mapOf<Any,Any>())
    }

    @Test fun calls() {
        assertEquals(KD("rgb()"), Call("rgb"))

        var call = Call("rgb")
        call.values.add(1);
        call.values.add(2);
        call.values.add(3);
        assertEquals(KD("rgb(1,2,3)"), call)
        assertEquals(KD("rgb(1 2 3)"), call)

        call[NSID("alpha")] = 4

        assertEquals(KD("rgb(1, 2, 3, alpha=4)"), call)
        assertEquals(KD("rgb(1 2 3 alpha=4)"), call)
        // assertEquals(KD("[]"), listOf<Any>())
    }

    @Test fun grid() {
        val intGrid = KD.read("""
            1 2 3
            4 5 6
        """.trimIndent()).getChildrenValues<Int>()

        assertEquals("""
            [1, 2, 3]
            [4, 5, 6]
        """.trimIndent(), "${intGrid[0]}\n${intGrid[1]}")
    }

    @Test fun structsFromFile() {
        val root = KD.readResource("structure_tests.kd")
        // log(root)
        val mapTag = root.findChild("fancyMap")!!
        assertEquals("[a, b, c]", (mapTag.getAttribute("map") as Map<*,*>)
                ["chars"].toString())
    }
}
