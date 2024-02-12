package io.kixi.kd

import io.kixi.core.text.ParseException

import java.io.*
import java.net.URL

/**
 * Utility class for KD
 */
class KD {

    companion object {
        /**
         * Reads tags from the reader. If there is a single tag it is returned as is.
         * If there are multiple tags they are returned as children of a root tag
         * called "root";
         *
         * @throws ParseException
         */
        @JvmStatic fun read(reader: Reader) : Tag {
            val tags = Interpreter().read(reader)
            if(tags.size == 0) {
                return Tag("root")
            } else if(tags.size == 1) {
                return tags[0]
            } else {
                val tag = Tag("root")
                tag.children.addAll(tags)
                return tag
            }
        }


        @JvmStatic fun read(text: String) : Tag = read(StringReader(text))
        @JvmStatic fun read(file: File) : Tag = read(FileReader(file))
        @JvmStatic fun read(url: URL) : Tag = read(url.readText())
        @JvmStatic fun readResource(resource:String) : Tag = read(this::class.java.
                getResource("/" + resource))

        /**
         * Create an object from a KD literal. This can be used like so:
         * ```
         * val birthday = KD("1995/9/16")
         * ```
         *
         * @param code A KD literal (https://github.com/kixi-io/Ki.Docs/wiki/Ki-Types)
         * @return Any? A Ki object
         * @throws KDParseException If `code` does not contain a valid KD literal
         */
        @JvmStatic operator fun invoke(code:String) = read(code).value
    }
}


