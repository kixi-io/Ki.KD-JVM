package io.kixi.kd.schema

import io.kixi.TypeDef
import io.kixi.kd.Tag

class AttDef(val name:String, val valueDef:ValueDef) {

    fun matches(namespaceMatcher: StringMatcher,
                nameMatcher: StringMatcher,
                valueDef: ValueDef,
                defaultValue: Any? = null) = valueDef.matches(valueDef)

    override fun toString() = "$name = " + (
        if(valueDef.defaultValue == null) valueDef.toString() else
        "$valueDef (default ${valueDef.defaultValue})"
    )
}