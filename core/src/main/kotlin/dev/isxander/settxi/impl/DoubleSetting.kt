package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

class DoubleSetting internal constructor(
    default: Double,
    lambda: DoubleSetting.() -> Unit = {},
) : Setting<Double>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    lateinit var range: ClosedFloatingPointRange<Double>
    override var shouldSave: Boolean = true

    override var value: Double = default
        set(value) {
            field = value.coerceIn(range)
        }

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.double }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("doubleSetting")
fun ConfigProcessor.double(default: Double, lambda: DoubleSetting.() -> Unit): DoubleSetting {
    return DoubleSetting(default, lambda).also { settings.add(it) }
}