package io.kixi.kd

import io.kixi.Ki
import io.kixi.Range
import io.kixi.uom.Unit
import io.kixi.uom.Mass
import io.kixi.uom.Quantity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal as Dec

class LiteralTest {

    @Test fun literals() {
        val root = KD.readResource("literal_tests.kd")

        val bytes = root.findChild("b64Greeting")!!.value as ByteArray
        assertEquals("Hello", String(bytes))

        @Suppress("UNCHECKED_CAST") val quantityRange = root.findChild("quantityRange2")!!.
            value as Range<Quantity<Mass>>
        assertEquals(Range<Quantity<Mass>>(Quantity(Dec(5), Unit.g), Quantity(Dec(20), Unit.kg)),
                quantityRange)
        assertTrue(quantityRange.contains(Quantity.mass("17g")))
        assertFalse(quantityRange.contains(Quantity.mass("21kg")))
    }
}