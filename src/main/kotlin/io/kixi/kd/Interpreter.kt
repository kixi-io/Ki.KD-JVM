package io.kixi.kd

import io.kixi.Ki
import io.kixi.Range
import io.kixi.Version
import io.kixi.log
import io.kixi.text.ParseException
import io.kixi.kd.antlr.KDLexer
import io.kixi.kd.antlr.KDParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.Reader
import java.io.StringReader
import java.math.BigDecimal
import java.net.MalformedURLException
import java.net.URL
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

/**
 * TODO:
 *
 * 1. Get Bin64 working
 * 2. Review and get Strings working in line with the spec
 *    https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD)
 * 3. Get formatting working correctly for all types (e.g. dates, durations, etc.)
 * 4. Ensure testing is comprehensive
 * 5. Clean up files
 * 6. Finish the Tag class
 * 7. Fix all warnings
 * 8. Add annotations to allow KD to play nice with Java
 * 9. Update documentation
 * 10. -- Beta 1 release --
 */
class Interpreter {

    private lateinit var lexer: KDLexer
    private lateinit var parser: KDParser

    fun read(reader:Reader) : List<Tag> {
        lexer = KDLexer(CharStreams.fromReader(reader))
        parser = KDParser(CommonTokenStream(lexer))
        parser.buildParseTree = true

        // Watch the Lexer
        /*
        var vocab = lexer.vocabulary

        var tokens = lexer.allTokens
        for(token in tokens) {
            log("$token ${vocab.getSymbolicName(token.type)}")
        }
        */

        val tagListCtx = parser.tagList()
        val tags = ArrayList<Tag>()

        if(tagListCtx == null || tagListCtx.tag().count() == 0)
            return tags

        val childCount = tagListCtx.tag().count()

        (0 until childCount).forEach { i ->
            val child = tagListCtx.tag(i)

            if(child is KDParser.TagContext) {
                tags.add(makeTag(child))
            }
        }

        return tags
    }

    private fun makeTag(tree: KDParser.TagContext) : Tag {

        val nsNameCtx = tree.nsName()
        var namespace = ""; var name = ""

        if(nsNameCtx != null) {
            if(nsNameCtx.COLON()!=null) {
                namespace = nsNameCtx.ID(0).text.trim()
                name = nsNameCtx.ID(1).text.trim()
            } else {
                name = nsNameCtx.ID(0).text.trim()
            }
        }

        val tag = Tag(name, namespace)

        val valuesCtx = tree.valueList()

        if(valuesCtx != null) {
            for(vc in valuesCtx.value()) {
                tag.values.add(makeValue(vc))
            }
        }

        val attsCtx = tree.attributeList()

        if(attsCtx != null) {
            for(att in attsCtx.attribute()) {
                val attNSNameCtx = att.nsName()
                var attNamespace = ""
                var attName = ""

                if(attNSNameCtx != null) {
                    if(attNSNameCtx.COLON()!=null) {
                        attNamespace = attNSNameCtx.ID(0).text.trim()
                        attName = attNSNameCtx.ID(1).text.trim()
                    } else {
                        attName = attNSNameCtx.ID(0).text.trim()
                    }
                }

                tag.setAttribute(attName, attNamespace, makeValue(att.value()))
            }
        }

        // Adding child tags
        val tagListContext = tree.tagList()

        if(tagListContext!=null) {
            val tagContexts  = tagListContext.tag()
            for (tagContext in tagContexts) {
                tag.children.add(makeTag(tagContext))
            }
        }

        return tag
    }

