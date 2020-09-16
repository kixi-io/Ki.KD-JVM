package io.kixi.kd.schema

class RegexMatcher(val regex: Regex) : Matcher {
    override fun matches(text:Any?) = if(text==null) false else regex.matches(text.toString())
    override fun toString() = "regex($regex)"
}