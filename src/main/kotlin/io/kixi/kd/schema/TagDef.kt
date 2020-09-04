package io.kixi.kd.schema

import io.kixi.kd.Tag

class TagDef(
        val annoDefs:List<AnnoDef> = EMPTY_ANNOS,
        namespaceMatcher:StringMatcher = StringMatcher.EMPTY,
        nameMatcher: StringMatcher,
        valueDefs:List<ValueDef>,
        attDefs:List<AttDef>,
        val childDefs:List<TagDef> = EMPTY_CHILDREN
    ) : TagEntityDef(namespaceMatcher, nameMatcher, valueDefs, attDefs) {

    companion object {
        val EMPTY_ANNOS = ArrayList<AnnoDef>()
        val EMPTY_CHILDREN = ArrayList<TagDef>()
    }

    // TODO
    fun matches(tag: Tag): Boolean {
        return false
    }

    /**
     * @throws MalformedTagException
     * @param tag Tag
     */
    // TODO
    fun verify(tag: Tag) {}
}