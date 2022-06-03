package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

class FloatSetting internal constructor(
    default: Float,
    lambda: FloatSetting.() -> Unit = {},
) : Setting<Float>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    lateinit var range: ClosedFloatingPointRange<Float>
    override var shouldSave: Boolean = true

    override var value: Float = default
        set(value) {
            field = value.coerceIn(range)
        }

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.float }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("floatSetting")
fun ConfigProcessor.float(default: Float, lambda: FloatSetting.() -> Unit): FloatSetting {
    return FloatSetting(default, lambda).also { settings.add(it) }
}
