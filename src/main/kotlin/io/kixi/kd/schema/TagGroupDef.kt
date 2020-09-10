package io.kixi.kd.schema

import io.kixi.Range

data class TagGroupDef(val def:TagDef, val pattern: Range<Int>)