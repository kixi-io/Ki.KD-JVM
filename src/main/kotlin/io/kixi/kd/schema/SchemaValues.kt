package io.kixi.kd.schema

import io.kixi.*
import io.kixi.kd.Annotation
import io.kixi.kd.Tag
import io.kixi.uom.Currency
import io.kixi.uom.Quantity
import java.math.BigDecimal
import java.net.URL

/** KDS-specific checks deliberately do not alter Ki.Core's Type/TypeDef behavior. */
internal object SchemaValues {
    val types: Set<String> = Type.entries.map { it.name }.toSet() + setOf("Call", "KiTZDateTime")

    fun matches(type: String, value: Any?): Boolean {
        if (value == null) return type == "nil"
        return when (type) {
            "Any" -> matchesKnown(value)
            "Call" -> value is Call && value !is Tag && value !is Annotation
            "KiTZDateTime" -> value is KiTZDateTime
            "nil" -> false
            else -> Type.entries.firstOrNull { it.name == type }?.kclass?.isInstance(value) == true
        }
    }

    private fun matchesKnown(value: Any): Boolean =
        (value is Call && value !is Tag && value !is Annotation) || value is KiTZDateTime ||
                Type.typeOf(value)?.let { it != Type.nil } == true

    fun decimal(value: Number): BigDecimal? = when (value) {
        is BigDecimal -> value
        is Int, is Long, is Short, is Byte -> BigDecimal.valueOf(value.toLong())
        is Double -> if (value.isFinite()) BigDecimal.valueOf(value) else null
        is Float -> if (value.isFinite()) BigDecimal(value.toString()) else null
        else -> null
    }

    fun compare(a: Any, b: Any): Int? {
        if (a is Number && b is Number) {
            val left = decimal(a) ?: return null
            val right = decimal(b) ?: return null
            return left.compareTo(right)
        }
        if (a is Quantity<*> && b is Quantity<*>) {
            if (!a.unit.isCompatibleWith(b.unit)) return null
            // Same dimension does not imply an exchange rate exists.
            if ((a.unit is Currency || b.unit is Currency) && a.unit != b.unit) return null
            val left = decimal(a.value) ?: return null
            val right = decimal(b.value) ?: return null
            return a.unit.convertValue(left, b.unit).compareTo(right)
        }
        return null
    }

    /** Numeric equality is magnitude-based after type checking, including nested collections. */
    fun equivalent(a: Any?, b: Any?): Boolean {
        if (a == null || b == null) return a == null && b == null
        if (a is Number && b is Number) {
            val c = compare(a, b)
            return c == 0 || (c == null && a::class == b::class && a == b)
        }
        if (a is Quantity<*> && b is Quantity<*>) return compare(a, b) == 0
        if (a is URL && b is URL) return a.toExternalForm() == b.toExternalForm()
        if (a is List<*> && b is List<*>)
            return a.size == b.size && a.indices.all { equivalent(a[it], b[it]) }
        if (a is Map<*, *> && b is Map<*, *>) {
            if (a.size != b.size) return false
            val unmatched = b.entries.toMutableList()
            return a.entries.all { entry ->
                val i = unmatched.indexOfFirst { equivalent(entry.key, it.key) && equivalent(entry.value, it.value) }
                if (i < 0) false else { unmatched.removeAt(i); true }
            }
        }
        if (a is Grid<*> && b is Grid<*>)
            return a.width == b.width && a.height == b.height && equivalent(a.toList(), b.toList())
        if (a is Range<*> && b is Range<*>)
            return a.bound == b.bound && equivalent(a.start, b.start) && equivalent(a.end, b.end)
        if (a is Call && b is Call)
            return a.nsid == b.nsid && equivalent(a.values, b.values) && equivalent(a.attributes, b.attributes)
        return a::class == b::class && a == b
    }
}
