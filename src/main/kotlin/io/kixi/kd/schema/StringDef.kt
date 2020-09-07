package io.kixi.kd.schema

import io.kixi.TypeDef
import java.lang.IllegalArgumentException

// TODO: Create matcher interface and string, set and regex implementations
// (possibly for K.Core)

/**
 * typeDef must be String or String_U
 */
class StringDef(typeDef: TypeDef, val matcher:StringMatcher) : ValueDef(typeDef) {

    init {
        if(typeDef != TypeDef.String && typeDef != TypeDef.String_N)
            throw IllegalArgumentException("StringDef type must be String or String_N.")
    }

    override fun matches(value: Any?): Boolean {
        if(value==null)
            return typeDef == TypeDef.String_N

        return matcher.matches(value as String)
    }

    override fun toString() = matcher.toString()
}