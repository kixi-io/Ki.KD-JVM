package ki.kd;

import ki.Version;
import ki.text.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static ki.LogKt.log;

public class JavaInterp {

    public List<Tag> read(Reader reader) throws IOException {
        var lexer = new KDLexer(CharStreams.fromReader(reader));
        var parser = new KDParser(new CommonTokenStream(lexer));
        parser.setBuildParseTree(true);

        var tagListCtx = parser.tagList();
        var tags = new ArrayList<Tag>();

        if(tagListCtx == null || tagListCtx.tag().size() == 0)
            return tags;

        var childCount = tagListCtx.tag().size();

        log("Child count: $childCount");
        // log("Child count 2: ${tagListCtx.tag().size}")
        // log()

        for(var child : tagListCtx.tag()) {
            if(child instanceof KDParser.TagContext) {
                tags.add(makeTag(child));
            }
        }

        return tags;
    }

    private Tag makeTag(KDParser.TagContext tree) {
        var nsNameCtx = tree.nsName();
        var namespace = "";
        var name = "";

        if (nsNameCtx != null) {
            if (nsNameCtx.COLON() != null) {
                namespace = nsNameCtx.ID(0).getText().trim();
                name = nsNameCtx.ID(1).getText().trim();
            } else {
                name = nsNameCtx.ID(0).getText().trim();
            }
        }

        log("Got tag name $name");

        var tag = new Tag(name, namespace);

        var valuesCtx = tree.valueList();

        log("Value count " + valuesCtx.value().size());

        if(valuesCtx != null) {
            for(var vc : valuesCtx.value()) {
                log("Parsing value " + vc.getText());
                // TODO: What? Why are integer literals being treated like decimals?
                // log("Decimal literal: ${vc.DecimalLiteral().text}")

                tag.getValues().add(makeValue(vc));
            }
        }

        /*
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
        */

        // Adding child tags
        var tagListContext = tree.tagList();

        if(tagListContext!=null) {
            var tagContexts  = tagListContext.tag();
            for (var tagContext : tagContexts) {
                tag.getChildren().add(makeTag(tagContext));
            }
        }

        return tag;
    }

    private Object makeValue(KDParser.ValueContext ctx) {

        var text = ctx.getText();

        //// Strings --- ---
        if(ctx.StringLiteral()!=null) return formatString(text);

        // Handles naked strings
        if(ctx.ID() != null) return formatString(text);

        //// Numbers --- ---

        if(ctx.IntegerLiteral()!= null) return Integer.valueOf(text.replace("_", ""));
        if(ctx.LongLiteral() != null) return Long.valueOf(text.replace("_", ""));

        if(ctx.DoubleLiteral() != null) return Double.valueOf(text.replace("_", ""));
        if(ctx.FloatLiteral() !=null) return Float.valueOf(text.replace("_", ""));
        if(ctx.DecimalLiteral() !=null) {
            var decText = text.replace("_", "");
            var lastChar = decText.charAt(decText.length()-1);
            if(lastChar=='m' || lastChar=='M') decText = decText.substring(0, decText.length()-1);
            return new BigDecimal(decText);
        }

        if(ctx.BinLiteral() != null) return Integer.parseInt(text.replace("_", "")
                .substring(2), 2);

        if(ctx.HexLiteral() != null) return Integer.parseInt(text.replace("_", "")
                .substring(2), 16);

        //// Booleans --- ---

        if(ctx.TRUE() != null) return true;

        if(ctx.FALSE() != null) return false;

        //// Other simple types --- ---

        if(ctx.NULL() != null) return null;

        if(ctx.CharLiteral() != null) return text.charAt(0);

        if(ctx.URL() != null) {
            try {
                return new URL(text);
            } catch (MalformedURLException e) {
                // already parsed - should never happen
                throw new ParseException("Malformed URL", -1, -1, e);
            }
        }

        if(ctx.Version() != null) {
            // String[] ints = text.split("\\.");
            // int major = Integer.parseInt(ints[0]), minor = Integer.parseInt(ints[1]), micro = Integer.parseInt(ints[2]);
            // return new Version(major, minor, micro, ints.length == 4 ? ints[3] : null);
            return Version.Companion.parse(text, '.');
        }

        //// TODO: Duration --- ---

        //// DateTime --- ---
        // Can be a LocalDate, LocalDateTime or ZonedDateTime

        /*
        val dateTimeCtx = ctx.dateTime()

        if(dateTimeCtx!=null) {
            return makeLocalOrZonedDateOrDateTime(dateTimeCtx, text)
        }

        //// TODO: Finish Range (exclusivity and open ended) ---

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

        val t = ctx.getStart()
         */

        throw new KDParseException("Unknown value type", -1, -1, null);
    }

    private String formatString(String value) {
        var text = value;

        if(text.startsWith("\"\"\"")) {
            text = text.substring(3, text.length()-3);
            if(text.startsWith("\n"))
                text = text.substring(1);
        } else if(text.charAt(0) == '"') {
            text = text.substring(1, text.length()-1);
        } else if(text.charAt(0) == '`') {
            text = text.substring(1, text.length()-1);
        }

        return text;
    }

    public static void main(String[] args) throws Exception {
        var root = KD.read("greeting \"hello\"");
        log(root);
    }
}
