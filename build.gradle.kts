import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Build
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    `java-library`

    // Publishing
    `maven-publish`
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "dev.isxander"
version = "2.1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("Settxi") {
            groupId = project.group as String
            artifactId = "settxi"
            version = project.version as String

            artifact(tasks.jar) { builtBy(tasks.jar) }
            artifact(tasks["sourcesJar"]) { builtBy(tasks["sourcesJar"]) }
            artifact(tasks["javadocJar"])

            pom {
                name.set("Settxi")
                description.set("Annotations based settings library.")
                url.set("https://github.com/isXander/Settxi")

                developers {
                    developer {
                        name.set("isXander")
                        id.set("isXander")
                        email.set("business.isxander@gmail.com")
                    }
                }

                scm {
                    connection.set("https://github.com/isXander/Settxi.git")
                    url.set("https://github.com/isXander/Settxi")
                }
            }
        }
    }

    repositories {
        if (hasProperty("woverflow.token")) {
            println("Publishing ${project.name} to W-OVERFLOW")
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = "xander"
                    password = property("woverflow.token") as? String
                }
            }
        }
    }

}

