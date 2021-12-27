package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

class BooleanSetting internal constructor(
    default: Boolean,
    lambda: BooleanSetting.() -> Unit = {},
) : Setting<Boolean>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: JsonElement
        get() = JsonPrimitive(value)
        set(new) { value = new.jsonPrimitive.boolean }

    override val defaultSerializedValue: JsonElement = JsonPrimitive(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("booleanSetting")
fun ConfigProcessor.boolean(default: Boolean, lambda: BooleanSetting.() -> Unit): BooleanSetting {
    return BooleanSetting(default, lambda).also { settings.add(it) }
}

