package io.kixi.kd

import io.kixi.lang.EmbeddedBlock
import io.kixi.lang.EmbeddedInterpolation
import io.kixi.lang.EmbeddedLanguage
import io.kixi.lang.EmbeddedLanguageException
import io.kixi.lang.EmbeddedSource
import io.kixi.lang.EmbeddedText

/**
 * KD as an embedded language (`io.kixi.lang`): `lang KD { ... }` in KS, and in any
 * other host. The block is KD, read by [KDParser] exactly as a KD file is, plus
 * interpolations that bring the host's values in:
 *
 * - `$name`, `${expression}` and `$(expression)` where a value can stand (a tag's
 *   value, an attribute's value, a list or map element or key, an annotation's
 *   value, a grid cell, a range's bound) put the host's value there as it is: never
 *   as text, so nothing the value contains is read as KD. An attribute's name cannot
 *   be interpolated.
 * - Inside a `"…"` or `"""…"""` string, and inside a dot literal's parentheses
 *   (`.ref($id)`, which its bag reads as text), they put the value's text in (the host
 *   says how a value prints); `\$` is a literal `$` in a string. Raw strings (`@"…"`,
 *   `` `…` ``), chars and comments are never interpolated.
 * - `$` followed by a digit is KD's currency prefix (`$5.25`), and `$` inside a word is
 *   part of it (`active$x` is one bare string), as in a KD file.
 *
 * Everything else is plain KD: a bare word is a string (`status=active`), a map key
 * is a value (`[fee = 23]`), and a type bag's literal is read through its bag.
 */
object KDLanguage : EmbeddedLanguage {

    override val name: String = "KD"

    override fun scan(source: String, start: Int): EmbeddedBlock {
        val out = ArrayList<EmbeddedInterpolation>()
        var depth = 0
        var i = start
        while (true) {
            if (i >= source.length) throw EmbeddedLanguageException("Unterminated KD block: expected '}'", start - 1)
            val c = source[i]
            when {
                c == '}' -> { if (depth == 0) return EmbeddedBlock(i, out); depth--; i++ }
                c == '{' -> { depth++; i++ }
                c == '#' || (c == '/' && at(source, i + 1) == '/') -> { while (i < source.length && source[i] != '\n') i++ }
                c == '/' && at(source, i + 1) == '*' -> i = blockComment(source, i)
                c == '"' && source.startsWith("\"\"\"", i) -> i = string(source, i + 3, triple = true, out)
                c == '"' -> i = string(source, i + 1, triple = false, out)
                c == '@' && source.startsWith("@\"\"\"", i) -> {
                    val e = source.indexOf("\"\"\"", i + 4)
                    if (e < 0) throw EmbeddedLanguageException("Unterminated string", i)
                    i = e + 3
                }
                c == '@' && at(source, i + 1) == '"' -> {
                    val e = source.indexOf('"', i + 2)
                    if (e < 0) throw EmbeddedLanguageException("Unterminated string", i)
                    i = e + 1
                }
                c == '`' && source.startsWith("```", i) -> {
                    val e = source.indexOf("```", i + 3)
                    if (e < 0) throw EmbeddedLanguageException("Unterminated string", i)
                    i = e + 3
                }
                c == '`' -> {
                    val e = source.indexOf('`', i + 1)
                    if (e < 0) throw EmbeddedLanguageException("Unterminated string", i)
                    i = e + 1
                }
                c == '\'' -> i = quotedChar(source, i)
                c == '<' && isUrlStart(source, i) -> {
                    val e = source.indexOf('>', i)
                    i = if (e < 0) source.length else e + 1
                }
                c == '$' -> i = interpolation(source, i, out) ?: (i + 1)
                isWordStart(c) -> i = word(source, i)
                else -> i++
            }
        }
    }

    override fun evaluate(source: EmbeddedSource, values: Map<String, Any?>): Any? =
        evaluate(source, values) { v -> if (v == null) "nil" else v.toString() }

    /**
     * Reads the block [source] with the interpolations' [values] (by key, as [scan]
     * reported them). [stringify] gives the text of a value put into a string. Returns
     * the block's tag, or a tag `root` holding several (as `KD.read` does).
     */
    fun evaluate(source: EmbeddedSource, values: Map<String, Any?>, stringify: (Any?) -> String): Any? {
        val text = source.text
        val block = scan("$text}", 0)
        val spans = HashMap<Int, KDParser.Interpolated>()
        for (x in block.interpolations) spans[x.start] = KDParser.Interpolated(x.end, values[x.key])
        try {
            return KDParser(KDParser.Interpolation(spans, stringify)).parse(text)
        } catch (e: KDParser.PositionedException) {
            throw EmbeddedLanguageException(e.reason, e.offset, cause = e)
        } catch (e: io.kixi.text.ParseException) {
            // A KD error without a position in the block (from a library KD calls): the host reports it at the block.
            throw EmbeddedLanguageException(e.message, -1, cause = e)
        }
    }

