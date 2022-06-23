import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Build
    kotlin("jvm") version "1.7.0" apply false
    `java-library`
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = "dev.isxander.settxi"
    version = "2.5.0"

    java {
        withSourcesJar()
        withJavadocJar()
    }

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
