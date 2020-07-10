package ki.kd

import ki.Range
import ki.Version
import ki.log
import ki.text.ParseException

import java.io.StringReader
import java.math.BigDecimal
import java.net.MalformedURLException
import java.net.URL
import java.time.*
import java.time.format.*

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.Reader

class Interpreter {

    private lateinit var lexer: KDLexer
    private lateinit var parser: KDParser

    fun read(reader:Reader) : List<Tag> {
        lexer = KDLexer(CharStreams.fromReader(reader))
        parser = KDParser(CommonTokenStream(lexer))
        parser.setBuildParseTree(true)

        val tagListCtx = parser.tagList()
        val tags = ArrayList<Tag>()

        if(tagListCtx!=null && tagListCtx.getChildCount() > 0) {
            val childCount = tagListCtx.getChildCount()

            for(i in 0..childCount-1) {
                val child = tagListCtx.getChild(i)
                if(child is KDParser.TagContext) {
                    tags.add(makeTag(child))
                }
            }
        }

        return tags
    }

    private fun makeTag(tree:KDParser.TagContext) : Tag {

        val nsNameCtx = tree.nsName()
        var namespace = ""; var name = ""

        if(nsNameCtx != null) {
            if(nsNameCtx.COLON()!=null) {
                namespace = nsNameCtx.ID(0).getText().trim()
                name = nsNameCtx.ID(1).getText().trim()
            } else {
                name = nsNameCtx.ID(0).getText().trim()
            }
        }

        val tag = Tag(name, namespace);

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
                        attNamespace = attNSNameCtx.ID(0).getText().trim()
                        attName = attNSNameCtx.ID(1).getText().trim()
                    } else {
                        attName = attNSNameCtx.ID(0).getText().trim()
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

        return tag;
    }

    // TODO: Ensure this is dealing only with values. Deal with formatting in toString().
    private fun makeValue(ctx: KDParser.ValueContext) : Any? {

        val text = ctx.getText()

        //// Strings --- ---
        if(ctx.StringLiteral()!=null) return formatString(text) // return "\"$text\""

        // Handles naked strings
        if(ctx.ID() != null) return formatString(text) // "\"$text\"";

        //// Numbers --- ---

        if(ctx.IntegerLiteral()!= null) return Integer.valueOf(text)
        if(ctx.LongLiteral() != null) return text.toLong()
        if(ctx.RealLiteral() != null) return makeRealNumber(ctx.text)

        if(ctx.BinLiteral() != null) return Integer.parseInt(text.substring(2), 2)

        if(ctx.HexLiteral() != null) return Integer.parseInt(text.substring(2), 16)

        //// Booleans --- ---

        if(ctx.TRUE() != null) return true

        if(ctx.FALSE() != null) return false

        //// Other simple types --- ---

        if(ctx.NULL() != null) return null;

        if(ctx.CharLiteral() != null) return "'${text[0]}'"

        if(ctx.URL() != null) {
            try {
                return URL(text);
            } catch (e:MalformedURLException) {
                // already parsed - should never happen
                throw ParseException("Malformed URL", -1, -1, e);
            }
        }

        if(ctx.Version() != null) {
            // String[] ints = text.split("\\.");
            // int major = Integer.parseInt(ints[0]), minor = Integer.parseInt(ints[1]), micro = Integer.parseInt(ints[2]);
            // return new Version(major, minor, micro, ints.length == 4 ? ints[3] : null);
            return Version.parse(text)
        }

        //// TODO: Bin64 --- ---

        if(ctx.bin64() != null) {
            return text // ctx.bin64().BIN64_DATA().text
        }

        //// TODO: Duration --- ---

        //// DateTime --- ---
        // Can be a LocalDate, LocalDateTime or ZonedDateTime

        val dateTimeCtx = ctx.dateTime();

        if(dateTimeCtx!=null) {
            return makeLocalOrZonedDateOrDateTime(dateTimeCtx, text)
        }

        //// TODO: Finish Range (exclusivity and open ended) ---

        if(ctx.range() != null) {
            return makeRange(ctx.range());
        }

        //// List --- ---

        val listCtx = ctx.list();
        if(listCtx!=null) {
            return makeList(listCtx);
        }

        //// Map --- ---

        val mapContext = ctx.map();
        if(mapContext!=null) return makeMap(mapContext);

        val t = ctx.getStart()

        throw KDParseException("Unknown literal type: $text at line ${t.line}, index ${t.charPositionInLine}")
    }

    /**
     * Can be Local or Zoned Date or DateTime
     */
    private fun makeLocalOrZonedDateOrDateTime(ctx: KDParser.DateTimeContext, text:String): Any? {
        val timeNode = ctx.Time();

        if(timeNode==null) {
            return LocalDate.parse(text, KD.LOCAL_DATE_FORMATTER);
        } else {
            val timeWithZone = timeNode.getText().substring(1);
            return makeDateTime(LocalDate.parse(ctx.Date().getText(), KD.LOCAL_DATE_FORMATTER),
                    timeWithZone);
        }
    }

