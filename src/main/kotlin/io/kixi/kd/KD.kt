package io.kixi.kd

import io.kixi.NSID
import io.kixi.kd.schema.KDS
import io.kixi.kd.schema.KDSchema
import io.kixi.kd.schema.KDSchemaException
import io.kixi.kd.schema.SchemaCompiler
import io.kixi.text.ParseException
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

/**
 * Utility class for KD parsing, reading, and snip resolution.
 *
 * ## Loading documents
 * [load] is the one call for files: it parses, resolves snips, and applies
 * the schema the document declares, if any. Simple files stay simple.
 * ```kotlin
 * val doc = KD.load(File("run.kd"))     // Document
 * doc.root["id"]
 * doc.requireValid()                      // no-op when no schema is declared
 * ```
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
        // Loading: parse + snips + declared schema
        // ====================================================================

        /**
         * Loads a KD file: parses it, resolves snips relative to the file, and
         * if the document declares a schema, compiles it and validates the data.
         *
         * A document declares its schema with a `.schema(path)` directive or an
         * inline `schema <Name> kds=1 { ... }` tag, either of which must be the
         * first top-level tag. Documents without a declaration load as plain
         * data with [Document.schema] null and [Document.isValid] true, unless
         * [LoadOptions.requireSchema] is set.
         *
         * Data that violates its schema is returned, not thrown: inspect
         * [Document.isValid] and [Document.issues], or call
         * [Document.requireValid]. Schema problems (a declaration that is not
         * first, a missing or invalid schema file, an invalid inline schema)
         * throw [KDSchemaException].
         *
         * @throws KDParseException if the KD text does not parse
         * @throws KDSchemaException on schema problems
         * @throws SnipException on snip resolution problems
         */
        @JvmStatic
        @JvmOverloads
        fun load(file: File, options: LoadOptions = LoadOptions.DEFAULT): Document =
            load(file.toPath(), options)

        /** See [load]. */
        @JvmStatic
        @JvmOverloads
        fun load(path: Path, options: LoadOptions = LoadOptions.DEFAULT): Document {
            val absolute = path.toAbsolutePath().normalize()
            val text = Files.readString(absolute)
            return load(text, absolute.parent ?: Path.of("."), options, listOf(absolute.toString()))
        }

        /**
         * Loads KD text. With a [basePath], `.schema` directives and snips
         * resolve against it; without one a `.schema` directive is an error and
         * snips stay unresolved. An inline schema works either way.
         */
        @JvmStatic
        @JvmOverloads
        fun load(text: String, basePath: Path? = null, options: LoadOptions = LoadOptions.DEFAULT): Document =
            load(text, basePath, options, emptyList())

        private fun load(text: String, basePath: Path?, options: LoadOptions, chain: List<String>): Document {
            val tags = readDocument(text)
            val first = tags.firstOrNull()

            var schema: KDSchema? = null
            var data = tags
            val ref = first?.schemaDirective
            when {
                first == null -> {}
                ref != null -> {
                    if (first.values.size != 1 || first.attributes.isNotEmpty() || first.children.isNotEmpty())
                        throw KDSchemaException("/tags[0]", "A .schema directive must appear alone in its tag")
                    if (basePath == null)
                        throw KDSchemaException("/tags[0]", "$ref needs a base directory to resolve against")
                    val schemaFile = ref.resolve(basePath)
                    if (!Files.isRegularFile(schemaFile))
                        throw KDSchemaException("/tags[0]", "Schema file not found: $schemaFile")
                    schema = KDS.compile(schemaFile.toFile())
                    data = tags.drop(1)
                }
                isSchemaTag(first) -> {
                    schema = SchemaCompiler().compile(listOf(first))
                    data = tags.drop(1)
                }
            }
            if (schema == null && options.requireSchema)
                throw KDSchemaException("/tags[0]",
                    "Expected a .schema(path) directive or a schema tag as the first top-level tag" +
                            (if (first == null) "; the document is empty" else "; found ${describe(first)}"))

            data.forEachIndexed { i, tag -> rejectMisplaced(tag, "/tags[${i + 1}]") }

            val resolved = if (!options.resolveSnips || basePath == null) data else {
                options.snipResolver.reset()
                data.flatMap { resolveSnipTree(it, basePath, options.snipResolver, chain, 0) }
            }
            val validation = schema?.validate(resolved, options.validation)
            return Document(resolved, schema, validation)
        }

        private fun isSchemaTag(tag: Tag): Boolean =
            tag.name == "schema" && tag.namespace.isEmpty() && tag.attributes.containsKey(NSID("kds"))

        private fun rejectMisplaced(tag: Tag, path: String) {
            if (tag.schemaDirective != null)
                throw KDSchemaException(path, "A .schema directive is only allowed as the first top-level tag")
            if (isSchemaTag(tag))
                throw KDSchemaException(path, "A schema tag is only allowed as the first top-level tag")
            tag.children.forEachIndexed { i, child -> rejectMisplaced(child, "$path/children[$i]") }
        }

        private fun describe(tag: Tag): String =
            if (tag.nsid.isAnonymous) "an anonymous tag" else "tag ${tag.nsid}"

        // ====================================================================
        // Snip Resolution Functions (superseded by load)
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
        @Deprecated(
            "Use KD.load(path, LoadOptions(snipResolver = ...)).root; load also applies a declared schema.",
            ReplaceWith("KD.load(path, LoadOptions(snipResolver = snipResolver)).root", "io.kixi.kd.LoadOptions")
        )
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
        @Deprecated(
            "Use KD.load(file, LoadOptions(snipResolver = ...)).root; load also applies a declared schema.",
            ReplaceWith("KD.load(file, LoadOptions(snipResolver = snipResolver)).root", "io.kixi.kd.LoadOptions")
        )
        @JvmStatic
        @JvmOverloads
        @Suppress("DEPRECATION")
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
        @Deprecated(
            "Use KD.load(text, basePath, LoadOptions(snipResolver = ...)).root; load also applies a declared schema.",
            ReplaceWith("KD.load(text, basePath, LoadOptions(snipResolver = snipResolver)).root", "io.kixi.kd.LoadOptions")
        )
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
