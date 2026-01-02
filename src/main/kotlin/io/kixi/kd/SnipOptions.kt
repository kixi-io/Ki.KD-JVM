package io.kixi.kd

/**
 * Configuration options for snip resolution.
 *
 * Controls how [SnipResolver] handles external file references, including security
 * settings for remote URLs and resource limits.
 *
 * ## Default Configuration
 * The default configuration is conservative for security:
 * - Remote URLs are **disabled** by default
 * - Absolute paths are allowed (with logging)
 * - Maximum snip depth is 50
 * - URL timeout is 10 seconds
 *
 * ## Usage
 * ```kotlin
 * // Create custom options
 * val options = SnipOptions(
 *     allowRemoteUrls = true,
 *     urlTimeoutMs = 5000,
 *     maxSnipDepth = 20
 * )
 *
 * // Use with parser
 * val resolver = SnipResolver(options)
 * val parser = KDParser(snipResolver = resolver)
 * ```
 *
 * ## Security Considerations
 *
 * ### Remote URLs
 * When `allowRemoteUrls` is true, the parser can fetch KD content from HTTP/HTTPS URLs.
 * This could potentially:
 * - Expose internal network structure through SSRF attacks
 * - Load malicious content from compromised servers
 * - Create denial-of-service through slow/infinite responses
 *
 * Enable remote URLs only when:
 * - You trust the source of the KD documents
 * - Network access is appropriately restricted
 * - Timeouts are configured
 *
 * ### Absolute Paths
 * Absolute file paths can access files outside the document's directory tree.
 * When `allowAbsolutePaths` is true (default), snips like `.snip(/etc/config)`
 * will be resolved. This is allowed by default for flexibility but can be
 * disabled in sandboxed environments.
 *
 * ### HTTP vs HTTPS
 * Non-HTTPS URLs transmit data in plaintext and cannot verify server identity.
 * When `warnOnInsecureUrls` is true (default), a warning is logged for HTTP URLs.
 * Set `requireHttps` to true to reject HTTP URLs entirely.
 *
 * @property allowRemoteUrls Whether to allow http:// and https:// URLs (default: false)
 * @property allowAbsolutePaths Whether to allow absolute file paths (default: true)
 * @property urlTimeoutMs Timeout in milliseconds for URL fetching (default: 10000)
 * @property maxSnipDepth Maximum nesting depth for snips to prevent infinite recursion (default: 50)
 * @property warnOnInsecureUrls Log a warning for non-HTTPS URLs (default: true)
 * @property requireHttps Reject HTTP URLs, only allow HTTPS (default: false)
 * @property cacheResolvedSnips Cache resolved snips within a parse operation (default: true)
 *
 * @see SnipResolver
 * @see Snip
 */
data class SnipOptions(
    /**
     * Whether to allow fetching content from remote URLs (http://, https://).
     *
     * **Security Warning:** Enabling this allows the parser to make network requests,
     * which could expose internal networks or load untrusted content.
     *
     * Default: `false`
     */
    val allowRemoteUrls: Boolean = false,

    /**
     * Whether to allow absolute file paths in snips.
     *
     * When enabled, snips like `.snip(/etc/config)` or `.snip(C:/data/file)` are allowed.
     * When disabled, only relative paths are permitted.
     *
     * **Security Note:** Absolute paths can access files outside the document tree.
     *
     * Default: `true`
     */
    val allowAbsolutePaths: Boolean = true,

    /**
     * Timeout in milliseconds for fetching remote URLs.
     *
     * If a URL takes longer than this to respond, a [SnipTimeoutException] is thrown.
     *
     * Default: `10000` (10 seconds)
     */
    val urlTimeoutMs: Long = 10_000,

    /**
     * Maximum depth of nested snips.
     *
     * Prevents stack overflow from deeply nested or circular references.
     * A [SnipDepthExceededException] is thrown if this limit is exceeded.
     *
     * Default: `50`
     */
    val maxSnipDepth: Int = 50,

    /**
     * Whether to log a warning when non-HTTPS URLs are used.
     *
     * HTTP URLs transmit data in plaintext. This option logs a warning but still
     * allows the request. Use [requireHttps] to reject HTTP URLs entirely.
     *
     * Default: `true`
     */
    val warnOnInsecureUrls: Boolean = true,

    /**
     * Whether to require HTTPS for remote URLs.
     *
     * When true, HTTP URLs will cause a [SnipSecurityException].
     * Has no effect if [allowRemoteUrls] is false.
     *
     * Default: `false`
     */
    val requireHttps: Boolean = false,

    /**
     * Whether to cache resolved snips within a single parse operation.
     *
     * When enabled, if the same file is snipped multiple times, it is only read once.
     * The cache is cleared between parse operations.
     *
     * Default: `true`
     */
    val cacheResolvedSnips: Boolean = true
) {

    init {
        require(urlTimeoutMs > 0) { "urlTimeoutMs must be positive" }
        require(maxSnipDepth > 0) { "maxSnipDepth must be positive" }
    }

    companion object {
        /**
         * Default options with conservative security settings.
         * - Remote URLs disabled
         * - Absolute paths allowed
         * - 10 second timeout
         * - Max depth 50
         */
        @JvmField
        val DEFAULT = SnipOptions()

        /**
         * Permissive options that allow remote URLs.
         * Use with caution in trusted environments only.
         */
        @JvmField
        val PERMISSIVE = SnipOptions(
            allowRemoteUrls = true,
            allowAbsolutePaths = true,
            urlTimeoutMs = 30_000,
            maxSnipDepth = 100,
            warnOnInsecureUrls = true,
            requireHttps = false
        )

        /**
         * Strict options for sandboxed/untrusted environments.
         * - No remote URLs
         * - No absolute paths
         * - Lower depth limit
         */
        @JvmField
        val STRICT = SnipOptions(
            allowRemoteUrls = false,
            allowAbsolutePaths = false,
            urlTimeoutMs = 5_000,
            maxSnipDepth = 20,
            warnOnInsecureUrls = true,
            requireHttps = true
        )

        /**
         * Options for secure remote fetching.
         * - HTTPS only (HTTP rejected)
         * - No absolute paths
         * - Short timeout
         */
        @JvmField
        val SECURE_REMOTE = SnipOptions(
            allowRemoteUrls = true,
            allowAbsolutePaths = false,
            urlTimeoutMs = 10_000,
            maxSnipDepth = 30,
            warnOnInsecureUrls = true,
            requireHttps = true
        )
    }

    /**
     * Creates a copy with remote URLs enabled.
     */
    fun withRemoteUrls(allow: Boolean = true): SnipOptions = copy(allowRemoteUrls = allow)

    /**
     * Creates a copy with absolute paths enabled/disabled.
     */
    fun withAbsolutePaths(allow: Boolean): SnipOptions = copy(allowAbsolutePaths = allow)

    /**
     * Creates a copy with the specified timeout.
     */
    fun withTimeout(timeoutMs: Long): SnipOptions = copy(urlTimeoutMs = timeoutMs)

    /**
     * Creates a copy with the specified max depth.
     */
    fun withMaxDepth(depth: Int): SnipOptions = copy(maxSnipDepth = depth)

    /**
     * Creates a copy that requires HTTPS for remote URLs.
     */
    fun requireHttps(require: Boolean = true): SnipOptions = copy(requireHttps = require)
}