    private fun makeRealNumber(text:String) : Number {
        val lastChar = text.last()

        return when(lastChar) {
            'f', 'F' -> text.toFloat()
            'd', 'D' -> text.toDouble()
            'm', 'M' -> return BigDecimal(text)
            else -> text.toDouble()
        }
    }

    /**
     * Returns a LocalDateTime or ZonedDateTime if the timezone is present
     *
     * TODO: Test, fix "z" zone and other mappings.
     */
    private fun makeDateTime(date: LocalDate, timeWithZone:String) : Any {
        var i=0;

        for(c in timeWithZone) {
            if(c!=':' && c!='.' && c!='_' && !(c>='0' && c<='9')) break;
            i++
        }

        var timeString = timeWithZone.substring(0, i).replace("_", "");
        if(timeString[1]==':')
            timeString = "0" + timeString;
        if(timeString[timeString.length-2]==':')
            timeString = timeString + "0";

        var zone: String = timeWithZone.substring(i);

        if(zone.isEmpty()) {
            // zone = null;
        } else if(zone.startsWith("/")) {
            zone = zone.substring(1);
        }


        val dtformatter = DateTimeFormatter.ISO_LOCAL_TIME;
        val time = LocalTime.parse(timeString, dtformatter)

        if(zone.isEmpty())
            return LocalDateTime.of(date, time);

        return ZonedDateTime.of(date, time, getZone(zone));
    }

    private fun getZone(zoneString:String) : ZoneId {
        val zoneSize = zoneString.length;
        var zone = zoneString

        if(zone.equals("Z", ignoreCase = true)) {
            zone = "UTC";
        } else if(zoneSize>1 && (zoneString[1]=='+' || zoneString[1]=='-')) {
            zone = "UTC" + zoneString.substring(1);
        } else if(zoneString.startsWith("GMT")) {
            zone = "UTC" + zoneString.substring(3);
        }


        if(zone.startsWith("UTC") && zoneSize>3) {
            val colonIndex = zone.indexOf(':');
            if(colonIndex!=-1 && zone.substring(4, colonIndex).length==1) {
                zone = zone.substring(0,4) + "0" + zone.substring(4);
            }
        }

        return ZoneId.of(zone, ZoneId.SHORT_IDS);
    }

    private fun makeList(listCtx:KDParser.ListContext) : List<Any?> {
        val list = ArrayList<Any?>()

        for(value in listCtx.value()) {
            list.add(makeValue(value))
        }

        return list;
    }

    // TODO: Question - Should KD allow null keys?
    private fun makeMap(mapCtx:KDParser.MapContext) : Map<Any?,Any?> {
        val map = HashMap<Any?,Any?>();

        for(pair in mapCtx.pair()) {
            map.put(makeValue(pair.value(0)), makeValue(pair.value(1)));
        }

        return map;
    }

    private fun makeRange(ctx:KDParser.RangeContext): Range<*> {

        val irCtx = ctx.intRange()
        if(irCtx!= null) return makeIntRange(irCtx)

        val lrCtx = ctx.longRange()
        if(lrCtx!= null) return makeLongRange(lrCtx)

        val rrCtx = ctx.realRange()
        if(rrCtx!= null) return makeRealRange(rrCtx)

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

        throw KDParseException("Uknown type in range", ctx.start.charPositionInLine, ctx.start.line)
    }

