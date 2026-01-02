package io.kixi.kd

import io.kixi.Parseable
import io.kixi.text.ParseException
import java.nio.file.Path

/**
 * A Snip is a reference to an external KD document that gets expanded inline during parsing.
 *
 * Snips enable modular KD documents by allowing one document to include content from another.
 * When a KD document containing a snip is parsed, the referenced file is loaded and its
 * root tag is inserted in place of the snip.
 *
 * ## Ki Literal Format
 * ```
 * .snip(path/to/file)              # Relative path (auto-appends .kd)
 * .snip(../components/button)      # Parent directory reference
 * .snip("path with spaces/file")   # Quoted for special characters
 * .snip(config.yaml)               # Explicit extension (no .kd added)
 * .snip(https://example.com/comp)  # URL reference
 * .snip(../toggles, expand=true)   # Insert only children of root tag
 * ```
 *
 * ## Path Resolution
 * - Paths are always relative to the containing KD file
 * - Use forward slashes (`/`) regardless of platform
 * - The `.kd` extension is auto-appended if no extension is present
 * - Absolute paths are supported but may pose security risks
 *
 * ## Unquoted Path Characters
 * Unquoted paths may only contain:
 * - Alphanumeric characters (a-z, A-Z, 0-9)
 * - Forward slash (`/`)
 * - Hyphen (`-`)
 * - Underscore (`_`)
 * - Period (`.`)
 * - Colon (`:`) - for URL schemes like `https://`
 *
 * Paths containing spaces, parentheses, or other special characters must be quoted.
 *
 * ## Expand Mode
 * By default, the entire root tag from the snipped file is inserted. With `expand=true`,
 * only the children of the root tag are inserted (the root tag itself is discarded).
 *
 * ## Example
 * **File: main.kd**
 * ```
 * app {
 *     header {
 *         .snip(components/navbar)
 *     }
 *     content {
 *         .snip(pages/home, expand=true)
 *     }
 * }
 * ```
 *
 * **File: components/navbar.kd**
 * ```
 * navbar {
 *     logo "MyApp"
 *     menu { item "Home"; item "About" }
 * }
 * ```
 *
 * **Result after expansion:**
 * ```
 * app {
 *     header {
 *         navbar {
 *             logo "MyApp"
 *             menu { item "Home"; item "About" }
 *         }
 *     }
 *     content {
 *         # Children of pages/home.kd root inserted here
 *     }
 * }
 * ```
 *
 * @property path The path or URL to the external KD document
 * @property expand If true, insert only the children of the root tag (default: false)
 *
 * @see SnipResolver
 * @see SnipOptions
 */
