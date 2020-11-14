package io.kixi.kd.schema

import io.kixi.*
import io.kixi.kd.Annotation
import io.kixi.kd.KDParseException
import io.kixi.NSID
import io.kixi.kd.Tag
import io.kixi.uom.*
import kotlin.reflect.KClass

/**
 * A KD schema provides a set of tag definitions and specifies one as the root via
 * the @Root annotation.
 */
class Schema(val rootDef:TagDef, var version:Version? = null) {

    companion object {

        val META_NSID = NSID("meta", namespace="kd")
        val VAR_VAL = NSID("varVal", namespace="kd")
        val ANY_ATTS = NSID("anyAtts", namespace="kd")

        val RANGE_1 = Range(1, 1)

        fun make(root:Tag): Schema {
            var rootDef: TagDef? = null
            val defs = mutableMapOf<NSID, TagDef>()

            var schemaVersion: Version? = null

            var newRoot = root

            // We have a single tag, so we need to wrap it in a root tag
            if(root.nsid.name!="root") {
                newRoot = Tag("root")
                root.annotations.add(Annotation("Root"))
                newRoot.children.add(root)
            }

            for(child in newRoot.children) {

                if(child.nsid == META_NSID) {
                    schemaVersion = child["version"] as Version
                    continue
                }

                var def: TagDef
                when(child.nsid.name) {
                    "tag" -> {
                        def= makeTagDef(child, defs)
                        defs.put(def.nsid, def)

                    }
                    else -> throw KDSException("Schema documents allow only \"tag\" " +
                            "tags in their body (i.e. everything below kd:meta) " +
                            "Got: ${child.nsid}", child)
                }

                if(child.annotations.isNotEmpty() && child.annotations[0].nsid.name == "Root") {
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
                    rootDef = defs.entries.first().value
                } else {
                    throw KDSException("No root was assigned in schema", null)
                }
            }

            return Schema(rootDef, schemaVersion)
        }

        private fun makeTagDef(tag: Tag, defs: MutableMap<NSID, TagDef>): TagDef {
            if(tag.value == null)
                throw KDSException("Tag name cannot be empty or null", tag)

            val nsid = NSID(tag.value as String)

            val valueDefs:List<ValueDef> =
                    if(tag.values.size > 1) makeValueDefs(tag, tag.values.subList(1, tag.values.size))
                    else CallDef.EMPTY_VALUES

            var varValDef: ValueDef? = null
            if(tag.attributes.containsKey(VAR_VAL)) {
                val valueDef = makeValueDef(tag[VAR_VAL], "variable length value type", tag)
                varValDef = valueDef
                tag.attributes.remove(VAR_VAL)
            }

            var anyAttsDef: ValueDef? = null
            if(tag.attributes.containsKey(ANY_ATTS)) {
                val anyAttsValue = tag[ANY_ATTS] ?: ""
                if(anyAttsValue !is Boolean)
                    throw KDSException("$ANY_ATTS must be a boolean. Got $anyAttsValue", tag)

                if(anyAttsValue == true) {
                    anyAttsDef = ValueDef(TypeDef.Any_N)
                }
                tag.attributes.remove(ANY_ATTS)
            }

            val attDefs:Map<NSID, ValueDef> =
                    if(tag.attributes.isEmpty()) CallDef.EMPTY_ATTS
                    else makeAttDefs(tag)

            val childGroupDefs = if (tag.children.isEmpty()) TagDef.EMPTY_CHILD_GROUPS
                    else makeChildGroupDefs(tag)

            return TagDef(nsid, valueDefs, varValDef, attDefs, anyAttsDef, childGroupDefs, defs)
        }

        private fun makeChildGroupDefs(tag: Tag): List<TagGroupDef> {
            val groupDefs = mutableListOf<TagGroupDef>()

            for(child in tag.children) {
                val nsid = child.nsid
                if(nsid== NSID.ANONYMOUS) {
                    throw KDSException("Tag child groups cannot be anonymous", child)
                }

                @Suppress("UNCHECKED_CAST")
                val range = if(child.values.isEmpty()) RANGE_1
                    else child.value as Range<Int>
                groupDefs.add(TagGroupDef(nsid, range))
            }

            return groupDefs
        }

        private fun makeValueDefs(tag: Tag, values: List<Any?>): List<ValueDef> {
            val valueDefs = ArrayList<ValueDef>()

            for(value in values) {
                valueDefs.add(makeValueDef(value, "value", tag))
            }

            return valueDefs
        }

        private fun makeAttDefs(child: Tag): Map<NSID, ValueDef> {
            val entryDefs = HashMap<NSID, ValueDef>()

            for(pair in child.attributes.entries) {
                val nsid = pair.key

                val valueDef = makeValueDef(pair.value!!, "attribute value", child)
                entryDefs.put(nsid, valueDef)
            }
            return entryDefs
        }

        /**
         * Make a value def. The matcher can be any of:
         * 1. A Ki type name such as `String` or `Bool`
         * 2. A default value such as `"unknown"` or `false`
         * 3. A matcher and optional default value in a map, e.g. [matcher=["red","green","blue"] default="red"]
         *
         * @param defObj Any
         * @param location String
         * @throws KDSException for malformed schema (not a type name, default value, matcher, or matcher / default value
         */
        private fun makeValueDef(defObj:Any?, location:String, tag: Tag): ValueDef {
            if(defObj == null || defObj == "nil" || defObj == "null")
                throw KDSException("$location type or default value in tag definition cannot be nil.", tag)

            val typeDef: TypeDef? = null

            if(defObj is Map<*, *>) {
                // matcher
                return makeMatcherValueDef(defObj, location, tag)
            } else if(defObj is List<*>) {
                // typed list or map structure
                
                if(defObj.isEmpty())
                    throw KDSException("Generic list or map constraint must have a type", tag)

                if(defObj.size==1) {
                    // We have a ListDef
                    val listDef = makeListTypeDef(defObj, location, tag)
                    return ValueDef(listDef)
                } else if(defObj.size==2) {
                    // We have a MapDef
                    val mapDef = makeMapTypeDef(defObj, location, tag)
                    return ValueDef(mapDef)
                }
            } else if(defObj is Range<*>) {
                return ValueDef(RangeDef(false, TypeDef(Type.typeOf(defObj.left)!!, false)),
                        defaultValue=defObj)
            } else if(defObj is Quantity<*>) {
                val quantDef = QuantityDef(false, defObj.unit::class, Type.typeOf(defObj.value)!!)
                return ValueDef(quantDef, defaultValue=defObj)
            } else {
                // simple type name, unit axis or a literal default value

                // check type name or unit axis
                if(defObj is String) {
                    val def = makeTypeDefFromName(defObj, location, tag)
                    if(def!=null)
                        return ValueDef(def)
                }

                // check default value
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
                                "${defObj.toString()} in ${tag.nsid} definition is not a " +
                                        "Ki type or valid default value.", tag)
                    }
                } else {
                    return ValueDef(typeDef)
                }
            }
            throw KDSException("$defObj is not a valid value type definition", tag)
        }

        /**
         * Creates a typeDef from a String. If no type or unit axis is matched, return
         * null.
         */
        private fun makeTypeDefFromName(text: String, location: String, tag: Tag): TypeDef? {
            if(text.startsWith("Range.")) {
                val def = makeTypeDefFromName(text.substring(text.indexOf('.') +1),
                    location, tag)
                if(def==null) {
                    throw KDSException("Range type in $location must be a KTS type name or unit axis. Got $text", tag)
                } else {
                    return RangeDef(false, def)
                }
            } else if(text.startsWith("Range_N.")) {
                val def = makeTypeDefFromName(text.substring(text.indexOf('.') +1),
                        location, tag)
                if(def==null) {
                    throw KDSException("Range_N type in $location must be a KTS type name or unit axis. Got $text", tag)
                } else {
                    return RangeDef(true, def)
                }
            }

            val typeDef = TypeDef.forName(text)
            if (typeDef == null) {

                // Check for unit axis name

                var unitClass = getBaseUnitClass(text)
                if(unitClass!=null) {
                    // TODO: Support specifying value number type
                    return QuantityDef(false, unitClass, Type.Dec)
                } else {
                    val nIndex = text.indexOf("_N")
                    if(nIndex!=-1) {
                        // Possibly a nullable quantity unit axis
                        unitClass = getBaseUnitClass(
                                text.substring(0, nIndex))
                        if(unitClass!=null) {
                            return QuantityDef(true, unitClass, Type.Dec)
                        }
                    }
                }
            } else {
                return typeDef
            }
            return null
        }

        private fun getBaseUnitClass(axes: String): KClass<*>? = when(axes) {
            // base types
            "Length" -> Length::class
            "Mass" -> Mass::class
            "SubstanceAmount" -> SubstanceAmount::class
            "Luminosity" -> Luminosity::class
            "Temperature" -> Temperature::class
            "Current" -> Current::class

            // common derived units
            "Area" -> Area::class
            "Volume" -> Volume::class
            "Speed" -> Speed::class
            "Density" ->Density::class
            else -> null
        }

        fun makeListTypeDef(defObj:List<*>, location: String, tag: Tag): ListDef {
            if(defObj.size!=1)
                throw KDSException("Internal Error: List type def in $location must specify a value type. Got $defObj",
                        tag)

            val valueDef = makeTypeDefFromAny(defObj.first(), location, tag)
            return ListDef(false, valueDef)
        }

        fun makeMapTypeDef(defObj:List<*>, location: String, tag: Tag): MapDef {
            // list or map typedef
            if(defObj.size!=2)
                throw KDSException("Internal Error: Map type def in $location must specify a key and value type. " +
                        "Got $defObj", tag)

            val keyDef = makeTypeDefFromAny(defObj.first(), location, tag)
            val valueDef = makeTypeDefFromAny(defObj[1], location, tag)

            return MapDef(false, keyDef, valueDef)
        }

        private fun makeTypeDefFromAny(obj: Any?, location: String, tag: Tag) : TypeDef {
            if(obj==null)
                throw KDSException("Map type definition in $location cannot have a null key type", tag)

            if(obj is String) {
                val def = TypeDef.forName(obj) ?:
                    throw KDSException("Type definition in $location has an unknown value type name $obj",
                        tag)
                return def
            } else if(obj is List<*>) {
                if(obj.size==1) {
                    return makeListTypeDef(obj, location, tag)
                } else if(obj.size==2) {
                    return makeMapTypeDef(obj, location, tag)
                } else {
                    throw KDSException("Type definition in $location is not valid: Too many type parameters. " +
                            "Got $obj", tag)
                }
            }

            throw KDSException("Internal Error: Type def in $location must be a type name or a List. Got $obj",
                    tag)
        }

        private fun makeMatcherValueDef(map: Map<*, *>, location: String, tag: Tag): ValueDef = when {
            map.containsKey("options") -> {
                val options = map.get("options")
                if(options is List<*>) {
                    if(options.isEmpty())
                        throw KDSException("$location options matcher must have at least one option", tag)

                    if(map.containsKey("default")) {
                        val default = map.get("default")!!
                        ValueDef(TypeDef.inferCollectionType(options as List<Any?>), defaultValue=default,
                                matcher=OptionsMatcher(options))
                    } else {
                        ValueDef(TypeDef.inferCollectionType(options as List<Any?>),
                                matcher=OptionsMatcher(options))
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
                        ValueDef(makeValueDef(range.left, location, tag).typeDef,
                                defaultValue=default,
                                matcher=RangeMatcher(range))
                    } else {
                        ValueDef(makeValueDef(range.left, location, tag).typeDef,
                                matcher=RangeMatcher(range))
                    }
                } else throw KDSException("$location range matcher much be a Range", tag)
            }
            map.containsKey("regex") -> {
                val regex = map.get("regex")
                if(regex is String) {
                    if(map.containsKey("default")) {
                        val default = map.get("default")!!
                        ValueDef(TypeDef.String, defaultValue=default, matcher=RegexMatcher(Regex(regex)))
                    } else {
                        ValueDef(TypeDef.String, matcher=RegexMatcher(Regex(regex)))
                    }
                } else {
                    throw KDSException("$location regex matcher much be a regular expression String", tag)
                }
            }
            map.containsKey("default") -> inferDefaultValueDef(map.get("default")!!,
                    location + " default value", tag)

            else -> throw KDSException("$location matcher must be options, range, regex or default", tag)
        }

        private fun inferDefaultValueDef(default: Any?, location: String, tag: Tag) = when(default) {
            null -> ValueDef(TypeDef.Any_N, null)
            is Range<*> -> ValueDef(RangeDef(false,
                    makeValueDef(default.left, location, tag).typeDef), default)
            is Quantity<*> -> ValueDef(QuantityDef(false, default.unit::class,
                    Type.typeOf(default.value)!!), default)
            is List<*> -> {
                val typeDef = if(default.isEmpty()) ListDef(false, TypeDef.Any_N)
                    else ListDef(false, TypeDef.inferCollectionType(default))
                ValueDef(typeDef, default)
            }
            is Map<*,*> -> {
                val empty = default.isEmpty()
                val keyDef = if(empty) TypeDef.Any_N
                    else TypeDef.inferCollectionType(default.keys)
                val valueDef = if(empty) TypeDef.Any_N
                    else TypeDef.inferCollectionType(default.values)

                ValueDef(MapDef(false, keyDef, valueDef), default)
            }
            else -> {
                val typeDef = Type.typeOf(default)
                if(typeDef==null) {
                    throw KDSException("$default in $location is not a valid literal", tag)
                } else {
                    ValueDef(TypeDef(typeDef, false), default)
                }
            }
        }
    }

    fun apply(docRoot:Tag) = rootDef.apply(docRoot)

    override fun toString(): String {
        return if(version==null) rootDef.toString()
            else "kd:meta version=$version\nrootDef.toString()"
    }
}