package ki.kd

import ki.log

class Token {

    enum class Type {
        String,
        ID,
        Colon,
        Semi,
        Comma,
        Int, Long,
        Float, Double, Decimal,
        Bool,
        Char,
        Date, DateTime, Time,
        Duration,
        URL,
        Null,
        Equal,
        LBrace, RBrace,
        LParen, RParen,
        LSquare, RSquare,
        LT, GT,
        At,
        RangeOp,
        LExRangeOp, RExRangeOp, LERERangeOp,
        Version,
        ConstructorStart,
        StarComment,
        LineComment,
        EOL,
        EOF
    }

    var text:String
    var type: Type
    var line:Int
    var index:Int

    constructor(text:String, type: Type, line:Int, index:Int) {
        this.text = text
        this.type = type
        this.line = line
        this.index = index
    }

    override fun toString() : String {
        return "${type.name} \"$text\" (line:$line, index:$index)"
    }
}

fun main() {
    log(Token("Foo", Token.Type.String, 0, 1))
    log(Token("5", Token.Type.Int, 5, 12))
}