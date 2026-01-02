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
        fun read(file: File): Tag = read(FileReader(file))

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
            this::class.java.getResource("/$resource")
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
            // First, parse the document
            val rootTag = read(text)

            // Then, recursively resolve snips
            resolveSnipsInTag(rootTag, basePath, snipResolver, initialChain, 0)

            return rootTag
        }

        /**
         * Recursively resolves snips within a tag tree.
         *
         * This modifies the tag tree in place, replacing snip placeholders with
         * the resolved content.
         *
         * @param tag The tag to process
         * @param basePath Base path for resolving relative snips
         * @param resolver The snip resolver
         * @param chain Chain of absolute paths for circular reference detection
         * @param depth Current nesting depth
         */
        private fun resolveSnipsInTag(
            tag: Tag,
            basePath: Path,
            resolver: SnipResolver,
            chain: List<String>,
            depth: Int
        ) {
            // Process children, replacing snips with resolved content
            val newChildren = mutableListOf<Tag>()

            for (child in tag.children) {
                if (isSnipTag(child)) {
                    // This is a snip - resolve it
                    val snip = extractSnip(child)
                    val line = 1  // TODO: Track line numbers through parsing
                    val index = 1

                    val resolvedTags = resolver.resolve(
                        snip, basePath, chain, depth, line, index
                    )

                    // Calculate new base path and absolute path for nested snips
                    val resolvedAbsolutePath = if (snip.isUrl) {
                        snip.path  // URLs stay as-is
                    } else {
                        snip.resolve(basePath).toAbsolutePath().normalize().toString()
                    }

                    val newBasePath = if (snip.isUrl) {
                        basePath  // Keep original for URLs
                    } else {
                        snip.resolve(basePath).parent ?: basePath
                    }

                    // Update chain with absolute path for circular detection
                    val newChain = chain + resolvedAbsolutePath

                    // Recursively resolve snips in the resolved content
                    for (resolved in resolvedTags) {
                        resolveSnipsInTag(resolved, newBasePath, resolver, newChain, depth + 1)
                        newChildren.add(resolved)
                    }
                } else {
                    // Regular tag - recurse into its children
                    resolveSnipsInTag(child, basePath, resolver, chain, depth)
                    newChildren.add(child)
                }
            }

            // Replace children with resolved content
            tag.children.clear()
            tag.children.addAll(newChildren)
        }

        /**
         * Checks if a tag represents a snip directive.
         *
         * A snip tag is an anonymous tag with a single string value that matches
         * the snip literal pattern `.snip(...)`.
         *
         * @param tag The tag to check
         * @return true if this tag represents a snip
         */
        private fun isSnipTag(tag: Tag): Boolean {
            if (!tag.nsid.isAnonymous) return false
            if (tag.values.size != 1) return false

            val value = tag.values[0]
            return value is String && Snip.isLiteral(value)
        }

        /**
         * Extracts a Snip from a snip tag.
         *
         * @param tag The tag containing the snip literal
         * @return The parsed Snip
         */
        private fun extractSnip(tag: Tag): Snip {
            val literal = tag.values[0] as String
            return Snip.parse(literal)
        }
    }
}