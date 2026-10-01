package io.kixi.kd

import io.kixi.text.ParseException
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

/**
 * Utility class for KD parsing, reading, and snip resolution.
 *
 * ## Basic Usage
 * ```kotlin
 * // Parse KD text
 * val tag = KD.read("person name=\"Alice\" age=30")
 *
 * // Read from file
 * val config = KD.read(File("config.kd"))
 *
 * // Parse a Ki literal
 * val date = KD("2024/3/15")
 * ```
 *
 * ## Snip Resolution
 * KD supports modular documents through snips - references to external KD files
 * that get expanded inline during parsing.
 *
 * ```kotlin
 * // Read with snip resolution (snips resolved relative to file location)
 * val doc = KD.readWithSnips(Path.of("app.kd"))
 *
 * // Custom snip options
 * val resolver = SnipResolver(SnipOptions(allowRemoteUrls = true))
 * val doc = KD.readWithSnips(Path.of("app.kd"), resolver)
 * ```
 *
 * @see Tag
 * @see KDParser
 * @see Snip
 * @see SnipResolver
 */
class KD {

    companion object {
        /** Reads a document as its top-level tags, without a synthetic root or snip resolution. */
        @JvmStatic
        fun readDocument(text: String): List<Tag> = KDParser().parseDocument(text)

        /** Reads a document without closing the caller-owned reader. */
        @JvmStatic
        fun readDocument(reader: Reader): List<Tag> = readDocument(reader.readText())

        /** Reads a UTF-8 document. Does not resolve snips. */
        @JvmStatic
        fun readDocument(file: File): List<Tag> = file.bufferedReader(Charsets.UTF_8).use { readDocument(it) }


        /**
         * Reads tags from the reader. If there is a single tag it is returned as is.
         * If there are multiple tags they are returned as children of a root tag
         * called "root".
         *
         * @param reader The reader to parse from
         * @return The parsed tag tree
         * @throws ParseException if parsing fails
         */
        @JvmStatic
        fun read(reader: Reader): Tag = KDParser().parse(reader.readText())

        /**
         * Reads tags from a string.
         *
         * @param text The KD text to parse
         * @return The parsed tag tree
         * @throws ParseException if parsing fails
         */
        @JvmStatic
        fun read(text: String): Tag = read(StringReader(text))

        /**
         * Reads tags from a file.
         *
         * Note: This method does NOT resolve snips. Use [readWithSnips] for
         * snip resolution.
         *
         * @param file The file to read
         * @return The parsed tag tree
         * @throws ParseException if parsing fails
         */
        @JvmStatic
        fun read(file: File): Tag = file.bufferedReader(Charsets.UTF_8).use { read(it) }

        /**
         * Reads tags from a URL.
         *
         * @param url The URL to read from
         * @return The parsed tag tree
         * @throws ParseException if parsing fails
         */
        @JvmStatic
        fun read(url: URL): Tag = read(url.readText())

        /**
         * Reads tags from a classpath resource.
         *
         * @param resource The resource path (without leading slash)
         * @return The parsed tag tree
         * @throws ParseException if parsing fails
         */
        @JvmStatic
        fun readResource(resource: String): Tag = read(
            requireNotNull(this::class.java.getResource("/${resource.removePrefix("/")}")) {
                "Classpath resource not found: $resource"
            }
        )

        /**
         * Create an object from a KD literal. This can be used like so:
         * ```kotlin
         * val birthday = KD("1995/9/16")
         * val price = KD("$29.99")
         * val duration = KD("2:30:00")
         * ```
         *
         * @param code A KD literal (https://github.com/kixi-io/Ki.Docs/wiki/Ki-Types)
         * @return The parsed Ki object
         * @throws KDParseException If `code` does not contain a valid KD literal
         */
        @JvmStatic
        operator fun invoke(code: String) = read(code).value

        // ====================================================================
        // Snip Resolution Functions
        // ====================================================================

        /**
         * Reads a KD file with snip resolution.
         *
         * Snips (`.snip(path)` directives) in the file are resolved relative to the
         * file's directory. Nested snips are supported up to the resolver's max depth.
         *
         * ## Example
         * ```kotlin
         * // config.kd contains:
         * // app {
         * //     .snip(shared/database)
         * //     .snip(shared/logging)
         * //     server { port 8080 }
         * // }
         *
         * val config = KD.readWithSnips(Path.of("config.kd"))
         * // Snips are replaced with content from shared/database.kd and shared/logging.kd
         * ```
         *
         * @param path Path to the KD file
         * @param snipResolver Resolver for snip directives (default: creates new with default options)
         * @return The parsed tag tree with all snips resolved
         * @throws KDParseException if parsing fails
         * @throws SnipPathNotFoundException if a snipped file doesn't exist
         * @throws SnipCircularReferenceException if circular snip references are detected
         * @throws SnipDepthExceededException if snip nesting exceeds max depth
         * @throws SnipSecurityException if a snip violates security constraints
         */
        @JvmStatic
        @JvmOverloads
        fun readWithSnips(
            path: Path,
            snipResolver: SnipResolver = SnipResolver()
        ): Tag {
            val absolutePath = path.toAbsolutePath().normalize()
            val content = Files.readString(absolutePath)
            val basePath = absolutePath.parent ?: Path.of(".")

            // Start chain with the main file to detect self-references
            val initialChain = listOf(absolutePath.toString())
            return parseWithSnips(content, basePath, snipResolver, initialChain)
        }

        /**
         * Reads a KD file with snip resolution.
         *
         * @param file The file to read
         * @param snipResolver Resolver for snip directives
         * @return The parsed tag tree with all snips resolved
         * @see readWithSnips(Path, SnipResolver)
         */
        @JvmStatic
        @JvmOverloads
        fun readWithSnips(
            file: File,
            snipResolver: SnipResolver = SnipResolver()
        ): Tag = readWithSnips(file.toPath(), snipResolver)

        /**
         * Parses KD text with snip resolution.
         *
         * @param text The KD text to parse
         * @param basePath Base path for resolving relative snips
         * @param snipResolver Resolver for snip directives
         * @param initialChain Initial chain of absolute paths for circular detection
         * @return The parsed tag tree with all snips resolved
         */
        @JvmStatic
        @JvmOverloads
        fun parseWithSnips(
            text: String,
            basePath: Path,
            snipResolver: SnipResolver = SnipResolver(),
            initialChain: List<String> = emptyList()
        ): Tag {
            snipResolver.reset()
            return resolvedRoot(resolveSnipTree(read(text), basePath, snipResolver, initialChain, 0))
        }

        internal fun resolvedRoot(tags: List<Tag>): Tag = when (tags.size) {
            1 -> tags.single()
            else -> Tag("root").apply { children.addAll(tags) }
        }

        internal fun resolveSnipTree(
            tag: Tag,
            basePath: Path,
            resolver: SnipResolver,
            chain: List<String>,
            depth: Int,
            baseUrl: String? = null
        ): List<Tag> {
            // Preserve the established readWithSnips/parseWithSnips contract:
            // anonymous string-valued snip tags are resolved even when quoted.
            // Plain KD.read continues to leave strings and directives unresolved.
            val legacyLiteral = if (tag.nsid.isAnonymous && tag.values.size == 1) {
                (tag.values.single() as? String)?.takeIf { Snip.isLiteral(it) }
            } else null
            val directive = tag.snipDirective ?: legacyLiteral?.let { Snip.parse(it) }
            if (directive != null) {
                if (!tag.nsid.isAnonymous || tag.values.size != 1 ||
                    tag.attributes.isNotEmpty() || tag.annotations.isNotEmpty() || tag.children.isNotEmpty()) {
                    throw KDParseException("A snip directive must appear alone in an anonymous tag")
                }
                val snip = if (baseUrl != null && !directive.isUrl) {
                    directive.copy(path = java.net.URI(baseUrl).resolve(directive.path).toString())
                } else directive
                return resolver.resolve(snip, basePath, chain, depth, -1, -1)
            }
            val children = tag.children.flatMap {
                resolveSnipTree(it, basePath, resolver, chain, depth, baseUrl)
            }
            tag.children.clear()
            tag.children.addAll(children)
            return listOf(tag)
        }
    }
}
