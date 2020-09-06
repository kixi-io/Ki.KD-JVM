package io.kixi.kd.schema

import io.kixi.kd.Tag
import io.kixi.kd.Annotation

class TagDef(
        val annoDefs:List<AnnoDef> = EMPTY_ANNOS,
        namespaceMatcher:StringMatcher = StringMatcher.EMPTY,
        nameMatcher: StringMatcher = StringMatcher.EMPTY,
        valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES,
        attDefs:List<AttDef> = TagEntityDef.EMPTY_ATTS,
        val childDefs:List<TagDef> = EMPTY_CHILDREN
    ) : TagEntityDef(namespaceMatcher, nameMatcher, valueDefs, attDefs) {

    companion object {
        val EMPTY_ANNOS = ArrayList<AnnoDef>()
        val EMPTY_CHILDREN = ArrayList<TagDef>()
    }

    // TODO
    fun matches(tag: Tag): Boolean {
        if(!annoDefs.isEmpty()) {
            var i = 0
            annoDefs.forEach {
                if (!it.matches(tag.annotations[i])) return false
                i++
            }
        }

        if(!namespaceMatcher.matches(tag.nsid.namespace))
            return false
        if(!nameMatcher.matches(tag.nsid.name))
            return false

        if(!valueDefs.isEmpty()) {
            var i = 0
            valueDefs.forEach {
                if (!it.matches(tag.values[i])) return false
                i++
            }
        }

        // TODO - check attributes
        if(!attDefs.isEmpty()) {
            var i = 0
            attDefs.forEach {

                i++
            }
        }

        return true
    }


    /**
     * @throws MalformedTagException
     * @param tag Tag
     */
    // TODO
    fun verify(tag: Tag) {}
}