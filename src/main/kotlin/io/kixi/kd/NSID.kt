package io.kixi.kd

import io.kixi.text.ParseException
import io.kixi.text.isKiIdentifier

/**
 * NSIDs are an ID (key identifier) with an optional namespace. They are used for
 * tag names and attributes. Anonymous tags all use the ANONYMOUS key.
 */
data class NSID(val name:String, val namespace:String = "") : Comparable<NSID> {

    companion object {
        /**
         * Used for anonymous tags
         */
        @JvmStatic val ANONYMOUS = NSID("", "")
    }

    init {
        if(!namespace.isEmpty() && name.isEmpty())
            throw ParseException("Anonymous tags cannot have a namespace ($namespace).")

        // Removing the '.' is necessary to allow KD-style dot path style strings, which
        // are not valid KiIdentitiers
        if(!name.isEmpty() && !name.replace(".","").isKiIdentifier())
            throw ParseException("NSID name component $name is not a valid Ki Identifier.")
        if(!namespace.isEmpty() && !namespace.isKiIdentifier())
            throw ParseException("NSID namespace component $name is not a valid Ki Identifier.")
    }

    val isAnonymous: Boolean = this == ANONYMOUS

    override fun toString() = if(namespace != "") "$namespace:$name" else name

    override fun compareTo(other: NSID): Int = toString().compareTo(other.toString())
}