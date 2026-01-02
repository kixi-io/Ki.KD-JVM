package io.kixi.kd

import io.kixi.text.ParseException

/**
 * Exception thrown when KD parsing fails.
 *
 * This extends [ParseException] to provide KD-specific error handling while
 * maintaining compatibility with the base Ki parsing infrastructure.
 *
 * @param message The error message
 * @param line The line number where the error occurred (-1 if unknown)
 * @param index The column/index where the error occurred (-1 if unknown)
 * @param cause The underlying cause, if any
 * @param suggestion Optional suggestion to help resolve the error
 *
 * @see ParseException
 */
open class KDParseException @JvmOverloads constructor(
    message: String,
    line: Int = -1,
    index: Int = -1,
    cause: Throwable? = null,
    suggestion: String? = null
) : ParseException(message, line, index, cause, suggestion) {

    companion object {
        /**
         * Creates a KDParseException for a single line of KD text.
         * The line number is disregarded.
         *
         * @param message The error message
         * @param index The column/index where the error occurred (-1 if unknown)
         * @param cause The underlying cause, if any
         * @param suggestion Optional suggestion to help resolve the error
         * @return A new KDParseException
         */
        @JvmStatic
        @JvmOverloads
        fun line(
            message: String,
            index: Int = -1,
            cause: Throwable? = null,
            suggestion: String? = null
        ): KDParseException {
            return KDParseException(message, -1, index, cause, suggestion)
        }
    }
}