    private fun makeStringRange(ctx: KDParser.StringRangeContext): Range<String> {
        var left = ctx.getChild(0).text
        var openLeft = (left == "_")
        var op = rangeOp(ctx.rangeOp().text)
        var right = ctx.getChild(2).text
        var openRight = (right == "_")

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

        log("Make char range with " + ctx.text)

        var leftText = ctx.getChild(0).text

        var openLeft = false
        var leftChar = '\u0000'

        if(leftText == "_") {
            openLeft = true
        } else {
            leftChar = leftText[1]
        }

        var op = rangeOp(ctx.rangeOp().text)

        var rightText = ctx.getChild(2).text

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
        var left = ctx.getChild(0).text;
        var openLeft = (left == "_")
        var op = rangeOp(ctx.rangeOp().text)
        var right = ctx.getChild(2).text;
        var openRight = (right == "_")

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
        var left = ctx.getChild(0).text;
        var openLeft = (left == "_")
        var op = rangeOp(ctx.rangeOp().text)
        var right = ctx.getChild(2).text;
        var openRight = (right == "_")

        if(openLeft) {
            var rightDT = makeLocalOrZonedDateOrDateTime(ctx.dateTime(0)!!, right)!!

            return when (rightDT) {
                is LocalDate -> Range<LocalDate>(LocalDate.MIN, rightDT, op, openLeft=true)
                is LocalDateTime -> Range<LocalDateTime>(LocalDateTime.MIN, rightDT, op, openLeft=true)
                is ZonedDateTime -> Range<ZonedDateTime>(ZonedDateTime.from(LocalDateTime.MIN),
                        rightDT, op, openLeft=true)
                else -> throw KDParseException(
                        "Error in DateTime Range calculation. Unknown type ${rightDT.javaClass.simpleName}")
            }
        } else if(openRight) {
            var leftDT = makeLocalOrZonedDateOrDateTime(ctx.dateTime(0)!!, left)!!

            return when (leftDT) {
                is LocalDate -> Range<LocalDate>(leftDT, LocalDate.MAX, op, openRight=true)
                is LocalDateTime -> Range<LocalDateTime>(leftDT, LocalDateTime.MAX, openRight=true)
                is ZonedDateTime -> Range<ZonedDateTime>(leftDT, ZonedDateTime.from(LocalDateTime.MAX), openRight=true)
                else -> throw KDParseException(
                        "Error in DateTime Range calculation. Unknown type ${leftDT.javaClass.simpleName}")
            }
        } else {
            var leftDT = makeLocalOrZonedDateOrDateTime(ctx.dateTime(0)!!, left)!!
            var rightDT = makeLocalOrZonedDateOrDateTime(ctx.dateTime(1)!!, right)!!

            return when (leftDT) {
                is LocalDate -> Range<LocalDate>(leftDT, rightDT as LocalDate, op)
                is LocalDateTime -> Range<LocalDateTime>(leftDT as LocalDateTime, rightDT as LocalDateTime, op)
                is ZonedDateTime -> Range<ZonedDateTime>(leftDT as ZonedDateTime, rightDT as ZonedDateTime, op)
                else -> throw KDParseException(
                        "Error in DateTime Range calculation. Unknown type ${leftDT.javaClass.simpleName}")
            }
        }
    }

    // TODO - This needs work. It is using Java's duration string format, not KD's
    private fun makeDurationRange(ctx: KDParser.DurationRangeContext): Range<Duration> {
        val left = ctx.getChild(0).text;
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text;
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

    private fun makeRealRange(ctx: KDParser.RealRangeContext): Range<*> {
        val leftText = ctx.getChild(0).text
        val leftOpen = (leftText == "_")
        val rightText = ctx.getChild(2).text
        val rightOpen = (rightText == "_")

        val op = rangeOp(ctx.rangeOp().text)

        if(leftOpen) {
            val rightNum = makeRealNumber(rightText)

            return when(rightNum) {
                is Float -> Range<Float>(Float.MIN_VALUE, rightNum as Float,
                    op, openLeft = true)
                is BigDecimal -> Range<BigDecimal>(
                        BigDecimal(Double.MIN_VALUE),
                        rightNum as BigDecimal,
                        op, openLeft = true)
                else -> Range<Double>(Double.MIN_VALUE, rightNum as Double,
                        op, openLeft = true)
            }
        } else if (rightOpen) {
            val leftNum = makeRealNumber(leftText)

            return when(leftNum) {
                is Float -> Range<Float>(leftNum as Float, Float.MAX_VALUE, op, openLeft = true)
                is BigDecimal -> Range<BigDecimal>(leftNum as BigDecimal, BigDecimal(Double.MAX_VALUE), op,
                        openLeft = true)
                else -> Range<Double>(leftNum as Double, Double.MAX_VALUE, op, openLeft = true)
            }
        } else {
            val leftNum = makeRealNumber(leftText)
            val rightNum = makeRealNumber(rightText)

            return when(leftNum) {
                is Float -> Range<Float>(leftNum as Float, rightNum as Float, op)
                is BigDecimal -> Range<BigDecimal>(leftNum as BigDecimal, rightNum as BigDecimal, op)
                else -> Range<Double>(leftNum as Double, rightNum as Double, op)
            }
        }
    }

    private fun makeLongRange(ctx: KDParser.LongRangeContext): Range<Long> {
        var left = ctx.getChild(0).text;
        var openLeft = (left == "_")
        var op = rangeOp(ctx.rangeOp().text)
        var right = ctx.getChild(2).text;
        var openRight = (right == "_")

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
        var left = ctx.getChild(0).text;
        var openLeft = (left == "_")
        var op = rangeOp(ctx.rangeOp().text)
        var right = ctx.getChild(2).text;
        var openRight = (right == "_")

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
            else -> throw KDParseException("Unknown range type") // will never happen
        }
    }

    private fun formatString(value:String): String{
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

        return text;
    }

    public fun read(code:String) : List<Tag> {
        return read(StringReader(code))
    }
}

// TODO: Move to tests area
fun main() {

    /*
    var file = Interpreter::class.java.getResource("temp_tests.kd")
    log(KD.read(file))
    */

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
            
            // .bin64(123) // TODO: Make this work!
            
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
                # 2d..5.24d    

                
                2010/12/25 .. 2020/5/15
                
                # 4.3.alpha <.. _
                # 5.1.beta .. 6.4.0
                
                # TODO: Duration
            }
        """.trimIndent()))
}

