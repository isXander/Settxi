plugins {
    kotlin("plugin.serialization") version "1.7.0"
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
        if (hasProperty("xander-repo.username") && hasProperty("xander-repo.password")) {
            maven(url = "https://maven.isxander.dev/releases") {
                name = "XanderMaven"
                credentials {
                    username = findProperty("xander-repo.username")?.toString()
                    password = findProperty("xander-repo.password")?.toString()
                }
            }
        }
    }
}
