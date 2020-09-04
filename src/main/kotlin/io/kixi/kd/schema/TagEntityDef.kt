package io.kixi.kd.schema

import io.kixi.kd.TagEntity

abstract class TagEntityDef(
        val namespaceMatcher: StringMatcher = StringMatcher.EMPTY,
        val nameMatcher: StringMatcher  = StringMatcher.EMPTY,
        val valueDefs:List<ValueDef> = EMPTY_VALUES,
        val attDefs:List<AttDef> = EMPTY_ATTS
) {
    companion object {
        val EMPTY_VALUES = ArrayList<ValueDef>()
        val EMPTY_ATTS = ArrayList<AttDef>()
    }
}


