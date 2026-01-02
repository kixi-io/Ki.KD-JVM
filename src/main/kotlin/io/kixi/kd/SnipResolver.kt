package io.kixi.kd

import io.kixi.KiException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.logging.Logger

/**
 * Resolves and caches snip references during KD parsing.
 *
 * The resolver handles:
 * - File path resolution relative to the source document
 * - URL fetching with timeout support
 * - Circular reference detection
 * - Depth limiting
 * - Caching of resolved content
 * - Security policy enforcement
 *
 * ## Usage
 * ```kotlin
 * val options = SnipOptions(allowRemoteUrls = true)
 * val resolver = SnipResolver(options)
 *
 * // Parse a document, resolving snips relative to its location
 * val basePath = Path.of("/path/to/documents")
 * val content = resolver.resolve(snip, basePath, emptyList(), 0, 1, 1)
 * ```
 *
 * ## Resolution Process
 * 1. Check security constraints (URLs allowed? Absolute paths?)
 * 2. Check circular reference prevention
 * 3. Check depth limit
 * 4. Look up in cache (if enabled)
 * 5. Read content from file or URL
 * 6. Parse content
 * 7. Cache result (if enabled)
 * 8. Return root tag or children based on `expand` flag
 *
 * ## Thread Safety
 * SnipResolver maintains state (cache, resolution stack) and is **not thread-safe**.
 * Create a new resolver for each parse operation or synchronize access externally.
 *
 * @param options Configuration options for snip resolution
 *
 * @see Snip
 * @see SnipOptions
 */
