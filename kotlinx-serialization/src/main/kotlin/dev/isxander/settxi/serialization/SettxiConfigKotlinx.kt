package dev.isxander.settxi.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText

/**
 * Imports and exports with [kotlinx.serialization] to a file path using JSON
 */
abstract class SettxiConfigKotlinx(val filePath: Path, val json: Json = Json) : SettxiConfig() {
    override fun import() {
        if (filePath.notExists()) {
            export()
            return
        }

        settings.kotlinx.importFromJson(json.decodeFromString(filePath.readText()))
    }

    override fun export() {
        filePath.writeText(json.encodeToString(settings.kotlinx.asJson()))
    }
}