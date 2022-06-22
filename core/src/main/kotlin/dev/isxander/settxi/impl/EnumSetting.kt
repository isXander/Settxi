package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

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
    private val values: Array<T>
) : Setting<T>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true
    lateinit var nameProvider: (T) -> String

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value.ordinal)
        set(new) { value = values[new.int] }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default.ordinal)

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