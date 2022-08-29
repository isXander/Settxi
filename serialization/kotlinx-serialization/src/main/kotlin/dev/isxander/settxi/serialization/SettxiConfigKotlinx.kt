package dev.isxander.settxi.serialization

import kotlinx.serialization.json.Json
import java.nio.file.Path

/**
 * Imports and exports with [kotlinx.serialization] to a file path using JSON
 */
@Deprecated("Replaced with new abstract api", ReplaceWith("SettxiFileConfig(filePath, kotlinxSerializer(json))"))
abstract class SettxiConfigKotlinx(filePath: Path, json: Json = Json) : SettxiFileConfig(filePath, kotlinxSerializer(json))