    // TODO: Ensure this is dealing only with values. Deal with formatting in toString().
    private fun makeValue(ctx: KDParser.ValueContext) : Any? {

        val text = ctx.getText()

        //// Strings --- ---
        if(ctx.StringLiteral()!=null) {
            return stripQuotes(text)
        }

        // Nakes strings (IDs treated as strings)
        if(ctx.ID() != null) {
            return text
        }

        //// Numbers --- ---

        if(ctx.IntegerLiteral() != null) return Integer.valueOf(text.replace("_", ""))
        if(ctx.LongLiteral() != null) return text.replace("_", "").toLong()

        if(ctx.DoubleLiteral() != null) return text.replace("_", "").toDouble()
        if(ctx.FloatLiteral() !=null) return text.replace("_", "").toFloat()
        if(ctx.DecimalLiteral() !=null) {
            var decText = text.replace("_", "")
            if(decText.last().equals('m', ignoreCase = true)) decText = decText.dropLast(1)
            return decText.toBigDecimal()
        }

        if(ctx.BinLiteral() != null) return Integer.parseInt(text.replace("_", "")
                .substring(2), 2)

        if(ctx.HexLiteral() != null) return Integer.parseInt(text.replace("_", "")
                .substring(2), 16)

        //// Booleans --- ---

        if(ctx.TRUE() != null) return true

        if(ctx.FALSE() != null) return false

        //// Other simple types --- ---

        if(ctx.NULL() != null) return null

        if(ctx.CharLiteral() != null) return text[0]

        if(ctx.URL() != null) {
            try {
                return URL(text)
            } catch (e:MalformedURLException) {
                // already parsed - should never happen
                throw ParseException("Malformed URL",
                    ctx.start.line, ctx.start.charPositionInLine)
            }
        }

        if(ctx.Version() != null) {
            return Version.parse(text)
        }

        //// TODO: Base64 - Finish implementation.

        if(ctx.base64() != null) {
            // log("Got bin64")
            // return "bin64"

            return ctx.base64().BASE64_DATA().text
        }

        //// Duration --- ---

        val durationCtx = ctx.duration()
        if (durationCtx!=null) {
            return makeDuration(durationCtx, text)
        }

        //// DateTime --- ---
        // Can be a Date, LocalDateTime or ZonedDateTime

        val dateTimeCtx = ctx.dateTime()

        if(dateTimeCtx!=null) {
            return makeTemporal(dateTimeCtx, text)
        }

        if(ctx.range() != null) {
            return makeRange(ctx.range())
        }

        //// List --- ---

        val listCtx = ctx.list()
        if(listCtx!=null) {
            return makeList(listCtx)
        }

        //// Map --- ---

        val mapContext = ctx.map()
        if(mapContext!=null) return makeMap(mapContext)

        log("Couldn't find a value type")

        val t = ctx.getStart()

        throw KDParseException("Unknown literal type.", t.line, t.charPositionInLine)
    }

    /**
     * Make single unit or compound Duration
     *
     * @param text The flattened string for the Duration node
     */
    private fun makeDuration(durationCtx: KDParser.DurationContext, text: String): Duration {
        // TODO - Finish day and fractional seconds for compound durations and add single
        // unit Durations.

        /*
        var parts = text.split(':')
        return Duration.ofHours(parts[0].toLong())
                .plus(Duration.ofMinutes(parts[1].toLong()))
                .plus(Duration.ofSeconds(parts[2].toLong()))
         */
        return Ki.parseDuration(text)
    }

    /**
     * Can be Date, LocalDateTime, or ZonedDateTime
     */
    private fun makeTemporal(ctx: KDParser.DateTimeContext, text:String): Any? {

        val timeNode = ctx.Time()
        val zoneNode = ctx.TimeZone()

        if(timeNode==null) {
            // LocalDate
            return Ki.parseLocalDate(text)
        } else if(zoneNode==null) {
            // LocalDateTime
            return Ki.parseLocalDateTime(text)
        } else {
            // ZonedDateTime
            return Ki.parseZonedDateTime(ctx.text)
        }
    }

    private fun makeList(listCtx: KDParser.ListContext) : List<Any?> {
        val list = ArrayList<Any?>()

        for(value in listCtx.value()) {
            list.add(makeValue(value))
        }

        return list
    }

    // TODO: Question - Should KD allow null keys?
    private fun makeMap(mapCtx: KDParser.MapContext) : Map<Any?,Any?> {
        val map = HashMap<Any?,Any?>()

        for(pair in mapCtx.pair()) {
            map.put(makeValue(pair.value(0)), makeValue(pair.value(1)))
        }

        return map
    }

    private fun makeRange(ctx: KDParser.RangeContext): Range<*> {

        val irCtx = ctx.intRange()
        if(irCtx!= null) return makeIntRange(irCtx)

        val lrCtx = ctx.longRange()
        if(lrCtx!= null) return makeLongRange(lrCtx)

        val fCtx = ctx.floatRange()
        if(fCtx!= null) return makeFloatRange(fCtx)

        val dbleCtx = ctx.doubleRange()
        if(dbleCtx!= null) return makeDoubleRange(dbleCtx)

        val decCtx = ctx.decimalRange()
        if(decCtx!= null) return makeDecimalRange(decCtx)

        val drCtx = ctx.durationRange()
        if(drCtx!= null) return makeDurationRange(drCtx)

        val dtCtx = ctx.dateTimeRange()
        if(dtCtx!= null) return makeDateTimeRange(dtCtx)

        val vCtx = ctx.versionRange()
        if(vCtx!= null) return makeVersionRange(vCtx)

        val cCtx = ctx.charRange()
        if(cCtx!= null) return makeCharRange(cCtx)

        val sCtx = ctx.stringRange()
        if(sCtx!= null) return makeStringRange(sCtx)

        throw KDParseException("Uknown type in range", ctx.start.line, ctx.start.charPositionInLine)
    }

