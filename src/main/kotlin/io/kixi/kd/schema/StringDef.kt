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
}