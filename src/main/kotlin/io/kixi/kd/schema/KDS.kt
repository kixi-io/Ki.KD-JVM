package io.kixi.kd.schema

import io.kixi.kd.Document
import io.kixi.kd.KD
import io.kixi.kd.LoadOptions
import io.kixi.kd.SnipResolver
import io.kixi.kd.Tag
import java.io.File
import java.io.Reader
import java.nio.file.Path

/** Compiles version 1 KDS schemas. A schema is an ordinary KD document. */
object KDS {
    @JvmStatic
    fun compile(text: String): KDSchema = SchemaCompiler().compile(KD.readDocument(text))

    /** Does not close the caller-owned reader. */
    @JvmStatic
    fun compile(reader: Reader): KDSchema = compile(reader.readText())

    @JvmStatic
    fun compile(file: File): KDSchema = file.bufferedReader(Charsets.UTF_8).use { compile(it) }

    // ========================================================================
    // Self-describing documents
    // ========================================================================

    /**
     * Loads a KD file that must declare its schema. This is
     * `KD.load(file, LoadOptions.SCHEMA_REQUIRED)`: see [KD.load] for the
     * declaration forms and the loading rules. Use [KD.load] directly when
     * a schema is optional.
     */
    @JvmStatic
    @JvmOverloads
    fun read(file: File, options: ValidationOptions = ValidationOptions(),
             snipResolver: SnipResolver = SnipResolver()): Document =
        KD.load(file, LoadOptions(requireSchema = true, validation = options, snipResolver = snipResolver))

    /** Loads self-describing KD text; see [read] and [KD.load]. */
    @JvmStatic
    @JvmOverloads
    fun read(text: String, basePath: Path? = null, options: ValidationOptions = ValidationOptions(),
             snipResolver: SnipResolver = SnipResolver()): Document =
        KD.load(text, basePath, LoadOptions(requireSchema = true, validation = options, snipResolver = snipResolver))
}

/** A compiled schema. Validation never changes document values or resolves external resources. */
class KDSchema internal constructor(val name: String, private val rule: DocumentRule) {
    /** Parses exactly the top-level document tags, including a real tag named root. */
    @JvmOverloads
    fun validate(text: String, options: ValidationOptions = ValidationOptions()): ValidationResult =
        validate(KD.readDocument(text), options)

    /** Validates a single actual tag, never interpreting its name as a synthetic root. */
    @JvmOverloads
    fun validate(tag: Tag, options: ValidationOptions = ValidationOptions()): ValidationResult =
        validate(listOf(tag), options)

    /** Use this overload for already-parsed documents or explicitly resolved snips. */
    @JvmOverloads
    fun validate(tags: List<Tag>, options: ValidationOptions = ValidationOptions()): ValidationResult =
        SchemaValidator(options).validate(rule, tags)
}

/** Limits validation of programmatically constructed trees as well as ordinary parsed documents. */
data class ValidationOptions(val maxIssues: Int = 100, val maxDepth: Int = 128) {
    init {
        require(maxIssues > 0) { "maxIssues must be positive" }
        require(maxDepth > 0) { "maxDepth must be positive" }
    }
}

data class ValidationIssue(
    val code: String,
    val documentPath: String,
    val schemaPath: String,
    val message: String,
    val expected: String? = null,
    val actual: String? = null
)

data class ValidationResult(val issues: List<ValidationIssue>, val truncated: Boolean = false) {
    val isValid: Boolean get() = issues.isEmpty()
    fun requireValid() {
        if (!isValid) throw KDValidationException(this)
    }

    /**
     * A human-readable, multi-line summary: `valid` for a clean result, or an
     * issue count followed by one entry per issue with its code, document
     * path, message, and expected/actual values when present.
     */
    fun report(): String {
        if (isValid) return "valid"
        val sb = StringBuilder("${issues.size} issue(s)")
        if (truncated) sb.append(" (list truncated)")
        for (i in issues) {
            sb.append("\n  ").append(i.code.padEnd(20)).append(i.documentPath)
            sb.append("\n      ").append(i.message)
            when {
                i.expected != null && i.actual != null -> sb.append("  [expected ${i.expected}, actual ${i.actual}]")
                i.expected != null -> sb.append("  [expected ${i.expected}]")
                i.actual != null -> sb.append("  [actual ${i.actual}]")
            }
        }
        return sb.toString()
    }
}

class KDValidationException(val result: ValidationResult) : IllegalArgumentException(
    result.issues.joinToString("\n") { "${it.documentPath}: ${it.code}: ${it.message}" }
)

/** A malformed schema is different from data which does not satisfy a valid schema. */
class KDSchemaException(val schemaPath: String, detail: String) :
    IllegalArgumentException("$schemaPath: $detail")

internal data class Occurrence(val min: Int = 1, val max: Int? = 1)
internal data class DocumentRule(val path: String, val tags: List<TagRule>, val additional: Boolean)
internal data class TagRule(
    val name: String, val namespace: String, val path: String,
    val occurs: Occurrence, val body: BodyRule
)
internal data class AttributeRule(
    val name: String, val namespace: String, val required: Boolean,
    val path: String, val value: ValueRule
)
internal data class PositionRule(val required: Boolean, val value: ValueRule)
internal data class ValuesRule(val min: Int, val max: Int?, val value: ValueRule)
internal data class BodyRule(
    val path: String,
    val attributes: List<AttributeRule>,
    val positions: List<PositionRule>,
    val values: ValuesRule?,
    val children: DocumentRule,
    val annotations: List<TagRule>,
    val additionalAttributes: Boolean,
    val additionalValues: Boolean,
    val additionalAnnotations: Boolean
)
internal data class ValueRule(
    val path: String,
    val type: String,
    val nullable: Boolean,
    val min: Any?, val max: Any?,
    val exclusiveMin: Any?, val exclusiveMax: Any?,
    val minLength: Int?, val maxLength: Int?, val pattern: Regex?,
    val choices: List<Any?>?,
    val minCount: Int?, val maxCount: Int?,
    val rows: Int?, val columns: Int?,
    val dimension: String?, val unit: io.kixi.uom.Unit?,
    val items: ValueRule?, val keys: ValueRule?
)
