package io.kixi.kd.schema

import io.kixi.TypeDef
import io.kixi.kd.KD
import io.kixi.kd.NSID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TagDefTest {

    @Test
    fun basic() {
        var tagDef = TagDef(
            TagDef.EMPTY_ANNOS,
            StringMatcher.EMPTY,
            StringMatcher.ID("Foo"),
            listOf<ValueDef>(
                ValueDef(TypeDef.String),
                ValueDef(TypeDef.Int, 5),
                ValueDef(TypeDef.Bool, true)
            )
        )

        var tag = KD.read("""
            Foo "HelloKDS"
        """.trimIndent())
        tagDef.apply(tag)
        assertEquals("Foo \"HelloKDS\" 5 true", tag.toString())

        tag = KD.read("""
            Foo "HelloKDS" 12
        """.trimIndent())
        tagDef.apply(tag)
        assertEquals("Foo \"HelloKDS\" 12 true", tag.toString())

        tag = KD.read("""
            Foo "HelloKDS" 12 false
        """.trimIndent())
        tagDef.apply(tag)
        assertEquals("Foo \"HelloKDS\" 12 false", tag.toString())

        // Fails because the second value is a Long rather then an Int
        assertThrows(KDSException::class.java) {
            tag = KD.read("""
                Foo "HelloKDS" 12L false
            """.trimIndent())
            tagDef.apply(tag)
        }

        tagDef = TagDef(
                TagDef.EMPTY_ANNOS,
                StringMatcher.EMPTY,
                StringMatcher.ID("Foo"),
                listOf<ValueDef>(
                        ValueDef(TypeDef.String),
                        ValueDef(TypeDef.Int),
                        ValueDef(TypeDef.Bool, true)
                )
        )

        assertThrows<KDSException>("io.kixi.kd.schema.KDSException: Foo: Missing value for Int") {
            tag = KD.read("""
                        Foo "poodle"
                    """.trimIndent())
            tagDef.apply(tag)
        }
    }

    @Test
    fun varValues() {
        val tagDef = TagDef(
                TagDef.EMPTY_ANNOS,
                StringMatcher.EMPTY,
                StringMatcher.ID("Foo"),
                listOf<ValueDef>(
                        ValueDef(TypeDef.String)
                ),
                ValueDef(TypeDef.Int)
        )

        val tag = KD.read("""
            Foo "HelloKDS" 1 2 3
        """.trimIndent())
        tagDef.apply(tag)
        assertEquals("Foo \"HelloKDS\" 1 2 3", tag.toString())
    }

    @Test
    fun stringMatcher() {
        val tagDef = TagDef(
                TagDef.EMPTY_ANNOS,
                StringMatcher.EMPTY,
                StringMatcher.ID("gift"),
                listOf<ValueDef>(
                        StringDef(TypeDef.String, StringListMatcher("flowers", "ring", "hat"))
                )
        )

        var tag = KD.read("""
                gift "flowers"
            """.trimIndent())
        tagDef.apply(tag)

        tag = KD.read("""
                gift "hat"
            """.trimIndent())
        tagDef.apply(tag)

        assertThrows<KDSException>("gift: Value poodle doesn't match String[flowers, ring, hat]") {
            tag = KD.read("""
                    gift "poodle"
                """.trimIndent())
            tagDef.apply(tag)
        }
    }

    @Test
    fun attributes() {
        var tagDef = TagDef(
                TagDef.EMPTY_ANNOS,
                StringMatcher.ID("animal"),
                StringMatcher.ID("bug"),
                attDefs = mapOf<NSID, ValueDef>(
                        NSID("name") to ValueDef(TypeDef.String)
                )
        )
        var tag = KD.read("""
                    animal:bug name="cricket"
                """.trimIndent())

        tagDef.apply(tag)
        assertEquals("animal:bug name=\"cricket\"", tag.toString())

        tag = KD.read("""
                    animal:bug name="cricket" insect=true
                """.trimIndent())
        assertThrows<KDSException>(
                "io.kixi.kd.schema.KDSException: animal:bug: Attribute key insect is not declared") {
            tagDef.apply(tag)
        }

        tagDef = TagDef(
                TagDef.EMPTY_ANNOS,
                StringMatcher.ID("animal"),
                StringMatcher.ID("bug"),
                attDefs = mapOf<NSID, ValueDef>(
                        NSID("name") to ValueDef(TypeDef.String),
                        NSID("insect") to ValueDef(TypeDef.Bool, true)
                )
        )
        tag = KD.read("""
                    animal:bug name="cricket"
                """.trimIndent())
        tagDef.apply(tag)
        assertEquals("animal:bug name=\"cricket\" insect=true", tag.toString())

        tag = KD.read("""
                    animal:bug name="spider" insect=false
                """.trimIndent())
        assertEquals("animal:bug name=\"spider\" insect=false", tag.toString())
    }

    @Test
    fun varAtts() {
        var tagDef = TagDef(
                TagDef.EMPTY_ANNOS,
                StringMatcher.ID("animal"),
                StringMatcher.ID("bug"),
                attDefs = mapOf<NSID, ValueDef>(
                        NSID("name") to ValueDef(TypeDef.String),
                        NSID("insect") to ValueDef(TypeDef.Bool, true)
                ),
                varAttDef = ValueDef(TypeDef.Any)
        )

        var tag = KD.read("""
            animal:bug name="water strider" aquatic=true
        """.trimIndent())
        tagDef.apply(tag)
        assertEquals("animal:bug aquatic=true name=\"water strider\" insect=true", tag.toString())
    }
}