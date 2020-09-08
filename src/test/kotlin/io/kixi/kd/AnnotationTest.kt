package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AnnotationTest {

    @Test fun testToString() {
        val a1 = Annotation("Capybara")
        assertEquals("@Capybara", a1.toString())

        a1.values.addAll(listOf(2,4,6))
        assertEquals("@Capybara(2 4 6)", a1.toString())

        a1.attributes.putAll(mapOf<NSID, Any?>(Pair(NSID("funny"), true), Pair(NSID("name"), "Harry")))
        assertEquals("@Capybara(2 4 6 funny=true name=\"Harry\")", a1.toString())

        a1.values.clear()
        assertEquals("@Capybara(funny=true name=\"Harry\")", a1.toString())
    }

    @Test fun testSimpleAnnotation() {
        val annoDef = """
            @Test
            date "some data"
        """.trimIndent()

        val anno = KD.read(annoDef)
        assertEquals(annoDef, anno.toString())
    }

    @Test fun testValuesAnnotation() {
        val annoDef = """
            @Test(1 2 3)
            date "some data"
        """.trimIndent()

        val anno = KD.read(annoDef)
        assertEquals(annoDef, anno.toString())
    }

    @Test fun testAttsAnnotation() {
        val annoDef = """
            @Test(working=true luck="good")
            date "some data"
        """.trimIndent()

        val anno = KD.read(annoDef)
        assertEquals(annoDef, anno.toString())
    }

    @Test fun testValuesAttsAnnotation() {
        val annoDef = """
            @Test(1 2 3 working=true luck="good")
            date "some data"
        """.trimIndent()

        val anno = KD.read(annoDef)
        assertEquals(annoDef, anno.toString())
    }
}