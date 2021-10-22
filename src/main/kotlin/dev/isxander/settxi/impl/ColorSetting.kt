package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import java.awt.Color

class ColorSetting internal constructor(
    default: Color,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    val allowTransparency: Boolean = true,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<Color>.() -> Unit = {},
) : Setting<Color>(default, lambda) {
    override var serializedValue: Any
        get() = value.rgb
        set(new) { value = Color(new as Int) }

    override val defaultSerializedValue: Int = default.rgb
}

fun ConfigProcessor.color(
    default: Color,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    allowTransparency: Boolean = true,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<Color>.() -> Unit = {},
): ColorSetting {
    val setting = ColorSetting(default, name, category, subcategory, description, allowTransparency, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}
