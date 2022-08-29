package dev.isxander.settxi.serialization

import dev.isxander.settxi.SettxiConfig
import java.nio.file.Path
import kotlin.io.path.notExists
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes

/**
 * Encodes and decodes settings from and to a file using a [SerializationHelper]
 */
abstract class SettxiFileConfig(val filePath: Path, val serializationHelper: SerializationHelper<*>) : SettxiConfig() {
    override fun import() {
        if (filePath.notExists() || !serializationHelper.importFromBytes(filePath.readBytes(), settings)) {
            export()
        }
    }

    override fun export() {
        filePath.writeBytes(serializationHelper.asBytes(settings))
    }
}

@Deprecated(message = "Moved package", replaceWith = ReplaceWith("dev.isxander.settxi.SettxiConfig"), level = DeprecationLevel.ERROR)
typealias SettxiConfig = SettxiConfig