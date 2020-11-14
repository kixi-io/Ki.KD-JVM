// Generated from /Users/danielleuck/projects/Ki/JVM/Ki.KD-JVM/src/main/kotlin/io/kixi/kd/KDParser.g4 by ANTLR 4.8
package io.kixi.kd.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class KDParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NULL=1, TRUE=2, FALSE=3, URL=4, FloatLiteral=5, DoubleLiteral=6, DecimalLiteral=7, 
		IntegerLiteral=8, HexLiteral=9, BinLiteral=10, LongLiteral=11, Version=12, 
		SimpleString=13, RawString=14, BlockStringStart=15, BlockRawStringStart=16, 
		BlockRawAltStringStart=17, CharLiteral=18, CompoundDuration=19, DayDuration=20, 
		HourDuration=21, MinuteDuration=22, SecondDuration=23, MillisecondDuration=24, 
		NanosecondDuration=25, Date=26, Time=27, IntegerQuantityLiteral=28, DecimalQuantityLiteral=29, 
		InclusiveRangeOp=30, ExclusiveRangeOp=31, ExclusiveLeftOp=32, ExclusiveRightOp=33, 
		DOT=34, COLON=35, SEMICOLON=36, EQUALS=37, OPEN=38, CLOSE=39, LPAREN=40, 
		RPAREN=41, LSQUARE=42, RSQUARE=43, COMMA=44, SLASH=45, DASH=46, AT=47, 
		PLUS=48, UNDERSCORE=49, BLOB_START=50, ID=51, BlockComment=52, LineComment=53, 
		WS=54, NL=55, BLOB_DATA=56, BLOB_END=57, BLOB_ERROR=58, BlockStringChunk=59, 
		BlockStringEnd=60, BlockStringError=61, BlockRawStringChunk=62, BlockRawStringEnd=63, 
		BlockRawStringError=64, BlockRawAltStringChunk=65, BlockRawAltStringEnd=66;
	public static final int
		RULE_tagList = 0, RULE_tag = 1, RULE_value = 2, RULE_stringLiteral = 3, 
		RULE_blockString = 4, RULE_blockRawString = 5, RULE_blockRawAltString = 6, 
		RULE_duration = 7, RULE_quantity = 8, RULE_rangeOp = 9, RULE_intRange = 10, 
		RULE_longRange = 11, RULE_floatRange = 12, RULE_doubleRange = 13, RULE_decimalRange = 14, 
		RULE_durationRange = 15, RULE_dateTimeRange = 16, RULE_versionRange = 17, 
		RULE_charRange = 18, RULE_stringRange = 19, RULE_quantityRange = 20, RULE_range = 21, 
		RULE_blob = 22, RULE_valueList = 23, RULE_attribute = 24, RULE_attributeList = 25, 
		RULE_nsName = 26, RULE_list = 27, RULE_pair = 28, RULE_map = 29, RULE_callValueList = 30, 
		RULE_callPair = 31, RULE_callAttributeList = 32, RULE_call = 33, RULE_annotation = 34, 
		RULE_annotationList = 35, RULE_dateTime = 36;
	private static String[] makeRuleNames() {
		return new String[] {
			"tagList", "tag", "value", "stringLiteral", "blockString", "blockRawString", 
			"blockRawAltString", "duration", "quantity", "rangeOp", "intRange", "longRange", 
			"floatRange", "doubleRange", "decimalRange", "durationRange", "dateTimeRange", 
			"versionRange", "charRange", "stringRange", "quantityRange", "range", 
			"blob", "valueList", "attribute", "attributeList", "nsName", "list", 
			"pair", "map", "callValueList", "callPair", "callAttributeList", "call", 
			"annotation", "annotationList", "dateTime"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "'@\"\"\"'", null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "'..'", "'<..<'", "'<..'", 
			"'..<'", "'.'", "':'", "';'", "'='", "'{'", "'}'", "'('", null, "'['", 
			"']'", "','", "'/'", "'-'", "'@'", "'+'", "'_'", "'.blob('"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NULL", "TRUE", "FALSE", "URL", "FloatLiteral", "DoubleLiteral", 
			"DecimalLiteral", "IntegerLiteral", "HexLiteral", "BinLiteral", "LongLiteral", 
			"Version", "SimpleString", "RawString", "BlockStringStart", "BlockRawStringStart", 
			"BlockRawAltStringStart", "CharLiteral", "CompoundDuration", "DayDuration", 
			"HourDuration", "MinuteDuration", "SecondDuration", "MillisecondDuration", 
			"NanosecondDuration", "Date", "Time", "IntegerQuantityLiteral", "DecimalQuantityLiteral", 
			"InclusiveRangeOp", "ExclusiveRangeOp", "ExclusiveLeftOp", "ExclusiveRightOp", 
			"DOT", "COLON", "SEMICOLON", "EQUALS", "OPEN", "CLOSE", "LPAREN", "RPAREN", 
			"LSQUARE", "RSQUARE", "COMMA", "SLASH", "DASH", "AT", "PLUS", "UNDERSCORE", 
			"BLOB_START", "ID", "BlockComment", "LineComment", "WS", "NL", "BLOB_DATA", 
			"BLOB_END", "BLOB_ERROR", "BlockStringChunk", "BlockStringEnd", "BlockStringError", 
			"BlockRawStringChunk", "BlockRawStringEnd", "BlockRawStringError", "BlockRawAltStringChunk", 
			"BlockRawAltStringEnd"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "KDParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public KDParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class TagListContext extends ParserRuleContext {
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public List<TagContext> tag() {
			return getRuleContexts(TagContext.class);
		}
		public TagContext tag(int i) {
			return getRuleContext(TagContext.class,i);
		}
		public TagListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterTagList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitTagList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitTagList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagListContext tagList() throws RecognitionException {
		TagListContext _localctx = new TagListContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_tagList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(74);
				match(NL);
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << URL) | (1L << FloatLiteral) | (1L << DoubleLiteral) | (1L << DecimalLiteral) | (1L << IntegerLiteral) | (1L << HexLiteral) | (1L << BinLiteral) | (1L << LongLiteral) | (1L << Version) | (1L << SimpleString) | (1L << RawString) | (1L << BlockStringStart) | (1L << BlockRawStringStart) | (1L << BlockRawAltStringStart) | (1L << CharLiteral) | (1L << CompoundDuration) | (1L << DayDuration) | (1L << HourDuration) | (1L << MinuteDuration) | (1L << SecondDuration) | (1L << MillisecondDuration) | (1L << NanosecondDuration) | (1L << Date) | (1L << IntegerQuantityLiteral) | (1L << DecimalQuantityLiteral) | (1L << LSQUARE) | (1L << AT) | (1L << UNDERSCORE) | (1L << BLOB_START) | (1L << ID))) != 0)) {
				{
				{
				setState(80);
				tag();
				setState(84);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(81);
					match(NL);
					}
					}
					setState(86);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TagContext extends ParserRuleContext {
		public AttributeListContext attributeList() {
			return getRuleContext(AttributeListContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(KDParser.SEMICOLON, 0); }
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public NsNameContext nsName() {
			return getRuleContext(NsNameContext.class,0);
		}
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public TerminalNode OPEN() { return getToken(KDParser.OPEN, 0); }
		public TagListContext tagList() {
			return getRuleContext(TagListContext.class,0);
		}
		public TerminalNode CLOSE() { return getToken(KDParser.CLOSE, 0); }
		public AnnotationListContext annotationList() {
			return getRuleContext(AnnotationListContext.class,0);
		}
		public TagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitTag(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagContext tag() throws RecognitionException {
		TagContext _localctx = new TagContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_tag);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				{
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AT) {
					{
					setState(92);
					annotationList();
					setState(96);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NL) {
						{
						{
						setState(93);
						match(NL);
						}
						}
						setState(98);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(101);
				nsName();
				setState(103);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(102);
					valueList();
					}
					break;
				}
				}
				}
				break;
			case 2:
				{
				{
				setState(105);
				valueList();
				}
				}
				break;
			}
			setState(109);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(108);
				attributeList();
				}
				break;
			}
			setState(117);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(111);
				match(SEMICOLON);
				}
				break;
			case 2:
				{
				setState(112);
				match(NL);
				}
				break;
			case 3:
				{
				{
				setState(113);
				match(OPEN);
				setState(114);
				tagList();
				setState(115);
				match(CLOSE);
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public StringLiteralContext stringLiteral() {
			return getRuleContext(StringLiteralContext.class,0);
		}
		public TerminalNode CharLiteral() { return getToken(KDParser.CharLiteral, 0); }
		public TerminalNode ID() { return getToken(KDParser.ID, 0); }
		public TerminalNode IntegerLiteral() { return getToken(KDParser.IntegerLiteral, 0); }
		public TerminalNode HexLiteral() { return getToken(KDParser.HexLiteral, 0); }
		public TerminalNode BinLiteral() { return getToken(KDParser.BinLiteral, 0); }
		public TerminalNode LongLiteral() { return getToken(KDParser.LongLiteral, 0); }
		public TerminalNode FloatLiteral() { return getToken(KDParser.FloatLiteral, 0); }
		public TerminalNode DoubleLiteral() { return getToken(KDParser.DoubleLiteral, 0); }
		public TerminalNode DecimalLiteral() { return getToken(KDParser.DecimalLiteral, 0); }
		public TerminalNode TRUE() { return getToken(KDParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(KDParser.FALSE, 0); }
		public TerminalNode NULL() { return getToken(KDParser.NULL, 0); }
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public MapContext map() {
			return getRuleContext(MapContext.class,0);
		}
		public CallContext call() {
			return getRuleContext(CallContext.class,0);
		}
		public DateTimeContext dateTime() {
			return getRuleContext(DateTimeContext.class,0);
		}
		public DurationContext duration() {
			return getRuleContext(DurationContext.class,0);
		}
		public QuantityContext quantity() {
			return getRuleContext(QuantityContext.class,0);
		}
		public TerminalNode URL() { return getToken(KDParser.URL, 0); }
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public TerminalNode Version() { return getToken(KDParser.Version, 0); }
		public BlobContext blob() {
			return getRuleContext(BlobContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_value);
		try {
			setState(142);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(119);
				stringLiteral();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(120);
				match(CharLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(121);
				match(ID);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(122);
				match(IntegerLiteral);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(123);
				match(HexLiteral);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(124);
				match(BinLiteral);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(125);
				match(LongLiteral);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(126);
				match(FloatLiteral);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(127);
				match(DoubleLiteral);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(128);
				match(DecimalLiteral);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(129);
				match(TRUE);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(130);
				match(FALSE);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(131);
				match(NULL);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(132);
				list();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(133);
				map();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(134);
				call();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(135);
				dateTime();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(136);
				duration();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(137);
				quantity();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(138);
				match(URL);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(139);
				range();
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(140);
				match(Version);
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(141);
				blob();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralContext extends ParserRuleContext {
		public TerminalNode SimpleString() { return getToken(KDParser.SimpleString, 0); }
		public TerminalNode RawString() { return getToken(KDParser.RawString, 0); }
		public BlockStringContext blockString() {
			return getRuleContext(BlockStringContext.class,0);
		}
		public BlockRawStringContext blockRawString() {
			return getRuleContext(BlockRawStringContext.class,0);
		}
		public BlockRawAltStringContext blockRawAltString() {
			return getRuleContext(BlockRawAltStringContext.class,0);
		}
		public StringLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralContext stringLiteral() throws RecognitionException {
		StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_stringLiteral);
		try {
			setState(149);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SimpleString:
				enterOuterAlt(_localctx, 1);
				{
				setState(144);
				match(SimpleString);
				}
				break;
			case RawString:
				enterOuterAlt(_localctx, 2);
				{
				setState(145);
				match(RawString);
				}
				break;
			case BlockStringStart:
				enterOuterAlt(_localctx, 3);
				{
				setState(146);
				blockString();
				}
				break;
			case BlockRawStringStart:
				enterOuterAlt(_localctx, 4);
				{
				setState(147);
				blockRawString();
				}
				break;
			case BlockRawAltStringStart:
				enterOuterAlt(_localctx, 5);
				{
				setState(148);
				blockRawAltString();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockStringContext extends ParserRuleContext {
		public TerminalNode BlockStringStart() { return getToken(KDParser.BlockStringStart, 0); }
		public TerminalNode BlockStringEnd() { return getToken(KDParser.BlockStringEnd, 0); }
		public List<TerminalNode> BlockStringChunk() { return getTokens(KDParser.BlockStringChunk); }
		public TerminalNode BlockStringChunk(int i) {
			return getToken(KDParser.BlockStringChunk, i);
		}
		public BlockStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterBlockString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitBlockString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitBlockString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStringContext blockString() throws RecognitionException {
		BlockStringContext _localctx = new BlockStringContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_blockString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			match(BlockStringStart);
			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BlockStringChunk) {
				{
				{
				setState(152);
				match(BlockStringChunk);
				}
				}
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(158);
			match(BlockStringEnd);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockRawStringContext extends ParserRuleContext {
		public TerminalNode BlockRawStringStart() { return getToken(KDParser.BlockRawStringStart, 0); }
		public TerminalNode BlockRawStringEnd() { return getToken(KDParser.BlockRawStringEnd, 0); }
		public List<TerminalNode> BlockRawStringChunk() { return getTokens(KDParser.BlockRawStringChunk); }
		public TerminalNode BlockRawStringChunk(int i) {
			return getToken(KDParser.BlockRawStringChunk, i);
		}
		public BlockRawStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockRawString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterBlockRawString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitBlockRawString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitBlockRawString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockRawStringContext blockRawString() throws RecognitionException {
		BlockRawStringContext _localctx = new BlockRawStringContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_blockRawString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			match(BlockRawStringStart);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BlockRawStringChunk) {
				{
				{
				setState(161);
				match(BlockRawStringChunk);
				}
				}
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(167);
			match(BlockRawStringEnd);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockRawAltStringContext extends ParserRuleContext {
		public TerminalNode BlockRawAltStringStart() { return getToken(KDParser.BlockRawAltStringStart, 0); }
		public TerminalNode BlockRawAltStringEnd() { return getToken(KDParser.BlockRawAltStringEnd, 0); }
		public List<TerminalNode> BlockRawAltStringChunk() { return getTokens(KDParser.BlockRawAltStringChunk); }
		public TerminalNode BlockRawAltStringChunk(int i) {
			return getToken(KDParser.BlockRawAltStringChunk, i);
		}
		public BlockRawAltStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockRawAltString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterBlockRawAltString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitBlockRawAltString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitBlockRawAltString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockRawAltStringContext blockRawAltString() throws RecognitionException {
		BlockRawAltStringContext _localctx = new BlockRawAltStringContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_blockRawAltString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(BlockRawAltStringStart);
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BlockRawAltStringChunk) {
				{
				{
				setState(170);
				match(BlockRawAltStringChunk);
				}
				}
				setState(175);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(176);
			match(BlockRawAltStringEnd);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DurationContext extends ParserRuleContext {
		public TerminalNode CompoundDuration() { return getToken(KDParser.CompoundDuration, 0); }
		public TerminalNode DayDuration() { return getToken(KDParser.DayDuration, 0); }
		public TerminalNode HourDuration() { return getToken(KDParser.HourDuration, 0); }
		public TerminalNode MinuteDuration() { return getToken(KDParser.MinuteDuration, 0); }
		public TerminalNode SecondDuration() { return getToken(KDParser.SecondDuration, 0); }
		public TerminalNode MillisecondDuration() { return getToken(KDParser.MillisecondDuration, 0); }
		public TerminalNode NanosecondDuration() { return getToken(KDParser.NanosecondDuration, 0); }
		public DurationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_duration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterDuration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitDuration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitDuration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DurationContext duration() throws RecognitionException {
		DurationContext _localctx = new DurationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_duration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CompoundDuration) | (1L << DayDuration) | (1L << HourDuration) | (1L << MinuteDuration) | (1L << SecondDuration) | (1L << MillisecondDuration) | (1L << NanosecondDuration))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuantityContext extends ParserRuleContext {
		public TerminalNode IntegerQuantityLiteral() { return getToken(KDParser.IntegerQuantityLiteral, 0); }
		public TerminalNode DecimalQuantityLiteral() { return getToken(KDParser.DecimalQuantityLiteral, 0); }
		public QuantityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterQuantity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitQuantity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitQuantity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantityContext quantity() throws RecognitionException {
		QuantityContext _localctx = new QuantityContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_quantity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			_la = _input.LA(1);
			if ( !(_la==IntegerQuantityLiteral || _la==DecimalQuantityLiteral) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeOpContext extends ParserRuleContext {
		public TerminalNode InclusiveRangeOp() { return getToken(KDParser.InclusiveRangeOp, 0); }
		public TerminalNode ExclusiveRangeOp() { return getToken(KDParser.ExclusiveRangeOp, 0); }
		public TerminalNode ExclusiveLeftOp() { return getToken(KDParser.ExclusiveLeftOp, 0); }
		public TerminalNode ExclusiveRightOp() { return getToken(KDParser.ExclusiveRightOp, 0); }
		public RangeOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterRangeOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitRangeOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitRangeOp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeOpContext rangeOp() throws RecognitionException {
		RangeOpContext _localctx = new RangeOpContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_rangeOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << InclusiveRangeOp) | (1L << ExclusiveRangeOp) | (1L << ExclusiveLeftOp) | (1L << ExclusiveRightOp))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<TerminalNode> IntegerLiteral() { return getTokens(KDParser.IntegerLiteral); }
		public TerminalNode IntegerLiteral(int i) {
			return getToken(KDParser.IntegerLiteral, i);
		}
		public IntRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterIntRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitIntRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitIntRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntRangeContext intRange() throws RecognitionException {
		IntRangeContext _localctx = new IntRangeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_intRange);
		try {
			setState(196);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(184);
				match(UNDERSCORE);
				setState(185);
				rangeOp();
				setState(186);
				match(IntegerLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(188);
				match(IntegerLiteral);
				setState(189);
				rangeOp();
				setState(190);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(192);
				match(IntegerLiteral);
				setState(193);
				rangeOp();
				setState(194);
				match(IntegerLiteral);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LongRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<TerminalNode> LongLiteral() { return getTokens(KDParser.LongLiteral); }
		public TerminalNode LongLiteral(int i) {
			return getToken(KDParser.LongLiteral, i);
		}
		public LongRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_longRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterLongRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitLongRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitLongRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LongRangeContext longRange() throws RecognitionException {
		LongRangeContext _localctx = new LongRangeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_longRange);
		try {
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(198);
				match(UNDERSCORE);
				setState(199);
				rangeOp();
				setState(200);
				match(LongLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(202);
				match(LongLiteral);
				setState(203);
				rangeOp();
				setState(204);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(206);
				match(LongLiteral);
				setState(207);
				rangeOp();
				setState(208);
				match(LongLiteral);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FloatRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<TerminalNode> FloatLiteral() { return getTokens(KDParser.FloatLiteral); }
		public TerminalNode FloatLiteral(int i) {
			return getToken(KDParser.FloatLiteral, i);
		}
		public FloatRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterFloatRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitFloatRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitFloatRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FloatRangeContext floatRange() throws RecognitionException {
		FloatRangeContext _localctx = new FloatRangeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_floatRange);
		try {
			setState(224);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(212);
				match(UNDERSCORE);
				setState(213);
				rangeOp();
				setState(214);
				match(FloatLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(216);
				match(FloatLiteral);
				setState(217);
				rangeOp();
				setState(218);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(220);
				match(FloatLiteral);
				setState(221);
				rangeOp();
				setState(222);
				match(FloatLiteral);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DoubleRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<TerminalNode> DoubleLiteral() { return getTokens(KDParser.DoubleLiteral); }
		public TerminalNode DoubleLiteral(int i) {
			return getToken(KDParser.DoubleLiteral, i);
		}
		public DoubleRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doubleRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterDoubleRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitDoubleRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitDoubleRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoubleRangeContext doubleRange() throws RecognitionException {
		DoubleRangeContext _localctx = new DoubleRangeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_doubleRange);
		try {
			setState(238);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(226);
				match(UNDERSCORE);
				setState(227);
				rangeOp();
				setState(228);
				match(DoubleLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(230);
				match(DoubleLiteral);
				setState(231);
				rangeOp();
				setState(232);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(234);
				match(DoubleLiteral);
				setState(235);
				rangeOp();
				setState(236);
				match(DoubleLiteral);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DecimalRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<TerminalNode> DecimalLiteral() { return getTokens(KDParser.DecimalLiteral); }
		public TerminalNode DecimalLiteral(int i) {
			return getToken(KDParser.DecimalLiteral, i);
		}
		public DecimalRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterDecimalRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitDecimalRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitDecimalRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalRangeContext decimalRange() throws RecognitionException {
		DecimalRangeContext _localctx = new DecimalRangeContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_decimalRange);
		try {
			setState(252);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(240);
				match(UNDERSCORE);
				setState(241);
				rangeOp();
				setState(242);
				match(DecimalLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(244);
				match(DecimalLiteral);
				setState(245);
				rangeOp();
				setState(246);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(248);
				match(DecimalLiteral);
				setState(249);
				rangeOp();
				setState(250);
				match(DecimalLiteral);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DurationRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<DurationContext> duration() {
			return getRuleContexts(DurationContext.class);
		}
		public DurationContext duration(int i) {
			return getRuleContext(DurationContext.class,i);
		}
		public DurationRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_durationRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterDurationRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitDurationRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitDurationRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DurationRangeContext durationRange() throws RecognitionException {
		DurationRangeContext _localctx = new DurationRangeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_durationRange);
		try {
			setState(266);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(254);
				match(UNDERSCORE);
				setState(255);
				rangeOp();
				setState(256);
				duration();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(258);
				duration();
				setState(259);
				rangeOp();
				setState(260);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(262);
				duration();
				setState(263);
				rangeOp();
				setState(264);
				duration();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateTimeRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<DateTimeContext> dateTime() {
			return getRuleContexts(DateTimeContext.class);
		}
		public DateTimeContext dateTime(int i) {
			return getRuleContext(DateTimeContext.class,i);
		}
		public DateTimeRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateTimeRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterDateTimeRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitDateTimeRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitDateTimeRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateTimeRangeContext dateTimeRange() throws RecognitionException {
		DateTimeRangeContext _localctx = new DateTimeRangeContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_dateTimeRange);
		try {
			setState(280);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(268);
				match(UNDERSCORE);
				setState(269);
				rangeOp();
				setState(270);
				dateTime();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(272);
				dateTime();
				setState(273);
				rangeOp();
				setState(274);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(276);
				dateTime();
				setState(277);
				rangeOp();
				setState(278);
				dateTime();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VersionRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<TerminalNode> Version() { return getTokens(KDParser.Version); }
		public TerminalNode Version(int i) {
			return getToken(KDParser.Version, i);
		}
		public VersionRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_versionRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterVersionRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitVersionRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitVersionRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VersionRangeContext versionRange() throws RecognitionException {
		VersionRangeContext _localctx = new VersionRangeContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_versionRange);
		try {
			setState(294);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(282);
				match(UNDERSCORE);
				setState(283);
				rangeOp();
				setState(284);
				match(Version);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(286);
				match(Version);
				setState(287);
				rangeOp();
				setState(288);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(290);
				match(Version);
				setState(291);
				rangeOp();
				setState(292);
				match(Version);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CharRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<TerminalNode> CharLiteral() { return getTokens(KDParser.CharLiteral); }
		public TerminalNode CharLiteral(int i) {
			return getToken(KDParser.CharLiteral, i);
		}
		public CharRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_charRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterCharRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitCharRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitCharRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharRangeContext charRange() throws RecognitionException {
		CharRangeContext _localctx = new CharRangeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_charRange);
		try {
			setState(308);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(296);
				match(UNDERSCORE);
				setState(297);
				rangeOp();
				setState(298);
				match(CharLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(300);
				match(CharLiteral);
				setState(301);
				rangeOp();
				setState(302);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(304);
				match(CharLiteral);
				setState(305);
				rangeOp();
				setState(306);
				match(CharLiteral);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<StringLiteralContext> stringLiteral() {
			return getRuleContexts(StringLiteralContext.class);
		}
		public StringLiteralContext stringLiteral(int i) {
			return getRuleContext(StringLiteralContext.class,i);
		}
		public StringRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterStringRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitStringRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitStringRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringRangeContext stringRange() throws RecognitionException {
		StringRangeContext _localctx = new StringRangeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_stringRange);
		try {
			setState(322);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(310);
				match(UNDERSCORE);
				setState(311);
				rangeOp();
				setState(312);
				stringLiteral();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(314);
				stringLiteral();
				setState(315);
				rangeOp();
				setState(316);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(318);
				stringLiteral();
				setState(319);
				rangeOp();
				setState(320);
				stringLiteral();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuantityRangeContext extends ParserRuleContext {
		public TerminalNode UNDERSCORE() { return getToken(KDParser.UNDERSCORE, 0); }
		public RangeOpContext rangeOp() {
			return getRuleContext(RangeOpContext.class,0);
		}
		public List<QuantityContext> quantity() {
			return getRuleContexts(QuantityContext.class);
		}
		public QuantityContext quantity(int i) {
			return getRuleContext(QuantityContext.class,i);
		}
		public QuantityRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantityRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterQuantityRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitQuantityRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitQuantityRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QuantityRangeContext quantityRange() throws RecognitionException {
		QuantityRangeContext _localctx = new QuantityRangeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_quantityRange);
		try {
			setState(336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(324);
				match(UNDERSCORE);
				setState(325);
				rangeOp();
				setState(326);
				quantity();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(328);
				quantity();
				setState(329);
				rangeOp();
				setState(330);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(332);
				quantity();
				setState(333);
				rangeOp();
				setState(334);
				quantity();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RangeContext extends ParserRuleContext {
		public IntRangeContext intRange() {
			return getRuleContext(IntRangeContext.class,0);
		}
		public LongRangeContext longRange() {
			return getRuleContext(LongRangeContext.class,0);
		}
		public FloatRangeContext floatRange() {
			return getRuleContext(FloatRangeContext.class,0);
		}
		public DoubleRangeContext doubleRange() {
			return getRuleContext(DoubleRangeContext.class,0);
		}
		public DecimalRangeContext decimalRange() {
			return getRuleContext(DecimalRangeContext.class,0);
		}
		public DurationRangeContext durationRange() {
			return getRuleContext(DurationRangeContext.class,0);
		}
		public DateTimeRangeContext dateTimeRange() {
			return getRuleContext(DateTimeRangeContext.class,0);
		}
		public VersionRangeContext versionRange() {
			return getRuleContext(VersionRangeContext.class,0);
		}
		public CharRangeContext charRange() {
			return getRuleContext(CharRangeContext.class,0);
		}
		public StringRangeContext stringRange() {
			return getRuleContext(StringRangeContext.class,0);
		}
		public QuantityRangeContext quantityRange() {
			return getRuleContext(QuantityRangeContext.class,0);
		}
		public RangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_range; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeContext range() throws RecognitionException {
		RangeContext _localctx = new RangeContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_range);
		try {
			setState(349);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(338);
				intRange();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(339);
				longRange();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(340);
				floatRange();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(341);
				doubleRange();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(342);
				decimalRange();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(343);
				durationRange();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(344);
				dateTimeRange();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(345);
				versionRange();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(346);
				charRange();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(347);
				stringRange();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(348);
				quantityRange();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlobContext extends ParserRuleContext {
		public TerminalNode BLOB_START() { return getToken(KDParser.BLOB_START, 0); }
		public TerminalNode BLOB_END() { return getToken(KDParser.BLOB_END, 0); }
		public List<TerminalNode> BLOB_DATA() { return getTokens(KDParser.BLOB_DATA); }
		public TerminalNode BLOB_DATA(int i) {
			return getToken(KDParser.BLOB_DATA, i);
		}
		public BlobContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blob; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterBlob(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitBlob(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitBlob(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlobContext blob() throws RecognitionException {
		BlobContext _localctx = new BlobContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_blob);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(351);
			match(BLOB_START);
			setState(355);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BLOB_DATA) {
				{
				{
				setState(352);
				match(BLOB_DATA);
				}
				}
				setState(357);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(358);
			match(BLOB_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueListContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public ValueListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterValueList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitValueList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitValueList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueListContext valueList() throws RecognitionException {
		ValueListContext _localctx = new ValueListContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_valueList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(361); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(360);
					value();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(363); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeContext extends ParserRuleContext {
		public NsNameContext nsName() {
			return getRuleContext(NsNameContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(KDParser.EQUALS, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(365);
			nsName();
			setState(366);
			match(EQUALS);
			setState(367);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeListContext extends ParserRuleContext {
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public AttributeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterAttributeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitAttributeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitAttributeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeListContext attributeList() throws RecognitionException {
		AttributeListContext _localctx = new AttributeListContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_attributeList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(370); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(369);
					attribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(372); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NsNameContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(KDParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(KDParser.ID, i);
		}
		public TerminalNode COLON() { return getToken(KDParser.COLON, 0); }
		public NsNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nsName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterNsName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitNsName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitNsName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NsNameContext nsName() throws RecognitionException {
		NsNameContext _localctx = new NsNameContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_nsName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(374);
				match(ID);
				setState(375);
				match(COLON);
				}
				break;
			}
			setState(378);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListContext extends ParserRuleContext {
		public TerminalNode LSQUARE() { return getToken(KDParser.LSQUARE, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode RSQUARE() { return getToken(KDParser.RSQUARE, 0); }
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KDParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KDParser.COMMA, i);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_list);
		int _la;
		try {
			int _alt;
			setState(413);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(380);
				match(LSQUARE);
				setState(384);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(381);
					match(NL);
					}
					}
					setState(386);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(387);
				value();
				setState(400);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(389);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==COMMA) {
							{
							setState(388);
							match(COMMA);
							}
						}

						setState(394);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==NL) {
							{
							{
							setState(391);
							match(NL);
							}
							}
							setState(396);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(397);
						value();
						}
						} 
					}
					setState(402);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				}
				setState(406);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(403);
					match(NL);
					}
					}
					setState(408);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(409);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(411);
				match(LSQUARE);
				setState(412);
				match(RSQUARE);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PairContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public TerminalNode EQUALS() { return getToken(KDParser.EQUALS, 0); }
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public PairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PairContext pair() throws RecognitionException {
		PairContext _localctx = new PairContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_pair);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(415);
				match(NL);
				}
				}
				setState(420);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(421);
			value();
			setState(422);
			match(EQUALS);
			setState(426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(423);
				match(NL);
				}
				}
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(429);
			value();
			setState(433);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(430);
					match(NL);
					}
					} 
				}
				setState(435);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MapContext extends ParserRuleContext {
		public TerminalNode LSQUARE() { return getToken(KDParser.LSQUARE, 0); }
		public List<PairContext> pair() {
			return getRuleContexts(PairContext.class);
		}
		public PairContext pair(int i) {
			return getRuleContext(PairContext.class,i);
		}
		public TerminalNode RSQUARE() { return getToken(KDParser.RSQUARE, 0); }
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KDParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KDParser.COMMA, i);
		}
		public TerminalNode EQUALS() { return getToken(KDParser.EQUALS, 0); }
		public MapContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_map; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterMap(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitMap(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitMap(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MapContext map() throws RecognitionException {
		MapContext _localctx = new MapContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_map);
		int _la;
		try {
			int _alt;
			setState(470);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(436);
				match(LSQUARE);
				setState(440);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(437);
						match(NL);
						}
						} 
					}
					setState(442);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
				}
				setState(443);
				pair();
				setState(456);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(445);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==COMMA) {
							{
							setState(444);
							match(COMMA);
							}
						}

						setState(450);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(447);
								match(NL);
								}
								} 
							}
							setState(452);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
						}
						setState(453);
						pair();
						}
						} 
					}
					setState(458);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				}
				setState(462);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(459);
					match(NL);
					}
					}
					setState(464);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(465);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(467);
				match(LSQUARE);
				setState(468);
				match(EQUALS);
				setState(469);
				match(RSQUARE);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallValueListContext extends ParserRuleContext {
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KDParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KDParser.COMMA, i);
		}
		public CallValueListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callValueList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterCallValueList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitCallValueList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitCallValueList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallValueListContext callValueList() throws RecognitionException {
		CallValueListContext _localctx = new CallValueListContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_callValueList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(475);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(472);
				match(NL);
				}
				}
				setState(477);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(478);
			value();
			setState(491);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(480);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(479);
						match(COMMA);
						}
					}

					setState(485);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NL) {
						{
						{
						setState(482);
						match(NL);
						}
						}
						setState(487);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(488);
					value();
					}
					} 
				}
				setState(493);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			}
			setState(497);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(494);
					match(NL);
					}
					} 
				}
				setState(499);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallPairContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(KDParser.ID, 0); }
		public TerminalNode EQUALS() { return getToken(KDParser.EQUALS, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public CallPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callPair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterCallPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitCallPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitCallPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallPairContext callPair() throws RecognitionException {
		CallPairContext _localctx = new CallPairContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_callPair);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(503);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(500);
				match(NL);
				}
				}
				setState(505);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(506);
			match(ID);
			setState(507);
			match(EQUALS);
			setState(511);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(508);
				match(NL);
				}
				}
				setState(513);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(514);
			value();
			setState(518);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(515);
					match(NL);
					}
					} 
				}
				setState(520);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallAttributeListContext extends ParserRuleContext {
		public List<CallPairContext> callPair() {
			return getRuleContexts(CallPairContext.class);
		}
		public CallPairContext callPair(int i) {
			return getRuleContext(CallPairContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(KDParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(KDParser.COMMA, i);
		}
		public CallAttributeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callAttributeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterCallAttributeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitCallAttributeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitCallAttributeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallAttributeListContext callAttributeList() throws RecognitionException {
		CallAttributeListContext _localctx = new CallAttributeListContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_callAttributeList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(524);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(521);
					match(NL);
					}
					} 
				}
				setState(526);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,53,_ctx);
			}
			setState(527);
			callPair();
			setState(540);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(529);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(528);
						match(COMMA);
						}
					}

					setState(534);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,55,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(531);
							match(NL);
							}
							} 
						}
						setState(536);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,55,_ctx);
					}
					setState(537);
					callPair();
					}
					} 
				}
				setState(542);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,56,_ctx);
			}
			setState(546);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(543);
				match(NL);
				}
				}
				setState(548);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(KDParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(KDParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(KDParser.RPAREN, 0); }
		public CallValueListContext callValueList() {
			return getRuleContext(CallValueListContext.class,0);
		}
		public CallAttributeListContext callAttributeList() {
			return getRuleContext(CallAttributeListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(KDParser.COMMA, 0); }
		public CallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_call; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CallContext call() throws RecognitionException {
		CallContext _localctx = new CallContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_call);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			match(ID);
			setState(550);
			match(LPAREN);
			setState(552);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(551);
				callValueList();
				}
				break;
			}
			setState(558);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << COMMA) | (1L << ID) | (1L << NL))) != 0)) {
				{
				setState(555);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(554);
					match(COMMA);
					}
				}

				setState(557);
				callAttributeList();
				}
			}

			setState(560);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(KDParser.AT, 0); }
		public NsNameContext nsName() {
			return getRuleContext(NsNameContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(KDParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(KDParser.RPAREN, 0); }
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public AttributeListContext attributeList() {
			return getRuleContext(AttributeListContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(562);
			match(AT);
			setState(563);
			nsName();
			setState(572);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(564);
				match(LPAREN);
				setState(566);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,61,_ctx) ) {
				case 1:
					{
					setState(565);
					valueList();
					}
					break;
				}
				setState(569);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(568);
					attributeList();
					}
				}

				setState(571);
				match(RPAREN);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationListContext extends ParserRuleContext {
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(KDParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(KDParser.NL, i);
		}
		public AnnotationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterAnnotationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitAnnotationList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitAnnotationList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationListContext annotationList() throws RecognitionException {
		AnnotationListContext _localctx = new AnnotationListContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_annotationList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(574);
			annotation();
			setState(584);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(578);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NL) {
						{
						{
						setState(575);
						match(NL);
						}
						}
						setState(580);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(581);
					annotation();
					}
					} 
				}
				setState(586);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,65,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateTimeContext extends ParserRuleContext {
		public TerminalNode Date() { return getToken(KDParser.Date, 0); }
		public TerminalNode Time() { return getToken(KDParser.Time, 0); }
		public DateTimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateTime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterDateTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitDateTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitDateTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateTimeContext dateTime() throws RecognitionException {
		DateTimeContext _localctx = new DateTimeContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_dateTime);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(587);
			match(Date);
			setState(589);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Time) {
				{
				setState(588);
				match(Time);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3D\u0252\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\7\2N\n\2\f\2\16\2Q\13\2\3\2\3"+
		"\2\7\2U\n\2\f\2\16\2X\13\2\7\2Z\n\2\f\2\16\2]\13\2\3\3\3\3\7\3a\n\3\f"+
		"\3\16\3d\13\3\5\3f\n\3\3\3\3\3\5\3j\n\3\3\3\5\3m\n\3\3\3\5\3p\n\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3x\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0091\n\4\3\5"+
		"\3\5\3\5\3\5\3\5\5\5\u0098\n\5\3\6\3\6\7\6\u009c\n\6\f\6\16\6\u009f\13"+
		"\6\3\6\3\6\3\7\3\7\7\7\u00a5\n\7\f\7\16\7\u00a8\13\7\3\7\3\7\3\b\3\b\7"+
		"\b\u00ae\n\b\f\b\16\b\u00b1\13\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00c7\n\f\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00d5\n\r\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00e3\n\16\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00f1\n\17\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00ff\n\20\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u010d\n\21"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u011b"+
		"\n\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23"+
		"\u0129\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\5\24\u0137\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\5\25\u0145\n\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\5\26\u0153\n\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\5\27\u0160\n\27\3\30\3\30\7\30\u0164\n\30\f\30\16\30\u0167"+
		"\13\30\3\30\3\30\3\31\6\31\u016c\n\31\r\31\16\31\u016d\3\32\3\32\3\32"+
		"\3\32\3\33\6\33\u0175\n\33\r\33\16\33\u0176\3\34\3\34\5\34\u017b\n\34"+
		"\3\34\3\34\3\35\3\35\7\35\u0181\n\35\f\35\16\35\u0184\13\35\3\35\3\35"+
		"\5\35\u0188\n\35\3\35\7\35\u018b\n\35\f\35\16\35\u018e\13\35\3\35\7\35"+
		"\u0191\n\35\f\35\16\35\u0194\13\35\3\35\7\35\u0197\n\35\f\35\16\35\u019a"+
		"\13\35\3\35\3\35\3\35\3\35\5\35\u01a0\n\35\3\36\7\36\u01a3\n\36\f\36\16"+
		"\36\u01a6\13\36\3\36\3\36\3\36\7\36\u01ab\n\36\f\36\16\36\u01ae\13\36"+
		"\3\36\3\36\7\36\u01b2\n\36\f\36\16\36\u01b5\13\36\3\37\3\37\7\37\u01b9"+
		"\n\37\f\37\16\37\u01bc\13\37\3\37\3\37\5\37\u01c0\n\37\3\37\7\37\u01c3"+
		"\n\37\f\37\16\37\u01c6\13\37\3\37\7\37\u01c9\n\37\f\37\16\37\u01cc\13"+
		"\37\3\37\7\37\u01cf\n\37\f\37\16\37\u01d2\13\37\3\37\3\37\3\37\3\37\3"+
		"\37\5\37\u01d9\n\37\3 \7 \u01dc\n \f \16 \u01df\13 \3 \3 \5 \u01e3\n "+
		"\3 \7 \u01e6\n \f \16 \u01e9\13 \3 \7 \u01ec\n \f \16 \u01ef\13 \3 \7"+
		" \u01f2\n \f \16 \u01f5\13 \3!\7!\u01f8\n!\f!\16!\u01fb\13!\3!\3!\3!\7"+
		"!\u0200\n!\f!\16!\u0203\13!\3!\3!\7!\u0207\n!\f!\16!\u020a\13!\3\"\7\""+
		"\u020d\n\"\f\"\16\"\u0210\13\"\3\"\3\"\5\"\u0214\n\"\3\"\7\"\u0217\n\""+
		"\f\"\16\"\u021a\13\"\3\"\7\"\u021d\n\"\f\"\16\"\u0220\13\"\3\"\7\"\u0223"+
		"\n\"\f\"\16\"\u0226\13\"\3#\3#\3#\5#\u022b\n#\3#\5#\u022e\n#\3#\5#\u0231"+
		"\n#\3#\3#\3$\3$\3$\3$\5$\u0239\n$\3$\5$\u023c\n$\3$\5$\u023f\n$\3%\3%"+
		"\7%\u0243\n%\f%\16%\u0246\13%\3%\7%\u0249\n%\f%\16%\u024c\13%\3&\3&\5"+
		"&\u0250\n&\3&\2\2\'\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@BDFHJ\2\5\3\2\25\33\3\2\36\37\3\2 #\2\u029d\2O\3\2\2\2\4"+
		"l\3\2\2\2\6\u0090\3\2\2\2\b\u0097\3\2\2\2\n\u0099\3\2\2\2\f\u00a2\3\2"+
		"\2\2\16\u00ab\3\2\2\2\20\u00b4\3\2\2\2\22\u00b6\3\2\2\2\24\u00b8\3\2\2"+
		"\2\26\u00c6\3\2\2\2\30\u00d4\3\2\2\2\32\u00e2\3\2\2\2\34\u00f0\3\2\2\2"+
		"\36\u00fe\3\2\2\2 \u010c\3\2\2\2\"\u011a\3\2\2\2$\u0128\3\2\2\2&\u0136"+
		"\3\2\2\2(\u0144\3\2\2\2*\u0152\3\2\2\2,\u015f\3\2\2\2.\u0161\3\2\2\2\60"+
		"\u016b\3\2\2\2\62\u016f\3\2\2\2\64\u0174\3\2\2\2\66\u017a\3\2\2\28\u019f"+
		"\3\2\2\2:\u01a4\3\2\2\2<\u01d8\3\2\2\2>\u01dd\3\2\2\2@\u01f9\3\2\2\2B"+
		"\u020e\3\2\2\2D\u0227\3\2\2\2F\u0234\3\2\2\2H\u0240\3\2\2\2J\u024d\3\2"+
		"\2\2LN\79\2\2ML\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2P[\3\2\2\2QO\3\2"+
		"\2\2RV\5\4\3\2SU\79\2\2TS\3\2\2\2UX\3\2\2\2VT\3\2\2\2VW\3\2\2\2WZ\3\2"+
		"\2\2XV\3\2\2\2YR\3\2\2\2Z]\3\2\2\2[Y\3\2\2\2[\\\3\2\2\2\\\3\3\2\2\2]["+
		"\3\2\2\2^b\5H%\2_a\79\2\2`_\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2cf\3"+
		"\2\2\2db\3\2\2\2e^\3\2\2\2ef\3\2\2\2fg\3\2\2\2gi\5\66\34\2hj\5\60\31\2"+
		"ih\3\2\2\2ij\3\2\2\2jm\3\2\2\2km\5\60\31\2le\3\2\2\2lk\3\2\2\2mo\3\2\2"+
		"\2np\5\64\33\2on\3\2\2\2op\3\2\2\2pw\3\2\2\2qx\7&\2\2rx\79\2\2st\7(\2"+
		"\2tu\5\2\2\2uv\7)\2\2vx\3\2\2\2wq\3\2\2\2wr\3\2\2\2ws\3\2\2\2wx\3\2\2"+
		"\2x\5\3\2\2\2y\u0091\5\b\5\2z\u0091\7\24\2\2{\u0091\7\65\2\2|\u0091\7"+
		"\n\2\2}\u0091\7\13\2\2~\u0091\7\f\2\2\177\u0091\7\r\2\2\u0080\u0091\7"+
		"\7\2\2\u0081\u0091\7\b\2\2\u0082\u0091\7\t\2\2\u0083\u0091\7\4\2\2\u0084"+
		"\u0091\7\5\2\2\u0085\u0091\7\3\2\2\u0086\u0091\58\35\2\u0087\u0091\5<"+
		"\37\2\u0088\u0091\5D#\2\u0089\u0091\5J&\2\u008a\u0091\5\20\t\2\u008b\u0091"+
		"\5\22\n\2\u008c\u0091\7\6\2\2\u008d\u0091\5,\27\2\u008e\u0091\7\16\2\2"+
		"\u008f\u0091\5.\30\2\u0090y\3\2\2\2\u0090z\3\2\2\2\u0090{\3\2\2\2\u0090"+
		"|\3\2\2\2\u0090}\3\2\2\2\u0090~\3\2\2\2\u0090\177\3\2\2\2\u0090\u0080"+
		"\3\2\2\2\u0090\u0081\3\2\2\2\u0090\u0082\3\2\2\2\u0090\u0083\3\2\2\2\u0090"+
		"\u0084\3\2\2\2\u0090\u0085\3\2\2\2\u0090\u0086\3\2\2\2\u0090\u0087\3\2"+
		"\2\2\u0090\u0088\3\2\2\2\u0090\u0089\3\2\2\2\u0090\u008a\3\2\2\2\u0090"+
		"\u008b\3\2\2\2\u0090\u008c\3\2\2\2\u0090\u008d\3\2\2\2\u0090\u008e\3\2"+
		"\2\2\u0090\u008f\3\2\2\2\u0091\7\3\2\2\2\u0092\u0098\7\17\2\2\u0093\u0098"+
		"\7\20\2\2\u0094\u0098\5\n\6\2\u0095\u0098\5\f\7\2\u0096\u0098\5\16\b\2"+
		"\u0097\u0092\3\2\2\2\u0097\u0093\3\2\2\2\u0097\u0094\3\2\2\2\u0097\u0095"+
		"\3\2\2\2\u0097\u0096\3\2\2\2\u0098\t\3\2\2\2\u0099\u009d\7\21\2\2\u009a"+
		"\u009c\7=\2\2\u009b\u009a\3\2\2\2\u009c\u009f\3\2\2\2\u009d\u009b\3\2"+
		"\2\2\u009d\u009e\3\2\2\2\u009e\u00a0\3\2\2\2\u009f\u009d\3\2\2\2\u00a0"+
		"\u00a1\7>\2\2\u00a1\13\3\2\2\2\u00a2\u00a6\7\22\2\2\u00a3\u00a5\7@\2\2"+
		"\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7"+
		"\3\2\2\2\u00a7\u00a9\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\7A\2\2\u00aa"+
		"\r\3\2\2\2\u00ab\u00af\7\23\2\2\u00ac\u00ae\7C\2\2\u00ad\u00ac\3\2\2\2"+
		"\u00ae\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2"+
		"\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b3\7D\2\2\u00b3\17\3\2\2\2\u00b4"+
		"\u00b5\t\2\2\2\u00b5\21\3\2\2\2\u00b6\u00b7\t\3\2\2\u00b7\23\3\2\2\2\u00b8"+
		"\u00b9\t\4\2\2\u00b9\25\3\2\2\2\u00ba\u00bb\7\63\2\2\u00bb\u00bc\5\24"+
		"\13\2\u00bc\u00bd\7\n\2\2\u00bd\u00c7\3\2\2\2\u00be\u00bf\7\n\2\2\u00bf"+
		"\u00c0\5\24\13\2\u00c0\u00c1\7\63\2\2\u00c1\u00c7\3\2\2\2\u00c2\u00c3"+
		"\7\n\2\2\u00c3\u00c4\5\24\13\2\u00c4\u00c5\7\n\2\2\u00c5\u00c7\3\2\2\2"+
		"\u00c6\u00ba\3\2\2\2\u00c6\u00be\3\2\2\2\u00c6\u00c2\3\2\2\2\u00c7\27"+
		"\3\2\2\2\u00c8\u00c9\7\63\2\2\u00c9\u00ca\5\24\13\2\u00ca\u00cb\7\r\2"+
		"\2\u00cb\u00d5\3\2\2\2\u00cc\u00cd\7\r\2\2\u00cd\u00ce\5\24\13\2\u00ce"+
		"\u00cf\7\63\2\2\u00cf\u00d5\3\2\2\2\u00d0\u00d1\7\r\2\2\u00d1\u00d2\5"+
		"\24\13\2\u00d2\u00d3\7\r\2\2\u00d3\u00d5\3\2\2\2\u00d4\u00c8\3\2\2\2\u00d4"+
		"\u00cc\3\2\2\2\u00d4\u00d0\3\2\2\2\u00d5\31\3\2\2\2\u00d6\u00d7\7\63\2"+
		"\2\u00d7\u00d8\5\24\13\2\u00d8\u00d9\7\7\2\2\u00d9\u00e3\3\2\2\2\u00da"+
		"\u00db\7\7\2\2\u00db\u00dc\5\24\13\2\u00dc\u00dd\7\63\2\2\u00dd\u00e3"+
		"\3\2\2\2\u00de\u00df\7\7\2\2\u00df\u00e0\5\24\13\2\u00e0\u00e1\7\7\2\2"+
		"\u00e1\u00e3\3\2\2\2\u00e2\u00d6\3\2\2\2\u00e2\u00da\3\2\2\2\u00e2\u00de"+
		"\3\2\2\2\u00e3\33\3\2\2\2\u00e4\u00e5\7\63\2\2\u00e5\u00e6\5\24\13\2\u00e6"+
		"\u00e7\7\b\2\2\u00e7\u00f1\3\2\2\2\u00e8\u00e9\7\b\2\2\u00e9\u00ea\5\24"+
		"\13\2\u00ea\u00eb\7\63\2\2\u00eb\u00f1\3\2\2\2\u00ec\u00ed\7\b\2\2\u00ed"+
		"\u00ee\5\24\13\2\u00ee\u00ef\7\b\2\2\u00ef\u00f1\3\2\2\2\u00f0\u00e4\3"+
		"\2\2\2\u00f0\u00e8\3\2\2\2\u00f0\u00ec\3\2\2\2\u00f1\35\3\2\2\2\u00f2"+
		"\u00f3\7\63\2\2\u00f3\u00f4\5\24\13\2\u00f4\u00f5\7\t\2\2\u00f5\u00ff"+
		"\3\2\2\2\u00f6\u00f7\7\t\2\2\u00f7\u00f8\5\24\13\2\u00f8\u00f9\7\63\2"+
		"\2\u00f9\u00ff\3\2\2\2\u00fa\u00fb\7\t\2\2\u00fb\u00fc\5\24\13\2\u00fc"+
		"\u00fd\7\t\2\2\u00fd\u00ff\3\2\2\2\u00fe\u00f2\3\2\2\2\u00fe\u00f6\3\2"+
		"\2\2\u00fe\u00fa\3\2\2\2\u00ff\37\3\2\2\2\u0100\u0101\7\63\2\2\u0101\u0102"+
		"\5\24\13\2\u0102\u0103\5\20\t\2\u0103\u010d\3\2\2\2\u0104\u0105\5\20\t"+
		"\2\u0105\u0106\5\24\13\2\u0106\u0107\7\63\2\2\u0107\u010d\3\2\2\2\u0108"+
		"\u0109\5\20\t\2\u0109\u010a\5\24\13\2\u010a\u010b\5\20\t\2\u010b\u010d"+
		"\3\2\2\2\u010c\u0100\3\2\2\2\u010c\u0104\3\2\2\2\u010c\u0108\3\2\2\2\u010d"+
		"!\3\2\2\2\u010e\u010f\7\63\2\2\u010f\u0110\5\24\13\2\u0110\u0111\5J&\2"+
		"\u0111\u011b\3\2\2\2\u0112\u0113\5J&\2\u0113\u0114\5\24\13\2\u0114\u0115"+
		"\7\63\2\2\u0115\u011b\3\2\2\2\u0116\u0117\5J&\2\u0117\u0118\5\24\13\2"+
		"\u0118\u0119\5J&\2\u0119\u011b\3\2\2\2\u011a\u010e\3\2\2\2\u011a\u0112"+
		"\3\2\2\2\u011a\u0116\3\2\2\2\u011b#\3\2\2\2\u011c\u011d\7\63\2\2\u011d"+
		"\u011e\5\24\13\2\u011e\u011f\7\16\2\2\u011f\u0129\3\2\2\2\u0120\u0121"+
		"\7\16\2\2\u0121\u0122\5\24\13\2\u0122\u0123\7\63\2\2\u0123\u0129\3\2\2"+
		"\2\u0124\u0125\7\16\2\2\u0125\u0126\5\24\13\2\u0126\u0127\7\16\2\2\u0127"+
		"\u0129\3\2\2\2\u0128\u011c\3\2\2\2\u0128\u0120\3\2\2\2\u0128\u0124\3\2"+
		"\2\2\u0129%\3\2\2\2\u012a\u012b\7\63\2\2\u012b\u012c\5\24\13\2\u012c\u012d"+
		"\7\24\2\2\u012d\u0137\3\2\2\2\u012e\u012f\7\24\2\2\u012f\u0130\5\24\13"+
		"\2\u0130\u0131\7\63\2\2\u0131\u0137\3\2\2\2\u0132\u0133\7\24\2\2\u0133"+
		"\u0134\5\24\13\2\u0134\u0135\7\24\2\2\u0135\u0137\3\2\2\2\u0136\u012a"+
		"\3\2\2\2\u0136\u012e\3\2\2\2\u0136\u0132\3\2\2\2\u0137\'\3\2\2\2\u0138"+
		"\u0139\7\63\2\2\u0139\u013a\5\24\13\2\u013a\u013b\5\b\5\2\u013b\u0145"+
		"\3\2\2\2\u013c\u013d\5\b\5\2\u013d\u013e\5\24\13\2\u013e\u013f\7\63\2"+
		"\2\u013f\u0145\3\2\2\2\u0140\u0141\5\b\5\2\u0141\u0142\5\24\13\2\u0142"+
		"\u0143\5\b\5\2\u0143\u0145\3\2\2\2\u0144\u0138\3\2\2\2\u0144\u013c\3\2"+
		"\2\2\u0144\u0140\3\2\2\2\u0145)\3\2\2\2\u0146\u0147\7\63\2\2\u0147\u0148"+
		"\5\24\13\2\u0148\u0149\5\22\n\2\u0149\u0153\3\2\2\2\u014a\u014b\5\22\n"+
		"\2\u014b\u014c\5\24\13\2\u014c\u014d\7\63\2\2\u014d\u0153\3\2\2\2\u014e"+
		"\u014f\5\22\n\2\u014f\u0150\5\24\13\2\u0150\u0151\5\22\n\2\u0151\u0153"+
		"\3\2\2\2\u0152\u0146\3\2\2\2\u0152\u014a\3\2\2\2\u0152\u014e\3\2\2\2\u0153"+
		"+\3\2\2\2\u0154\u0160\5\26\f\2\u0155\u0160\5\30\r\2\u0156\u0160\5\32\16"+
		"\2\u0157\u0160\5\34\17\2\u0158\u0160\5\36\20\2\u0159\u0160\5 \21\2\u015a"+
		"\u0160\5\"\22\2\u015b\u0160\5$\23\2\u015c\u0160\5&\24\2\u015d\u0160\5"+
		"(\25\2\u015e\u0160\5*\26\2\u015f\u0154\3\2\2\2\u015f\u0155\3\2\2\2\u015f"+
		"\u0156\3\2\2\2\u015f\u0157\3\2\2\2\u015f\u0158\3\2\2\2\u015f\u0159\3\2"+
		"\2\2\u015f\u015a\3\2\2\2\u015f\u015b\3\2\2\2\u015f\u015c\3\2\2\2\u015f"+
		"\u015d\3\2\2\2\u015f\u015e\3\2\2\2\u0160-\3\2\2\2\u0161\u0165\7\64\2\2"+
		"\u0162\u0164\7:\2\2\u0163\u0162\3\2\2\2\u0164\u0167\3\2\2\2\u0165\u0163"+
		"\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u0168\3\2\2\2\u0167\u0165\3\2\2\2\u0168"+
		"\u0169\7;\2\2\u0169/\3\2\2\2\u016a\u016c\5\6\4\2\u016b\u016a\3\2\2\2\u016c"+
		"\u016d\3\2\2\2\u016d\u016b\3\2\2\2\u016d\u016e\3\2\2\2\u016e\61\3\2\2"+
		"\2\u016f\u0170\5\66\34\2\u0170\u0171\7\'\2\2\u0171\u0172\5\6\4\2\u0172"+
		"\63\3\2\2\2\u0173\u0175\5\62\32\2\u0174\u0173\3\2\2\2\u0175\u0176\3\2"+
		"\2\2\u0176\u0174\3\2\2\2\u0176\u0177\3\2\2\2\u0177\65\3\2\2\2\u0178\u0179"+
		"\7\65\2\2\u0179\u017b\7%\2\2\u017a\u0178\3\2\2\2\u017a\u017b\3\2\2\2\u017b"+
		"\u017c\3\2\2\2\u017c\u017d\7\65\2\2\u017d\67\3\2\2\2\u017e\u0182\7,\2"+
		"\2\u017f\u0181\79\2\2\u0180\u017f\3\2\2\2\u0181\u0184\3\2\2\2\u0182\u0180"+
		"\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0185\3\2\2\2\u0184\u0182\3\2\2\2\u0185"+
		"\u0192\5\6\4\2\u0186\u0188\7.\2\2\u0187\u0186\3\2\2\2\u0187\u0188\3\2"+
		"\2\2\u0188\u018c\3\2\2\2\u0189\u018b\79\2\2\u018a\u0189\3\2\2\2\u018b"+
		"\u018e\3\2\2\2\u018c\u018a\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018f\3\2"+
		"\2\2\u018e\u018c\3\2\2\2\u018f\u0191\5\6\4\2\u0190\u0187\3\2\2\2\u0191"+
		"\u0194\3\2\2\2\u0192\u0190\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0198\3\2"+
		"\2\2\u0194\u0192\3\2\2\2\u0195\u0197\79\2\2\u0196\u0195\3\2\2\2\u0197"+
		"\u019a\3\2\2\2\u0198\u0196\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u019b\3\2"+
		"\2\2\u019a\u0198\3\2\2\2\u019b\u019c\7-\2\2\u019c\u01a0\3\2\2\2\u019d"+
		"\u019e\7,\2\2\u019e\u01a0\7-\2\2\u019f\u017e\3\2\2\2\u019f\u019d\3\2\2"+
		"\2\u01a09\3\2\2\2\u01a1\u01a3\79\2\2\u01a2\u01a1\3\2\2\2\u01a3\u01a6\3"+
		"\2\2\2\u01a4\u01a2\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01a7\3\2\2\2\u01a6"+
		"\u01a4\3\2\2\2\u01a7\u01a8\5\6\4\2\u01a8\u01ac\7\'\2\2\u01a9\u01ab\79"+
		"\2\2\u01aa\u01a9\3\2\2\2\u01ab\u01ae\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ac"+
		"\u01ad\3\2\2\2\u01ad\u01af\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01b3\5\6"+
		"\4\2\u01b0\u01b2\79\2\2\u01b1\u01b0\3\2\2\2\u01b2\u01b5\3\2\2\2\u01b3"+
		"\u01b1\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4;\3\2\2\2\u01b5\u01b3\3\2\2\2"+
		"\u01b6\u01ba\7,\2\2\u01b7\u01b9\79\2\2\u01b8\u01b7\3\2\2\2\u01b9\u01bc"+
		"\3\2\2\2\u01ba\u01b8\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb\u01bd\3\2\2\2\u01bc"+
		"\u01ba\3\2\2\2\u01bd\u01ca\5:\36\2\u01be\u01c0\7.\2\2\u01bf\u01be\3\2"+
		"\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c4\3\2\2\2\u01c1\u01c3\79\2\2\u01c2"+
		"\u01c1\3\2\2\2\u01c3\u01c6\3\2\2\2\u01c4\u01c2\3\2\2\2\u01c4\u01c5\3\2"+
		"\2\2\u01c5\u01c7\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c7\u01c9\5:\36\2\u01c8"+
		"\u01bf\3\2\2\2\u01c9\u01cc\3\2\2\2\u01ca\u01c8\3\2\2\2\u01ca\u01cb\3\2"+
		"\2\2\u01cb\u01d0\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cd\u01cf\79\2\2\u01ce"+
		"\u01cd\3\2\2\2\u01cf\u01d2\3\2\2\2\u01d0\u01ce\3\2\2\2\u01d0\u01d1\3\2"+
		"\2\2\u01d1\u01d3\3\2\2\2\u01d2\u01d0\3\2\2\2\u01d3\u01d4\7-\2\2\u01d4"+
		"\u01d9\3\2\2\2\u01d5\u01d6\7,\2\2\u01d6\u01d7\7\'\2\2\u01d7\u01d9\7-\2"+
		"\2\u01d8\u01b6\3\2\2\2\u01d8\u01d5\3\2\2\2\u01d9=\3\2\2\2\u01da\u01dc"+
		"\79\2\2\u01db\u01da\3\2\2\2\u01dc\u01df\3\2\2\2\u01dd\u01db\3\2\2\2\u01dd"+
		"\u01de\3\2\2\2\u01de\u01e0\3\2\2\2\u01df\u01dd\3\2\2\2\u01e0\u01ed\5\6"+
		"\4\2\u01e1\u01e3\7.\2\2\u01e2\u01e1\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3"+
		"\u01e7\3\2\2\2\u01e4\u01e6\79\2\2\u01e5\u01e4\3\2\2\2\u01e6\u01e9\3\2"+
		"\2\2\u01e7\u01e5\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01ea\3\2\2\2\u01e9"+
		"\u01e7\3\2\2\2\u01ea\u01ec\5\6\4\2\u01eb\u01e2\3\2\2\2\u01ec\u01ef\3\2"+
		"\2\2\u01ed\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01f3\3\2\2\2\u01ef"+
		"\u01ed\3\2\2\2\u01f0\u01f2\79\2\2\u01f1\u01f0\3\2\2\2\u01f2\u01f5\3\2"+
		"\2\2\u01f3\u01f1\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4?\3\2\2\2\u01f5\u01f3"+
		"\3\2\2\2\u01f6\u01f8\79\2\2\u01f7\u01f6\3\2\2\2\u01f8\u01fb\3\2\2\2\u01f9"+
		"\u01f7\3\2\2\2\u01f9\u01fa\3\2\2\2\u01fa\u01fc\3\2\2\2\u01fb\u01f9\3\2"+
		"\2\2\u01fc\u01fd\7\65\2\2\u01fd\u0201\7\'\2\2\u01fe\u0200\79\2\2\u01ff"+
		"\u01fe\3\2\2\2\u0200\u0203\3\2\2\2\u0201\u01ff\3\2\2\2\u0201\u0202\3\2"+
		"\2\2\u0202\u0204\3\2\2\2\u0203\u0201\3\2\2\2\u0204\u0208\5\6\4\2\u0205"+
		"\u0207\79\2\2\u0206\u0205\3\2\2\2\u0207\u020a\3\2\2\2\u0208\u0206\3\2"+
		"\2\2\u0208\u0209\3\2\2\2\u0209A\3\2\2\2\u020a\u0208\3\2\2\2\u020b\u020d"+
		"\79\2\2\u020c\u020b\3\2\2\2\u020d\u0210\3\2\2\2\u020e\u020c\3\2\2\2\u020e"+
		"\u020f\3\2\2\2\u020f\u0211\3\2\2\2\u0210\u020e\3\2\2\2\u0211\u021e\5@"+
		"!\2\u0212\u0214\7.\2\2\u0213\u0212\3\2\2\2\u0213\u0214\3\2\2\2\u0214\u0218"+
		"\3\2\2\2\u0215\u0217\79\2\2\u0216\u0215\3\2\2\2\u0217\u021a\3\2\2\2\u0218"+
		"\u0216\3\2\2\2\u0218\u0219\3\2\2\2\u0219\u021b\3\2\2\2\u021a\u0218\3\2"+
		"\2\2\u021b\u021d\5@!\2\u021c\u0213\3\2\2\2\u021d\u0220\3\2\2\2\u021e\u021c"+
		"\3\2\2\2\u021e\u021f\3\2\2\2\u021f\u0224\3\2\2\2\u0220\u021e\3\2\2\2\u0221"+
		"\u0223\79\2\2\u0222\u0221\3\2\2\2\u0223\u0226\3\2\2\2\u0224\u0222\3\2"+
		"\2\2\u0224\u0225\3\2\2\2\u0225C\3\2\2\2\u0226\u0224\3\2\2\2\u0227\u0228"+
		"\7\65\2\2\u0228\u022a\7*\2\2\u0229\u022b\5> \2\u022a\u0229\3\2\2\2\u022a"+
		"\u022b\3\2\2\2\u022b\u0230\3\2\2\2\u022c\u022e\7.\2\2\u022d\u022c\3\2"+
		"\2\2\u022d\u022e\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u0231\5B\"\2\u0230"+
		"\u022d\3\2\2\2\u0230\u0231\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u0233\7+"+
		"\2\2\u0233E\3\2\2\2\u0234\u0235\7\61\2\2\u0235\u023e\5\66\34\2\u0236\u0238"+
		"\7*\2\2\u0237\u0239\5\60\31\2\u0238\u0237\3\2\2\2\u0238\u0239\3\2\2\2"+
		"\u0239\u023b\3\2\2\2\u023a\u023c\5\64\33\2\u023b\u023a\3\2\2\2\u023b\u023c"+
		"\3\2\2\2\u023c\u023d\3\2\2\2\u023d\u023f\7+\2\2\u023e\u0236\3\2\2\2\u023e"+
		"\u023f\3\2\2\2\u023fG\3\2\2\2\u0240\u024a\5F$\2\u0241\u0243\79\2\2\u0242"+
		"\u0241\3\2\2\2\u0243\u0246\3\2\2\2\u0244\u0242\3\2\2\2\u0244\u0245\3\2"+
		"\2\2\u0245\u0247\3\2\2\2\u0246\u0244\3\2\2\2\u0247\u0249\5F$\2\u0248\u0244"+
		"\3\2\2\2\u0249\u024c\3\2\2\2\u024a\u0248\3\2\2\2\u024a\u024b\3\2\2\2\u024b"+
		"I\3\2\2\2\u024c\u024a\3\2\2\2\u024d\u024f\7\34\2\2\u024e\u0250\7\35\2"+
		"\2\u024f\u024e\3\2\2\2\u024f\u0250\3\2\2\2\u0250K\3\2\2\2EOV[beilow\u0090"+
		"\u0097\u009d\u00a6\u00af\u00c6\u00d4\u00e2\u00f0\u00fe\u010c\u011a\u0128"+
		"\u0136\u0144\u0152\u015f\u0165\u016d\u0176\u017a\u0182\u0187\u018c\u0192"+
		"\u0198\u019f\u01a4\u01ac\u01b3\u01ba\u01bf\u01c4\u01ca\u01d0\u01d8\u01dd"+
		"\u01e2\u01e7\u01ed\u01f3\u01f9\u0201\u0208\u020e\u0213\u0218\u021e\u0224"+
		"\u022a\u022d\u0230\u0238\u023b\u023e\u0244\u024a\u024f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}