    // TODO - Fix - This leaves quotes on strings.
    private fun makeStringRange(ctx: KDParser.StringRangeContext): Range<String> {
        val left = ctx.getChild(0).text
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text
        val openRight = (right == "_")

        return when {
            openLeft -> Range<String>("", ctx.getChild(2).text,
                    op, openLeft, openRight)
            openRight -> Range<String>(ctx.getChild(0).text, "",
                    op, openLeft, openRight)
            else -> Range<String>(ctx.getChild(0).text, ctx.getChild(2).text,
                    op, openLeft, openRight)
        }
    }

    private fun makeCharRange(ctx: KDParser.CharRangeContext): Range<Char> {

        val leftText = ctx.getChild(0).text

        var openLeft = false
        var leftChar = '\u0000'

        if(leftText == "_") {
            openLeft = true
        } else {
            leftChar = leftText[1]
        }

        var op = rangeOp(ctx.rangeOp().text)

        val rightText = ctx.getChild(2).text

        var openRight = false
        var rightChar = '\u0000'

        if(rightText == "_") {
            openRight = true
        } else {
            rightChar = rightText[1]
        }

        return when {
            openLeft -> Range<Char>(Char.MIN_VALUE, ctx.getChild(2).text[1],
                    op, openLeft, openRight)
            openRight -> Range<Char>(ctx.getChild(0).text[1], Char.MAX_VALUE,
                    op, openLeft, openRight)
            else -> Range<Char>(leftChar, rightChar, op, openLeft, openRight)
        }
    }

    private fun makeVersionRange(ctx: KDParser.VersionRangeContext): Range<Version> {
        val left = ctx.getChild(0).text
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text
        val openRight = (right == "_")

        return when {
            openLeft -> Range<Version>(Version.MIN, Version.parse(ctx.getChild(0).text),
                    op, openLeft, openRight)
            openRight -> Range<Version>(Version.parse(ctx.getChild(0).text), Version.MAX,
                    op, openLeft, openRight)
            else -> Range<Version>(Version.parse(ctx.getChild(0).text),
                    Version.parse(ctx.getChild(2).text),
                    op, openLeft, openRight)
        }
    }

    private fun makeDateTimeRange(ctx: KDParser.DateTimeRangeContext): Range<*> {
        val left = ctx.getChild(0).text
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text
        val openRight = (right == "_")

        if(openLeft) {
            val rightDT = makeTemporal(ctx.dateTime(0)!!, right)!!

            return when (rightDT) {
                is LocalDate -> Range<LocalDate>(LocalDate.MIN, rightDT, op, openLeft=true)
                is LocalDateTime -> Range<LocalDateTime>(LocalDateTime.MIN, rightDT, op, openLeft=true)
                is ZonedDateTime -> Range<ZonedDateTime>(ZonedDateTime.from(LocalDateTime.MIN),
                        rightDT, op, openLeft=true)
                else -> throw KDParseException(
                        "Error in DateTime Range calculation. Unknown type ${rightDT.javaClass.simpleName}",
                        ctx.start.line, ctx.start.charPositionInLine)
            }
        } else if(openRight) {
            val leftDT = makeTemporal(ctx.dateTime(0)!!, left)!!

            return when (leftDT) {
                is LocalDate -> Range<LocalDate>(leftDT, LocalDate.MAX, op, openRight=true)
                is LocalDateTime -> Range<LocalDateTime>(leftDT, LocalDateTime.MAX, openRight=true)
                is ZonedDateTime -> Range<ZonedDateTime>(leftDT, ZonedDateTime.from(LocalDateTime.MAX), openRight=true)
                else -> throw KDParseException(
                        "Error in DateTime Range calculation. Unknown type ${leftDT.javaClass.simpleName}",
                        ctx.start.line, ctx.start.charPositionInLine)
            }
        } else {
            val leftDT = makeTemporal(ctx.dateTime(0)!!, left)!!
            val rightDT = makeTemporal(ctx.dateTime(1)!!, right)!!

            return when (leftDT) {
                is LocalDate -> Range<LocalDate>(leftDT, rightDT as LocalDate, op)
                is LocalDateTime -> Range<LocalDateTime>(leftDT, rightDT as LocalDateTime, op)
                is ZonedDateTime -> Range<ZonedDateTime>(leftDT, rightDT as ZonedDateTime, op)
                else -> throw KDParseException(
                        "Error in DateTime Range calculation. Unknown type ${leftDT.javaClass.simpleName}",
                        ctx.start.line, ctx.start.charPositionInLine)
            }
        }
    }

