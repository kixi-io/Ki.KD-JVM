lexer grammar KDLexer;

/**
 * KD Lexer
 *
 * @author Daniel Leuck
 */
 channels {
   WHITESPACE,
   COMMENTS
 }

NULL: 'null' | 'nil';

TRUE: 'true' | 'on';
FALSE: 'false' | 'off';

// Note: This requires a "protocol:/" at the beginning, which excludes URLs such as jar:file:/home/duke/duke.jar!
// The trainling':/' is necessary to disabiguate protocol: and namespace:
URL: [a-zA-Z][a-zA-Z0-9.+\-]* ':' '/' [a-zA-Z0-9.+\-_~:/?#[\]@!$&'()*;,%=]+;
// Numbers --- ---
// SECTION: literals
fragment DecDigit: '0'..'9';
fragment DecDigitNoZero: '1'..'9';
fragment DecDigitOrSeparator: DecDigit | '_';
fragment DecDigits
    : DecDigit DecDigitOrSeparator* DecDigit
    | DecDigit
    ;
fragment DoubleExponent: [eE] [+-]? DecDigits;

fragment NonZeroNumberPart
    : (DecDigitNoZero DecDigitOrSeparator* DecDigit | DecDigit)
    ;
fragment NumberPart
    : (DecDigit DecDigitOrSeparator* DecDigit | DecDigit)
    ;
fragment ASCIIAlpha: [a-zA-Z];

FloatLiteral
    : '-'? NumberPart? '.' NumberPart DoubleExponent? [fF]
    | '-'? NumberPart DoubleExponent? [fF]
    ;

DoubleLiteral
    : '-'? NumberPart? '.' NumberPart DoubleExponent? [dD]?
    | '-'? NumberPart DoubleExponent [dD]?
    | '-'? NumberPart [dD]
    ;

DecimalLiteral
    : '-'? NumberPart? '.' NumberPart DoubleExponent? ('bd' | 'BD')
    | '-'? NumberPart DoubleExponent? ('bd' | 'BD')
    ;
IntegerLiteral
    : '-'? NumberPart
    ;

fragment HexDigit: [0-9a-fA-F];
fragment HexDigitOrSeparator: HexDigit | '_';
HexLiteral
    : '-'?
    (
        '0' [xX] HexDigit HexDigitOrSeparator* HexDigit
            |
        '0' [xX] HexDigit
    )
    ;
fragment BinDigit: [01];
fragment BinDigitOrSeparator: BinDigit | '_';

BinLiteral:
    '-'?
    (
        '0' [bB] BinDigit BinDigitOrSeparator* BinDigit
            |
        '0' [bB] BinDigit
    )
    ;
LongLiteral
    : (IntegerLiteral | HexLiteral | BinLiteral) 'L'
    ;

fragment Number: IntegerLiteral | DoubleLiteral;

// Version --- ---
Version: (NumberPart '-' VersionQualifierAndNum) |
         (NumberPart '.' NumberPart '-' VersionQualifierAndNum) |
         (NumberPart '.' NumberPart '.' NumberPart ('-' VersionQualifierAndNum)?);

fragment VersionQualifierAndNum: VersionQualifier ('-'? NumberPart)?;

// String --- ---

SimpleString: '"' (Esc | ~["\r\n\\])* '"';
RawString: '@"' (~["\r\n])* '"';
BlockString: '"""' ([\t\r\n] | '""' ~'"' | '"' ~'"' | Esc | SafeCodePoint)* '"""';
BlockRawString:   '@"""' ([\t\r\n\\] | '""' ~'"' | '"' ~'"' | Esc | SafeCodePoint)* '"""';
BlockRawAltString:   '`' (~'`')* '`';

/*
BasicString: '"' (Esc | ~["\r\n\\])* '"';
RawString: '@"' (~["\r\n])* '"';
BlockBasicString: '"""' ([\t\r\n] | '""' ~'"' | '"' ~'"' | Esc | SafeCodePoint)* '"""';
BlockRawString:   '@"""' ([\t\r\n\\] | '""' ~'"' | '"' ~'"' | Esc | SafeCodePoint)* '"""';
BlockRawAltString:   '`' ([\t\r\n\\] | Esc | SafeCodePoint)*? '`';
*/

fragment UniCharacterLiteral
    : '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;
fragment EscapedIdentifier
    : '\\' ('t' | 'b' | 'r' | 'n' | '\'' | '"' | '\\' )
    ;
fragment Esc
    : UniCharacterLiteral
    | EscapedIdentifier
    ;
fragment Unicode // TODO - Add six HEX version
   : 'u' HexDigit HexDigit HexDigit HexDigit
   ;

fragment SafeCodePoint
   : ~ ["\\\u0000-\u001F]
   ;
fragment CharSafeCodePoint
   : ~ ['\\\u0000-\u001F]
   ;

// TODO: This doesn't capture some emoji correctly
CharLiteral: '\'' (Esc | CharSafeCodePoint) '\'';

// End String --- ---

// Date, Time and Duration

fragment DAYS: 'day' | 'days';
fragment HOURS: 'h';
fragment MINUTES: 'min';
fragment SECONDS: 's';
fragment MILLIS: 'ms';
fragment NANOS: 'ns';

// Duration: CompoundDuration | DayDuration | HourDuration | MinuteDuration | SecondDuration | MillisecondDuration
//          | NanosecondDuration;

// If days are included the label "day" is required. All others unit specifications are
// optional. We can't use the unit durations for other segments because the labels are
// optional.
//
// TODO: We should handle this in the Parser
CompoundDuration: '-'? (DayDuration ':')? DecDigits HOURS? ':' DecDigits MINUTES? ':'
          DecDigits ('.' NumberPart DoubleExponent?)? SECONDS?;

DayDuration: Number DAYS;
HourDuration: Number HOURS;
MinuteDuration: Number MINUTES;
SecondDuration: Number SECONDS;
MillisecondDuration: IntegerLiteral MILLIS;
NanosecondDuration: IntegerLiteral NANOS;

// See reference: https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD)#ZonedDateTime

// TODO: Consider adding ISO style representation
//   https://en.wikipedia.org/wiki/ISO_8601#Times

Date: '-'? DecDigits '/' DecDigit DecDigit? '/' DecDigit DecDigit?;

Time: AT DecDigits':' DecDigits (':' DecDigits ('.' NumberPart DoubleExponent?)?)?;

// TODO - Break this into separate parser rules
TimeZone:
    // positive offset
    ('+' DecDigit DecDigit? (':' DecDigit DecDigit)?) |
    ('-'(
        // negative offset
        ( DecDigit DecDigit? (':' DecDigit DecDigit)?) |
        // UTC (Z)
        ('UTC' | 'GMT' | 'Z') |
        // KiTZ
        ( ASCIIAlpha ASCIIAlpha '/' ASCIIAlpha+ )
        )
    )
;

// Quantity ////

// Assumed to be an Int or a Decimal unless Double, Float or Long is specified
IntegerQuantityLiteral
    : '-'? NumberPart ID (':' 'L')?
    ;
DecimalQuantityLiteral
    : '-'? NumberPart ID ':' [dDfF]
    | '-'? NumberPart? '.' NumberPart DoubleExponent? ID (':' [dDfFL])?
    | '-'? NumberPart DoubleExponent ID (':' [dDfFL])?
    ;

// Range Operators
InclusiveRangeOp: '..';
ExclusiveRangeOp: '<..<';
ExclusiveLeftOp: '<..';
ExclusiveRightOp: '..<';

// Punctuation
DOT: '.';
COLON: ':';
SEMICOLON: ';';
EQUALS: '=';
OPEN: '{';
CLOSE: '}';
LPAREN: '(';
RPAREN: ')';
LSQUARE: '[';
RSQUARE: ']';
COMMA: ',';
SLASH: '/';
DASH: '-';
AT: '@';
PLUS: '+';
UNDERSCORE: '_';

// Encodings --- ---

Blob: '.blob' BLOB_DATA;

// Complies with "The Base64 Alphabet" as specified in Table 1 of RFC 4648
fragment BLOB_DATA: '(' [ \t\r\n]* ([A-Za-z0-9+=/] [ \t\r\n]*)+ ')';

// Identifiers --- ---

// Any unicode letter, emoji, $ or _
fragment IDStart: [$_\p{Alpha}\p{General_Category=Other_Letter}\p{Emoji_Presentation}];  // [$_\p{Alpha}\p{General_Category=Other_Letter}\p{Emoji}\p{Join_Control}\p{Variation_Selector}];

// Any unicode letter, digit, emoji, $ or _
fragment IDChar: [$_\p{Alnum}\p{General_Category=Other_Letter}\p{Emoji}\p{Join_Control}\p{Variation_Selector}];

fragment VersionQualifier: [\p{Alpha}]+;

ID: IDStart IDChar*;

// COMMENTS --- ---

BlockComment: '/*' ( BlockComment | . )*? '*/' -> channel(HIDDEN); //-> skip; //-> channel(COMMENTS);

LineComment: ('#' | '//') ~[\u000A\u000D]* -> channel(HIDDEN); //-> skip; //-> channel(COMMENTS);

// WHITE_SPACE --- ---
WS: ([ \t\r] | '\\' '\n')+ -> channel(HIDDEN); //-> skip;
NL: ('\n' | ';')+;
