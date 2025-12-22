package io.kixi.kd

import io.kixi.Ki
import io.kixi.Call
import io.kixi.NSID

/**
 * An annotation for a tag.
 */
class Annotation : Call {

    constructor(_nsid: NSID): super(_nsid) {}

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
}
