import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Build
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    `java-library`

    // Publishing
    `maven-publish`
    signing
}

java {
    withSourcesJar()
    withJavadocJar()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

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

val jarTask = tasks.named("jar").get()
val javadocJarTask = tasks.getByName("javadocJar")
val sourcesJarTask = tasks.named("sourcesJar").get()

publishing {
    publications {
        create<MavenPublication>("Settxi") {
            groupId = project.group as String
            artifactId = "settxi"
            version = project.version as String

            artifact(jarTask) { builtBy(jarTask) }
            artifact(sourcesJarTask) { builtBy(sourcesJarTask) }
            artifact(javadocJarTask)

            pom {
                name.set("Settxi")
                description.set("Annotations based settings library.")
                url.set("https://github.com/isXander/Settxi")

                licenses {
                    license {
                        name.set("LGPL 2.1 License")
                        url.set("https://www.gnu.org/licenses/lgpl-2.1.en.html")
                    }
                }

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
        val user = System.getenv("OSSRH_USERNAME")
        val pass = System.getenv("OSSRH_PASSWORD")
        if (user != null && pass != null) {
            println("Publishing...")
            maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2") {
                name = "Central"
                credentials {
                    username = user
                    password = pass
                }
            }
        }

        mavenLocal()
    }

}

