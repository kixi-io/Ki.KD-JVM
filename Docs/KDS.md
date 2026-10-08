# KDS 1 — KD Schema

KDS is an opt-in schema compiler and validator in `io.kixi.kd.schema`.
Schemas are KD documents; use `.kds` for clarity. Ki.Core 2.4.0 is unchanged.

## Quick start

```kd
schema Assay kds=1 {
    tag assay {
        attribute id type=String required=true minLength=1
        children {
            tag temperature {
                value type=Quantity dimension=Temperature min=20°C max=40°C
            }
            tag status {
                value type=String enum=[planned running complete]
            }
            tag measurement occurs=1.._ {
                attribute well type=String required=true pattern=@"[A-P]\d{1,2}"
                value type=Number min=0
            }
        }
    }
}
```

```kotlin
val schema = KDS.compile(File("assay.kds"))
val result = schema.validate(File("assay.kd").readText())
if (!result.isValid) {
    result.issues.forEach {
        println("${it.documentPath}: ${it.code}: ${it.message}")
    }
}
// Optional throwing interface:
result.requireValid()
```

`KDS.compile` accepts text, a Reader, or a File. A Reader is caller-owned;
File input is UTF-8 and closes its reader. Compiled schemas may be reused.

`validate` accepts text, an actual Tag, or a list of actual top-level Tags.
It never resolves snips, executes Call values, coerces types, fills defaults,
changes units, or mutates document contents. Do not concurrently mutate an
input document while validating it.

## Bare strings and raw strings

Use bare strings wherever the KD parser treats them as strings:

```kd
tag status occurs=1
tag plate {
    value type=Grid rows=16 columns=24 {
        items type=Number nullable=true
    }
}
```

Quoted strings remain valid and equivalent. Names containing spaces and the
empty anonymous tag name need quotes. Reserved literal words (including
`true`, `false`, `on`, `off`, `nil`, `null`, `NaN`, and `Infinity`) need quotes
when a string is intended. Use `type="nil"` for the nil-only type.

`@"..."` is KD's raw string form. It preserves backslashes and is especially
useful for regex patterns and paths. For example, `pattern=@"\d+\.\d+"`.
KDS uses KD's parser unchanged for literal interpretation. Bare strings and
raw strings are syntax for ordinary String values, not distinct KDS types.

## Document and structural rules

A schema has exactly one unnamespaced `schema` tag, one string name, and
`kds=1`. Its children are `tag` rules and optionally one `definitions` block.
Unknown vocabulary and attributes are schema errors, including unused
invalid definitions. Schema vocabulary cannot carry KD annotations.

| Rule | Values | Main attributes | Allowed children |
| --- | --- | --- | --- |
| schema | name | kds, additionalChildren | definitions, tag |
| tag | name | namespace, occurs, ref, structural options | attribute, value, values, children, annotation |
| attribute | name | namespace, required, ref, value constraints | items, keys |
| value | none | required, ref, value constraints | items, keys |
| values | none | minCount, maxCount, ref, value constraints | items, keys |
| children | none | additionalChildren | tag |
| annotation | name | namespace, occurs, ref, structural options | attribute, value, values |
| definitions | none | none | valueType, tagType |
| valueType | name | ref, value constraints | items, keys |
| tagType | name | structural options | attribute, value, values, children, annotation |
| items / keys | none | ref, value constraints | items, keys |

- Tag rules match exact, case-sensitive `(namespace, name)` pairs. Namespace
  defaults to the empty string. No namespace URI expansion occurs.
- `tag ""` matches anonymous tags. Attribute-only configuration lines remain
  separate anonymous tags; KDS does not flatten properties across them.
- `occurs` defaults to `1`. Accepts a nonnegative Int, an inclusive ascending
  Int range such as `0..1`, or a range with no upper bound such as `1.._`.
- Occurrences count matching immediate siblings. Sibling ordering is free.
  Duplicate rules for one name/namespace in one scope are schema errors.
