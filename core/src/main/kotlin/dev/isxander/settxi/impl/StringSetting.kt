package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

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
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.string }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

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

