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
    }

    @Test fun schemaWithValuesAndAtts() {
        var schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
                tag Person String age=Int birthday=Date # nerd=true # nerd 
            """)
        var schema = Schema.make(schemaTag)
        println(schema)

        var doc = KD.read("Person \"Joe\" age=25 birthday=1975/5/12")
        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("Person \"Joe\" age=35")
        assertThrows<KDSException>("Person: Missing attribute birthday") { schema.apply(doc) }

        doc = KD.read("Person \"Joe\" age=true birthday=1975/5/12")
        assertThrows<KDSException>("Person: Attribute value type in age=true does not match Int") {
            schema.apply(doc)
        }

        /*
        doc = KD.read("Person \"Joe\" 23.5")
        assertThrows<KDSException>("Person: Value 23.5 doesn't match Int") { schema.apply(doc) }
        */
    }
}
