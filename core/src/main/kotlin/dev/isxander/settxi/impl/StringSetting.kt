package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType

/**
 * Setting backed by a [String] value.
 *
 * No unique setting configuration.
 *
 * ```
 * var myString by string("Wow, I think this is a string!") {
 *     // ...
 * }
 * ```
 */
class StringSetting internal constructor(
    default: String,
    lambda: StringSetting.() -> Unit = {},
) : Setting<String>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    override var shouldSave: Boolean = true

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value)
        set(new) { value = new.primitive.string }

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default) }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [StringSetting]
 */
@JvmName("stringSetting")
fun ConfigProcessor.string(default: String, lambda: StringSetting.() -> Unit): StringSetting {
    return StringSetting(default, lambda).also { settings.add(it) }
}

