package io.kixi.kd

import io.kixi.Ki
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * An annotation for a tag.
 */
class Annotation {

    // TODO: Consider extracting a base class for Annotation and Tag given they both have
    //       an NSID, values and attributes. So far, there are no differences in these
    //       areas.

    var nsid: NSID
    var values = ArrayList<Any?>()
    var attributes = TreeMap<NSID, Any?>()

    constructor(nsid: NSID) {
        this.nsid = nsid
    }

    constructor(name:String, namespace:String = ""): this(NSID(name, namespace)) {}

    override fun toString(): String {
        if(values.isEmpty() && attributes.isEmpty())
            return "@$nsid"

        val builder = StringBuilder("@$nsid(")

        // output values
        if(values.isNotEmpty()) {
            val i: Iterator<*> = values.iterator()
            while (i.hasNext()) {
                builder.append(Ki.format(i.next()))
                if(i.hasNext()) {
                    builder.append(" ")
                }
            }
        }

        // output attributes
        if (attributes.isNotEmpty()) {
            if(values.isNotEmpty()) {
                builder.append(" ")
            }

            val i = attributes.entries.iterator()
            while (i.hasNext()) {
                val e = i.next()
                builder.append("${e.key}=${Ki.format(e.value)}")
                if(i.hasNext()) {
                    builder.append(" ")
                }
            }
        }
        return builder.append(')').toString()
    }

    fun <T> get(): T = value as T

    // Attributes ////

    fun setAttribute(name: String, namespace: String = "", value: Any?) =
            attributes.put(NSID(name, namespace), value)

    fun <T> getAttribute(name: String, namespace: String = "") : T =
            attributes[NSID(name, namespace)] as T

    fun <T> getAttributesInNamespace(namespace:String): Map<String, T> {
        val map = HashMap<String, Any?>()
        for(e in attributes.entries) {
            if(e.key.namespace == namespace)
                map.put(e.key.name, e.value)
        }
        return map as Map<String, T>
    }

    /**
     * Convenience method that returns the first value in the value list or null if there
     * are no values.
     */
    var value: Any? = null
        get() = if(values.size == 0) null else values[0]

    /**
     * Returns true if this annotation (including its values and attributes) is
     * equivalent to the given annotation.
     *
     * @return true if the annotations are equivalent
     */
    override fun equals(other: Any?): Boolean = other is Annotation && other.toString() == toString()

    /**
     * @return The hash (based on the output from toString())
     */
    override fun hashCode(): Int = toString().hashCode()
}

fun main() {
    val a1 = Annotation("Foo")
    println(a1)
}