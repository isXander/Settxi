plugins {
    id("fabric-loom") version "1.0.+"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com")
    maven("https://maven.gegy.dev")
}

val testmod by sourceSets.registering {
    compileClasspath += sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

loom {
    runs {
        register("testmod") {
            client()
            ideConfigGenerated(true)
            name("Test Mod")
            source(testmod.get())
        }
    }

    createRemapConfigurations(testmod.get())
}

val minecraftVersion = "1.19.2"

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.+:v2")

    modImplementation("net.fabricmc:fabric-loader:0.14.+")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.3+kotlin.1.7.10")

    api(project(":gui"))
    modApi("dev.lambdaurora:spruceui:4.0.0+1.19")

    "modTestmodImplementation"("com.terraformersmc:modmenu:4.0.6")

    "testmodImplementation"(sourceSets.main.get().output)
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
            artifactId = "settxi-gui-spruce-ui"

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
        } else {
            println("Xander Maven can't publish")
        }
    }
}
