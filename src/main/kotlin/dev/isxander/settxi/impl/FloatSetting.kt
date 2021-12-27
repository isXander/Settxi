package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonPrimitive

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

    override var serializedValue: JsonElement
        get() = JsonPrimitive(value)
        set(new) { value = new.jsonPrimitive.float }

    override val defaultSerializedValue: JsonElement = JsonPrimitive(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("floatSetting")
fun ConfigProcessor.float(default: Float, lambda: FloatSetting.() -> Unit): FloatSetting {
    return FloatSetting(default, lambda).also { settings.add(it) }
}
