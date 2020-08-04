package io.kixi.kd

import io.kixi.Ki
import io.kixi.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class Base64Test {

    // TODO: Create Base64 type in KD or Ki to make manipulation easier
    // TODO: Break data into 60 char lines indented with new lines
    // TODO: Test on small image

    @Test fun testBase64() {
        val text = "Hello";
        val encodedText = Base64.getEncoder().encodeToString(text.toByteArray())

        var bytes = KD.read(".base64($encodedText)").value as ByteArray
        var kiBase64Literal = Ki.format(bytes);
        assertEquals(".base64(SGVsbG8=)", kiBase64Literal)

        var base64Data = kiBase64Literal.substring(".base64(".length, kiBase64Literal.length-1)
        var decodedBytes = Base64.getDecoder().decode(base64Data)
        assertEquals("Hello", String(decodedBytes))
    }

    @Test fun testWhitespace() {
        var bytes = KD.read(""".base64(
            SGVs    
            bG8=  
        )""".trimMargin()).value as ByteArray

        var kiBase64Literal = Ki.format(bytes);
        assertEquals(".base64(SGVsbG8=)", kiBase64Literal)
    }
}