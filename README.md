# Ki.KD-JVM

A JVM implementation of KD (Ki Data), the declarative data language of the
Ki family, with KDS (KD Schema) for describing and validating documents.
Published as `io.kixi:Ki.KD-JVM`. Depends on `io.kixi:Ki.Core-JVM`.

KD is a tag-based format with typed literals (numbers, strings, dates,
durations, versions, quantities with units, currencies, grids, ranges,
lists, maps) and a tree of tags with values, attributes, annotations and
children:

```kd
assay id="HTS-0042" operator="dan" {
    temperature 37°C
    measurement 0.82 well=A1
    measurement 0.91 well=A2
}
```

## Reading a file

`KD.load` is the one call for files. It parses, resolves `.snip(path)`
includes relative to the file, and if the document declares a schema,
validates the data against it. Documents without a schema load as plain
data; schemas are optional but recommended.

```kotlin
// import io.kixi.kd.KD

val doc = KD.load(File("assay.kd"))
val assay = doc.root
println(assay["id"])

if (!doc.isValid) println(doc.report())   // or doc.requireValid() to throw
```

`Document` gives you `root` (the single top-level tag, or a synthetic
`root` when there are several), `tags`, `hasSchema`, `schema`, `isValid`,
`issues`, `requireValid()` and `report()`.

To parse text without touching the file system, use `KD.read(text)` for a
tag or `KD.readDocument(text)` for the top-level tag list. `KD("2024/3/15")`
parses a single literal.

## Declaring a schema

Put the declaration first. Either name an external schema file:

```kd
.schema(assay)

assay id="HTS-0042" operator="dan" { ... }
```

or write the schema inline, followed by the data:

```kd
schema Assay kds=1 {
    tag assay {
        attribute id type=String required=true pattern=@"HTS-\d{4}"
        children {
            tag measurement occurs=1.._ {
                attribute well type=String required=true pattern=@"[A-P]\d{1,2}"
                value type=Number min=0
            }
        }
    }
}

assay id="HTS-0042" {
    measurement 0.82 well=A1
}
```

See `docs/KDS.md` for the schema language and `docs/SimpleIntegration.md`
for snips.

## Validating without a declaration

Compile a schema once and validate any number of documents:

```kotlin
// import io.kixi.kd.schema.KDS

val schema = KDS.compile(File("assay.kds"))
val result = schema.validate(KD.readDocument(text))
result.requireValid()
```

## Deprecated API

`KD.readWithSnips` and `KD.parseWithSnips` are deprecated in favor of
`KD.load`, which resolves snips the same way and also applies a declared
schema. They keep working for now (`KD.load(file).root` returns the same
tree) and will be removed in a future release. `KD.read` and
`KD.readDocument` are unchanged.

## Building

```
./gradlew test
./gradlew build
```

Ki.KS-JVM consumes this project through a Gradle composite build
(`includeBuild("../Ki.KD-JVM")`), so a local checkout is used in place of
the published artifact when both are present.
