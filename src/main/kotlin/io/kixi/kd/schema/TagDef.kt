package io.kixi.kd.schema

import io.kixi.TypeDef
import io.kixi.kd.Tag
import io.kixi.kd.Annotation
import io.kixi.kd.KD
import io.kixi.kd.NSID

class TagDef(
        val annoDefs:List<AnnoDef> = EMPTY_ANNOS,
        nsid:NSID = NSID.ANONYMOUS,
        valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES,
        varValueDef:ValueDef? = null,
        attDefs:Map<NSID, ValueDef> = EMPTY_ATTS,
        varAttDef:ValueDef? = null,
        val childDefs:List<TagDef> = EMPTY_CHILDREN
    ) : TagEntityDef(nsid, valueDefs, varValueDef, attDefs, varAttDef) {

    companion object {
        val EMPTY_ANNOS = listOf<AnnoDef>()
        val EMPTY_CHILDREN = listOf<TagDef>()
    }

    /**
     * Verifies the structure and applies default values.
     *
     * @throws KDSException If tag does not match this definition
     * @param tag Tag
     */
    fun apply(tag: Tag) {
        // Checks NSID, values and attributes
        // Applies default values
        applyTagEntity(tag)
        applyAnnotations(tag)
        // TODO: Check children types
    }

    private fun applyAnnotations(tag: Tag) {
        for(anno in annoDefs) {

        }
    }
}