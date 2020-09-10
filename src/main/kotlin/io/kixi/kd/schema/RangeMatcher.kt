package io.kixi.kd.schema

import io.kixi.Range

class RangeMatcher<T: Comparable<T>>(val range:Range<T>) : Matcher {
    override fun matches(obj:Any?) = if(obj==null) false else range.contains(obj as T)
    override fun toString() = range.toString()
}

