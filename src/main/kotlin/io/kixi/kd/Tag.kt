package io.kixi.kd

import io.kixi.Ki
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * KD documents are composed of KD tags. Tags can contain the following components:
 *
 * 1. a list of annotations (optional) - see details below
 * 1. a namespace (optional)
 * 1. a name (if not present, the empty string "" is used)
 * 1. 0 or more values (optional)
 * 1. 0 or more attributes (optional)
 * 1. 0 or more children (optional)
 *
 * KD tag examples:
 * ```
 *     size 4
 *     secure true
 *     version 5.2.3-beta-3
 *     range 25..50
 *     oddValues 1 3 5 7
 *     valueAndAtt "manahoana" greeting=true
 *     myList [2 4 6]
 *     myMap [name="Isabella" programmer=true]
 *
 *     @test // annotations start with @ and occur in front of or above tag names
 *     @log(true, file="log.txt")
 *     kitchenSink 1 'a' true [1 2 3] [4 5 6] [pizza=true sardines=false] 3.0..<5.0
 * ```
 *
 * The KD utility class can be used to read KD code from a file, URL, string or reader.
 * ```
 *     var root = KD.read("""
 *          size 15
 *          optimized true
 *          version 5.0.2
 *     """)
 *
 *     var size = root.getChild("size").value as Int
 *     var optimized = root.getChild("optimized").value as Boolean
 *     var version = root.getChild("version").value as Version
 * ```
 *
 * In the example above, the tags in the string are read into a tag called "root".  It has
 * three children (tags) called "size", "optimized", and "version".  They each have one
 * value, no attributes, and no bodies.
 *
 * KD is often used for simple key-value mappings.  Because of this common case, Tag
 * provides convenience methods such as getValue and setValue which operate on the first
 * element in the values list.
 *
 * The example above used the simple format common in property files:
 * ```
 *     name value
 * ```
 *
 * The full KD tag format is:
 * ```
 *     @annotation_list
 *     namespace:name value_list attribute_list {
 *         children_tags
 *     }
 * ```
 *
 * 1. `annotation_list`: 0 or more annotations (space and/or line separated)
 * 2. `namespace:name`: An optional namespace string followed ':' and a name string
 * 3. `value_list`: 0 or more space separated KD literals and
 * 4. `attribute_list` 0 or more space separated (namespace:)key=value pairs
 *
 * The name, namespace, and keys are KD identifiers.  Values are KD literals.
 * Namespace is optional for tag names and attributes.  Tag bodies are also
 * optional.  KD identifiers begin with a unicode letter, underscore, dollar
 * sign or emoji followed by zero or more Unicode letters, numbers, underscores,
 * dollar signs or emoji.
 *
 * Annotations in the annotation list have the form `@namespace:name value_list attribute_list`
 * Only the name is required.
 *
 * **Examples**
 * ```
 * @Test
 * tag "Some data"
 *
 * @Test(true)
 * tag "Some data"
 *
 * @Test(true log="output.txt")
 * tag "Some data"
 * ```
 *
 * KD also supports anonymous tags which have the name "" (the empty string).
 * An anonymous tag starts with a literal and is followed by zero or more
 * additional literals and zero or more attributes.
 *
 * For more information on the KD language see the
 * [KD Wiki Page](https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD)).
 */
class Tag : TagEntityBase {

    var annotations = ArrayList<Annotation>()

    var children = ArrayList<Tag>()

    constructor(_nsid: NSID): super(_nsid) {}

    constructor(name:String, namespace:String = ""): this(NSID(name, namespace)) {}

    fun isAnonymous(): Boolean = nsid == NSID.ANONYMOUS

