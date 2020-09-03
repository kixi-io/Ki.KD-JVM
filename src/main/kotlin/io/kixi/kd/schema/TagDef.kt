package io.kixi.kd.schema

import io.kixi.kd.Tag

class TagDef(val name: String, val valueDefs:List<ValueDef>, val attDefs:List<AttDef>,
    val childDefs:List<TagDef>) {

    // TODO
    fun matches(tag: Tag) = false
}

/*
kd:tag person String name {
    tag 0.._
}
*/