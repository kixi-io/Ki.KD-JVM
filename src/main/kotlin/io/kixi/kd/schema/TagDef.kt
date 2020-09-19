package io.kixi.kd.schema

import io.kixi.TypeDef
import io.kixi.kd.Tag
import io.kixi.kd.Annotation
import io.kixi.kd.KD
import io.kixi.kd.NSID

class TagDef(
        nsid:NSID = NSID.ANONYMOUS,
        valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES,
        varValueDef:ValueDef? = null,
        attDefs:Map<NSID, ValueDef> = EMPTY_ATTS,
        varAttDef:ValueDef? = null,
        val childGroupDefs:List<TagGroupDef> = EMPTY_CHILD_GROUPS,
        val tagDefs:Map<NSID, TagDef> = EMPTY_TAG_DEFS
    ) : TagEntityDef(nsid, valueDefs, varValueDef, attDefs, varAttDef) {

    companion object {
        // val EMPTY_ANNOS = listOf<AnnoDef>()
        val EMPTY_CHILD_GROUPS = listOf<TagGroupDef>()
        val EMPTY_TAG_DEFS = mapOf<NSID, TagDef>()
    }

    /**
     * Verifies the structure and applies default values.
     *
     * @throws KDSException If tag does not match this definition
     * @param tag Tag
     */
    fun apply(tag: Tag) {
        // Checks NSID, values & attributes; Applies default values
        applyTagEntity(tag)
        // applyAnnotations(tag)
        applyChildDefs(tag)
    }

    /*
    private fun applyAnnotations(tag: Tag) {
        for(anno in annoDefs) {
        }
    }
    */

    fun applyChildDefs(tag: Tag) {
        if(childGroupDefs.isEmpty() && !tag.children.isEmpty())
            throw KDSException("$nsid does not allow child tags", tag)

        var counters = mutableMapOf<NSID, TagGroupCounter>()
        for(child in tag.children) {
            val childGroupDef = childGroupDefs.find{ it.nsid == child.nsid }
            if(childGroupDef==null) {
                // No definition for this tag's nsid
                throw KDSException("$nsid does not allow child tags of type ${child.nsid}", tag)
            } else {
                // tgDef.def.apply(child)
                val tagDef = tagDefs[childGroupDef.nsid]
                if(tagDef==null)
                    throw KDSException("Tag id ${childGroupDef.nsid} not found", tag)
                tagDef.apply(child)

                var counter = counters[childGroupDef.nsid]
                if(counter==null) {
                    counter = TagGroupCounter(child.nsid)
                    counters.put(child.nsid, counter)
                }
                counter.count++
            }
        }

        // Check if we have too many or too few of children for a given tag type (nsid)
        for(childGroupDef in childGroupDefs) {
            val nsid = childGroupDef.nsid
            val counter = counters[nsid]
            val count = if (counter==null) 0 else counter.count

            if(!childGroupDef.pattern.contains(count)) {
                throw KDSException("Tag $nsid allows a range of ${childGroupDef.pattern} ${childGroupDef.nsid} " +
                        "child tags. Found: $count", tag)
            }
        }
    }

    override fun toString(): String {
        var childDefsSB = StringBuilder("")
        if(!childGroupDefs.isEmpty()) {
            childDefsSB.appendLine(" {")
            for(child in childGroupDefs) {
                childDefsSB.append('\t')
                childDefsSB.appendLine(child)
            }
            childDefsSB.appendLine("}")
        }

        return "$nsid $valueDefs $attDefs$childDefsSB"
    }

    private class TagGroupCounter(val nsid:NSID, var count:Int = 0) {
        override fun hashCode(): Int = nsid.hashCode()
        override fun equals(other: Any?): Boolean = other is TagGroupCounter && other.nsid == nsid
    }
}

