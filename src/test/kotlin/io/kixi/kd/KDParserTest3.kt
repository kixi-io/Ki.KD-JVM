package io.kixi.kd

import io.kixi.*
import io.kixi.text.ParseException
import io.kixi.uom.Quantity
import io.kixi.uom.Unit as UOM
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.collections.shouldContainExactly
import java.math.BigDecimal as Dec
import java.time.Duration

/**
 * Comprehensive tests for KD Parser literal types Phase 3:
 * 1. Quantity<U> - numbers with units of measure
 * 2. Range<T> - inclusive/exclusive ranges
 * 3. List<T> - ordered collections (already implemented, testing edge cases)
 * 4. Map<K,V> - key-value pairs (already implemented, testing edge cases)
 * 5. Call - function call syntax
 * 6. nil - absence of value
 */
class KDParserTest3 : FunSpec({

    val parser = KDParser()

    // ========================================================================
    // Quantity Tests
    // ========================================================================

    context("Quantity parsing - Length units") {

        test("should parse simple length quantity") {
            val tag = parser.parse("length 5cm")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.cm
        }

        test("should parse length in meters") {
            val tag = parser.parse("distance 100m")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m
        }

        test("should parse length in kilometers") {
            val tag = parser.parse("distance 42km")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.km
        }

        test("should parse length in millimeters") {
            val tag = parser.parse("width 150mm")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.mm
        }

        test("should parse decimal length") {
            val tag = parser.parse("height 1.75m")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m
        }

        test("should parse length with underscores") {
            val tag = parser.parse("distance 1_000_000m")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }
    }

    context("Quantity parsing - Mass units") {

        test("should parse mass in kilograms") {
            val tag = parser.parse("weight 75kg")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.kg
        }

        test("should parse mass in grams") {
            val tag = parser.parse("weight 500g")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.g
        }

        test("should parse mass in milligrams") {
            val tag = parser.parse("dose 250mg")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.mg
        }
    }

    context("Quantity parsing - Area units") {

        test("should parse area in square meters using superscript") {
            val tag = parser.parse("area 100m²")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m2
        }

        test("should parse area in square meters using ASCII") {
            val tag = parser.parse("area 100m2")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m2
        }

        test("should parse area in square centimeters") {
            val tag = parser.parse("area 50cm2")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.cm2
        }
    }

    context("Quantity parsing - Volume units") {

        test("should parse volume in cubic meters") {
            val tag = parser.parse("volume 10m3")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m3
        }

        test("should parse volume in cubic meters with superscript") {
            val tag = parser.parse("volume 10m³")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m3
        }

        test("should parse volume in liters using special char") {
            val tag = parser.parse("volume 5ℓ")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.L
        }

        test("should parse volume in liters using LT") {
            val tag = parser.parse("volume 5LT")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.L
        }

        test("should parse volume in milliliters") {
            val tag = parser.parse("volume 250mℓ")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.mL
        }
    }

    context("Quantity parsing - Temperature units") {

        test("should parse temperature in Kelvin") {
            val tag = parser.parse("temp 300K")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.K
        }

        test("should parse temperature in Celsius") {
            val tag = parser.parse("temp 25°C")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.dC
        }

        test("should parse temperature in Celsius using dC") {
            val tag = parser.parse("temp 25dC")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.dC
        }
    }

    context("Quantity parsing - Type specifiers") {

        test("should parse quantity with Long type specifier") {
            val tag = parser.parse("length 100m:L")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.value shouldBe 100L
            q.value::class shouldBe Long::class
        }

        test("should parse quantity with Double type specifier") {
            val tag = parser.parse("length 3.14m:d")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.value shouldBe 3.14
            q.value::class shouldBe Double::class
        }

        test("should parse quantity with Float type specifier") {
            val tag = parser.parse("length 3.14m:f")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.value::class shouldBe Float::class
        }

        test("should parse quantity with Int type specifier") {
            val tag = parser.parse("length 100m:i")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.value shouldBe 100
            q.value::class shouldBe Int::class
        }

        test("default quantity type is BigDecimal") {
            val tag = parser.parse("length 5cm")
            val q = tag.value as Quantity<*>
            q.value::class shouldBe Dec::class
        }
    }

    context("Quantity parsing - Scientific notation") {

        test("should parse quantity with parentheses style positive exponent") {
            val tag = parser.parse("distance 5.5e(8)km")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.km
        }

        test("should parse quantity with parentheses style negative exponent") {
            val tag = parser.parse("length 5.5e(-7)m")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m
        }

        test("should parse quantity with letter style negative exponent") {
            val tag = parser.parse("length 5.5en7m")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.m
        }

        test("should parse quantity with letter style positive exponent") {
            val tag = parser.parse("distance 5.5ep8km")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.km
        }

        test("should parse quantity with implicit positive exponent") {
            val tag = parser.parse("distance 5.5e8km")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.km
        }
    }

    context("Quantity parsing - Negative values") {

        test("should parse negative quantity") {
            val tag = parser.parse("offset -15cm")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe UOM.cm
            q.isNegative shouldBe true
        }

        test("should parse negative temperature") {
            val tag = parser.parse("temp -40°C")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.isNegative shouldBe true
        }
    }

    context("Quantity in various contexts") {

        test("should parse quantity as attribute value") {
            val tag = parser.parse("box \"Package\" width=50cm height=30cm depth=20cm")
            tag.value shouldBe "Package"
            (tag["width"] as Quantity<*>).unit shouldBe UOM.cm
            (tag["height"] as Quantity<*>).unit shouldBe UOM.cm
            (tag["depth"] as Quantity<*>).unit shouldBe UOM.cm
        }

        test("should parse multiple quantities in values") {
            val tag = parser.parse("dimensions 100cm 50cm 25cm")
            tag.values.size shouldBe 3
            tag.values.all { it is Quantity<*> } shouldBe true
        }

        test("should parse quantity in list") {
            val tag = parser.parse("lengths [5cm, 10cm, 15cm]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            list.all { it is Quantity<*> } shouldBe true
        }

        test("should parse anonymous quantity tag") {
            val tag = parser.parse("5cm")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }
    }

    // ========================================================================
    // Range Tests
    // ========================================================================

    context("Range parsing - Inclusive ranges") {

        test("should parse inclusive integer range") {
            val tag = parser.parse("range 1..5")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.Inclusive
            range.left shouldBe 1
            range.right shouldBe 5
        }

        test("should parse inclusive negative range") {
            val tag = parser.parse("range -10..10")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.left shouldBe -10
            range.right shouldBe 10
        }

        test("should parse inclusive decimal range") {
            val tag = parser.parse("range 1.5..5.5")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.Inclusive
        }

        test("should parse inclusive Long range") {
            val tag = parser.parse("range 1L..100L")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.left shouldBe 1L
            range.right shouldBe 100L
        }

        test("should parse reversed range") {
            val tag = parser.parse("countdown 10..1")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.reversed shouldBe true
        }
    }

    context("Range parsing - Exclusive ranges") {

        test("should parse exclusive both sides") {
            val tag = parser.parse("range 1<..<10")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.Exclusive
        }

        test("should parse exclusive left") {
            val tag = parser.parse("range 1<..10")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.ExclusiveLeft
        }

        test("should parse exclusive right") {
            val tag = parser.parse("range 1..<10")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.ExclusiveRight
        }
    }

    context("Range parsing - Open ranges") {

        test("should parse open right range") {
            val tag = parser.parse("minimum 0.._")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.openRight shouldBe true
            range.openLeft shouldBe false
        }

        test("should parse open left range") {
            val tag = parser.parse("maximum _..100")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.openLeft shouldBe true
            range.openRight shouldBe false
        }

        test("should parse open right with exclusive left") {
            val tag = parser.parse("range 5<.._")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.ExclusiveLeft
            range.openRight shouldBe true
        }

        test("should parse open left with exclusive right") {
            val tag = parser.parse("range _..<100")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.ExclusiveRight
            range.openLeft shouldBe true
        }
    }

    context("Range parsing - Char ranges") {

        test("should parse char range") {
            val tag = parser.parse("letters 'a'..'z'")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.left shouldBe 'a'
            range.right shouldBe 'z'
        }

        test("should parse char range exclusive") {
            val tag = parser.parse("letters 'a'<..<'z'")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.Exclusive
        }
    }

    context("Range parsing - Version ranges") {

        test("should parse version range") {
            val tag = parser.parse("supported 1.0.0..2.0.0")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.left.shouldBeInstanceOf<Version>()
            range.right.shouldBeInstanceOf<Version>()
        }

        test("should parse version range with qualifier") {
            val tag = parser.parse("supported 1.0.0-alpha..2.0.0-beta")
            tag.value.shouldBeInstanceOf<Range<*>>()
        }

        test("should parse open version range") {
            val tag = parser.parse("minimum 1.0.0.._")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.openRight shouldBe true
        }
    }

    context("Range parsing - Duration ranges") {

        test("should parse duration range") {
            val tag = parser.parse("timeout 1h..10h")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.left.shouldBeInstanceOf<Duration>()
            range.right.shouldBeInstanceOf<Duration>()
        }

        test("should parse duration range exclusive") {
            val tag = parser.parse("window 5min<..<30min")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.type shouldBe Range.Type.Exclusive
        }
    }

    context("Range parsing - Quantity ranges") {

        test("should parse quantity range") {
            val tag = parser.parse("tolerance 5mm..10mm")
            tag.value.shouldBeInstanceOf<Range<*>>()
            val range = tag.value as Range<*>
            range.left.shouldBeInstanceOf<Quantity<*>>()
            range.right.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse quantity range with different units") {
            val tag = parser.parse("range 7mm..12cm")
            tag.value.shouldBeInstanceOf<Range<*>>()
        }
    }

    context("Range in various contexts") {

        test("should parse range as attribute value") {
            val tag = parser.parse("config \"settings\" range=1..100")
            tag.value shouldBe "settings"
            tag["range"].shouldBeInstanceOf<Range<*>>()
        }

        test("should parse range in list") {
            val tag = parser.parse("ranges [1..10, 20..30, 50..100]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            list.all { it is Range<*> } shouldBe true
        }

        test("should parse anonymous range tag") {
            val tag = parser.parse("1..100")
            tag.value.shouldBeInstanceOf<Range<*>>()
        }
    }

    // ========================================================================
    // List Tests (Edge Cases)
    // ========================================================================

    context("List parsing") {

        test("should parse empty list") {
            val tag = parser.parse("items []")
            val list = tag.value as List<*>
            list.size shouldBe 0
        }

        test("should parse list with commas") {
            val tag = parser.parse("nums [1, 2, 3, 4, 5]")
            val list = tag.value as List<*>
            list.shouldContainExactly(1, 2, 3, 4, 5)
        }

        test("should parse list without commas") {
            val tag = parser.parse("nums [1 2 3 4 5]")
            val list = tag.value as List<*>
            list.shouldContainExactly(1, 2, 3, 4, 5)
        }

        test("should parse list with mixed separators") {
            val tag = parser.parse("nums [1, 2 3, 4 5]")
            val list = tag.value as List<*>
            list.shouldContainExactly(1, 2, 3, 4, 5)
        }

        test("should parse nested lists") {
            val tag = parser.parse("matrix [[1, 2], [3, 4], [5, 6]]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            (list[0] as List<*>).shouldContainExactly(1, 2)
        }

        test("should parse list with mixed types") {
            val tag = parser.parse("items [1, \"hello\", true, 3.14]")
            val list = tag.value as List<*>
            list.size shouldBe 4
            list[0] shouldBe 1
            list[1] shouldBe "hello"
            list[2] shouldBe true
            list[3] shouldBe 3.14
        }

        test("should parse list with nil") {
            val tag = parser.parse("items [1, nil, 3]")
            val list = tag.value as List<*>
            list.shouldContainExactly(1, null, 3)
        }

        test("should parse list with ranges") {
            val tag = parser.parse("ranges [1..5, 10..20]")
            val list = tag.value as List<*>
            list.size shouldBe 2
            list.all { it is Range<*> } shouldBe true
        }

        test("should parse list with quantities") {
            val tag = parser.parse("lengths [5cm, 10m, 1km]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            list.all { it is Quantity<*> } shouldBe true
        }
    }

    // ========================================================================
    // Map Tests (Edge Cases)
    // ========================================================================

    context("Map parsing") {

        test("should parse empty map") {
            // Empty brackets are parsed as empty list, not map
            val tag = parser.parse("items []")
            tag.value shouldBe emptyList<Any>()
        }

        test("should parse simple map with string keys") {
            val tag = parser.parse("person [name=\"John\", age=30]")
            val map = tag.value as Map<*, *>
            map["name"] shouldBe "John"
            map["age"] shouldBe 30
        }

        test("should parse map without commas") {
            val tag = parser.parse("person [name=\"John\" age=30]")
            val map = tag.value as Map<*, *>
            map["name"] shouldBe "John"
            map["age"] shouldBe 30
        }

        test("should parse map with char keys") {
            val tag = parser.parse("grades ['a'=1, 'b'=2, 'c'=3]")
            val map = tag.value as Map<*, *>
            map['a'] shouldBe 1
            map['b'] shouldBe 2
            map['c'] shouldBe 3
        }

        test("should parse map with integer keys") {
            val tag = parser.parse("lookup [1=\"one\", 2=\"two\", 3=\"three\"]")
            val map = tag.value as Map<*, *>
            map[1] shouldBe "one"
            map[2] shouldBe "two"
        }

        test("should parse map with mixed value types") {
            val tag = parser.parse("config [debug=true, timeout=30, name=\"test\"]")
            val map = tag.value as Map<*, *>
            map["debug"] shouldBe true
            map["timeout"] shouldBe 30
            map["name"] shouldBe "test"
        }

        test("should parse map with nil values") {
            val tag = parser.parse("data [a=1, b=nil, c=3]")
            val map = tag.value as Map<*, *>
            map["a"] shouldBe 1
            map["b"] shouldBe null
            map["c"] shouldBe 3
        }

        test("should parse map with quantity values") {
            val tag = parser.parse("dimensions [width=100cm, height=50cm]")
            val map = tag.value as Map<*, *>
            (map["width"] as Quantity<*>).unit shouldBe UOM.cm
            (map["height"] as Quantity<*>).unit shouldBe UOM.cm
        }

        test("should parse nested map") {
            val tag = parser.parse("config [db=[host=\"localhost\", port=5432]]")
            val map = tag.value as Map<*, *>
            val db = map["db"] as Map<*, *>
            db["host"] shouldBe "localhost"
            db["port"] shouldBe 5432
        }
    }

    // ========================================================================
    // Call Tests
    // ========================================================================

    context("Call parsing - Basic") {

        test("should parse call with no arguments") {
            val tag = parser.parse("action myFunc()")
            tag.value.shouldBeInstanceOf<Call>()
            val call = tag.value as Call
            call.nsid.name shouldBe "myFunc"
            call.hasValues() shouldBe false
            call.hasAttributes() shouldBe false
        }

        test("should parse call with positional arguments") {
            val tag = parser.parse("color rgb(255, 128, 0)")
            tag.value.shouldBeInstanceOf<Call>()
            val call = tag.value as Call
            call.nsid.name shouldBe "rgb"
            call.values.size shouldBe 3
            call.values.shouldContainExactly(255, 128, 0)
        }

        test("should parse call without commas") {
            val tag = parser.parse("color rgb(255 128 0)")
            tag.value.shouldBeInstanceOf<Call>()
            val call = tag.value as Call
            call.values.shouldContainExactly(255, 128, 0)
        }

        test("should parse call with named arguments") {
            val tag = parser.parse("font style(family=\"Arial\", size=12)")
            tag.value.shouldBeInstanceOf<Call>()
            val call = tag.value as Call
            call["family"] shouldBe "Arial"
            call["size"] shouldBe 12
        }

        test("should parse call with mixed arguments") {
            val tag = parser.parse("color rgba(255, 128, 0, alpha=0.5)")
            tag.value.shouldBeInstanceOf<Call>()
            val call = tag.value as Call
            call.values.size shouldBe 3
            call["alpha"] shouldBe 0.5
        }
    }

    context("Call parsing - Complex arguments") {

        test("should parse call with string arguments") {
            val tag = parser.parse("log print(\"Hello\", \"World\")")
            val call = tag.value as Call
            call.values.shouldContainExactly("Hello", "World")
        }

        test("should parse call with boolean arguments") {
            val tag = parser.parse("config options(debug=true, verbose=false)")
            val call = tag.value as Call
            call["debug"] shouldBe true
            call["verbose"] shouldBe false
        }

        test("should parse call with nil argument") {
            val tag = parser.parse("action reset(value=nil)")
            val call = tag.value as Call
            call["value"] shouldBe null
        }

        test("should parse call with list argument") {
            val tag = parser.parse("action process([1, 2, 3])")
            val call = tag.value as Call
            val list = call.values[0] as List<*>
            list.shouldContainExactly(1, 2, 3)
        }

        test("should parse call with quantity arguments") {
            val tag = parser.parse("draw rect(width=100cm, height=50cm)")
            val call = tag.value as Call
            (call["width"] as Quantity<*>).unit shouldBe UOM.cm
            (call["height"] as Quantity<*>).unit shouldBe UOM.cm
        }

        test("should parse call with range argument") {
            val tag = parser.parse("filter range(1..100)")
            val call = tag.value as Call
            call.values[0].shouldBeInstanceOf<Range<*>>()
        }
    }

    context("Call parsing - Namespaced") {

        test("should parse namespaced call") {
            val tag = parser.parse("action math:sin(3.14)")
            val call = tag.value as Call
            call.nsid.namespace shouldBe "math"
            call.nsid.name shouldBe "sin"
        }
    }

    context("Call in various contexts") {

        test("should parse call as attribute value") {
            val tag = parser.parse("element \"div\" style=css(color=\"red\")")
            tag.value shouldBe "div"
            tag["style"].shouldBeInstanceOf<Call>()
        }

        test("should parse multiple calls in values") {
            val tag = parser.parse("colors rgb(255, 0, 0) rgb(0, 255, 0) rgb(0, 0, 255)")
            tag.values.size shouldBe 3
            tag.values.all { it is Call } shouldBe true
        }

        test("should parse call in list") {
            val tag = parser.parse("funcs [add(1, 2), sub(5, 3)]")
            val list = tag.value as List<*>
            list.size shouldBe 2
            list.all { it is Call } shouldBe true
        }

        test("should parse nested calls") {
            val tag = parser.parse("expr outer(inner(1, 2), 3)")
            val outerCall = tag.value as Call
            outerCall.values.size shouldBe 2
            val innerCall = outerCall.values[0] as Call
            innerCall.nsid.name shouldBe "inner"
        }

        test("should parse anonymous call tag") {
            val tag = parser.parse("func(1, 2, 3)")
            tag.value.shouldBeInstanceOf<Call>()
        }
    }

    // ========================================================================
    // nil Tests
    // ========================================================================

    context("nil parsing") {

        test("should parse nil keyword") {
            val tag = parser.parse("value nil")
            tag.value shouldBe null
        }

        test("should parse null keyword") {
            val tag = parser.parse("value null")
            tag.value shouldBe null
        }

        test("should parse nil as attribute value") {
            val tag = parser.parse("item \"test\" data=nil")
            tag["data"] shouldBe null
        }

        test("should parse nil in list") {
            val tag = parser.parse("items [1, nil, 3]")
            val list = tag.value as List<*>
            list[1] shouldBe null
        }

        test("should parse nil in map") {
            val tag = parser.parse("data [a=1, b=nil]")
            val map = tag.value as Map<*, *>
            map["b"] shouldBe null
        }

        test("should parse anonymous nil tag") {
            val tag = parser.parse("nil")
            tag.value shouldBe null
        }

        test("should parse standalone nil creates anonymous tag") {
            val tag = parser.parse("nil")
            tag.nsid shouldBe NSID.ANONYMOUS
            tag.value shouldBe null
        }
    }

    // ========================================================================
    // Integration Tests - Mixed Types
    // ========================================================================

    context("Integration - complex combinations") {

        test("should parse tag with all Phase 3 types") {
            val tag = parser.parse("""
                config "app" 
                    sizes=[10cm, 20cm, 30cm]
                    range=1..100
                    init=setup(debug=true)
                    optional=nil
            """.trimIndent())

            tag.value shouldBe "app"
            (tag["sizes"] as List<*>).all { it is Quantity<*> } shouldBe true
            tag["range"].shouldBeInstanceOf<Range<*>>()
            tag["init"].shouldBeInstanceOf<Call>()
            tag["optional"] shouldBe null
        }

        test("should parse nested tags with Phase 3 types") {
            val tag = parser.parse("""
                graphics {
                    shape rect(width=100cm, height=50cm)
                    colors [rgb(255, 0, 0), rgb(0, 255, 0)]
                    opacity 0.0..1.0
                }
            """.trimIndent())

            val shape = tag.getChild("shape")
            shape?.value.shouldBeInstanceOf<Call>()

            val colors = tag.getChild("colors")
            (colors?.value as List<*>).all { it is Call } shouldBe true

            val opacity = tag.getChild("opacity")
            opacity?.value.shouldBeInstanceOf<Range<*>>()
        }

        test("should parse range of quantities") {
            val tag = parser.parse("tolerance 1mm..5mm")
            val range = tag.value as Range<*>
            (range.left as Quantity<*>).unit shouldBe UOM.mm
            (range.right as Quantity<*>).unit shouldBe UOM.mm
        }

        test("should parse call with range argument") {
            val tag = parser.parse("filter between(1..100)")
            val call = tag.value as Call
            call.values[0].shouldBeInstanceOf<Range<*>>()
        }

        test("should parse list of ranges") {
            val tag = parser.parse("buckets [1..10, 11..20, 21..30]")
            val list = tag.value as List<*>
            list.all { it is Range<*> } shouldBe true
        }

        test("should parse map with call values") {
            val tag = parser.parse("handlers [onClick=handle(\"click\"), onLoad=init()]")
            val map = tag.value as Map<*, *>
            (map["onClick"] as Call).nsid.name shouldBe "handle"
            (map["onLoad"] as Call).nsid.name shouldBe "init"
        }
    }

    // ========================================================================
    // Error Handling Tests
    // ========================================================================

    context("Error handling") {

        test("should fail on invalid range operator combination for open left") {
            shouldThrow<ParseException> {
                parser.parse("range _<..100")  // Invalid: <.. with open left
            }
        }

        test("should fail on invalid range operator combination for open right") {
            shouldThrow<ParseException> {
                parser.parse("range 1..<_")  // Invalid: ..< with open right
            }
        }

        test("should fail on unterminated call") {
            shouldThrow<ParseException> {
                parser.parse("action func(1, 2, 3")
            }
        }

        test("should fail on unknown unit") {
            // This should NOT parse as a Quantity, but as identifier + identifier
            val tag = parser.parse("value 5xyz")
            // 5 is a number, xyz is separate
            tag.value shouldBe 5
        }
    }

    // ========================================================================
    // Spec Examples
    // ========================================================================

    context("Examples from KD specification") {

        test("should parse quantity examples from spec") {
            parser.parse("q 23cm").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("q 51.4m3").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("q 97LT").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("q 1000kg").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("q 1_000kg").value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse range examples from spec") {
            // 1..5 - inclusive
            var range = parser.parse("r 1..5").value as Range<*>
            range.type shouldBe Range.Type.Inclusive

            // 5.0<..<15.0 - exclusive
            range = parser.parse("r 5.0<..<15.0").value as Range<*>
            range.type shouldBe Range.Type.Exclusive

            // 2<..17 - exclusive left
            range = parser.parse("r 2<..17").value as Range<*>
            range.type shouldBe Range.Type.ExclusiveLeft

            // 6..<12 - exclusive right
            range = parser.parse("r 6..<12").value as Range<*>
            range.type shouldBe Range.Type.ExclusiveRight

            // 6.._ - open right
            range = parser.parse("r 6.._").value as Range<*>
            range.openRight shouldBe true

            // _..100 - open left
            range = parser.parse("r _..100").value as Range<*>
            range.openLeft shouldBe true
        }

        test("should parse call examples from spec") {
            // rgb(200, 100, 120)
            var call = parser.parse("c rgb(200, 100, 120)").value as Call
            call.values.shouldContainExactly(200, 100, 120)

            // rgb(200 100 120) - commas optional
            call = parser.parse("c rgb(200 100 120)").value as Call
            call.values.shouldContainExactly(200, 100, 120)

            // rgb(200, 100, 120, alpha=.5) - mixed positional and named
            call = parser.parse("c rgb(200, 100, 120, alpha=0.5)").value as Call
            call.values.size shouldBe 3
            call["alpha"] shouldBe 0.5
        }

        test("should parse list examples from spec") {
            // [1, 2, 3, 4]
            var list = parser.parse("l [1, 2, 3, 4]").value as List<*>
            list.shouldContainExactly(1, 2, 3, 4)

            // [5 6 7 8] - commas optional
            list = parser.parse("l [5 6 7 8]").value as List<*>
            list.shouldContainExactly(5, 6, 7, 8)

            // ['a' 'b' 'c']
            list = parser.parse("l ['a' 'b' 'c']").value as List<*>
            list.shouldContainExactly('a', 'b', 'c')

            // [[1 2] [3 4]] - nested
            list = parser.parse("l [[1 2] [3 4]]").value as List<*>
            (list[0] as List<*>).shouldContainExactly(1, 2)
            (list[1] as List<*>).shouldContainExactly(3, 4)
        }

        test("should parse map examples from spec") {
            // [Spanish="hola", Fijian="Bula"]
            var map = parser.parse("m [Spanish=\"hola\", Fijian=\"Bula\"]").value as Map<*, *>
            map["Spanish"] shouldBe "hola"
            map["Fijian"] shouldBe "Bula"

            // ['a'="Ant", 'b'="Bird"]
            map = parser.parse("m ['a'=\"Ant\", 'b'=\"Bird\"]").value as Map<*, *>
            map['a'] shouldBe "Ant"
            map['b'] shouldBe "Bird"
        }
    }
})