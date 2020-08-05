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
		BASE64=49, BASE64_DATA=50, ID=51, BlockComment=52, LineComment=53, WS=54, 
		NL=55;
	public static final int
		RULE_tagList = 0, RULE_tag = 1, RULE_value = 2, RULE_stringLiteral = 3, 
		RULE_duration = 4, RULE_rangeOp = 5, RULE_intRange = 6, RULE_longRange = 7, 
		RULE_floatRange = 8, RULE_doubleRange = 9, RULE_decimalRange = 10, RULE_durationRange = 11, 
		RULE_dateTimeRange = 12, RULE_versionRange = 13, RULE_charRange = 14, 
		RULE_stringRange = 15, RULE_range = 16, RULE_valueList = 17, RULE_attribute = 18, 
		RULE_attributeList = 19, RULE_nsName = 20, RULE_list = 21, RULE_pair = 22, 
		RULE_map = 23, RULE_base64 = 24, RULE_dateTime = 25;
	private static String[] makeRuleNames() {
		return new String[] {
			"tagList", "tag", "value", "stringLiteral", "duration", "rangeOp", "intRange", 
			"longRange", "floatRange", "doubleRange", "decimalRange", "durationRange", 
			"dateTimeRange", "versionRange", "charRange", "stringRange", "range", 
			"valueList", "attribute", "attributeList", "nsName", "list", "pair", 
			"map", "base64", "dateTime"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "'..'", "'<..<'", "'<..'", "'..<'", "'.'", 
			"':'", "';'", "'='", "'{'", "'}'", "'('", "')'", "'['", "']'", "','", 
			"'/'", "'-'", "'@'", "'+'", "'_'", "'.base64'"
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
			"RSQUARE", "COMMA", "SLASH", "DASH", "AT", "PLUS", "UNDERSCORE", "BASE64", 
			"BASE64_DATA", "ID", "BlockComment", "LineComment", "WS", "NL"
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
			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(52);
				match(NL);
				}
				}
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << URL) | (1L << FloatLiteral) | (1L << DoubleLiteral) | (1L << DecimalLiteral) | (1L << IntegerLiteral) | (1L << HexLiteral) | (1L << BinLiteral) | (1L << LongLiteral) | (1L << Version) | (1L << BasicString) | (1L << RawString) | (1L << BlockBasicString) | (1L << BlockRawString) | (1L << BlockRawAltString) | (1L << CharLiteral) | (1L << CompoundDuration) | (1L << DayDuration) | (1L << HourDuration) | (1L << MinuteDuration) | (1L << SecondDuration) | (1L << MillisecondDuration) | (1L << NanosecondDuration) | (1L << Date) | (1L << LSQUARE) | (1L << UNDERSCORE) | (1L << BASE64) | (1L << ID))) != 0)) {
				{
				{
				setState(58);
				tag();
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(59);
					match(NL);
					}
					}
					setState(64);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(69);
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
		public TerminalNode NL() { return getToken(KDParser.NL, 0); }
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				{
				setState(70);
				nsName();
				setState(72);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(71);
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
				setState(74);
				valueList();
				}
				}
				break;
			}
			setState(78);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(77);
				attributeList();
				}
				break;
			}
			setState(86);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(80);
				match(SEMICOLON);
				}
				break;
			case 2:
				{
				setState(81);
				match(NL);
				}
				break;
			case 3:
				{
				{
				setState(82);
				match(OPEN);
				setState(83);
				tagList();
				setState(84);
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
		public Base64Context base64() {
			return getRuleContext(Base64Context.class,0);
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
			setState(109);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(88);
				stringLiteral();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(89);
				match(CharLiteral);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(90);
				match(ID);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(91);
				match(IntegerLiteral);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(92);
				match(HexLiteral);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(93);
				match(BinLiteral);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(94);
				match(LongLiteral);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(95);
				match(FloatLiteral);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(96);
				match(DoubleLiteral);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(97);
				match(DecimalLiteral);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(98);
				match(TRUE);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(99);
				match(FALSE);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(100);
				match(NULL);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(101);
				list();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(102);
				map();
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(103);
				dateTime();
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(104);
				duration();
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(105);
				match(URL);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(106);
				range();
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(107);
				match(Version);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(108);
				base64();
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
			setState(111);
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
			setState(113);
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
			setState(115);
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
			setState(129);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(117);
				match(UNDERSCORE);
				setState(118);
				rangeOp();
				setState(119);
				match(IntegerLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(121);
				match(IntegerLiteral);
				setState(122);
				rangeOp();
				setState(123);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(125);
				match(IntegerLiteral);
				setState(126);
				rangeOp();
				setState(127);
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
			setState(143);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(131);
				match(UNDERSCORE);
				setState(132);
				rangeOp();
				setState(133);
				match(LongLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(135);
				match(LongLiteral);
				setState(136);
				rangeOp();
				setState(137);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(139);
				match(LongLiteral);
				setState(140);
				rangeOp();
				setState(141);
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
			setState(157);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(145);
				match(UNDERSCORE);
				setState(146);
				rangeOp();
				setState(147);
				match(FloatLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(149);
				match(FloatLiteral);
				setState(150);
				rangeOp();
				setState(151);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(153);
				match(FloatLiteral);
				setState(154);
				rangeOp();
				setState(155);
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
			setState(171);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(159);
				match(UNDERSCORE);
				setState(160);
				rangeOp();
				setState(161);
				match(DoubleLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(163);
				match(DoubleLiteral);
				setState(164);
				rangeOp();
				setState(165);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(167);
				match(DoubleLiteral);
				setState(168);
				rangeOp();
				setState(169);
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
			setState(185);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(173);
				match(UNDERSCORE);
				setState(174);
				rangeOp();
				setState(175);
				match(DecimalLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(177);
				match(DecimalLiteral);
				setState(178);
				rangeOp();
				setState(179);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(181);
				match(DecimalLiteral);
				setState(182);
				rangeOp();
				setState(183);
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
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(187);
				match(UNDERSCORE);
				setState(188);
				rangeOp();
				setState(189);
				duration();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(191);
				duration();
				setState(192);
				rangeOp();
				setState(193);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(195);
				duration();
				setState(196);
				rangeOp();
				setState(197);
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
			setState(213);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(201);
				match(UNDERSCORE);
				setState(202);
				rangeOp();
				setState(203);
				dateTime();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(205);
				dateTime();
				setState(206);
				rangeOp();
				setState(207);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(209);
				dateTime();
				setState(210);
				rangeOp();
				setState(211);
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
			setState(227);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(215);
				match(UNDERSCORE);
				setState(216);
				rangeOp();
				setState(217);
				match(Version);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(219);
				match(Version);
				setState(220);
				rangeOp();
				setState(221);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(223);
				match(Version);
				setState(224);
				rangeOp();
				setState(225);
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
			setState(241);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(229);
				match(UNDERSCORE);
				setState(230);
				rangeOp();
				setState(231);
				match(CharLiteral);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(233);
				match(CharLiteral);
				setState(234);
				rangeOp();
				setState(235);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(237);
				match(CharLiteral);
				setState(238);
				rangeOp();
				setState(239);
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
			setState(255);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(243);
				match(UNDERSCORE);
				setState(244);
				rangeOp();
				setState(245);
				stringLiteral();
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(247);
				stringLiteral();
				setState(248);
				rangeOp();
				setState(249);
				match(UNDERSCORE);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(251);
				stringLiteral();
				setState(252);
				rangeOp();
				setState(253);
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
			setState(267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(257);
				intRange();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(258);
				longRange();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(259);
				floatRange();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(260);
				doubleRange();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(261);
				decimalRange();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(262);
				durationRange();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(263);
				dateTimeRange();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(264);
				versionRange();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(265);
				charRange();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(266);
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
			setState(270); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(269);
					value();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(272); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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
			setState(274);
			nsName();
			setState(275);
			match(EQUALS);
			setState(276);
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
			setState(279); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(278);
					attribute();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(281); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
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
			setState(285);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(283);
				match(ID);
				setState(284);
				match(COLON);
				}
				break;
			}
			setState(287);
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
			setState(316);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(289);
				match(LSQUARE);
				setState(293);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(290);
					match(NL);
					}
					}
					setState(295);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(296);
				value();
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << TRUE) | (1L << FALSE) | (1L << URL) | (1L << FloatLiteral) | (1L << DoubleLiteral) | (1L << DecimalLiteral) | (1L << IntegerLiteral) | (1L << HexLiteral) | (1L << BinLiteral) | (1L << LongLiteral) | (1L << Version) | (1L << BasicString) | (1L << RawString) | (1L << BlockBasicString) | (1L << BlockRawString) | (1L << BlockRawAltString) | (1L << CharLiteral) | (1L << CompoundDuration) | (1L << DayDuration) | (1L << HourDuration) | (1L << MinuteDuration) | (1L << SecondDuration) | (1L << MillisecondDuration) | (1L << NanosecondDuration) | (1L << Date) | (1L << LSQUARE) | (1L << COMMA) | (1L << UNDERSCORE) | (1L << BASE64) | (1L << ID))) != 0)) {
					{
					{
					setState(298);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(297);
						match(COMMA);
						}
					}

					setState(300);
					value();
					}
					}
					setState(305);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(309);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(306);
					match(NL);
					}
					}
					setState(311);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(312);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(314);
				match(LSQUARE);
				setState(315);
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
			setState(321);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(318);
				match(NL);
				}
				}
				setState(323);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(324);
			value();
			setState(325);
			match(EQUALS);
			setState(329);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(326);
				match(NL);
				}
				}
				setState(331);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(332);
			value();
			setState(336);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(333);
					match(NL);
					}
					} 
				}
				setState(338);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
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
			setState(367);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(339);
				match(LSQUARE);
				setState(343);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(340);
						match(NL);
						}
						} 
					}
					setState(345);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				setState(346);
				pair();
				setState(353);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(348);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==COMMA) {
							{
							setState(347);
							match(COMMA);
							}
						}

						setState(350);
						pair();
						}
						} 
					}
					setState(355);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				}
				setState(359);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NL) {
					{
					{
					setState(356);
					match(NL);
					}
					}
					setState(361);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(362);
				match(RSQUARE);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(364);
				match(LSQUARE);
				setState(365);
				match(EQUALS);
				setState(366);
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

	public static class Base64Context extends ParserRuleContext {
		public TerminalNode BASE64() { return getToken(KDParser.BASE64, 0); }
		public TerminalNode BASE64_DATA() { return getToken(KDParser.BASE64_DATA, 0); }
		public Base64Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_base64; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).enterBase64(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof KDParserListener ) ((KDParserListener)listener).exitBase64(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof KDParserVisitor ) return ((KDParserVisitor<? extends T>)visitor).visitBase64(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Base64Context base64() throws RecognitionException {
		Base64Context _localctx = new Base64Context(_ctx, getState());
		enterRule(_localctx, 48, RULE_base64);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			match(BASE64);
			setState(370);
			match(BASE64_DATA);
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
		enterRule(_localctx, 50, RULE_dateTime);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			match(Date);
			setState(377);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Time) {
				{
				setState(373);
				match(Time);
				setState(375);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==TimeZone) {
					{
					setState(374);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\39\u017e\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\3\2\7\28\n\2\f\2\16\2;\13\2\3\2\3\2\7\2?\n\2\f\2"+
		"\16\2B\13\2\7\2D\n\2\f\2\16\2G\13\2\3\3\3\3\5\3K\n\3\3\3\5\3N\n\3\3\3"+
		"\5\3Q\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3Y\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4p\n\4\3\5"+
		"\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5"+
		"\b\u0084\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u0092"+
		"\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00a0\n\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00ae\n\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00bc\n\f\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00ca\n\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00d8\n\16\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00e6\n\17\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00f4\n\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u0102"+
		"\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u010e\n\22"+
		"\3\23\6\23\u0111\n\23\r\23\16\23\u0112\3\24\3\24\3\24\3\24\3\25\6\25\u011a"+
		"\n\25\r\25\16\25\u011b\3\26\3\26\5\26\u0120\n\26\3\26\3\26\3\27\3\27\7"+
		"\27\u0126\n\27\f\27\16\27\u0129\13\27\3\27\3\27\5\27\u012d\n\27\3\27\7"+
		"\27\u0130\n\27\f\27\16\27\u0133\13\27\3\27\7\27\u0136\n\27\f\27\16\27"+
		"\u0139\13\27\3\27\3\27\3\27\3\27\5\27\u013f\n\27\3\30\7\30\u0142\n\30"+
		"\f\30\16\30\u0145\13\30\3\30\3\30\3\30\7\30\u014a\n\30\f\30\16\30\u014d"+
		"\13\30\3\30\3\30\7\30\u0151\n\30\f\30\16\30\u0154\13\30\3\31\3\31\7\31"+
		"\u0158\n\31\f\31\16\31\u015b\13\31\3\31\3\31\5\31\u015f\n\31\3\31\7\31"+
		"\u0162\n\31\f\31\16\31\u0165\13\31\3\31\7\31\u0168\n\31\f\31\16\31\u016b"+
		"\13\31\3\31\3\31\3\31\3\31\3\31\5\31\u0172\n\31\3\32\3\32\3\32\3\33\3"+
		"\33\3\33\5\33\u017a\n\33\5\33\u017c\n\33\3\33\2\2\34\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\2\5\3\2\17\23\3\2\25\33\3\2\37"+
		"\"\2\u01af\29\3\2\2\2\4M\3\2\2\2\6o\3\2\2\2\bq\3\2\2\2\ns\3\2\2\2\fu\3"+
		"\2\2\2\16\u0083\3\2\2\2\20\u0091\3\2\2\2\22\u009f\3\2\2\2\24\u00ad\3\2"+
		"\2\2\26\u00bb\3\2\2\2\30\u00c9\3\2\2\2\32\u00d7\3\2\2\2\34\u00e5\3\2\2"+
		"\2\36\u00f3\3\2\2\2 \u0101\3\2\2\2\"\u010d\3\2\2\2$\u0110\3\2\2\2&\u0114"+
		"\3\2\2\2(\u0119\3\2\2\2*\u011f\3\2\2\2,\u013e\3\2\2\2.\u0143\3\2\2\2\60"+
		"\u0171\3\2\2\2\62\u0173\3\2\2\2\64\u0176\3\2\2\2\668\79\2\2\67\66\3\2"+
		"\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:E\3\2\2\2;9\3\2\2\2<@\5\4\3\2=?\7"+
		"9\2\2>=\3\2\2\2?B\3\2\2\2@>\3\2\2\2@A\3\2\2\2AD\3\2\2\2B@\3\2\2\2C<\3"+
		"\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2F\3\3\2\2\2GE\3\2\2\2HJ\5*\26\2IK"+
		"\5$\23\2JI\3\2\2\2JK\3\2\2\2KN\3\2\2\2LN\5$\23\2MH\3\2\2\2ML\3\2\2\2N"+
		"P\3\2\2\2OQ\5(\25\2PO\3\2\2\2PQ\3\2\2\2QX\3\2\2\2RY\7%\2\2SY\79\2\2TU"+
		"\7\'\2\2UV\5\2\2\2VW\7(\2\2WY\3\2\2\2XR\3\2\2\2XS\3\2\2\2XT\3\2\2\2XY"+
		"\3\2\2\2Y\5\3\2\2\2Zp\5\b\5\2[p\7\24\2\2\\p\7\65\2\2]p\7\n\2\2^p\7\13"+
		"\2\2_p\7\f\2\2`p\7\r\2\2ap\7\7\2\2bp\7\b\2\2cp\7\t\2\2dp\7\4\2\2ep\7\5"+
		"\2\2fp\7\3\2\2gp\5,\27\2hp\5\60\31\2ip\5\64\33\2jp\5\n\6\2kp\7\6\2\2l"+
		"p\5\"\22\2mp\7\16\2\2np\5\62\32\2oZ\3\2\2\2o[\3\2\2\2o\\\3\2\2\2o]\3\2"+
		"\2\2o^\3\2\2\2o_\3\2\2\2o`\3\2\2\2oa\3\2\2\2ob\3\2\2\2oc\3\2\2\2od\3\2"+
		"\2\2oe\3\2\2\2of\3\2\2\2og\3\2\2\2oh\3\2\2\2oi\3\2\2\2oj\3\2\2\2ok\3\2"+
		"\2\2ol\3\2\2\2om\3\2\2\2on\3\2\2\2p\7\3\2\2\2qr\t\2\2\2r\t\3\2\2\2st\t"+
		"\3\2\2t\13\3\2\2\2uv\t\4\2\2v\r\3\2\2\2wx\7\62\2\2xy\5\f\7\2yz\7\n\2\2"+
		"z\u0084\3\2\2\2{|\7\n\2\2|}\5\f\7\2}~\7\62\2\2~\u0084\3\2\2\2\177\u0080"+
		"\7\n\2\2\u0080\u0081\5\f\7\2\u0081\u0082\7\n\2\2\u0082\u0084\3\2\2\2\u0083"+
		"w\3\2\2\2\u0083{\3\2\2\2\u0083\177\3\2\2\2\u0084\17\3\2\2\2\u0085\u0086"+
		"\7\62\2\2\u0086\u0087\5\f\7\2\u0087\u0088\7\r\2\2\u0088\u0092\3\2\2\2"+
		"\u0089\u008a\7\r\2\2\u008a\u008b\5\f\7\2\u008b\u008c\7\62\2\2\u008c\u0092"+
		"\3\2\2\2\u008d\u008e\7\r\2\2\u008e\u008f\5\f\7\2\u008f\u0090\7\r\2\2\u0090"+
		"\u0092\3\2\2\2\u0091\u0085\3\2\2\2\u0091\u0089\3\2\2\2\u0091\u008d\3\2"+
		"\2\2\u0092\21\3\2\2\2\u0093\u0094\7\62\2\2\u0094\u0095\5\f\7\2\u0095\u0096"+
		"\7\7\2\2\u0096\u00a0\3\2\2\2\u0097\u0098\7\7\2\2\u0098\u0099\5\f\7\2\u0099"+
		"\u009a\7\62\2\2\u009a\u00a0\3\2\2\2\u009b\u009c\7\7\2\2\u009c\u009d\5"+
		"\f\7\2\u009d\u009e\7\7\2\2\u009e\u00a0\3\2\2\2\u009f\u0093\3\2\2\2\u009f"+
		"\u0097\3\2\2\2\u009f\u009b\3\2\2\2\u00a0\23\3\2\2\2\u00a1\u00a2\7\62\2"+
		"\2\u00a2\u00a3\5\f\7\2\u00a3\u00a4\7\b\2\2\u00a4\u00ae\3\2\2\2\u00a5\u00a6"+
		"\7\b\2\2\u00a6\u00a7\5\f\7\2\u00a7\u00a8\7\62\2\2\u00a8\u00ae\3\2\2\2"+
		"\u00a9\u00aa\7\b\2\2\u00aa\u00ab\5\f\7\2\u00ab\u00ac\7\b\2\2\u00ac\u00ae"+
		"\3\2\2\2\u00ad\u00a1\3\2\2\2\u00ad\u00a5\3\2\2\2\u00ad\u00a9\3\2\2\2\u00ae"+
		"\25\3\2\2\2\u00af\u00b0\7\62\2\2\u00b0\u00b1\5\f\7\2\u00b1\u00b2\7\t\2"+
		"\2\u00b2\u00bc\3\2\2\2\u00b3\u00b4\7\t\2\2\u00b4\u00b5\5\f\7\2\u00b5\u00b6"+
		"\7\62\2\2\u00b6\u00bc\3\2\2\2\u00b7\u00b8\7\t\2\2\u00b8\u00b9\5\f\7\2"+
		"\u00b9\u00ba\7\t\2\2\u00ba\u00bc\3\2\2\2\u00bb\u00af\3\2\2\2\u00bb\u00b3"+
		"\3\2\2\2\u00bb\u00b7\3\2\2\2\u00bc\27\3\2\2\2\u00bd\u00be\7\62\2\2\u00be"+
		"\u00bf\5\f\7\2\u00bf\u00c0\5\n\6\2\u00c0\u00ca\3\2\2\2\u00c1\u00c2\5\n"+
		"\6\2\u00c2\u00c3\5\f\7\2\u00c3\u00c4\7\62\2\2\u00c4\u00ca\3\2\2\2\u00c5"+
		"\u00c6\5\n\6\2\u00c6\u00c7\5\f\7\2\u00c7\u00c8\5\n\6\2\u00c8\u00ca\3\2"+
		"\2\2\u00c9\u00bd\3\2\2\2\u00c9\u00c1\3\2\2\2\u00c9\u00c5\3\2\2\2\u00ca"+
		"\31\3\2\2\2\u00cb\u00cc\7\62\2\2\u00cc\u00cd\5\f\7\2\u00cd\u00ce\5\64"+
		"\33\2\u00ce\u00d8\3\2\2\2\u00cf\u00d0\5\64\33\2\u00d0\u00d1\5\f\7\2\u00d1"+
		"\u00d2\7\62\2\2\u00d2\u00d8\3\2\2\2\u00d3\u00d4\5\64\33\2\u00d4\u00d5"+
		"\5\f\7\2\u00d5\u00d6\5\64\33\2\u00d6\u00d8\3\2\2\2\u00d7\u00cb\3\2\2\2"+
		"\u00d7\u00cf\3\2\2\2\u00d7\u00d3\3\2\2\2\u00d8\33\3\2\2\2\u00d9\u00da"+
		"\7\62\2\2\u00da\u00db\5\f\7\2\u00db\u00dc\7\16\2\2\u00dc\u00e6\3\2\2\2"+
		"\u00dd\u00de\7\16\2\2\u00de\u00df\5\f\7\2\u00df\u00e0\7\62\2\2\u00e0\u00e6"+
		"\3\2\2\2\u00e1\u00e2\7\16\2\2\u00e2\u00e3\5\f\7\2\u00e3\u00e4\7\16\2\2"+
		"\u00e4\u00e6\3\2\2\2\u00e5\u00d9\3\2\2\2\u00e5\u00dd\3\2\2\2\u00e5\u00e1"+
		"\3\2\2\2\u00e6\35\3\2\2\2\u00e7\u00e8\7\62\2\2\u00e8\u00e9\5\f\7\2\u00e9"+
		"\u00ea\7\24\2\2\u00ea\u00f4\3\2\2\2\u00eb\u00ec\7\24\2\2\u00ec\u00ed\5"+
		"\f\7\2\u00ed\u00ee\7\62\2\2\u00ee\u00f4\3\2\2\2\u00ef\u00f0\7\24\2\2\u00f0"+
		"\u00f1\5\f\7\2\u00f1\u00f2\7\24\2\2\u00f2\u00f4\3\2\2\2\u00f3\u00e7\3"+
		"\2\2\2\u00f3\u00eb\3\2\2\2\u00f3\u00ef\3\2\2\2\u00f4\37\3\2\2\2\u00f5"+
		"\u00f6\7\62\2\2\u00f6\u00f7\5\f\7\2\u00f7\u00f8\5\b\5\2\u00f8\u0102\3"+
		"\2\2\2\u00f9\u00fa\5\b\5\2\u00fa\u00fb\5\f\7\2\u00fb\u00fc\7\62\2\2\u00fc"+
		"\u0102\3\2\2\2\u00fd\u00fe\5\b\5\2\u00fe\u00ff\5\f\7\2\u00ff\u0100\5\b"+
		"\5\2\u0100\u0102\3\2\2\2\u0101\u00f5\3\2\2\2\u0101\u00f9\3\2\2\2\u0101"+
		"\u00fd\3\2\2\2\u0102!\3\2\2\2\u0103\u010e\5\16\b\2\u0104\u010e\5\20\t"+
		"\2\u0105\u010e\5\22\n\2\u0106\u010e\5\24\13\2\u0107\u010e\5\26\f\2\u0108"+
		"\u010e\5\30\r\2\u0109\u010e\5\32\16\2\u010a\u010e\5\34\17\2\u010b\u010e"+
		"\5\36\20\2\u010c\u010e\5 \21\2\u010d\u0103\3\2\2\2\u010d\u0104\3\2\2\2"+
		"\u010d\u0105\3\2\2\2\u010d\u0106\3\2\2\2\u010d\u0107\3\2\2\2\u010d\u0108"+
		"\3\2\2\2\u010d\u0109\3\2\2\2\u010d\u010a\3\2\2\2\u010d\u010b\3\2\2\2\u010d"+
		"\u010c\3\2\2\2\u010e#\3\2\2\2\u010f\u0111\5\6\4\2\u0110\u010f\3\2\2\2"+
		"\u0111\u0112\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0113\3\2\2\2\u0113%\3"+
		"\2\2\2\u0114\u0115\5*\26\2\u0115\u0116\7&\2\2\u0116\u0117\5\6\4\2\u0117"+
		"\'\3\2\2\2\u0118\u011a\5&\24\2\u0119\u0118\3\2\2\2\u011a\u011b\3\2\2\2"+
		"\u011b\u0119\3\2\2\2\u011b\u011c\3\2\2\2\u011c)\3\2\2\2\u011d\u011e\7"+
		"\65\2\2\u011e\u0120\7$\2\2\u011f\u011d\3\2\2\2\u011f\u0120\3\2\2\2\u0120"+
		"\u0121\3\2\2\2\u0121\u0122\7\65\2\2\u0122+\3\2\2\2\u0123\u0127\7+\2\2"+
		"\u0124\u0126\79\2\2\u0125\u0124\3\2\2\2\u0126\u0129\3\2\2\2\u0127\u0125"+
		"\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u012a\3\2\2\2\u0129\u0127\3\2\2\2\u012a"+
		"\u0131\5\6\4\2\u012b\u012d\7-\2\2\u012c\u012b\3\2\2\2\u012c\u012d\3\2"+
		"\2\2\u012d\u012e\3\2\2\2\u012e\u0130\5\6\4\2\u012f\u012c\3\2\2\2\u0130"+
		"\u0133\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0137\3\2"+
		"\2\2\u0133\u0131\3\2\2\2\u0134\u0136\79\2\2\u0135\u0134\3\2\2\2\u0136"+
		"\u0139\3\2\2\2\u0137\u0135\3\2\2\2\u0137\u0138\3\2\2\2\u0138\u013a\3\2"+
		"\2\2\u0139\u0137\3\2\2\2\u013a\u013b\7,\2\2\u013b\u013f\3\2\2\2\u013c"+
		"\u013d\7+\2\2\u013d\u013f\7,\2\2\u013e\u0123\3\2\2\2\u013e\u013c\3\2\2"+
		"\2\u013f-\3\2\2\2\u0140\u0142\79\2\2\u0141\u0140\3\2\2\2\u0142\u0145\3"+
		"\2\2\2\u0143\u0141\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0146\3\2\2\2\u0145"+
		"\u0143\3\2\2\2\u0146\u0147\5\6\4\2\u0147\u014b\7&\2\2\u0148\u014a\79\2"+
		"\2\u0149\u0148\3\2\2\2\u014a\u014d\3\2\2\2\u014b\u0149\3\2\2\2\u014b\u014c"+
		"\3\2\2\2\u014c\u014e\3\2\2\2\u014d\u014b\3\2\2\2\u014e\u0152\5\6\4\2\u014f"+
		"\u0151\79\2\2\u0150\u014f\3\2\2\2\u0151\u0154\3\2\2\2\u0152\u0150\3\2"+
		"\2\2\u0152\u0153\3\2\2\2\u0153/\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0159"+
		"\7+\2\2\u0156\u0158\79\2\2\u0157\u0156\3\2\2\2\u0158\u015b\3\2\2\2\u0159"+
		"\u0157\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015c\3\2\2\2\u015b\u0159\3\2"+
		"\2\2\u015c\u0163\5.\30\2\u015d\u015f\7-\2\2\u015e\u015d\3\2\2\2\u015e"+
		"\u015f\3\2\2\2\u015f\u0160\3\2\2\2\u0160\u0162\5.\30\2\u0161\u015e\3\2"+
		"\2\2\u0162\u0165\3\2\2\2\u0163\u0161\3\2\2\2\u0163\u0164\3\2\2\2\u0164"+
		"\u0169\3\2\2\2\u0165\u0163\3\2\2\2\u0166\u0168\79\2\2\u0167\u0166\3\2"+
		"\2\2\u0168\u016b\3\2\2\2\u0169\u0167\3\2\2\2\u0169\u016a\3\2\2\2\u016a"+
		"\u016c\3\2\2\2\u016b\u0169\3\2\2\2\u016c\u016d\7,\2\2\u016d\u0172\3\2"+
		"\2\2\u016e\u016f\7+\2\2\u016f\u0170\7&\2\2\u0170\u0172\7,\2\2\u0171\u0155"+
		"\3\2\2\2\u0171\u016e\3\2\2\2\u0172\61\3\2\2\2\u0173\u0174\7\63\2\2\u0174"+
		"\u0175\7\64\2\2\u0175\63\3\2\2\2\u0176\u017b\7\34\2\2\u0177\u0179\7\35"+
		"\2\2\u0178\u017a\7\36\2\2\u0179\u0178\3\2\2\2\u0179\u017a\3\2\2\2\u017a"+
		"\u017c\3\2\2\2\u017b\u0177\3\2\2\2\u017b\u017c\3\2\2\2\u017c\65\3\2\2"+
		"\2\'9@EJMPXo\u0083\u0091\u009f\u00ad\u00bb\u00c9\u00d7\u00e5\u00f3\u0101"+
		"\u010d\u0112\u011b\u011f\u0127\u012c\u0131\u0137\u013e\u0143\u014b\u0152"+
		"\u0159\u015e\u0163\u0169\u0171\u0179\u017b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}