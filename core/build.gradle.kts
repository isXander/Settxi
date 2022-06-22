dependencies {
    api(kotlin("stdlib-jdk8"))
}

publishing {
    publications {
        create<MavenPublication>("SettxiCore") {
            groupId = "dev.isxander.settxi"
            artifactId = "settxi-core"

            from(components["java"])
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
