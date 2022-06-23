package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

/**
 * Setting backed by a [Double] value.
 *
 * A range option is present which coerces all set values into it.
 *
 * ```
 * var myDouble by double(0.0) {
 *     range = 0.0..1.0
 *     // ...
 * }
 * ```
 */
class DoubleSetting internal constructor(
    default: Double,
    lambda: DoubleSetting.() -> Unit = {},
) : Setting<Double>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    var range: ClosedFloatingPointRange<Double>? = null
    override var shouldSave: Boolean = true

    override var value: Double = default
        set(value) {
            field = range?.let { value.coerceIn(it) } ?: value
        }

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.double }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [DoubleSetting]
 */
@JvmName("doubleSetting")
fun ConfigProcessor.double(default: Double, lambda: DoubleSetting.() -> Unit): DoubleSetting {
    return DoubleSetting(default, lambda).also { settings.add(it) }
}