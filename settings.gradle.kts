pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
    }
}

include("core")
include("kotlinx-serialization")

listOf(
    "cloth-config"
).forEach {
    include("gui:$it")
    findProject("gui:$it")?.name = it
}
