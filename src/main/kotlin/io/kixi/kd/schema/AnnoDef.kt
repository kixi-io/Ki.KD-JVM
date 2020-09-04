package io.kixi.kd.schema

import io.kixi.kd.Tag

class AnnoDef(
        namespaceMatcher:StringMatcher = StringMatcher.EMPTY,
        nameMatcher: StringMatcher,
        valueDefs:List<ValueDef>,
        attDefs:List<AttDef>,
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