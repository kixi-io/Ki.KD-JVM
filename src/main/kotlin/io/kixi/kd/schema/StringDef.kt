package io.kixi.kd.schema

import io.kixi.TypeDef

// TODO: Create matcher interface and string, set and regex implementations
// (possibly for K.Core)

class StringDef(typeDef: TypeDef, var matcher:String) : ValueDef(typeDef)