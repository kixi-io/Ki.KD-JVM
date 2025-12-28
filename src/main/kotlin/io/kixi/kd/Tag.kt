package io.kixi.kd

import io.kixi.Ki
import io.kixi.Call
import io.kixi.NSID

import kotlin.collections.ArrayList

/**
 * Exception thrown when a property is not found in a tag's children.
 *
 * @param key The property key that was not found
 * @param namespace The namespace of the property (empty string if none)
 */
class PropertyNotFoundException(
    val key: String,
    val namespace: String = ""
) : RuntimeException(
    if (namespace.isEmpty()) "Property '$key' not found"
    else "Property '$namespace:$key' not found"
)

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
 * See [KD Docs](https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD)) for details.
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
 * An anonymous tag can start with either:
 * - A literal value (e.g., `"hello"`, `42`, `true`)
 * - An attribute (e.g., `name="Jose"`, `age=35`)
 *
 * This allows for config-file style syntax:
 * ```
 * host = "localhost"
 * port = 8080
 * debug = true
 * ```
 *
 * For more information on the KD language see the
 * [KD Wiki Page](https://github.com/kixi-io/Ki.Docs/wiki/Ki-Data-(KD))
 */
@Suppress("unused")
class Tag : Call {

    // TODO: We need better lazy initialization.
    // These will be initialized anytime find operations are used, or toString() is called.
    val annotations: MutableList<Annotation> by lazy { ArrayList<Annotation>() }
    val children: MutableList<Tag> by lazy { ArrayList<Tag>() }

    constructor(_nsid: NSID): super(_nsid) {}

    constructor(name:String, namespace:String = ""): this(NSID(name, namespace)) {}

    fun isAnonymous(): Boolean = nsid == NSID.ANONYMOUS

    companion object {
        private val NEW_LINE = System.getProperty("line.separator")
    }

    fun setAttribute(key: String, value: Any) = setAttribute(NSID(name = key), value)

    /*
    fun getAttribute(key: String): Any {
        return getAttribute(key)
    }
    */

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

                val next = i.next()
                val literalText = Ki.format(next).trim()

                // Deal with multiline blob literals
                if(next is ByteArray && literalText.contains('\n')) {
                    val buf = StringBuilder()
                    val lines = literalText.lines()

                    var first = true
                    for (line in lines) {
                        if (first) {
                            buf.append(line + "\n")
                            first = false
                        } else {
                            buf.append(linePrefix + line + "\n")
                        }
                    }
                    builder.append(buf.toString().trimEnd())
                } else {
                    builder.append(literalText)
                }
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

    // ========================================================================
    // Property Access (for config-file style KD)
    // ========================================================================

    /**
     * Gets a property value from this tag's attributes or its children's attributes.
     *
     * This method first checks this tag's own attributes, then searches through
     * child tags for one that has an attribute with the given key. This is useful
     * for treating KD as a properties/config file format.
     *
     * **Example**
     * ```kotlin
     * val config = KD.read("""
     *     host = "localhost"
     *     port = 8080
     *     debug = true
     * """)
     *
     * val host = config.getProperty("host")     // "localhost"
     * val port = config.getProperty("port")     // 8080
     * val debug = config.getProperty("debug")   // true
     * ```
     *
     * @param key The attribute key to search for
     * @param namespace The namespace of the attribute (default: empty string)
     * @return The attribute value (may be null if the attribute value is nil)
     * @throws PropertyNotFoundException if no attribute with the given key is found
     */
    fun getProperty(key: String, namespace: String = ""): Any? {
        val nsid = NSID(key, namespace)

        // First check this tag's own attributes
        if (attributes.containsKey(nsid)) {
            return this[key, namespace]
        }

        // Then search children
        for (child in children) {
            if (child.attributes.containsKey(nsid)) {
                return child[key, namespace]
            }
        }
        throw PropertyNotFoundException(key, namespace)
    }

    /**
     * Gets a property value from this tag or its children, or null if not found.
     *
     * This is a non-throwing variant of [getProperty]. Returns null if no attribute
     * with the given key is found. Note that this means you cannot distinguish
     * between a missing property and a property with a nil value using this method
     * - use [hasProperty] or [getProperty] if you need that distinction.
     *
     * **Example**
     * ```kotlin
     * val config = KD.read("""
     *     host = "localhost"
     *     port = 8080
     * """)
     *
     * val host = config.getPropertyOrNull("host")       // "localhost"
     * val timeout = config.getPropertyOrNull("timeout") // null (not found)
     * ```
     *
     * @param key The attribute key to search for
     * @param namespace The namespace of the attribute (default: empty string)
     * @return The attribute value, or null if not found
     */
    fun getPropertyOrNull(key: String, namespace: String = ""): Any? {
        val nsid = NSID(key, namespace)

        // First check this tag's own attributes
        if (attributes.containsKey(nsid)) {
            return this[key, namespace]
        }

        // Then search children
        for (child in children) {
            if (child.attributes.containsKey(nsid)) {
                return child[key, namespace]
            }
        }
        return null
    }

    /**
     * Checks if this tag or its children contain a property with the given key.
     *
     * **Example**
     * ```kotlin
     * val config = KD.read("""
     *     host = "localhost"
     *     optional = nil
     * """)
     *
     * config.hasProperty("host")     // true
     * config.hasProperty("optional") // true (even though value is nil)
     * config.hasProperty("missing")  // false
     * ```
     *
     * @param key The attribute key to search for
     * @param namespace The namespace of the attribute (default: empty string)
     * @return true if an attribute with the given key exists
     */
    fun hasProperty(key: String, namespace: String = ""): Boolean {
        val nsid = NSID(key, namespace)

        // Check this tag's own attributes
        if (attributes.containsKey(nsid)) {
            return true
        }

        // Check children
        return children.any { it.attributes.containsKey(nsid) }
    }

    /**
     * Gets all properties from this tag and its children as a Map.
     *
     * Collects attributes from this tag and all child tags into a single map.
     * This tag's attributes take precedence over children's. If multiple children
     * have the same attribute key, the first one encountered wins.
     *
     * **Example**
     * ```kotlin
     * val config = KD.read("""
     *     host = "localhost"
     *     port = 8080
     *     debug = true
     * """)
     *
     * val props = config.getProperties()
     * // {NSID(host)="localhost", NSID(port)=8080, NSID(debug)=true}
     * ```
     *
     * @return A map of all properties
     */
    fun getProperties(): Map<NSID, Any?> {
        val result = LinkedHashMap<NSID, Any?>()

        // Add this tag's attributes first
        for ((nsid, value) in attributes) {
            result[nsid] = value
        }

        // Then add children's attributes
        for (child in children) {
            for ((nsid, value) in child.attributes) {
                if (!result.containsKey(nsid)) {
                    result[nsid] = value
                }
            }
        }
        return result
    }

    /**
     * Gets all properties from this tag and its children as a simple String-keyed Map.
     *
     * This is a convenience method that ignores namespaces. This tag's attributes
     * take precedence. If multiple children have attributes with the same name
     * (even in different namespaces), the first one encountered wins.
     *
     * **Example**
     * ```kotlin
     * val config = KD.read("""
     *     host = "localhost"
     *     port = 8080
     * """)
     *
     * val props = config.getPropertiesMap()
     * props["host"] // "localhost"
     * props["port"] // 8080
     * ```
     *
     * @return A map of property names to values
     */
    fun getPropertiesMap(): Map<String, Any?> {
        val result = LinkedHashMap<String, Any?>()

        // Add this tag's attributes first
        for ((nsid, value) in attributes) {
            if (!result.containsKey(nsid.name)) {
                result[nsid.name] = value
            }
        }

        // Then add children's attributes
        for (child in children) {
            for ((nsid, value) in child.attributes) {
                if (!result.containsKey(nsid.name)) {
                    result[nsid.name] = value
                }
            }
        }
        return result
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
     * val thing = tag.findChild { it.value == "green" && it.attributes["foo"] == "bar" }
     * `
     */
    inline fun findChild(predicate: (Tag) -> Boolean): Tag? {
        val searchMe = getDescendants()

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
     * val things = tag.findChildren { it.value == "green" && it.attributes["foo"] == "bar" }
     * `
     */
    inline fun findChildren(predicate: (Tag) -> Boolean): List<Tag> {
        val searchMe = getDescendants()

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
        val rows = ArrayList<List<T>>()
        for(child in children) {
            @Suppress("UNCHECKED_CAST")
            rows.add(child.values as List<T>)
        }
        return rows
    }
}