- Attributes default to optional; `required=true` demands presence.
- Each `value` describes one position in declaration order and defaults to
  required. Only trailing positions can have `required=false`.
- `values` applies one rule to every positional value. `minCount` defaults to
  zero and `maxCount` defaults to unbounded. Cannot coexist with `value` rules.
  Its minCount/maxCount count the positional sequence, not each member's
  collection size. Use a referenced collection valueType to constrain both.
- An absent children rule permits no children, unless additionalChildren=true.
- A tagType describes a body, without a name, namespace, or occurrence rule.

Structural options are local, not inherited:

| Option | Default | Location |
| --- | --- | --- |
| additionalAttributes | false | tag / tagType / annotation |
| additionalValues | false | tag / tagType / annotation |
| additionalChildren | false | schema, children, or tag/tagType without a children block |
| additionalAnnotations | true | tag / tagType |

`additionalValues=true` permits unconstrained trailing positions after tuple
rules, or all positions if none are declared. It cannot accompany `values`.
Unknown allowed children and annotations are not structurally validated, but
are still checked for unresolved directives, graph cycles, and depth limits.
Annotation occurrences default to one when an annotation rule is declared.
Annotation rules cannot require nested annotations or children.

## Value rules

`type` defaults to Any and `nullable` defaults to false. Nullable means an
explicit nil value is permitted; it does not mean an attribute or position
can be absent. A permitted nil skips other constraints except enum membership.

Types use Ki.Core names: Any, Number, String, Char, Int, Long, Float, Double,
Dec, Bool, URL, Date, LocalDateTime, ZonedDateTime, Duration, Version, Blob,
GeoPoint, Email, Coordinate, Grid, Quantity, Range, List, Map, and `"nil"`.
KDS also recognizes Call and KiTZDateTime, which the supplied Type enum does
not cover. A KD Call remains data. Tag and Annotation objects are not accepted
as Call literals. Any accepts recognized non-null KD values.

Numeric types are exact runtime types: Int does not accept Long or a
mathematically integral Double. Number accepts supported number types.
No string parsing or numeric widening is performed during validation.

| Constraint | Types | Meaning |
| --- | --- | --- |
| min / max | numeric, Quantity | inclusive bound |
| exclusiveMin / exclusiveMax | numeric, Quantity | exclusive bound |
| minLength / maxLength | String | Unicode code-point length, not grapheme count |
| pattern | String | JVM/Kotlin regex, full-string match |
| enum | any | nonempty list of permitted values |
| minCount / maxCount | List, Map, Grid | item/entry/cell count |
| rows / columns | Grid | exact height/width |
| dimension | Quantity | registered Ki.Core dimension name |
| unit | Quantity | exact registered unit |
| items child | List, Map, Grid, Range | element/value/cell/endpoint rule |
| keys child | Map | key rule |

Inapplicable constraints are errors, not ignored. Counts are nonnegative Ints.
Bounds must be finite and comparable; contradictory intervals are errors.
`min` cannot accompany `exclusiveMin`, and similarly for maximum bounds.
Bounds support numbers and quantities in this version, not dates or versions.
A nonfinite data value cannot satisfy numeric bounds. Unbounded numeric rules
retain KD's acceptance of nonfinite values.

Numeric comparisons preserve Long and BigDecimal precision. Floating values
use their decimal string representations; comparisons do not convert all
numbers to Double. Enum matching runs after type validation. Numbers compare
by magnitude across representations, quantities by compatible-unit magnitude,
and lists/maps/grids/ranges/Calls recursively by content. Maps compare without
entry ordering. Strings and other scalar types use same-type equality; URLs
compare textual external forms without DNS resolution. NaN/infinity enum
members compare only within the same numeric representation.
Every enum member must satisfy the rest of its value rule.

Open Range endpoints are absent bounds, not nil elements, and are skipped by
items rules. Grid cell paths are `[row,column]` with zero-based coordinates.

## Quantities and currency

