package io.kixi.kd

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

/**
 * KD keeps a tag's attributes in the order they are written, as it keeps the
 * entries of a map literal, and two tags that differ only in that order are
 * equal (Ki.Core `Call.canonicalText`).
 */
class AttributeOrderTest : FunSpec({

    test("a tag writes its attributes back in the order they were read") {
        val text = "compound zeta=1 name=\"aspirin\" mw=180.16Da alpha=true"
        KD.read(text).toString() shouldBe text
        KD.read(KD.read(text).toString()).toString() shouldBe text
    }

    test("children and annotations keep the order of their attributes too") {
        val text = "@node(zeta=1 alpha=2)\nplate b=2 a=1 {\n    well z=1 y=2\n}"
        KD.read(text).toString().replace("\r\n", "\n") shouldBe text
    }

    test("the order of attributes does not count for equality; values and children do") {
        KD.read("t z=1 a=2") shouldBe KD.read("t a=2 z=1")
        KD.read("t z=1 a=2").hashCode() shouldBe KD.read("t a=2 z=1").hashCode()
        KD.read("t { c y=1 x=2 }") shouldBe KD.read("t { c x=2 y=1 }")
        KD.read("@a(q=1 p=2)\nt") shouldBe KD.read("@a(p=2 q=1)\nt")
        KD.read("t 1 2") shouldNotBe KD.read("t 2 1")
        KD.read("t { a; b }") shouldNotBe KD.read("t { b; a }")
        KD.read("t z=1 a=2") shouldNotBe KD.read("t z=1 a=3")
    }

    test("a map literal keeps its order, as it did before") {
        (KD.read("t m=[z=1 a=2]")["m"] as Map<*, *>).keys.toList() shouldBe listOf("z", "a")
    }
})
