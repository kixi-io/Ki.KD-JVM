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

                val tagDef = makeTagDef(child)
                defs.add(tagDef)
                if(!child.annotations.isEmpty() && child.annotations[0].nsid.name == "Root") {
                    if(rootDef!=null) {
                        throw KDSException("Only one root can be assigned. Found root " +
                                "\"${child.nsid}\" but already had root \"${rootDef.nsid}\"", child)
                    } else {
                        rootDef = tagDef
                    }
                }
            }

            if(rootDef==null) {
                if(defs.size==1) {
                    rootDef = defs[0]
                }
            } else {
                throw KDSException("No root was assigned in schema", null)
            }

            return Schema(rootDef!!, defs)
        }

        private fun makeTagDef(child: Tag): TagDef {
            var nsid = NSID(child.value as String)
            val valueDefs:List<ValueDef> = TagEntityDef.EMPTY_VALUES
            var varValueDef:ValueDef? = null
            val attDefs:Map<NSID, ValueDef> = TagEntityDef.EMPTY_ATTS
            var varAttDef:ValueDef? = null
            val childDefs:List<TagGroupDef> = TagDef.EMPTY_CHILDREN

            if(child.nsid.name == "template") {
                // TODO: valueDefs, varValueDef, attDefs, varAttDef, childDefs
            } else if(child.nsid.name == "tag") {
                // TODO: valueDefs, varValueDef, attDefs, varAttDef, childDefs
            } else {
                throw KDSException("Tag definitions must be of type \"tag\" or \"template\". " +
                        "Got: ${child.nsid.name}", child)
            }

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