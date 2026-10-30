package io.kixi.kd

import io.kixi.*
import io.kixi.text.ParseException
import io.kixi.text.resolveEscapes
import io.kixi.uom.Currency
import io.kixi.uom.Length
import io.kixi.uom.Quantity
import io.kixi.uom.Unit
import java.math.BigDecimal as Dec
import java.net.URL
import java.time.*

/** An unsigned or signed run of digits (with `_` separators): a whole number as KD writes one. */
private val BIG_WHOLE = Regex("[+-]?[0-9][0-9_]*")

/**
 * A complete KD (Ki Data) parser supporting core KD features.
 *
 * ## Supported Literal Types
 *
 * ### Phase 1 (Core)
 * 1. String - simple, raw, block, and block-raw forms
 * 2. Char - single characters in single quotes
 * 3. Int - 32-bit signed integers with optional underscores
 * 4. Long - 64-bit signed integers with 'L' suffix
 * 5. Float - 32-bit floats with 'f' or 'F' suffix
 * 6. Double - 64-bit floats (default for decimals) or 'd'/'D' suffix
 * 7. Dec - BigDecimal with 'bd' or 'BD' suffix
 * 8. Bool - true/false (also on/off for SDL compatibility)
 * 9. URL - URLs enclosed in angle brackets or naked
 * 10. Email - email addresses (e.g., user@domain.com)
 *
 * ### Phase 2 (DateTime & Complex)
 * 11. Date - LocalDate in y/M/d format
 * 12. LocalDateTime - date with time in y/M/d@H:mm:ss format
 * 13. ZonedDateTime - date/time with numeric zone offset (e.g., +5, -8:30)
 * 14. KiTZDateTime - date/time with KiTZ timezone (e.g., -JP/JST, -US/PST, -Z)
 * 15. Duration - time duration in compound or unit format
 * 16. Version - semantic version (major.minor.micro-qualifier)
 * 17. Blob - Base64 encoded binary data (.blob(...))
 * 18. GeoPoint - geographic coordinates (.geo(lat, lon, alt?))
 *
 * ### Phase 3 (Collections & Quantities)
 * 19. Quantity - number with unit of measure (e.g., 5cm, 23.5kg)
 * 20. Range - inclusive/exclusive ranges (e.g., 1..10, 1<..<10)
 * 21. List - ordered collection (e.g., [1, 2, 3])
 * 22. Map - key-value pairs (e.g., [a=1, b=2])
 * 23. Call - function call (e.g., rgb(255, 128, 0))
 * 24. nil - absence of value (nil or null)
 *
 * ### Phase 4 (Grid & Coordinate)
 * 25. Coordinate - 2D/3D grid positions (.coordinate(x=0, y=0) or .coordinate(c="A", r=1))
 * 26. Grid - 2D tabular data (.grid(...) with rows of values)
 * 27. Snip - external document reference (.snip(path) or .snip(path, expand=true))
 * 28. Schema directive - reference to the document's KDS schema (.schema(path))
 *
 * ## Tag Structure
 * ```
 * @annotation(s)
 * namespace:name value(s) attribute(s) {
 *     children
 * }
 * ```
 *
 * ## Anonymous Tags
 * Anonymous tags have an empty string as their name. They can start with:
 * - A value literal (e.g., `"hello"`, `42`, `true`)
 * - An attribute (e.g., `name="Jose"`, `age=35`)
 *
 * Example of attribute-only anonymous tags (config file style):
 * ```
 * host = "localhost"
 * port = 8080
 * debug = true
 * ```
 *
 * See [KD Docs](https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD)) for details.
 *
 * @see Tag
 * @see Annotation
 */
class KDParser internal constructor(private val interpolation: Interpolation?) {

    constructor() : this(null)

    /** A value put in at a position of the text, up to [end] (`$x` in a `lang KD` block, [KDLanguage]). */
    internal class Interpolated(val end: Int, val value: Any?)

    /**
     * The host's values for a `lang KD` block, by the position of each interpolation
     * in the text, and how a value reads inside a string ([stringify]).
     */
    internal class Interpolation(val spans: Map<Int, Interpolated>, val stringify: (Any?) -> String)

    /** A parse error that also knows its position in the text, for [KDLanguage]. */
    internal class PositionedException(val reason: String, line: Int, column: Int, val offset: Int) :
        KDParseException(reason, line, column)

    /**
     * Parses KD content and returns a Tag.
     *
     * If there is a single top-level tag, it is returned directly.
     * If there are multiple top-level tags, they are wrapped in a root tag named "root".
     *
     * @param content The KD content to parse
     * @return The parsed Tag
     * @throws ParseException if the content cannot be parsed
     */
    fun parse(content: String): Tag {
        val tags = parseDocument(content)
        return when (tags.size) {
            0 -> Tag("root")
            1 -> tags[0]
            else -> Tag("root").apply { children.addAll(tags) }
        }
    }

    /** Parses top-level tags without introducing a synthetic root or resolving snips. */
    fun parseDocument(content: String): List<Tag> {
        val ctx = ParseContext(content, positioned = interpolation != null)
        val tags = mutableListOf<Tag>()

        while (!ctx.isEOF()) {
            skipWhitespaceAndComments(ctx)
            if (ctx.isEOF()) break

            val posBefore = ctx.pos
            val tag = parseTag(ctx)
            if (tag != null) {
                tags.add(tag)
            }
            // Always check for progress, regardless of whether tag was parsed
            if (ctx.pos == posBefore && !ctx.isEOF()) {
                throw ctx.error("Unexpected character '${ctx.peek()}'")
            }
        }

        return tags.toList()
    }

    // ========================================================================
    // Parse Context
    // ========================================================================

    private data class ParseState(val pos: Int, val line: Int, val column: Int)

    private class ParseContext(val content: String, val positioned: Boolean = false) {
        var pos = 0
        var line = 1
        var column = 1
        val length = content.length

        fun peek(): Char? = if (pos < length) content[pos] else null
        fun peek(offset: Int): Char? = if (pos + offset < length) content[pos + offset] else null

        fun advance(): Char? {
            if (pos >= length) return null
            val ch = content[pos++]
            if (ch == '\n') {
                line++
                column = 1
            } else {
                column++
            }
            return ch
        }

        fun advance(count: Int) {
            repeat(count) { advance() }
        }

        fun saveState(): ParseState = ParseState(pos, line, column)

        fun restoreState(state: ParseState) {
            pos = state.pos
            line = state.line
            column = state.column
        }

        fun isEOF(): Boolean = pos >= length
        fun remaining(): String = if (pos < length) content.substring(pos) else ""
        fun substring(start: Int, end: Int = pos): String = content.substring(start, end)
        fun error(message: String): KDParseException =
            if (positioned) PositionedException(message, line, column, pos) else KDParseException(message, line, column)
        fun errorAt(state: ParseState, message: String): KDParseException =
            if (positioned) PositionedException(message, state.line, state.column, state.pos) else KDParseException(message, state.line, state.column)
    }

    // ========================================================================
    // Whitespace and Comment Handling
    // ========================================================================