class SnipResolver(
    val options: SnipOptions = SnipOptions.DEFAULT
) {

    private val logger = Logger.getLogger(SnipResolver::class.java.name)

    /**
     * Cache of resolved snips: normalized path -> parsed Tag
     */
    private val cache = mutableMapOf<String, Tag>()

    /**
     * Stack of paths currently being resolved (for circular reference detection)
     */
    private val resolutionStack = mutableListOf<String>()

    /**
     * Resolves a snip and returns the tag(s) to insert.
     *
     * If [Snip.expand] is false, returns a list containing just the root tag.
     * If [Snip.expand] is true, returns the children of the root tag.
     *
     * @param snip The snip to resolve
     * @param basePath The directory containing the KD file with the snip
     * @param currentChain The chain of files leading to this snip (for error messages)
     * @param currentDepth The current nesting depth
     * @param line The line number where the snip appears (for error messages)
     * @param index The column where the snip appears (for error messages)
     * @return List of tags to insert in place of the snip
     * @throws SnipPathNotFoundException if the snip file cannot be found
     * @throws SnipCircularReferenceException if a circular reference is detected
     * @throws SnipDepthExceededException if nesting exceeds the maximum depth
     * @throws SnipSecurityException if security constraints are violated
     * @throws SnipTimeoutException if a URL fetch times out
     * @throws SnipParseException if the snipped file contains parse errors
     * @throws SnipIOException if an I/O error occurs reading the snip
     */
    fun resolve(
        snip: Snip,
        basePath: Path,
        currentChain: List<String>,
        currentDepth: Int,
        line: Int,
        index: Int
    ): List<Tag> {
        // Check depth limit
        if (currentDepth >= options.maxSnipDepth) {
            throw SnipDepthExceededException(
                snip.path, currentDepth, options.maxSnipDepth, line, index
            )
        }

        // Determine the resolved path/URL (use absolute normalized path for consistency)
        val resolvedPath = if (snip.isUrl) {
            snip.normalizedPath
        } else {
            snip.resolve(basePath).toAbsolutePath().normalize().toString()
        }

        // Check for circular reference - check both internal stack and caller's chain
        if (resolutionStack.contains(resolvedPath) || currentChain.contains(resolvedPath)) {
            val fullChain = currentChain + resolutionStack
            throw SnipCircularReferenceException(
                snip.path, fullChain, line, index
            )
        }

        // Check security constraints
        checkSecurityConstraints(snip, line, index)

        // Check cache
        if (options.cacheResolvedSnips && cache.containsKey(resolvedPath)) {
            val cachedTag = cache[resolvedPath]!!
            return extractResult(cachedTag, snip.expand)
        }

        // Push onto resolution stack
        resolutionStack.add(resolvedPath)

        try {
            // Read content
            val content = if (snip.isUrl) {
                fetchUrl(snip, line, index)
            } else {
                readFile(snip, basePath, resolvedPath, line, index)
            }

            // Parse content
            val rootTag = parseContent(snip, content, resolvedPath, line, index)

            // Cache the result
            if (options.cacheResolvedSnips) {
                cache[resolvedPath] = rootTag
            }

            return extractResult(rootTag, snip.expand)

        } finally {
            // Pop from resolution stack
            resolutionStack.removeAt(resolutionStack.lastIndex)
        }
    }

    /**
     * Checks security constraints and throws if violated.
     */
    private fun checkSecurityConstraints(snip: Snip, line: Int, index: Int) {
        if (snip.isUrl) {
            // Check if remote URLs are allowed
            if (!options.allowRemoteUrls) {
                throw SnipSecurityException.remoteUrlNotAllowed(snip.path, line, index)
            }

            // Check HTTPS requirement
            if (options.requireHttps && !snip.isSecure) {
                throw SnipSecurityException.httpsRequired(snip.path, line, index)
            }

            // Warn about insecure URLs
            if (options.warnOnInsecureUrls && !snip.isSecure) {
                logger.warning("Insecure URL in snip: ${snip.path}. Consider using HTTPS.")
            }
        } else if (snip.isAbsolutePath) {
            // Check if absolute paths are allowed
            if (!options.allowAbsolutePaths) {
                throw SnipSecurityException.absolutePathNotAllowed(snip.path, line, index)
            }
        }
    }

    /**
     * Reads content from a file.
     */
    private fun readFile(
        snip: Snip,
        basePath: Path,
        resolvedPath: String,
        line: Int,
        index: Int
    ): String {
        val path = Path.of(resolvedPath)

        if (!Files.exists(path)) {
            throw SnipPathNotFoundException(snip.path, resolvedPath, line, index)
        }

        try {
            return Files.readString(path)
        } catch (e: IOException) {
            throw SnipIOException(snip.path, line, index, e)
        }
    }

    /**
     * Fetches content from a URL with timeout support.
     */
    private fun fetchUrl(snip: Snip, line: Int, index: Int): String {
        val urlString = snip.normalizedPath

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            connection.connectTimeout = options.urlTimeoutMs.toInt()
            connection.readTimeout = options.urlTimeoutMs.toInt()
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "text/plain, application/x-kd, */*")

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw SnipPathNotFoundException(
                    snip.path, urlString, line, index,
                    IOException("HTTP $responseCode: ${connection.responseMessage}")
                )
            }

            return connection.inputStream.bufferedReader().use { it.readText() }

        } catch (e: java.net.SocketTimeoutException) {
            throw SnipTimeoutException(snip.path, options.urlTimeoutMs, line, index, e)
        } catch (e: KiException) {
            throw e  // Re-throw Ki exceptions as-is (includes all snip exceptions)
        } catch (e: Exception) {
            throw SnipIOException(snip.path, line, index, e)
        }
    }

    /**
     * Parses the content of a snipped file.
     */
    private fun parseContent(
        snip: Snip,
        content: String,
        resolvedPath: String,
        line: Int,
        index: Int
    ): Tag {
        try {
            // Create a new parser with this resolver for nested snips
            val parser = KDParser()

            // Calculate the new base path for nested snips
            val newBasePath = if (snip.isUrl) {
                // For URLs, extract the base URL
                val lastSlash = resolvedPath.lastIndexOf('/')
                if (lastSlash > 0) resolvedPath.substring(0, lastSlash) else resolvedPath
            } else {
                Path.of(resolvedPath).parent?.toString() ?: "."
            }

            // Parse the content
            // Note: The parser would need to be extended to support snip resolution
            // For now, we parse without nested snip support
            return parser.parse(content)

        } catch (e: KDParseException) {
            throw SnipParseException(
                snipPath = snip.path,
                parseError = e,
                snipLine = line,
                snipIndex = index
            )
        }
    }

    /**
     * Extracts the result based on the expand flag.
     */
    private fun extractResult(rootTag: Tag, expand: Boolean): List<Tag> {
        return if (expand) {
            rootTag.children.toList()
        } else {
            listOf(rootTag)
        }
    }

    /**
     * Clears the resolution cache.
     *
     * Call this between parse operations if reusing the resolver.
     */
    fun clearCache() {
        cache.clear()
    }

    /**
     * Clears the resolution stack.
     *
     * This is automatically managed during resolution, but can be called
     * to reset state after an error.
     */
    fun clearStack() {
        resolutionStack.clear()
    }

    /**
     * Resets all state (cache and stack).
     */
    fun reset() {
        clearCache()
        clearStack()
    }

    /**
     * Returns the current resolution stack (for debugging).
     */
    fun getResolutionStack(): List<String> = resolutionStack.toList()

    /**
     * Returns the current cache keys (for debugging).
     */
    fun getCachedPaths(): Set<String> = cache.keys.toSet()

    companion object {
        /**
         * Creates a resolver with default options.
         */
        @JvmStatic
        fun create(): SnipResolver = SnipResolver(SnipOptions.DEFAULT)

        /**
         * Creates a resolver that allows remote URLs.
         */
        @JvmStatic
        fun createWithRemoteUrls(): SnipResolver = SnipResolver(SnipOptions.PERMISSIVE)

        /**
         * Creates a resolver with strict security settings.
         */
        @JvmStatic
        fun createStrict(): SnipResolver = SnipResolver(SnipOptions.STRICT)
    }
}