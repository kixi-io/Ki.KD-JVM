package io.kixi.kd.schema

// import io.kixi.kd.Interpreter
// import io.kixi.kd.KD
import io.kixi.Type
import io.kixi.TypeDef
import io.kixi.kd.NSID
import io.kixi.kd.Tag

/**
 * A KD schema provides a set of tag definitions and specifies one as the root via
 * the @root annotation.
 *
 * Tags are either templates or full tag definitions. Template tags are named "template".
 * Full tag definitions are named "tag".
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
                    "template" -> { def= makeTemplateDef(child); defs.add(def) }
                    "tag" -> { def= makeTagDef(child); defs.add(def) }
                    else -> throw KDSException("Tag definitions must be of type \"tag\" or \"template\". " +
                            "Got: ${child.nsid}", child)
                }

                // if(!child.annotations.isEmpty()) println(child.annotations[0].nsid.name)

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

        private fun makeTemplateDef(child: Tag): TagDef {
            if(child.value == null)
                throw KDSException("Template name cannot be empty or null", child)
            var nsid = NSID(child.value as String)

            var valueDefs:List<ValueDef> =
                    if(child.values.size > 1) makeTemplateValueDefs(child, child.values.subList(1, child.values.size))
                    else TagEntityDef.EMPTY_VALUES

            var varValueDef:ValueDef? = null
            var attDefs:Map<NSID, ValueDef> = TagEntityDef.EMPTY_ATTS
            var varAttDef:ValueDef? = null
            var childDefs:List<TagGroupDef> = TagDef.EMPTY_CHILDREN

            // TODO: valueDefs, varValueDef, attDefs, varAttDef, childDefs

            return TagDef(nsid, valueDefs, varValueDef, attDefs, varAttDef, childDefs)
        }

        private fun makeTemplateValueDefs(tag: Tag, values: List<Any?>): List<ValueDef> {
            val valueDefs = ArrayList<ValueDef>()
            for(value in values) {
                val tdef = TypeDef.forName(value?.toString() ?: "nil")
                if(tdef==null)
                    throw KDSException("Unknow type name $value", tag)
                valueDefs.add(ValueDef(tdef))
            }
            return valueDefs
        }

        private fun makeTagDef(child: Tag): TagDef {
            var nsid = NSID(child.value as String)

            val valuesTag = child.getChild("values")
            var valueDefs:List<ValueDef> =
                    if(valuesTag==null || valuesTag.children.isEmpty()) TagEntityDef.EMPTY_VALUES
                    else makeTagValueDefs(valuesTag)

            var varValueDef:ValueDef? = null
            var attDefs:Map<NSID, ValueDef> = TagEntityDef.EMPTY_ATTS
            var varAttDef:ValueDef? = null
            var childDefs:List<TagGroupDef> = TagDef.EMPTY_CHILDREN

            // TODO: valueDefs, varValueDef, attDefs, varAttDef, childDefs

            return TagDef(nsid, valueDefs, varValueDef, attDefs, varAttDef, childDefs)
        }

        private fun makeTagValueDefs(tag: Tag): List<ValueDef> {
            return TagEntityDef.EMPTY_VALUES
        }
    }

    fun apply(docRoot:Tag) = rootDef.apply(docRoot)

    override fun toString(): String {
        return rootDef.toString()
    }
}