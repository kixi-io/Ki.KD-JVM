package io.kixi.kd

import io.kixi.*
import io.kixi.text.ParseException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.string.shouldContain
import java.math.BigDecimal as Dec
import java.time.*

/**
 * Comprehensive tests for KD Parser literal types Phase 2:
 * 1. Date (LocalDate)
 * 2. LocalDateTime
 * 3. ZonedDateTime (offset-based)
 * 4. KiTZDateTime (named timezone)
 * 5. Duration
 * 6. Version
 * 7. Blob
 * 8. GeoPoint
 */
class KDParserTest2 : FunSpec({

    val parser = KDParser()

    // ========================================================================
    // Date (LocalDate) Tests
    // ========================================================================

    context("Date parsing") {

        test("should parse simple date y/M/d") {
            val tag = parser.parse("date 2005/12/05")
            tag.nsid.name shouldBe "date"
            tag.value.shouldBeInstanceOf<LocalDate>()
            tag.value shouldBe LocalDate.of(2005, 12, 5)
        }

        test("should parse date with single-digit month and day") {
            val tag = parser.parse("date 2024/3/9")
            tag.value.shouldBeInstanceOf<LocalDate>()
            tag.value shouldBe LocalDate.of(2024, 3, 9)
        }

        test("should parse date with zero-padded month and day") {
            val tag = parser.parse("date 2020/09/18")
            tag.value.shouldBeInstanceOf<LocalDate>()
            tag.value shouldBe LocalDate.of(2020, 9, 18)
        }

        test("should parse historical date") {
            val tag = parser.parse("published 1937/9/21")
            tag.value.shouldBeInstanceOf<LocalDate>()
            tag.value shouldBe LocalDate.of(1937, 9, 21)
        }

        test("should parse date as attribute value") {
            val tag = parser.parse("book \"The Hobbit\" published=1937/9/21")
            tag.nsid.name shouldBe "book"
            tag.value shouldBe "The Hobbit"
            tag["published"] shouldBe LocalDate.of(1937, 9, 21)
        }

        test("should parse multiple dates in values") {
            val tag = parser.parse("dates 2020/1/1 2020/6/15 2020/12/31")
            tag.values.size shouldBe 3
            tag.values[0] shouldBe LocalDate.of(2020, 1, 1)
            tag.values[1] shouldBe LocalDate.of(2020, 6, 15)
            tag.values[2] shouldBe LocalDate.of(2020, 12, 31)
        }

        test("should parse date in list") {
            val tag = parser.parse("dates [2020/1/1, 2020/12/31]")
            val dates = tag.value as List<*>
            dates.size shouldBe 2
            dates[0] shouldBe LocalDate.of(2020, 1, 1)
            dates[1] shouldBe LocalDate.of(2020, 12, 31)
        }

        test("should parse anonymous date tag") {
            val tag = parser.parse("2024/12/25")
            tag.value shouldBe LocalDate.of(2024, 12, 25)
        }
    }

    // ========================================================================
    // LocalDateTime Tests
    // ========================================================================

    context("LocalDateTime parsing") {

        test("should parse basic LocalDateTime") {
            val tag = parser.parse("event 2021/12/05@05:21:23")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
            val ldt = tag.value as LocalDateTime
            ldt.year shouldBe 2021
            ldt.monthValue shouldBe 12
            ldt.dayOfMonth shouldBe 5
            ldt.hour shouldBe 5
            ldt.minute shouldBe 21
            ldt.second shouldBe 23
        }

        test("should parse LocalDateTime with fractional seconds") {
            val tag = parser.parse("event 2021/12/05@05:21:23.53")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
            val ldt = tag.value as LocalDateTime
            ldt.second shouldBe 23
            ldt.nano shouldBe 530000000
        }

        test("should parse LocalDateTime with nanoseconds") {
            val tag = parser.parse("event 2021/12/05@05:21:23.123456789")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
            val ldt = tag.value as LocalDateTime
            ldt.nano shouldBe 123456789
        }

        test("should parse LocalDateTime without seconds") {
            val tag = parser.parse("event 2021/12/05@05:21")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
            val ldt = tag.value as LocalDateTime
            ldt.hour shouldBe 5
            ldt.minute shouldBe 21
            ldt.second shouldBe 0
        }

        test("should parse LocalDateTime with single-digit hour") {
            val tag = parser.parse("event 2024/3/9@9:05:03")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
            val ldt = tag.value as LocalDateTime
            ldt.monthValue shouldBe 3
            ldt.dayOfMonth shouldBe 9
            ldt.hour shouldBe 9
            ldt.minute shouldBe 5
            ldt.second shouldBe 3
        }

        test("should parse LocalDateTime at midnight") {
            val tag = parser.parse("event 2024/1/1@0:00:00")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
            val ldt = tag.value as LocalDateTime
            ldt.hour shouldBe 0
            ldt.minute shouldBe 0
            ldt.second shouldBe 0
        }

        test("should parse LocalDateTime at end of day") {
            val tag = parser.parse("event 2024/12/31@23:59:59")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
            val ldt = tag.value as LocalDateTime
            ldt.hour shouldBe 23
            ldt.minute shouldBe 59
            ldt.second shouldBe 59
        }

        test("should parse anonymous LocalDateTime") {
            val tag = parser.parse("2024/3/15@14:30:00")
            tag.value.shouldBeInstanceOf<LocalDateTime>()
        }
    }

    // ========================================================================
    // ZonedDateTime Tests (offset-based timezones)
    // ========================================================================

    context("ZonedDateTime parsing") {

        test("should parse ZonedDateTime with positive offset") {
            val tag = parser.parse("event 2005/12/05@05:21:23.532+2")
            tag.value.shouldBeInstanceOf<ZonedDateTime>()
            val zdt = tag.value as ZonedDateTime
            zdt.offset shouldBe ZoneOffset.ofHours(2)
        }

        test("should parse ZonedDateTime with negative offset") {
            val tag = parser.parse("event 2005/12/05@05:21:23.532-8")
            tag.value.shouldBeInstanceOf<ZonedDateTime>()
            val zdt = tag.value as ZonedDateTime
            zdt.offset shouldBe ZoneOffset.ofHours(-8)
        }

        test("should parse ZonedDateTime with offset including minutes") {
            val tag = parser.parse("event 2005/12/05@05:21:23.532+5:30")
            tag.value.shouldBeInstanceOf<ZonedDateTime>()
            val zdt = tag.value as ZonedDateTime
            zdt.offset shouldBe ZoneOffset.ofHoursMinutes(5, 30)
        }

        test("should parse ZonedDateTime with negative offset including minutes") {
            val tag = parser.parse("event 2005/12/05@05:21:23-3:30")
            tag.value.shouldBeInstanceOf<ZonedDateTime>()
            val zdt = tag.value as ZonedDateTime
            zdt.offset shouldBe ZoneOffset.ofHoursMinutes(-3, -30)
        }

        test("should parse ZonedDateTime with two-digit offset") {
            val tag = parser.parse("event 2005/12/05@05:21:23+09")
            tag.value.shouldBeInstanceOf<ZonedDateTime>()
            val zdt = tag.value as ZonedDateTime
            zdt.offset shouldBe ZoneOffset.ofHours(9)
        }

        test("should parse ZonedDateTime as attribute") {
            val tag = parser.parse("entry \"Something happened\" time=2005/11/23@10:14:23.253+5")
            tag.value shouldBe "Something happened"
            val time = tag["time"]
            time.shouldBeInstanceOf<ZonedDateTime>()
        }
    }

    // ========================================================================
    // KiTZDateTime Tests (named timezone identifiers)
    // ========================================================================

    context("KiTZDateTime parsing") {

        test("should parse KiTZDateTime with -Z") {
            val tag = parser.parse("event 2005/12/05@05:21:23.532-Z")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.UTC
            kdt.kiTZ shouldBe KiTZ.UTC
        }

        test("should parse KiTZDateTime with -UTC") {
            val tag = parser.parse("event 2005/12/05@05:21:23.532-UTC")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.UTC
            kdt.kiTZ shouldBe KiTZ.UTC
        }

        test("should parse KiTZDateTime with -GMT") {
            val tag = parser.parse("event 2005/12/05@05:21:23-GMT")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.UTC
            kdt.kiTZ shouldBe KiTZ.UTC
        }

        test("should parse KiTZDateTime with KiTZ (JP/JST)") {
            val tag = parser.parse("event 2005/12/05@05:21:23.532-JP/JST")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.ofHours(9)
            kdt.kiTZ shouldBe KiTZ.JP_JST
            kdt.kiTZ.country shouldBe "Japan"
        }

        test("should parse KiTZDateTime with KiTZ (US/PST)") {
            val tag = parser.parse("event 2005/12/05@05:21:23.532-US/PST")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.ofHours(-8)
            kdt.kiTZ shouldBe KiTZ.US_PST
            kdt.kiTZ.country shouldBe "United States"
        }

        test("should parse KiTZDateTime with KiTZ (US/EST)") {
            val tag = parser.parse("event 2005/12/05@05:21:23-US/EST")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.ofHours(-5)
            kdt.kiTZ shouldBe KiTZ.US_EST
        }

        test("should parse KiTZDateTime with KiTZ (GB/GMT)") {
            val tag = parser.parse("event 2005/12/05@05:21:23-GB/GMT")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.UTC
            kdt.kiTZ shouldBe KiTZ.GB_GMT
        }

        test("should parse KiTZDateTime with KiTZ (DE/CET)") {
            val tag = parser.parse("event 2005/12/05@05:21:23-DE/CET")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.ofHours(1)
            kdt.kiTZ shouldBe KiTZ.DE_CET
            kdt.kiTZ.country shouldBe "Germany"
        }

        test("should parse KiTZDateTime with KiTZ (AU/AEST)") {
            val tag = parser.parse("event 2024/3/15@14:30:00-AU/AEST")
            tag.value.shouldBeInstanceOf<KiTZDateTime>()
            val kdt = tag.value as KiTZDateTime
            kdt.offset shouldBe ZoneOffset.ofHours(10)
            kdt.kiTZ shouldBe KiTZ.AU_AEST
            kdt.kiTZ.country shouldBe "Australia"
        }

        test("should parse KiTZDateTime as attribute") {
            val tag = parser.parse("entry \"Log message\" time=2005/11/23@10:14:23.253-UTC")
            tag.value shouldBe "Log message"
            val time = tag["time"]
            time.shouldBeInstanceOf<KiTZDateTime>()
            (time as KiTZDateTime).kiTZ shouldBe KiTZ.UTC
        }

        test("should parse KiTZDateTime in list") {
            val tag = parser.parse("events [2024/1/1@00:00:00-US/EST, 2024/6/15@12:00:00-JP/JST]")
            val events = tag.value as List<*>
            events.size shouldBe 2
            events[0].shouldBeInstanceOf<KiTZDateTime>()
            events[1].shouldBeInstanceOf<KiTZDateTime>()
            (events[0] as KiTZDateTime).kiTZ shouldBe KiTZ.US_EST
            (events[1] as KiTZDateTime).kiTZ shouldBe KiTZ.JP_JST
        }

        test("should preserve KiTZ timezone identity") {
            // US/PST and CA/PST have the same offset but different KiTZ
            val tag1 = parser.parse("event 2024/3/15@14:30:00-US/PST")
            val tag2 = parser.parse("event 2024/3/15@14:30:00-CA/PST")

            val kdt1 = tag1.value as KiTZDateTime
            val kdt2 = tag2.value as KiTZDateTime

            kdt1.offset shouldBe kdt2.offset  // Same offset
            kdt1.kiTZ shouldNotBe kdt2.kiTZ   // Different KiTZ
            kdt1.kiTZ.countryCode shouldBe "US"
            kdt2.kiTZ.countryCode shouldBe "CA"
        }

        test("KiTZDateTime provides formatted output with KiTZ") {
            val tag = parser.parse("event 2024/3/15@14:30:00-JP/JST")
            val kdt = tag.value as KiTZDateTime
            kdt.kiFormat() shouldBe "2024/3/15@14:30:00-JP/JST"
        }
    }

    // ========================================================================
    // Duration Tests
    // ========================================================================

    context("Duration parsing - compound format") {

        test("should parse duration hh:mm:ss") {
            val tag = parser.parse("elapsed 12:30:00")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toHours() shouldBe 12
            dur.toMinutesPart() shouldBe 30
        }

        test("should parse duration with fractional seconds") {
            val tag = parser.parse("elapsed 1:30:45.5")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toHours() shouldBe 1
            dur.toMinutesPart() shouldBe 30
            dur.toSecondsPart() shouldBe 45
            dur.toNanosPart() shouldBe 500000000
        }

        test("should parse duration with days") {
            val tag = parser.parse("elapsed 3day:12:30:00")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toDays() shouldBe 3
            dur.toHoursPart() shouldBe 12
        }

        test("should parse duration with 1 day") {
            val tag = parser.parse("elapsed 1day:00:00:00")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toDays() shouldBe 1
        }

        test("should parse negative duration") {
            val tag = parser.parse("offset -1:30:00")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.isNegative shouldBe true
        }
    }

    context("Duration parsing - single unit format") {

        test("should parse days duration") {
            val tag = parser.parse("timeout 5days")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toDays() shouldBe 5
        }

        test("should parse single day duration") {
            val tag = parser.parse("timeout 1day")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toDays() shouldBe 1
        }

        test("should parse hours duration") {
            val tag = parser.parse("timeout 24h")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toHours() shouldBe 24
        }

        test("should parse minutes duration") {
            val tag = parser.parse("timeout 90min")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toMinutes() shouldBe 90
        }

        test("should parse seconds duration") {
            val tag = parser.parse("timeout 30s")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toSeconds() shouldBe 30
        }

        test("should parse milliseconds duration") {
            val tag = parser.parse("timeout 500ms")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toMillis() shouldBe 500
        }

        test("should parse nanoseconds duration") {
            val tag = parser.parse("timeout 1000000ns")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toNanos() shouldBe 1000000
        }

        test("should parse fractional seconds") {
            val tag = parser.parse("timeout 2.5s")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.toMillis() shouldBe 2500
        }

        test("should parse negative single unit duration") {
            val tag = parser.parse("offset -5h")
            tag.value.shouldBeInstanceOf<Duration>()
            val dur = tag.value as Duration
            dur.isNegative shouldBe true
            dur.abs().toHours() shouldBe 5
        }
    }

    context("Duration in various contexts") {

        test("should parse duration in list") {
            val tag = parser.parse("timeouts [1h, 30min, 5s]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            (list[0] as Duration).toHours() shouldBe 1
            (list[1] as Duration).toMinutes() shouldBe 30
            (list[2] as Duration).toSeconds() shouldBe 5
        }

        test("should parse duration as attribute") {
            val tag = parser.parse("task \"Build\" timeout=30min")
            tag.value shouldBe "Build"
            val timeout = tag["timeout"]
            timeout.shouldBeInstanceOf<Duration>()
            (timeout as Duration).toMinutes() shouldBe 30
        }

        test("should parse anonymous duration tag") {
            val tag = parser.parse("5h")
            tag.value.shouldBeInstanceOf<Duration>()
        }
    }

    // ========================================================================
    // Version Tests
    // ========================================================================

    context("Version parsing") {

        test("should parse version with major.minor.micro") {
            val tag = parser.parse("version 1.2.3")
            tag.value.shouldBeInstanceOf<Version>()
            val ver = tag.value as Version
            ver.major shouldBe 1
            ver.minor shouldBe 2
            ver.micro shouldBe 3
        }

        test("should parse version with qualifier") {
            val tag = parser.parse("version 1.0.0-alpha")
            tag.value.shouldBeInstanceOf<Version>()
            val ver = tag.value as Version
            ver.major shouldBe 1
            ver.minor shouldBe 0
            ver.micro shouldBe 0
            ver.qualifier shouldBe "alpha"
        }

        test("should parse version with qualifier number") {
            val tag = parser.parse("version 2.0.0-beta-3")
            tag.value.shouldBeInstanceOf<Version>()
            val ver = tag.value as Version
            ver.qualifier shouldBe "beta"
            ver.qualifierNumber shouldBe 3
        }

        test("should parse version with qualifier number no dash") {
            val tag = parser.parse("version 2.0.0-rc1")
            tag.value.shouldBeInstanceOf<Version>()
            val ver = tag.value as Version
            ver.qualifier shouldBe "rc"
            ver.qualifierNumber shouldBe 1
        }

        test("should parse full version") {
            val tag = parser.parse("version 5.2.7-rc-5")
            tag.value.shouldBeInstanceOf<Version>()
            val ver = tag.value as Version
            ver.major shouldBe 5
            ver.minor shouldBe 2
            ver.micro shouldBe 7
            ver.qualifier shouldBe "rc"
            ver.qualifierNumber shouldBe 5
        }

        test("should parse version in list") {
            val tag = parser.parse("versions [1.0.0, 2.0.0-alpha, 3.1.4]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            (list[0] as Version).major shouldBe 1
            (list[1] as Version).qualifier shouldBe "alpha"
            (list[2] as Version).micro shouldBe 4
        }

        test("should parse version as attribute") {
            val tag = parser.parse("software \"MyApp\" version=2.0.0-beta")
            tag.value shouldBe "MyApp"
            val ver = tag["version"]
            ver.shouldBeInstanceOf<Version>()
            (ver as Version).qualifier shouldBe "beta"
        }

        test("should parse anonymous version tag") {
            val tag = parser.parse("3.1.4")
            tag.value.shouldBeInstanceOf<Version>()
        }
    }

    // ========================================================================
    // Blob Tests
    // ========================================================================

    context("Blob parsing") {

        test("should parse simple blob") {
            val tag = parser.parse("data .blob(SGVsbG8gV29ybGQh)")
            tag.value.shouldBeInstanceOf<Blob>()
            val blob = tag.value as Blob
            blob.decodeToString() shouldBe "Hello World!"
        }

        test("should parse empty blob") {
            val tag = parser.parse("data .blob()")
            tag.value.shouldBeInstanceOf<Blob>()
            val blob = tag.value as Blob
            blob.isEmpty() shouldBe true
        }

        test("should parse blob with whitespace in literal") {
            val tag = parser.parse("data .blob(  SGVsbG8=  )")
            tag.value.shouldBeInstanceOf<Blob>()
            val blob = tag.value as Blob
            blob.decodeToString() shouldBe "Hello"
        }

        test("should parse blob as attribute") {
            val tag = parser.parse("file \"test.bin\" content=.blob(SGVsbG8=)")
            tag.value shouldBe "test.bin"
            val content = tag["content"]
            content.shouldBeInstanceOf<Blob>()
            (content as Blob).decodeToString() shouldBe "Hello"
        }

        test("should parse blob in list") {
            val tag = parser.parse("blobs [.blob(SGVsbG8=), .blob(V29ybGQ=)]")
            val list = tag.value as List<*>
            list.size shouldBe 2
            (list[0] as Blob).decodeToString() shouldBe "Hello"
            (list[1] as Blob).decodeToString() shouldBe "World"
        }

        test("should parse anonymous blob tag") {
            val tag = parser.parse(".blob(SGVsbG8=)")
            tag.value.shouldBeInstanceOf<Blob>()
        }
    }

    // ========================================================================
    // GeoPoint Tests
    // ========================================================================

    context("GeoPoint parsing") {

        test("should parse basic geo point") {
            val tag = parser.parse("location .geo(37.7749, -122.4194)")
            tag.value.shouldBeInstanceOf<GeoPoint>()
            val geo = tag.value as GeoPoint
            geo.lat shouldBe (37.7749 plusOrMinus 0.0001)
            geo.lon shouldBe (-122.4194 plusOrMinus 0.0001)
        }

        test("should parse geo point with altitude") {
            val tag = parser.parse("location .geo(35.6762, 139.6503, 40.0)")
            tag.value.shouldBeInstanceOf<GeoPoint>()
            val geo = tag.value as GeoPoint
            geo.lat shouldBe (35.6762 plusOrMinus 0.0001)
            geo.lon shouldBe (139.6503 plusOrMinus 0.0001)
            geo.alt shouldBe (40.0 plusOrMinus 0.0001)
        }

        test("should parse geo point with negative coordinates") {
            val tag = parser.parse("location .geo(-33.8688, 151.2093)")
            tag.value.shouldBeInstanceOf<GeoPoint>()
            val geo = tag.value as GeoPoint
            geo.lat shouldBe (-33.8688 plusOrMinus 0.0001)
            geo.lon shouldBe (151.2093 plusOrMinus 0.0001)
        }

        test("should parse origin geo point") {
            val tag = parser.parse("location .geo(0, 0)")
            tag.value.shouldBeInstanceOf<GeoPoint>()
            val geo = tag.value as GeoPoint
            geo.isOrigin shouldBe true
        }

        test("should parse geo point with high precision") {
            val tag = parser.parse("location .geo(37.77490001, -122.41940002)")
            tag.value.shouldBeInstanceOf<GeoPoint>()
        }

        test("should parse geo point with whitespace") {
            val tag = parser.parse("location .geo(  37.7749 ,  -122.4194  )")
            tag.value.shouldBeInstanceOf<GeoPoint>()
            val geo = tag.value as GeoPoint
            geo.lat shouldBe (37.7749 plusOrMinus 0.0001)
        }

        test("should parse geo point as attribute") {
            val tag = parser.parse("office \"HQ\" coords=.geo(37.7749, -122.4194)")
            tag.value shouldBe "HQ"
            val coords = tag["coords"]
            coords.shouldBeInstanceOf<GeoPoint>()
        }

        test("should parse geo point in list") {
            val tag = parser.parse("locations [.geo(37.7749, -122.4194), .geo(35.6762, 139.6503)]")
            val list = tag.value as List<*>
            list.size shouldBe 2
            list[0].shouldBeInstanceOf<GeoPoint>()
            list[1].shouldBeInstanceOf<GeoPoint>()
        }

        test("should parse anonymous geo point tag") {
            val tag = parser.parse(".geo(37.7749, -122.4194)")
            tag.value.shouldBeInstanceOf<GeoPoint>()
        }

        test("should parse geo point with integer coordinates") {
            val tag = parser.parse("location .geo(37, -122)")
            tag.value.shouldBeInstanceOf<GeoPoint>()
            val geo = tag.value as GeoPoint
            geo.lat shouldBe 37.0
            geo.lon shouldBe -122.0
        }
    }

    // ========================================================================
    // Integration Tests - Mixed Types
    // ========================================================================

    context("Integration - mixed literal types") {

        test("should parse tag with multiple Phase 2 literal types") {
            val tag = parser.parse("""
                event "Conference" date=2024/3/15 time=2024/3/15@09:00:00-US/PST duration=8h version=1.0.0 location=.geo(37.7749, -122.4194)
            """.trimIndent())
            tag.nsid.name shouldBe "event"
            tag.value shouldBe "Conference"
            tag["date"].shouldBeInstanceOf<LocalDate>()
            tag["time"].shouldBeInstanceOf<KiTZDateTime>()
            tag["duration"].shouldBeInstanceOf<Duration>()
            tag["version"].shouldBeInstanceOf<Version>()
            tag["location"].shouldBeInstanceOf<GeoPoint>()
        }

        test("should parse nested tags with various literals") {
            val tag = parser.parse("""
                project "MyApp" version=2.0.0-beta {
                    release date=2024/3/15 time=2024/3/15@10:00:00-Z
                    location coords=.geo(37.7749, -122.4194)
                    durations [1h, 30min, 2h]
                }
            """.trimIndent())
            tag.nsid.name shouldBe "project"
            tag.value shouldBe "MyApp"
            (tag["version"] as Version).qualifier shouldBe "beta"

            val release = tag.getChild("release")
            release?.get("date").shouldBeInstanceOf<LocalDate>()
            release?.get("time").shouldBeInstanceOf<KiTZDateTime>()
        }

        test("should parse dates correctly not confused with versions") {
            val tag = parser.parse("""
                info date=2024/3/15 version=2.3.4
            """.trimIndent())
            tag["date"].shouldBeInstanceOf<LocalDate>()
            tag["version"].shouldBeInstanceOf<Version>()
        }

        test("should parse durations not confused with times") {
            val tag = parser.parse("""
                data elapsed=5h compound=1:30:00 eventTime=2024/3/15@14:30:00
            """.trimIndent())
            tag["elapsed"].shouldBeInstanceOf<Duration>()
            tag["compound"].shouldBeInstanceOf<Duration>()
            tag["eventTime"].shouldBeInstanceOf<LocalDateTime>()
        }

        test("should handle anonymous tags with different literal types") {
            val tag = parser.parse("""
                root {
                    2024/3/15
                    5.2.7
                    3h
                    .geo(37.0, -122.0)
                }
            """.trimIndent())
            val children = tag.children
            children.size shouldBe 4
            children[0].value.shouldBeInstanceOf<LocalDate>()
            children[1].value.shouldBeInstanceOf<Version>()
            children[2].value.shouldBeInstanceOf<Duration>()
            children[3].value.shouldBeInstanceOf<GeoPoint>()
        }

        test("should parse book example from spec") {
            val tag = parser.parse("""
                book "The Hobbit" author="J.R.R. Tolkien" published=1937/9/21
            """.trimIndent())
            tag.value shouldBe "The Hobbit"
            tag["author"] shouldBe "J.R.R. Tolkien"
            tag["published"] shouldBe LocalDate.of(1937, 9, 21)
        }

        test("should parse log entry example") {
            val tag = parser.parse("""
                entry "Something happened" time=2005/11/23@10:14:23.253-US/PST
            """.trimIndent())
            tag.value shouldBe "Something happened"
            val time = tag["time"]
            time.shouldBeInstanceOf<KiTZDateTime>()
        }
    }

    // ========================================================================
    // Error Handling Tests
    // ========================================================================

    context("Error handling for Phase 2 literals") {

        test("should fail on invalid GeoPoint - too few coordinates") {
            shouldThrow<ParseException> {
                parser.parse("location .geo(37.7749)")
            }
        }

        test("should fail on invalid GeoPoint - too many coordinates") {
            shouldThrow<ParseException> {
                parser.parse("location .geo(37.7749, -122.4194, 40.0, 100.0)")
            }
        }

        test("should fail on invalid latitude") {
            shouldThrow<ParseException> {
                parser.parse("location .geo(91.0, -122.4194)")
            }
        }

        test("should fail on invalid longitude") {
            shouldThrow<ParseException> {
                parser.parse("location .geo(37.7749, 181.0)")
            }
        }

        test("should fail on unterminated blob") {
            shouldThrow<ParseException> {
                parser.parse("data .blob(SGVsbG8=")
            }
        }

        test("should fail on invalid KiTZ") {
            shouldThrow<ParseException> {
                parser.parse("event 2024/3/15@14:30:00-XX/INVALID")
            }
        }
    }

    // ========================================================================
    // Distinguishing Numbers from Versions
    // ========================================================================

    context("Number vs Version distinction") {

        test("should parse single integer as Int not Version") {
            val tag = parser.parse("count 5")
            tag.value shouldBe 5
            tag.value.shouldBeInstanceOf<Int>()
        }

        test("should parse single float as Double not Version") {
            val tag = parser.parse("value 3.14")
            tag.value shouldBe 3.14
            tag.value.shouldBeInstanceOf<Double>()
        }

        test("should parse 3.1.4 as Version") {
            val tag = parser.parse("version 3.1.4")
            tag.value.shouldBeInstanceOf<Version>()
        }

        test("should distinguish 3.14 (Double) from 3.1.4 (Version)") {
            val tag1 = parser.parse("pi 3.14")
            tag1.value.shouldBeInstanceOf<Double>()
            tag1.value shouldBe 3.14

            val tag2 = parser.parse("version 3.1.4")
            tag2.value.shouldBeInstanceOf<Version>()
        }

        test("should parse scientific notation as Double") {
            val tag = parser.parse("value 1.5e10")
            tag.value shouldBe 1.5e10
            tag.value.shouldBeInstanceOf<Double>()
        }
    }
})