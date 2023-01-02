import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL
import org.jetbrains.dokka.gradle.DokkaTask

group = "io.kixi"
version = "1.0.0-beta-3"
description = "ki-kd"

plugins {
    `java-library`
    kotlin("jvm") version "1.8.+"
    antlr
    id("org.jetbrains.dokka") version "1.6.21"
}

repositories {
    mavenCentral()
    // maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(files("lib/Ki.Core-1.0.0-beta-3.jar"))
    antlr("org.antlr:antlr4-runtime:4.8-1")
    // implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(platform("org.junit:junit-bom:5.8.+"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        apiVersion = "1.6"
        languageVersion = "1.6"
        jvmTarget = "${JavaVersion.VERSION_11}"
        allWarningsAsErrors = true
    }
}

/**
 * An jar containing all dependencies, optimized for Kotlin-only projects.
 */
tasks.register("jar-ktAll", Jar::class) {

    // TODO - Shut off JVM annotations that produce extra code for Java access
    group = "build"
    // manifest { attributes["Main-Class"] = "com.example.MainKt"
    archiveBaseName.set("Ki.KD-ktAll")

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
 * An jar containing all dependencies and optimized for Kotlin-only projects.
 */
tasks.register("jar-javaAll", Jar::class) {
    group = "build"
    // manifest { attributes["Main-Class"] = "com.example.MainKt"
    archiveBaseName.set("Ki.KD-javaAll")

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
