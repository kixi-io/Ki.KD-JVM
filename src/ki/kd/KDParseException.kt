package ki.kd

import ki.text.ParseException

class KDParseException(message:String, index:Int = -1, line:Int = -1, cause:Throwable? = null) :
        ParseException(message, index, line, cause) {}