    // TODO - This needs work. It is using Java's duration string format, not KD's
    private fun makeDurationRange(ctx: KDParser.DurationRangeContext): Range<Duration> {
        val left = ctx.getChild(0).text
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text
        val openRight = (right == "_")

        return when {
            openLeft -> Range<Duration>(Duration.ofDays(Long.MIN_VALUE), Duration.parse(ctx.getChild(0).text),
                    op, openLeft, openRight)
            openRight -> Range<Duration>(Duration.parse(ctx.getChild(0).text), Duration.ofDays(Long.MAX_VALUE),
                    op, openLeft, openRight)
            else -> Range<Duration>(Duration.parse(ctx.getChild(0).text),
                    Duration.parse(ctx.getChild(2).text),
                    op, openLeft, openRight)
        }
    }

    private fun makeFloatRange(ctx: KDParser.FloatRangeContext): Range<Float> {
        val leftText = ctx.getChild(0).text
        val leftOpen = (leftText == "_")
        val rightText = ctx.getChild(2).text
        val rightOpen = (rightText == "_")

        val op = rangeOp(ctx.rangeOp().text)

        if(leftOpen) {
            val rightNum = rightText.replace("_", "").toFloat()

            return Range<Float>(Float.MIN_VALUE, rightNum,
                        op, openLeft = true)
        } else if (rightOpen) {
            val leftNum = leftText.replace("_", "").toFloat()

            return Range<Float>(leftNum, Float.MAX_VALUE, op, openLeft = true)
        } else {
            val leftNum = leftText.replace("_", "").toFloat()
            val rightNum = rightText.replace("_", "").toFloat()

            return Range<Float>(leftNum, rightNum, op)
        }
    }

    private fun makeDoubleRange(ctx: KDParser.DoubleRangeContext): Range<Double> {
        val leftText = ctx.getChild(0).text
        val leftOpen = (leftText == "_")
        val rightText = ctx.getChild(2).text
        val rightOpen = (rightText == "_")

        val op = rangeOp(ctx.rangeOp().text)

        if(leftOpen) {
            val rightNum = rightText.replace("_", "").toDouble()

            return Range<Double>(Double.MIN_VALUE, rightNum,
                    op, openLeft = true)
        } else if (rightOpen) {
            val leftNum = leftText.replace("_", "").toDouble()

            return Range<Double>(leftNum, Double.MAX_VALUE, op, openLeft = true)
        } else {
            val leftNum = leftText.replace("_", "").toDouble()
            val rightNum = rightText.replace("_", "").toDouble()

            return Range<Double>(leftNum, rightNum, op)
        }
    }

    private fun makeDecimalRange(ctx: KDParser.DecimalRangeContext): Range<BigDecimal> {
        var leftText = ctx.getChild(0).text
        val leftOpen = (leftText == "_")
        var rightText = ctx.getChild(2).text
        val rightOpen = (rightText == "_")

        if(leftText.last().equals('m', ignoreCase = true))
            leftText = leftText.substring(0, leftText.length-1)
        if(rightText.last().equals('m', ignoreCase = true))
            rightText = rightText.substring(0, rightText.length-1)

        val op = rangeOp(ctx.rangeOp().text)

        if(leftOpen) {
            val rightNum = rightText.replace("_", "").toBigDecimal()

            return Range<BigDecimal>(BigDecimal(Double.MIN_VALUE), rightNum,
                    op, openLeft = true)
        } else if (rightOpen) {
            val leftNum = leftText.replace("_", "").toBigDecimal()

            return Range<BigDecimal>(leftNum, BigDecimal(Double.MAX_VALUE), op, openLeft = true)
        } else {
            val leftNum = leftText.replace("_", "").toBigDecimal()
            val rightNum = rightText.replace("_", "").toBigDecimal()

            return Range<BigDecimal>(leftNum, rightNum, op)
        }
    }

