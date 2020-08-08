package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TagTest {

    @Test fun testFind() {
        val root = KD.read("""
            fee purple size =2 {
                child1 blue size =3
                child3 green size=6 {
                    grandchild red size=2
                    grandchild red size=7
                }
            }
            fi red size=6
        """.trimIndent())

        // Find a tag that is red with a size > 5
        assertEquals(
                """fi "red" size=6""".trim(),
                root.findChild {
                    it.value == "red" && it.getAttribute<Int>("size") > 5
                }.toString()
        )

        // Find all tags that are red with sizes > 5
        assertEquals("""[fi "red" size=6, grandchild "red" size=7]""",
                root.findChildren {
                    it.value == "red" && it.getAttribute<Int>("size") > 5
                }.toString()
        )
    }

    @Test fun gridTest() {
        val intRows = KD.read("""
                1 2 3
                4 5 6
        """).getChildrenValues<Int>()
        assertEquals(6, intRows[1][2])
    }
}