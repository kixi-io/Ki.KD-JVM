package io.kixi.kd.schema

import io.kixi.Range
import io.kixi.kd.Tag
import io.kixi.uom.Quantity
import io.kixi.uom.Unit as MeasureUnit

internal class SchemaCompiler {
    private val valueDefs = linkedMapOf<String, Pair<Tag, String>>()
    private val tagDefs = linkedMapOf<String, Pair<Tag, String>>()
    private val compiledValues = mutableMapOf<String, ValueRule>()
    private val compiledTags = mutableMapOf<String, BodyRule>()
    private val resolving = mutableSetOf<String>()
    private var depth = 0

    fun compile(tags: List<Tag>): KDSchema {
        if (tags.size != 1) fail("/", "Expected exactly one schema tag")
        val root = tags.single()
        val path = "/schema"
        if (root.name != "schema" || root.namespace.isNotEmpty()) fail(path, "Expected schema")
        check(root, path, setOf("kds", "additionalChildren"), setOf("definitions", "tag"), 1)
        if (root.attributes.entries.firstOrNull { it.key.name == "kds" }?.value != 1)
            fail(path, "kds=1 is required")
        val name = stringValue(root, path)
        val defs = singleton(root, "definitions", path)
        if (defs != null) {
            check(defs, "$path/definitions", emptySet(), setOf("valueType", "tagType"), 0)
            defs.children.forEachIndexed { i, d ->
                val dp = "$path/definitions/${d.name}[$i]"
                val n = stringValue(d, dp)
                if (n.isEmpty()) fail(dp, "Definition name must not be empty")
                val table = if (d.name == "valueType") valueDefs else tagDefs
                if (table.put(n, d to dp) != null) fail(dp, "Duplicate definition $n")
            }
        }
        // Check unused definitions too: typos must not be silently accepted.
        valueDefs.keys.forEach { valueDefinition(it, path) }
        tagDefs.keys.forEach { tagDefinition(it, path) }
        val rules = compileTags(root.children.filter { it.name == "tag" }, path)
        return KDSchema(name, DocumentRule(path, rules, bool(root, "additionalChildren", false, path)))
    }

    private fun compileTags(tags: List<Tag>, path: String): List<TagRule> {
        val result = tags.mapIndexed { i, tag -> tag(tag, "$path/${tag.name}[$i]") }
        if (result.map { it.namespace to it.name }.distinct().size != result.size)
            fail(path, "Duplicate tag or annotation rule for the same name and namespace")
        return result
    }

    private fun tag(t: Tag, p: String): TagRule = nested(p) {
        val name = stringValue(t, p)
        val ns = text(t, "namespace", p) ?: ""
        val occurs = occurrence(t, p)
        val ref = text(t, "ref", p)
        val body = if (ref != null) {
            check(t, p, setOf("namespace", "occurs", "ref"), emptySet(), 1)
            tagDefinition(ref, p)
        } else body(t, p, setOf("namespace", "occurs"), 1, t.name == "annotation")
        if (t.name == "annotation" && (body.children.tags.isNotEmpty() || body.children.additional || body.annotations.isNotEmpty()))
            fail(p, "Annotation rules cannot have children or annotations")
        TagRule(name, ns, p, occurs, body)
    }

    private fun body(t: Tag, p: String, extra: Set<String>, values: Int, annotation: Boolean = false): BodyRule {
        val attrs = (if (annotation) setOf("additionalAttributes", "additionalValues")
        else setOf("additionalAttributes", "additionalValues", "additionalAnnotations", "additionalChildren")) + extra
        check(t, p, attrs, if (annotation) setOf("attribute", "value", "values") else setOf("attribute", "value", "values", "children", "annotation"), values)
        val attributes = t.children.filter { it.name == "attribute" }.mapIndexed { i, a ->
            val ap = "$p/attribute[$i]"
            val n = stringValue(a, ap)
            AttributeRule(n, text(a, "namespace", ap) ?: "", bool(a, "required", false, ap), ap,
                value(a, ap, setOf("namespace", "required"), 1))
        }
        if (attributes.map { it.namespace to it.name }.distinct().size != attributes.size)
            fail(p, "Duplicate attribute rule")
        var optional = false
        val positions = t.children.filter { it.name == "value" }.mapIndexed { i, v ->
            val vp = "$p/value[$i]"
            val required = bool(v, "required", true, vp)
            if (optional && required) fail(vp, "Required positional values cannot follow optional values")
            if (!required) optional = true
            PositionRule(required, value(v, vp, setOf("required"), 0))
        }
        val sequence = singleton(t, "values", p)?.let { v ->
            if (positions.isNotEmpty()) fail(p, "value and values cannot be combined")
            val vp = "$p/values"
            val min = count(v, "minCount", vp) ?: 0
            val max = count(v, "maxCount", vp)
            bounds(min, max, vp)
            if (bool(t, "additionalValues", false, p)) fail(p, "additionalValues cannot be combined with values")
            // Here minCount/maxCount constrain the sequence, not each collection-valued member.
            ValuesRule(min, max, value(v, vp, setOf("minCount", "maxCount"), 0, sequence = true))
        }
        val childTag = singleton(t, "children", p)
        if (childTag != null && has(t, "additionalChildren"))
            fail(p, "Put additionalChildren on the children rule when a children rule exists")
        val children = if (childTag == null) DocumentRule(p, emptyList(), bool(t, "additionalChildren", false, p)) else {
            val cp = "$p/children"
            check(childTag, cp, setOf("additionalChildren"), setOf("tag"), 0)
            DocumentRule(cp, compileTags(childTag.children, cp), bool(childTag, "additionalChildren", false, cp))
        }
        return BodyRule(p, attributes, positions, sequence, children,
            compileTags(t.children.filter { it.name == "annotation" }, p),
            bool(t, "additionalAttributes", false, p), bool(t, "additionalValues", false, p),
            bool(t, "additionalAnnotations", true, p))
    }

