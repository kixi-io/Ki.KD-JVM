package io.kixi.kd

import io.kixi.KiException

/**
 * Thrown when a snip path cannot be found.
 *
 * This can occur when:
 * - The file does not exist
 * - The path is invalid
 * - The URL is unreachable
 *
 * @property snipPath The original path from the snip literal
 * @property resolvedPath The fully resolved path that was searched for
 * @property line The line number where the snip was encountered (-1 if unknown)
 * @property index The column/index where the snip was encountered (-1 if unknown)
 *
 * ## Example
 * ```kotlin
 * try {
 *     KD.read("""
 *         config {
 *             .snip(missing/file)
 *         }
 *     """)
 * } catch (e: SnipPathNotFoundException) {
 *     println("Could not find: ${e.resolvedPath}")
 *     // Could not find: /path/to/missing/file.kd
 * }
 * ```
 *
 * @see Snip
 * @see SnipResolver
 */
class SnipPathNotFoundException @JvmOverloads constructor(
    val snipPath: String,
    val resolvedPath: String,
    val line: Int = -1,
    val index: Int = -1,
    cause: Throwable? = null,
    suggestion: String? = "Check that the file exists and the path is correct. " +
            "Paths are relative to the containing KD file."
) : KiException(
    "Snip path not found: $snipPath (resolved to: $resolvedPath)",
    suggestion,
    cause
) {
    override val message: String
        get() {
            val builder = StringBuilder("SnipPathNotFoundException")
            builder.append(" path: $snipPath")
            builder.append(" resolvedPath: $resolvedPath")
            if (line != -1) builder.append(" line: $line")
            if (index != -1) builder.append(" index: $index")
            cause?.let { builder.append(" cause: ${it.message}") }
            if (suggestion != null) builder.append(" Suggestion: $suggestion")
            return builder.toString()
        }
}

/**
 * Thrown when a circular snip reference is detected.
 *
 * Circular references occur when file A snips file B, and file B (directly or
 * indirectly) snips file A again. This would cause infinite recursion.
 *
 * @property snipPath The path that completed the cycle
 * @property chain The chain of snip references that led to the cycle
 * @property line The line number where the snip was encountered (-1 if unknown)
 * @property index The column/index where the snip was encountered (-1 if unknown)
 *
 * ## Example
 * Given:
 * - `a.kd` contains `.snip(b)`
 * - `b.kd` contains `.snip(c)`
 * - `c.kd` contains `.snip(a)`
 *
 * Parsing `a.kd` would throw:
 * ```kotlin
 * SnipCircularReferenceException(
 *     snipPath = "a.kd",
 *     chain = ["a.kd", "b.kd", "c.kd"]
 * )
 * // Message: "Circular snip reference: a.kd -> b.kd -> c.kd -> a.kd"
 * ```
 *
 * @see Snip
 * @see SnipResolver
 */
class SnipCircularReferenceException @JvmOverloads constructor(
    val snipPath: String,
    val chain: List<String>,
    val line: Int = -1,
    val index: Int = -1,
    suggestion: String? = "Reorganize your snip structure to avoid circular dependencies. " +
            "Consider extracting shared content into a separate file."
) : KiException(
    "Circular snip reference: ${chain.joinToString(" -> ")} -> $snipPath",
    suggestion
) {
    override val message: String
        get() {
            val builder = StringBuilder("SnipCircularReferenceException")
            builder.append(" circular reference: ${chain.joinToString(" -> ")} -> $snipPath")
            if (line != -1) builder.append(" line: $line")
            if (index != -1) builder.append(" index: $index")
            if (suggestion != null) builder.append(" Suggestion: $suggestion")
            return builder.toString()
        }
}

/**
 * Thrown when snip nesting exceeds the maximum depth.
 *
 * This is a safety limit to prevent stack overflow from very deeply nested
 * (but not necessarily circular) snip references.
 *
 * @property snipPath The path of the snip that exceeded the limit
 * @property currentDepth The depth at which the limit was exceeded
 * @property maxDepth The configured maximum depth limit
 * @property line The line number where the snip was encountered (-1 if unknown)
 * @property index The column/index where the snip was encountered (-1 if unknown)
 *
 * @see SnipOptions.maxSnipDepth
 */
class SnipDepthExceededException @JvmOverloads constructor(
    val snipPath: String,
    val currentDepth: Int,
    val maxDepth: Int,
    val line: Int = -1,
    val index: Int = -1,
    suggestion: String? = null
) : KiException(
    "Snip depth exceeded: $currentDepth > $maxDepth at $snipPath",
    suggestion ?: "Reduce snip nesting depth or increase SnipOptions.maxSnipDepth (current max: $maxDepth)."
) {
    override val message: String
        get() {
            val builder = StringBuilder("SnipDepthExceededException")
            builder.append(" depth: $currentDepth (max: $maxDepth)")
            builder.append(" path: $snipPath")
            if (line != -1) builder.append(" line: $line")
            if (index != -1) builder.append(" index: $index")
            if (suggestion != null) builder.append(" Suggestion: $suggestion")
            return builder.toString()
        }
}

/**
 * Thrown when a snip operation violates security constraints.
 *
 * This can occur when:
 * - Remote URLs are used but [SnipOptions.allowRemoteUrls] is false
 * - HTTP URLs are used but [SnipOptions.requireHttps] is true
 * - Absolute paths are used but [SnipOptions.allowAbsolutePaths] is false
 *
 * @property snipPath The path that violated security constraints
 * @property reason A description of the security violation
 * @property line The line number where the snip was encountered (-1 if unknown)
 * @property index The column/index where the snip was encountered (-1 if unknown)
 *
 * @see SnipOptions
 */
