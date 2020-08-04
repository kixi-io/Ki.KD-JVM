package io.kixi.kd

import io.kixi.text.ParseException

/**
 * A `ParseException` while reading KD code. Note that the index is the location within
 * the current line.
 *
 * @constructor
 */
class KDParseException(message:String, line:Int = -1, index:Int = -1, cause:Throwable? = null) :
        ParseException(message, line, index, cause) {}