    private val valueAttributes = setOf("type", "nullable", "min", "max", "exclusiveMin", "exclusiveMax",
        "minLength", "maxLength", "pattern", "enum", "minCount", "maxCount", "rows", "columns", "dimension", "unit")

    private fun value(t: Tag, p: String, extra: Set<String> = emptySet(), valueCount: Int = 0, sequence: Boolean = false): ValueRule = nested(p) {
        val ref = text(t, "ref", p)
        if (ref != null) {
            check(t, p, extra + "ref", emptySet(), valueCount)
            return@nested valueDefinition(ref, p)
        }
        check(t, p, valueAttributes + extra, setOf("items", "keys"), valueCount)
        val type = text(t, "type", p) ?: "Any"
        if (type !in SchemaValues.types) fail(p, "Unknown type $type")
        val nullable = bool(t, "nullable", false, p)
        if (type == "nil" && has(t, "nullable")) fail(p, "The nil type already matches only nil")
        val pattern = text(t, "pattern", p)?.let {
            try { Regex(it) } catch (e: IllegalArgumentException) { fail(p, "Invalid pattern: ${e.message}") }
        }
        val minLength = count(t, "minLength", p)
        val maxLength = count(t, "maxLength", p)
        bounds(minLength, maxLength, p)
        if ((minLength != null || maxLength != null || pattern != null) && type != "String")
            fail(p, "String constraints require type=String")
        val minCount = if (sequence) null else count(t, "minCount", p)
        val maxCount = if (sequence) null else count(t, "maxCount", p)
        bounds(minCount, maxCount, p)
        if ((minCount != null || maxCount != null) && type !in setOf("List", "Map", "Grid"))
            fail(p, "Count constraints require List, Map, or Grid")
        val rows = count(t, "rows", p)
        val columns = count(t, "columns", p)
        if ((rows != null || columns != null) && type != "Grid") fail(p, "rows and columns require Grid")
        val dimension = text(t, "dimension", p)
        val unit = text(t, "unit", p)?.let { MeasureUnit.getUnit(it) ?: fail(p, "Unknown unit $it") }
        if ((dimension != null || unit != null) && type != "Quantity") fail(p, "Unit constraints require Quantity")
        if (dimension != null && MeasureUnit.allUnits().none { it.dimensionName == dimension })
            fail(p, "Unknown dimension $dimension")
        if (dimension != null && unit != null && unit.dimensionName != dimension) fail(p, "Unit and dimension disagree")
        val items = singleton(t, "items", p)?.let { value(it, "$p/items") }
        val keys = singleton(t, "keys", p)?.let { value(it, "$p/keys") }
        if (items != null && type !in setOf("List", "Map", "Grid", "Range")) fail(p, "items requires List, Map, Grid, or Range")
        if (keys != null && type != "Map") fail(p, "keys requires Map")
        if (has(t, "min") && has(t, "exclusiveMin")) fail(p, "Use min or exclusiveMin, not both")
        if (has(t, "max") && has(t, "exclusiveMax")) fail(p, "Use max or exclusiveMax, not both")
        val boundNames = listOf("min", "max", "exclusiveMin", "exclusiveMax")
        val boundValues = boundNames.map { key ->
            if (!has(t, key)) null else {
                val b = attr(t, key) ?: fail(p, "$key cannot be nil")
                if (type == "Quantity") {
                    if (b !is Quantity<*>) fail(p, "$key must be a quantity")
                    if (dimension != null && b.unit.dimensionName != dimension) fail(p, "$key has an incompatible dimension")
                    if (unit != null && !b.unit.isCompatibleWith(unit)) fail(p, "$key has an incompatible unit")
                    if (unit is io.kixi.uom.Currency && b.unit != unit) fail(p, "Currency bounds must use the restricted currency")
                } else if (type !in setOf("Number", "Int", "Long", "Float", "Double", "Dec") || b !is Number)
                    fail(p, "Bounds require a numeric type or Quantity")
                if (SchemaValues.compare(b, b) == null) fail(p, "Bounds must be finite and comparable")
                b
            }
        }
        val lo = boundValues[0] ?: boundValues[2]
        val hi = boundValues[1] ?: boundValues[3]
        if (lo != null && hi != null) {
            val cmp = SchemaValues.compare(lo, hi) ?: fail(p, "Bounds are incompatible")
            if (cmp > 0 || (cmp == 0 && (boundValues[2] != null || boundValues[3] != null))) fail(p, "Empty bound interval")
        }
        val choices = if (has(t, "enum")) {
            val list = attr(t, "enum") as? List<*> ?: fail(p, "enum must be a list")
            if (list.isEmpty()) fail(p, "enum must not be empty")
            list.toList()
        } else null
        val rule = ValueRule(p, type, nullable, boundValues[0], boundValues[1], boundValues[2], boundValues[3],
            minLength, maxLength, pattern, choices, minCount, maxCount, rows, columns, dimension, unit, items, keys)
        choices?.forEach { choice ->
            val errors = SchemaValidator(ValidationOptions()).validateValue(rule.copy(choices = null), choice)
            if (errors.isNotEmpty()) fail(p, "enum member violates its value rule: ${errors.first().message}")
        }
        rule
    }

