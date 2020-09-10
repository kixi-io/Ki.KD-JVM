package io.kixi.kd
import io.kixi.Ki
import io.kixi.Range
import io.kixi.Version
import io.kixi.kd.antlr.KDLexer
import io.kixi.kd.antlr.KDParser
import io.kixi.log
import io.kixi.text.ParseException
import io.kixi.text.resolveEscapes
import io.kixi.uom.Quantity
import io.kixi.uom.Unit
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
 * A [Ki Declarative (KD)](https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD))
 * interpreter utilizing an [ANTLR4](https://www.antlr.org/) grammar. Many thanks to
 * [Terrance Parr](https://parrt.cs.usfca.edu/) for his superb parser generator and books.
 *
 * This implementation of KD implements all features and passes all tests.
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
            log("${vocab.getSymbolicName(token.type)}")
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

        // Add Annotations ////

        val annotationCtx = tree.annotationList()
        if(annotationCtx != null) {
            for(anno in annotationCtx.annotation()) {
                tag.annotations.add(makeAnnotation(anno))
            }
        }

        // Add Values ////

        val valuesCtx = tree.valueList()

        if(valuesCtx != null) {
            for(vc in valuesCtx.value()) {
                tag.values.add(makeValue(vc))
            }
        }

        // Add Attributes ////

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

    private fun makeAnnotation(annoCtx: KDParser.AnnotationContext?): Annotation {
        val nsNameCtx = annoCtx!!.nsName()
        var namespace = ""; var name = ""

        if(nsNameCtx != null) {
            if(nsNameCtx.COLON()!=null) {
                namespace = nsNameCtx.ID(0).text.trim()
                name = nsNameCtx.ID(1).text.trim()
            } else {
                name = nsNameCtx.ID(0).text.trim()
            }
        }

        val anno = Annotation(name, namespace)

        val valuesCtx = annoCtx.valueList()

        // Add Values

        if(valuesCtx != null) {
            for(vc in valuesCtx.value()) {
                anno.values.add(makeValue(vc))
            }
        }

        // Add Attributes ////

        val attsCtx = annoCtx.attributeList()

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

                anno.setAttribute(attName, attNamespace, makeValue(att.value()))
            }
        }

        return anno
    }

    // TODO: Ensure this is dealing only with values. Deal with formatting in toString().
    private fun makeValue(ctx: KDParser.ValueContext) : Any? {

        val text = ctx.getText()

        //// Strings --- ---
        if(ctx.stringLiteral()!=null) {
            return makeString(ctx.stringLiteral())
        }

        // Nakes strings (IDs treated as strings)
        if(ctx.ID() != null) {
            return text
        }

        //// Numbers --- ---

        if(ctx.IntegerLiteral() != null) return Integer.valueOf(text.replace("_", ""))

        if(ctx.LongLiteral() != null) {
            var ntext = text.replace("_", "")
            if(ntext.last().equals('L', ignoreCase = true)) ntext = ntext.dropLast(1)
            return ntext.toLong()
        }
        if(ctx.FloatLiteral() !=null) {
            var ntext = text.replace("_", "")
            if(ntext.last().equals('F', ignoreCase = true)) ntext = ntext.dropLast(1)
            return ntext.toFloat()
        }
        if(ctx.DecimalLiteral() !=null) {
            var decText = text.replace("_", "")
            if(decText.endsWith("bd", ignoreCase = true)) decText = decText.dropLast(2)
            return decText.toBigDecimal()
        }
        if(ctx.DoubleLiteral() != null) {
            var ntext = text.replace("_", "")
            if(ntext.last().equals('D', ignoreCase = true)) ntext = ntext.dropLast(1)
            return ntext.toDouble()
        }
        if(ctx.BinLiteral() != null) {
            if(text[0] == '-') {
                return -Integer.parseInt(text.replace("_", "")
                        .substring(3), 2)
            } else {
                return Integer.parseInt(text.replace("_", "")
                        .substring(2), 2)
            }
        }

        if(ctx.HexLiteral() != null) {
            if(text[0] == '-') {
                return -Integer.parseInt(text.replace("_", "")
                        .substring(3), 16)
            } else {
                return Integer.parseInt(text.replace("_", "")
                        .substring(2), 16)
            }
        }

        //// Booleans --- ---

        if(ctx.TRUE() != null) return true

        if(ctx.FALSE() != null) return false

        //// Other simple types --- ---

        if(ctx.NULL() != null) return null

        if(ctx.CharLiteral() != null) {
            if(text[1]!='\\') {
                return text[1]
            } else if(text.length>3)  {
                // We have an escaped char
                when (text[2]) {
                    '\\' -> return '\\'
                    '\'' -> return '\''
                    't' -> return '\t'
                    'r' -> return '\r'
                    'n' -> return '\n'
                }
            }

            throw ParseException("Malformed Char literal at line ${ctx.start.line}, " +
                "index ${ctx.start.charPositionInLine}: $text")
        } // escapes

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
            try {
                return Version.parse(text)
            } catch(pe:ParseException) {
                throw KDParseException("Malformed version ${ctx.text}", line = ctx.start.line,
                        index = ctx.start.charPositionInLine, pe)
            }
        }

        if(ctx.blob() != null) {
            try {
                return Ki.parseBlob(ctx.blob().text)
            } catch(pe:ParseException) {
                throw KDParseException("Malformed blob ${ctx.text}", line = ctx.start.line,
                        index = ctx.start.charPositionInLine, pe)
            }
        }

        //// Duration --- ---

        val durationCtx = ctx.duration()
        if (durationCtx!=null) {
            return makeDuration(text)
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

        if(ctx.quantity() != null) {
            try {
                return Quantity.parse(text)
            } catch(pe:ParseException) {
                throw KDParseException("Malformed quantity ${ctx.text}", line = ctx.start.line,
                        index = ctx.start.charPositionInLine, pe)
            }
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

        throw KDParseException("Unknown literal type ${ctx.text}.", t.line, t.charPositionInLine)
    }

    private fun makeString(parentCtx: KDParser.StringLiteralContext): String {
        var text = parentCtx.text

        when {
            parentCtx.SimpleString()!=null -> {
                text = text.substring(1, text.length-1).resolveEscapes()
            }
            parentCtx.RawString()!=null -> {
                text = text.substring(2, text.length-1)
            }
            parentCtx.blockString()!=null -> {
                text = trimStringBlockLinePrefixesAndNewLines(
                        text.substring(3, text.length-3).resolveEscapes()
                )
            }
            parentCtx.blockRawString()!=null -> {
                text = trimStringBlockLinePrefixesAndNewLines(
                        text.substring(4, text.length-3)
                )
            }
            parentCtx.blockRawAltString()!=null -> {
                text = trimStringBlockLinePrefixesAndNewLines(
                        text.substring(1, text.length-1)
                )
            }
            else -> throw ParseException("Unkown String literal type") // should never happen
        }

        return text
    }

    /**
     * KD String Blocks trim the beginning of each line to match the indentation of the
     * closing quotes. Additionally, new lines are removed from the beginning and end
     * of strings to allow for breaking immediately after the opening string block quotes
     * and before the ending block quotes.
     *
     * ### Example
     * ```
     * var text = """
     *     Lorem ipsum dolor sit amet,
     *         consectetur adipiscing elit,
     *     sed do eiusmod tempor incididunt
     *     """
     *  ```
     *
     *  This `text` string's closing quotes (`"""`) are prefixed with four spaces. These
     *  four spaces are removed from the beginning of each line. Additionally the new line
     *  after the opening `"""` and the closing `"""` are removed. The resulting String is
     *  ```
     *  Lorem ipsum dolor sit amet,
     *      consectetur adipiscing elit,
     *  sed do eiusmod tempor incididunt
     *  ```
     *
     *  Note that the four space indent in the second line is preserved. See
     *  [KD Strings](https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD)#String) for more
     *  details.
     */
    fun trimStringBlockLinePrefixesAndNewLines(text:String) : String {

        // TODO: When indent is greater than line start, numbers replace some lines

        if(text.isEmpty() || !text.contains("\n"))
            return text

        // remove newlines from beginning and end
        var trimmedText = text

        if(trimmedText.startsWith('\n'))
            trimmedText = trimmedText.substring(1)


        val lines = trimmedText.lines().toMutableList()
        val lastLine = lines.last()

        if(lastLine.isBlank()) {

            // find the ws prefix of the last line
            var wsEnd = 0
            for (c in lastLine) {
                if (!c.isWhitespace())
                    break
                wsEnd++
            }

            if (wsEnd != 0 || lastLine.isEmpty()) {
                val wsPrefix = lastLine.substring(0, wsEnd)
                lines.removeAt(lines.size-1)

                val buf = StringBuilder()
                for (i in 0..lines.size - 1) {
                    buf.append(lines[i].removePrefix(wsPrefix))
                    if (i != lines.size - 1)
                        buf.append('\n')
                }

                trimmedText = buf.toString()
            }
        }
        return trimmedText
    }

    /**
     * Make single unit or compound Duration
     *
     * @param text The flattened string for the Duration node
     */
    private fun makeDuration(text: String): Duration {
        return Ki.parseDuration(text)
    }

    /**
     * Can be Date, LocalDateTime, or ZonedDateTime
     */
    private fun makeTemporal(ctx: KDParser.DateTimeContext, text:String): Any? {

        val timeNode = ctx.Time()
        // val zoneNode = ctx.TimeZone()

        if(timeNode==null) {
            // LocalDate
            return Ki.parseLocalDate(text.replace('-', '/'))
        // } else if(zoneNode==null) {
            // LocalDateTime
        //    return Ki.parseLocalDateTime(text)
        } else {
            val dateText = ctx.Date().text.replace('-', '/')
            val timeText = ctx.Time().text

            // Needed for ISO 8601 support
            var convertedTimeText = if (timeText.last()=='Z' && timeText[timeText.length-2].isDigit())
                timeText.dropLast(1) + "-Z" else timeText

            if(convertedTimeText.first()=='T') {
                convertedTimeText = "@" + convertedTimeText.substring(1)
            }
            val newText = "${dateText}${convertedTimeText}"

            try {
                if(convertedTimeText.contains(REGEX_PLUS_MINUS))
                    return Ki.parseZonedDateTime(newText)
                else
                    return Ki.parseLocalDateTime(newText)
            } catch(pe:ParseException) {
                throw KDParseException("Malformed DateTime $newText", line = ctx.start.line,
                        index = ctx.start.charPositionInLine, pe)
            }
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
            map[makeValue(pair.value(0))] = makeValue(pair.value(1))
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

        val qCtx = ctx.quantityRange()
        if(qCtx!= null) return makeQuantityRange(qCtx)

        throw KDParseException("Uknown type in range", ctx.start.line, ctx.start.charPositionInLine)
    }

    // TODO - Tests!
    private fun makeStringRange(ctx: KDParser.StringRangeContext): Range<String> {
        val left = makeString(ctx.stringLiteral(0))
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = makeString(ctx.stringLiteral(1))
        val openRight = (right == "_")

        return when {
            openLeft -> Range<String>("", right,
                    op, openLeft, openRight)
            openRight -> Range<String>(left, "",
                    op, openLeft, openRight)
            else -> Range<String>(left, right,
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

        val op = rangeOp(ctx.rangeOp().text)

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

        try {
            return when {
                openLeft -> Range<Version>(Version.MIN, Version.parse(ctx.getChild(0).text),
                        op, openLeft, openRight)
                openRight -> Range<Version>(Version.parse(ctx.getChild(0).text), Version.MAX,
                        op, openLeft, openRight)
                else -> Range<Version>(Version.parse(ctx.getChild(0).text),
                        Version.parse(ctx.getChild(2).text),
                        op, openLeft, openRight)
            }
        } catch(pe:ParseException) {
            throw KDParseException("Malformed version range ${ctx.text}", line = ctx.start.line,
                    index = ctx.start.charPositionInLine, pe)
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

        try {
            return when {
                openLeft -> Range<Duration>(Duration.ofDays(Long.MIN_VALUE), Ki.parseDuration(ctx.getChild(0).text),
                        op, openLeft, openRight)
                openRight -> Range<Duration>(Ki.parseDuration(ctx.getChild(0).text), Duration.ofDays(Long.MAX_VALUE),
                        op, openLeft, openRight)
                else -> Range<Duration>(Ki.parseDuration(ctx.getChild(0).text),
                        Ki.parseDuration(ctx.getChild(2).text),
                        op, openLeft, openRight)
            }
        } catch(pe:ParseException) {
            throw KDParseException("Malformed duration range ${ctx.text}", line = ctx.start.line,
                    index = ctx.start.charPositionInLine, pe)
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

        if(leftText.endsWith("bd", ignoreCase = true))
            leftText = leftText.substring(0, leftText.length-2)
        if(rightText.endsWith("bd", ignoreCase = true))
            rightText = rightText.substring(0, rightText.length-2)

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
        val left = ctx.getChild(0).text
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

    private fun makeQuantityRange(ctx: KDParser.QuantityRangeContext) : Range<Quantity<Unit>> {
        val left = ctx.getChild(0).text
        val openLeft = (left == "_")
        val op = rangeOp(ctx.rangeOp().text)
        val right = ctx.getChild(2).text
        val openRight = (right == "_")

        try {
            return when {
                openLeft -> {
                    val bound = Quantity.parse(ctx.getChild(2).text) as Quantity<Unit>
                    Range<Quantity<Unit>>(bound, bound, op, openLeft, openRight)
                }
                openRight -> {
                    val bound = Quantity.parse(ctx.getChild(0).text) as Quantity<Unit>
                    Range<Quantity<Unit>>(bound, bound, op, openLeft, openRight)
                }
                else -> Range<Quantity<Unit>>(
                        Quantity.parse(ctx.getChild(0).text) as Quantity<Unit>,
                        Quantity.parse(ctx.getChild(2).text) as Quantity<Unit>,
                            op, openLeft, openRight)
            }
        } catch(pe:ParseException) {
            throw KDParseException("Malformed quantity range ${ctx.text}", line = ctx.start.line,
                    index = ctx.start.charPositionInLine, pe)
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

    fun read(code:String) : List<Tag> {
        return read(StringReader(code))
    }

    companion object {
        val REGEX_PLUS_MINUS = Regex("[+-]")
    }
}