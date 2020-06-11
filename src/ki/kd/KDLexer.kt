package ki.kd

import ki.log
import ki.text.Lexer
import ki.text.ParseException
import ki.text.isKiIdentifier

import java.io.Reader

class KDLexer : Lexer {

    val breakChars = " \t\r;:=,{}[]()\u0000"

    constructor(reader: Reader) : super(reader) {}
    constructor(text: CharSequence) : super(text) {}

    fun next(): Token {
        nextWhiteSpace()

        if(!hasNextChar()) {
            return Token(
                "EOF",
                Token.Type.EOF,
                line,
                index
                // if (index==0) 0 else index-1
            )
        } else {
            var c = peekChar()

            if(c=='\n') {

                val oldLine = line.apply{}
                val oldIndex = index.apply{}

                skipChar()

                val tok = Token(
                    "EOL",
                    Token.Type.EOL,
                    oldLine,
                    oldIndex
                    // index
                    // if (index==0) 0 else index-1
                )

                return tok
            } else if(breakChars.contains(c)) {
                return makeBreakToken(c)
            } else if(c=='"' || startsWith("@\"") == true) {
                val oldLine = line.apply{}
                val oldIndex = index.apply{}

                var text = nextString()
                return Token(text, Token.Type.String, oldLine, oldIndex);
            } else {
                val oldLine = line.apply{}
                val oldIndex = index.apply{}

                var text = untilAny(breakChars)
                if(text==null) {
                    text=untilEnd()
                }

                return makeToken(text.trim(), oldLine, oldIndex);
            }
        }
    }

    /**
     * This handles all tokens that aren't punctuation or strings.
     */
    private fun makeToken(s:String, startLine:Int, startIndex:Int): Token {
        var ktype = when {
            s.isKiIdentifier() -> Token.Type.ID
            s[0].isDigit() || s[0]=='-' || s[0]=='.' -> getNumberType(s)
            s == "true" || s == "false" -> Token.Type.Bool

            else -> { // Note the block
                throw ParseException("Unknown token: $s.", line, if (index==0) 0 else index-1)
            }
        }

        var tok = Token(s.toString(),ktype,startLine,startIndex)
        skipChar();
        return tok
    }

    private fun getNumberType(s:String): Token.Type {
        when {
            s.endsWith("f", ignoreCase = true) -> return Token.Type.Float
            s.endsWith("l", ignoreCase = true) -> return Token.Type.Long
            s.endsWith("m", ignoreCase = true) -> return Token.Type.Decimal
            s.endsWith("d", ignoreCase = true) || s.contains('.') -> return Token.Type.Double
            else -> return Token.Type.Int
        }
    }

    private fun makeBreakToken(c:Char): Token {

        val oldLine = line.apply{}
        val oldIndex = index.apply{}

        var ktype = when(c) {
            '{' -> Token.Type.LBrace; '}' -> Token.Type.RBrace
            '[' -> Token.Type.LSquare; ']' -> Token.Type.RSquare
            '(' -> Token.Type.LParen; ')' -> Token.Type.RParen
            ':' -> Token.Type.Colon
            ';' -> Token.Type.Semi
            '=' -> Token.Type.Equal
            ',' -> Token.Type.Comma

            else -> {
                throw ParseException("Unknown breaking char.", oldLine, oldIndex)
            }
        }

        var tok = Token(c.toString(),ktype,line,if (index==0) 0 else index-1)
        skipChar();
        return tok
    }

    fun hasNext(): Boolean {
        return hasNextChar();
    }
}

fun main() {
    val sample = """
        
        this "value" size=5 {  
        
            hello;;;there
            @"raw\\string"
            "regular\\string"
            
            nums 23 -4.5 .6 .5f .0234m 234L
        }
        hi
        """.trimIndent()
    var lex = KDLexer(sample)

    log("---")
    log(sample)
    log("---")

    while(lex.hasNext())
        log(lex.next())

    log()
    log("Checking: \\nhello")

    lex = KDLexer("\nhello")
    while(lex.hasNext())
        log(lex.next())
}
