package io.kixi.kd

import io.kixi.*
import io.kixi.uom.Quantity
import io.kixi.uom.Unit as UOM
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.string.shouldContain
import java.math.BigDecimal as Dec
import java.net.URL
import java.time.*

/**
 * Comprehensive KD Parser Tests using large KD files.
 *
 * These tests exercise the parser with real-world complex KD documents
 * containing all literal types, deep nesting, annotations, unicode, emoji,
 * and various formatting options.
 *
 * 大規模なKDファイルを使用した包括的なKDパーサーテスト 🎯
 */
class KDLargeFileTest : FunSpec({

    // ========================================================================
    // 📁 File Loading Tests
    // ========================================================================

    context("Loading and parsing KDTest.kd - Main comprehensive test file") {

        val root = KD.readResource("KDTest.kd")

        test("should parse without errors") {
            root shouldNotBe null
            root.nsid.name shouldBe "root"
        }

        test("should have multiple top-level tags") {
            root.children.shouldNotBeEmpty()
            root.children.size shouldBeGreaterThan 10
        }

        test("should have metadata tag with annotations") {
            val metadata = root.getChild("metadata")
            metadata shouldNotBe null
            metadata!!.annotations.shouldNotBeEmpty()

            // Check for version annotation
            val versionAnno = metadata.annotations.find { it.nsid.name == "version" }
            versionAnno shouldNotBe null
            versionAnno!!.value.shouldBeInstanceOf<Version>()
        }

        test("should parse strings section with all string types") {
            val strings = root.getChild("strings")
            strings shouldNotBe null

            // Simple strings
            val simpleBasic = strings!!.getChild("simple_basic")
            simpleBasic shouldNotBe null
            simpleBasic!!.value shouldBe "Hello, World! 👋"

            // Unicode strings
            val simpleUnicode = strings.getChild("simple_unicode")
            simpleUnicode shouldNotBe null
            (simpleUnicode!!.value as String) shouldContain "日本語"

            // Raw strings
            val rawPath = strings.getChild("raw_path")
            rawPath shouldNotBe null
            (rawPath!!.value as String) shouldContain "C:\\Users"

            // Block strings
            val poem = strings.getChild("poem")
            poem shouldNotBe null
            (poem!!.value as String) shouldContain "古池や"
            (poem.value as String) shouldContain "松尾芭蕉"

            // Backtick strings
            val inlineCode = strings.getChild("inline_code")
            inlineCode shouldNotBe null
        }

        test("should parse characters section") {
            val characters = root.getChild("characters")
            characters shouldNotBe null

            val letter = characters!!.getChild("letter")
            letter shouldNotBe null
            letter!!.value shouldBe 'A'

            val japanese = characters.getChild("japanese")
            japanese shouldNotBe null
            japanese!!.value shouldBe '桜'

            val emoji = characters.getChild("emoji")
            emoji shouldNotBe null
            // Using BMP emoji ☺ (U+263A) which fits in single Char
            emoji!!.value shouldBe '☺'
        }

        test("should parse numbers section with all numeric types") {
            val numbers = root.getChild("numbers")
            numbers shouldNotBe null

            // Integers
            val integers = numbers!!.getChild("integers")
            integers shouldNotBe null

            val zero = integers!!.getChild("zero")
            zero!!.value shouldBe 0

            val hexVal = integers.getChild("hex")
            hexVal!!.value shouldBe 255

            // Longs
            val longs = numbers.getChild("longs")
            longs shouldNotBe null
            val basicLong = longs!!.getChild("basic")
            basicLong!!.value shouldBe 123L
            basicLong.value.shouldBeInstanceOf<Long>()

            // Floats
            val floats = numbers.getChild("floats")
            floats shouldNotBe null
            val basicFloat = floats!!.getChild("basic")
            basicFloat!!.value shouldBe 3.14f
            basicFloat.value.shouldBeInstanceOf<Float>()

            // Doubles
            val doubles = numbers.getChild("doubles")
            doubles shouldNotBe null
            val implicitDouble = doubles!!.getChild("implicit")
            implicitDouble!!.value.shouldBeInstanceOf<Double>()

            val infinity = doubles.getChild("infinity")
            infinity!!.value shouldBe Double.POSITIVE_INFINITY

            // BigDecimals
            val decimals = numbers.getChild("decimals")
            decimals shouldNotBe null
            val basicDec = decimals!!.getChild("basic")
            basicDec!!.value.shouldBeInstanceOf<Dec>()
        }

        test("should parse booleans section with all variants") {
            val booleans = root.getChild("booleans")
            booleans shouldNotBe null

            val stdTrue = booleans!!.getChild("standard_true")
            stdTrue!!.value shouldBe true

            val stdFalse = booleans.getChild("standard_false")
            stdFalse!!.value shouldBe false

            val sdlOn = booleans.getChild("sdl_on")
            sdlOn!!.value shouldBe true

            val sdlOff = booleans.getChild("sdl_off")
            sdlOff!!.value shouldBe false
        }

        test("should parse URLs section") {
            val urls = root.getChild("urls")
            urls shouldNotBe null

            val httpsUrl = urls!!.getChild("https_url")
            httpsUrl shouldNotBe null
            httpsUrl!!.value.shouldBeInstanceOf<URL>()
            (httpsUrl.value as URL).protocol shouldBe "https"

            val nakedHttps = urls.getChild("naked_https")
            nakedHttps shouldNotBe null
            nakedHttps!!.value.shouldBeInstanceOf<URL>()
        }

        test("should parse emails section") {
            val emails = root.getChild("emails")
            emails shouldNotBe null

            val basic = emails!!.getChild("basic")
            basic shouldNotBe null
            basic!!.value.shouldBeInstanceOf<Email>()
            (basic.value as Email).domain shouldBe "leuck.org"

            val japanese = emails.getChild("japanese")
            japanese shouldNotBe null
            (japanese!!.value as Email).domain shouldBe "example.co.jp"
        }

        test("should parse datetime section") {
            val datetime = root.getChild("datetime")
            datetime shouldNotBe null

            // Dates
            val dates = datetime!!.getChild("dates")
            dates shouldNotBe null
            val today = dates!!.getChild("today")
            today!!.value.shouldBeInstanceOf<LocalDate>()
            (today.value as LocalDate).year shouldBe 2024

            // LocalDateTime
            val localTimes = datetime.getChild("local_times")
            localTimes shouldNotBe null
            val basic = localTimes!!.getChild("basic")
            basic!!.value.shouldBeInstanceOf<LocalDateTime>()

            // KiTZDateTime
            val kitzTimes = datetime.getChild("kitz_times")
            kitzTimes shouldNotBe null
            val tokyo = kitzTimes!!.getChild("tokyo")
            tokyo!!.value.shouldBeInstanceOf<KiTZDateTime>()
            (tokyo.value as KiTZDateTime).kiTZ shouldBe KiTZ.JP_JST
        }

        test("should parse durations section") {
            val durations = root.getChild("durations")
            durations shouldNotBe null

            val singleUnit = durations!!.getChild("single_unit")
            singleUnit shouldNotBe null

            val daysPlural = singleUnit!!.getChild("days_plural")
            daysPlural!!.value.shouldBeInstanceOf<Duration>()
            (daysPlural.value as Duration).toDays() shouldBe 5

            val compound = durations.getChild("compound")
            compound shouldNotBe null
            val basicCompound = compound!!.getChild("basic")
            basicCompound!!.value.shouldBeInstanceOf<Duration>()
        }

        test("should parse versions section") {
            val versions = root.getChild("versions")
            versions shouldNotBe null

            val majorOnly = versions!!.getChild("major_only")
            majorOnly!!.value.shouldBeInstanceOf<Version>()
            (majorOnly.value as Version).major shouldBe 5

            val withQualifier = versions.getChild("alpha")
            withQualifier!!.value.shouldBeInstanceOf<Version>()
            (withQualifier.value as Version).qualifier shouldBe "alpha"
        }

        test("should parse blobs section") {
            val blobs = root.getChild("blobs")
            blobs shouldNotBe null

            val greeting = blobs!!.getChild("greeting")
            greeting!!.value.shouldBeInstanceOf<Blob>()
            (greeting.value as Blob).decodeToString() shouldBe "Hello World!"

            val empty = blobs.getChild("empty")
            empty!!.value.shouldBeInstanceOf<Blob>()
            (empty.value as Blob).isEmpty() shouldBe true
        }

        test("should parse geopoints section") {
            val geopoints = root.getChild("geopoints")
            geopoints shouldNotBe null

            val tokyo = geopoints!!.getChild("tokyo")
            tokyo!!.value.shouldBeInstanceOf<GeoPoint>()

            val mtFuji = geopoints.getChild("mount_fuji")
            mtFuji!!.value.shouldBeInstanceOf<GeoPoint>()
            val fujiGeo = mtFuji.value as GeoPoint
            fujiGeo.alt!! shouldBeGreaterThan 3000.0
        }

        test("should parse quantities section") {
            val quantities = root.getChild("quantities")
            quantities shouldNotBe null

            val lengths = quantities!!.getChild("lengths")
            lengths shouldNotBe null

            val cm = lengths!!.getChild("centimeters")
            cm!!.value.shouldBeInstanceOf<Quantity<*>>()
            (cm.value as Quantity<*>).unit shouldBe UOM.cm

            val temperature = quantities.getChild("temperature")
            temperature shouldNotBe null
            val celsius = temperature!!.getChild("celsius")
            celsius!!.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse ranges section") {
            val ranges = root.getChild("ranges")
            ranges shouldNotBe null

            val inclusive = ranges!!.getChild("inclusive")
            inclusive shouldNotBe null

            val integers = inclusive!!.getChild("integers")
            integers!!.value.shouldBeInstanceOf<Range<*>>()
            val range = integers.value as Range<*>
            range.type shouldBe Range.Type.Inclusive
            range.left shouldBe 1
            range.right shouldBe 10

            val exclusive = ranges.getChild("exclusive")
            exclusive shouldNotBe null
            val bothSides = exclusive!!.getChild("both_sides")
            bothSides!!.value.shouldBeInstanceOf<Range<*>>()
            (bothSides.value as Range<*>).type shouldBe Range.Type.Exclusive

            val open = ranges.getChild("open")
            open shouldNotBe null
            val rightOpen = open!!.getChild("right_open")
            rightOpen!!.value.shouldBeInstanceOf<Range<*>>()
            (rightOpen.value as Range<*>).openRight shouldBe true
        }

        test("should parse lists section") {
            val lists = root.getChild("lists")
            lists shouldNotBe null

            val empty = lists!!.getChild("empty")
            empty!!.value shouldBe emptyList<Any>()

            val withCommas = lists.getChild("with_commas")
            withCommas!!.value shouldBe listOf(1, 2, 3, 4, 5)

            val matrix = lists.getChild("matrix")
            val matrixVal = matrix!!.value as List<*>
            matrixVal shouldHaveSize 3
            (matrixVal[0] as List<*>) shouldHaveSize 3
        }

        test("should parse maps section") {
            val maps = root.getChild("maps")
            maps shouldNotBe null

            val stringKeys = maps!!.getChild("string_keys")
            val map = stringKeys!!.value as Map<*, *>
            map["name"] shouldBe "太郎"
            map["age"] shouldBe 25
        }

        test("should parse calls section") {
            val calls = root.getChild("calls")
            calls shouldNotBe null

            val emptyCall = calls!!.getChild("empty_call")
            emptyCall!!.value.shouldBeInstanceOf<Call>()
            (emptyCall.value as Call).nsid.name shouldBe "myFunc"

            val positional = calls.getChild("positional")
            val rgb = positional!!.value as Call
            rgb.nsid.name shouldBe "rgb"
            rgb.values shouldHaveSize 3
            rgb.values[0] shouldBe 255

            val namespaced = calls.getChild("namespaced")
            val math = namespaced!!.value as Call
            math.nsid.namespace shouldBe "math"
            math.nsid.name shouldBe "sin"
        }

        test("should parse nils section") {
            val nils = root.getChild("nils")
            nils shouldNotBe null

            val nilKeyword = nils!!.getChild("nil_keyword")
            nilKeyword!!.value shouldBe null

            val nullKeyword = nils.getChild("null_keyword")
            nullKeyword!!.value shouldBe null
        }

        test("should parse annotations section with complex annotations") {
            val annotations = root.getChild("annotations")
            annotations shouldNotBe null

            val simpleTest = annotations!!.getChild("simple_test")
            simpleTest shouldNotBe null
            simpleTest!!.annotations shouldHaveSize 1
            simpleTest.annotations[0].nsid.name shouldBe "Test"

            val legacyFeature = annotations.getChild("legacy_feature")
            legacyFeature shouldNotBe null
            legacyFeature!!.annotations.size shouldBeGreaterThan 1
        }

        test("should parse complex nested structure - config") {
            val config = root.getChild("config")
            config shouldNotBe null
            config!!.annotations.shouldNotBeEmpty()

            val database = config.getChild("database")
            database shouldNotBe null
            // database children are tags with values, not attributes
            database!!.getChild("host")!!.value shouldBe "db.example.com"
            database.getChild("port")!!.value shouldBe 5432
            database.getChild("ssl")!!.value shouldBe true

            val logging = config.getChild("logging")
            logging shouldNotBe null
            logging!!.getChild("file")!!.value.shouldBeInstanceOf<URL>()
        }

        test("should parse character structure with nested tags") {
            val character = root.getChild("character")
            character shouldNotBe null
            character!!.value shouldBe "勇者タロウ"

            val stats = character.getChild("stats")
            stats shouldNotBe null
            val health = stats!!.getChild("health")
            health!!.value shouldBe 100

            val equipment = character.getChild("equipment")
            equipment shouldNotBe null
            val weapon = equipment!!.getChild("weapon")
            weapon shouldNotBe null
            weapon!!.value shouldBe "伝説の剣"
            weapon["type"] shouldBe "slashing"
        }

        test("should parse recipe book structure") {
            val recipes = root.getChild("recipes")
            recipes shouldNotBe null
            recipes!!.annotations.shouldNotBeEmpty()

            val recipeChildren = recipes.getChildren("recipe")
            recipeChildren.size shouldBeGreaterThan 0
        }

        test("should parse matrices") {
            val matrices = root.getChild("matrices")
            matrices shouldNotBe null

            val intMatrix = matrices!!.getChild("int_matrix")
            intMatrix shouldNotBe null
            intMatrix!!.children shouldHaveSize 3

            val rows = intMatrix.getChildrenValues<Int>()
            rows shouldHaveSize 3
            rows[0][0] shouldBe 1
            rows[1][1] shouldBe 5
            rows[2][2] shouldBe 9
        }

        test("should parse edge cases") {
            val edgeCases = root.getChild("edge_cases")
            edgeCases shouldNotBe null

            val emptyString = edgeCases!!.getChild("empty_string")
            emptyString!!.value shouldBe ""

            val specialNumbers = edgeCases.getChild("special_numbers")
            specialNumbers shouldNotBe null
            val posInf = specialNumbers!!.getChild("positive_infinity")
            posInf!!.value shouldBe Double.POSITIVE_INFINITY

            val deep = edgeCases.getChild("deep")
            deep shouldNotBe null
            // Navigate the deep structure: deep -> level1 -> level2 -> level3 -> level4 -> level5 -> value
            var current: Tag? = deep
            repeat(6) {
                current = current?.children?.firstOrNull()
            }
            current shouldNotBe null
            val deepValue = current!!
            deepValue.nsid.name shouldBe "value"
            deepValue.value shouldBe "Deep! 🕳️"
        }

        test("should print parsed structure") {
            println("\n" + "=".repeat(80))
            println("📄 KDTest.kd - Parsed Structure")
            println("=".repeat(80))
            println(root)
            println("=".repeat(80) + "\n")
        }
    }

    // ========================================================================
    // 🎮 Game World File Tests
    // ========================================================================

    context("Loading and parsing KDTestWorld.kd - Game world definition") {

        val root = KD.readResource("KDTestWorld.kd")
        // Note: This file has two top-level tags (world and summary), so parser wraps in "root"
        val world = root.getChild("world")!!

        test("should parse without errors") {
            root shouldNotBe null
            root.nsid.name shouldBe "root"  // Multiple top-level tags get wrapped
            root.children.size shouldBe 2   // world and summary
            world.annotations.shouldNotBeEmpty()
        }

        test("should have world settings") {
            val settings = world.getChild("settings")
            settings shouldNotBe null
            settings!!.getChild("name")!!.value shouldBe "Eternia: 永遠の王国"
        }

        test("should parse regions with complex nested structure") {
            val regions = world.getChild("regions")
            regions shouldNotBe null

            val regionList = regions!!.getChildren("region")
            regionList.size shouldBeGreaterThan 0

            val aurelia = regionList.find { it.value == "Aurelia" }
            aurelia shouldNotBe null
            aurelia!!.getChild("capital")!!.value shouldBe "Goldenhall"

            val cities = aurelia.getChild("cities")
            cities shouldNotBe null
        }

        test("should parse character classes") {
            val classes = world.getChild("classes")
            classes shouldNotBe null

            val classList = classes!!.getChildren("class")
            classList.size shouldBeGreaterThan 0

            val warrior = classList.find { it.value == "Warrior" }
            warrior shouldNotBe null

            val abilities = warrior!!.getChild("abilities")
            abilities shouldNotBe null
        }

        test("should parse items with quantities") {
            val items = world.getChild("items")
            items shouldNotBe null

            val weapons = items!!.getChild("weapons")
            weapons shouldNotBe null
        }

        test("should parse quests with objectives") {
            val quests = world.getChild("quests")
            quests shouldNotBe null

            val questList = quests!!.getChildren("quest")
            questList.shouldNotBeEmpty()
        }

        test("should parse events with date/time") {
            val events = world.getChild("events")
            events shouldNotBe null

            val eventList = events!!.getChildren("event")
            eventList.shouldNotBeEmpty()
        }

        test("should print parsed structure") {
            println("\n" + "=".repeat(80))

            println("🎮 KDTestWorld.kd - Parsed Structure")
            println("=".repeat(80))
            println(root)
            println("=".repeat(80) + "\n")
        }
    }

    // ========================================================================
    // 🔬 Science File Tests
    // ========================================================================

    context("Loading and parsing KDTestScience.kd - Scientific data") {

        val root = KD.readResource("KDTestScience.kd")
        // Note: This file has two top-level tags (science and summary), so parser wraps in "root"
        val science = root.getChild("science")!!

        test("should parse without errors") {
            root shouldNotBe null
            root.nsid.name shouldBe "root"  // Multiple top-level tags get wrapped
            root.children.size shouldBe 2   // science and summary
        }

        test("should parse physical constants") {
            val constants = science.getChild("constants")
            constants shouldNotBe null

            val speedOfLight = constants!!.getChild("speed_of_light")
            speedOfLight shouldNotBe null
            speedOfLight!!.value.shouldBeInstanceOf<Quantity<*>>()
            speedOfLight["symbol"] shouldBe "c"
        }

        test("should parse chemical elements") {
            val elements = science.getChild("elements")
            elements shouldNotBe null

            val elementList = elements!!.getChildren("element")
            elementList.shouldNotBeEmpty()

            val hydrogen = elementList.find { it.value == "Hydrogen" }
            hydrogen shouldNotBe null
            hydrogen!!["symbol"] shouldBe "H"
        }

        test("should parse solar system data") {
            val solarSystem = science.getChild("solar_system")
            solarSystem shouldNotBe null

            val star = solarSystem!!.getChild("star")
            star shouldNotBe null
            star!!.value shouldBe "Sun"
            star.getChild("japanese_name")!!.value shouldBe "太陽"

            val planets = solarSystem.getChild("planets")
            planets shouldNotBe null

            val planetList = planets!!.getChildren("planet")
            planetList.size shouldBe 8

            val earth = planetList.find { it.value == "Earth" }
            earth shouldNotBe null
            earth!!.getChild("japanese_name")!!.value shouldBe "地球"
            earth.getChild("life")!!.value shouldBe true
        }

        test("should parse experiments") {
            val experiments = science.getChild("experiments")
            experiments shouldNotBe null

            val experimentList = experiments!!.getChildren("experiment")
            experimentList.shouldNotBeEmpty()
        }

        test("should parse computing benchmarks") {
            val computing = science.getChild("computing")
            computing shouldNotBe null

            val benchmarks = computing!!.getChild("benchmarks")
            benchmarks shouldNotBe null
        }

        test("should print parsed structure") {
            println("\n" + "=".repeat(80))
            println("🔬 KDTestScience.kd - Parsed Structure")
            println("=".repeat(80))
            println(root)
            println("=".repeat(80) + "\n")
        }
    }

    // ========================================================================
    // 📊 Statistics and Summary
    // ========================================================================

    context("Combined statistics") {

        test("should process all three files and report statistics") {
            val files = listOf("KDTest.kd", "KDTestWorld.kd", "KDTestScience.kd")

            println("\n" + "=".repeat(80))
            println("📊 KD Parsing Statistics")
            println("=".repeat(80))

            var totalTags = 0
            var totalAnnotations = 0
            var totalValues = 0
            var totalAttributes = 0
            var totalDepth = 0

            for (filename in files) {
                val root = KD.readResource(filename)

                val stats = collectStats(root)
                totalTags += stats.tagCount
                totalAnnotations += stats.annotationCount
                totalValues += stats.valueCount
                totalAttributes += stats.attributeCount
                if (stats.maxDepth > totalDepth) totalDepth = stats.maxDepth

                println("\n📁 $filename:")
                println("   Tags: ${stats.tagCount}")
                println("   Annotations: ${stats.annotationCount}")
                println("   Values: ${stats.valueCount}")
                println("   Attributes: ${stats.attributeCount}")
                println("   Max Depth: ${stats.maxDepth}")
            }

            println("\n" + "-".repeat(40))
            println("📈 TOTALS:")
            println("   Total Tags: $totalTags")
            println("   Total Annotations: $totalAnnotations")
            println("   Total Values: $totalValues")
            println("   Total Attributes: $totalAttributes")
            println("   Max Depth: $totalDepth")
            println("=".repeat(80) + "\n")

            totalTags shouldBeGreaterThan 100
        }
    }
})

/**
 * Statistics collected from a KD tag tree.
 */
data class KDStats(
    val tagCount: Int,
    val annotationCount: Int,
    val valueCount: Int,
    val attributeCount: Int,
    val maxDepth: Int
)

/**
 * Recursively collects statistics from a tag tree.
 */
fun collectStats(tag: Tag, depth: Int = 1): KDStats {
    var tagCount = 1
    var annotationCount = tag.annotations.size
    var valueCount = tag.values.size
    var attributeCount = tag.attributes.size
    var maxDepth = depth

    for (child in tag.children) {
        val childStats = collectStats(child, depth + 1)
        tagCount += childStats.tagCount
        annotationCount += childStats.annotationCount
        valueCount += childStats.valueCount
        attributeCount += childStats.attributeCount
        if (childStats.maxDepth > maxDepth) maxDepth = childStats.maxDepth
    }

    return KDStats(tagCount, annotationCount, valueCount, attributeCount, maxDepth)
}