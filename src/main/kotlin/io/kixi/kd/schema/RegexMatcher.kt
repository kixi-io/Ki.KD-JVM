package io.kixi.kd.schema

class RegexMatcher(val regex: Regex) : StringMatcher {
    override fun matches(text:String) = regex.matches(text)
    override fun toString() = regex.toString()
}