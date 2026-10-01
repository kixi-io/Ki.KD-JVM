package io.kixi.kd.schema

import io.kixi.Call
import io.kixi.Grid
import io.kixi.Ki
import io.kixi.NSID
import io.kixi.Range
import io.kixi.kd.Tag
import io.kixi.uom.Quantity
import java.util.IdentityHashMap

internal class SchemaValidator(private val options: ValidationOptions) {
    private val issues = mutableListOf<ValidationIssue>()
    private var truncated = false
    private val full: Boolean get() = issues.size >= options.maxIssues

    fun validate(rule: DocumentRule, tags: List<Tag>): ValidationResult {
        val active = IdentityHashMap<Any, Boolean>()
        tags.forEachIndexed { i, tag -> scan(tag, "/tags[$i]", rule.path, 0, active) }
        // Unsafe graphs and unresolved directives must be addressed before structural validation.
        if (issues.isEmpty()) document(rule, tags, "")
        return ValidationResult(issues.toList(), truncated)
    }

    fun validateValue(rule: ValueRule, value: Any?): List<ValidationIssue> {
        value(rule, value, "/")
        return issues.toList()
    }

    private fun add(code: String, dp: String, sp: String, message: String, expected: String? = null, actual: String? = null) {
        if (full) { truncated = true; return }
        issues.add(ValidationIssue(code, dp, sp, message, expected, actual))
        if (full) truncated = true
    }

    private fun scan(v: Any?, p: String, sp: String, depth: Int, active: IdentityHashMap<Any, Boolean>) {
        if (full || v == null) return
        if (depth > options.maxDepth) {
            add("DEPTH_LIMIT", p, sp, "Document nesting exceeds ${options.maxDepth}")
            return
        }
        if (v !is Call && v !is List<*> && v !is Map<*, *> && v !is Grid<*> && v !is Range<*>) return
        if (active.put(v, true) != null) {
            add("CYCLIC_DOCUMENT", p, sp, "Document contains a cycle")
            return
        }
        try {
            when (v) {
                is Call -> {
                    if (v is Tag) {
                        if (v.snipDirective != null) add("UNRESOLVED_SNIP", p, sp, "Resolve snip directives explicitly before validation")
                        v.children.forEachIndexed { i, child -> scan(child, "$p/children[$i]", sp, depth + 1, active) }
                        v.annotations.forEachIndexed { i, a -> scan(a, "$p/annotations[$i]", sp, depth + 1, active) }
                    }
                    v.values.forEachIndexed { i, x -> scan(x, "$p/values[$i]", sp, depth + 1, active) }
                    v.attributes.entries.sortedBy { it.key.toString() }.forEach { (k, x) ->
                        scan(x, "$p/attributes/${escape(k.toString())}", sp, depth + 1, active)
                    }
                }
                is List<*> -> v.forEachIndexed { i, x -> scan(x, "$p/items[$i]", sp, depth + 1, active) }
                is Map<*, *> -> v.entries.forEachIndexed { i, e ->
                    scan(e.key, "$p/keys[$i]", sp, depth + 1, active)
                    scan(e.value, "$p/items[$i]", sp, depth + 1, active)
                }
                is Grid<*> -> v.toList().forEachIndexed { i, x -> scan(x, "$p/items[$i]", sp, depth + 1, active) }
                is Range<*> -> {
                    scan(v.start, "$p/start", sp, depth + 1, active)
                    scan(v.end, "$p/end", sp, depth + 1, active)
                }
            }
        } finally { active.remove(v) }
    }

    private fun document(rule: DocumentRule, tags: List<Tag>, p: String) {
        if (full) return
        val rules = rule.tags.associateBy { NSID(it.name, it.namespace) }
        val counts = mutableMapOf<NSID, Int>()
        tags.forEach { tag ->
            if (full) return@forEach
            val index = counts.getOrDefault(tag.nsid, 0)
            counts[tag.nsid] = index + 1
            val tp = "$p/${escape(tag.nsid.toString()).ifEmpty { "~anonymous" }}[$index]"
            val match = rules[tag.nsid]
            if (match == null) {
                if (!rule.additional) add("UNEXPECTED_TAG", tp, rule.path, "Undeclared tag ${tag.nsid}")
            } else body(match.body, tag, tp)
        }
        rule.tags.forEach { r -> occurrence(r, counts.getOrDefault(NSID(r.name, r.namespace), 0), if (p.isEmpty()) "/" else p) }
    }

