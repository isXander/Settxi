java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    api(kotlin("stdlib-jdk8"))
}

publishing {
    publications {
        create<MavenPublication>("SettxiCore") {
            groupId = "dev.isxander.settxi"
            artifactId = "settxi-core"
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
