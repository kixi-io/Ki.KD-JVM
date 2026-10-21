package io.kixi.kd.tb

import io.kixi.text.ParseException
import io.kixi.types.BagFunction
import io.kixi.types.Member
import io.kixi.types.TypeBag
import io.kixi.types.TypeBags
import io.kixi.types.ValueType

/*
 * A throwaway type bag for the TB1 tests (Ki-TypeBags design, 7): no domain in it,
 * just one of each thing the contract has. A copy of Ki.Core's test bag
 * (io.kixi.types.SampleBag); keep the two alike.
 *
 * Bag `tb`, version 2:
 * - Seq: `.seq("ACGT")` (alias `.sq`), bases A, C, G, T. Encoded two bits per base
 *   (version 2) or one byte per base (version 1, still decoded). Orders lexically.
 *   Members length, gc (nil when empty), slice(a, b), reverse, contains(s), trim(n).
 * - Shape: `.shape(circle)`, a bare token; open, so a subclass is a Shape. Does not order.
 * - Pairish: an interface type with no literal, so it cannot be stored.
 * - Functions revcomp(s), concat(a, b, ...); constant answer = 42.
 * Bag `tbx` requires `tb` and adds member isPalindrome to Seq.
 */

data class Seq(val bases: String) {
    init {
        require(bases.all { it in "ACGT" }) { "A Seq holds only A, C, G and T: $bases" }
    }
    override fun toString(): String = "Seq($bases)"
}

open class Shape(val kind: String) {
    override fun equals(other: Any?): Boolean = other is Shape && other.kind == kind
    override fun hashCode(): Int = kind.hashCode()
}

interface Pairish { val a: Int; val b: Int }
data class PairImpl(override val a: Int, override val b: Int) : Pairish

fun revcomp(s: Seq): Seq = Seq(s.bases.reversed().map { when (it) { 'A' -> 'T'; 'T' -> 'A'; 'C' -> 'G'; else -> 'C' } }.joinToString(""))

object SeqType : ValueType<Seq> {
    override val name = "Seq"
    override val kotlinClass = Seq::class
    override val literals = listOf("seq", "sq")

    override fun parse(literal: String): Seq {
        val text = TypeBags.stringOrToken(TypeBags.inside(literal))
        if (!text.all { it in "ACGT" }) throw ParseException("Not a Seq: $text")
        return Seq(text)
    }

    override fun format(value: Seq): String = ".seq(\"${value.bases}\")"

    override fun encode(value: Seq): ByteArray {
        val n = value.bases.length
        val out = ByteArray(4 + (n + 3) / 4)
        out[0] = (n ushr 24).toByte(); out[1] = (n ushr 16).toByte(); out[2] = (n ushr 8).toByte(); out[3] = n.toByte()
        for ((i, c) in value.bases.withIndex()) {
            val code = "ACGT".indexOf(c)
            out[4 + i / 4] = (out[4 + i / 4].toInt() or (code shl (6 - 2 * (i % 4)))).toByte()
        }
        return out
    }

    override fun decode(bytes: ByteArray, version: Int): Seq = when (version) {
        1 -> Seq(String(bytes, Charsets.US_ASCII))
        2 -> {
            require(bytes.size >= 4) { "Short Seq" }
            val n = ((bytes[0].toInt() and 0xFF) shl 24) or ((bytes[1].toInt() and 0xFF) shl 16) or
                    ((bytes[2].toInt() and 0xFF) shl 8) or (bytes[3].toInt() and 0xFF)
            require(n >= 0 && bytes.size == 4 + (n + 3) / 4) { "Bad Seq length $n" }
            Seq(String(CharArray(n) { i -> "ACGT"[(bytes[4 + i / 4].toInt() ushr (6 - 2 * (i % 4))) and 3] }))
        }
        else -> throw IllegalArgumentException("Seq version $version")
    }

    override val ordered = true
    override fun sortKey(value: Seq): ByteArray = value.bases.toByteArray(Charsets.US_ASCII)

    override val samples = listOf(Seq(""), Seq("A"), Seq("ACGT"), Seq("TTGCA"), Seq("ACGTACGTA"), Seq("G".repeat(1000)))

