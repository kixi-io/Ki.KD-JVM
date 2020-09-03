package io.kixi.kd.schema

import io.kixi.*
import io.kixi.uom.Unit
import io.kixi.uom.Length
import io.kixi.uom.Quantity
import kotlin.reflect.full.isSubclassOf

/**
 * A value pattern for a single value, which can be any KTS type, including a generic
 * structure.
 */
open class ValueDef(val def: TypeDef) {

    // TODO
    fun matches(value: Any?) = false
}
