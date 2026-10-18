package io.kixi.kd

import io.kixi.Ki
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import kotlin.random.Random

/** Every Duration written as KD text by Ki.formatDuration reads back as the same Duration. */
class DurationTextRoundTripTest : FunSpec({

    fun roundTrip(d: Duration) {
        for (text in listOf(Ki.formatDuration(d), Ki.formatDuration(d, zeroPad = true))) {
            withClue("$d as \"$text\"") {
                KD.read("t $text").values.single() shouldBe d
                KD.read("t value=$text").attributes[io.kixi.NSID("value")] shouldBe d
            }
        }
    }

    test("each format the formatter writes") {
        listOf(
            Duration.ZERO, Duration.ofNanos(500), Duration.ofMillis(100), Duration.ofNanos(22_708_751),
            Duration.ofSeconds(45), Duration.ofMillis(5_500), Duration.ofMinutes(30), Duration.ofHours(5),
            Duration.ofDays(1), Duration.ofDays(3), Duration.ofSeconds(5_445), Duration.ofDays(2).plusHours(3).plusMinutes(30),
            Duration.ofMillis(-59_500), Duration.ofMillis(-119_500), Duration.ofMillis(-86_399_500), Duration.ofHours(-5),
            Duration.ofNanos(-22_708_751), Duration.ofDays(365L * 300),
            Duration.ofSeconds(Long.MAX_VALUE, 999_999_999), Duration.ofSeconds(Long.MIN_VALUE),
        ).forEach(::roundTrip)
    }

    test("random durations") {
        val r = Random(20261018)
        repeat(3_000) {
            roundTrip(
                when (r.nextInt(3)) {
                    0 -> Duration.ofNanos(r.nextLong() shr r.nextInt(63))
                    1 -> Duration.ofSeconds(r.nextLong(), r.nextLong(0, 1_000_000_000))
                    else -> Duration.ofSeconds(r.nextLong(-200_000, 200_000), r.nextLong(0, 1_000_000_000))
                }
            )
        }
    }
})
