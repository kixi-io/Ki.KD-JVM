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

    @Test fun template() {
        var schemaTag = KD.read("""
                kd:meta version=1.0.0-beta-1
                
                @Root
                template Person String Int
            """)
        var schema = Schema.make(schemaTag)

        var doc = KD.read("Person \"Joe\" 23")

        assertDoesNotThrow { schema.apply(doc) }

        doc = KD.read("Person \"Joe\"")
        assertThrows<KDSException>("Person: Missing value for Int") {
            schema.apply(doc)
        }
    }
}
