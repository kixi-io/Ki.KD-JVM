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
		BasicString=13, RawString=14, BlockBasicString=15, BlockRawString=16, 
		BlockRawAltString=17, CharLiteral=18, CompoundDuration=19, DayDuration=20, 
		HourDuration=21, MinuteDuration=22, SecondDuration=23, MillisecondDuration=24, 
		NanosecondDuration=25, Date=26, Time=27, TimeZone=28, InclusiveRangeOp=29, 
		ExclusiveRangeOp=30, ExclusiveLeftOp=31, ExclusiveRightOp=32, DOT=33, 
		COLON=34, SEMICOLON=35, EQUALS=36, OPEN=37, CLOSE=38, LPAREN=39, RPAREN=40, 
		LSQUARE=41, RSQUARE=42, COMMA=43, SLASH=44, DASH=45, AT=46, PLUS=47, UNDERSCORE=48, 
		Base64=49, ID=50, BlockComment=51, LineComment=52, WS=53, NL=54;
	public static final int
		RULE_tagList = 0, RULE_tag = 1, RULE_value = 2, RULE_stringLiteral = 3, 
		RULE_duration = 4, RULE_rangeOp = 5, RULE_intRange = 6, RULE_longRange = 7, 
		RULE_floatRange = 8, RULE_doubleRange = 9, RULE_decimalRange = 10, RULE_durationRange = 11, 
		RULE_dateTimeRange = 12, RULE_versionRange = 13, RULE_charRange = 14, 
		RULE_stringRange = 15, RULE_range = 16, RULE_valueList = 17, RULE_attribute = 18, 
		RULE_attributeList = 19, RULE_nsName = 20, RULE_list = 21, RULE_pair = 22, 
		RULE_map = 23, RULE_annotation = 24, RULE_annotationList = 25, RULE_dateTime = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"tagList", "tag", "value", "stringLiteral", "duration", "rangeOp", "intRange", 
			"longRange", "floatRange", "doubleRange", "decimalRange", "durationRange", 
			"dateTimeRange", "versionRange", "charRange", "stringRange", "range", 
			"valueList", "attribute", "attributeList", "nsName", "list", "pair", 
			"map", "annotation", "annotationList", "dateTime"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "'..'", "'<..<'", "'<..'", "'..<'", "'.'", 
			"':'", "';'", "'='", "'{'", "'}'", "'('", "')'", "'['", "']'", "','", 
			"'/'", "'-'", "'@'", "'+'", "'_'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NULL", "TRUE", "FALSE", "URL", "FloatLiteral", "DoubleLiteral", 
			"DecimalLiteral", "IntegerLiteral", "HexLiteral", "BinLiteral", "LongLiteral", 
			"Version", "BasicString", "RawString", "BlockBasicString", "BlockRawString", 
			"BlockRawAltString", "CharLiteral", "CompoundDuration", "DayDuration", 
			"HourDuration", "MinuteDuration", "SecondDuration", "MillisecondDuration", 
			"NanosecondDuration", "Date", "Time", "TimeZone", "InclusiveRangeOp", 
			"ExclusiveRangeOp", "ExclusiveLeftOp", "ExclusiveRightOp", "DOT", "COLON", 
			"SEMICOLON", "EQUALS", "OPEN", "CLOSE", "LPAREN", "RPAREN", "LSQUARE", 
			"RSQUARE", "COMMA", "SLASH", "DASH", "AT", "PLUS", "UNDERSCORE", "Base64", 
			"ID", "BlockComment", "LineComment", "WS", "NL"
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
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(54);
				match(NL);
				}
				}
				setState(59);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << URL) | (1L << FloatLiteral) | (1L << DoubleLiteral) | (1L << DecimalLiteral) | (1L << IntegerLiteral) | (1L << HexLiteral) | (1L << BinLiteral) | (1L << LongLiteral) | (1L << Version) | (1L << BasicString) | (1L << RawString) | (1L << BlockBasicString) | (1L << BlockRawString) | (1L << BlockRawAltString) | (1L << CharLiteral) | (1L << CompoundDuration) | (1L << DayDuration) | (1L << HourDuration) | (1L << MinuteDuration) | (1L << SecondDuration) | (1L << MillisecondDuration) | (1L << NanosecondDuration) | (1L << Date) | (1L << LSQUARE) | (1L << AT) | (1L << UNDERSCORE) | (1L << Base64) | (1L << ID))) != 0)) {
				{
				{
				setState(60);
				tag();
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(61);
					match(NL);
					}
					}
					setState(66);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(71);
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
			setState(86);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				{
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==AT) {
					{
					setState(72);
					annotationList();
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

				setState(81);
				nsName();
				setState(83);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(82);
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
				setState(85);
				valueList();
				}
				}
				break;
			}
			setState(89);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(88);
				attributeList();
				}
				break;
			}
			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				setState(91);
				match(SEMICOLON);
				}
				break;
			case 2:
				{
				setState(92);
				match(NL);
				}
				break;
			case 3:
				{
				{
				setState(93);
				match(OPEN);
				setState(94);
				tagList();
				setState(95);
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
		public TerminalNode URL() { return getToken(KDParser.URL, 0); }
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
		}
		public TerminalNode Version() { return getToken(KDParser.Version, 0); }
		public TerminalNode Base64() { return getToken(KDParser.Base64, 0); }
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
			setState(120);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				stringLiteral();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				match(CharLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				match(ID);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(102);
				match(IntegerLiteral);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(103);
				match(HexLiteral);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(104);
				match(BinLiteral);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(105);
				match(LongLiteral);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(106);
				match(FloatLiteral);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(107);
				match(DoubleLiteral);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(108);
				match(DecimalLiteral);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(109);
				match(TRUE);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(110);
				match(FALSE);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(111);
				match(NULL);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(112);
				list();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(113);
				map();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(114);
				dateTime();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(115);
				duration();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(116);
				match(URL);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(117);
				range();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(118);
				match(Version);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(119);
				match(Base64);
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
		public TerminalNode BasicString() { return getToken(KDParser.BasicString, 0); }
		public TerminalNode RawString() { return getToken(KDParser.RawString, 0); }
		public TerminalNode BlockBasicString() { return getToken(KDParser.BlockBasicString, 0); }
		public TerminalNode BlockRawString() { return getToken(KDParser.BlockRawString, 0); }
		public TerminalNode BlockRawAltString() { return getToken(KDParser.BlockRawAltString, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BasicString) | (1L << RawString) | (1L << BlockBasicString) | (1L << BlockRawString) | (1L << BlockRawAltString))) != 0)) ) {
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
		enterRule(_localctx, 8, RULE_duration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
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
		enterRule(_localctx, 10, RULE_rangeOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
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
		enterRule(_localctx, 12, RULE_intRange);
		try {
			setState(140);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(128);
				match(UNDERSCORE);
				setState(129);
				rangeOp();
				setState(130);
				match(IntegerLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(132);
				match(IntegerLiteral);
				setState(133);
				rangeOp();
				setState(134);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(136);
				match(IntegerLiteral);
				setState(137);
				rangeOp();
				setState(138);
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
		enterRule(_localctx, 14, RULE_longRange);
		try {
			setState(154);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(142);
				match(UNDERSCORE);
				setState(143);
				rangeOp();
				setState(144);
				match(LongLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(146);
				match(LongLiteral);
				setState(147);
				rangeOp();
				setState(148);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(150);
				match(LongLiteral);
				setState(151);
				rangeOp();
				setState(152);
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
		enterRule(_localctx, 16, RULE_floatRange);
		try {
			setState(168);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(156);
				match(UNDERSCORE);
				setState(157);
				rangeOp();
				setState(158);
				match(FloatLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(160);
				match(FloatLiteral);
				setState(161);
				rangeOp();
				setState(162);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(164);
				match(FloatLiteral);
				setState(165);
				rangeOp();
				setState(166);
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
		enterRule(_localctx, 18, RULE_doubleRange);
		try {
			setState(182);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(170);
				match(UNDERSCORE);
				setState(171);
				rangeOp();
				setState(172);
				match(DoubleLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(174);
				match(DoubleLiteral);
				setState(175);
				rangeOp();
				setState(176);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(178);
				match(DoubleLiteral);
				setState(179);
				rangeOp();
				setState(180);
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
		enterRule(_localctx, 20, RULE_decimalRange);
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
				match(DecimalLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(188);
				match(DecimalLiteral);
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
				match(DecimalLiteral);
				setState(193);
				rangeOp();
				setState(194);
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
		enterRule(_localctx, 22, RULE_durationRange);
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
				duration();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(202);
				duration();
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
				duration();
				setState(207);
				rangeOp();
				setState(208);
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
		enterRule(_localctx, 24, RULE_dateTimeRange);
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
				dateTime();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(216);
				dateTime();
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
				dateTime();
				setState(221);
				rangeOp();
				setState(222);
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
		enterRule(_localctx, 26, RULE_versionRange);
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
				match(Version);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(230);
				match(Version);
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
				match(Version);
				setState(235);
				rangeOp();
				setState(236);
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
		enterRule(_localctx, 28, RULE_charRange);
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
				match(CharLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(244);
				match(CharLiteral);
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
				match(CharLiteral);
				setState(249);
				rangeOp();
				setState(250);
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
		enterRule(_localctx, 30, RULE_stringRange);
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
				stringLiteral();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(258);
				stringLiteral();
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
				stringLiteral();
				setState(263);
				rangeOp();
				setState(264);
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
		enterRule(_localctx, 32, RULE_range);
		try {
			setState(278);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(268);
				intRange();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(269);
				longRange();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(270);
				floatRange();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(271);
				doubleRange();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(272);
				decimalRange();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(273);
				durationRange();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(274);
				dateTimeRange();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(275);
				versionRange();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(276);
				charRange();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(277);
				stringRange();
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
		enterRule(_localctx, 34, RULE_valueList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(281); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(280);
					value();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(283); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
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
		enterRule(_localctx, 36, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			nsName();
			setState(286);
			match(EQUALS);
			setState(287);
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
		enterRule(_localctx, 38, RULE_attributeList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(290); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(289);
					attribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(292); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
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
		enterRule(_localctx, 40, RULE_nsName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(294);
				match(ID);
				setState(295);
				match(COLON);
				}
				break;
			}
			setState(298);
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
		enterRule(_localctx, 42, RULE_list);
		int _la;
		try {
			setState(327);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(300);
				match(LSQUARE);
				setState(304);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(301);
					match(NL);
					}
					}
					setState(306);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(307);
				value();
				setState(314);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << URL) | (1L << FloatLiteral) | (1L << DoubleLiteral) | (1L << DecimalLiteral) | (1L << IntegerLiteral) | (1L << HexLiteral) | (1L << BinLiteral) | (1L << LongLiteral) | (1L << Version) | (1L << BasicString) | (1L << RawString) | (1L << BlockBasicString) | (1L << BlockRawString) | (1L << BlockRawAltString) | (1L << CharLiteral) | (1L << CompoundDuration) | (1L << DayDuration) | (1L << HourDuration) | (1L << MinuteDuration) | (1L << SecondDuration) | (1L << MillisecondDuration) | (1L << NanosecondDuration) | (1L << Date) | (1L << LSQUARE) | (1L << COMMA) | (1L << UNDERSCORE) | (1L << Base64) | (1L << ID))) != 0)) {
					{
					{
					setState(309);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(308);
						match(COMMA);
						}
					}

					setState(311);
					value();
					}
					}
					setState(316);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(320);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(317);
					match(NL);
					}
					}
					setState(322);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(323);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(325);
				match(LSQUARE);
				setState(326);
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
		enterRule(_localctx, 44, RULE_pair);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(329);
				match(NL);
				}
				}
				setState(334);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(335);
			value();
			setState(336);
			match(EQUALS);
			setState(340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(337);
				match(NL);
				}
				}
				setState(342);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(343);
			value();
			setState(347);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(344);
					match(NL);
					}
					} 
				}
				setState(349);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
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
		enterRule(_localctx, 46, RULE_map);
		int _la;
		try {
			int _alt;
			setState(378);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(350);
				match(LSQUARE);
				setState(354);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(351);
						match(NL);
						}
						} 
					}
					setState(356);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				}
				setState(357);
				pair();
				setState(364);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(359);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==COMMA) {
							{
							setState(358);
							match(COMMA);
							}
						}

						setState(361);
						pair();
						}
						} 
					}
					setState(366);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
				}
				setState(370);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(367);
					match(NL);
					}
					}
					setState(372);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(373);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(375);
				match(LSQUARE);
				setState(376);
				match(EQUALS);
				setState(377);
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
		enterRule(_localctx, 48, RULE_annotation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			match(AT);
			setState(381);
			nsName();
			setState(390);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LPAREN) {
				{
				setState(382);
				match(LPAREN);
				setState(384);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(383);
					valueList();
					}
					break;
				}
				setState(387);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ID) {
					{
					setState(386);
					attributeList();
					}
				}

				setState(389);
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
		enterRule(_localctx, 50, RULE_annotationList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			annotation();
			setState(402);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
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
					annotation();
					}
					} 
				}
				setState(404);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
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
		public TerminalNode TimeZone() { return getToken(KDParser.TimeZone, 0); }
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
		enterRule(_localctx, 52, RULE_dateTime);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(405);
			match(Date);
			setState(410);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Time) {
				{
				setState(406);
				match(Time);
				setState(408);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TimeZone) {
					{
					setState(407);
					match(TimeZone);
					}
				}

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\38\u019f\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\7\2:\n\2\f\2\16\2=\13\2\3\2\3\2\7\2"+
		"A\n\2\f\2\16\2D\13\2\7\2F\n\2\f\2\16\2I\13\2\3\3\3\3\7\3M\n\3\f\3\16\3"+
		"P\13\3\5\3R\n\3\3\3\3\3\5\3V\n\3\3\3\5\3Y\n\3\3\3\5\3\\\n\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\5\3d\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4{\n\4\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u008f\n\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u009d\n\t\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00ab\n\n\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00b9\n\13\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00c7\n\f\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00d5\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\5\16\u00e3\n\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00f1\n\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00ff\n\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u010d\n\21\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u0119\n\22\3\23\6\23\u011c"+
		"\n\23\r\23\16\23\u011d\3\24\3\24\3\24\3\24\3\25\6\25\u0125\n\25\r\25\16"+
		"\25\u0126\3\26\3\26\5\26\u012b\n\26\3\26\3\26\3\27\3\27\7\27\u0131\n\27"+
		"\f\27\16\27\u0134\13\27\3\27\3\27\5\27\u0138\n\27\3\27\7\27\u013b\n\27"+
		"\f\27\16\27\u013e\13\27\3\27\7\27\u0141\n\27\f\27\16\27\u0144\13\27\3"+
		"\27\3\27\3\27\3\27\5\27\u014a\n\27\3\30\7\30\u014d\n\30\f\30\16\30\u0150"+
		"\13\30\3\30\3\30\3\30\7\30\u0155\n\30\f\30\16\30\u0158\13\30\3\30\3\30"+
		"\7\30\u015c\n\30\f\30\16\30\u015f\13\30\3\31\3\31\7\31\u0163\n\31\f\31"+
		"\16\31\u0166\13\31\3\31\3\31\5\31\u016a\n\31\3\31\7\31\u016d\n\31\f\31"+
		"\16\31\u0170\13\31\3\31\7\31\u0173\n\31\f\31\16\31\u0176\13\31\3\31\3"+
		"\31\3\31\3\31\3\31\5\31\u017d\n\31\3\32\3\32\3\32\3\32\5\32\u0183\n\32"+
		"\3\32\5\32\u0186\n\32\3\32\5\32\u0189\n\32\3\33\3\33\7\33\u018d\n\33\f"+
		"\33\16\33\u0190\13\33\3\33\7\33\u0193\n\33\f\33\16\33\u0196\13\33\3\34"+
		"\3\34\3\34\5\34\u019b\n\34\5\34\u019d\n\34\3\34\2\2\35\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66\2\5\3\2\17\23\3\2\25\33"+
		"\3\2\37\"\2\u01d6\2;\3\2\2\2\4X\3\2\2\2\6z\3\2\2\2\b|\3\2\2\2\n~\3\2\2"+
		"\2\f\u0080\3\2\2\2\16\u008e\3\2\2\2\20\u009c\3\2\2\2\22\u00aa\3\2\2\2"+
		"\24\u00b8\3\2\2\2\26\u00c6\3\2\2\2\30\u00d4\3\2\2\2\32\u00e2\3\2\2\2\34"+
		"\u00f0\3\2\2\2\36\u00fe\3\2\2\2 \u010c\3\2\2\2\"\u0118\3\2\2\2$\u011b"+
		"\3\2\2\2&\u011f\3\2\2\2(\u0124\3\2\2\2*\u012a\3\2\2\2,\u0149\3\2\2\2."+
		"\u014e\3\2\2\2\60\u017c\3\2\2\2\62\u017e\3\2\2\2\64\u018a\3\2\2\2\66\u0197"+
		"\3\2\2\28:\78\2\298\3\2\2\2:=\3\2\2\2;9\3\2\2\2;<\3\2\2\2<G\3\2\2\2=;"+
		"\3\2\2\2>B\5\4\3\2?A\78\2\2@?\3\2\2\2AD\3\2\2\2B@\3\2\2\2BC\3\2\2\2CF"+
		"\3\2\2\2DB\3\2\2\2E>\3\2\2\2FI\3\2\2\2GE\3\2\2\2GH\3\2\2\2H\3\3\2\2\2"+
		"IG\3\2\2\2JN\5\64\33\2KM\78\2\2LK\3\2\2\2MP\3\2\2\2NL\3\2\2\2NO\3\2\2"+
		"\2OR\3\2\2\2PN\3\2\2\2QJ\3\2\2\2QR\3\2\2\2RS\3\2\2\2SU\5*\26\2TV\5$\23"+
		"\2UT\3\2\2\2UV\3\2\2\2VY\3\2\2\2WY\5$\23\2XQ\3\2\2\2XW\3\2\2\2Y[\3\2\2"+
		"\2Z\\\5(\25\2[Z\3\2\2\2[\\\3\2\2\2\\c\3\2\2\2]d\7%\2\2^d\78\2\2_`\7\'"+
		"\2\2`a\5\2\2\2ab\7(\2\2bd\3\2\2\2c]\3\2\2\2c^\3\2\2\2c_\3\2\2\2cd\3\2"+
		"\2\2d\5\3\2\2\2e{\5\b\5\2f{\7\24\2\2g{\7\64\2\2h{\7\n\2\2i{\7\13\2\2j"+
		"{\7\f\2\2k{\7\r\2\2l{\7\7\2\2m{\7\b\2\2n{\7\t\2\2o{\7\4\2\2p{\7\5\2\2"+
		"q{\7\3\2\2r{\5,\27\2s{\5\60\31\2t{\5\66\34\2u{\5\n\6\2v{\7\6\2\2w{\5\""+
		"\22\2x{\7\16\2\2y{\7\63\2\2ze\3\2\2\2zf\3\2\2\2zg\3\2\2\2zh\3\2\2\2zi"+
		"\3\2\2\2zj\3\2\2\2zk\3\2\2\2zl\3\2\2\2zm\3\2\2\2zn\3\2\2\2zo\3\2\2\2z"+
		"p\3\2\2\2zq\3\2\2\2zr\3\2\2\2zs\3\2\2\2zt\3\2\2\2zu\3\2\2\2zv\3\2\2\2"+
		"zw\3\2\2\2zx\3\2\2\2zy\3\2\2\2{\7\3\2\2\2|}\t\2\2\2}\t\3\2\2\2~\177\t"+
		"\3\2\2\177\13\3\2\2\2\u0080\u0081\t\4\2\2\u0081\r\3\2\2\2\u0082\u0083"+
		"\7\62\2\2\u0083\u0084\5\f\7\2\u0084\u0085\7\n\2\2\u0085\u008f\3\2\2\2"+
		"\u0086\u0087\7\n\2\2\u0087\u0088\5\f\7\2\u0088\u0089\7\62\2\2\u0089\u008f"+
		"\3\2\2\2\u008a\u008b\7\n\2\2\u008b\u008c\5\f\7\2\u008c\u008d\7\n\2\2\u008d"+
		"\u008f\3\2\2\2\u008e\u0082\3\2\2\2\u008e\u0086\3\2\2\2\u008e\u008a\3\2"+
		"\2\2\u008f\17\3\2\2\2\u0090\u0091\7\62\2\2\u0091\u0092\5\f\7\2\u0092\u0093"+
		"\7\r\2\2\u0093\u009d\3\2\2\2\u0094\u0095\7\r\2\2\u0095\u0096\5\f\7\2\u0096"+
		"\u0097\7\62\2\2\u0097\u009d\3\2\2\2\u0098\u0099\7\r\2\2\u0099\u009a\5"+
		"\f\7\2\u009a\u009b\7\r\2\2\u009b\u009d\3\2\2\2\u009c\u0090\3\2\2\2\u009c"+
		"\u0094\3\2\2\2\u009c\u0098\3\2\2\2\u009d\21\3\2\2\2\u009e\u009f\7\62\2"+
		"\2\u009f\u00a0\5\f\7\2\u00a0\u00a1\7\7\2\2\u00a1\u00ab\3\2\2\2\u00a2\u00a3"+
		"\7\7\2\2\u00a3\u00a4\5\f\7\2\u00a4\u00a5\7\62\2\2\u00a5\u00ab\3\2\2\2"+
		"\u00a6\u00a7\7\7\2\2\u00a7\u00a8\5\f\7\2\u00a8\u00a9\7\7\2\2\u00a9\u00ab"+
		"\3\2\2\2\u00aa\u009e\3\2\2\2\u00aa\u00a2\3\2\2\2\u00aa\u00a6\3\2\2\2\u00ab"+
		"\23\3\2\2\2\u00ac\u00ad\7\62\2\2\u00ad\u00ae\5\f\7\2\u00ae\u00af\7\b\2"+
		"\2\u00af\u00b9\3\2\2\2\u00b0\u00b1\7\b\2\2\u00b1\u00b2\5\f\7\2\u00b2\u00b3"+
		"\7\62\2\2\u00b3\u00b9\3\2\2\2\u00b4\u00b5\7\b\2\2\u00b5\u00b6\5\f\7\2"+
		"\u00b6\u00b7\7\b\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00ac\3\2\2\2\u00b8\u00b0"+
		"\3\2\2\2\u00b8\u00b4\3\2\2\2\u00b9\25\3\2\2\2\u00ba\u00bb\7\62\2\2\u00bb"+
		"\u00bc\5\f\7\2\u00bc\u00bd\7\t\2\2\u00bd\u00c7\3\2\2\2\u00be\u00bf\7\t"+
		"\2\2\u00bf\u00c0\5\f\7\2\u00c0\u00c1\7\62\2\2\u00c1\u00c7\3\2\2\2\u00c2"+
		"\u00c3\7\t\2\2\u00c3\u00c4\5\f\7\2\u00c4\u00c5\7\t\2\2\u00c5\u00c7\3\2"+
		"\2\2\u00c6\u00ba\3\2\2\2\u00c6\u00be\3\2\2\2\u00c6\u00c2\3\2\2\2\u00c7"+
		"\27\3\2\2\2\u00c8\u00c9\7\62\2\2\u00c9\u00ca\5\f\7\2\u00ca\u00cb\5\n\6"+
		"\2\u00cb\u00d5\3\2\2\2\u00cc\u00cd\5\n\6\2\u00cd\u00ce\5\f\7\2\u00ce\u00cf"+
		"\7\62\2\2\u00cf\u00d5\3\2\2\2\u00d0\u00d1\5\n\6\2\u00d1\u00d2\5\f\7\2"+
		"\u00d2\u00d3\5\n\6\2\u00d3\u00d5\3\2\2\2\u00d4\u00c8\3\2\2\2\u00d4\u00cc"+
		"\3\2\2\2\u00d4\u00d0\3\2\2\2\u00d5\31\3\2\2\2\u00d6\u00d7\7\62\2\2\u00d7"+
		"\u00d8\5\f\7\2\u00d8\u00d9\5\66\34\2\u00d9\u00e3\3\2\2\2\u00da\u00db\5"+
		"\66\34\2\u00db\u00dc\5\f\7\2\u00dc\u00dd\7\62\2\2\u00dd\u00e3\3\2\2\2"+
		"\u00de\u00df\5\66\34\2\u00df\u00e0\5\f\7\2\u00e0\u00e1\5\66\34\2\u00e1"+
		"\u00e3\3\2\2\2\u00e2\u00d6\3\2\2\2\u00e2\u00da\3\2\2\2\u00e2\u00de\3\2"+
		"\2\2\u00e3\33\3\2\2\2\u00e4\u00e5\7\62\2\2\u00e5\u00e6\5\f\7\2\u00e6\u00e7"+
		"\7\16\2\2\u00e7\u00f1\3\2\2\2\u00e8\u00e9\7\16\2\2\u00e9\u00ea\5\f\7\2"+
		"\u00ea\u00eb\7\62\2\2\u00eb\u00f1\3\2\2\2\u00ec\u00ed\7\16\2\2\u00ed\u00ee"+
		"\5\f\7\2\u00ee\u00ef\7\16\2\2\u00ef\u00f1\3\2\2\2\u00f0\u00e4\3\2\2\2"+
		"\u00f0\u00e8\3\2\2\2\u00f0\u00ec\3\2\2\2\u00f1\35\3\2\2\2\u00f2\u00f3"+
		"\7\62\2\2\u00f3\u00f4\5\f\7\2\u00f4\u00f5\7\24\2\2\u00f5\u00ff\3\2\2\2"+
		"\u00f6\u00f7\7\24\2\2\u00f7\u00f8\5\f\7\2\u00f8\u00f9\7\62\2\2\u00f9\u00ff"+
		"\3\2\2\2\u00fa\u00fb\7\24\2\2\u00fb\u00fc\5\f\7\2\u00fc\u00fd\7\24\2\2"+
		"\u00fd\u00ff\3\2\2\2\u00fe\u00f2\3\2\2\2\u00fe\u00f6\3\2\2\2\u00fe\u00fa"+
		"\3\2\2\2\u00ff\37\3\2\2\2\u0100\u0101\7\62\2\2\u0101\u0102\5\f\7\2\u0102"+
		"\u0103\5\b\5\2\u0103\u010d\3\2\2\2\u0104\u0105\5\b\5\2\u0105\u0106\5\f"+
		"\7\2\u0106\u0107\7\62\2\2\u0107\u010d\3\2\2\2\u0108\u0109\5\b\5\2\u0109"+
		"\u010a\5\f\7\2\u010a\u010b\5\b\5\2\u010b\u010d\3\2\2\2\u010c\u0100\3\2"+
		"\2\2\u010c\u0104\3\2\2\2\u010c\u0108\3\2\2\2\u010d!\3\2\2\2\u010e\u0119"+
		"\5\16\b\2\u010f\u0119\5\20\t\2\u0110\u0119\5\22\n\2\u0111\u0119\5\24\13"+
		"\2\u0112\u0119\5\26\f\2\u0113\u0119\5\30\r\2\u0114\u0119\5\32\16\2\u0115"+
		"\u0119\5\34\17\2\u0116\u0119\5\36\20\2\u0117\u0119\5 \21\2\u0118\u010e"+
		"\3\2\2\2\u0118\u010f\3\2\2\2\u0118\u0110\3\2\2\2\u0118\u0111\3\2\2\2\u0118"+
		"\u0112\3\2\2\2\u0118\u0113\3\2\2\2\u0118\u0114\3\2\2\2\u0118\u0115\3\2"+
		"\2\2\u0118\u0116\3\2\2\2\u0118\u0117\3\2\2\2\u0119#\3\2\2\2\u011a\u011c"+
		"\5\6\4\2\u011b\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011d\u011b\3\2\2\2\u011d"+
		"\u011e\3\2\2\2\u011e%\3\2\2\2\u011f\u0120\5*\26\2\u0120\u0121\7&\2\2\u0121"+
		"\u0122\5\6\4\2\u0122\'\3\2\2\2\u0123\u0125\5&\24\2\u0124\u0123\3\2\2\2"+
		"\u0125\u0126\3\2\2\2\u0126\u0124\3\2\2\2\u0126\u0127\3\2\2\2\u0127)\3"+
		"\2\2\2\u0128\u0129\7\64\2\2\u0129\u012b\7$\2\2\u012a\u0128\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012d\7\64\2\2\u012d+\3\2\2\2"+
		"\u012e\u0132\7+\2\2\u012f\u0131\78\2\2\u0130\u012f\3\2\2\2\u0131\u0134"+
		"\3\2\2\2\u0132\u0130\3\2\2\2\u0132\u0133\3\2\2\2\u0133\u0135\3\2\2\2\u0134"+
		"\u0132\3\2\2\2\u0135\u013c\5\6\4\2\u0136\u0138\7-\2\2\u0137\u0136\3\2"+
		"\2\2\u0137\u0138\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u013b\5\6\4\2\u013a"+
		"\u0137\3\2\2\2\u013b\u013e\3\2\2\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2"+
		"\2\2\u013d\u0142\3\2\2\2\u013e\u013c\3\2\2\2\u013f\u0141\78\2\2\u0140"+
		"\u013f\3\2\2\2\u0141\u0144\3\2\2\2\u0142\u0140\3\2\2\2\u0142\u0143\3\2"+
		"\2\2\u0143\u0145\3\2\2\2\u0144\u0142\3\2\2\2\u0145\u0146\7,\2\2\u0146"+
		"\u014a\3\2\2\2\u0147\u0148\7+\2\2\u0148\u014a\7,\2\2\u0149\u012e\3\2\2"+
		"\2\u0149\u0147\3\2\2\2\u014a-\3\2\2\2\u014b\u014d\78\2\2\u014c\u014b\3"+
		"\2\2\2\u014d\u0150\3\2\2\2\u014e\u014c\3\2\2\2\u014e\u014f\3\2\2\2\u014f"+
		"\u0151\3\2\2\2\u0150\u014e\3\2\2\2\u0151\u0152\5\6\4\2\u0152\u0156\7&"+
		"\2\2\u0153\u0155\78\2\2\u0154\u0153\3\2\2\2\u0155\u0158\3\2\2\2\u0156"+
		"\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u0159\3\2\2\2\u0158\u0156\3\2"+
		"\2\2\u0159\u015d\5\6\4\2\u015a\u015c\78\2\2\u015b\u015a\3\2\2\2\u015c"+
		"\u015f\3\2\2\2\u015d\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015e/\3\2\2\2"+
		"\u015f\u015d\3\2\2\2\u0160\u0164\7+\2\2\u0161\u0163\78\2\2\u0162\u0161"+
		"\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162\3\2\2\2\u0164\u0165\3\2\2\2\u0165"+
		"\u0167\3\2\2\2\u0166\u0164\3\2\2\2\u0167\u016e\5.\30\2\u0168\u016a\7-"+
		"\2\2\u0169\u0168\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b\3\2\2\2\u016b"+
		"\u016d\5.\30\2\u016c\u0169\3\2\2\2\u016d\u0170\3\2\2\2\u016e\u016c\3\2"+
		"\2\2\u016e\u016f\3\2\2\2\u016f\u0174\3\2\2\2\u0170\u016e\3\2\2\2\u0171"+
		"\u0173\78\2\2\u0172\u0171\3\2\2\2\u0173\u0176\3\2\2\2\u0174\u0172\3\2"+
		"\2\2\u0174\u0175\3\2\2\2\u0175\u0177\3\2\2\2\u0176\u0174\3\2\2\2\u0177"+
		"\u0178\7,\2\2\u0178\u017d\3\2\2\2\u0179\u017a\7+\2\2\u017a\u017b\7&\2"+
		"\2\u017b\u017d\7,\2\2\u017c\u0160\3\2\2\2\u017c\u0179\3\2\2\2\u017d\61"+
		"\3\2\2\2\u017e\u017f\7\60\2\2\u017f\u0188\5*\26\2\u0180\u0182\7)\2\2\u0181"+
		"\u0183\5$\23\2\u0182\u0181\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0185\3\2"+
		"\2\2\u0184\u0186\5(\25\2\u0185\u0184\3\2\2\2\u0185\u0186\3\2\2\2\u0186"+
		"\u0187\3\2\2\2\u0187\u0189\7*\2\2\u0188\u0180\3\2\2\2\u0188\u0189\3\2"+
		"\2\2\u0189\63\3\2\2\2\u018a\u0194\5\62\32\2\u018b\u018d\78\2\2\u018c\u018b"+
		"\3\2\2\2\u018d\u0190\3\2\2\2\u018e\u018c\3\2\2\2\u018e\u018f\3\2\2\2\u018f"+
		"\u0191\3\2\2\2\u0190\u018e\3\2\2\2\u0191\u0193\5\62\32\2\u0192\u018e\3"+
		"\2\2\2\u0193\u0196\3\2\2\2\u0194\u0192\3\2\2\2\u0194\u0195\3\2\2\2\u0195"+
		"\65\3\2\2\2\u0196\u0194\3\2\2\2\u0197\u019c\7\34\2\2\u0198\u019a\7\35"+
		"\2\2\u0199\u019b\7\36\2\2\u019a\u0199\3\2\2\2\u019a\u019b\3\2\2\2\u019b"+
		"\u019d\3\2\2\2\u019c\u0198\3\2\2\2\u019c\u019d\3\2\2\2\u019d\67\3\2\2"+
		"\2.;BGNQUX[cz\u008e\u009c\u00aa\u00b8\u00c6\u00d4\u00e2\u00f0\u00fe\u010c"+
		"\u0118\u011d\u0126\u012a\u0132\u0137\u013c\u0142\u0149\u014e\u0156\u015d"+
		"\u0164\u0169\u016e\u0174\u017c\u0182\u0185\u0188\u018e\u0194\u019a\u019c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}