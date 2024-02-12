package io.kixi.kd.schema

import io.kixi.core.toString

class OptionsMatcher(val options: List<Any?>) : Matcher {
    override fun matches(obj:Any?) = obj in options
    override fun toString() = "[${options.toString(", ")}]"
}
