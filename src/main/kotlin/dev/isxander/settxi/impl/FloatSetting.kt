package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettingAdapter
import dev.isxander.settxi.serialization.ConfigProcessor

class FloatSetting internal constructor(
    default: Float,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<Float>.() -> Unit = {},
) : Setting<Float>(default, lambda) {
    override var serializedValue: Any
        get() = value
        set(new) { value = new as Float }

    override val defaultSerializedValue: Float = default
}

fun ConfigProcessor.float(
    default: Float,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<Float>.() -> Unit = {},
): FloatSetting {
    val setting = FloatSetting(default, name, category, subcategory, description, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}