package io.kixi.kd.schema

import io.kixi.*
import io.kixi.uom.Unit
import io.kixi.uom.Length
import io.kixi.uom.Quantity
import kotlin.reflect.full.isSubclassOf

/**
 * A value pattern for a single value, which can be any KTS type, including a generic
 * structure.
 */
class ValueDef(val def: TypeDef) {

    fun matches(value: Any?) = when {
        value == null -> def == TypeDef.nil || def.nullable
        !def.generic -> def.type.kclass == value!!::class
        def is QuantityDef -> value is Quantity<*> &&
                def.unitType == value.unit::class &&
                value.value::class == def.numType.kclass
        def is RangeDef -> value is Range<*> &&
                value.left::class == def.valueDef.type.kclass
        def is ListDef -> if(value is List<*>) matchesList(value) else false
        def is MapDef -> if(value is Map<*,*>) matchesMap(value) else false
        else -> false
    }

    private fun matchesList(list: List<*>): Boolean {
        if(list.isEmpty()) return true

        val valueDef = (def as ListDef).valueDef

        for(e in list) {
            if(e==null) {
                if(valueDef.nullable) {
                    continue
                } else {
                    return false
                }
            }

            if(valueDef.type.kclass != e::class &&
                    !e::class.isSubclassOf(valueDef.type.kclass))
                return false
        }

        return true
    }

    private fun matchesMap(map: Map<*,*>): Boolean {
        if (map.isEmpty()) return true

        for(e in map) {
            def as MapDef

            if(e.key==null && !def.keyTypeDef.nullable) return false
            if(e.value==null && !def.valueTypeDef.nullable) return false

            if(e.key!=null && def.keyTypeDef.type.kclass != e.key!!::class &&
                    !e.key!!::class.isSubclassOf(def.keyTypeDef.type.kclass))
                return false

            if(e.value!=null && def.valueTypeDef.type.kclass != e.value!!::class &&
                    !e.value!!::class.isSubclassOf(def.valueTypeDef.type.kclass))
                return false
        }

        return true
    }
}