    /** The version-1 bytes, for tests that read old data. */
    fun encodeV1(value: Seq): ByteArray = value.bases.toByteArray(Charsets.US_ASCII)

    override val members: List<Member<Seq>> = listOf(
        Member("length", null, "Int") { s, _ -> s.bases.length },
        Member("gc", null, "Double") { s, _ -> if (s.bases.isEmpty()) null else s.bases.count { it == 'G' || it == 'C' }.toDouble() / s.bases.length },
        Member("slice", 2..2, "Seq") { s, a -> Seq(s.bases.substring((a[0] as Number).toInt(), (a[1] as Number).toInt())) },
        Member("reverse", 0..0, "Seq") { s, _ -> Seq(s.bases.reversed()) },
        Member("contains", 1..1, "Bool") { s, a ->
            when (val x = a[0]) { is Seq -> x.bases in s.bases; is String -> x in s.bases; else -> false }
        },
        // Shares its name with KQ's built-in trim(), which takes no argument (the checker's ARITY case).
        Member("trim", 1..1, "Seq") { s, a -> val n = (a[0] as Number).toInt(); Seq(s.bases.drop(n).dropLast(n)) },
    )
}

object ShapeType : ValueType<Shape> {
    override val name = "Shape"
    override val kotlinClass = Shape::class
    override val literals = listOf("shape")
    override fun parse(literal: String): Shape {
        val k = TypeBags.stringOrToken(TypeBags.inside(literal))
        if (k != "circle" && k != "square") throw ParseException("Not a shape: $k")
        return Shape(k)
    }
    override fun format(value: Shape): String = ".shape(${value.kind})"
    override fun encode(value: Shape): ByteArray = value.kind.toByteArray()
    override fun decode(bytes: ByteArray, version: Int): Shape = Shape(String(bytes))
    override val members: List<Member<Shape>> = listOf(Member("kind", null, "String") { s, _ -> s.kind })
    override val samples = listOf(Shape("circle"), Shape("square"))
}

object PairishType : ValueType<Pairish> {
    override val name = "Pairish"
    override val kotlinClass = Pairish::class
    override val literals = emptyList<String>()
    override fun parse(literal: String): Pairish = throw ParseException("Pairish has no literal")
    override fun format(value: Pairish): String = "Pairish(${value.a}, ${value.b})"
    override fun encode(value: Pairish): ByteArray = byteArrayOf(value.a.toByte(), value.b.toByte())
    override fun decode(bytes: ByteArray, version: Int): Pairish = PairImpl(bytes[0].toInt(), bytes[1].toInt())
    override val members: List<Member<Pairish>> = listOf(Member("sum", null, "Int") { p, _ -> p.a + p.b })
    override val samples = listOf<Pairish>(PairImpl(1, 2), PairImpl(0, 0))
}

object SampleBag : TypeBag {
    override val name = "tb"
    override val version = 2
    override val types = listOf(SeqType, ShapeType, PairishType)
    override val functions = listOf(
        BagFunction("revcomp", 1..1, "Seq") { a -> revcomp(a[0] as Seq) },
        BagFunction("concat", 2..Int.MAX_VALUE, "Seq") { a -> Seq(a.joinToString("") { (it as Seq).bases }) },
    )
    override val constants = mapOf<String, Any>("answer" to 42)
}

object SampleExtensionBag : TypeBag {
    override val name = "tbx"
    override val version = 1
    override val requires = listOf("tb")
    override val types = emptyList<ValueType<*>>()
    override val extensions: Map<String, List<Member<*>>> = mapOf(
        "Seq" to listOf(Member<Seq>("isPalindrome", null, "Bool") { s, _ -> revcomp(s) == s }),
    )
}

/** Runs [block] with the sample bags registered, removing them afterwards. */
fun <R> withSampleBags(extension: Boolean = true, block: () -> R): R {
    TypeBags.register(SampleBag)
    if (extension) TypeBags.register(SampleExtensionBag)
    try {
        return block()
    } finally {
        if (extension) TypeBags.unregister(SampleExtensionBag.name)
        TypeBags.unregister(SampleBag.name)
    }
}
