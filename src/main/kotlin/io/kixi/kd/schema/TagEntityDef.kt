package io.kixi.kd.schema

import io.kixi.ListDef
import io.kixi.TypeDef
import io.kixi.kd.TagEntity
import java.lang.IllegalArgumentException

abstract class TagEntityDef(
        val namespaceMatcher: StringMatcher = StringMatcher.EMPTY,
        val nameMatcher: StringMatcher  = StringMatcher.EMPTY,
        val valueDefs:List<ValueDef> = EMPTY_VALUES,
        val varValueDef: ValueDef? = null,
        val attDefs:Map<String, ValueDef> = EMPTY_ATTS,
        val varAttDef: ValueDef? = null
) {

    companion object {
        val EMPTY_VALUES = ArrayList<ValueDef>()
        val EMPTY_ATTS = HashMap<String, ValueDef>()
    }

    var beginDefaultValues = -1

    /**
     * @throws IllegalArgumentException If a non-default value def occurs after a default
     *   value def, or a definition is defined with both default values and varValues
     */
    init {
        var index = 0
        for(valueDef in valueDefs) {
            if(valueDef.defaultValue == null) {
                if(beginDefaultValues!=-1) {
                    var id = if (nameMatcher == StringMatcher.EMPTY) "(anonymous)" else nameMatcher.toString()
                    if (namespaceMatcher != StringMatcher.EMPTY)
                        id = "${namespaceMatcher.toString()}:$id"

                    throw IllegalArgumentException("Tag Entity Definition $id cannot have a non-default value def " +
                            "($valueDef) after a default value def.")
                }
            } else if(beginDefaultValues!=-1) {
                beginDefaultValues = index
            }
            index++
        }

        if(beginDefaultValues!=-1 && varValueDef!=null) {
            var id = if (nameMatcher == StringMatcher.EMPTY) "(anonymous)" else nameMatcher.toString()
            if (namespaceMatcher != StringMatcher.EMPTY)
                id = "${namespaceMatcher.toString()}:$id"
            throw IllegalArgumentException("Tag Entity Definition $id cannot declare a varValue ($varValueDef) in a " +
                    "definition with default values")
        }
    }

    val hasVarValues: Boolean = varValueDef != null
    val hasVarAtts: Boolean = varAttDef != null

    /**
     * Sets default values and verifies tag entity structure.
     *
     * @throws KDSException If the entity (tag or annotation) does not match the structure
     *   defined by this tag entity definition.
     */
    protected fun applyTagEntity(tagEntity: TagEntity) {
        verifyNSID(tagEntity)
        applyValues(tagEntity)
        applyAtts(tagEntity)
    }

    private fun verifyNSID(tagEntity: TagEntity) {
        if(!namespaceMatcher.matches(tagEntity.nsid.namespace))
            throw KDSException("Namespace does not match \"${namespaceMatcher.toString()}\"", tagEntity)
        if(!nameMatcher.matches(tagEntity.nsid.name))
            throw KDSException("Name does not match \"${nameMatcher.toString()}\"", tagEntity)
    }

    private fun applyValues(tagEntity: TagEntity) {
        var values = tagEntity.values

        // We have no regular value definitions
        if(valueDefs.isEmpty()) {
            if(values.isEmpty()) {
                return
            }

            // We have no regular value definitions but we have varValues
            if(hasVarValues) {
                for(value in values) {
                    if(!varValueDef!!.matches(value))
                      throw KDSException("Value \"$value\" in variable length value list " +
                              "doesn't match $varValueDef", tagEntity)
                }
            }
            return
        }

        // We have variable length values and no default values
        if(hasVarValues) {
            var valIndex = 0
            for(valDef in valueDefs) {
                val value = values[valIndex]
                if(!valDef.matches(value))
                    throw KDSException("Value $value doesn't match $valDef", tagEntity)
            }

            // handle variable length values
            if(values.size > valueDefs.size) {
                for(index in valueDefs.size..values.size-1) {
                    val value = values[index]
                    if(!varValueDef!!.matches(value))
                        throw KDSException("Value $value doesn't match $varValueDef", tagEntity)
                }
            }

        // We don't have variable length values
        } else {
            if(values.size > valueDefs.size) {
                throw KDSException("Too many values. Value definitios are: $valueDefs", tagEntity)
            }

            var valIndex = 0

            // String, Int=5 Bool=true
            // called with: "hi"
            // called with: "hi" 2
            // called with: "hi" 2 false

            val limit = if (beginDefaultValues==-1) values.size-1 else
                beginDefaultValues

            for(i in 0..valueDefs.size-1) {
                val valueDef = valueDefs[i]

                if(i>values.size-1) {
                    if(valueDef.defaultValue==null) {
                        throw KDSException("Missing value for $valueDef", tagEntity)
                    }

                    tagEntity.values.add(valueDef.defaultValue)
                } else {
                    val value = values[i]
                    if(!valueDef.matches(value)) {
                        throw KDSException("Value $value doesn't match $valueDef", tagEntity)
                    }
                }
            }
        }
    }

    private fun applyAtts(tagEntity: TagEntity) {
        // TODO - check attributes
        if(!attDefs.isEmpty()) {
            var i = 0
            attDefs.forEach {
                // TODO: check value types
                // TODO: set defaults
                i++
            }
        }
    }
}