    companion object {
        private val NEW_LINE = System.getProperty("line.separator")
    }
    /**
     * @param linePrefix A prefix to insert before every line.
     * @return A string representation of this tag using KD
     *
     * TODO: break up long lines using the backslash
     */
    fun toString(linePrefix: String): String {

        val builder = StringBuilder()

        // If present, output annotations at the top, one per line.

        if(annotations.isNotEmpty()) {
            for(anno in annotations) {
                builder.append(linePrefix).append(anno.toString() + "\n")
            }
        }

        builder.append(linePrefix)
        var skipValueSpace = false

        if (isAnonymous()) {
            skipValueSpace = true
        } else {
            builder.append(nsid.toString())
        }
        // output values
        if (!values.isEmpty()) {
            val i: Iterator<*> = values.iterator()
            while (i.hasNext()) {
                if (skipValueSpace) skipValueSpace = false else builder.append(" ")
                builder.append(Ki.format(i.next()))
            }
        }

        // output attributes
        if (attributes.isNotEmpty()) {
            val i = attributes.entries.iterator()
            while (i.hasNext()) {
                builder.append(" ")
                val e = i.next()
                // val key = e.key
                // val value = e.value
                builder.append("${Ki.format(e.key)}=${Ki.format(e.value)}")
            }
        }

        // output children
        if (!children.isEmpty()) {
            builder.append(" {$NEW_LINE")
            for (t in children) {
                builder.append(t.toString("$linePrefix    ") + NEW_LINE)
            }
            builder.append("$linePrefix}")
        }
        return builder.toString()
    }

    override fun toString(): String = toString("")

    /**
     * Gets the first child tag with the given name
     */
    fun getChild(name: String, namespace: String = "") : Tag? = children.find {
        it.nsid.name == name && it.nsid.namespace == namespace
    }

    /**
     * Gets the first child tag with the given name, searching all descendants
     */
    fun findChild(name: String, namespace: String = "")  : Tag? {
        val child = getChild(name, namespace)
        if(child==null) {
            for(kid in children) {
                val grandChild = kid.getChild(name, namespace)
                if(grandChild!=null)
                    return grandChild
            }
        }
        return null
    }

    /**
     * Gets all child tags with the given name
     */
    fun getChildren(name: String, namespace: String = "") : List<Tag> = children.filter {
        it.nsid.name == name && it.nsid.namespace == namespace
    }

    /**
     * Gets all child tags with the given name, searching all descendants
     */
    fun findChildren(name: String, namespace: String = "") : List<Tag> {
        val descendents = ArrayList<Tag>()

        descendents.addAll(children.filter {
            it.nsid.name == name && it.nsid.namespace == namespace
        })

        for(descendent in children) {
            descendents.addAll(descendent.children.filter {
                it.nsid.name == name && it.nsid.namespace == namespace
            })
        }

        return descendents
    }

    /**
     * Gets all child tags in the given namespace
     */
    fun getChildrenInNamespace(namespace: String) : List<Tag> = children.filter  {
        it.nsid.namespace == namespace
    }

    /**
     * Finds the first child tag that tests true for the given predicate. The search is
     * recursive.
     *
     * **Example**
     * `
     * val thing = tag.findChild { it.color = "green" && it.size > 5 }
     * `
     */
    inline fun findChild(predicate: (Tag) -> Boolean): Tag? {
        var searchMe = getDescendants()

        for(child in searchMe) {
            if(predicate(child))
                return child
        }

        return null
    }

    /**
     * Finds all tags that tests true for the given predicate. The search is recursive.
     *
     * **Example**
     * `
     * val things = tag.findChildren { it.color = "green" && it.size > 5 }
     * `
     */
    inline fun findChildren(predicate: (Tag) -> Boolean): List<Tag> {
        var searchMe = getDescendants()

        val results = ArrayList<Tag>()
        for(child in searchMe) {
            if(predicate(child))
                results+=child
        }

        return results
    }

    /**
     * Creates a list of all contained tags (recursive).
     */
    fun getDescendants(descendants:MutableList<Tag> = ArrayList()) : List<Tag> {
        descendants.addAll<Tag>(children)
        children.forEach { it.getDescendants(descendants) }
        return descendants
    }


    // TODO: Add a grid datatype to allow get(x,y) style access to grids
    // Q: Should we have number grids with 0 defaults for missing values?

    /**
     * Gets the values of all immediate children as a list of rows. This allows you to
     * easily create grids.
     *
     * **Example**
     * `
     *     val intRows = KD.read("""
     *         1 2 3
     *         4 5 6
     *     """).getChildrenValues<Int>()
     *
     *     // Prints "6"
     *     println(intRows[1][2])
     * `
     */
    fun <T> getChildrenValues() : List<List<T>> {
        var rows = ArrayList<List<T>>()
        for(child in children) {
            rows.add(child.values as List<T>)
        }
        return rows
    }
}
