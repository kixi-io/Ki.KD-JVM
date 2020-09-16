package io.kixi.kd.schema

import io.kixi.toString

class OptionsMatcher(val options: List<Any?>) : Matcher {
    override fun matches(obj:Any?) = options.contains(obj)
    override fun toString() = "[${options.toString(", ")}]"
}