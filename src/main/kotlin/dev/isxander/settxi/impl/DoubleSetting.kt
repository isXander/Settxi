package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive

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

    override var serializedValue: JsonElement
        get() = JsonPrimitive(value)
        set(new) { value = new.jsonPrimitive.double }

    override val defaultSerializedValue: JsonElement = JsonPrimitive(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("doubleSetting")
fun ConfigProcessor.double(default: Double, lambda: DoubleSetting.() -> Unit): DoubleSetting {
    return DoubleSetting(default, lambda).also { settings.add(it) }
}