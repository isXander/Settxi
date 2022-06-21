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
        if (hasProperty("xander-repo.username") && hasProperty("xander-repo.password")) {
            maven(url = "https://maven.isxander.dev/releases") {
                credentials {
                    username = findProperty("xander-repo.username")?.toString()
                    password = findProperty("xander-repo.password")?.toString()
                }
            }
        }
    }
}