    private fun valueDefinition(name: String, p: String): ValueRule = compiledValues.getOrPut(name) {
        resolving("value:$name", p) {
            val (t, dp) = valueDefs[name] ?: fail(p, "Unknown value definition $name")
            value(t, dp, valueCount = 1)
        }
    }

    private fun tagDefinition(name: String, p: String): BodyRule = compiledTags.getOrPut(name) {
        resolving("tag:$name", p) {
            val (t, dp) = tagDefs[name] ?: fail(p, "Unknown tag definition $name")
            nested(dp) { body(t, dp, emptySet(), 1) }
        }
    }

    private fun <T> resolving(key: String, p: String, block: () -> T): T {
        if (!resolving.add(key)) fail(p, "Cyclic definition reference: $key")
        return try { block() } finally { resolving.remove(key) }
    }

    private fun <T> nested(p: String, block: () -> T): T {
        if (++depth > 64) fail(p, "Schema nesting/reference depth exceeds 64")
        return try { block() } finally { depth-- }
    }

    private fun occurrence(t: Tag, p: String): Occurrence {
        if (!has(t, "occurs")) return Occurrence()
        val v = attr(t, "occurs")
        if (v is Int && v >= 0) return Occurrence(v, v)
        if (v is Range<*> && v.bound == Range.Bound.Inclusive && v.start is Int && (v.end == null || v.end is Int)) {
            val min = v.start as Int
            val max = v.end as Int?
            if (min < 0) fail(p, "occurs cannot be negative")
            bounds(min, max, p)
            return Occurrence(min, max)
        }
        fail(p, "occurs must be a nonnegative Int or inclusive Int range such as 0..1 or 1.._")
    }

    private fun check(t: Tag, p: String, attrs: Set<String>, children: Set<String>, values: Int) {
        if (t.namespace.isNotEmpty() || t.annotations.isNotEmpty() || t.snipDirective != null || t.schemaDirective != null)
            fail(p, "Schema vocabulary must be unnamespaced and cannot carry annotations or directives")
        if (t.values.size != values) fail(p, "Expected $values positional value(s)")
        t.attributes.keys.forEach { if (it.namespace.isNotEmpty() || it.name !in attrs) fail(p, "Unknown attribute $it") }
        t.children.forEach { if (it.namespace.isNotEmpty() || it.name !in children) fail(p, "Unknown child ${it.nsid}") }
    }

    private fun singleton(t: Tag, name: String, p: String): Tag? {
        val list = t.children.filter { it.name == name }
        if (list.size > 1) fail(p, "Only one $name rule is allowed")
        return list.firstOrNull()
    }
    private fun stringValue(t: Tag, p: String): String =
        t.values.singleOrNull() as? String ?: fail(p, "Expected one string name (quote reserved literals such as true)")
    private fun attr(t: Tag, key: String): Any? = t.attributes[io.kixi.NSID(key)]
    private fun has(t: Tag, key: String): Boolean = t.attributes.containsKey(io.kixi.NSID(key))
    private fun text(t: Tag, key: String, p: String): String? =
        if (!has(t, key)) null else attr(t, key) as? String ?: fail(p, "$key must be a string")
    private fun bool(t: Tag, key: String, default: Boolean, p: String): Boolean =
        if (!has(t, key)) default else attr(t, key) as? Boolean ?: fail(p, "$key must be a Boolean")
    private fun count(t: Tag, key: String, p: String): Int? {
        if (!has(t, key)) return null
        val n = attr(t, key) as? Int ?: fail(p, "$key must be an Int")
        if (n < 0) fail(p, "$key must be nonnegative")
        return n
    }
    private fun bounds(min: Int?, max: Int?, p: String) {
        if (min != null && max != null && min > max) fail(p, "Minimum exceeds maximum")
    }
    private fun fail(p: String, message: String): Nothing = throw KDSchemaException(p, message)
}