    /**
     * Reads [source] as a KD document of any number of top-level tags with the
     * interpolations' [values] (by key, as [scan] reports them), as [evaluate] reads a
     * block: `insert into compounds compound name=$name` in KQ reads its documents so.
     */
    fun evaluateDocument(source: EmbeddedSource, values: Map<String, Any?>, stringify: (Any?) -> String = { v -> if (v == null) "nil" else v.toString() }): List<Tag> {
        val text = source.text
        val block = scan("$text}", 0)
        if (block.end != text.length) throw EmbeddedLanguageException("Unexpected '}'", block.end)
        val spans = HashMap<Int, KDParser.Interpolated>()
        for (x in block.interpolations) spans[x.start] = KDParser.Interpolated(x.end, values[x.key])
        try {
            return KDParser(KDParser.Interpolation(spans, stringify)).parseDocument(text)
        } catch (e: KDParser.PositionedException) {
            throw EmbeddedLanguageException(e.reason, e.offset, cause = e)
        } catch (e: io.kixi.text.ParseException) {
            throw EmbeddedLanguageException(e.message, -1, cause = e)
        }
    }

    /** Scans a `"…"` (or `"""…"""`) string whose content starts at [from]; returns the position after it. */
    private fun string(source: String, from: Int, triple: Boolean, out: MutableList<EmbeddedInterpolation>): Int {
        var k = from
        while (k < source.length) {
            val c = source[k]
            when {
                c == '\\' && (!triple || at(source, k + 1) == '$') -> k += 2
                triple && source.startsWith("\"\"\"", k) -> return k + 3
                !triple && c == '"' -> return k + 1
                c == '$' -> k = interpolation(source, k, out) ?: (k + 1)
                else -> k++
            }
        }
        throw EmbeddedLanguageException("Unterminated string", from - if (triple) 3 else 1)
    }

    /** A `/* … */` comment starting at [i]; they nest, as in KDParser. Returns the position after it. */
    private fun blockComment(source: String, i: Int): Int {
        var depth = 0
        var k = i
        while (k < source.length) {
            when {
                source.startsWith("/*", k) -> { depth++; k += 2 }
                source.startsWith("*/", k) -> { depth--; k += 2; if (depth == 0) return k }
                else -> k++
            }
        }
        throw EmbeddedLanguageException("Unterminated comment", i)
    }

    /**
     * A word starting at [i]: KD reads letters, digits, `_` and `$` as one identifier or
     * bare string (`active$x` is a string, not a value), and a word followed by `://` as a
     * naked URL up to whitespace or one of `{}[](),;` (its `//` and `#` are not comments).
     * Returns the position after it.
     */
    private fun word(source: String, i: Int): Int {
        var k = i + 1
        while (k < source.length && isWordPart(source[k])) k++
        if (!source.startsWith("://", k)) return k
        while (k < source.length && !source[k].isWhitespace() && source[k] !in "{}[](),;") k++
        return k
    }

    // As KDParser.isIdentifierStart / isIdentifierPart.
    private fun isWordStart(c: Char) = c.isLetter() || c == '_' || c.isSurrogate()
    private fun isWordPart(c: Char) = c.isLetterOrDigit() || c == '_' || c == '$' || c.isSurrogate()

    /** A `'…'` character literal on one line, or a lone apostrophe (plain text). */
    private fun quotedChar(source: String, i: Int): Int {
        var k = i + 1
        while (k < source.length && source[k] != '\'' && source[k] != '\n') { if (source[k] == '\\') k++; k++ }
        return if (k < source.length && source[k] == '\'') k + 1 else i + 1
    }

    /** Reads the interpolation at [start] (a `$`) into [out] and returns the position after it, or null when it is not one. */
    private fun interpolation(source: String, start: Int, out: MutableList<EmbeddedInterpolation>): Int? {
        val next = at(source, start + 1)
        return when {
            next == '{' || next == '(' -> {
                val close = if (next == '{') '}' else ')'
                val end = EmbeddedText.closing(source, start + 1, next, close)
                    ?: throw EmbeddedLanguageException("Unterminated interpolation: expected '$close'", start)
                val expression = source.substring(start + 2, end)
                if (expression.isBlank()) throw EmbeddedLanguageException("Empty interpolation", start)
                out.add(EmbeddedInterpolation(start, end + 1, expression.trim(), expression, start + 2))
                end + 1
            }
            next != null && (next.isLetter() || next == '_') -> {
                var end = start + 2
                while (end < source.length && (source[end].isLetterOrDigit() || source[end] == '_')) end++
                val name = source.substring(start + 1, end)
                out.add(EmbeddedInterpolation(start, end, name, name, start + 1))
                end
            }
            else -> null
        }
    }

    private fun isUrlStart(source: String, i: Int): Boolean {
        var k = i + 1
        while (k < source.length && source[k].isLetter()) k++
        return k > i + 1 && source.startsWith("://", k)
    }

    private fun at(source: String, i: Int): Char? = if (i < source.length) source[i] else null
}

/**
 * [KDLanguage] for `ServiceLoader` (`META-INF/services/io.kixi.lang.EmbeddedLanguage`),
 * which needs a class with a public constructor, so any host finds `KD` by name
 * through `io.kixi.lang.EmbeddedLanguages`.
 */
class KDEmbeddedLanguage : EmbeddedLanguage by KDLanguage
