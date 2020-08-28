// Generated from /Users/danielleuck/projects/Ki/JVM/Ki.KD-JVM/src/main/kotlin/io/kixi/kd/KDParser.g4 by ANTLR 4.8
package io.kixi.kd.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link KDParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface KDParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link KDParser#tagList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagList(KDParser.TagListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#tag}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTag(KDParser.TagContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(KDParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(KDParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#blockString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockString(KDParser.BlockStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#blockRawString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockRawString(KDParser.BlockRawStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#blockRawAltString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockRawAltString(KDParser.BlockRawAltStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#duration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDuration(KDParser.DurationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#quantity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantity(KDParser.QuantityContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#rangeOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeOp(KDParser.RangeOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#intRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntRange(KDParser.IntRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#longRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongRange(KDParser.LongRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#floatRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatRange(KDParser.FloatRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#doubleRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleRange(KDParser.DoubleRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#decimalRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalRange(KDParser.DecimalRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#durationRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDurationRange(KDParser.DurationRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#dateTimeRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTimeRange(KDParser.DateTimeRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#versionRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersionRange(KDParser.VersionRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#charRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharRange(KDParser.CharRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#stringRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringRange(KDParser.StringRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#quantityRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantityRange(KDParser.QuantityRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(KDParser.RangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#blob}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlob(KDParser.BlobContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#valueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueList(KDParser.ValueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(KDParser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#attributeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeList(KDParser.AttributeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#nsName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNsName(KDParser.NsNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(KDParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#pair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair(KDParser.PairContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#map}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMap(KDParser.MapContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(KDParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#annotationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationList(KDParser.AnnotationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link KDParser#dateTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateTime(KDParser.DateTimeContext ctx);
}