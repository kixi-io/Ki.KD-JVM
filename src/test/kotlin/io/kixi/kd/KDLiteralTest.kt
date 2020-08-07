package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KDLiteralTest {

    @Test fun testLiterals() {
        val root = KD.readResource("literal_tests.kd")
        val bytes = root.findChild("b64Greeting")!!.value as ByteArray
        assertEquals("Hello", String(bytes))
    }
}