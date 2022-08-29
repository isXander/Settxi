package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType

/**
 * Setting backed by a [Float] value.
 *
 * A range option is present which coerces all set values into it.
 *
 * ```
 * var myDouble by float(0f) {
 *     range = 0f..1f
 *     // ...
 * }
 * ```
 */
class FloatSetting internal constructor(
    default: Float,
    lambda: FloatSetting.() -> Unit = {},
) : NumberSetting<Float>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    var range: ClosedFloatingPointRange<Float>? = null
    override var shouldSave: Boolean = true

    override var value: Float = default
        set(value) {
            field = range?.let { value.coerceIn(it) } ?: value
        }

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value)
        set(new) { value = new.primitive.float }

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default) }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [FloatSetting]
 */
@JvmName("floatSetting")
fun ConfigProcessor.float(default: Float, lambda: FloatSetting.() -> Unit): FloatSetting {
    return FloatSetting(default, lambda).also { settings.add(it) }
}
