package io.kixi.kd.schema

class RegexMatcher(val regex: Regex) : Matcher {
    override fun matches(obj:Any?) = if(obj==null) false else regex.matches(obj.toString())
    override fun toString() = "regex(${regex})"
}