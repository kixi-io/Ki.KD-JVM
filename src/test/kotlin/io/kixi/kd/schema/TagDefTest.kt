package io.kixi.kd.schema

import io.kixi.Range
import io.kixi.TypeDef
import io.kixi.kd.KD
import io.kixi.kd.NSID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TagDefTest {

    @Test fun basic() {
        var tagDef = TagDef(
            NSID("Foo"),
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
                NSID("Foo"),
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

    @Test fun varValues() {
        val tagDef = TagDef(
                NSID("Foo"),
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

    @Test fun optionsMatcher() {
        val tagDef = TagDef(
                NSID("gift"),
                listOf<ValueDef>(
                    ValueDef(TypeDef.String, matcher = OptionsMatcher(listOf("flowers", "ring", "hat"))),
                    ValueDef(TypeDef.Int, matcher = RangeMatcher(Range(1, 5)))
                )
        )

        var tag = KD.read("""
                gift "flowers" 2
            """.trimIndent())
        tagDef.apply(tag)

        tag = KD.read("""
                gift "hat" 3
            """.trimIndent())
        tagDef.apply(tag)

        tag = KD.read("""
                    gift "poodle" 1
                """.trimIndent())
        assertThrows<KDSException>("gift: Value poodle doesn't match String[flowers, ring, hat]") {
            tagDef.apply(tag)
        }

        tag = KD.read("""
                gift "hat" 6
            """.trimIndent())
        assertThrows<KDSException>("io.kixi.kd.schema.KDSException: gift: Value 6 doesn't match Int 1..5") {
            tagDef.apply(tag)
        }
    }

    @Test fun attributes() {
        var tagDef = TagDef(
                NSID("bug", "animal"),
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
                NSID("bug", "animal"),
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

    @Test fun varAtts() {
        val tagDef = TagDef(
                NSID("bug", "animal"),
                attDefs = mapOf<NSID, ValueDef>(
                        NSID("name") to ValueDef(TypeDef.String),
                        NSID("insect") to ValueDef(TypeDef.Bool, true)
                ),
                varAttDef = ValueDef(TypeDef.Any)
        )

        val tag = KD.read("""
            animal:bug name="water strider" aquatic=true
        """.trimIndent())
        tagDef.apply(tag)
        assertEquals("animal:bug aquatic=true name=\"water strider\" insect=true", tag.toString())
    }

    @Test fun childDefs() {

        val tagDefs = mapOf<NSID, TagDef>(
            NSID("ID") to
            TagDef(
                    NSID("ID"),
                    listOf<ValueDef>(
                            ValueDef(TypeDef.Int)
                    )
            ),
            NSID("referredBy") to
            TagDef(
                    NSID("referredBy"),
                    listOf<ValueDef>(
                            ValueDef(TypeDef.String)
                    )
            ),
            NSID("purchase") to
            TagDef(
                    NSID("purchase"),
                    listOf<ValueDef>(
                            ValueDef(TypeDef.String)
                    )
            )
        );

        val tagDef = TagDef(
                NSID("customer"),
                childGroupDefs = listOf<TagGroupDef>(
                    TagGroupDef(NSID("ID")), // defaults to 1..1
                    TagGroupDef(NSID("referredBy"), Range(0, 1)),
                    TagGroupDef(NSID("purchase"), Range(0, 0, openRight = true))
                ),
                tagDefs = tagDefs
        )

        var tag = KD.read("""
            customer {
                ID 1232
                referredBy "Bill"
                purchase "blanket"
                purchase "cup"
            }
        """.trimIndent())
        assertDoesNotThrow {
            tagDef.apply(tag)
        }


        tag = KD.read("""
            customer {
                referredBy "Bill"
                purchase "blanket"
                purchase "cup"
            }
        """.trimIndent())
        assertThrows<KDSException>(
                "io.kixi.kd.schema.KDSException: customer: " +
                "Tag ID allows a range of 1..1 ID child tags. Found: 0") {
            tagDef.apply(tag)
        }

        // referredBy is optional, so this should be OK
        tag = KD.read("""
            customer {
                ID 1232
                purchase "blanket"
                purchase "cup"
            }
        """.trimIndent())
        assertDoesNotThrow {
            tagDef.apply(tag)
        }
    }

    @Test fun complex() {
        val tagDef = TagDef(
                NSID("bug", "animal"),
                listOf<ValueDef>(
                        ValueDef(TypeDef.String, "unknown") // name
                ),
                attDefs = mapOf<NSID, ValueDef>(
                        NSID("location") to ValueDef(TypeDef.String),
                        NSID("date") to ValueDef(TypeDef.ZonedDateTime),
                        NSID("insect") to ValueDef(TypeDef.Bool, true)
                ),
                varAttDef = ValueDef(TypeDef.Any)
        )

        val tag = KD.read("""
            animal:bug ranatra aquatic=true date=2020/11/16 @11:32-Z location=Peru
        """)
        tagDef.apply(tag)
        assertEquals("""
            animal:bug "ranatra" aquatic=true date=2020/11/16@11:32:00-Z location="Peru" insect=true
        """.trim(), tag.toString())
    }

}
