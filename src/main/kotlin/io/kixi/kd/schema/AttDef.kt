package io.kixi.kd.schema

import io.kixi.kd.Tag

class AttDef(name:String, valueDef:ValueDef, optional:Boolean) {

    // TODO
    fun matches(tag: Pair<String, Any?>) =  false
}