package ki.kd

import java.io.File
import java.io.FileReader
import java.io.Reader
import java.io.StringReader
import java.time.format.DateTimeFormatter

/**
 * Utility class for KD
 */
class KD {
    companion object {

        /**
         *
         * The SDL standard time format HH:mm:ss.SSS-z
         *
         *
         * Note: SDL uses a 24 hour clock (0-23)
         *
         * Note: This is not the same as a duration. This format is used
         * for the time component of a date_time instance
         */
        const val TIME_FORMAT = "HH:mm:ss.SSS-z"

        /**
         *
         * The SDL standard date format: yyyy/MM/dd
         *
         *
         * SDL uses the Gregorian calendar.
         */
        const val DATE_FORMAT = "yyyy/MM/dd"

        // TODO: Separate patterns for parsing and formatting (formatting should use padding as appropriate)
        val LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("y/M/d")
        val LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("y/M/d@H:m:s.nnnnnnnnn")
        val ZONED_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("y/M/d@H:m:s.nnnnnnnnn/VV")

        /**
         * The SDL standard DATE_TIME format yyyy/MM/dd-HH:mm:ss.SSS-z
         *
         *
         * Note: SDL uses a 24 hour clock (0-23) and the Gregorian calendar
         */
        const val DATE_TIME_FORMAT = DATE_FORMAT + "-" +
                TIME_FORMAT

        // TODO support all KD types
        fun format(obj: Any?): String {
            return when (obj) {
                null -> "nil"
                is String -> "\"$obj\""
                else -> obj.toString()
            }
        }

        /*
        fun value(text:String) : Any? {
            // TODO
        }
        */

        /**
         * Reads tags from the reader. If there is a single tag it is returned as is.
         * If there are multiple tags they are returned as children of a root tag
         * called "root";
         */
        fun read(reader: Reader) : Tag {
            var tags = Interpreter().read(reader)
            if(tags.size == 0) {
                return Tag("root")
            } else if(tags.size == 1) {
                return tags[0]
            } else {
                var tag = Tag("root")
                tag.children.addAll(tags)
                return tag;
            }
        }

        fun read(kd: String) : Tag = read(StringReader(kd));
        fun read(file: File) : Tag = read(FileReader(file));
    }
}