    private fun skipWhitespaceAndComments(ctx: ParseContext) {
        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break

            when {
                ch.isWhitespace() -> ctx.advance()
                ch == '#' -> skipToEndOfLine(ctx)
                ch == '/' -> {
                    when (ctx.peek(1)) {
                        '/' -> skipToEndOfLine(ctx)
                        '*' -> skipBlockComment(ctx)
                        else -> return
                    }
                }
                else -> return
            }
        }
    }

    private fun skipSpacesAndTabs(ctx: ParseContext) {
        while (!ctx.isEOF()) {
            val ch = ctx.peek()
            if (ch == ' ' || ch == '\t') {
                ctx.advance()
            } else {
                break
            }
        }
    }

    private fun skipToEndOfLine(ctx: ParseContext) {
        while (!ctx.isEOF() && ctx.peek() != '\n') {
            ctx.advance()
        }
        if (ctx.peek() == '\n') ctx.advance()
    }

    private fun skipBlockComment(ctx: ParseContext) {
        ctx.advance(2) // skip /*
        var depth = 1

        while (!ctx.isEOF() && depth > 0) {
            val ch = ctx.peek()
            val next = ctx.peek(1)

            when {
                ch == '/' && next == '*' -> { ctx.advance(2); depth++ }
                ch == '*' && next == '/' -> { ctx.advance(2); depth-- }
                else -> ctx.advance()
            }
        }
        if (depth > 0) throw ctx.error("Unterminated block comment")
    }

    // ========================================================================
    // Tag Parsing
    // ========================================================================

    private fun parseTag(ctx: ParseContext): Tag? {
        skipWhitespaceAndComments(ctx)
        if (ctx.isEOF()) return null

        val annotations = parseAnnotations(ctx)

        skipWhitespaceAndComments(ctx)
        if (ctx.isEOF()) {
            if (annotations.isNotEmpty()) throw ctx.error("Expected tag after annotations")
            return null
        }

        if (ctx.peek() == ';') {
            ctx.advance()
            return null
        }

        val savedState = ctx.saveState()
        val nsid = tryParseNSID(ctx)

        val tag: Tag
        if (nsid != null) {
            val name = nsid.name

            // Check if this is actually an email (identifier followed by @ or email chars then @)
            // If so, treat as anonymous tag with email value
            // But NOT if @ is followed by " (that's a raw string)
            if (!nsid.hasNamespace) {
                val nextCh = ctx.peek()
                val nextCh2 = ctx.peek(1)
                // Check for @ but exclude @" which is raw string syntax
                val couldBeEmail = (nextCh == '@' && nextCh2 != '"') ||
                        nextCh == '+' || nextCh == '.' || nextCh == '-' || nextCh == '%'
                if (couldBeEmail) {
                    // Look ahead to see if there's an @ sign (indicating email)
                    val lookAheadState = ctx.saveState()
                    while (!ctx.isEOF()) {
                        val ch = ctx.peek() ?: break
                        if (ch == '@') {
                            // Make sure @ is not followed by " (raw string)
                            if (ctx.peek(1) == '"') {
                                break
                            }
                            // This is an email pattern, parse as anonymous tag
                            ctx.restoreState(savedState)
                            tag = Tag(NSID.ANONYMOUS)
                            tag.annotations.addAll(annotations)
                            parseValuesAndAttributes(ctx, tag)
                            skipSpacesAndTabs(ctx)
                            if (ctx.peek() == '{') {
                                ctx.advance()
                                parseChildren(ctx, tag)
                            }
                            return tag
                        } else if (ch.isLetterOrDigit() || ch == '+' || ch == '.' || ch == '-' || ch == '%' || ch == '_') {
                            ctx.advance()
                        } else {
                            break
                        }
                    }
                    ctx.restoreState(lookAheadState)
                }
            }

            // Check if this is an attribute-only anonymous tag (identifier followed by =)
            // e.g., "name=jose" or "ns:name=jose" should be anonymous tags with attributes
            // We check this regardless of whether the NSID has a namespace
            val attrCheckState = ctx.saveState()
            skipSpacesAndTabs(ctx)
            if (ctx.peek() == '=') {
                // This is an attribute-only anonymous tag
                ctx.restoreState(savedState)
                tag = Tag(NSID.ANONYMOUS)
                tag.annotations.addAll(annotations)
                parseValuesAndAttributes(ctx, tag, isAttributeOnlyAnonymous = true)
                skipSpacesAndTabs(ctx)
                if (ctx.peek() == '{') {
                    ctx.advance()
                    parseChildren(ctx, tag)
                }
                return tag
            }
            ctx.restoreState(attrCheckState)

            if (!nsid.hasNamespace && isKeyword(name)) {
                tag = Tag(NSID.ANONYMOUS)
                tag.values.add(keywordToValue(name))
            } else {
                tag = Tag(nsid)
            }
        } else {
            ctx.restoreState(savedState)
            val ch = ctx.peek()
            if (ch != null && isValueStart(ch, ctx)) {
                tag = Tag(NSID.ANONYMOUS)
            } else {
                return null
            }
        }

        tag.annotations.addAll(annotations)
        parseValuesAndAttributes(ctx, tag)

        skipSpacesAndTabs(ctx)
        if (ctx.peek() == '{') {
            ctx.advance()
            parseChildren(ctx, tag)
        }

        return tag
    }

    private fun isKeyword(name: String): Boolean =
        name == "true" || name == "false" || name == "nil" || name == "null"

    private fun keywordToValue(name: String): Any? = when (name) {
        "true" -> true
        "false" -> false
        "nil", "null" -> null
        else -> throw IllegalArgumentException("Not a keyword: $name")
    }

    private fun parseAnnotations(ctx: ParseContext): List<Annotation> {
        val annotations = mutableListOf<Annotation>()

        while (!ctx.isEOF()) {
            skipWhitespaceAndComments(ctx)

            // Check for '@' but NOT '@"' which is a raw string
            if (ctx.peek() == '@' && ctx.peek(1) != '"') {
                val annotation = parseAnnotation(ctx)
                if (annotation != null) {
                    annotations.add(annotation)
                }
            } else {
                break
            }
        }

        return annotations
    }

    private fun parseAnnotation(ctx: ParseContext): Annotation? {
        if (ctx.peek() != '@') return null
        ctx.advance()

        // For annotations, '(' means parameters, not a call - so disable call detection
        val nsid = tryParseNSID(ctx, detectCalls = false) ?: throw ctx.error("Expected annotation name after '@'")
        val annotation = Annotation(nsid)

        skipSpacesAndTabs(ctx)
        if (ctx.peek() == '(') {
            ctx.advance()
            parseAnnotationParams(ctx, annotation)
        }

        return annotation
    }

    private fun parseAnnotationParams(ctx: ParseContext, annotation: Annotation) {
        while (!ctx.isEOF()) {
            skipWhitespaceAndComments(ctx)

            if (ctx.peek() == ')') {
                ctx.advance()
                return
            }

            if (isAttributeStart(ctx)) {
                val (nsid, value) = parseAttribute(ctx)
                annotation.setAttribute(nsid, value)
            } else {
                val value = parseRequiredValue(ctx)
                annotation.values.add(value)
            }

            skipSpacesAndTabs(ctx)
            if (ctx.peek() == ',') ctx.advance()
        }
        throw ctx.error("Unterminated annotation: expected ')'")
    }

    private fun parseValuesAndAttributes(ctx: ParseContext, tag: Tag, isAttributeOnlyAnonymous: Boolean = false) {
        while (!ctx.isEOF()) {
            skipSpacesAndTabs(ctx)

            while (ctx.peek() == '/' && ctx.peek(1) == '*') {
                skipBlockComment(ctx)
                skipSpacesAndTabs(ctx)
            }

            val ch = ctx.peek() ?: break

            if (ch == '{' || ch == ';' || ch == '}') {
                break
            }

            // Handle newlines - check if next line has attributes
            if (ch == '\n' || ch == '\r') {
                // For attribute-only anonymous tags, newline ends the tag
                // Each line is a separate anonymous tag in config-file style
                if (isAttributeOnlyAnonymous) {
                    break
                }

                val savedState = ctx.saveState()
                skipWhitespaceAndComments(ctx)

                val nextCh = ctx.peek()
                // If next non-whitespace is end of content, block markers, or not an attribute, stop
                if (nextCh == null || nextCh == '{' || nextCh == '}' || nextCh == ';') {
                    ctx.restoreState(savedState)
                    break
                }

                // Check if next line starts with an attribute (identifier=)
                if (isAttributeStart(ctx)) {
                    // Continue parsing - it's a continuation of attributes
                    continue
                } else {
                    // Not an attribute - this is a new tag or something else
                    ctx.restoreState(savedState)
                    break
                }
            }

            if (ch == '#' || (ch == '/' && ctx.peek(1) == '/')) {
                while (!ctx.isEOF() && ctx.peek() != '\n' && ctx.peek() != '\r') ctx.advance()
                continue
            }

            if (ch == '\\') {
                ctx.advance()
                skipToEndOfLine(ctx)
                continue
            }

            if (isAttributeStart(ctx)) {
                val (nsid, value) = parseAttribute(ctx)
                tag.setAttribute(nsid, value)
            } else {
                interpolation?.spans?.get(ctx.pos)?.let { span ->
                    if (span.end < ctx.length && ctx.content[span.end] == '=')
                        throw ctx.error("An attribute's name cannot be interpolated, only its value (name=\$x)")
                }
                val directiveStart = ctx.peek() == '.'
                val value = parseRequiredValue(ctx)
                if (directiveStart && value is String) {
                    if (Snip.isLiteral(value)) tag.snipDirective = Snip.parse(value)
                    else if (SchemaRef.isLiteral(value)) tag.schemaDirective = SchemaRef.parse(value)
                }
                tag.values.add(value)
            }
        }
    }

    private fun parseChildren(ctx: ParseContext, parent: Tag) {
        while (!ctx.isEOF()) {
            skipWhitespaceAndComments(ctx)

            if (ctx.peek() == '}') {
                ctx.advance()
                return
            }

            val posBefore = ctx.pos
            val child = parseTag(ctx)
            if (child != null) {
                parent.children.add(child)
            } else if (ctx.pos == posBefore && !ctx.isEOF() && ctx.peek() != '}') {
                throw ctx.error("Unexpected character '${ctx.peek()}' in children block")
            }
        }
        throw ctx.error("Unterminated children block: expected '}'")
    }

    // ========================================================================
    // NSID Parsing
    // ========================================================================

    private fun tryParseNSID(ctx: ParseContext, detectCalls: Boolean = true): NSID? {
        val startPos = ctx.saveState()
        val first = parseIdentifier(ctx) ?: return null

        // Check if this looks like a Call (identifier followed by '(')
        // If so, return null so it gets parsed as a value instead of tag name
        // Skip this check for annotation names where '(' means parameters
        if (detectCalls) {
            skipSpacesAndTabs(ctx)
            if (ctx.peek() == '(') {
                // Backtrack - this is a call, not a tag NSID
                ctx.restoreState(startPos)
                return null
            }
        }

        if (ctx.peek() == ':' && ctx.peek(1) != '=') {
            // Could be namespace:name or namespace:name(...)
            ctx.advance()
            val name = parseIdentifier(ctx) ?: return null

            // Check if namespaced identifier is followed by '(' (a call)
            if (detectCalls) {
                skipSpacesAndTabs(ctx)
                if (ctx.peek() == '(') {
                    // Backtrack - this is a namespaced call, not a tag NSID
                    ctx.restoreState(startPos)
                    return null
                }
            }

            return NSID(name, first)
        }

        return NSID(first)
    }

    private fun parseIdentifier(ctx: ParseContext): String? {
        val start = ctx.pos
        val first = ctx.peek() ?: return null

        if (!isIdentifierStart(first)) return null
        ctx.advance()

        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (isIdentifierPart(ch)) {
                ctx.advance()
            } else {
                break
            }
        }

        return ctx.substring(start, ctx.pos)
    }

    private fun isIdentifierStart(ch: Char): Boolean =
        ch.isLetter() || ch == '_' || ch.isSurrogate()

    private fun isIdentifierPart(ch: Char): Boolean =
        ch.isLetterOrDigit() || ch == '_' || ch == '$' || ch.isSurrogate()

    // ========================================================================
    // Attribute Parsing
    // ========================================================================

    private fun isAttributeStart(ctx: ParseContext): Boolean {
        val savedState = ctx.saveState()
        val result = tryParseAttributeKey(ctx) && skipAndCheckEquals(ctx)
        ctx.restoreState(savedState)
        return result
    }

    private fun tryParseAttributeKey(ctx: ParseContext): Boolean {
        val first = ctx.peek() ?: return false
        if (!isIdentifierStart(first)) return false

        ctx.advance()
        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (isIdentifierPart(ch) || ch == ':') {
                ctx.advance()
            } else {
                break
            }
        }
        return true
    }

    private fun skipAndCheckEquals(ctx: ParseContext): Boolean {
        while (ctx.peek() == ' ' || ctx.peek() == '\t') {
            ctx.advance()
        }
        return ctx.peek() == '='
    }

    private fun parseAttribute(ctx: ParseContext): Pair<NSID, Any?> {
        val nsid = tryParseNSID(ctx) ?: throw ctx.error("Expected attribute name")

        skipSpacesAndTabs(ctx)
        if (ctx.peek() != '=') {
            throw ctx.error("Expected '=' in attribute")
        }
        ctx.advance()
        skipSpacesAndTabs(ctx)

        val value = parseRequiredValue(ctx)
        return nsid to value
    }

    // ========================================================================
    // Value Parsing
    // ========================================================================

    private fun isValueStart(ch: Char, ctx: ParseContext): Boolean {
        return when {
            ch == '"' -> true
            ch == '\'' -> true
            ch == '@' && ctx.peek(1) == '"' -> true
            ch == '`' -> true
            ch == '[' -> true
            ch == '<' -> true
            ch == '-' || ch == '+' -> true
            ch.isDigit() -> true
            ch == '.' && ctx.peek(1)?.isLetter() == true -> true
            isIdentifierStart(ch) -> true
            Currency.isPrefixSymbol(ch) -> true  // Currency prefix symbols ($, €, ¥, £, ₿, Ξ)
            else -> false
        }
    }

    private fun parseRequiredValue(ctx: ParseContext): Any? {
        skipSpacesAndTabs(ctx)
        val start = ctx.pos
        val value = parseValue(ctx)
        if (ctx.pos == start) throw ctx.error("Expected value")
        return value
    }

    private fun parseValue(ctx: ParseContext): Any? {
        skipSpacesAndTabs(ctx)

        val ch = ctx.peek() ?: return null

        // A host's value (`$x`, `${expr}` in a `lang KD` block): put in as it is, never read as KD text.
        // It may start a range, as a literal can (`$lo..$hi`).
        interpolationAt(ctx)?.let { span ->
            val value = span.value
            if (value != null && isRangeableValue(value)) {
                skipSpacesAndTabs(ctx)
                tryParseRange(ctx, value)?.let { return it }
            } else if (value == null) {
                val after = ctx.saveState()
                skipSpacesAndTabs(ctx)
                if (isRangeOperatorStart(ctx)) throw ctx.error("A range bound cannot be nil (use _ for an open bound)")
                ctx.restoreState(after)
            }
            return value
        }

        // Check for open range marker '_'
        if (ch == '_') {
            return parseOpenRangeOrIdentifier(ctx)
        }

        // Check for negative/positive currency prefix: -$100, +€50, -$.50
        if ((ch == '-' || ch == '+') && ctx.peek(1)?.let { Currency.isPrefixSymbol(it) } == true) {
            val afterPrefix = ctx.peek(2)
            // Only treat as currency if digit or period (for decimals like $.50) follows the prefix symbol
            if (afterPrefix?.isDigit() == true || (afterPrefix == '.' && ctx.peek(3)?.isDigit() == true)) {
                val isNegative = ch == '-'
                ctx.advance() // consume the sign
                val quantity = parseCurrencyWithPrefix(ctx)
                @Suppress("UNCHECKED_CAST")
                return if (isNegative) -(quantity as Quantity<Currency>) else quantity
            }
            // Otherwise fall through to normal parsing
        }

        val baseValue = when {
            ch == '@' && ctx.peek(1) == '"' -> parseRawString(ctx)
            ch == '"' -> {
                if (ctx.peek(1) == '"' && ctx.peek(2) == '"') {
                    parseBlockString(ctx)
                } else {
                    parseSimpleString(ctx)
                }
            }
            ch == '\'' -> parseChar(ctx)
            ch == '`' -> parseBacktickString(ctx)
            ch == '[' -> parseListOrMap(ctx)
            ch == '<' -> parseURL(ctx)
            ch == '-' || ch == '+' || ch.isDigit() -> parseNumberOrMeasure(ctx)
            ch == '.' && ctx.peek(1)?.isLetter() == true -> parseDotLiteral(ctx)
            Currency.isPrefixSymbol(ch) -> {
                // Currency prefix must be followed by digit or period (e.g., $100, $.50)
                val nextChar = ctx.peek(1)
                when {
                    // Digit follows -> currency (e.g., $100)
                    nextChar?.isDigit() == true -> parseCurrencyWithPrefix(ctx)
                    // Period followed by digit -> currency (e.g., $.50)
                    nextChar == '.' && ctx.peek(2)?.isDigit() == true -> parseCurrencyWithPrefix(ctx)
                    // Sign followed by digit -> currency (e.g., unusual but $-100)
                    (nextChar == '-' || nextChar == '+') && ctx.peek(2)?.isDigit() == true ->
                        parseCurrencyWithPrefix(ctx)
                    // Invalid - currency prefix must be followed by number
                    else -> throw ctx.error("Currency prefix '$ch' must be followed by a number")
                }
            }
            isIdentifierStart(ch) -> parseKeywordOrNakedOrCall(ctx)
            else -> null
        }

        // Check for range operator after the base value
        if (baseValue != null && isRangeableValue(baseValue)) {
            skipSpacesAndTabs(ctx)
            val rangeResult = tryParseRange(ctx, baseValue)
            if (rangeResult != null) return rangeResult
        }

        return baseValue
    }

    /**
     * A number, date, duration, quantity or version; a number or quantity followed at
     * once by `±` (or its ASCII spelling `+-`) and a second number or quantity is a
     * [Measure] (`0.0042±0.0006µM`, `0.512+-0.008`, `5cm±1mm`). KD finds the literal's
     * extent; Ki.Core's [Measure.parse] reads it, so the unit written once after the
     * uncertainty applies to both numbers.
     */
    private fun parseNumberOrMeasure(ctx: ParseContext): Any {
        val start = ctx.saveState()
        // A measure is read whole by Ki.Core, which wrote it: its sides are numbers or quantities by Ki.Core's
        // rules, so 12±1min:i is a time quantity (not a Duration) and a whole number past Long's range reads.
        measureEnd(ctx)?.let { end ->
            val text = ctx.substring(start.pos, end)
            ctx.advance(end - ctx.pos)
            return try {
                Measure.parse(text)
            } catch (e: Exception) {
                throw ctx.errorAt(start, "Invalid measure $text: ${e.message}")
            }
        }
        val value = parseNumberOrDateOrDuration(ctx)
        if (value !is Number && value !is Quantity<*>) return value
        val width = when {
            ctx.peek() == Measure.PLUS_MINUS -> 1
            ctx.peek() == '+' && ctx.peek(1) == '-' -> 2
            else -> return value
        }
        ctx.advance(width)
        val next = ctx.peek()
        if (next == null || !(next.isDigit() || next == '.' || next == '+' || next == '-')) {
            throw ctx.error("Expected the uncertainty after ${if (width == 1) "±" else "+-"} (a measure is written 0.512±0.008 or 5±0.1cm)")
        }
        val uncertainty = parseNumberOrDateOrDuration(ctx)
        if (uncertainty !is Number && uncertainty !is Quantity<*>) {
            throw ctx.errorAt(start, "A measure's uncertainty is a number or a quantity, not ${Ki.format(uncertainty)}")
        }
        val text = ctx.substring(start.pos, ctx.pos)
        return try {
            Measure.parse(text)
        } catch (e: Exception) {
            throw ctx.errorAt(start, "Invalid measure $text: ${e.message}")
        }
    }

    /**
     * Where the measure literal starting here ends, or null when the value here is not
     * a measure: the run of characters up to whitespace, a delimiter, `..` or a comment
     * holds `±`, or `+-` after its first character.
     */
    private fun measureEnd(ctx: ParseContext): Int? {
        val text = ctx.content
        var k = ctx.pos
        var measure = false
        while (k < ctx.length) {
            val c = text[k]
            if (c.isWhitespace() || c in ",;)]}=\"'#@[({<>`") break
            if (c == '.' && k + 1 < ctx.length && text[k + 1] == '.') break
            if (c == '/' && k + 1 < ctx.length && (text[k + 1] == '/' || text[k + 1] == '*')) break
            if (c == Measure.PLUS_MINUS || (c == '+' && k > ctx.pos && k + 1 < ctx.length && text[k + 1] == '-')) measure = true
            k++
        }
        return if (measure) k else null
    }

    /**
     * A whole number too large for Long, written unsuffixed in a Dec, Double or Float
     * cell (`.vec<Dec> { 10000000000000000000 }`, as Ki.Core writes a large Dec), read
     * as a Dec; null when the text here is anything else, for the usual parse.
     */
    private fun bigWhole(ctx: ParseContext, declared: DeclaredType): Dec? {
        if (declared.type != Dec::class.java && declared.type != java.lang.Double::class.java && declared.type != java.lang.Float::class.java) return null
        val m = BIG_WHOLE.matchAt(ctx.content, ctx.pos) ?: return null
        val end = ctx.pos + m.value.length
        val next = ctx.content.getOrNull(end)
        if (next != null && !(next.isWhitespace() || next in ",;})#/")) return null
        val digits = m.value.replace("_", "")
        if (digits.toLongOrNull() != null) return null
        ctx.advance(m.value.length)
        return Dec(digits)
    }

    /**
     * Checks if a value can be the left side of a range.
     */
    private fun isRangeableValue(value: Any?): Boolean {
        return value is Number || value is Char || value is String ||
                value is Version || value is Duration || value is Quantity<*> || value is Measure ||
                value is LocalDate || value is LocalDateTime || value is ZonedDateTime || value is KiTZDateTime
    }

    /**
     * Parses `_` which could be an open range boundary or an identifier.
     */
    private fun parseOpenRangeOrIdentifier(ctx: ParseContext): Any? {
        val start = ctx.pos
        ctx.advance() // consume '_'

        // Check if this is just '_' followed by range operator
        if (ctx.peek() != null && !isIdentifierPart(ctx.peek()!!)) {
            // Check for range operator
            skipSpacesAndTabs(ctx)
            if (isRangeOperatorStart(ctx)) {
                // Parse as open-left range
                return parseRangeFromOpenLeft(ctx)
            }
            // Just a standalone '_' - return as identifier
            return "_"
        }

        // It's an identifier starting with '_'
        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break
            if (isIdentifierPart(c)) {
                ctx.advance()
            } else {
                break
            }
        }

        val token = ctx.substring(start, ctx.pos)

        // Check if followed by '(' for Call
        skipSpacesAndTabs(ctx)
        if (ctx.peek() == '(') {
            return parseCall(ctx, token)
        }

        return token
    }

    /**
     * Checks if current position is at the start of a range operator.
     */
    private fun isRangeOperatorStart(ctx: ParseContext): Boolean {
        val c1 = ctx.peek() ?: return false
        val c2 = ctx.peek(1)

        return (c1 == '.' && c2 == '.') ||  // .. or ..<
                (c1 == '<' && c2 == '.')      // <.. or <..<
    }

    /**
     * Tries to parse a range starting from the left value.
     * Returns null if not a range.
     */
    @Suppress("UNCHECKED_CAST")
    private fun tryParseRange(ctx: ParseContext, left: Any): Range<*>? {
        val c1 = ctx.peek() ?: return null
        val c2 = ctx.peek(1) ?: return null

        // Determine range type
        val rangeType: Range.Bound
        when {
            c1 == '<' && c2 == '.' && ctx.peek(2) == '.' && ctx.peek(3) == '<' -> {
                // <..<  exclusive both sides
                rangeType = Range.Bound.Exclusive
                ctx.advance(4)
            }
            c1 == '<' && c2 == '.' && ctx.peek(2) == '.' -> {
                // <..   exclusive left
                rangeType = Range.Bound.ExclusiveStart
                ctx.advance(3)
            }
            c1 == '.' && c2 == '.' && ctx.peek(2) == '<' -> {
                // ..<   exclusive right
                rangeType = Range.Bound.ExclusiveEnd
                ctx.advance(3)
            }
            c1 == '.' && c2 == '.' -> {
                // ..    inclusive
                rangeType = Range.Bound.Inclusive
                ctx.advance(2)
            }
            else -> return null
        }

        skipSpacesAndTabs(ctx)

        // Check for open right (_)
        if (ctx.peek() == '_') {
            ctx.advance()
            // Make sure _ is not part of an identifier
            if (ctx.peek()?.let { isIdentifierPart(it) } == true) {
                throw ctx.error("Invalid range: open marker '_' cannot be part of identifier")
            }

            // Validate range type for open right
            if (rangeType !in listOf(Range.Bound.Inclusive, Range.Bound.ExclusiveStart)) {
                throw ctx.error("Right open ranges can only use .. and <.. operators")
            }

            return createOpenRightRange(ctx, left, rangeType)
        }

        // Parse the right value
        nilBound(ctx)
        val rightValue = parseValue(ctx)
            ?: throw ctx.error("Expected value after range operator")

        // Create the range
        return createRange(ctx, left, rightValue, rangeType)
    }

    /**
     * Parses a range that starts with open left (_).
     */
    @Suppress("UNCHECKED_CAST")
    private fun parseRangeFromOpenLeft(ctx: ParseContext): Range<*> {
        val c1 = ctx.peek() ?: throw ctx.error("Expected range operator after '_'")
        val c2 = ctx.peek(1)

        // Determine range type - only .. and ..< are valid for open left
        val rangeType: Range.Bound
        when {
            c1 == '.' && c2 == '.' && ctx.peek(2) == '<' -> {
                rangeType = Range.Bound.ExclusiveEnd
                ctx.advance(3)
            }
            c1 == '.' && c2 == '.' -> {
                rangeType = Range.Bound.Inclusive
                ctx.advance(2)
            }
            else -> throw ctx.error("Left open ranges can only use .. and ..< operators")
        }

        skipSpacesAndTabs(ctx)

        // Parse the right value
        nilBound(ctx)
        val rightValue = parseValue(ctx)
            ?: throw ctx.error("Expected value after range operator in open-left range")

        return createOpenLeftRange(ctx, rightValue, rangeType)
    }

    /**
     * Creates an open-right range (end is null).
     */
    @Suppress("UNCHECKED_CAST")
    private fun createOpenRightRange(ctx: ParseContext, start: Any, type: Range.Bound): Range<*> {
        return createRangeInternal(ctx, start, null, type)
    }

    /**
     * Creates an open-left range (start is null).
     */
    @Suppress("UNCHECKED_CAST")
    private fun createOpenLeftRange(ctx: ParseContext, end: Any, type: Range.Bound): Range<*> {
        return createRangeInternal(ctx, null, end, type)
    }

    /**
     * Creates a range from two values.
     */
    @Suppress("UNCHECKED_CAST")
    private fun createRange(ctx: ParseContext, start: Any, end: Any, type: Range.Bound): Range<*> {
        // Validate that start and end are compatible types
        if (start::class != end::class) {
            // Allow numeric type mixing
            if (start is Number && end is Number) {
                // Convert to same type
                val (s, e) = normalizeNumbers(start, end)
                return createRangeInternal(ctx, s, e, type)
            }
            throw ctx.error("Range endpoints must be of compatible types: ${start::class.simpleName} vs ${end::class.simpleName}")
        }
        return createRangeInternal(ctx, start, end, type)
    }

    /**
     * Internal helper to create a Range with proper type handling.
     * Either start or end may be null for open ranges.
     */
    @Suppress("UNCHECKED_CAST")
    private fun createRangeInternal(ctx: ParseContext, start: Any?, end: Any?, type: Range.Bound): Range<*> {
        // Use whichever end is non-null to determine the type
        val sample = start ?: end!!
        return when (sample) {
            is Int -> Range(start as Int?, end as Int?, type)
            is Long -> Range(start as Long?, end as Long?, type)
            is Double -> Range(start as Double?, end as Double?, type)
            is Float -> Range(start as Float?, end as Float?, type)
            is Dec -> Range(start as Dec?, end as Dec?, type)
            is String -> Range(start as String?, end as String?, type)
            is Char -> Range(start as Char?, end as Char?, type)
            is Version -> Range(start as Version?, end as Version?, type)
            is Duration -> Range(start as Duration?, end as Duration?, type)
            is LocalDate -> Range(start as LocalDate?, end as LocalDate?, type)
            is LocalDateTime -> Range(start as LocalDateTime?, end as LocalDateTime?, type)
            is ZonedDateTime -> Range(start as ZonedDateTime?, end as ZonedDateTime?, type)
            is KiTZDateTime -> Range(start as KiTZDateTime?, end as KiTZDateTime?, type)
            is Measure -> Range(start as Measure?, end as Measure?, type)
            is Quantity<*> -> {
                val s = start as Quantity<Length>?
                val e = end as Quantity<Length>?
                Range<Quantity<Length>?>(s, e, type)
            }
            else -> throw ctx.error("Unsupported type for range: ${sample::class.simpleName}")
        }
    }

    /**
     * Normalizes two numbers to the same type for range creation.
     */
    private fun normalizeNumbers(left: Number, right: Number): Pair<Number, Number> {
        // Promote to the "larger" type
        return when {
            left is Dec || right is Dec -> {
                val l = if (left is Dec) left else Dec(left.toString())
                val r = if (right is Dec) right else Dec(right.toString())
                Pair(l, r)
            }
            left is Double || right is Double -> Pair(left.toDouble(), right.toDouble())
            left is Float || right is Float -> Pair(left.toFloat(), right.toFloat())
            left is Long || right is Long -> Pair(left.toLong(), right.toLong())
            else -> Pair(left.toInt(), right.toInt())
        }
    }

    // ========================================================================
    // String Parsing
    // ========================================================================

    /** A range's bound from a host's value (`1..$n`, `lang KD`) that is nil: KD has no nil bound (`_` opens a range). */
    private fun nilBound(ctx: ParseContext) {
        val span = interpolation?.spans?.get(ctx.pos) ?: return
        if (span.value == null) throw ctx.error("A range bound cannot be nil (use _ for an open bound)")
    }

    /**
     * If a host's value starts at the current position (`lang KD`), moves past it and
     * returns it; otherwise null.
     */
    private fun interpolationAt(ctx: ParseContext): Interpolated? {
        val span = interpolation?.spans?.get(ctx.pos) ?: return null
        ctx.advance(span.end - ctx.pos)
        return span
    }

    private fun parseSimpleString(ctx: ParseContext): String {
        val startLine = ctx.line
        val startState = ctx.saveState()
        ctx.advance()

        val sb = StringBuilder()
        // With a host's values (lang KD): the text read so far, escapes resolved, with the values' text between.
        val done = StringBuilder()

        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: throw unterminatedString(ctx, startState, startLine)

            if (interpolation != null) {
                if (ch == '\\' && ctx.peek(1) == '$') { sb.append('$'); ctx.advance(2); continue }
                val span = interpolationAt(ctx)
                if (span != null) {
                    done.append(unescape(ctx, startState, sb)).append(interpolation.stringify(span.value))
                    sb.setLength(0)
                    continue
                }
            }

            when (ch) {
                '"' -> {
                    ctx.advance()
                    return done.append(unescape(ctx, startState, sb)).toString()
                }
                '\\' -> {
                    sb.append(ch)
                    ctx.advance()
                    val escaped = ctx.peek()
                    if (escaped != null) {
                        sb.append(escaped)
                        ctx.advance()
                    }
                }
                else -> {
                    sb.append(ch)
                    ctx.advance()
                }
            }
        }

        throw unterminatedString(ctx, startState, startLine)
    }

    /**
     * An unterminated `"…"` string: in a `lang KD` block the error stands at the string's
     * start (the host reports its own line); in a KD file it names the line it started on.
     */
    private fun unterminatedString(ctx: ParseContext, start: ParseState, startLine: Int): KDParseException =
        if (ctx.positioned) ctx.errorAt(start, "Unterminated string")
        else ctx.error("Unterminated string starting at line $startLine")

    /** Resolves the escapes of a string's text, reporting a bad escape at the string ([start]). */
    private fun unescape(ctx: ParseContext, start: ParseState, text: CharSequence): String =
        try {
            text.toString().resolveEscapes('"')
        } catch (e: ParseException) {
            val at = Regex("""\\(u.{0,4}|.)""").findAll(text).map { it.groupValues[1] }
                .firstOrNull { it.length == 1 && it !in setOf("t", "r", "n", "\\", "\"") || it.startsWith("u") && !Regex("u[0-9a-fA-F]{4}").matches(it) }
            throw ctx.errorAt(start, if (at == null) "Invalid escape in string" else "Invalid escape '\\$at' in string")
        }

    private fun parseRawString(ctx: ParseContext): String {
        ctx.advance() // @
        ctx.advance() // "

        // Check for raw block string @"""..."""
        if (ctx.peek() == '"' && ctx.peek(1) == '"') {
            ctx.advance(2) // skip remaining ""
            return parseRawBlockString(ctx)
        }

        val sb = StringBuilder()

        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break

            if (ch == '"') {
                ctx.advance()
                return sb.toString()
            }

            sb.append(ch)
            ctx.advance()
        }

        throw ctx.error("Unterminated raw string")
    }

    /**
     * Parses a raw block string (the content after @""").
     * No escape processing - content is taken as-is until closing """.
     */
    private fun parseRawBlockString(ctx: ParseContext): String {
        val sb = StringBuilder()

        // Skip leading newline if present (like block strings)
        if (ctx.peek() == '\n') {
            ctx.advance()
        } else if (ctx.peek() == '\r' && ctx.peek(1) == '\n') {
            ctx.advance(2)
        }

        while (!ctx.isEOF()) {
            if (ctx.peek() == '"' && ctx.peek(1) == '"' && ctx.peek(2) == '"') {
                ctx.advance(3)
                return processBlockStringContent(sb.toString())
            }

            sb.append(ctx.peek())
            ctx.advance()
        }

        throw ctx.error("Unterminated raw block string")
    }

    private fun parseBlockString(ctx: ParseContext): String {
        ctx.advance(3)

        val sb = StringBuilder()

        if (ctx.peek() == '\n') {
            ctx.advance()
        } else if (ctx.peek() == '\r' && ctx.peek(1) == '\n') {
            ctx.advance(2)
        }

        while (!ctx.isEOF()) {
            if (ctx.peek() == '"' && ctx.peek(1) == '"' && ctx.peek(2) == '"') {
                ctx.advance(3)
                return processBlockStringContent(sb.toString())
            }

            if (interpolation != null) {
                // A host's value (lang KD) puts its text in; a backslash before `$` makes it a literal `$`.
                if (ctx.peek() == '\\' && ctx.peek(1) == '$') { sb.append('$'); ctx.advance(2); continue }
                val span = interpolationAt(ctx)
                if (span != null) { sb.append(interpolation.stringify(span.value)); continue }
            }

            sb.append(ctx.peek())
            ctx.advance()
        }

        throw ctx.error("Unterminated block string")
    }

    private fun processBlockStringContent(content: String): String {
        return when {
            content.endsWith("\r\n") -> content.dropLast(2)
            content.endsWith("\n") -> content.dropLast(1)
            content.endsWith("\r") -> content.dropLast(1)
            else -> content
        }
    }

    private fun parseBacktickString(ctx: ParseContext): String {
        if (ctx.peek() == '`' && ctx.peek(1) == '`' && ctx.peek(2) == '`') {
            ctx.advance(3)
            val sb = StringBuilder()

            while (!ctx.isEOF()) {
                if (ctx.peek() == '`' && ctx.peek(1) == '`' && ctx.peek(2) == '`') {
                    ctx.advance(3)
                    return sb.toString()
                }
                sb.append(ctx.peek())
                ctx.advance()
            }

            throw ctx.error("Unterminated triple-backtick string")
        }

        ctx.advance()
        val sb = StringBuilder()

        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (ch == '`') {
                ctx.advance()
                return sb.toString()
            }
            sb.append(ch)
            ctx.advance()
        }

        throw ctx.error("Unterminated backtick string")
    }

    // ========================================================================
    // Char Parsing
    // ========================================================================

    private fun parseChar(ctx: ParseContext): Char {
        ctx.advance()

        val ch = ctx.peek() ?: throw ctx.error("Empty character literal")

        if (ch == '\\') {
            ctx.advance()
            val escaped = ctx.peek() ?: throw ctx.error("Incomplete escape in character")

            val result = when (escaped) {
                'n' -> '\n'
                't' -> '\t'
                'r' -> '\r'
                '\\' -> '\\'
                '\'' -> '\''
                'u' -> {
                    ctx.advance()
                    val hex = StringBuilder()
                    repeat(4) {
                        val hexCh = ctx.peek() ?: throw ctx.error("Incomplete unicode escape")
                        hex.append(hexCh)
                        ctx.advance()
                    }
                    try {
                        hex.toString().toInt(16).toChar()
                    } catch (e: Exception) {
                        throw ctx.error("Invalid unicode escape: \\u$hex")
                    }
                }
                else -> throw ctx.error("Invalid escape character: \\$escaped")
            }

            if (escaped != 'u') ctx.advance()

            if (ctx.peek() != '\'') {
                throw ctx.error("Expected closing quote for character literal")
            }
            ctx.advance()
            return result
        }

        ctx.advance()

        if (ctx.peek() != '\'') {
            throw ctx.error("Expected closing quote for character literal")
        }
        ctx.advance()

        return ch
    }

    // ========================================================================
    // Number, Date, DateTime, Duration, Version Parsing
    // ========================================================================

    /**
     * Entry point for parsing numeric-like values.
     * Determines if this is a number, date, datetime, duration, or version.
     */
    private fun parseNumberOrDateOrDuration(ctx: ParseContext): Any {
        val start = ctx.pos

        // Peek ahead to determine the type
        val savedState = ctx.saveState()
        var hasSlash = false
        var hasAt = false
        var hasColon = false
        var hasDot = false
        var dotCount = 0

        // Handle sign
        if (ctx.peek() == '-' || ctx.peek() == '+') {
            ctx.advance()
        }

        // Scan ahead to identify the pattern
        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break

            when {
                c.isDigit() || c == '_' -> ctx.advance()
                c == '/' -> { hasSlash = true; ctx.advance() }
                c == '@' -> { hasAt = true; break } // Stop at @ to let date parsing handle the rest
                c == ':' -> { hasColon = true; ctx.advance() }
                c == '.' -> {
                    if (ctx.peek(1) == '.') break // Range operator
                    hasDot = true
                    dotCount++
                    ctx.advance()
                }
                // Check for day/days compound duration pattern (e.g., 3day:12:30:00)
                c == 'd' -> {
                    // Check if this is "day:" or "days:" pattern
                    val remaining = ctx.remaining()
                    if (remaining.startsWith("day:") || remaining.startsWith("days:")) {
                        // Skip past "day" or "days"
                        if (remaining.startsWith("days:")) {
                            ctx.advance(); ctx.advance(); ctx.advance(); ctx.advance() // "days"
                        } else {
                            ctx.advance(); ctx.advance(); ctx.advance() // "day"
                        }
                        // Now we should be at ':', continue scanning
                    } else {
                        break
                    }
                }
                else -> break
            }
        }

        // Reset position
        ctx.restoreState(savedState)

        return when {
            hasSlash -> parseDateOrDateTime(ctx)
            hasColon && !hasAt -> parseCompoundDuration(ctx)
            else -> parseNumberOrDurationOrVersion(ctx)
        }
    }

    /**
     * Parses a date (y/M/d) or datetime (y/M/d@H:mm:ss with optional zone).
     */
    private fun parseDateOrDateTime(ctx: ParseContext): Any {
        val start = ctx.pos
        val sb = StringBuilder()
        var hasAt = false
        var hasTzSuffix = false

        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break

            when {
                c.isDigit() || c == '/' || c == '_' -> {
                    sb.append(c)
                    ctx.advance()
                }
                c == '@' -> {
                    hasAt = true
                    sb.append(c)
                    ctx.advance()
                }
                c == ':' -> {
                    sb.append(c)
                    ctx.advance()
                }
                c == '.' -> {
                    if (ctx.peek(1) == '.') break // Range operator
                    sb.append(c)
                    ctx.advance()
                }
                c == '-' || c == '+' -> {
                    // Timezone offset or KiTZ suffix
                    if (hasAt) {
                        hasTzSuffix = true
                        sb.append(c)
                        ctx.advance()
                        // Read the rest of the timezone
                        while (!ctx.isEOF()) {
                            val next = ctx.peek() ?: break
                            if (next.isLetterOrDigit() || next == '/' || next == ':' || next == '_') {
                                sb.append(next)
                                ctx.advance()
                            } else {
                                break
                            }
                        }
                        break
                    } else {
                        break // Not part of date/time
                    }
                }
                else -> break
            }
        }

        val text = sb.toString()

        return when {
            hasAt && hasTzSuffix -> {
                // Determine if this is a KiTZ suffix (CC/TZ format) or a plain offset
                val tzPart = text.substringAfterLast('@').let { afterAt ->
                    when {
                        afterAt.contains("-") -> afterAt.substringAfter("-")
                        afterAt.contains("+") -> afterAt.substringAfter("+")
                        else -> ""
                    }
                }

                // KiTZ format has CC/TZ pattern (2 letter country code, slash, timezone abbrev)
                // Examples: JP/JST, US/PST, DE/CET, Z, UTC, GMT
                val isKiTZ = tzPart.matches(Regex("[A-Z]{2}/[A-Z]+")) ||
                        tzPart == "Z" || tzPart == "UTC" || tzPart == "GMT"

                try {
                    if (isKiTZ) {
                        KiTZDateTime.parse(text)
                    } else {
                        Ki.parseZonedDateTime(text)
                    }
                } catch (e: Exception) {
                    throw ctx.error("Invalid ZonedDateTime/KiTZDateTime: $text - ${e.message}")
                }
            }
            hasAt -> {
                try {
                    Ki.parseLocalDateTime(text)
                } catch (e: Exception) {
                    throw ctx.error("Invalid LocalDateTime: $text - ${e.message}")
                }
            }
            else -> {
                try {
                    Ki.parseLocalDate(text)
                } catch (e: Exception) {
                    throw ctx.error("Invalid Date: $text - ${e.message}")
                }
            }
        }
    }

    /**
     * Parses a compound duration (hh:mm:ss or Nday:hh:mm:ss).
     */
    private fun parseCompoundDuration(ctx: ParseContext): Duration {
        val start = ctx.pos
        val sb = StringBuilder()

        // Handle sign
        if (ctx.peek() == '-' || ctx.peek() == '+') {
            sb.append(ctx.peek())
            ctx.advance()
        }

        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break

            when {
                c.isDigit() || c == ':' || c == '.' || c == '_' -> {
                    sb.append(c)
                    ctx.advance()
                }
                c.isLetter() -> {
                    // Could be 'day' or 'days' in compound duration
                    sb.append(c)
                    ctx.advance()
                }
                else -> break
            }
        }

        val text = sb.toString()

        return try {
            Ki.parseDuration(text)
        } catch (e: Exception) {
            throw ctx.error("Invalid Duration: $text - ${e.message}")
        }
    }

    /**
     * Parses a regular number, single-unit duration, quantity, or version.
     */
    private fun parseNumberOrDurationOrVersion(ctx: ParseContext): Any {
        val start = ctx.pos

        // Handle sign
        val hasSign = ctx.peek() == '-' || ctx.peek() == '+'
        if (hasSign) {
            ctx.advance()
        }

        // Check for hex
        if (ctx.peek() == '0' && (ctx.peek(1) == 'x' || ctx.peek(1) == 'X')) {
            return parseHexNumber(ctx, start)
        }

        // Check for binary
        if (ctx.peek() == '0' && (ctx.peek(1) == 'b' || ctx.peek(1) == 'B')) {
            return parseBinaryNumber(ctx, start)
        }

        // Parse digits and dots
        var hasDecimal = false
        var hasExponent = false
        var dotCount = 0

        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break

            when {
                c.isDigit() -> ctx.advance()
                c == '_' -> ctx.advance()
                c == '.' && !hasExponent -> {
                    if (ctx.peek(1) == '.') break // Range operator
                    if (dotLiterAt(ctx)) break   // `1.L`: the liter, read below
                    hasDecimal = true
                    dotCount++
                    ctx.advance()
                }
                (c == 'e' || c == 'E') && !hasExponent -> {
                    // Check for Quantity scientific notation: e(8), e(-7), en7, ep8
                    // Or standard scientific notation: e-2, e+3, e5
                    val next = ctx.peek(1)
                    if (next == '(' || next == 'n' || next == 'p' || next?.isDigit() == true || next == '-' || next == '+') {
                        hasExponent = true
                        ctx.advance()
                        // Handle Quantity-style scientific notation
                        if (ctx.peek() == '(') {
                            ctx.advance() // skip (
                            if (ctx.peek() == '+' || ctx.peek() == '-') ctx.advance()
                            while (ctx.peek()?.isDigit() == true) ctx.advance()
                            if (ctx.peek() == ')') ctx.advance()
                        } else if (ctx.peek() == 'n' || ctx.peek() == 'p') {
                            ctx.advance() // skip n or p
                            while (ctx.peek()?.isDigit() == true) ctx.advance()
                        } else {
                            // Standard scientific notation: e-2, e+3, e5
                            if (ctx.peek() == '+' || ctx.peek() == '-') ctx.advance()
                            while (ctx.peek()?.isDigit() == true) ctx.advance()
                        }
                    } else {
                        break
                    }
                }
                else -> break
            }
        }

        val numEnd = ctx.pos
        val numStr = ctx.substring(start, numEnd).replace("_", "")

        // `1.L`, `2.5.L`: a liter. A bare `L` after a number is a Long (`1L`), so the
        // liter's ASCII form puts a dot before the L (Ki.Core reads it as `ℓ`).
        if (dotLiterAt(ctx)) {
            ctx.advance(2)
            var typeSpec = ""
            if (ctx.peek() == ':' && ctx.peek(1).let { it == 'i' || it == 'L' || it == 'd' || it == 'f' }) {
                typeSpec = ":" + ctx.peek(1)
                ctx.advance(2)
            }
            return try {
                Quantity.parse("$numStr.L$typeSpec")
            } catch (e: Exception) {
                throw ctx.error("Invalid liter quantity ${ctx.substring(start, ctx.pos)}: ${e.message}")
            }
        }

        // Check what follows - could be duration unit, quantity unit, version qualifier, or type suffix
        val suffix = ctx.peek()

        // Check for duration units first
        if (suffix != null) {
            val durationResult = tryParseDurationUnit(ctx, start)
            if (durationResult != null) return durationResult
        }

        // Helper to check if a character can start a unit symbol (including °C, ℓ)
        fun isUnitStart(c: Char): Boolean = c.isLetter() || c == '°' || c == 'ℓ'

        // Check for Quantity (number followed by unit symbol)
        // Try this BEFORE type suffix handling since LT, dC could look like type suffixes
        if (suffix != null && isUnitStart(suffix)) {
            val quantityResult = tryParseQuantity(ctx, start, numEnd)
            if (quantityResult != null) return quantityResult
        }

        // Check for version with qualifier (e.g., 5.2.7-beta)
        if (suffix == '-' && dotCount >= 1 && !hasExponent && !hasSign) {
            val versionResult = tryParseVersionWithQualifier(ctx, start)
            if (versionResult != null) return versionResult
        }

        // Check if this looks like a version (2+ dots required for plain versions, no exponent)
        // Note: 3.14 is Double, but 3.1.4 is Version
        if (dotCount >= 2 && !hasExponent && !hasSign) {
            val versionResult = tryParseVersionOnly(numStr)
            if (versionResult != null) return versionResult
        }

        // Regular number
        return parseNumberWithSuffix(ctx, start, numEnd, hasDecimal || hasExponent)
    }

    /** At [ctx]'s position: `.L` not followed by a letter, digit or `_`, the liter after a number (`1.L`). */
    private fun dotLiterAt(ctx: ParseContext): Boolean {
        if (ctx.peek() != '.' || ctx.peek(1) != 'L') return false
        val after = ctx.peek(2) ?: return true
        return !(after.isLetterOrDigit() || after == '_')
    }

    /**
     * Tries to parse a Quantity (number followed by unit symbol and optional type specifier).
     * Format: number + unitSymbol + optional(:i|:L|:d|:f)
     */
    private fun tryParseQuantity(ctx: ParseContext, start: Int, numEnd: Int): Quantity<*>? {
        val savedState = ctx.saveState()

        // Collect the unit symbol
        val symbolStart = ctx.pos
        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break
            // Unit symbols can contain letters, digits (for m², m³), and special chars (°, ℓ)
            if (c.isLetter() || c == '°' || c == 'ℓ' || c == '²' || c == '³' ||
                (c.isDigit() && ctx.pos > symbolStart)) {
                ctx.advance()
            } else {
                break
            }
        }

        val symbolEnd = ctx.pos
        var symbol = ctx.substring(symbolStart, symbolEnd)

        if (symbol.isEmpty()) {
            ctx.restoreState(savedState)
            return null
        }

        // Check for type specifier (:i, :L, :d, :f)
        var typeSpec = '\u0000'
        if (ctx.peek() == ':') {
            val typeChar = ctx.peek(1)
            if (typeChar == 'i' || typeChar == 'L' || typeChar == 'd' || typeChar == 'f') {
                typeSpec = typeChar
                ctx.advance(2) // skip : and type char
            }
        }

        // Check if this is a valid unit
        val unit = Unit.getUnit(symbol)
        if (unit == null) {
            ctx.restoreState(savedState)
            return null
        }

        // Build the quantity text and parse it
        val numStr = ctx.substring(start, numEnd).replace("_", "")
        val quantityText = if (typeSpec != '\u0000') {
            "$numStr$symbol:$typeSpec"
        } else {
            "$numStr$symbol"
        }

        return try {
            Quantity.parse(quantityText)
        } catch (e: Exception) {
            ctx.restoreState(savedState)
            null
        }
    }

    /**
     * Parses a currency quantity with prefix notation (e.g., $100, €50.25, ¥10000, £75, ₿0.5, Ξ2.5).
     *
     * The prefix symbol is consumed, then the number is parsed, and the result is a
     * Quantity<Currency> using the corresponding currency code.
     *
     * Supports:
     * - Sign after prefix: $-100 (negative $100)
     * - Decimals: €50.25
     * - Underscores: $1_000_000
     * - Type specifiers: $100:L (Long)
     * - Scientific notation: $1.5e6
     *
     * @return Quantity<Currency> for the parsed currency amount
     * @throws ParseException if the format is invalid
     */
    private fun parseCurrencyWithPrefix(ctx: ParseContext): Quantity<*> {
        val prefixSymbol = ctx.peek() ?: throw ctx.error("Expected currency prefix symbol")

        // Verify it's a valid currency prefix
        val currency = Currency.fromPrefix(prefixSymbol)
            ?: throw ctx.error("Unknown currency prefix symbol: $prefixSymbol")

        ctx.advance() // consume the prefix symbol

        val start = ctx.pos

        // Handle optional sign after prefix
        if (ctx.peek() == '-' || ctx.peek() == '+') {
            ctx.advance()
        }

        // Parse digits before decimal, or handle leading decimal (e.g., $.50)
        val startsWithDecimal = ctx.peek() == '.' && ctx.peek(1)?.isDigit() == true

        if (ctx.peek()?.isDigit() != true && !startsWithDecimal) {
            throw ctx.error("Expected digit after currency symbol '$prefixSymbol'")
        }

        // Parse integer part (may be empty for $.50 style)
        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break
            when {
                c.isDigit() -> ctx.advance()
                c == '_' -> ctx.advance()  // Allow underscores for readability
                else -> break
            }
        }

        // Parse optional decimal part
        if (ctx.peek() == '.' && ctx.peek(1)?.isDigit() == true) {
            ctx.advance() // consume '.'
            while (!ctx.isEOF() && (ctx.peek()?.isDigit() == true || ctx.peek() == '_')) {
                ctx.advance()
            }
        }

        // Parse optional exponent (scientific notation)
        if (ctx.peek() == 'e' || ctx.peek() == 'E') {
            val next = ctx.peek(1)
            if (next == '(' || next == 'n' || next == 'p' || next?.isDigit() == true || next == '-' || next == '+') {
                ctx.advance() // consume 'e' or 'E'

                when {
                    ctx.peek() == '(' -> {
                        ctx.advance() // skip (
                        if (ctx.peek() == '+' || ctx.peek() == '-') ctx.advance()
                        while (ctx.peek()?.isDigit() == true) ctx.advance()
                        if (ctx.peek() == ')') ctx.advance()
                    }
                    ctx.peek() == 'n' || ctx.peek() == 'p' -> {
                        ctx.advance() // skip n or p
                        while (ctx.peek()?.isDigit() == true) ctx.advance()
                    }
                    else -> {
                        if (ctx.peek() == '+' || ctx.peek() == '-') ctx.advance()
                        while (ctx.peek()?.isDigit() == true) ctx.advance()
                    }
                }
            }
        }

        val numEnd = ctx.pos
        var numStr = ctx.substring(start, numEnd).replace("_", "")

        // Check for type specifier (:i, :L, :d, :f)
        var typeSpec = '\u0000'
        if (ctx.peek() == ':') {
            val typeChar = ctx.peek(1)
            if (typeChar == 'i' || typeChar == 'L' || typeChar == 'd' || typeChar == 'f') {
                typeSpec = typeChar
                ctx.advance(2) // skip : and type char
            }
        }

        // Build the quantity text with suffix notation and parse it
        val quantityText = if (typeSpec != '\u0000') {
            "$numStr${currency.symbol}:$typeSpec"
        } else {
            "$numStr${currency.symbol}"
        }

        return try {
            Quantity.parse(quantityText)
        } catch (e: Exception) {
            throw ctx.error("Invalid currency amount: $prefixSymbol$numStr - ${e.message}")
        }
    }

    /**
     * Tries to parse a duration with a unit suffix.
     */
    private fun tryParseDurationUnit(ctx: ParseContext, start: Int): Duration? {
        val suffix = ctx.peek() ?: return null
        val savePos = ctx.saveState()

        when {
            // days or day
            suffix == 'd' && ctx.peek(1) == 'a' && ctx.peek(2) == 'y' -> {
                ctx.advance(3)
                if (ctx.peek() == 's') ctx.advance()
                // Make sure this isn't followed by more identifier chars
                if (ctx.peek()?.isLetterOrDigit() == true) {
                    ctx.restoreState(savePos)
                    return null
                }
                val text = ctx.substring(start, ctx.pos).replace("_", "")
                return try { Ki.parseDuration(text) } catch (e: Exception) { ctx.restoreState(savePos); null }
            }
            // hours
            suffix == 'h' && ctx.peek(1)?.isLetterOrDigit() != true -> {
                ctx.advance()
                val text = ctx.substring(start, ctx.pos).replace("_", "")
                return try { Ki.parseDuration(text) } catch (e: Exception) { ctx.restoreState(savePos); null }
            }
            // minutes
            suffix == 'm' && ctx.peek(1) == 'i' && ctx.peek(2) == 'n' -> {
                ctx.advance(3)
                if (ctx.peek()?.isLetterOrDigit() == true) {
                    ctx.restoreState(savePos)
                    return null
                }
                val text = ctx.substring(start, ctx.pos).replace("_", "")
                return try { Ki.parseDuration(text) } catch (e: Exception) { ctx.restoreState(savePos); null }
            }
            // milliseconds
            suffix == 'm' && ctx.peek(1) == 's' -> {
                ctx.advance(2)
                if (ctx.peek()?.isLetterOrDigit() == true) {
                    ctx.restoreState(savePos)
                    return null
                }
                val text = ctx.substring(start, ctx.pos).replace("_", "")
                return try { Ki.parseDuration(text) } catch (e: Exception) { ctx.restoreState(savePos); null }
            }
            // nanoseconds
            suffix == 'n' && ctx.peek(1) == 's' -> {
                ctx.advance(2)
                if (ctx.peek()?.isLetterOrDigit() == true) {
                    ctx.restoreState(savePos)
                    return null
                }
                val text = ctx.substring(start, ctx.pos).replace("_", "")
                return try { Ki.parseDuration(text) } catch (e: Exception) { ctx.restoreState(savePos); null }
            }
            // seconds (but not type suffix 's')
            suffix == 's' && ctx.peek(1)?.isLetter() != true -> {
                ctx.advance()
                val text = ctx.substring(start, ctx.pos).replace("_", "")
                return try { Ki.parseDuration(text) } catch (e: Exception) { ctx.restoreState(savePos); null }
            }
        }

        return null
    }

    /**
     * Tries to parse a version with qualifier.
     */
    private fun tryParseVersionWithQualifier(ctx: ParseContext, start: Int): Version? {
        val savedPos = ctx.saveState()

        ctx.advance() // skip -

        // Read qualifier and qualifier number
        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break
            if (c.isLetterOrDigit() || c == '-' || c == '_') {
                ctx.advance()
            } else {
                break
            }
        }

        val text = ctx.substring(start, ctx.pos).replace("_", "")

        return try {
            Version.parse(text)
        } catch (e: Exception) {
            ctx.restoreState(savedPos)
            null
        }
    }

    /**
     * Tries to parse a version without qualifier.
     */
    private fun tryParseVersionOnly(numStr: String): Version? {
        if (numStr.contains('e') || numStr.contains('E')) return null

        return try {
            val ver = Version.parse(numStr)
            // Only treat as version if it has 2+ dots (like 3.1.4), not just 1 dot (like 3.14)
            if (numStr.count { it == '.' } >= 2) ver else null
        } catch (e: Exception) {
            null
        }
    }

    private fun parseHexNumber(ctx: ParseContext, start: Int): Any {
        ctx.advance(2) // skip 0x

        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break
            if (c.isDigit() || c in 'a'..'f' || c in 'A'..'F' || c == '_') {
                ctx.advance()
            } else {
                break
            }
        }

        val numEnd = ctx.pos

        if (ctx.peek() == 'L') {
            ctx.advance()
            val numStr = ctx.substring(start, numEnd).replace("_", "").removePrefix("0x").removePrefix("0X")
            return numStr.toLong(16)
        }

        val numStr = ctx.substring(start, numEnd).replace("_", "").removePrefix("0x").removePrefix("0X")
        val value = numStr.toLong(16)

        return if (value in Int.MIN_VALUE..Int.MAX_VALUE) value.toInt() else value
    }

    private fun parseBinaryNumber(ctx: ParseContext, start: Int): Any {
        ctx.advance(2) // skip 0b

        while (!ctx.isEOF()) {
            val c = ctx.peek() ?: break
            if (c == '0' || c == '1' || c == '_') {
                ctx.advance()
            } else {
                break
            }
        }

        val numEnd = ctx.pos

        if (ctx.peek() == 'L') {
            ctx.advance()
            val numStr = ctx.substring(start, numEnd).replace("_", "").removePrefix("0b").removePrefix("0B")
            return numStr.toLong(2)
        }

        val numStr = ctx.substring(start, numEnd).replace("_", "").removePrefix("0b").removePrefix("0B")
        val value = numStr.toLong(2)

        return if (value in Int.MIN_VALUE..Int.MAX_VALUE) value.toInt() else value
    }

    private fun parseNumberWithSuffix(ctx: ParseContext, start: Int, numEnd: Int, isFloat: Boolean): Any {
        val suffix = ctx.peek()
        val numStr = ctx.substring(start, numEnd).replace("_", "")

        return when {
            (suffix == 'b' || suffix == 'B') && (ctx.peek(1) == 'd' || ctx.peek(1) == 'D') -> {
                ctx.advance(2)
                try { Dec(numStr) } catch (e: Exception) { throw ctx.error("Invalid BigDecimal: $numStr") }
            }
            suffix == 'L' -> {
                ctx.advance()
                numStr.toLongOrNull() ?: throw ctx.error("Invalid Long: $numStr")
            }
            suffix == 'f' || suffix == 'F' -> {
                ctx.advance()
                numStr.toFloatOrNull() ?: throw ctx.error("Invalid Float: $numStr")
            }
            (suffix == 'd' || suffix == 'D') && ctx.peek(1) != 'a' -> {
                ctx.advance()
                numStr.toDoubleOrNull() ?: throw ctx.error("Invalid Double: $numStr")
            }
            isFloat -> {
                numStr.toDoubleOrNull() ?: throw ctx.error("Invalid number: $numStr")
            }
            else -> {
                numStr.toIntOrNull() ?: numStr.toLongOrNull() ?: throw ctx.error("Invalid integer: $numStr")
            }
        }
    }

    // ========================================================================
    // URL Parsing
    // ========================================================================

    private fun parseURL(ctx: ParseContext): URL {
        ctx.advance() // skip <

        val start = ctx.pos

        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (ch == '>') {
                val urlStr = ctx.substring(start, ctx.pos)
                ctx.advance()
                return try { URL(urlStr) } catch (e: Exception) { throw ctx.error("Invalid URL: $urlStr") }
            }
            ctx.advance()
        }

        throw ctx.error("Unterminated URL literal")
    }

    // ========================================================================
    // List and Map Parsing
    // ========================================================================

    private fun parseListOrMap(ctx: ParseContext): Any {
        ctx.advance() // skip [
        skipWhitespaceAndComments(ctx)

        if (ctx.peek() == ']') {
            ctx.advance()
            return emptyList<Any?>()
        }

        val savedPos = ctx.saveState()
        val isMap = isMapStart(ctx)
        ctx.restoreState(savedPos)

        return if (isMap) parseMap(ctx) else parseList(ctx)
    }

    private fun isMapStart(ctx: ParseContext): Boolean {
        skipWhitespaceAndComments(ctx)

        val ch = ctx.peek() ?: return false

        // Parse a potential key value
        if (interpolationAt(ctx) != null) {
            skipSpacesAndTabs(ctx)
            return ctx.peek() == '='
        }
        when {
            ch == '"' || ch == '\'' || ch == '`' -> parseValue(ctx)
            ch == '.' && ctx.peek(1)?.isLetter() == true -> parseValue(ctx)   // a dot literal key: [.id(…)=1], [.seq("A")=2]
            ch == '-' || ch == '+' || ch.isDigit() -> parseValue(ctx)  // Handle numeric keys
            isIdentifierStart(ch) -> parseIdentifier(ctx)
            else -> return false
        }

        skipSpacesAndTabs(ctx)
        return ctx.peek() == '='
    }

    private fun parseList(ctx: ParseContext): List<Any?> {
        val items = mutableListOf<Any?>()

        while (!ctx.isEOF()) {
            skipWhitespaceAndComments(ctx)

            if (ctx.peek() == ']') {
                ctx.advance()
                return items
            }

            val posBefore = ctx.pos
            val value = parseValue(ctx)

            // Prevent infinite loop: if parseValue returned null and didn't advance, error out
            if (value == null && ctx.pos == posBefore) {
                throw ctx.error("Unexpected character '${ctx.peek()}' in list")
            }

            items.add(value)

            skipWhitespaceAndComments(ctx)
            if (ctx.peek() == ',') ctx.advance()
        }

        throw ctx.error("Unterminated list")
    }

    private fun parseMap(ctx: ParseContext): Map<Any?, Any?> {
        val items = mutableMapOf<Any?, Any?>()

        while (!ctx.isEOF()) {
            skipWhitespaceAndComments(ctx)

            if (ctx.peek() == ']') {
                ctx.advance()
                return items
            }

            val keyPosBefore = ctx.pos
            val key = parseValue(ctx)

            // Prevent infinite loop on unparseable key
            if (key == null && ctx.pos == keyPosBefore) {
                throw ctx.error("Unexpected character '${ctx.peek()}' in map key")
            }

            skipSpacesAndTabs(ctx)
            if (ctx.peek() != '=') {
                throw ctx.error("Expected '=' in map entry")
            }
            ctx.advance()
            skipSpacesAndTabs(ctx)

            val valuePosBefore = ctx.pos
            val value = parseValue(ctx)

            // Prevent infinite loop on unparseable value
            if (value == null && ctx.pos == valuePosBefore) {
                throw ctx.error("Unexpected character '${ctx.peek()}' in map value")
            }

            items[key] = value

            skipWhitespaceAndComments(ctx)
            if (ctx.peek() == ',') ctx.advance()
        }

        throw ctx.error("Unterminated map")
    }

    // ========================================================================
    // Keywords and Naked Strings
    // ========================================================================

    private fun parseKeywordOrNakedOrCall(ctx: ParseContext): Any? {
        val start = ctx.pos

        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (isIdentifierPart(ch)) {
                ctx.advance()
            } else {
                break
            }
        }

        var token = ctx.substring(start, ctx.pos)

        // Check if this is a naked URL
        if (ctx.peek() == ':' && ctx.peek(1) == '/' && ctx.peek(2) == '/') {
            return parseNakedURL(ctx, token)
        }

        // Check if this could be an Email (local part may contain . + - % before @)
        val nextCh = ctx.peek()
        if (nextCh == '@' || nextCh == '+' || nextCh == '.' || nextCh == '-' || nextCh == '%') {
            val emailResult = tryParseEmail(ctx, token, start)
            if (emailResult != null) {
                return emailResult
            }
        }

        // Check for namespace:name pattern (for namespaced calls)
        if (ctx.peek() == ':' && ctx.peek(1) != '=') {
            ctx.advance() // skip ':'
            val nameStart = ctx.pos
            while (!ctx.isEOF()) {
                val ch = ctx.peek() ?: break
                if (isIdentifierPart(ch)) {
                    ctx.advance()
                } else {
                    break
                }
            }
            val name = ctx.substring(nameStart, ctx.pos)
            if (name.isNotEmpty()) {
                token = "$token:$name"  // Full namespaced identifier
            }
        }

        // Check if this is a Call (identifier followed by '(')
        skipSpacesAndTabs(ctx)
        if (ctx.peek() == '(') {
            return parseCall(ctx, token)
        }

        return when (token) {
            "true", "on" -> true
            "false", "off" -> false
            "nil", "null" -> null
            "Infinity" -> Double.POSITIVE_INFINITY
            "NaN" -> Double.NaN
            else -> token
        }
    }

    /**
     * Tries to parse an Email literal.
     * Email local parts can contain: letters, digits, and . + - % _
     * The '@' symbol separates local part from domain.
     *
     * @param ctx The parse context
     * @param initialLocalPart The already-parsed initial part of local (before special chars)
     * @param originalStart The original start position for backtracking
     * @return The parsed Email or null if not a valid email
     */
    private fun tryParseEmail(ctx: ParseContext, initialLocalPart: String, originalStart: Int): Email? {
        val savedState = ctx.saveState()

        // Build the full local part by continuing to consume email-valid characters
        val localPartBuilder = StringBuilder(initialLocalPart)

        // Continue consuming local part characters: letters, digits, . + - % _
        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (ch.isLetterOrDigit() || ch == '.' || ch == '+' || ch == '-' || ch == '%' || ch == '_') {
                localPartBuilder.append(ch)
                ctx.advance()
            } else {
                break
            }
        }

        // Must have '@' next
        if (ctx.peek() != '@') {
            ctx.restoreState(savedState)
            return null
        }

        // Consume the '@'
        ctx.advance()

        // Parse the domain part
        val domainStart = ctx.pos
        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            // Domain chars: letters, digits, hyphen, dot
            if (ch.isLetterOrDigit() || ch == '-' || ch == '.') {
                ctx.advance()
            } else {
                break
            }
        }

        val domain = ctx.substring(domainStart, ctx.pos)

        // Validate: domain must have content and contain a dot (for TLD)
        if (domain.isEmpty() || !domain.contains('.') || domain.endsWith('.')) {
            ctx.restoreState(savedState)
            return null
        }

        val localPart = localPartBuilder.toString()
        val emailAddress = "$localPart@$domain"

        return try {
            Email.of(emailAddress)
        } catch (e: Exception) {
            // Not a valid email, backtrack
            ctx.restoreState(savedState)
            null
        }
    }

    /**
     * Parses a Call literal: name(values, attributes)
     */
    private fun parseCall(ctx: ParseContext, name: String): Call {
        ctx.advance() // skip '('

        val nsid = NSID.parse(name)
        val call = Call(nsid)

        while (!ctx.isEOF()) {
            skipWhitespaceAndComments(ctx)

            if (ctx.peek() == ')') {
                ctx.advance()
                return call
            }

            // Check if this is an attribute (key=value)
            if (isCallAttributeStart(ctx)) {
                val (attrNsid, value) = parseCallAttribute(ctx)
                call.setAttribute(attrNsid, value)
            } else {
                val value = parseRequiredValue(ctx)
                call.values.add(value)
            }

            skipSpacesAndTabs(ctx)
            if (ctx.peek() == ',') ctx.advance()
        }

        throw ctx.error("Unterminated Call: expected ')'")
    }

    /**
     * Checks if we're at the start of a Call attribute (identifier followed by =).
     */
    private fun isCallAttributeStart(ctx: ParseContext): Boolean {
        val savedPos = ctx.saveState()
        val first = ctx.peek() ?: return false

        if (!isIdentifierStart(first)) {
            return false
        }

        ctx.advance()
        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (isIdentifierPart(ch) || ch == ':') {
                ctx.advance()
            } else {
                break
            }
        }

        skipSpacesAndTabs(ctx)
        val result = ctx.peek() == '='
        ctx.restoreState(savedPos)
        return result
    }

    /**
     * Parses a Call attribute (key=value pair).
     */
    private fun parseCallAttribute(ctx: ParseContext): Pair<NSID, Any?> {
        val nsid = tryParseNSID(ctx) ?: throw ctx.error("Expected attribute name in Call")

        skipSpacesAndTabs(ctx)
        if (ctx.peek() != '=') {
            throw ctx.error("Expected '=' in Call attribute")
        }
        ctx.advance()
        skipSpacesAndTabs(ctx)

        val value = parseRequiredValue(ctx)
        return nsid to value
    }

    private fun parseNakedURL(ctx: ParseContext, protocol: String): URL {
        val start = ctx.pos

        while (!ctx.isEOF()) {
            val ch = ctx.peek() ?: break
            if (ch.isWhitespace() || ch == '{' || ch == '}' || ch == '[' || ch == ']' ||
                ch == '(' || ch == ')' || ch == ',' || ch == ';') {
                break
            }
            ctx.advance()
        }

        val rest = ctx.substring(start, ctx.pos)
        val urlStr = protocol + rest

        return try { URL(urlStr) } catch (e: Exception) { throw ctx.error("Invalid URL: $urlStr") }
    }

    // ========================================================================
    // Dot-prefixed Literals (.blob, .geo, .coordinate, .grid, .snip)
    // ========================================================================

    /**
     * The text of a dot literal's body, from [start] to [end]. A literal is read from its
     * text (by its bag, or by KD for `.geo`, `.snip` and the rest), so in a `lang KD`
     * block a host's value inside it (`.ref($id)`, `.id("$x")`) puts its text in, as in
     * a string.
     */
    private fun literalText(start: Int, end: Int, ctx: ParseContext): String {
        val interpolation = interpolation ?: return ctx.substring(start, end)
        val sb = StringBuilder()
        var i = start
        while (i < end) {
            val span = interpolation.spans[i]
            if (span != null) { sb.append(interpolation.stringify(span.value)); i = span.end } else sb.append(ctx.content[i++])
        }
        return sb.toString()
    }

    private fun parseDotLiteral(ctx: ParseContext): Any? {
        val start = ctx.saveState()
        ctx.advance() // skip .

        val nameStart = ctx.pos
        while (ctx.peek()?.isLetter() == true) {
            ctx.advance()
        }
        val name = ctx.substring(nameStart, ctx.pos)

        skipSpacesAndTabs(ctx)

        // Handle optional type parameter for .grid<Type>
        var typeParam: String? = null
        if (name.lowercase() == "grid" && ctx.peek() == '<') {
            ctx.advance() // skip <
            val typeStart = ctx.pos
            while (!ctx.isEOF() && ctx.peek() != '>') {
                ctx.advance()
            }
            if (ctx.peek() != '>') {
                throw ctx.error("Unterminated type parameter in .grid<...>")
            }
            typeParam = ctx.substring(typeStart, ctx.pos)
            ctx.advance() // skip >
            skipSpacesAndTabs(ctx)
        }

        if (name.equals("grid", ignoreCase = true) && (ctx.peek() == '(' || ctx.peek() == '{')) {
            val closing = if (ctx.advance() == '(') ')' else '}'
            return parseGrid(ctx, closing, typeParam)
        }

        if (name == "vec" && ctx.peek() == '<') return parseVector(ctx, start)

        if (ctx.peek() != '(') {
            ctx.restoreState(start)
            return null
        }

        ctx.advance() // skip (
        var depth = 1
        val contentStart = ctx.pos

        // For grid and coordinate, we need smarter parenthesis tracking
        // that accounts for strings
        while (!ctx.isEOF() && depth > 0) {
            val ch = ctx.peek()
            when {
                interpolationAt(ctx) != null -> {}   // a host's value (lang KD), whatever its text holds
                ch == '"' -> skipStringContent(ctx)
                ch == '\'' -> skipCharContent(ctx)
                ch == '(' -> { depth++; ctx.advance() }
                ch == ')' -> { depth--; if (depth > 0) ctx.advance() }
                else -> ctx.advance()
            }
        }

        if (depth > 0) {
            throw ctx.error("Unterminated .$name literal")
        }

        val content = literalText(contentStart, ctx.pos, ctx)
        ctx.advance() // skip )

        val literal = if (typeParam != null) ".$name<$typeParam>($content)" else ".$name($content)"

        return when (name.lowercase()) {
            "blob" -> {
                try {
                    Blob.parseLiteral(literal)
                } catch (e: Exception) {
                    throw ctx.error("Invalid blob literal: ${e.message}")
                }
            }
            "geo" -> {
                try {
                    GeoPoint.parse(literal)
                } catch (e: Exception) {
                    throw ctx.error("Invalid geo literal: ${e.message}")
                }
            }
            "coordinate" -> {
                try {
                    Coordinate.parseLiteral(literal)
                } catch (e: Exception) {
                    throw ctx.error("Invalid coordinate literal: ${e.message}")
                }
            }
            "id" -> {
                try {
                    Id.parseLiteral(literal)
                } catch (e: Exception) {
                    throw ctx.error("Invalid id literal: ${e.message}")
                }
            }
            "ref" -> {
                try {
                    Ref.parseLiteral(literal)
                } catch (e: Exception) {
                    throw ctx.error("Invalid ref literal: ${e.message}")
                }
            }
            "snip" -> {
                // Snip literals are returned as strings for later resolution
                // by KD.readWithSnips(). This validates the literal syntax.
                try {
                    Snip.parse(literal)  // Validate syntax
                    literal  // Return as string for KD.readWithSnips to process
                } catch (e: Exception) {
                    throw ctx.error("Invalid snip literal: ${e.message}")
                }
            }
            "schema" -> {
                // Schema directives are returned as strings; KDS.read() applies
                // them. Plain parsing only validates the literal syntax.
                try {
                    SchemaRef.parse(literal)
                    literal
                } catch (e: Exception) {
                    throw ctx.error("Invalid schema directive: ${e.message}")
                }
            }
            else -> {
                // A type bag's literal (io.kixi.types). One no loaded bag claims is kept as written, so a
                // document written with a bag reads and writes back without it (Ki type bags design, 3.1).
                // (Only .grid takes a type parameter, so there is none here.)
                val type = io.kixi.types.TypeBags.literal(name) ?: return io.kixi.types.ExtLiteral(literal)
                try {
                    type.parse(literal)
                } catch (e: Exception) {
                    throw ctx.errorAt(start, "Invalid .$name literal: ${e.message}")      // at the literal's start
                }
            }
        }
    }

    /**
     * Skips over string content (for correct parenthesis tracking).
     */
    private fun skipStringContent(ctx: ParseContext) {
        val quote = ctx.peek()
        if (quote != '"') return
        ctx.advance()

        // Check for triple quote
        if (ctx.peek() == '"' && ctx.peek(1) == '"') {
            ctx.advance(2) // now inside """
            while (!ctx.isEOF()) {
                if (ctx.peek() == '"' && ctx.peek(1) == '"' && ctx.peek(2) == '"') {
                    ctx.advance(3)
                    return
                }
                ctx.advance()
            }
        } else {
            // Single quote string
            while (!ctx.isEOF()) {
                val ch = ctx.peek()
                if (ch == '\\') {
                    ctx.advance(2)
                } else if (ch == '"') {
                    ctx.advance()
                    return
                } else {
                    ctx.advance()
                }
            }
        }
    }

    /**
     * Skips over char content (for correct parenthesis tracking).
     */
    private fun skipCharContent(ctx: ParseContext) {
        if (ctx.peek() != '\'') return
        ctx.advance()
        if (ctx.peek() == '\\') ctx.advance()
        if (!ctx.isEOF()) ctx.advance()
        if (ctx.peek() == '\'') ctx.advance()
    }

    /** Parse rows directly, letting the value parser consume nested literals intact. */
    private fun parseGrid(ctx: ParseContext, closing: Char, typeParam: String?): Grid<*> {
        val declared = typeParam?.let { parseTypeParam(ctx, it.trim()) }
        val rows = mutableListOf<List<Any?>>()
        var row = mutableListOf<Any?>()
        fun endRow() {
            if (row.isEmpty()) return
            if (rows.isNotEmpty() && row.size != rows[0].size) {
                throw ctx.error("Grid row ${rows.size} has ${row.size} values, expected ${rows[0].size}")
            }
            rows.add(row)
            row = mutableListOf()
        }
        while (!ctx.isEOF()) {
            val ch = ctx.peek()
            when {
                ch == closing -> {
                    ctx.advance()
                    endRow()
                    if (rows.isEmpty()) throw ctx.error("Empty grid is not allowed. Grid requires at least one cell.")
                    val values = rows.flatten()
                    val (elementType, nullable) = declared?.let { it.type to it.nullable }
                        ?: (inferElementType(values) to values.any { it == null })
                    return Grid<Any?>(rows.firstOrNull()?.size ?: 0, rows.size,
                        values.toTypedArray(), elementType, nullable)
                }
                ch == '\n' || ch == '\r' || ch == ';' -> { ctx.advance(); endRow() }
                ch == ' ' || ch == '\t' || ch == ',' -> ctx.advance()
                ch == '#' || (ch == '/' && ctx.peek(1) == '/') -> {
                    while (!ctx.isEOF() && ctx.peek() != '\n' && ctx.peek() != '\r') ctx.advance()
                }
                ch == '/' && ctx.peek(1) == '*' -> skipBlockComment(ctx)
                ch == '-' && (ctx.peek(1) == closing || ctx.peek(1) == ',' ||
                        ctx.peek(1) == ';' || ctx.peek(1)?.isWhitespace() == true) -> {
                    ctx.advance(); row.add(declared?.let { typedCell(ctx, it, null, "-") })
                }
                else -> {
                    val cellStart = ctx.pos
                    val big = declared?.let { bigWhole(ctx, it) }
                    if (big != null) { row.add(typedCell(ctx, declared!!, convertBig(declared, big), ctx.substring(cellStart, ctx.pos).trim())); continue }
                    val value = parseRequiredValue(ctx)
                    row.add(declared?.let { typedCell(ctx, it, value, ctx.substring(cellStart, ctx.pos).trim()) } ?: value)
                }
            }
        }
        throw ctx.error("Unterminated grid: expected '$closing'")
    }

    /**
     * `.vec<T> { n n n }` (Ki.Core's [Vector]): T is Int, Long, Float, Double or Dec,
     * and each number is read as a typed grid's cell is (unsuffixed numbers take T
     * exactly from their text; a suffixed one must be a T). Spaces, tabs, commas,
     * newlines and comments separate the numbers.
     */
    private fun parseVector(ctx: ParseContext, start: ParseState): Vector<*> {
        ctx.advance() // skip <
        val typeStart = ctx.pos
        while (!ctx.isEOF() && ctx.peek() != '>' && ctx.peek() != '\n') ctx.advance()
        if (ctx.peek() != '>') throw ctx.errorAt(start, "Unterminated type parameter in .vec<...>")
        val typeName = ctx.substring(typeStart, ctx.pos).trim()
        ctx.advance() // skip >
        val type = Type.forName(typeName)?.takeIf { it in Vector.ELEMENT_TYPES }
            ?: throw ctx.errorAt(start, "A vector holds Int, Long, Float, Double or Dec numbers, not '$typeName' (.vec<Float> { 1.0 2.5 })")
        val declared = DeclaredType(type.kclass.javaObjectType, type.name, false, "vec")
        skipSpacesAndTabs(ctx)
        if (ctx.peek() != '{') throw ctx.error("Expected '{' after .vec<${type.name}>")
        ctx.advance()
        val numbers = ArrayList<Number>()
        while (!ctx.isEOF()) {
            val ch = ctx.peek()
            when {
                ch == '}' -> {
                    ctx.advance()
                    return try {
                        Vector.of(type, numbers)
                    } catch (e: IllegalArgumentException) {
                        throw ctx.errorAt(start, e.message ?: "Invalid vector")
                    }
                }
                ch == '\n' || ch == '\r' || ch == ' ' || ch == '\t' || ch == ',' -> ctx.advance()
                ch == '#' || (ch == '/' && ctx.peek(1) == '/') -> {
                    while (!ctx.isEOF() && ctx.peek() != '\n' && ctx.peek() != '\r') ctx.advance()
                }
                ch == '/' && ctx.peek(1) == '*' -> skipBlockComment(ctx)
                else -> {
                    val cellStart = ctx.pos
                    val big = bigWhole(ctx, declared)
                    if (big != null) { numbers.add(convertBig(declared, big)); continue }
                    val value = parseRequiredValue(ctx)
                    val text = ctx.substring(cellStart, ctx.pos).trim()
                    if (value !is Number) throw ctx.error("'$text' is not a number, in .vec<${type.name}>")
                    numbers.add(typedCell(ctx, declared, value, text) as Number)
                }
            }
        }
        throw ctx.errorAt(start, "Unterminated vector: expected '}'")
    }

    /**
     * A grid's (or a vector's) declared element type: its class, Ki name, whether it was
     * declared nullable (`Int?`), and the literal it belongs to, for messages.
     */
    private class DeclaredType(val type: Class<*>, val name: String, val nullable: Boolean, val literal: String = "grid")

    /**
     * Parses a type parameter such as `Int`, `Dec?` or `Quantity`. Every Ki type name
     * is accepted, plus `BigDecimal` and `Boolean` and the Java class names that older
     * versions wrote (`Character`, `LocalDate`, ...). An unknown name is an error.
     */
    private fun parseTypeParam(ctx: ParseContext, typeParam: String): DeclaredType {
        val isNullable = typeParam.endsWith("?")
        val baseType = if (isNullable) typeParam.dropLast(1) else typeParam
        val type = typeParamToType(baseType)
            ?: throw ctx.error("Unknown grid element type '$baseType'. Use a Ki type name such as Int, Dec, String, Date or Quantity.")
        return DeclaredType(type.kclass.javaObjectType, type.name, isNullable)
    }

    /**
     * A typed grid's cell: a number without a suffix takes the declared numeric type,
     * exactly from its text (`.grid<Dec> { 68.2 }` holds Dec 68.2; Int and Long need a
     * whole number in range); any other cell must already be of the declared type.
     * `nil` needs a nullable type: `.grid<Int>` has a value in every cell, `.grid<Int?>`
     * may have `nil` (or `-`).
     */
    private fun typedCell(ctx: ParseContext, declared: DeclaredType, value: Any?, text: String): Any? {
        if (value == null) {
            if (declared.nullable) return null
            // Same wording as Ki.KS's grid literal check
            throw ctx.error("'$text' in .${declared.literal}<${declared.name}>: Grid<${declared.name}> cannot contain nil values (use .grid<${declared.name}?> for nullable)")
        }
        val cell = if (value is Number && isUnsuffixed(value, text)) convertNumber(ctx, declared, value, text) else value
        if (!declared.type.isInstance(cell)) {
            val actual = io.kixi.types.TypeBags.typeFor(value)?.name ?: Type.typeOf(value)?.name ?: value::class.java.simpleName
            throw ctx.error("'$text' is $actual, not ${declared.name}, in .${declared.literal}<${declared.name}>")
        }
        return cell
    }

    /** [big] (a whole number past Long's range) as the cell's type: Dec as it is, Double or Float as the nearest. */
    private fun convertBig(declared: DeclaredType, big: Dec): Number = when (declared.type) {
        java.lang.Double::class.java -> big.toDouble()
        java.lang.Float::class.java -> big.toFloat()
        else -> big
    }

    /** True for a number literal written without a type suffix (`5`, `68.2`, `0xFF`; not `5L`, `68.2bd`, `1.5f`, `2d`). */
    private fun isUnsuffixed(value: Number, text: String): Boolean = when (value) {
        is Int -> true
        is Long -> !text.endsWith("L")
        is Double -> text.lastOrNull()?.let { it != 'd' && it != 'D' } ?: false
        else -> false
    }

    private fun convertNumber(ctx: ParseContext, declared: DeclaredType, value: Number, text: String): Number {
        val clean = text.replace("_", "")
        val radix = clean.removePrefix("-").let { it.startsWith("0x") || it.startsWith("0X") || it.startsWith("0b") || it.startsWith("0B") }
        fun exact(): Dec = if (value is Double && !radix) {
            try { Dec(clean) } catch (e: NumberFormatException) { Dec(value.toString()) }
        } else Dec.valueOf(value.toLong())
        fun whole(type: String, convert: (Dec) -> Number): Number = try {
            convert(exact())
        } catch (e: ArithmeticException) {
            val d = exact()
            throw ctx.error(
                if (d.signum() != 0 && d.stripTrailingZeros().scale() > 0) "'$text' is not a whole number, so it cannot be $type in .${declared.literal}<${declared.name}>"
                else "'$text' does not fit in $type in .${declared.literal}<${declared.name}>"
            )
        }
        return when (declared.type) {
            java.lang.Integer::class.java -> whole("Int") { it.intValueExact() }
            java.lang.Long::class.java -> whole("Long") { it.longValueExact() }
            Dec::class.java -> exact()
            java.lang.Double::class.java -> if (value is Double) value else value.toDouble()
            java.lang.Float::class.java -> if (value is Double && !radix) (clean.toFloatOrNull() ?: value.toFloat()) else value.toFloat()
            else -> value   // Number, Any: numbers keep their own type
        }
    }

    /**
     * Infers the element type from a list of values.
     *
     * Rules:
     * - If all non-null values have the same type → use that type
     * - If all non-null values are Numbers (Int, Long, Float, Double, BigDecimal) → use Number
     * - Otherwise → use Any
     *
     * @param values The list of values to infer type from
     * @return The inferred Class, or null if all values are null
     */
    private fun inferElementType(values: List<Any?>): Class<*>? {
        val nonNullValues = values.filterNotNull()
        if (nonNullValues.isEmpty()) return null

        val types = nonNullValues.map { it::class.java }.toSet()

        // A type bag's values (or literals kept without their bag) have no Ki type name, so the grid is
        // untyped: it writes back as `.grid {…}` and stores its cells as values.
        if (types.any { io.kixi.types.TypeBags.typeForClass(it) != null || io.kixi.types.ExtLiteral::class.java.isAssignableFrom(it) }) {
            return Any::class.java
        }

        // All same type
        if (types.size == 1) {
            return types.first()
        }

        // Check if all types are numeric
        val numericTypes = setOf(
            Int::class.java, Int::class.javaObjectType,
            Long::class.java, Long::class.javaObjectType,
            Float::class.java, Float::class.javaObjectType,
            Double::class.java, Double::class.javaObjectType,
            java.math.BigDecimal::class.java
        )

        if (types.all { it in numericTypes }) {
            return Number::class.java
        }

        // Mixed unrelated types
        return Any::class.java
    }

    /** Maps a type parameter to its Ki type, or null if not recognized. */
    private fun typeParamToType(typeParam: String): Type? = Type.forName(typeParam) ?: when (typeParam) {
        "BigDecimal" -> Type.Dec
        "Boolean" -> Type.Bool
        // Java class names written by Grid.toString before it used Ki type names
        "Integer" -> Type.Int
        "Character" -> Type.Char
        "LocalDate" -> Type.Date
        "ArrayList" -> Type.List
        "LinkedHashMap" -> Type.Map
        else -> null
    }

}
