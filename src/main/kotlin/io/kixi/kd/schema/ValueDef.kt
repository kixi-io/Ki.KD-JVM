package io.kixi.kd.schema

import io.kixi.*

/**
 * A value pattern for a single value, which can be any KTS type, including a generic
 * structure.
 */
open class ValueDef(val typeDef: TypeDef, val defaultValue: Any? = null, val matcher:Matcher? = null) {

    // TODO
    open fun matches(value: Any?) = typeDef.matches(value) && (matcher==null || matcher.matches(value))
    override fun toString(): String {
        if(defaultValue == null) {
            return if(matcher==null) typeDef.toString() else "$typeDef $matcher"
        } else {
            return if(matcher==null) "$typeDef (default $defaultValue)" else "$typeDef (default $defaultValue) $matcher"
        }
    }

    val hasDefaultValue: Boolean = defaultValue != null
}