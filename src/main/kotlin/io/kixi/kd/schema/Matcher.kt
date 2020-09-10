package io.kixi.kd.schema

interface Matcher {
    fun matches(obj:Any?): Boolean
}