package dev.isxander.settxi.serialization

import com.google.gson.Gson
import java.nio.file.Path

/**
 * Imports and exports with [com.google.gson] to a file path using JSON
 */
@Deprecated("Replaced with new abstract api", ReplaceWith("SettxiFileConfig(filePath, gsonSerializer(gson))"))
abstract class SettxiConfigGson(filePath: Path, gson: Gson = Gson()) : SettxiFileConfig(filePath, gsonSerializer(gson))