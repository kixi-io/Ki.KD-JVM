package io.kixi.kd.schema

interface StringMatcher {
    companion object {
        val EMPTY = StringMatcher.ID("")
    }

    fun matches(text:String): Boolean

    class ID(val value:String) : StringMatcher {
        override fun matches(text:String): Boolean {
            return value == text
        }
    }
}