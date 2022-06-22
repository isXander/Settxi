pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
    }
}

include("core")
include("kotlinx-serialization")
include("gson")

listOf(
    "cloth-config"
).forEach {
    include("gui:$it")
    findProject("gui:$it")?.name = it
}
