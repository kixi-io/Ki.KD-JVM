package io.kixi.kd.schema

class StringsMatcher(val options: List<String>) : StringMatcher {
    override fun matches(text:String) = options.contains(text)
}