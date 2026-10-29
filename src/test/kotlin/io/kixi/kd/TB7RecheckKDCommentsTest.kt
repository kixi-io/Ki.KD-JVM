package io.kixi.kd

import io.kixi.Grid
import io.kixi.Ki
import io.kixi.Vector
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal as Dec

/**
 * TB7 re-check (2026-10-29), Ki.KD: the comment texts of TB7RecheckCoreCommentsTest as KD reads
 * them, and big whole numbers in a Dec grid or vector right before the closing bracket.
 */
class TB7RecheckKDCommentsTest : FunSpec({
    test("C2 KD reads a /* inside a line comment as part of the comment; big whole numbers before ) or }") {
        val problems = ArrayList<String>()
        for (text in listOf(".vec<Int> {\n 1 # see /* the notes\n 2\n}", ".vec<Int> {\n 1 // a /* b\n 2\n}", ".vec<Int> {\n 1 # a */ b\n 2\n}")) {
            try { val v = KD.read("t $text").values.single(); if (v != Vector.ofInts(1, 2)) problems += "KD ${text.replace("\n", "⏎")} -> $v" } catch (e: Exception) { problems += "KD ${text.replace("\n", "⏎")}: ${e.message?.lineSequence()?.first()}" }
        }
        for (text in listOf(".grid<Dec>( 1 100000000000000000000)", ".grid<Dec>( 1 100000000000000000000 )", ".vec<Dec> { 1 100000000000000000000}", ".grid<Dec> { 1 100000000000000000000}")) {
            try { KD.read("t $text") } catch (e: Exception) { problems += "KD $text: ${e.message?.lineSequence()?.first()}" }
        }
        // what Ki.Core writes for a Dec grid with a big whole number reads back
        try {
            val g = KD.read("t .grid<Dec> { 1 100000000000000000000 }").values.single() as Grid<*>
            val text = Ki.format(g)
            val back = KD.read("t $text").values.single()
            if (back != g) problems += "grid ${text.replace("\n", "⏎")} -> $back"
        } catch (e: Exception) { problems += "grid round trip: ${e.message?.lineSequence()?.first()}" }
        if (problems.isNotEmpty()) println("[C2] ${problems.size} problems:\n  " + problems.joinToString("\n  "))
        withClue(problems.joinToString("\n")) { problems.size shouldBe 0 }
    }
})
