plugins {
    kotlin("plugin.serialization") version "1.6.21"
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(project(":core"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.3")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("SettxiKotlinxSerialization") {
            groupId = "dev.isxander.settxi"
            artifactId = "settxi-kotlinx-serialization"
            version = project.version as String

            artifact(tasks.jar) { builtBy(tasks.jar) }
            artifact(tasks["sourcesJar"]) { builtBy(tasks["sourcesJar"]) }
            artifact(tasks["javadocJar"])

            pom {
                name.set("Settxi")
                description.set("Annotations based settings library.")
                url.set("https://github.com/isXander/Settxi")

                scm {
                    connection.set("https://github.com/isXander/Settxi.git")
                    url.set("https://github.com/isXander/Settxi")
                }
            }
        }
    }

    repositories {
        if (hasProperty("woverflow.username") && hasProperty("woverflow.password")) {
            println("Publishing ${project.name} to W-OVERFLOW")
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = findProperty("woverflow.username")?.toString()
                    password = findProperty("woverflow.password")?.toString()
                }
            }
        }
    }
}
