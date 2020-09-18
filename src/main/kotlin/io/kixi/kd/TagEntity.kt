package io.kixi.kd

import io.kixi.Ki
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * This is the base class for entities that have an NSID, value list and attribute map.
 * The values list, attributes list and attributes map use lazy initialization to
 * conserve memory.
 *
 * TagEntityBase is the superclass of Tag and Annotation.
 */
abstract class TagEntity {

    var nsid: NSID

    // TODO: We need better lazy initialization.
    // These will be initialized anytime find operations are used, or toString() is called.
    val values: MutableList<Any?> by lazy { ArrayList<Any?>() }
    val attributes: MutableMap<NSID, Any?> by lazy { HashMap<NSID, Any?>() }

    constructor(nsid: NSID) {
        this.nsid = nsid
    }

    constructor(name:String, namespace:String = ""): this(NSID(name, namespace)) {}

    override fun toString(): String {
        if(values.isEmpty() && attributes.isEmpty())
            return "$nsid"

        val builder = StringBuilder("$nsid ")

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
        return builder.toString()
    }

    // Values ////

    operator fun get(valueIndex:Int): Any? = values[valueIndex]
    operator fun set(valueIndex:Int, obj:Any?): Any? = values.set(valueIndex, obj)

    // Attributes ////

    operator fun get(name: String, namespace: String = ""): Any? = getAttribute(name, namespace)
    operator fun set(name: String, namespace: String = "", value:Any?): Any? = setAttribute(name, namespace, value)

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
     * Convenience method that gets or sets the first value in the value list. The getter
     * returns null if no values are present.
     */
    var value: Any?
        get() = if(values.size == 0) null else values[0]
        set(_value) {
            if (values.size == 0) {
                values.add(_value)
            } else {
                values[0] = _value
            }
        }

    /**
     * Returns true if this tag entity (including its values and attributes) is
     * equivalent to the given tag entity.
     *
     * @return true if the tag entities are equivalent
     */
    override fun equals(other: Any?): Boolean = other is Annotation && other.toString() == toString()

    /**
     * @return The hash (based on the output from toString())
     */
    override fun hashCode(): Int = toString().hashCode()
}