class SnipSecurityException @JvmOverloads constructor(
    val snipPath: String,
    val reason: String,
    val line: Int = -1,
    val index: Int = -1,
    suggestion: String? = null
) : KiException(
    "Snip security violation: $reason",
    suggestion
) {
    override val message: String
        get() {
            val builder = StringBuilder("SnipSecurityException")
            builder.append(" \"$reason\"")
            builder.append(" path: $snipPath")
            if (line != -1) builder.append(" line: $line")
            if (index != -1) builder.append(" index: $index")
            if (suggestion != null) builder.append(" Suggestion: $suggestion")
            return builder.toString()
        }

    companion object {
        /**
         * Creates an exception for disallowed remote URLs.
         */
        @JvmStatic
        fun remoteUrlNotAllowed(snipPath: String, line: Int = -1, index: Int = -1) =
            SnipSecurityException(
                snipPath,
                "Remote URLs are not allowed",
                line, index,
                "Enable remote URLs with SnipOptions.allowRemoteUrls=true"
            )

        /**
         * Creates an exception for insecure (HTTP) URLs when HTTPS is required.
         */
        @JvmStatic
        fun httpsRequired(snipPath: String, line: Int = -1, index: Int = -1) =
            SnipSecurityException(
                snipPath,
                "HTTPS is required for remote URLs",
                line, index,
                "Use https:// or disable requirement with SnipOptions.requireHttps=false"
            )

        /**
         * Creates an exception for disallowed absolute paths.
         */
        @JvmStatic
        fun absolutePathNotAllowed(snipPath: String, line: Int = -1, index: Int = -1) =
            SnipSecurityException(
                snipPath,
                "Absolute paths are not allowed",
                line, index,
                "Use relative paths or enable with SnipOptions.allowAbsolutePaths=true"
            )
    }
}

/**
 * Thrown when a remote URL fetch times out.
 *
 * @property snipPath The URL that timed out
 * @property timeoutMs The timeout value that was exceeded
 * @property line The line number where the snip was encountered (-1 if unknown)
 * @property index The column/index where the snip was encountered (-1 if unknown)
 *
 * @see SnipOptions.urlTimeoutMs
 */
class SnipTimeoutException @JvmOverloads constructor(
    val snipPath: String,
    val timeoutMs: Long,
    val line: Int = -1,
    val index: Int = -1,
    cause: Throwable? = null,
    suggestion: String? = null
) : KiException(
    "Snip URL fetch timed out after ${timeoutMs}ms: $snipPath",
    suggestion ?: "Check network connectivity or increase SnipOptions.urlTimeoutMs (current: ${timeoutMs}ms).",
    cause
) {
    override val message: String
        get() {
            val builder = StringBuilder("SnipTimeoutException")
            builder.append(" timeout: ${timeoutMs}ms")
            builder.append(" path: $snipPath")
            if (line != -1) builder.append(" line: $line")
            if (index != -1) builder.append(" index: $index")
            if (suggestion != null) builder.append(" Suggestion: $suggestion")
            return builder.toString()
        }
}

/**
 * Thrown when an error occurs while parsing a snipped file.
 *
 * This wraps parse errors from the snipped file, providing context about which
 * snip caused the error. Inherits from [KDParseException] since it represents
 * a parsing failure.
 *
 * @property snipPath The path to the file that failed to parse
 * @property parseError The original parse error from the snipped file
 * @property snipLine The line number where the snip directive was encountered (-1 if unknown)
 * @property snipIndex The column where the snip directive was encountered (-1 if unknown)
 *
 * @see KDParseException
 */
class SnipParseException @JvmOverloads constructor(
    val snipPath: String,
    val parseError: KDParseException,
    val snipLine: Int = -1,
    val snipIndex: Int = -1,
    suggestion: String? = "Check the syntax of the snipped file for errors."
) : KDParseException(
    "Error parsing snipped file: $snipPath",
    snipLine,
    snipIndex,
    parseError,
    suggestion
) {
    override val message: String
        get() {
            val builder = StringBuilder("SnipParseException")
            builder.append(" path: $snipPath")
            if (snipLine != -1) builder.append(" snipLine: $snipLine")
            if (snipIndex != -1) builder.append(" snipIndex: $snipIndex")
            builder.append(" parseError: ${parseError.message}")
            if (suggestion != null) builder.append(" Suggestion: $suggestion")
            return builder.toString()
        }
}

/**
 * Thrown when an I/O error occurs while reading a snipped file.
 *
 * This can occur when:
 * - File read permissions are denied
 * - Network errors occur during URL fetch
 * - File system errors occur
 *
 * @property snipPath The path that caused the I/O error
 * @property line The line number where the snip was encountered (-1 if unknown)
 * @property index The column/index where the snip was encountered (-1 if unknown)
 *
 * @see Snip
 * @see SnipResolver
 */
class SnipIOException @JvmOverloads constructor(
    val snipPath: String,
    val line: Int = -1,
    val index: Int = -1,
    cause: Throwable? = null,
    suggestion: String? = "Check file permissions and that the path is accessible."
) : KiException(
    "I/O error reading snip: $snipPath",
    suggestion,
    cause
) {
    override val message: String
        get() {
            val builder = StringBuilder("SnipIOException")
            builder.append(" path: $snipPath")
            if (line != -1) builder.append(" line: $line")
            if (index != -1) builder.append(" index: $index")
            cause?.let { builder.append(" cause: ${it.message}") }
            if (suggestion != null) builder.append(" Suggestion: $suggestion")
            return builder.toString()
        }
}