package io.kixi.kd

import io.kixi.core.Ki
import io.kixi.core.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StringTest {

    val root = KD.readResource("string_tests.kd")

    /**
     * Test basic strings
     */
    @Test fun basicStrings() {
        assertEquals("Foo", KD.read("""
            "Foo"
        """.trimIndent()).value)
        assertEquals("Foo", KD.read("""
            @"Foo"
        """.trimIndent()).value)
        assertEquals("Foo", KD.read("""
            `Foo`
        """.trimIndent()).value)
    }

    @Test fun bareStrings() {
        assertEquals("Foo\\Bar", KD.read("""
            @"Foo\Bar"
        """.trimIndent()).value)
        assertEquals("Foo\\Bar", KD.read("""
            `Foo\Bar`
        """.trimIndent()).value)
    }

    /**
     * Some of the "expected" values look odd because, unlike KD and Swift, Kotlin's
     * string blocks don't trim line prefixed white-space with the indentation of the
     * closing quotes. This gives you much better control than Kotlin's .trimIndent().
     */
    @Test fun basicBlocks() {
        assertEquals("Line1", root.getChild("text1")!!.value)
        assertEquals("Line1", root.getChild("text2")!!.value)

        assertEquals("""    Line1
        Line2
    Line3""", root.getChild("text3")!!.value)

        assertEquals("""
        Line1
            Line2
        Line3
        """.trimIndent(),
        root.getChild("text4")!!.value)

        assertEquals("""
        Line1
            Line2
        Line3
        """.trimIndent(),
                root.getChild("text5")!!.value)
    }

    @Test fun rawBlocks() {
        assertEquals("""
            \a\path\readme.md
            """.trimIndent(),
            root.getChild("text6")!!.value
        )

        assertEquals("""
                slash: \
                other_escapes: \t\f\n
                \a\path\readme.md
            """.trimIndent(),
            root.getChild("text7")!!.value
        )

        assertEquals(
                root.getChild("text7")!!.value,
                root.getChild("text8")!!.value
        )
    }
}
