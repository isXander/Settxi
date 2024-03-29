plugins {
    id("fabric-loom") version "1.2.+"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.isxander.dev/releases")
    maven("https://maven.terraformersmc.com")
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

val minecraftVersion = "1.20"

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$minecraftVersion+build.+:v2")

    modImplementation("net.fabricmc:fabric-loader:0.14.+")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.4+kotlin.1.8.21")

    api(project(":gui"))
    modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:3.0.0+1.20")

    "testmodImplementation"(sourceSets.main.get().output)
    "modTestmodImplementation"("com.terraformersmc:modmenu:7.0.0")
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
            artifactId = "settxi-gui-yacl"

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("XANDER_MAVEN_USER") && hasProperty("XANDER_MAVEN_PASS")) {
            maven(url = "https://maven.isxander.dev/releases") {
                name = "XanderMaven"
                credentials {
                    username = property("XANDER_MAVEN_USER") as? String
                    password = property("XANDER_MAVEN_PASS") as? String
                }
            }
        } else {
            println("Xander Maven can't publish")
        }
    }
}
