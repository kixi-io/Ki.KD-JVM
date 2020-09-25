package io.kixi.kd.schema

import io.kixi.kd.KD
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

@Suppress("unused")
class SchemaTest {

    @Test fun findRoot() {
        val schemaKD = KD.readResource("customers.kds")
        val schema = Schema.make(schemaKD)
        assertEquals("Customers", schema.rootDef.nsid.name)
    }

    @Test fun values() {
        var schemaTag = KD.read("tag Person String Int")
        var schema = Schema.make(schemaTag)

        var doc = KD.read("Person \"Joe\" 23")

        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("Person \"Joe\"")
        assertThrows<KDSException>("Person: Missing value for Int") { schema.apply(doc) }
        doc = KD.read("Person \"Joe\" 23.5")
        assertThrows<KDSException>("Person: Value 23.5 doesn't match Int") { schema.apply(doc) }

        // Value with a default
        schemaTag = KD.read("tag Species \"unknown\"")
        schema = Schema.make(schemaTag)

        doc = KD.read("Species")
        schema.apply(doc)
        assertEquals("Species \"unknown\"", doc.toString())

        doc = KD.read("Species \"Acontista brevipennis\"")
        schema.apply(doc)
        assertEquals("Species \"Acontista brevipennis\"", doc.toString())
    }

    @Test fun valuesAndAtts() {
        val schemaTag = KD.read("""
                tag Person String age=Int birthday=Date nerd=true # nerd type is Bool, default is true
            """)
        val schema = Schema.make(schemaTag)
        assertEquals("Person [String] {nerd=Bool (default true), age=Int, birthday=Date}",
                schema.toString())

        var doc = KD.read("Person \"Joe\" age=25 birthday=1975/5/12")
        assertDoesNotThrow { schema.apply(doc) }
        assertEquals("Person \"Joe\" nerd=true age=25 birthday=1975/5/12",
                doc.toString())

        // Test override of default attribute value
        doc = KD.read("Person \"Joe\" age=25 birthday=1975/5/12 nerd=false")
        assertDoesNotThrow { schema.apply(doc) }
        assertEquals("Person \"Joe\" nerd=false age=25 birthday=1975/5/12",
                doc.toString())

        doc = KD.read("Person \"Joe\" age=35")
        assertThrows<KDSException>("Person: Missing attribute birthday") { schema.apply(doc) }

        doc = KD.read("Person \"Joe\" age=true birthday=1975/5/12")
        assertThrows<KDSException>("Person: Attribute value type in age=true does not match Int") {
            schema.apply(doc)
        }
    }

    @Test fun matchers() {
        val schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
                tag Widget name=[regex=".* widget"] \
                           color=[options=[red, green, blue] default=red] \
                           size=[range=5..10 default=7]
            """)
        var schema = Schema.make(schemaTag)

        var doc = KD.read("""
            Widget name="Acme widget" color=green
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }
        assertEquals("""
                Widget color="green" size=7 name="Acme widget"
            """.trim(), doc.toString())

        doc = KD.read("""
            Widget name="Acme" color=green
        """.trimIndent())
        assertThrows<KDSException>("Widget: Attribute value type in name=Acme does not match String regex(.* widget)") {
            schema.apply(doc)
        }

        doc = KD.read("""
            Widget name="Acme" color=purple
        """.trimIndent())
        assertThrows<KDSException>("Widget: Attribute value type in color=purple does not match String " +
                "(default red) [red, green, blue]") {
            schema.apply(doc)
        }

        doc = KD.read("""
            Widget name="Acme" size=11 
        """.trimIndent())
        assertThrows<KDSException>("Widget: Attribute value type in size=11 does not match " +
            "Range<Int> (default 7) 5..10") {
            schema.apply(doc)
        }

        schema = Schema.make(KD.read("""
            tag Yarn color=[options=[red green blue] default=green] length=[range=5cm..50cm]
        """.trimIndent()))
        doc = KD.read("""
            Yarn color=blue length=6cm
        """.trimIndent())
        schema.apply(doc)
        assertEquals("Yarn color=\"blue\" length=6cm", doc.toString())

