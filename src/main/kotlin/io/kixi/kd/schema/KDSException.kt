package io.kixi.kd.schema

import io.kixi.kd.TagEntity

/**
 * A `KDSException` indicates that a tag structure does not match its corresponding KD
 * Schema.
 *
 * @property entity Any
 * @constructor
 */
class KDSException(message:String, var entity: TagEntity? = null, cause:Throwable? = null) :
        RuntimeException(if(entity==null) message else getTagDesc(entity) + ": $message", cause) {


    companion object {
        fun getTagDesc(tagEntity: TagEntity): String {
            var tagName = tagEntity.nsid.toString()
            return if (tagName == "") "(anonymous)" else tagName
        }
    }
}