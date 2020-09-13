package io.kixi.kd.schema

import io.kixi.kd.KD
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SchemaTest {

    @Test fun findRoot() {
        val schemaKD = KD.readResource("customers.kds")
        val schema = Schema.make(schemaKD)
        assertEquals("customers", schema.rootDef.nsid.name)
        // println(schema.rootDef)
    }
}
