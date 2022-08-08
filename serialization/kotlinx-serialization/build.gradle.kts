plugins {
    kotlin("plugin.serialization") version "1.7.10"
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
