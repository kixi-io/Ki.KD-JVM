package io.kixi.kd.schema

import io.kixi.kd.KD
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class SchemaTest {
    @Test fun findRoot() {
        val schemaKD = KD.readResource("customers.kds")
        val schema = Schema.make(schemaKD)
        assertEquals("Customers", schema.rootDef.nsid.name)
    }

    @Test fun schemaWithValues() {
        var schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
                tag Person String Int
            """)
        var schema = Schema.make(schemaTag)

        var doc = KD.read("Person \"Joe\" 23")

        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("Person \"Joe\"")
        assertThrows<KDSException>("Person: Missing value for Int") { schema.apply(doc) }
        doc = KD.read("Person \"Joe\" 23.5")
        assertThrows<KDSException>("Person: Value 23.5 doesn't match Int") { schema.apply(doc) }

        // Value with a default
        schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
                tag Species "unknown"
            """)
        schema = Schema.make(schemaTag)

        doc = KD.read("Species")
        schema.apply(doc)
        assertEquals("Species \"unknown\"", doc.toString())

        doc = KD.read("Species \"Acontista brevipennis\"")
        schema.apply(doc)
        assertEquals("Species \"Acontista brevipennis\"", doc.toString())
    }

    @Test fun schemaWithValuesAndAtts() {
        var schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
                tag Person String age=Int birthday=Date nerd=true # nerd type is Bool, default is true
            """)
        var schema = Schema.make(schemaTag)
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

    @Test fun schemaWithMatchers() {
        var schemaTag = KD.read("""
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
    }

    @Test fun schemaWithLists() {
        // Defining a list
        var schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
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
                kd:meta version=1.0.0-beta-1
                
                @Root
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
                kd:meta version=1.0.0-beta-1
                
                @Root
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
                kd:meta version=1.0.0-beta-1
                
                @Root
                tag Box [String]
            """)
        schema = Schema.make(schemaTag)
        doc = KD.read("""
            Box ["marbles", "rocks"]
        """.trimIndent())
        assertDoesNotThrow { schema.apply(doc) }
    }

    @Test fun schemaWithMaps() {
        // Defining a map
        var schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
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
                kd:meta version=1.0.0-beta-1
                
                @Root
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
}
