package ki.kd

import java.io.*
import java.net.URL
import java.time.format.DateTimeFormatter

/**
 * Utility class for KD
 */
class KD {
    companion object {

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

        fun read(kd: String) : Tag = read(StringReader(kd))
        fun read(file: File) : Tag = read(FileReader(file))
        fun read(file: URL) : Tag = read(file.readText())
    }
}