package io.kixi.kd.schema

class EqMatcher(val value:Any?) : Matcher {
    override fun matches(obj:Any?): Boolean = if(value==null) obj==null else value.equals(obj)
    override fun toString() = if(value == null) "null" else value.toString()
}