    private fun occurrence(rule: TagRule, n: Int, p: String) {
        val o = rule.occurs
        if (n < o.min || (o.max != null && n > o.max))
            add("OCCURRENCE", p, rule.path, "${rule.namespace.let { if (it.isEmpty()) "" else "$it:" }}${rule.name}: expected ${o.min}..${o.max ?: "unbounded"} occurrences; found $n",
                "${o.min}..${o.max ?: "unbounded"}", n.toString())
    }

    private fun body(rule: BodyRule, call: Call, p: String) {
        if (full) return
        rule.attributes.forEach { a ->
            val id = NSID(a.name, a.namespace)
            val ap = "$p/attributes/${escape(id.toString())}"
            if (!call.attributes.containsKey(id)) {
                if (a.required) add("MISSING_ATTRIBUTE", ap, a.path, "Required attribute $id is missing")
            } else value(a.value, call.attributes[id], ap)
        }
        if (!rule.additionalAttributes) {
            val names = rule.attributes.map { NSID(it.name, it.namespace) }.toSet()
            call.attributes.keys.sortedBy { it.toString() }.filter { it !in names }.forEach {
                add("UNEXPECTED_ATTRIBUTE", "$p/attributes/${escape(it.toString())}", rule.path, "Undeclared attribute $it")
            }
        }
        val sequence = rule.values
        if (sequence != null) {
            count(call.values.size, sequence.min, sequence.max, "$p/values", sequence.value.path)
            call.values.forEachIndexed { i, v -> value(sequence.value, v, "$p/values[$i]") }
        } else {
            rule.positions.forEachIndexed { i, r ->
                if (i < call.values.size) value(r.value, call.values[i], "$p/values[$i]")
                else if (r.required) add("MISSING_VALUE", "$p/values[$i]", r.value.path, "Required positional value is missing")
            }
            if (!rule.additionalValues && call.values.size > rule.positions.size)
                add("UNEXPECTED_VALUE", "$p/values[${rule.positions.size}]", rule.path, "Unexpected positional values",
                    "at most ${rule.positions.size}", call.values.size.toString())
        }
        if (call is Tag) {
            document(rule.children, call.children, p)
            val counts = mutableMapOf<NSID, Int>()
            val rules = rule.annotations.associateBy { NSID(it.name, it.namespace) }
            call.annotations.forEach { a ->
                val index = counts.getOrDefault(a.nsid, 0)
                counts[a.nsid] = index + 1
                val ap = "$p/annotations/${escape(a.nsid.toString())}[$index]"
                val match = rules[a.nsid]
                if (match != null) body(match.body, a, ap)
                else if (!rule.additionalAnnotations) add("UNEXPECTED_ANNOTATION", ap, rule.path, "Undeclared annotation ${a.nsid}")
            }
            rule.annotations.forEach { occurrence(it, counts.getOrDefault(NSID(it.name, it.namespace), 0), "$p/annotations") }
        }
    }

