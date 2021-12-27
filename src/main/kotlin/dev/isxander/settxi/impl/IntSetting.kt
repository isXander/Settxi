package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

class IntSetting internal constructor(
    default: Int,
    lambda: IntSetting.() -> Unit = {},
) : Setting<Int>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    lateinit var range: IntRange
    override var shouldSave: Boolean = true

    override var value: Int = default
        set(value) {
            field = value.coerceIn(range)
        }

    override var serializedValue: JsonElement
        get() = JsonPrimitive(value)
        set(new) { value = new.jsonPrimitive.int }

    override val defaultSerializedValue: JsonElement = JsonPrimitive(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("intSetting")
fun ConfigProcessor.int(default: Int, lambda: IntSetting.() -> Unit): IntSetting {
    return IntSetting(default, lambda).also { settings.add(it) }
}
