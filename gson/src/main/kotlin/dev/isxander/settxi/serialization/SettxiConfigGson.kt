package dev.isxander.settxi.serialization

import com.google.gson.Gson
import java.nio.file.Path
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

/**
 * Imports and exports with [com.google.gson] to a file path using JSON
 */
abstract class SettxiConfigGson(val filePath: Path, val gson: Gson = Gson()) : SettxiConfig() {
    override fun import() {
        if (filePath.notExists()) {
            export()
            return
        }

        settings.gson.importFromJson(gson.toJsonTree(filePath.readText()).asJsonObject)
    }

    override fun export() {
        filePath.writeText(gson.toJson(settings.gson.asJson()))
    }
}