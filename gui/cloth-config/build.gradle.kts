plugins {
    id("fabric-loom") version "0.12.+"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.shedaniel.me/")
}

val minecraftVersion = "1.19"

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.+:v2")

    modImplementation("net.fabricmc:fabric-loader:0.14.+")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.2+kotlin.1.7.10")

    api(project(":core"))
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:7.+")
}

tasks {
    remapJar {
        archiveClassifier.set("fabric-$minecraftVersion")
    }

    remapSourcesJar {
        archiveClassifier.set("fabric-$minecraftVersion-sources")
    }

    javadocJar {
        archiveClassifier.set("fabric-$minecraftVersion-javadoc")
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("settxi") {
            groupId = "dev.isxander.settxi"
            artifactId = "settxi-gui-cloth-config"

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("xander-repo.username") && hasProperty("xander-repo.password")) {
            maven(url = "https://maven.isxander.dev/releases") {
                name = "XanderMaven"
                credentials {
                    username = property("xander-repo.username") as? String
                    password = property("xander-repo.password") as? String
                }
            }
        }
    }
}
