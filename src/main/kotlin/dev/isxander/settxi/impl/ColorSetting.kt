package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import java.awt.Color

class ColorSetting internal constructor(
    default: Color,
    lambda: ColorSetting.() -> Unit = {},
) : Setting<Color>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: JsonElement
        get() = JsonPrimitive(value.rgb)
        set(new) { value = Color(new.jsonPrimitive.int) }

    override val defaultSerializedValue: JsonElement = JsonPrimitive(default.rgb)

    init {
        this.apply(lambda)
    }
}

@JvmName("colorSetting")
fun ConfigProcessor.color(default: Color, lambda: ColorSetting.() -> Unit): ColorSetting {
    return ColorSetting(default, lambda).also { settings.add(it) }
}
