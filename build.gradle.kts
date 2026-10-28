plugins {
    kotlin("jvm") version "2.0.20"
    `java-library`
    `maven-publish`
}

group = "io.kixi"
version = "2.4.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

dependencies {
    // Ki.Core-JVM via composite build. `api`: KD's own API hands out Ki.Core
    // types (Tag, Call, Quantity, Grid...), so a consumer must see them too.
    api("io.kixi:Ki.Core-JVM:2.5.0")

    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-property:5.9.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

// ============================================================================
// Publishing (GitHub Packages, as Ki.Core-JVM and Ki.KS-JVM)
// ============================================================================

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kixi-io/Ki.KD-JVM")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                    ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                groupId = "${project.group}"
                artifactId = "Ki.KD-JVM"
                name.set("Ki.KD")
                description.set("A JVM implementation of KD (Ki Declarative) and KDS, its schema language")
                url.set("https://kixi.io")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://raw.githubusercontent.com/kixi-io/Ki.KD-JVM/master/LICENSE.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("dleuck")
                        name.set("Daniel Leuck")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/kixi-io/Ki.KD-JVM.git")
                    developerConnection.set("scm:git:ssh://github.com/kixi-io/Ki.KD-JVM.git")
                    url.set("https://github.com/kixi-io/Ki.KD-JVM/")
                }
            }
        }
    }
}
