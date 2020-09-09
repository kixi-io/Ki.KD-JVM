package io.kixi.kd.schema

import io.kixi.kd.Tag
import io.kixi.kd.Annotation
import io.kixi.kd.NSID

class AnnoDef(
        nsid:NSID = NSID.ANONYMOUS,
        valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES,
        varValueDef:ValueDef? = null,
        attDefs:Map<NSID, ValueDef> = EMPTY_ATTS,
        varAttDef:ValueDef? = null,
) : TagEntityDef(nsid, valueDefs, varValueDef, attDefs, varAttDef) {

    /**
     * Verifies the structure and applies default values.
     *
     * @throws KDSException If anno does not match this definition
     * @param tag Tag
     */
    fun apply(anno: Annotation) {
        applyTagEntity(anno)
    }
}