package io.kixi.kd.schema

import io.kixi.toString

class StringListMatcher(vararg val options: String) : StringMatcher {
    override fun matches(text:String) = options.contains(text)
    override fun toString() = "String[${options.toString(", ")}]"
}