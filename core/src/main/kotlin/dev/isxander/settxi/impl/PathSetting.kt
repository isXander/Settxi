package dev.isxander.settxi.impl

import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType
import java.nio.file.Path

/**
 * Setting backed by a [java.nio.file.Path] value.
 *
 * No unique setting configuration.
 *
 * ```
 * var myPath by path(Path.of(".")) {
 *     // ...
 * }
 * ```
 */
class PathSetting internal constructor(
    default: Path,
    lambda: PathSetting.() -> Unit = {},
) : Setting<Path>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    override var shouldSave: Boolean = true

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value.toAbsolutePath().toString())
        set(new) { value = Path.of(new.primitive.string) }

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default.toAbsolutePath().toString()) }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [FileSetting]
 */
@JvmName("pathSetting")
fun ConfigProcessor.path(default: Path, lambda: PathSetting.() -> Unit): PathSetting {
    return PathSetting(default, lambda).also { settings.add(it) }
}