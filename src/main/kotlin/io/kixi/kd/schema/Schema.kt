package io.kixi.kd.schema

// import io.kixi.kd.Interpreter
// import io.kixi.kd.KD
import io.kixi.kd.KD
import io.kixi.kd.NSID
import io.kixi.kd.Tag

class Schema(val rootDef:TagDef, val tagDefs:List<TagDef>) {

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
            var nsid = NSID(child.value as String)
            var valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES
            var varValueDef:ValueDef? = null
            var attDefs:Map<NSID, ValueDef> = TagEntityDef.EMPTY_ATTS
            var varAttDef:ValueDef? = null
            var childDefs:List<TagGroupDef> = TagDef.EMPTY_CHILDREN

            // TODO: valueDefs, varValueDef, attDefs, varAttDef, childDefs

            return TagDef(nsid, valueDefs, varValueDef, attDefs, varAttDef, childDefs)
        }

        private fun templateAddValueDefs(tag: Tag): List<ValueDef> {
            return TagEntityDef.EMPTY_VALUES
        }

        private fun makeTagDef(child: Tag): TagDef {
            var nsid = NSID(child.value as String)
            var valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES
            var varValueDef:ValueDef? = null
            var attDefs:Map<NSID, ValueDef> = TagEntityDef.EMPTY_ATTS
            var varAttDef:ValueDef? = null
            var childDefs:List<TagGroupDef> = TagDef.EMPTY_CHILDREN

            // TODO: valueDefs, varValueDef, attDefs, varAttDef, childDefs

            return TagDef(nsid, valueDefs, varValueDef, attDefs, varAttDef, childDefs)
        }
    }

    override fun toString(): String {
        return rootDef.toString()
    }
}

/*
fun main() {
    val schemaKD = KD.readResource("customers.kds")
    val schema = Schema.make(schemaKD)
    println("nsid: ${schema.rootDef.nsid}")
    println(schema)
}
*/