package io.kixi.kd.schema

import io.kixi.TypeDef
import io.kixi.kd.Tag
import io.kixi.kd.Annotation
import io.kixi.kd.KD

class TagDef(
        val annoDefs:List<AnnoDef> = EMPTY_ANNOS,
        namespaceMatcher:StringMatcher = StringMatcher.EMPTY,
        nameMatcher: StringMatcher = StringMatcher.EMPTY,
        valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES,
        varValueDef:ValueDef? = null,
        attDefs:Map<String, ValueDef> = EMPTY_ATTS,
        varAttDef:ValueDef? = null,
        val childDefs:List<TagDef> = EMPTY_CHILDREN
    ) : TagEntityDef(namespaceMatcher, nameMatcher, valueDefs, varValueDef, attDefs, varAttDef) {

    companion object {
        val EMPTY_ANNOS = ArrayList<AnnoDef>()
        val EMPTY_CHILDREN = ArrayList<TagDef>()
    }

    /**
     * Verifies the structure and applies default values.
     *
     * @throws KDSException If tag does not match this definition
     * @param tag Tag
     */
    fun apply(tag: Tag) {
        // TODO - Check annotations and children types
        applyTagEntity(tag)
    }
}