```kd
value type=Quantity dimension=Length min=1cm max=10cm
```

Accepts `50mm`. Bounds use Ki.Core 2.4.0 conversion, including temperature
offsets, without changing the data. Nonterminating conversions retain
Ki.Core's DECIMAL128 precision. A `unit=cm` restriction would reject millimeters
even if equivalent. Registered custom dimensions/units must be registered
before compilation. The compiler does not load classes named by the schema.

Currency bounds and enum comparisons require the same currency. There is no
exchange-rate lookup. For example:

```kd
value type=Quantity dimension=Currency unit=USD min=0USD
```

## Definitions

```kd
schema Measurements kds=1 {
    definitions {
        valueType Identifier type=String minLength=1
        tagType Measurement {
            attribute well ref=Identifier required=true
            value type=Number min=0
        }
    }
    tag measurement ref=Measurement occurs=1.._
}
```

Forward references work. Value and tag definition names occupy separate
namespaces. Value references retain local placement (`required`, attribute
name/namespace, or sequence count); tag references retain local name,
namespace, and occurs. Other constraints cannot override or merge with a
reference. Cyclic references, including recursive tag definitions, are not
supported in version 1. Nesting/reference compilation depth is limited to 64.
External schema imports, inheritance, unions, ordered-child grammars,
cross-field expressions, and default insertion are outside this version.

## Parsing, snips, and compatibility

`KD.read` still returns one tag directly and wraps zero/multiple tags in a
synthetic root. The additive `KD.readDocument` and `KDParser.parseDocument`
return the exact top-level tag list instead. A real tag named root has no
special interpretation in KDS. `schema.validate(tag)` always validates that
actual tag; pass the explicitly known children of a synthetic root yourself.

KDS does not resolve snips. Resolve them explicitly with the existing KD APIs
and pass the known top-level tags to validation. A parsed unresolved directive
produces UNRESOLVED_SNIP, including inside otherwise open content. A quoted
string with snip-like text remains ordinary string data in plain parsing.

KD's attribute maps retain the parser's existing duplicate-key behavior. KDS
validates the resulting tree and cannot recover overwritten duplicate
attributes. Similarly, KD's model does not distinguish absent from empty
child braces. Existing source-position behavior is unchanged.

## Errors and limits

KD syntax errors remain KDParseException. Valid KD with an invalid schema
raises KDSchemaException with a schema path. Data rule violations return
ValidationResult: isValid, issues, and truncated. `requireValid()` throws
KDValidationException containing that result.

Each ValidationIssue contains code, documentPath, schemaPath, message,
expected, and actual (the latter two are optional). Paths use zero-based
same-name sibling indices; map entries use iteration-order indices. Names
escape `~`, `/`, `[`, `]` as `~0`, `~1`, `~2`, `~3`. Anonymous tag paths use
`~anonymous` (distinct from a named tag). Schema paths may point into a reused definition.

ValidationOptions defaults to maxIssues=100, maxDepth=128. truncated means
the issue cap was reached and validation may be incomplete. The preflight
checks graph cycles, nesting, and unresolved snips; if it finds an issue,
structural validation waits until those issues are fixed. These depth limits
apply after KD parsing; they do not change the existing KD parser's limits.

Codes include TYPE, NIL_NOT_ALLOWED, ENUM, PATTERN, STRING_TOO_SHORT,
STRING_TOO_LONG, VALUE_BELOW_MIN, VALUE_ABOVE_MAX, INCOMPARABLE_BOUND,
DIMENSION, UNIT, GRID_ROWS, GRID_COLUMNS, COUNT_BELOW_MIN, COUNT_ABOVE_MAX,
OCCURRENCE, MISSING_ATTRIBUTE, MISSING_VALUE, UNEXPECTED_TAG,
UNEXPECTED_ATTRIBUTE, UNEXPECTED_VALUE, UNEXPECTED_ANNOTATION,
UNRESOLVED_SNIP, CYCLIC_DOCUMENT, and DEPTH_LIMIT.
## Loading documents with KD.load

