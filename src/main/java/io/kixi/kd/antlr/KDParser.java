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
		RULE_nsName = 26, RULE_list = 27, RULE_pair = 28, RULE_map = 29, RULE_annotation = 30, 
		RULE_annotationList = 31, RULE_dateTime = 32;
	private static String[] makeRuleNames() {
		return new String[] {
			"tagList", "tag", "value", "stringLiteral", "blockString", "blockRawString", 
			"blockRawAltString", "duration", "quantity", "rangeOp", "intRange", "longRange", 
			"floatRange", "doubleRange", "decimalRange", "durationRange", "dateTimeRange", 
			"versionRange", "charRange", "stringRange", "quantityRange", "range", 
			"blob", "valueList", "attribute", "attributeList", "nsName", "list", 
			"pair", "map", "annotation", "annotationList", "dateTime"
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
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(66);
				match(NL);
				}
				}
				setState(71);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << URL) | (1L << FloatLiteral) | (1L << DoubleLiteral) | (1L << DecimalLiteral) | (1L << IntegerLiteral) | (1L << HexLiteral) | (1L << BinLiteral) | (1L << LongLiteral) | (1L << Version) | (1L << SimpleString) | (1L << RawString) | (1L << BlockStringStart) | (1L << BlockRawStringStart) | (1L << BlockRawAltStringStart) | (1L << CharLiteral) | (1L << CompoundDuration) | (1L << DayDuration) | (1L << HourDuration) | (1L << MinuteDuration) | (1L << SecondDuration) | (1L << MillisecondDuration) | (1L << NanosecondDuration) | (1L << Date) | (1L << IntegerQuantityLiteral) | (1L << DecimalQuantityLiteral) | (1L << LSQUARE) | (1L << AT) | (1L << UNDERSCORE) | (1L << BLOB_START) | (1L << ID))) != 0)) {
				{
				{
				setState(72);
				tag();
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(73);
					match(NL);
					}
					}
					setState(78);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(83);
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
			setState(98);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				{
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AT) {
					{
					setState(84);
					annotationList();
					setState(88);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NL) {
						{
						{
						setState(85);
						match(NL);
						}
						}
						setState(90);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(93);
				nsName();
				setState(95);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(94);
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
				setState(97);
				valueList();
				}
				}
				break;
			}
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(100);
				attributeList();
				}
				break;
			}
			setState(109);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(103);
				match(SEMICOLON);
				}
				break;
			case 2:
				{
				setState(104);
				match(NL);
				}
				break;
			case 3:
				{
				{
				setState(105);
				match(OPEN);
				setState(106);
				tagList();
				setState(107);
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
			setState(133);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(111);
				stringLiteral();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(112);
				match(CharLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(113);
				match(ID);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(114);
				match(IntegerLiteral);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(115);
				match(HexLiteral);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(116);
				match(BinLiteral);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(117);
				match(LongLiteral);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(118);
				match(FloatLiteral);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(119);
				match(DoubleLiteral);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(120);
				match(DecimalLiteral);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(121);
				match(TRUE);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(122);
				match(FALSE);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(123);
				match(NULL);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(124);
				list();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(125);
				map();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(126);
				dateTime();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(127);
				duration();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(128);
				quantity();
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(129);
				match(URL);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(130);
				range();
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(131);
				match(Version);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(132);
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
			setState(140);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SimpleString:
				enterOuterAlt(_localctx, 1);
				{
				setState(135);
				match(SimpleString);
				}
				break;
			case RawString:
				enterOuterAlt(_localctx, 2);
				{
				setState(136);
				match(RawString);
				}
				break;
			case BlockStringStart:
				enterOuterAlt(_localctx, 3);
				{
				setState(137);
				blockString();
				}
				break;
			case BlockRawStringStart:
				enterOuterAlt(_localctx, 4);
				{
				setState(138);
				blockRawString();
				}
				break;
			case BlockRawAltStringStart:
				enterOuterAlt(_localctx, 5);
				{
				setState(139);
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
			setState(142);
			match(BlockStringStart);
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BlockStringChunk) {
				{
				{
				setState(143);
				match(BlockStringChunk);
				}
				}
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(149);
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
			setState(151);
			match(BlockRawStringStart);
			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BlockRawStringChunk) {
				{
				{
				setState(152);
				match(BlockRawStringChunk);
				}
				}
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(158);
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
			setState(160);
			match(BlockRawAltStringStart);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BlockRawAltStringChunk) {
				{
				{
				setState(161);
				match(BlockRawAltStringChunk);
				}
				}
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(167);
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
			setState(169);
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
			setState(171);
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
			setState(173);
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
			setState(187);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(175);
				match(UNDERSCORE);
				setState(176);
				rangeOp();
				setState(177);
				match(IntegerLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(179);
				match(IntegerLiteral);
				setState(180);
				rangeOp();
				setState(181);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(183);
				match(IntegerLiteral);
				setState(184);
				rangeOp();
				setState(185);
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
			setState(201);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(189);
				match(UNDERSCORE);
				setState(190);
				rangeOp();
				setState(191);
				match(LongLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(193);
				match(LongLiteral);
				setState(194);
				rangeOp();
				setState(195);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(197);
				match(LongLiteral);
				setState(198);
				rangeOp();
				setState(199);
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
			setState(215);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(203);
				match(UNDERSCORE);
				setState(204);
				rangeOp();
				setState(205);
				match(FloatLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(207);
				match(FloatLiteral);
				setState(208);
				rangeOp();
				setState(209);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(211);
				match(FloatLiteral);
				setState(212);
				rangeOp();
				setState(213);
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
			setState(229);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(217);
				match(UNDERSCORE);
				setState(218);
				rangeOp();
				setState(219);
				match(DoubleLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(221);
				match(DoubleLiteral);
				setState(222);
				rangeOp();
				setState(223);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(225);
				match(DoubleLiteral);
				setState(226);
				rangeOp();
				setState(227);
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
			setState(243);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(231);
				match(UNDERSCORE);
				setState(232);
				rangeOp();
				setState(233);
				match(DecimalLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(235);
				match(DecimalLiteral);
				setState(236);
				rangeOp();
				setState(237);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(239);
				match(DecimalLiteral);
				setState(240);
				rangeOp();
				setState(241);
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
			setState(257);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(245);
				match(UNDERSCORE);
				setState(246);
				rangeOp();
				setState(247);
				duration();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(249);
				duration();
				setState(250);
				rangeOp();
				setState(251);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(253);
				duration();
				setState(254);
				rangeOp();
				setState(255);
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
			setState(271);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(259);
				match(UNDERSCORE);
				setState(260);
				rangeOp();
				setState(261);
				dateTime();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(263);
				dateTime();
				setState(264);
				rangeOp();
				setState(265);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(267);
				dateTime();
				setState(268);
				rangeOp();
				setState(269);
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
			setState(285);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(273);
				match(UNDERSCORE);
				setState(274);
				rangeOp();
				setState(275);
				match(Version);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(277);
				match(Version);
				setState(278);
				rangeOp();
				setState(279);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(281);
				match(Version);
				setState(282);
				rangeOp();
				setState(283);
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
			setState(299);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(287);
				match(UNDERSCORE);
				setState(288);
				rangeOp();
				setState(289);
				match(CharLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(291);
				match(CharLiteral);
				setState(292);
				rangeOp();
				setState(293);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(295);
				match(CharLiteral);
				setState(296);
				rangeOp();
				setState(297);
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
			setState(313);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(301);
				match(UNDERSCORE);
				setState(302);
				rangeOp();
				setState(303);
				stringLiteral();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(305);
				stringLiteral();
				setState(306);
				rangeOp();
				setState(307);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(309);
				stringLiteral();
				setState(310);
				rangeOp();
				setState(311);
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
			setState(327);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(315);
				match(UNDERSCORE);
				setState(316);
				rangeOp();
				setState(317);
				quantity();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(319);
				quantity();
				setState(320);
				rangeOp();
				setState(321);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(323);
				quantity();
				setState(324);
				rangeOp();
				setState(325);
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
			setState(340);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(329);
				intRange();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(330);
				longRange();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(331);
				floatRange();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(332);
				doubleRange();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(333);
				decimalRange();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(334);
				durationRange();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(335);
				dateTimeRange();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(336);
				versionRange();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(337);
				charRange();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(338);
				stringRange();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(339);
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
			setState(342);
			match(BLOB_START);
			setState(344); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(343);
				match(BLOB_DATA);
				}
				}
				setState(346); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==BLOB_DATA );
			setState(348);
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
			setState(351); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(350);
					value();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(353); 
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
			setState(355);
			nsName();
			setState(356);
			match(EQUALS);
			setState(357);
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
			setState(360); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(359);
					attribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(362); 
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
			setState(366);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
			case 1:
				{
				setState(364);
				match(ID);
				setState(365);
				match(COLON);
				}
				break;
			}
			setState(368);
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
			setState(403);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(370);
				match(LSQUARE);
				setState(374);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(371);
					match(NL);
					}
					}
					setState(376);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(377);
				value();
				setState(390);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(379);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==COMMA) {
							{
							setState(378);
							match(COMMA);
							}
						}

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
						}
						} 
					}
					setState(392);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,33,_ctx);
				}
				setState(396);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(393);
					match(NL);
					}
					}
					setState(398);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(399);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(401);
				match(LSQUARE);
				setState(402);
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
			setState(408);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(405);
				match(NL);
				}
				}
				setState(410);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(411);
			value();
			setState(412);
			match(EQUALS);
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(413);
				match(NL);
				}
				}
				setState(418);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(419);
			value();
			setState(423);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,38,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(420);
					match(NL);
					}
					} 
				}
				setState(425);
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
			setState(460);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(426);
				match(LSQUARE);
				setState(430);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(427);
						match(NL);
						}
						} 
					}
					setState(432);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
				}
				setState(433);
				pair();
				setState(446);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(435);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==COMMA) {
							{
							setState(434);
							match(COMMA);
							}
						}

						setState(440);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
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
							_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
						}
						setState(443);
						pair();
						}
						} 
					}
					setState(448);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
				}
				setState(452);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(449);
					match(NL);
					}
					}
					setState(454);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(455);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(457);
				match(LSQUARE);
				setState(458);
				match(EQUALS);
				setState(459);
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
		enterRule(_localctx, 60, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(462);
			match(AT);
			setState(463);
			nsName();
			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(464);
				match(LPAREN);
				setState(466);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
				case 1:
					{
					setState(465);
					valueList();
					}
					break;
				}
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(468);
					attributeList();
					}
				}

				setState(471);
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
		enterRule(_localctx, 62, RULE_annotationList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(474);
			annotation();
			setState(484);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,49,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(478);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==NL) {
						{
						{
						setState(475);
						match(NL);
						}
						}
						setState(480);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					setState(481);
					annotation();
					}
					} 
				}
				setState(486);
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
		enterRule(_localctx, 64, RULE_dateTime);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(487);
			match(Date);
			setState(489);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Time) {
				{
				setState(488);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3D\u01ee\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\7\2F\n\2\f\2\16\2I\13\2\3\2\3\2\7\2M\n\2\f\2\16\2P\13"+
		"\2\7\2R\n\2\f\2\16\2U\13\2\3\3\3\3\7\3Y\n\3\f\3\16\3\\\13\3\5\3^\n\3\3"+
		"\3\3\3\5\3b\n\3\3\3\5\3e\n\3\3\3\5\3h\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"p\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0088\n\4\3\5\3\5\3\5\3\5\3\5\5\5\u008f\n"+
		"\5\3\6\3\6\7\6\u0093\n\6\f\6\16\6\u0096\13\6\3\6\3\6\3\7\3\7\7\7\u009c"+
		"\n\7\f\7\16\7\u009f\13\7\3\7\3\7\3\b\3\b\7\b\u00a5\n\b\f\b\16\b\u00a8"+
		"\13\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\5\f\u00be\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\5\r\u00cc\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\5\16\u00da\n\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\5\17\u00e8\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\5\20\u00f6\n\20\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u0104\n\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u0112\n\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u0120\n\23\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u012e\n\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u013c\n\25\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u014a\n\26"+
		"\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0157\n\27"+
		"\3\30\3\30\6\30\u015b\n\30\r\30\16\30\u015c\3\30\3\30\3\31\6\31\u0162"+
		"\n\31\r\31\16\31\u0163\3\32\3\32\3\32\3\32\3\33\6\33\u016b\n\33\r\33\16"+
		"\33\u016c\3\34\3\34\5\34\u0171\n\34\3\34\3\34\3\35\3\35\7\35\u0177\n\35"+
		"\f\35\16\35\u017a\13\35\3\35\3\35\5\35\u017e\n\35\3\35\7\35\u0181\n\35"+
		"\f\35\16\35\u0184\13\35\3\35\7\35\u0187\n\35\f\35\16\35\u018a\13\35\3"+
		"\35\7\35\u018d\n\35\f\35\16\35\u0190\13\35\3\35\3\35\3\35\3\35\5\35\u0196"+
		"\n\35\3\36\7\36\u0199\n\36\f\36\16\36\u019c\13\36\3\36\3\36\3\36\7\36"+
		"\u01a1\n\36\f\36\16\36\u01a4\13\36\3\36\3\36\7\36\u01a8\n\36\f\36\16\36"+
		"\u01ab\13\36\3\37\3\37\7\37\u01af\n\37\f\37\16\37\u01b2\13\37\3\37\3\37"+
		"\5\37\u01b6\n\37\3\37\7\37\u01b9\n\37\f\37\16\37\u01bc\13\37\3\37\7\37"+
		"\u01bf\n\37\f\37\16\37\u01c2\13\37\3\37\7\37\u01c5\n\37\f\37\16\37\u01c8"+
		"\13\37\3\37\3\37\3\37\3\37\3\37\5\37\u01cf\n\37\3 \3 \3 \3 \5 \u01d5\n"+
		" \3 \5 \u01d8\n \3 \5 \u01db\n \3!\3!\7!\u01df\n!\f!\16!\u01e2\13!\3!"+
		"\7!\u01e5\n!\f!\16!\u01e8\13!\3\"\3\"\5\"\u01ec\n\"\3\"\2\2#\2\4\6\b\n"+
		"\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@B\2\5\3\2\25\33"+
		"\3\2\36\37\3\2 #\2\u022c\2G\3\2\2\2\4d\3\2\2\2\6\u0087\3\2\2\2\b\u008e"+
		"\3\2\2\2\n\u0090\3\2\2\2\f\u0099\3\2\2\2\16\u00a2\3\2\2\2\20\u00ab\3\2"+
		"\2\2\22\u00ad\3\2\2\2\24\u00af\3\2\2\2\26\u00bd\3\2\2\2\30\u00cb\3\2\2"+
		"\2\32\u00d9\3\2\2\2\34\u00e7\3\2\2\2\36\u00f5\3\2\2\2 \u0103\3\2\2\2\""+
		"\u0111\3\2\2\2$\u011f\3\2\2\2&\u012d\3\2\2\2(\u013b\3\2\2\2*\u0149\3\2"+
		"\2\2,\u0156\3\2\2\2.\u0158\3\2\2\2\60\u0161\3\2\2\2\62\u0165\3\2\2\2\64"+
		"\u016a\3\2\2\2\66\u0170\3\2\2\28\u0195\3\2\2\2:\u019a\3\2\2\2<\u01ce\3"+
		"\2\2\2>\u01d0\3\2\2\2@\u01dc\3\2\2\2B\u01e9\3\2\2\2DF\79\2\2ED\3\2\2\2"+
		"FI\3\2\2\2GE\3\2\2\2GH\3\2\2\2HS\3\2\2\2IG\3\2\2\2JN\5\4\3\2KM\79\2\2"+
		"LK\3\2\2\2MP\3\2\2\2NL\3\2\2\2NO\3\2\2\2OR\3\2\2\2PN\3\2\2\2QJ\3\2\2\2"+
		"RU\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\3\3\2\2\2US\3\2\2\2VZ\5@!\2WY\79\2\2X"+
		"W\3\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[^\3\2\2\2\\Z\3\2\2\2]V\3\2\2"+
		"\2]^\3\2\2\2^_\3\2\2\2_a\5\66\34\2`b\5\60\31\2a`\3\2\2\2ab\3\2\2\2be\3"+
		"\2\2\2ce\5\60\31\2d]\3\2\2\2dc\3\2\2\2eg\3\2\2\2fh\5\64\33\2gf\3\2\2\2"+
		"gh\3\2\2\2ho\3\2\2\2ip\7&\2\2jp\79\2\2kl\7(\2\2lm\5\2\2\2mn\7)\2\2np\3"+
		"\2\2\2oi\3\2\2\2oj\3\2\2\2ok\3\2\2\2op\3\2\2\2p\5\3\2\2\2q\u0088\5\b\5"+
		"\2r\u0088\7\24\2\2s\u0088\7\65\2\2t\u0088\7\n\2\2u\u0088\7\13\2\2v\u0088"+
		"\7\f\2\2w\u0088\7\r\2\2x\u0088\7\7\2\2y\u0088\7\b\2\2z\u0088\7\t\2\2{"+
		"\u0088\7\4\2\2|\u0088\7\5\2\2}\u0088\7\3\2\2~\u0088\58\35\2\177\u0088"+
		"\5<\37\2\u0080\u0088\5B\"\2\u0081\u0088\5\20\t\2\u0082\u0088\5\22\n\2"+
		"\u0083\u0088\7\6\2\2\u0084\u0088\5,\27\2\u0085\u0088\7\16\2\2\u0086\u0088"+
		"\5.\30\2\u0087q\3\2\2\2\u0087r\3\2\2\2\u0087s\3\2\2\2\u0087t\3\2\2\2\u0087"+
		"u\3\2\2\2\u0087v\3\2\2\2\u0087w\3\2\2\2\u0087x\3\2\2\2\u0087y\3\2\2\2"+
		"\u0087z\3\2\2\2\u0087{\3\2\2\2\u0087|\3\2\2\2\u0087}\3\2\2\2\u0087~\3"+
		"\2\2\2\u0087\177\3\2\2\2\u0087\u0080\3\2\2\2\u0087\u0081\3\2\2\2\u0087"+
		"\u0082\3\2\2\2\u0087\u0083\3\2\2\2\u0087\u0084\3\2\2\2\u0087\u0085\3\2"+
		"\2\2\u0087\u0086\3\2\2\2\u0088\7\3\2\2\2\u0089\u008f\7\17\2\2\u008a\u008f"+
		"\7\20\2\2\u008b\u008f\5\n\6\2\u008c\u008f\5\f\7\2\u008d\u008f\5\16\b\2"+
		"\u008e\u0089\3\2\2\2\u008e\u008a\3\2\2\2\u008e\u008b\3\2\2\2\u008e\u008c"+
		"\3\2\2\2\u008e\u008d\3\2\2\2\u008f\t\3\2\2\2\u0090\u0094\7\21\2\2\u0091"+
		"\u0093\7=\2\2\u0092\u0091\3\2\2\2\u0093\u0096\3\2\2\2\u0094\u0092\3\2"+
		"\2\2\u0094\u0095\3\2\2\2\u0095\u0097\3\2\2\2\u0096\u0094\3\2\2\2\u0097"+
		"\u0098\7>\2\2\u0098\13\3\2\2\2\u0099\u009d\7\22\2\2\u009a\u009c\7@\2\2"+
		"\u009b\u009a\3\2\2\2\u009c\u009f\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e"+
		"\3\2\2\2\u009e\u00a0\3\2\2\2\u009f\u009d\3\2\2\2\u00a0\u00a1\7A\2\2\u00a1"+
		"\r\3\2\2\2\u00a2\u00a6\7\23\2\2\u00a3\u00a5\7C\2\2\u00a4\u00a3\3\2\2\2"+
		"\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a9"+
		"\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00aa\7D\2\2\u00aa\17\3\2\2\2\u00ab"+
		"\u00ac\t\2\2\2\u00ac\21\3\2\2\2\u00ad\u00ae\t\3\2\2\u00ae\23\3\2\2\2\u00af"+
		"\u00b0\t\4\2\2\u00b0\25\3\2\2\2\u00b1\u00b2\7\63\2\2\u00b2\u00b3\5\24"+
		"\13\2\u00b3\u00b4\7\n\2\2\u00b4\u00be\3\2\2\2\u00b5\u00b6\7\n\2\2\u00b6"+
		"\u00b7\5\24\13\2\u00b7\u00b8\7\63\2\2\u00b8\u00be\3\2\2\2\u00b9\u00ba"+
		"\7\n\2\2\u00ba\u00bb\5\24\13\2\u00bb\u00bc\7\n\2\2\u00bc\u00be\3\2\2\2"+
		"\u00bd\u00b1\3\2\2\2\u00bd\u00b5\3\2\2\2\u00bd\u00b9\3\2\2\2\u00be\27"+
		"\3\2\2\2\u00bf\u00c0\7\63\2\2\u00c0\u00c1\5\24\13\2\u00c1\u00c2\7\r\2"+
		"\2\u00c2\u00cc\3\2\2\2\u00c3\u00c4\7\r\2\2\u00c4\u00c5\5\24\13\2\u00c5"+
		"\u00c6\7\63\2\2\u00c6\u00cc\3\2\2\2\u00c7\u00c8\7\r\2\2\u00c8\u00c9\5"+
		"\24\13\2\u00c9\u00ca\7\r\2\2\u00ca\u00cc\3\2\2\2\u00cb\u00bf\3\2\2\2\u00cb"+
		"\u00c3\3\2\2\2\u00cb\u00c7\3\2\2\2\u00cc\31\3\2\2\2\u00cd\u00ce\7\63\2"+
		"\2\u00ce\u00cf\5\24\13\2\u00cf\u00d0\7\7\2\2\u00d0\u00da\3\2\2\2\u00d1"+
		"\u00d2\7\7\2\2\u00d2\u00d3\5\24\13\2\u00d3\u00d4\7\63\2\2\u00d4\u00da"+
		"\3\2\2\2\u00d5\u00d6\7\7\2\2\u00d6\u00d7\5\24\13\2\u00d7\u00d8\7\7\2\2"+
		"\u00d8\u00da\3\2\2\2\u00d9\u00cd\3\2\2\2\u00d9\u00d1\3\2\2\2\u00d9\u00d5"+
		"\3\2\2\2\u00da\33\3\2\2\2\u00db\u00dc\7\63\2\2\u00dc\u00dd\5\24\13\2\u00dd"+
		"\u00de\7\b\2\2\u00de\u00e8\3\2\2\2\u00df\u00e0\7\b\2\2\u00e0\u00e1\5\24"+
		"\13\2\u00e1\u00e2\7\63\2\2\u00e2\u00e8\3\2\2\2\u00e3\u00e4\7\b\2\2\u00e4"+
		"\u00e5\5\24\13\2\u00e5\u00e6\7\b\2\2\u00e6\u00e8\3\2\2\2\u00e7\u00db\3"+
		"\2\2\2\u00e7\u00df\3\2\2\2\u00e7\u00e3\3\2\2\2\u00e8\35\3\2\2\2\u00e9"+
		"\u00ea\7\63\2\2\u00ea\u00eb\5\24\13\2\u00eb\u00ec\7\t\2\2\u00ec\u00f6"+
		"\3\2\2\2\u00ed\u00ee\7\t\2\2\u00ee\u00ef\5\24\13\2\u00ef\u00f0\7\63\2"+
		"\2\u00f0\u00f6\3\2\2\2\u00f1\u00f2\7\t\2\2\u00f2\u00f3\5\24\13\2\u00f3"+
		"\u00f4\7\t\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00e9\3\2\2\2\u00f5\u00ed\3\2"+
		"\2\2\u00f5\u00f1\3\2\2\2\u00f6\37\3\2\2\2\u00f7\u00f8\7\63\2\2\u00f8\u00f9"+
		"\5\24\13\2\u00f9\u00fa\5\20\t\2\u00fa\u0104\3\2\2\2\u00fb\u00fc\5\20\t"+
		"\2\u00fc\u00fd\5\24\13\2\u00fd\u00fe\7\63\2\2\u00fe\u0104\3\2\2\2\u00ff"+
		"\u0100\5\20\t\2\u0100\u0101\5\24\13\2\u0101\u0102\5\20\t\2\u0102\u0104"+
		"\3\2\2\2\u0103\u00f7\3\2\2\2\u0103\u00fb\3\2\2\2\u0103\u00ff\3\2\2\2\u0104"+
		"!\3\2\2\2\u0105\u0106\7\63\2\2\u0106\u0107\5\24\13\2\u0107\u0108\5B\""+
		"\2\u0108\u0112\3\2\2\2\u0109\u010a\5B\"\2\u010a\u010b\5\24\13\2\u010b"+
		"\u010c\7\63\2\2\u010c\u0112\3\2\2\2\u010d\u010e\5B\"\2\u010e\u010f\5\24"+
		"\13\2\u010f\u0110\5B\"\2\u0110\u0112\3\2\2\2\u0111\u0105\3\2\2\2\u0111"+
		"\u0109\3\2\2\2\u0111\u010d\3\2\2\2\u0112#\3\2\2\2\u0113\u0114\7\63\2\2"+
		"\u0114\u0115\5\24\13\2\u0115\u0116\7\16\2\2\u0116\u0120\3\2\2\2\u0117"+
		"\u0118\7\16\2\2\u0118\u0119\5\24\13\2\u0119\u011a\7\63\2\2\u011a\u0120"+
		"\3\2\2\2\u011b\u011c\7\16\2\2\u011c\u011d\5\24\13\2\u011d\u011e\7\16\2"+
		"\2\u011e\u0120\3\2\2\2\u011f\u0113\3\2\2\2\u011f\u0117\3\2\2\2\u011f\u011b"+
		"\3\2\2\2\u0120%\3\2\2\2\u0121\u0122\7\63\2\2\u0122\u0123\5\24\13\2\u0123"+
		"\u0124\7\24\2\2\u0124\u012e\3\2\2\2\u0125\u0126\7\24\2\2\u0126\u0127\5"+
		"\24\13\2\u0127\u0128\7\63\2\2\u0128\u012e\3\2\2\2\u0129\u012a\7\24\2\2"+
		"\u012a\u012b\5\24\13\2\u012b\u012c\7\24\2\2\u012c\u012e\3\2\2\2\u012d"+
		"\u0121\3\2\2\2\u012d\u0125\3\2\2\2\u012d\u0129\3\2\2\2\u012e\'\3\2\2\2"+
		"\u012f\u0130\7\63\2\2\u0130\u0131\5\24\13\2\u0131\u0132\5\b\5\2\u0132"+
		"\u013c\3\2\2\2\u0133\u0134\5\b\5\2\u0134\u0135\5\24\13\2\u0135\u0136\7"+
		"\63\2\2\u0136\u013c\3\2\2\2\u0137\u0138\5\b\5\2\u0138\u0139\5\24\13\2"+
		"\u0139\u013a\5\b\5\2\u013a\u013c\3\2\2\2\u013b\u012f\3\2\2\2\u013b\u0133"+
		"\3\2\2\2\u013b\u0137\3\2\2\2\u013c)\3\2\2\2\u013d\u013e\7\63\2\2\u013e"+
		"\u013f\5\24\13\2\u013f\u0140\5\22\n\2\u0140\u014a\3\2\2\2\u0141\u0142"+
		"\5\22\n\2\u0142\u0143\5\24\13\2\u0143\u0144\7\63\2\2\u0144\u014a\3\2\2"+
		"\2\u0145\u0146\5\22\n\2\u0146\u0147\5\24\13\2\u0147\u0148\5\22\n\2\u0148"+
		"\u014a\3\2\2\2\u0149\u013d\3\2\2\2\u0149\u0141\3\2\2\2\u0149\u0145\3\2"+
		"\2\2\u014a+\3\2\2\2\u014b\u0157\5\26\f\2\u014c\u0157\5\30\r\2\u014d\u0157"+
		"\5\32\16\2\u014e\u0157\5\34\17\2\u014f\u0157\5\36\20\2\u0150\u0157\5 "+
		"\21\2\u0151\u0157\5\"\22\2\u0152\u0157\5$\23\2\u0153\u0157\5&\24\2\u0154"+
		"\u0157\5(\25\2\u0155\u0157\5*\26\2\u0156\u014b\3\2\2\2\u0156\u014c\3\2"+
		"\2\2\u0156\u014d\3\2\2\2\u0156\u014e\3\2\2\2\u0156\u014f\3\2\2\2\u0156"+
		"\u0150\3\2\2\2\u0156\u0151\3\2\2\2\u0156\u0152\3\2\2\2\u0156\u0153\3\2"+
		"\2\2\u0156\u0154\3\2\2\2\u0156\u0155\3\2\2\2\u0157-\3\2\2\2\u0158\u015a"+
		"\7\64\2\2\u0159\u015b\7:\2\2\u015a\u0159\3\2\2\2\u015b\u015c\3\2\2\2\u015c"+
		"\u015a\3\2\2\2\u015c\u015d\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u015f\7;"+
		"\2\2\u015f/\3\2\2\2\u0160\u0162\5\6\4\2\u0161\u0160\3\2\2\2\u0162\u0163"+
		"\3\2\2\2\u0163\u0161\3\2\2\2\u0163\u0164\3\2\2\2\u0164\61\3\2\2\2\u0165"+
		"\u0166\5\66\34\2\u0166\u0167\7\'\2\2\u0167\u0168\5\6\4\2\u0168\63\3\2"+
		"\2\2\u0169\u016b\5\62\32\2\u016a\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c"+
		"\u016a\3\2\2\2\u016c\u016d\3\2\2\2\u016d\65\3\2\2\2\u016e\u016f\7\65\2"+
		"\2\u016f\u0171\7%\2\2\u0170\u016e\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0172"+
		"\3\2\2\2\u0172\u0173\7\65\2\2\u0173\67\3\2\2\2\u0174\u0178\7,\2\2\u0175"+
		"\u0177\79\2\2\u0176\u0175\3\2\2\2\u0177\u017a\3\2\2\2\u0178\u0176\3\2"+
		"\2\2\u0178\u0179\3\2\2\2\u0179\u017b\3\2\2\2\u017a\u0178\3\2\2\2\u017b"+
		"\u0188\5\6\4\2\u017c\u017e\7.\2\2\u017d\u017c\3\2\2\2\u017d\u017e\3\2"+
		"\2\2\u017e\u0182\3\2\2\2\u017f\u0181\79\2\2\u0180\u017f\3\2\2\2\u0181"+
		"\u0184\3\2\2\2\u0182\u0180\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0185\3\2"+
		"\2\2\u0184\u0182\3\2\2\2\u0185\u0187\5\6\4\2\u0186\u017d\3\2\2\2\u0187"+
		"\u018a\3\2\2\2\u0188\u0186\3\2\2\2\u0188\u0189\3\2\2\2\u0189\u018e\3\2"+
		"\2\2\u018a\u0188\3\2\2\2\u018b\u018d\79\2\2\u018c\u018b\3\2\2\2\u018d"+
		"\u0190\3\2\2\2\u018e\u018c\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u0191\3\2"+
		"\2\2\u0190\u018e\3\2\2\2\u0191\u0192\7-\2\2\u0192\u0196\3\2\2\2\u0193"+
		"\u0194\7,\2\2\u0194\u0196\7-\2\2\u0195\u0174\3\2\2\2\u0195\u0193\3\2\2"+
		"\2\u01969\3\2\2\2\u0197\u0199\79\2\2\u0198\u0197\3\2\2\2\u0199\u019c\3"+
		"\2\2\2\u019a\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b\u019d\3\2\2\2\u019c"+
		"\u019a\3\2\2\2\u019d\u019e\5\6\4\2\u019e\u01a2\7\'\2\2\u019f\u01a1\79"+
		"\2\2\u01a0\u019f\3\2\2\2\u01a1\u01a4\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2"+
		"\u01a3\3\2\2\2\u01a3\u01a5\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a5\u01a9\5\6"+
		"\4\2\u01a6\u01a8\79\2\2\u01a7\u01a6\3\2\2\2\u01a8\u01ab\3\2\2\2\u01a9"+
		"\u01a7\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa;\3\2\2\2\u01ab\u01a9\3\2\2\2"+
		"\u01ac\u01b0\7,\2\2\u01ad\u01af\79\2\2\u01ae\u01ad\3\2\2\2\u01af\u01b2"+
		"\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b3\3\2\2\2\u01b2"+
		"\u01b0\3\2\2\2\u01b3\u01c0\5:\36\2\u01b4\u01b6\7.\2\2\u01b5\u01b4\3\2"+
		"\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01ba\3\2\2\2\u01b7\u01b9\79\2\2\u01b8"+
		"\u01b7\3\2\2\2\u01b9\u01bc\3\2\2\2\u01ba\u01b8\3\2\2\2\u01ba\u01bb\3\2"+
		"\2\2\u01bb\u01bd\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bd\u01bf\5:\36\2\u01be"+
		"\u01b5\3\2\2\2\u01bf\u01c2\3\2\2\2\u01c0\u01be\3\2\2\2\u01c0\u01c1\3\2"+
		"\2\2\u01c1\u01c6\3\2\2\2\u01c2\u01c0\3\2\2\2\u01c3\u01c5\79\2\2\u01c4"+
		"\u01c3\3\2\2\2\u01c5\u01c8\3\2\2\2\u01c6\u01c4\3\2\2\2\u01c6\u01c7\3\2"+
		"\2\2\u01c7\u01c9\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c9\u01ca\7-\2\2\u01ca"+
		"\u01cf\3\2\2\2\u01cb\u01cc\7,\2\2\u01cc\u01cd\7\'\2\2\u01cd\u01cf\7-\2"+
		"\2\u01ce\u01ac\3\2\2\2\u01ce\u01cb\3\2\2\2\u01cf=\3\2\2\2\u01d0\u01d1"+
		"\7\61\2\2\u01d1\u01da\5\66\34\2\u01d2\u01d4\7*\2\2\u01d3\u01d5\5\60\31"+
		"\2\u01d4\u01d3\3\2\2\2\u01d4\u01d5\3\2\2\2\u01d5\u01d7\3\2\2\2\u01d6\u01d8"+
		"\5\64\33\2\u01d7\u01d6\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01d9\3\2\2\2"+
		"\u01d9\u01db\7+\2\2\u01da\u01d2\3\2\2\2\u01da\u01db\3\2\2\2\u01db?\3\2"+
		"\2\2\u01dc\u01e6\5> \2\u01dd\u01df\79\2\2\u01de\u01dd\3\2\2\2\u01df\u01e2"+
		"\3\2\2\2\u01e0\u01de\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e3\3\2\2\2\u01e2"+
		"\u01e0\3\2\2\2\u01e3\u01e5\5> \2\u01e4\u01e0\3\2\2\2\u01e5\u01e8\3\2\2"+
		"\2\u01e6\u01e4\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7A\3\2\2\2\u01e8\u01e6"+
		"\3\2\2\2\u01e9\u01eb\7\34\2\2\u01ea\u01ec\7\35\2\2\u01eb\u01ea\3\2\2\2"+
		"\u01eb\u01ec\3\2\2\2\u01ecC\3\2\2\2\65GNSZ]adgo\u0087\u008e\u0094\u009d"+
		"\u00a6\u00bd\u00cb\u00d9\u00e7\u00f5\u0103\u0111\u011f\u012d\u013b\u0149"+
		"\u0156\u015c\u0163\u016c\u0170\u0178\u017d\u0182\u0188\u018e\u0195\u019a"+
		"\u01a2\u01a9\u01b0\u01b5\u01ba\u01c0\u01c6\u01ce\u01d4\u01d7\u01da\u01e0"+
		"\u01e6\u01eb";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}