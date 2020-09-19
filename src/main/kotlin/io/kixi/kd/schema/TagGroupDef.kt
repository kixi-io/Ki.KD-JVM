package io.kixi.kd.schema

import io.kixi.Range
import io.kixi.kd.NSID

// data class TagGroupDef(val def:TagDef, val pattern: Range<Int>)
data class TagGroupDef(val nsid: NSID, val pattern: Range<Int> = RANGE_1) {
    companion object {
        val RANGE_1 = Range(1,1)
    }
}