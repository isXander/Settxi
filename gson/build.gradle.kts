dependencies {
    api(project(":core"))
    api("com.google.code.gson:gson:2.9.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("SettxiGson") {
            groupId = "dev.isxander.settxi"
            artifactId = "settxi-gson"

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
