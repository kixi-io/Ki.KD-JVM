package io.kixi.kd.schema

import io.kixi.ListDef
import io.kixi.TypeDef
import io.kixi.kd.NSID
import io.kixi.kd.TagEntity
import java.lang.IllegalArgumentException

abstract class TagEntityDef(
        val nsid: NSID = NSID.ANONYMOUS,
        val valueDefs:List<ValueDef> = EMPTY_VALUES,
        val varValueDef: ValueDef? = null,
        val attDefs:Map<NSID, ValueDef> = EMPTY_ATTS,
        val varAttDef: ValueDef? = null
) {

    companion object {
        val EMPTY_VALUES = listOf<ValueDef>()
        val EMPTY_ATTS = mapOf<NSID, ValueDef>()
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
                    var id = if (nsid.isAnonymous) "(anonymous)" else nsid.toString()
                    throw IllegalArgumentException("Tag Entity Definition $id cannot have a non-default value def " +
                            "($valueDef) after a default value def.")
                }
            } else if(beginDefaultValues!=-1) {
                beginDefaultValues = index
            }
            index++
        }

        if(beginDefaultValues!=-1 && varValueDef!=null) {
            var id = if (nsid.isAnonymous) "(anonymous)" else nsid.toString()
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
        if(nsid != tagEntity.nsid)
            throw KDSException("NSID (namespace:)name ${tagEntity.nsid} != $nsid",
                    tagEntity)
        applyValues(tagEntity)
        applyAtts(tagEntity)
    }

    private fun applyValues(tagEntity: TagEntity) {
        var values = tagEntity.values

        // We have no regular value definitions
        if(valueDefs.isEmpty()) {

            if(values.isEmpty()) return

            // We have no regular value definitions but we have varValues
            if(hasVarValues) {
                for(value in values) {
                    if(!varValueDef!!.matches(value))
                      throw KDSException("Value \"$value\" in variable length value list " +
                              "doesn't match $varValueDef", tagEntity)
                }
            } else {
                if(!values.isEmpty())
                    throw KDSException("Tag does not allow values", tagEntity)
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

            // Check for missing attributes and attribute value type.
            // Populate default values if the attribute isn't explicitly set
            attDefs.forEach {
                var hasKey = tagEntity.attributes.containsKey(it.key)
                if(!hasKey) {
                    if(it.value.hasDefaultValue)
                        tagEntity.attributes.put(it.key, it.value.defaultValue)
                    else
                        throw KDSException("Missing attribute ${it.key}", tagEntity)
                } else {
                    val entityValue = tagEntity.attributes.get(it.key)
                    if(!it.value.matches(entityValue))
                        throw KDSException("Attribute value type in ${it.key}=$entityValue does not match ${it.value}",
                                tagEntity)
                }
            }

            // Deal with undeclared atts ////

            // Check values for varAtts
            if(hasVarAtts) {
                if(varAttDef!!.typeDef != TypeDef.Any_N) {
                    for(entityAtt in tagEntity.attributes.entries) {
                        if(!varAttDef.matches(entityAtt.value))
                            throw KDSException("Attribute value type in ${entityAtt.key}=${entityAtt.value} does not " +
                                    "match undeclared varatts type $varValueDef",
                                    tagEntity)
                    }
                }
            // No varatts allowed. Undeclared atts results in an exception
            } else {
                val declaredAttKeys = attDefs.keys
                for(entityAttKey in tagEntity.attributes.keys) {
                    if(!declaredAttKeys.contains(entityAttKey)) {
                        throw KDSException("Attribute key $entityAttKey is not declared", tagEntity)
                    }
                }
            }
        }
    }
}


