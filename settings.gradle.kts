pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
    }
}

include("core")
include(":serialization:kotlinx-serialization")
include(":serialization:gson")

listOf(
    "cloth-config",
    "spruce-ui"
).forEach {
    include("gui:$it")
    findProject("gui:$it")?.name = it
}
