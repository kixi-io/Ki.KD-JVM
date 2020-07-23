// Generated from /Users/danielleuck/projects/Ki/Kotlin/Ki.Kotlin.KD/src/ki/kd/KDParser.g4 by ANTLR 4.8
package ki.kd;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KDParser}.
 */
public interface KDParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KDParser#tagList}.
	 * @param ctx the parse tree
	 */
	void enterTagList(KDParser.TagListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#tagList}.
	 * @param ctx the parse tree
	 */
	void exitTagList(KDParser.TagListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#tag}.
	 * @param ctx the parse tree
	 */
	void enterTag(KDParser.TagContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#tag}.
	 * @param ctx the parse tree
	 */
	void exitTag(KDParser.TagContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(KDParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(KDParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#duration}.
	 * @param ctx the parse tree
	 */
	void enterDuration(KDParser.DurationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#duration}.
	 * @param ctx the parse tree
	 */
	void exitDuration(KDParser.DurationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#rangeOp}.
	 * @param ctx the parse tree
	 */
	void enterRangeOp(KDParser.RangeOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#rangeOp}.
	 * @param ctx the parse tree
	 */
	void exitRangeOp(KDParser.RangeOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#intRange}.
	 * @param ctx the parse tree
	 */
	void enterIntRange(KDParser.IntRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#intRange}.
	 * @param ctx the parse tree
	 */
	void exitIntRange(KDParser.IntRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#longRange}.
	 * @param ctx the parse tree
	 */
	void enterLongRange(KDParser.LongRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#longRange}.
	 * @param ctx the parse tree
	 */
	void exitLongRange(KDParser.LongRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#floatRange}.
	 * @param ctx the parse tree
	 */
	void enterFloatRange(KDParser.FloatRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#floatRange}.
	 * @param ctx the parse tree
	 */
	void exitFloatRange(KDParser.FloatRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#doubleRange}.
	 * @param ctx the parse tree
	 */
	void enterDoubleRange(KDParser.DoubleRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#doubleRange}.
	 * @param ctx the parse tree
	 */
	void exitDoubleRange(KDParser.DoubleRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#decimalRange}.
	 * @param ctx the parse tree
	 */
	void enterDecimalRange(KDParser.DecimalRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#decimalRange}.
	 * @param ctx the parse tree
	 */
	void exitDecimalRange(KDParser.DecimalRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#durationRange}.
	 * @param ctx the parse tree
	 */
	void enterDurationRange(KDParser.DurationRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#durationRange}.
	 * @param ctx the parse tree
	 */
	void exitDurationRange(KDParser.DurationRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#dateTimeRange}.
	 * @param ctx the parse tree
	 */
	void enterDateTimeRange(KDParser.DateTimeRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#dateTimeRange}.
	 * @param ctx the parse tree
	 */
	void exitDateTimeRange(KDParser.DateTimeRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#versionRange}.
	 * @param ctx the parse tree
	 */
	void enterVersionRange(KDParser.VersionRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#versionRange}.
	 * @param ctx the parse tree
	 */
	void exitVersionRange(KDParser.VersionRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#charRange}.
	 * @param ctx the parse tree
	 */
	void enterCharRange(KDParser.CharRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#charRange}.
	 * @param ctx the parse tree
	 */
	void exitCharRange(KDParser.CharRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#stringRange}.
	 * @param ctx the parse tree
	 */
	void enterStringRange(KDParser.StringRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#stringRange}.
	 * @param ctx the parse tree
	 */
	void exitStringRange(KDParser.StringRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(KDParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(KDParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#valueList}.
	 * @param ctx the parse tree
	 */
	void enterValueList(KDParser.ValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#valueList}.
	 * @param ctx the parse tree
	 */
	void exitValueList(KDParser.ValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(KDParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(KDParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#attributeList}.
	 * @param ctx the parse tree
	 */
	void enterAttributeList(KDParser.AttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#attributeList}.
	 * @param ctx the parse tree
	 */
	void exitAttributeList(KDParser.AttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#nsName}.
	 * @param ctx the parse tree
	 */
	void enterNsName(KDParser.NsNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#nsName}.
	 * @param ctx the parse tree
	 */
	void exitNsName(KDParser.NsNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(KDParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(KDParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(KDParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(KDParser.PairContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#map}.
	 * @param ctx the parse tree
	 */
	void enterMap(KDParser.MapContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#map}.
	 * @param ctx the parse tree
	 */
	void exitMap(KDParser.MapContext ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#bin64}.
	 * @param ctx the parse tree
	 */
	void enterBin64(KDParser.Bin64Context ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#bin64}.
	 * @param ctx the parse tree
	 */
	void exitBin64(KDParser.Bin64Context ctx);
	/**
	 * Enter a parse tree produced by {@link KDParser#dateTime}.
	 * @param ctx the parse tree
	 */
	void enterDateTime(KDParser.DateTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KDParser#dateTime}.
	 * @param ctx the parse tree
	 */
	void exitDateTime(KDParser.DateTimeContext ctx);
}