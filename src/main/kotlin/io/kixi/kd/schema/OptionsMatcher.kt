package io.kixi.kd.schema

import io.kixi.toString

class OptionsMatcher<T>(vararg val options: T) : Matcher {
    override fun matches(obj:Any?) = options.contains(obj)
    override fun toString() = "[${options.toString(", ")}]"
}