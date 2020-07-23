package ki.kd

import ki.text.ParseException

class KDParseException(message:String, line:Int = -1, index:Int = -1, cause:Throwable? = null) :
        ParseException(message, line, index, cause) {}
