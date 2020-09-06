package io.kixi.kd.schema

import io.kixi.kd.Tag

class AttDef(name:String, valueDef:ValueDef, defaultValue:Any? = null) {

    // TODO
    fun matches(namespaceMatcher: StringMatcher, nameMatcher: StringMatcher, valueDef: ValueDef) =  false
}