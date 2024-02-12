package io.kixi.kd.schema

import io.kixi.core.Range

// class RangeMatcher<T: Comparable<T>>(val range:Range<T>) : Matcher {
class RangeMatcher<T: Comparable<T>>(val range:Range<T>) : Matcher {
    @Suppress("UNCHECKED_CAST")
    override fun matches(obj:Any?) = if(obj==null) false else obj as T in range
    override fun toString() = range.toString()
}
