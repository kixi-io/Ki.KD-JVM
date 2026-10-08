package io.kixi.kd

import io.kixi.text.ParseException
import io.kixi.text.resolveEscapes
import java.nio.file.Path

/**
 * A reference from a KD document to the KDS schema that describes it, written
 * as the `.schema(path)` directive.
 *
 * The directive occupies an anonymous tag of its own and must be the first
 * top-level tag of the document:
 *
 * ```
 * .schema(assay)
 *
 * assay id="HTS-0042" {
 *     ...
 * }
 * ```
 *
 * The path is resolved against the directory of the file that contains the
 * directive, exactly like a snip. A path without an extension gets `.kds`
 * appended. Plain parsing records the directive on the tag without loading
 * anything; `io.kixi.kd.schema.KDS.read` compiles the referenced schema and
 * validates the rest of the document against it.
 *
 * @property path The schema path as written in the directive
 */
data class SchemaRef(val path: String) {

    /** The path with `.kds` appended when no extension was given. */
    val normalizedPath: String =
        if (path.substringAfterLast('/').substringAfterLast('\\').contains('.')) path else "$path.kds"

    /** Resolves [normalizedPath] against [basePath]. Absolute paths are returned as is. */
    fun resolve(basePath: Path): Path {
        val p = Path.of(normalizedPath)
        return if (p.isAbsolute) p.normalize() else basePath.resolve(p).normalize()
    }

    override fun toString(): String =
        if (path.any { it.isWhitespace() || it == '"' || it == ')' }) ".schema(\"${path.replace("\"", "\\\"")}\")"
        else ".schema($path)"

    companion object {
        private const val PREFIX = ".schema("

        /** True when [text] has the shape of a schema directive. */
        @JvmStatic
        fun isLiteral(text: String): Boolean {
            val trimmed = text.trim()
            return trimmed.startsWith(PREFIX) && trimmed.endsWith(")")
        }

        /**
         * Parses a schema directive such as `.schema(assay)` or
         * `.schema("schemas/plate reads.kds")`.
         *
         * @throws ParseException if the literal is malformed or the path is empty
         */
        @JvmStatic
        fun parse(text: String): SchemaRef {
            val trimmed = text.trim()
            if (!trimmed.startsWith(PREFIX)) throw ParseException("Schema directive must start with '.schema('")
            if (!trimmed.endsWith(")")) throw ParseException("Schema directive must end with ')'")
            val content = trimmed.substring(PREFIX.length, trimmed.length - 1).trim()
            if (content.isEmpty()) throw ParseException("Schema path cannot be empty")
            val path = if (content.startsWith("\"")) {
                if (content.length < 2 || !content.endsWith("\"")) throw ParseException("Unterminated quoted schema path")
                content.substring(1, content.length - 1).resolveEscapes()
            } else content
            if (path.isBlank()) throw ParseException("Schema path cannot be empty")
            if (path.contains(',')) throw ParseException("The schema directive takes a single path")
            return SchemaRef(path)
        }
    }
}
