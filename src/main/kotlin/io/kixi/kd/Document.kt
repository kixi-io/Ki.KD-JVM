package io.kixi.kd

import io.kixi.kd.schema.KDSchema
import io.kixi.kd.schema.KDValidationException
import io.kixi.kd.schema.ValidationIssue
import io.kixi.kd.schema.ValidationOptions
import io.kixi.kd.schema.ValidationResult

/**
 * A KD document as returned by [KD.load]: its top-level tags with snips
 * resolved, and, when the document declared a schema, the compiled schema
 * and the validation outcome.
 *
 * Simple documents stay simple: with no schema declaration, [schema] and
 * [validation] are null, [isValid] is true, and [tags] is just the data.
 *
 * ```kotlin
 * val doc = KD.load(File("run.kd"))
 * doc.root["id"]                 // work with the data
 * if (!doc.isValid) println(doc.report())
 * doc.requireValid()             // or fail loudly
 * ```
 *
 * @property tags The top-level data tags. A schema declaration is not included.
 * @property schema The schema the document declared, or null.
 * @property validation The result of validating [tags] against [schema], or null when there is no schema.
 */
class Document internal constructor(
    val tags: List<Tag>,
    val schema: KDSchema?,
    val validation: ValidationResult?
) {
    /** True when the document declared a schema. */
    val hasSchema: Boolean get() = schema != null

    /** True when there is no schema, or the data satisfies it. */
    val isValid: Boolean get() = validation?.isValid ?: true

    /** Validation issues; empty when valid or when no schema was declared. */
    val issues: List<ValidationIssue> get() = validation?.issues ?: emptyList()

    /**
     * The single top-level tag, or a synthetic `root` tag wrapping several
     * (or none). This mirrors [KD.read], so code written against it keeps working.
     */
    val root: Tag by lazy {
        if (tags.size == 1) tags.single() else Tag("root").apply { children.addAll(tags) }
    }

    /** Throws [KDValidationException] if the document declared a schema and does not satisfy it. */
    fun requireValid(): Document {
        validation?.requireValid()
        return this
    }

    /**
     * A human-readable summary: `no schema`, `<Schema>: valid`, or the
     * schema name followed by [ValidationResult.report].
     */
    fun report(): String = when {
        schema == null -> "no schema"
        else -> "${schema.name}: ${validation!!.report()}"
    }

    override fun toString(): String =
        "Document(tags=${tags.size}, schema=${schema?.name ?: "none"}, valid=$isValid)"
}

/**
 * Options for [KD.load].
 *
 * @property resolveSnips Resolve `.snip` directives relative to the source (default true).
 *   When false, directives stay in the tree and a schema reports them as UNRESOLVED_SNIP.
 * @property requireSchema Fail with [io.kixi.kd.schema.KDSchemaException] when the
 *   document declares no schema (default false: schemas are optional).
 * @property validation Limits for schema validation.
 * @property snipResolver Resolver (and security options) for snips.
 */
data class LoadOptions(
    val resolveSnips: Boolean = true,
    val requireSchema: Boolean = false,
    val validation: ValidationOptions = ValidationOptions(),
    val snipResolver: SnipResolver = SnipResolver()
) {
    companion object {
        /** Schemas optional, snips resolved. */
        @JvmField val DEFAULT = LoadOptions()
        /** Like [DEFAULT] but a document without a schema declaration is an error. */
        @JvmField val SCHEMA_REQUIRED = LoadOptions(requireSchema = true)
    }
}