    private fun value(r: ValueRule, v: Any?, p: String) {
        if (full) return
        if (v == null) {
            if (!r.nullable && r.type != "nil") add("NIL_NOT_ALLOWED", p, r.path, "nil is not allowed", r.type, "nil")
            else if (r.choices != null && r.choices.none { it == null }) add("ENUM", p, r.path, "nil is not in enum")
            return
        }
        if (!SchemaValues.matches(r.type, v)) {
            add("TYPE", p, r.path, "Expected ${r.type}; found ${v.javaClass.simpleName}", r.type, v.javaClass.simpleName)
            return
        }
        if (v is Quantity<*>) {
            if (r.dimension != null && v.unit.dimensionName != r.dimension) {
                add("DIMENSION", p, r.path, "Expected dimension ${r.dimension}; found ${v.unit.dimensionName}")
                return
            }
            if (r.unit != null && v.unit != r.unit) {
                add("UNIT", p, r.path, "Expected unit ${r.unit.symbol}; found ${v.unit.symbol}")
                return
            }
        }
        for ((bound, minimum, exclusive) in listOf(
            Triple(r.min, true, false), Triple(r.max, false, false),
            Triple(r.exclusiveMin, true, true), Triple(r.exclusiveMax, false, true))) {
            if (bound == null) continue
            val c = SchemaValues.compare(v, bound)
            if (c == null) {
                add("INCOMPARABLE_BOUND", p, r.path, "Value is not comparable with bound ${display(bound)} (requires finite numbers and compatible units/currency)")
                break
            }
            if ((minimum && (c < 0 || (exclusive && c == 0))) || (!minimum && (c > 0 || (exclusive && c == 0))))
                add(if (minimum) "VALUE_BELOW_MIN" else "VALUE_ABOVE_MAX", p, r.path,
                    "Expected ${if (minimum) if (exclusive) ">" else ">=" else if (exclusive) "<" else "<="} ${display(bound)}; found ${display(v)}",
                    display(bound), display(v))
        }
        if (v is String) {
            val n = v.codePointCount(0, v.length)
            if (r.minLength != null && n < r.minLength) add("STRING_TOO_SHORT", p, r.path, "Expected at least ${r.minLength} Unicode code points; found $n")
            if (r.maxLength != null && n > r.maxLength) add("STRING_TOO_LONG", p, r.path, "Expected at most ${r.maxLength} Unicode code points; found $n")
            if (r.pattern != null && !r.pattern.matches(v)) add("PATTERN", p, r.path, "String does not fully match ${r.pattern.pattern}")
        }
        if (r.choices != null && r.choices.none { SchemaValues.equivalent(v, it) }) add("ENUM", p, r.path, "Value is not in enum", actual = display(v))
        when (v) {
            is List<*> -> {
                count(v.size, r.minCount, r.maxCount, p, r.path)
                r.items?.let { item -> v.forEachIndexed { i, x -> value(item, x, "$p/items[$i]") } }
            }
            is Map<*, *> -> {
                count(v.size, r.minCount, r.maxCount, p, r.path)
                v.entries.forEachIndexed { i, e ->
                    r.keys?.let { value(it, e.key, "$p/keys[$i]") }
                    r.items?.let { value(it, e.value, "$p/items[$i]") }
                }
            }
            is Grid<*> -> {
                count(v.size, r.minCount, r.maxCount, p, r.path)
                if (r.rows != null && v.height != r.rows) add("GRID_ROWS", p, r.path, "Expected ${r.rows} rows; found ${v.height}")
                if (r.columns != null && v.width != r.columns) add("GRID_COLUMNS", p, r.path, "Expected ${r.columns} columns; found ${v.width}")
                r.items?.let { item ->
                    for (y in 0 until v.height) for (x in 0 until v.width) value(item, v[x, y], "$p/cells[$y,$x]")
                }
            }
            is Range<*> -> r.items?.let { item ->
                // An absent endpoint is an open bound, not a nil item.
                v.start?.let { value(item, it, "$p/start") }
                v.end?.let { value(item, it, "$p/end") }
            }
        }
    }

    private fun count(n: Int, min: Int?, max: Int?, p: String, sp: String) {
        if (min != null && n < min) add("COUNT_BELOW_MIN", p, sp, "Expected at least $min items; found $n")
        if (max != null && n > max) add("COUNT_ABOVE_MAX", p, sp, "Expected at most $max items; found $n")
    }

    private fun display(value: Any?): String = Ki.format(value).take(160)
    private fun escape(s: String): String = s.replace("~", "~0").replace("/", "~1").replace("[", "~2").replace("]", "~3")
}
