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
open class ValueDef(val typeDef: TypeDef, val defaultValue: Any? = null) {

    // TODO
    open fun matches(value: Any?) = typeDef.matches(value)
    override fun toString() = if(defaultValue == null) typeDef.toString() else
        typeDef.toString() + " (default $defaultValue)"

}