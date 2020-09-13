package io.kixi.kd.schema

import io.kixi.kd.KD
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SchemaTest {

    @Test fun findRoot() {
        val schemaKD = KD.readResource("customers.kds")
        val schema = Schema.make(schemaKD)
        assertEquals("customers", schema.rootDef.nsid.name)
        println(schema.rootDef)
    }
}
