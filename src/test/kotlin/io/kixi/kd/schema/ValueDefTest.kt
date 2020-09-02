package io.kixi.kd.schema

import io.kixi.*
import io.kixi.uom.Length
import io.kixi.uom.Unit
import io.kixi.uom.Quantity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ValueDefTest {

    @Test fun testBool() {
        assertTrue(ValueDef(TypeDef.Bool).matches(true))
        assertTrue(ValueDef(TypeDef.Bool).matches(false))
        assertFalse(ValueDef(TypeDef.Bool).matches(5))
        assertTrue(ValueDef(TypeDef.Bool_N).matches(null))
        assertFalse(ValueDef(TypeDef.Bool).matches(null))
    }

    @Test fun testInts() {
        assertTrue(ValueDef(TypeDef.Int).matches(5))
        assertFalse(ValueDef(TypeDef.Int).matches(6L))
        assertFalse(ValueDef(TypeDef.Int).matches(null))
        assertTrue(ValueDef(TypeDef.Int_N).matches(null))
    }

    @Test fun testQuantities() {
        assertTrue(ValueDef(QuantityDef(true, Length::class, Type.Decimal)).matches(
                Quantity("5.0", Unit.cm)))
        assertFalse(ValueDef(QuantityDef(true, Length::class, Type.Decimal)).matches(
                Quantity(5, Unit.cm)))
        assertFalse(ValueDef(QuantityDef(true, Length::class, Type.Decimal)).matches(
                Quantity("5.0", Unit.g)))
    }

    @Test fun testRanges() {
        assertTrue(ValueDef(RangeDef(true, TypeDef.Int)).matches(Range(1,5)))
        assertFalse(ValueDef(RangeDef(true, TypeDef.Int)).matches(Range(1.0,5.2)))
    }

    @Test fun testLists() {
        assertTrue(ValueDef(ListDef(false, TypeDef.Int)).matches(emptyList<Int>()))
        assertTrue(ValueDef(ListDef(false, TypeDef.Int)).matches(listOf(1, 2, 3)))
        assertTrue(ValueDef(ListDef(false, TypeDef.Number)).matches(listOf(1, 2, 3.0)))
        assertFalse(ValueDef(ListDef(false, TypeDef.Int)).matches(listOf(1, null, 3)))
        assertTrue(ValueDef(ListDef(false, TypeDef.Int_N)).matches(listOf(1, null, 3)))
    }

    @Test fun testMaps() {
        assertTrue(ValueDef(MapDef(false, TypeDef.String, TypeDef.Int))
                .matches(emptyMap<String, Int>()))
        assertTrue(ValueDef(MapDef(false, TypeDef.String, TypeDef.Int))
                .matches(mapOf("one" to 1, "one" to 2, "three" to 3)))
        assertFalse(ValueDef(MapDef(false, TypeDef.String, TypeDef.Int))
                .matches(mapOf("one" to 1, "one" to 2, "three" to 3.0)))
        assertFalse(ValueDef(MapDef(false, TypeDef.String, TypeDef.Int))
                .matches(mapOf("one" to 1, "one" to 2, "three" to null)))
        assertTrue(ValueDef(MapDef(false, TypeDef.String, TypeDef.Int_N))
                .matches(mapOf("one" to 1, "one" to 2, "three" to null)))
    }
}