    private fun makeLongRange(ctx: KDParser.LongRangeContext): Range<Long> {
        val left = ctx.getChild(0).text
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text
        val openRight = (right == "_")

        return when {
            openLeft -> Range<Long>(Long.MIN_VALUE, ctx.getChild(2).text.toLong(),
                    op, openLeft, openRight)
            openRight -> Range<Long>(ctx.getChild(0).text.toLong(), Long.MAX_VALUE,
                    op, openLeft, openRight)
            else -> Range<Long>(ctx.getChild(0).text.toLong(), ctx.getChild(2).text.toLong(),
                    op, openLeft, openRight)
        }
    }

    private fun makeIntRange(ctx: KDParser.IntRangeContext) : Range<Int> {
        val left = ctx.getChild(0).text;
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text
        val openRight = (right == "_")

        return when {
            openLeft -> Range<Int>(Int.MIN_VALUE, ctx.getChild(2).text.toInt(),
                    op, openLeft, openRight)
            openRight -> Range<Int>(ctx.getChild(0).text.toInt(), Integer.MAX_VALUE,
                    op, openLeft, openRight)
            else -> Range<Int>(ctx.getChild(0).text.toInt(), ctx.getChild(2).text.toInt(),
                    op, openLeft, openRight)
        }
    }

    private fun rangeOp(rangeOpText:String): Range.Type {
        return when(rangeOpText) {
            ".." -> Range.Type.Inclusive
            "<..<" -> Range.Type.Exclusive
            "..<" -> Range.Type.ExclusiveRight
            "<.." -> Range.Type.ExclusiveLeft
            // this is verified by the lexer, so it can't happen
            else -> throw KDParseException("Unknown range type")

        }
    }

    private fun stripQuotes(value:String): String{
        var text = value

        if(text.startsWith("\"\"\"")) {
            text = text.substring(3, text.length-3)
            if(text.startsWith("\n"))
                text = text.substring(1)
        } else if(text[0] == '"') {
            text = text.substring(1, text.length-1)
        } else if(text[0] == '`') {
            text = text.substring(1, text.length-1)
        }

        return text
    }

    fun read(code:String) : List<Tag> {
        return read(StringReader(code))
    }
}

// TODO: Move to tests
fun main() {
    /*
    var file = Interpreter::class.java.getResource("temp_tests.kd")
    log(KD.read(file))
    */

    val root = KD.read("""
        odds 5 7 9
        23d
        array [1 2 3] [4, 5, 6] # Commas optional
        greet "Aloha" {
            "It works again!"
        }
        data .base64(213)
        12.5.2-beta5
        12..90
        _..<4.5m
        
        doubles {
            100_000.5
            100_000.222_333
        }

        durations {
            1:30:00
            0:15:00
            10:23:53
            10:23:53.234
            10:03:53.002412532
            2days:10:23:03.002412532
            1day
            5days
            423ns
        }
        
        2020/6/5
        2020/7/9@8:02
        2020/8/10@9:02-Z
        2020/9/11@10:02-Z

        2020/5/2
        2020/06/02 @9:00
        2020/7/3@10:30:23
        2020/08/04 @11:00:23.001
        2020/9/5@12:00:23.001392
        2020/10/06 @13:35:23.001_392
        2020/11/7@14:00-9:30
        2020/12/08 @15:40+9:30
        2020/1/9@16:00-Z
        2020/2/10 @17:45-Z
        2020/3/11@18:00-JP/JST
        2020/4/12 @19:50-IN/IST
    """)

    log(root)

    // TODO: Fix broken cases below

    log(KD.read("""
            dan leuck age=48 birthday=1972/5/23 url=http://ikayzo.com {
                kai
                noa {
                    chocolate
                    bata
                }
            }

            x:nums [1 2 3]
            x:map [name="Dan" animal="lemur"]
            
            .base64(123)
            
            ranges {
                0..5  
                5..0  
                0<..<5
                0..<5 
                0<..5 
                0.._  
                _..5  
                0<.._ 
                _..<5    
                
                'a'..'z'
                'b'..<'d'
                _..'g'
                'g'.._
                2f<..<5.24f
                2d..5.24d    

                
                2010/12/25 .. 2020/5/15
                
                4.3-alpha <.. _
                5.1-beta .. 6.4.0
                
                # TODO: Duration
            }
        """.trimIndent()))
}