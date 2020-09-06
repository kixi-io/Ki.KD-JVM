package io.kixi.kd.schema

import io.kixi.kd.Tag
import io.kixi.kd.Annotation

class AnnoDef(
        namespaceMatcher:StringMatcher = StringMatcher.EMPTY,
        nameMatcher: StringMatcher = StringMatcher.EMPTY,
        valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES,
        attDefs:List<AttDef> = TagEntityDef.EMPTY_ATTS,
) : TagEntityDef(namespaceMatcher, nameMatcher, valueDefs, attDefs) {

    // TODO
    fun matches(anno: Annotation): Boolean {
        return false
    }

    /**
     * @throws MalformedTagException
     * @param tag Tag
     */
    // TODO
    fun verify(anno: Annotation) {}
}