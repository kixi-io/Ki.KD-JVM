package io.kixi.kd.schema

// import io.kixi.kd.Interpreter
// import io.kixi.kd.KD
import io.kixi.*
import io.kixi.kd.KD
import io.kixi.kd.KDParseException
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
                if (nsid == null)
                    throw KDSException("Attribute key in tag definition cannot be nil.", child)

                val valyeDef = makeValueDef(pair.value!!, "attribute value", child)
                entryDefs.put(nsid, valyeDef)
            }
            return entryDefs
        }

        /**
         * Make a value def. The matcher can be any of:
         * 1. A Ki type name such as `String` or `Bool`
         * 2. A default value such as `"unknown"` or `false`
         * 3. A matcher such as a 5..10 (range), `["red" "green" "blue"]` (options) or `"*.jpg"` (regex)
         * 4. A matcher and default value in a map, e.g. [matcher=["red","green","blue"] default="red"]
         *
         * @param obj Any
         * @param location String
         * @throws KDSException for malformed schema (not a type name, default value, matcher, or matcher / default value
         */
        private fun makeValueDef(defObj:Any, location:String, tag: Tag): ValueDef {
            if(defObj == null || defObj == "nil" || defObj == "null")
                throw KDSException("$location type or default value in tag definition cannot be nil.", tag)

            val typeDef: TypeDef?

            if(defObj is Map<*, *>) {
                // matcher
                return makeMatcherValueType(defObj as Map<*,*>, location, tag)
            // } else if(defObj is List<*>) {

            // return makeGenericValueType(defObj as List<*>, location, tag)
            } else {
                typeDef = if(defObj is String) TypeDef.forName(defObj) else null

                if (typeDef == null) {
                    try {
                        // val value = // KD(defObj.toString())
                        val type = Type.typeOf(defObj)
                        if (type == null) { // Should never happen, because it was successfully parsed
                            throw KDSException("$defObj is not a Ki type or valid default value.", tag)
                        } else {
                            return ValueDef(TypeDef.forName(type.name)!!, defaultValue = defObj)
                        }
                    } catch (e: KDParseException) {
                        throw KDSException(
                                "$defObj in ${tag.nsid} definition is not a Ki type or valid default value.", tag)
                    }
                } else {
                    return ValueDef(typeDef)
                }
            }
            throw KDSException("$defObj is not a valid value type definition", tag)
        }

        private fun makeMatcherValueType(map: Map<*, *>, location: String, tag: Tag): ValueDef = when {
            map.containsKey("options") -> {
                val options = map.get("options")
                if(options is List<*>) {
                    if(options.isEmpty())
                        throw KDSException("$location options matcher must have at least one option", tag)

                    if(map.containsKey("default")) {
                        val default = map.get("default")!!
                        ValueDef(inferListType(options as List<Any?>), defaultValue=default, matcher=OptionsMatcher(options as List<Any?>))
                    } else {
                        ValueDef(inferListType(options as List<Any?>), matcher=OptionsMatcher(options as List<Any?>))
                    }
                } else {
                    throw KDSException("$location options matcher much be a List", tag)
                }
            }
            map.containsKey("range") -> {
                val range = map.get("range")
                if(range is Range<*>) {
                    if(map.containsKey("default")) {
                        val default = map.get("default")!!
                        ValueDef(RangeDef(false, TypeDef.forName(Type.typeOf(range.left)!!.name)!!),
                                defaultValue=default,
                                matcher=RangeMatcher(range))
                    } else {
                        ValueDef(RangeDef(false, TypeDef.forName(Type.typeOf(range.left)!!.name)!!),
                                matcher=RangeMatcher(range))
                    }
                } else throw KDSException("$location range matcher much be a Range", tag)
            }
            map.containsKey("regex") -> {
                val regex = map.get("regex")
                if(regex is String) {
                    if(map.containsKey("default")) {
                        val default = map.get("default")!!
                        ValueDef(TypeDef.String, defaultValue=default, matcher=RegexMatcher(Regex(regex as String)))
                    } else {
                        ValueDef(TypeDef.String, matcher=RegexMatcher(Regex(regex as String)))
                    }
                } else {
                    throw KDSException("$location regex matcher much be a regular expression String", tag)
                }
            }
            else -> throw KDSException("$location matcher much be a options, range or regex", tag)
        }

        fun inferListType(options: List<Any?>): TypeDef {
            var widestType = Type.nil
            var gotNil = false

            for(opt in options) {
                if(opt == null) {
                    gotNil = true
                    continue
                }

                val itemType = Type.typeOf(opt)!!

                if(widestType == Type.nil) {
                    widestType = itemType
                } else if(widestType != itemType && !widestType.isAssignableFrom(itemType)) {
                    if(widestType.isNumber() && itemType.isNumber()) {
                        widestType = Type.Number
                    } else {
                        widestType = Type.Any
                    }
                }
            }

            return TypeDef(widestType, gotNil)
        }

        // private fun makeGenericValueType(list: List<*>, location: String, tag: Tag): ValueDef {
        // }
    }

    fun apply(docRoot:Tag) = rootDef.apply(docRoot)

    override fun toString(): String {
        return rootDef.toString()
    }
}

fun main() {
    println(Schema.inferListType(listOf(1, 3)))
    println(Schema.inferListType(listOf(1, 3, null)))
    // We specify Object to avoid Kotlin coercing 1 to 1L
    println(Schema.inferListType(listOf<Any>(1, 3L)))
    println(Schema.inferListType(listOf(1, "hi")))
}