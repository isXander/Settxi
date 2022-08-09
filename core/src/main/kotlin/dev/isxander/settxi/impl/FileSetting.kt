package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType
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
    override var description: String? = null
    override var shouldSave: Boolean = true

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value.absolutePath)
        set(new) { value = File(new.primitive.string) }

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default.absolutePath) }

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
