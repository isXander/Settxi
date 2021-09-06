plugins {
    // Build
    kotlin("jvm") version "1.5.30"
    `java-library`

    // Publishing
    id("org.jetbrains.dokka") version "1.5.0"
    `maven-publish`
    signing
}

java {
    withSourcesJar()
    withJavadocJar()
}

group = "dev.isxander"
version = "1.0"

dependencies {
    api("com.electronwill.night-config:core:3.6.4")
    api("org.jetbrains.kotlin:kotlin-reflect:1.5.30")
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
        maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2") {
            name = "Central"
            credentials {
                username = property("ossrh.username") as String
                password = property("ossrh.password") as String
            }
        }
        maven("https://maven.pkg.github.com/isxander/settxi") {
            name = "GitHub"
            credentials {
                username = property("gpr.user") as String
                password = property("gpr.key") as String
            }
        }
    }

}

signing {
    sign(publishing.publications)
}