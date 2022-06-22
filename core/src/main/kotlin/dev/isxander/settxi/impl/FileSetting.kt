package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType
import java.io.File

/**
 * Setting backed by a [java.io.File] value.
 *
 * No unique setting configuration.
 *
 * ```
 * var myFile by file(File(".")) {
 *     // ...
 * }
 * ```
 */
class FileSetting internal constructor(
    default: File,
    lambda: FileSetting.() -> Unit = {},
) : Setting<File>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value.path)
        set(new) { value = File(new.string) }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default.path)

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [FileSetting]
 */
@JvmName("fileSetting")
fun ConfigProcessor.file(default: File, lambda: FileSetting.() -> Unit): FileSetting {
    return FileSetting(default, lambda).also { settings.add(it) }
}