`KD.load` is the one call for reading a KD file. It parses, resolves snips
relative to the file, and if the document declares a schema, compiles it and
validates the data. Schemas are optional but recommended: a file with no
declaration loads as plain data.

```kotlin
val doc = KD.load(File("run.kd"))   // Document
doc.root                            // the single top-level tag, or a synthetic root
doc.tags                            // the top-level tags
doc.hasSchema                       // did the document declare one?
doc.isValid                         // true when no schema, or the data satisfies it
doc.issues                          // validation issues (empty when none)
doc.requireValid()                  // throw KDValidationException on data problems
println(doc.report())               // "no schema", "Assay: valid", or the issue list
```

`load` also accepts a `Path`, or text with an optional base path. Text
without a base path cannot resolve a `.schema` directive or snips; an inline
schema works either way.

### Declaring a schema

The declaration is always the first top-level tag, in one of two forms.

**External schema.** The `.schema(path)` directive names a `.kds` file. It
occupies an anonymous tag of its own, like `.snip`. The path resolves
against the document's directory; a missing extension means `.kds`.

```kd
.schema(assay)

assay id="HTS-0042" operator="dan" {
    .snip(shared/instrument)
    measurement 0.82 well=A1
}
```

**Inline schema.** The schema tag itself comes first and the data follows.
A KDS schema is already a KD document, so nothing new is needed.

```kd
schema Reagents kds=1 {
    tag reagent occurs=1.._ {
        attribute name type=String required=true
        attribute volume type=Quantity dimension=Volume max=10ℓ
    }
}

reagent name="Trypsin" volume=250mℓ
reagent name="DMEM" volume=2ℓ
```

Comments may precede the declaration; tags may not. A tag named `schema`
without `kds=1` is ordinary data.

### What is thrown and what is returned

Data that violates its schema is returned in `Document.issues`, so a caller
decides how to report it. Schema problems throw `KDSchemaException` with a
path: a declaration that is not the first tag, a `.schema` directive sharing
its tag with other content, a missing or unreadable schema file, or an
invalid schema (inline or external). KD syntax errors remain
`KDParseException`; snip problems remain `SnipException`.

### LoadOptions

| Option | Default | Meaning |
| --- | --- | --- |
| resolveSnips | true | Resolve `.snip` directives. When false they stay in the tree and a schema reports UNRESOLVED_SNIP. |
| requireSchema | false | Treat a missing declaration as a schema error. `LoadOptions.SCHEMA_REQUIRED` is the ready-made instance. |
| validation | ValidationOptions() | maxIssues and maxDepth for validation. |
| snipResolver | SnipResolver() | Resolver and security options for snips. |

`KDS.read(file)` is a one-line alias for `KD.load(file, LoadOptions.SCHEMA_REQUIRED)`.

### Report format

`ValidationResult.report()` returns `valid`, or an issue count followed by
one entry per issue; `Document.report()` prefixes the schema name:

```
Assay: 6 issue(s)
  PATTERN             /assay[0]/attributes/id
      String does not fully match HTS-\d{4}
  VALUE_ABOVE_MAX     /assay[0]/temperature[0]/values[0]
      Expected <= 40°C; found 120°F  [expected 40°C, actual 120°F]
```

### Plain parsing

`KD.read` and `KD.readDocument` still parse without resolving anything. They
record a `.schema` directive on its tag, as they do for snips, and a quoted
string with the same text stays a string. A directive that reaches
`schema.validate` directly is reported as `MISPLACED_SCHEMA_DIRECTIVE`.
Schema vocabulary cannot carry directives.

### Deprecated

`KD.readWithSnips` and `KD.parseWithSnips` are superseded by `load`, which
does the same work and also applies a declared schema. They still work and
return the same tree as `KD.load(...).root`; they will be removed in a later
release.

New code: `MISPLACED_SCHEMA_DIRECTIVE`.