        doc = KD.read("""
            Yarn length=6cm
        """.trimIndent())
        schema.apply(doc)
        assertEquals("Yarn length=6cm color=\"green\"", doc.toString())
    }

    @Test fun lists() {
        // Defining a list
        var schemaTag = KD.read("""
                tag Box things=[String] // List of Strings
            """)
        var schema = Schema.make(schemaTag)

        var doc = KD.read("""
            Box things=["apples","oranges"]
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("""
            Box things=["apples","oranges", 12]
        """.trimIndent())

        assertThrows<KDSException>("Box: Attribute value type in " +
                "things=[apples, oranges, 12] does not match List<String>") {
            schema.apply(doc)
        }

        // Defining a list of lists
        schemaTag = KD.read("""
                tag Box things=[[String]] // List of List of Strings
            """)
        schema = Schema.make(schemaTag)
        doc = KD.read("""
            Box things=[["apples","oranges"], ["dollars","yuan"]]
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("""
            Box things=[["apples","oranges"], ["dollars","yuan", 3]]
        """.trimIndent())
        assertThrows<KDSException>("Box: Attribute value type in " +
                "things=[[apples, oranges], [dollars, yuan, 3]] does not match " +
                "List<List<String>>") {
            schema.apply(doc)
        }

        // Defining a list of maps
        schemaTag = KD.read("""
                tag Box things=[[String,Int]] // List of Maps
            """)
        schema = Schema.make(schemaTag)
        doc = KD.read("""
            Box things=[[marbles=5, coins=7], [geodes=2, agates=11]]
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("""
            Box things=[[marbles=5, coins=7], [geodes=2, agates=11.5]]
        """.trimIndent())
        assertThrows<KDSException>("Box: Attribute value type in " +
                "things=[{coins=7, marbles=5}, {geodes=2, agates=11.5}] does not match " +
                "List<Map<String, Int>>") {
            schema.apply(doc)
        }

        // List type for values
        schemaTag = KD.read("""
                tag Box [String]
            """)
        schema = Schema.make(schemaTag)
        doc = KD.read("""
            Box ["marbles", "rocks"]
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }
    }

    @Test fun listWithDefault() {
        val schemaTag = KD.read("""
                tag Box things=[default=["cat", "hat"]] // List of Strings
            """)
        val schema = Schema.make(schemaTag)

        var doc = KD.read("Box")
        schema.apply(doc)
        assertEquals("""Box things=["cat", "hat"]""", doc.toString())

        doc = KD.read("""Box things=["monkey", "car"]""")
        schema.apply(doc)
        assertEquals("""Box things=["monkey", "car"]""", doc.toString())
    }

    @Test fun maps() {
        // Defining a map
        var schemaTag = KD.read("""
                tag Box things=[String, Number] // List of Strings
            """)
        var schema = Schema.make(schemaTag)

        var doc = KD.read("""
            Box things=["apples"=5,"oranges"=10]
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("""
            Box things=["apples"=5,"oranges"=true]
        """.trimIndent())
        assertThrows<KDSException>("Box: Attribute value type in things={oranges=true, apples=5} does not " +
                "match Map<String, Number>") { schema.apply(doc) }

        // Defining a map of lists
        schemaTag = KD.read("""
                tag Game scores=[String, [Number]] // Map of Strings
            """)
        schema = Schema.make(schemaTag)

        doc = KD.read("""
            Game scores=["Benny"=[10,5,9],"Atsuko"=[9,5,10]]
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("""
            Game scores=["Benny"=[10,5,9],"Atsuko"=12]
        """.trimIndent())
        assertThrows<KDSException>("Game: Attribute value type in scores={Benny=[10, 5, 9], Atsuko=12} does " +
                "not match Map<String, List<Number>>") { schema.apply(doc) }
    }

    @Test fun mapWithDefault() {
        val schemaTag = KD.read("""
                tag Game players=[default=["Mika"=23, "Joe"=15]] // List of Strings
            """)
        val schema = Schema.make(schemaTag)

        var doc = KD.read("Game")
        schema.apply(doc)
        assertEquals("""Game players=["Joe"=15, "Mika"=23]""", doc.toString())

        doc = KD.read("""Game players=["Jose"=15, "Sajjad"=3]""")
        schema.apply(doc)
        assertEquals("""Game players=["Jose"=15, "Sajjad"=3]""", doc.toString())
    }

    @Test fun ranges() {
        var schema = Schema.make(KD.read("""
            tag Temp 80..99 
        """.trimIndent()))

        // Testing for a range as a default value
        var doc = KD.read("Temp")
        schema.apply(doc)
        assertEquals("Temp 80..99", doc.toString())

        schema = Schema.make(KD.read("""
            tag Temp within=80..99 
        """.trimIndent()))

        // Testing for a range as a default attribute value
        doc = KD.read("Temp")
        schema.apply(doc)
        assertEquals("Temp within=80..99", doc.toString())

        // Testing for overriding a default range value
        doc = KD.read("Temp within=85..105")
        schema.apply(doc)
        assertEquals("Temp within=85..105", doc.toString())

        // Testing for range as a matcher
        schema = Schema.make(KD.read("""
            tag Temp is=[range=80..99] 
        """.trimIndent()))

        doc = KD.read("Temp is=82")
        assertDoesNotThrow {
            schema.apply(doc)
        }

        doc = KD.read("Temp is=79")
        assertThrows<KDSException>("Temp: Attribute value type in is=79 does " +
                "not match Int 80..99") { schema.apply(doc) }
    }

    @Test fun typedRangeDefs() {
        var schema = Schema.make(KD.read("tag Within Range.Length".trimIndent()))

        // Testing for a range as a default value
        var doc = KD.read("Within 5cm..10cm")
        schema.apply(doc)
        assertEquals("Within 5cm..10cm", doc.toString())

        doc = KD.read("Within 5..10")
        assertThrows<KDSException>("Within: Value 5..10 doesn't match " +
            "Range<Quantity<Length>>") { schema.apply(doc) }

        doc = KD.read("Within nil")
        assertThrows<KDSException>("Within: Value nil doesn't match " +
                "Range<Quantity<Length>>") { schema.apply(doc) }

        // Test a nullable range
        schema = Schema.make(KD.read("""
            tag Temp within=Range_N.Int 
        """.trimIndent()))

        doc = KD.read("Temp within=75..105")
        assertDoesNotThrow { schema.apply(doc) }

        // nil is allowed
        doc = KD.read("Temp within=nil")
        assertDoesNotThrow { schema.apply(doc) }
    }

    @Test fun quantities() {
        var schema = Schema.make(KD.read("""
            tag Temp 5cm
        """.trimIndent()))

        // Testing for a quantity as a default value
        var doc = KD.read("Temp")
        schema.apply(doc)
        assertEquals("Temp 5cm", doc.toString())

        // Testing for overriding quantity as a default value
        doc = KD.read("Temp 51mm")
        schema.apply(doc)
        assertEquals("Temp 51mm", doc.toString())

        // Testing failure with incompatible unit axes
        doc = KD.read("Temp 6ℓ")
        assertThrows<KDSException>("Temp: Value 6ℓ doesn't match " +
                "Quantity<Length> (default 5cm)") { schema.apply(doc) }

        // Test nullable quantity
        schema = Schema.make(KD.read("""
            tag Board length=Length_N.Int 
        """.trimIndent()))

        doc = KD.read("Board length=2.5m")
        assertDoesNotThrow { schema.apply(doc) }

        // nil is allowed
        doc = KD.read("Board length=nil")
        assertDoesNotThrow { schema.apply(doc) }
    }

    @Test fun quantityAxes() {
        var schema = Schema.make(KD.read("""
            tag Measures Length Current Volume
        """.trimIndent()))

        // Testing for a quantity as a default value
        var doc = KD.read("Measures 23cm 5A 14cm3")
        schema.apply(doc)
        assertEquals("Measures 23cm 5A 14cm³", doc.toString())

        doc = KD.read("Measures 23cm 5A 14cm2")
        assertThrows<KDSException>("Measures: Value 14cm² doesn't match " +
                "Quantity<Volume>") { schema.apply(doc) }

        schema = Schema.make(KD.read("""
            tag Measures Length Current 5cm³
        """.trimIndent()))
        doc = KD.read("Measures 23cm 5A")
        schema.apply(doc)
        assertEquals("Measures 23cm 5A 5cm³", doc.toString())

        doc = KD.read("Measures 23cm 5A 6.5cm³")
        schema.apply(doc)
        assertEquals("Measures 23cm 5A 6.5cm³", doc.toString())
    }

    @Test fun children() {
        var schema = Schema.make(KD.read("""
            tag Customer String
            
            @Root tag Customers group=String {
                Customer 1.._ # Must have 1 or more customers
            }
        """.trimIndent()))

        var doc = KD.read(
          """
              Customers group="premium" {
                Customer "Natasha"
                Customer "Jose"
              }
          """.trimIndent()
        );
        schema.apply(doc)

        doc = KD.read(
                """
              Customers group="premium" {
              }
          """.trimIndent()
        );
        assertThrows<KDSException>("Customers: Tag Customer allows a range of " +
                "1.._ Customer child tags. Found: 0") { schema.apply(doc) }
    }

    @Test fun varVal() {
        // Thing tag with any number of Int values and an attribute
        var schema = Schema.make(KD.read("""
            tag Thing kd:varVal=Int name=String
        """.trimIndent()))

        var doc = KD.read("""
            Thing 1 3 5 name="Odds"
        """.trimIndent())
        schema.apply(doc)

        doc = KD.read("""
            Thing 1 3 5L name="Odds"
        """.trimIndent())
        assertThrows<KDSException>("Thing: Value \"5\" in variable length " +
                "value list doesn't match Int") { schema.apply(doc) }
    }

    @Test fun anyAtts() {
        // Dog tag with a required "works" attribute and any other attributes
        var schema = Schema.make(KD.read("""
            tag Dog name=String kd:anyAtts=true
        """.trimIndent()))

        var doc = KD.read("""
            Dog name="Bruno" breed="chihuahua" troubleMaker=true
        """.trimIndent())
        schema.apply(doc)
    }

    @Test fun recursiveTags() {
        val schema = Schema.make(KD.read("""
            tag Component type="Pane" {
                Component 0.._
            }
        """.trimIndent()))

        var doc = KD.read("""
            Component {
                Component type="Toolbar" {
                    Component type="Button"
                }
            }
        """.trimIndent())
        schema.apply(doc)
        assertEquals("""
            Component type="Pane" {
                Component type="Toolbar" {
                    Component type="Button"
                }
            }""".trimIndent(), doc.toString())
    }
}
