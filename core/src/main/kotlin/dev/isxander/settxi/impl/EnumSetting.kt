package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType

/**
 * Setting backed by an [Enum] value.
 * Serializes with the index of the enum, not the name.
 *
 * No unique setting configuration.
 *
 * ```
 * var myEnum by enum(Alphabet.A) {
 *     // ...
 * }
 * ```
 */
class EnumSetting<T : Enum<T>>(
    default: T,
    lambda: EnumSetting<T>.() -> Unit = {},
    val enumClass: Class<T>,
    val values: Array<T>
) : Setting<T>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    override var shouldSave: Boolean = true
    var nameProvider: (T) -> String = {
        if (it is SettingDisplayName) it.displayName else it.name
    }

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value.ordinal)
        set(new) { value = values[new.primitive.int] }

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default.ordinal) }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers an [EnumSetting]
 */
@JvmName("enumSetting")
inline fun <reified T : Enum<T>> ConfigProcessor.enum(default: T, noinline lambda: EnumSetting<T>.() -> Unit): EnumSetting<T> {
    return EnumSetting(default, lambda, T::class.java, enumValues()).also { settings.add(it) }
}

interface SettingDisplayName {
    val displayName: String
}