import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Build
    kotlin("jvm") version "1.6.21" apply false
    `java-library`
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "dev.isxander"
    version = "2.2.2"

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}
