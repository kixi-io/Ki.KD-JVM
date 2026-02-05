
import io.kixi.kd.KD
import io.kixi.NSID
import io.kixi.kd.Tag

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val name = "Kotlin"
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    println("Hello, " + name + "!")

    for (i in 1..5) {
        //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
        // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
        println("i = $i")
    }

    println(KD.read("0:00:00"))

    println(KD.read("""
        lemurs {
            Sifaka
            Ruffed
        }
    """.trimIndent()
    ))

    println(KD.read("""
        @Greeting("English")
        @UI(framework="HTML5") @Coco
        @Test(true log="output.txt")
        @Test2(hola on "Spanish" log="output.txt")
        hello {
            tag1 foo fun=true [1 2 3]
        }
    """.trimIndent()))

    println(KD.read("""
        literals {
            lit1 23
            [5, 6, 7]
            colors red green orange
            lit2 46.12345bd [1, 2, 4]
            lit3 https://ikayzo.com
            lit4 46.12345cm
            .blob(sdf789GSfsb2+3324sf2) // Blob
        }
    """.trimIndent()))

    println(KD.read("""
        calls {
            rgb()
            rgb(1)
            rgb(1, 2)
            rgb(name = "Noa")
            rgb(name = "Noa", animal = "lizard")
            rgb(6, 7, name = "Noa", animal = "lizard")
        }
        
        println()
    """))

    println(KD.read("""
        "hello guys"
        @hello
        guys
        @aloha wahine
        // @"hello guys"
    """)
    )

    var annotatedTag1 = KD.read("@aloha wahine")
    println("Tag1 name: '${annotatedTag1.nsid.name}'")
    println("Tag1 annotations: ${annotatedTag1.annotations}")
    println("Tag1 full: $annotatedTag1")

    println(KD.read("""@"This is text""""))

    println(annotatedTag1.annotations)

    var annotatedTag2 = KD.read("""
                                    @aloha
                                    kane
                                    """)
    println(annotatedTag2.annotations + " for ${annotatedTag2.nsid.name}")

    println(KD.read("wahine 1; bula 2"))
    println(KD.read("""
        wahine 1
        bula 2
    """.trimIndent()))
    // println(KD.read("@aloha wahine; bula"))

    println(KD.read("""
        .grid(
           2 4 6
           8 9 10
        )
    """.trimIndent()))

    println(KD.read("""
        .grid(
           "two" "four" "six"
           "eight" "nine" "ten"
        )
    """.trimIndent()))

    println(KD.read("""
        .grid<String>(
           "two" "four" "six"
           "eight" "nine" "ten"
        )
    """.trimIndent()))

    println(KD.read("""
        .grid(
           "uno" "dos" "tres"
           "cuatro" "cinco" "seis"
        )
    """.trimIndent()))

    println(KD.read("""
        .grid(
           1 2 3
        )
    """.trimIndent()))

    println(KD.read("""
        .grid(10 20 30)
    """.trimIndent()))

    println(KD.read("""
        .grid(1 2 3; 40 50 60)
    """.trimIndent()))

    println(KD.read("""
        .grid(1, 2, 3)
    """.trimIndent()))

    println(KD.read("""
        .grid(
           111.23, 222.42, 3.1
        )
    """.trimIndent()))

    println(KD.read("""
        .grid(
           111.23, 222, 3.1f
        )
    """.trimIndent()))

    println(KD.read("""
        .grid<Int>(
           111, 222, 3
        )
    """.trimIndent()))

    println(KD.read("""
        .grid(
           1, "hi", dan@ikayzo.com
        )
    """.trimIndent()))

    println(KD.read("""
        .grid(
           dan@ikayzo.com noa@reptiles.org
           bill@monkeys.io atsuko@okinawa.co.jp
        )
    """.trimIndent()))

    println(KD.read("""
        123 name = jose
    """.trimIndent()))

    // Read a KD String, File, URL or Resource
    val root = KD.read("""
        name = jose;
        age = 25
    """)

    println(root)
    println(root.getProperty("name")) // "jose"
    println(root.getProperty("age")) // 25
}