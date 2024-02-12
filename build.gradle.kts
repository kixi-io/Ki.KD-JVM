import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

group = "io.kixi"
version = "1.0.0-beta-4"
description = "ki-kd"

val jpmsModuleName = "kixi.ki.kd"

plugins {
    `java-library`
    kotlin("jvm") version "1.9.+"
    antlr
    id("org.jetbrains.dokka") version "1.9.+"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(files("lib/ki-core-1.0.0-20240211.jar"))
    antlr("org.antlr:antlr4-runtime:4.9.+")
    // implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(platform("org.junit:junit-bom:5.10.+"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    dependsOn(":test", ":compileJava", ":compileKotlin")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    this.archiveBaseName.set("ki-kd")
    manifest {
        attributes(mapOf(
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Automatic-Module-Name" to jpmsModuleName
        ))
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
        jvmTarget.set(JvmTarget.JVM_11)
    }
    kotlinOptions {
        apiVersion = KotlinVersion.KOTLIN_1_9.version
        languageVersion = KotlinVersion.KOTLIN_1_9.version
    }
}

/**
 * A jar containing all dependencies, optimized for Kotlin-only projects.
 */
tasks.register("jar-ktAll", Jar::class) {
    dependsOn(":test", ":compileJava", ":compileKotlin")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // TODO - Shut off JVM annotations that produce extra code for Java access
    group = "build"
    // manifest { attributes["Main-Class"] = "com.example.MainKt"
    archiveBaseName.set("ki-kd-ktAll")

    var deps = configurations.compileClasspath.get().map {
        if(it.name.contains("kotlin") || it.name.contains("annotations")) {
            null
        } else {
            if (it.isDirectory) it else zipTree(it)
        }
    }

    var newDeps = mutableListOf<Any?>()
    newDeps.addAll(deps)
    newDeps.add("build/classes/kotlin/main")
    newDeps.add("build/classes/java/main")
    from(newDeps)
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
}

/**
 * A jar containing all dependencies and optimized for Kotlin-only projects.
 */
tasks.register("jar-javaAll", Jar::class) {
    dependsOn(":test", ":compileJava", ":compileKotlin")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    group = "build"
    // manifest { attributes["Main-Class"] = "com.example.MainKt"
    archiveBaseName.set("ki-kd-javaAll")

    var deps = configurations.compileClasspath.get().map {
        if(it.name.contains("kotlin-stdlib-common")) {
            null
        } else {
            if (it.isDirectory) it else zipTree(it)
        }
    }
    var newDeps = mutableListOf<Any?>()
    newDeps.addAll(deps)
    newDeps.add("build/classes/kotlin/main")
    newDeps.add("build/classes/java/main")

    for(dep in newDeps) println(dep?.javaClass?.simpleName + " $dep")

    from(newDeps)
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
}

tasks.compileTestKotlin {
    dependsOn(":generateTestGrammarSource")
}

tasks.test {
    useJUnitPlatform()

    val failedTests = mutableListOf<TestDescriptor>()
    val skippedTests = mutableListOf<TestDescriptor>()

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            when (result.resultType) {
                TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
                TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                else -> Unit
            }
        }
        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) { // root suite
                logger.lifecycle("----")
                logger.lifecycle("Test result: ${result.resultType}")
                logger.lifecycle(
                    "Test summary: ${result.testCount} tests, " +
                            "${result.successfulTestCount} succeeded, " +
                            "${result.failedTestCount} failed, " +
                            "${result.skippedTestCount} skipped")
            }
        }
    })
}
