package io.kixi.kd.schema

// import io.kixi.kd.Interpreter
// import io.kixi.kd.KD
import io.kixi.Type
import io.kixi.TypeDef
import io.kixi.kd.NSID
import io.kixi.kd.Tag

/**
 * A KD schema provides a set of tag definitions and specifies one as the root via
 * the @Root annotation.
 */
class Schema(val rootDef:TagDef, val tagDefs:List<TagDef>) {

    // TODO: Question - Should Schema subclass TagDef? Every document has a root, which
    // is constrained by a TagDef.

    companion object {
        fun make(root:Tag): Schema {
            var rootDef: TagDef? = null
            val defs = mutableListOf<TagDef>()

            for(child in root.children) {
                if(child.nsid.toString() == "kd:meta")
                    continue // skip for now

                var def: TagDef
                when(child.nsid.name) {
                    "tag" -> { def= makeTagDef(child); defs.add(def) }
                    else -> throw KDSException("Tag definitions must be of type \"tag\" or \"template\". " +
                            "Got: ${child.nsid}", child)
                }

                if(!child.annotations.isEmpty() && child.annotations[0].nsid.name == "Root") {
                    if(rootDef!=null) {
                        throw KDSException("Only one root can be assigned. Found root " +
                                "\"${child.nsid}\" but already had root \"${rootDef.nsid}\"", child)
                    } else {
                        rootDef = def
                    }
                }
            }

            // If roots isn't specified and only one tag definition exists, use it as the
            // root
            if(rootDef==null) {
                if(defs.size==1) {
                    rootDef = defs[0]
                } else {
                    throw KDSException("No root was assigned in schema", null)
                }
            }

            return Schema(rootDef!!, defs)
        }

        private fun makeTagDef(child: Tag): TagDef {
            if(child.value == null)
                throw KDSException("Tag name cannot be empty or null", child)

            var nsid = NSID(child.value as String)

            var valueDefs:List<ValueDef> =
                    if(child.values.size > 1) makeValueDefs(child, child.values.subList(1, child.values.size))
                    else TagEntityDef.EMPTY_VALUES

            var attDefs:Map<NSID, ValueDef> =
                    if(child.attributes.isEmpty()) TagEntityDef.EMPTY_ATTS
                    else makeAttDefs(child)

            var varValueDef:ValueDef? = null
            var varAttDef:ValueDef? = null
            var childDefs:List<TagGroupDef> = TagDef.EMPTY_CHILDREN

            // TODO: varValueDef, attDefs, varAttDef, childDefs

            return TagDef(nsid, valueDefs, varValueDef, attDefs, varAttDef, childDefs)
        }

        private fun makeValueDefs(tag: Tag, values: List<Any?>): List<ValueDef> {
            val valueDefs = ArrayList<ValueDef>()
            for(value in values) {
                val tdef = TypeDef.forName(value?.toString() ?: "nil")
                if(tdef==null)
                    throw KDSException("Unknow type name in tag definition value list: $value", tag)
                valueDefs.add(ValueDef(tdef))
            }
            return valueDefs
        }

        private fun makeAttDefs(child: Tag): Map<NSID, ValueDef> {
            val entryDefs = HashMap<NSID, ValueDef>()
            for(pair in child.attributes.entries) {
                val nsid = pair.key
                if(nsid == null)
                    throw throw KDSException("Attribute key cannot be nil.", child)
                val valueDefText = pair.value

                // TODO: Deal with default values, infer type
                val tdef = TypeDef.forName(valueDefText?.toString() ?: "nil")
                        ?: throw KDSException("Unknow type name in tag definition value list: $valueDefText", child)

                entryDefs.put(nsid, ValueDef(tdef))
            }
            return entryDefs
        }
    }

    fun apply(docRoot:Tag) = rootDef.apply(docRoot)

    override fun toString(): String {
        return rootDef.toString()
    }
}