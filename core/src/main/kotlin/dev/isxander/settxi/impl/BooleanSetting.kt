package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType

/**
 * Setting backed by a [Boolean] value.
 *
 * No unique setting configuration.
 *
 * ```
 * var myBoolean by boolean(true) {
 *     // ...
 * }
 * ```
 */
class BooleanSetting internal constructor(
    default: Boolean,
    lambda: BooleanSetting.() -> Unit = {},
) : Setting<Boolean>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    override var shouldSave: Boolean = true

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value)
        set(new) { value = new.primitive.boolean }

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default) }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [BooleanSetting]
 */
@JvmName("booleanSetting")
fun ConfigProcessor.boolean(default: Boolean, lambda: BooleanSetting.() -> Unit): BooleanSetting {
    return BooleanSetting(default, lambda).also { settings.add(it) }
}