data class Snip(
    val path: String,
    val expand: Boolean = false
) {

    /**
     * Whether this snip references a URL rather than a file path.
     */
    val isUrl: Boolean = path.startsWith("http://") ||
            path.startsWith("https://") ||
            path.startsWith("file://")

    /**
     * Whether this snip uses HTTPS (secure) protocol.
     * Returns true for non-URL snips (file paths are considered "secure").
     */
    val isSecure: Boolean = !isUrl || path.startsWith("https://") || path.startsWith("file://")

    /**
     * Whether this snip references an absolute file path.
     *
     * **Security Warning:** Absolute paths can access files outside the document's
     * directory tree, which may pose security risks in untrusted environments.
     */
    val isAbsolutePath: Boolean = !isUrl && (
            path.startsWith("/") ||                    // Unix absolute
                    (path.length >= 2 && path[1] == ':')       // Windows absolute (C:, D:, etc.)
            )

    /**
     * The path with `.kd` extension added if no extension is present.
     *
     * If the path already has an extension (contains a `.` in the filename portion),
     * it is returned unchanged. Otherwise, `.kd` is appended.
     */
    val normalizedPath: String = if (hasExtension(path)) path else "$path.kd"

    /**
     * Resolves this snip's path relative to a base path.
     *
     * @param basePath The directory containing the KD file with this snip
     * @return The resolved absolute path to the snipped file
     * @throws IllegalStateException if called on a URL snip
     */
    fun resolve(basePath: Path): Path {
        if (isUrl) throw IllegalStateException("Cannot resolve URL snip as file path. Use SnipResolver for URL handling.")
        return basePath.resolve(normalizedPath).normalize()
    }

    /**
     * Returns the Ki literal representation of this snip.
     */
    override fun toString(): String {
        val pathStr = if (needsQuotes(path)) "\"$path\"" else path
        return if (expand) {
            ".snip($pathStr, expand=true)"
        } else {
            ".snip($pathStr)"
        }
    }

    companion object : Parseable<Snip> {

        /**
         * Regex for allowed characters in unquoted paths.
         * Allows: alphanumeric, forward slash, hyphen, underscore, period, colon (for URLs)
         */
        private val ALLOWED_UNQUOTED = Regex("""^[a-zA-Z0-9_./:-]+$""")

        /**
         * Checks if a path has a file extension.
         * Looks for a period in the last path segment (filename).
         */
        private fun hasExtension(path: String): Boolean {
            val lastSegment = path.substringAfterLast('/')
            return lastSegment.contains('.') && !lastSegment.startsWith('.')
        }

        /**
         * Checks if a path needs to be quoted in a literal.
         */
        private fun needsQuotes(path: String): Boolean {
            return !ALLOWED_UNQUOTED.matches(path)
        }

        /**
         * Checks if a string appears to be a snip literal.
         * This is a quick structural check, not a full validation.
         *
         * @param text The string to check
         * @return true if the string looks like a snip literal
         */
        @JvmStatic
        fun isLiteral(text: String): Boolean {
            val trimmed = text.trim()
            return trimmed.startsWith(".snip(") && trimmed.endsWith(")")
        }

        /**
         * Parses a Ki snip literal.
         *
         * ```kotlin
         * Snip.parse(".snip(components/button)")
         * Snip.parse(".snip(\"path with spaces\")")
         * Snip.parse(".snip(../config, expand=true)")
         * ```
         *
         * @param text The Ki snip literal string
         * @return The parsed Snip
         * @throws ParseException if the literal is malformed
         */
        @JvmStatic
        fun parse(text: String): Snip {
            val trimmed = text.trim()

            if (!trimmed.startsWith(".snip(")) {
                throw ParseException("Snip literal must start with '.snip('")
            }

            if (!trimmed.endsWith(")")) {
                throw ParseException("Snip literal must end with ')'")
            }

            // Extract content between .snip( and )
            val content = trimmed.substring(6, trimmed.length - 1).trim()

            if (content.isEmpty()) {
                throw ParseException("Snip path cannot be empty")
            }

            // Parse path and optional expand parameter
            val (path, expand) = parseContent(content)

            if (path.isEmpty()) {
                throw ParseException("Snip path cannot be empty")
            }

            return Snip(path, expand)
        }

        /**
         * Parses the content inside .snip(...), extracting path and expand flag.
         */
        private fun parseContent(content: String): Pair<String, Boolean> {
            var path: String
            var expand = false

            // Check for quoted path
            if (content.startsWith("\"")) {
                // Find the closing quote (handling escaped quotes)
                val endQuote = findClosingQuote(content, 0)
                if (endQuote == -1) {
                    throw ParseException("Unterminated quoted string in snip path")
                }

                path = content.substring(1, endQuote)

                // Check for expand parameter after the path
                val remaining = content.substring(endQuote + 1).trim()
                if (remaining.isNotEmpty()) {
                    expand = parseExpandParameter(remaining)
                }
            } else {
                // Unquoted path - look for comma separator or end
                val commaIndex = content.indexOf(',')

                if (commaIndex == -1) {
                    // No comma - entire content is the path
                    path = content.trim()

                    // Validate unquoted path characters
                    if (!ALLOWED_UNQUOTED.matches(path)) {
                        throw ParseException(
                            "Unquoted snip path contains invalid characters. " +
                                    "Use quotes for paths with spaces or special characters: $path"
                        )
                    }
                } else {
                    // Has comma - path is before, parameters after
                    path = content.substring(0, commaIndex).trim()

                    // Validate unquoted path characters
                    if (!ALLOWED_UNQUOTED.matches(path)) {
                        throw ParseException(
                            "Unquoted snip path contains invalid characters. " +
                                    "Use quotes for paths with spaces or special characters: $path"
                        )
                    }

                    val paramsPart = content.substring(commaIndex + 1).trim()
                    expand = parseExpandParameter(paramsPart)
                }
            }

            return Pair(path, expand)
        }

        /**
         * Finds the closing quote for a quoted string, handling escapes.
         */
        private fun findClosingQuote(content: String, startQuote: Int): Int {
            var i = startQuote + 1
            while (i < content.length) {
                when (content[i]) {
                    '\\' -> i += 2  // Skip escaped character
                    '"' -> return i
                    else -> i++
                }
            }
            return -1
        }

        /**
         * Parses the expand parameter from the remaining content after the path.
         */
        private fun parseExpandParameter(params: String): Boolean {
            // Remove leading comma if present
            val trimmed = params.trimStart(',').trim()

            if (trimmed.isEmpty()) return false

            // Parse expand=true or expand=false
            val expandMatch = Regex("""expand\s*=\s*(true|false)""", RegexOption.IGNORE_CASE)
                .find(trimmed)

            if (expandMatch != null) {
                return expandMatch.groupValues[1].equals("true", ignoreCase = true)
            }

            // If there's content but it's not a valid expand parameter, throw error
            throw ParseException("Invalid snip parameter: $trimmed. Expected 'expand=true' or 'expand=false'")
        }

        /**
         * Parses a Ki snip literal string into a Snip instance.
         *
         * @param text The Ki snip literal string to parse
         * @return The parsed Snip
         * @throws ParseException if the text cannot be parsed as a valid snip
         */
        override fun parseLiteral(text: String): Snip = parse(text)

        /**
         * Parses a snip literal, returning null on failure instead of throwing.
         *
         * @param text The snip literal string
         * @return The parsed Snip, or null if parsing fails
         */
        @JvmStatic
        fun parseOrNull(text: String): Snip? = try {
            parse(text)
        } catch (e: Exception) {
            null
        }
    }
}