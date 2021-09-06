package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettingAdapter
import dev.isxander.settxi.serialization.ConfigProcessor

class BooleanSetting internal constructor(
    default: Boolean,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<Boolean>.() -> Unit = {},
) : Setting<Boolean>(default, lambda) {
    override var serializedValue: Any
        get() = value
        set(new) { value = new as Boolean }

    override val defaultSerializedValue: Boolean = default
}

fun ConfigProcessor.boolean(
    default: Boolean,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<Boolean>.() -> Unit = {},
): BooleanSetting {
    val setting = BooleanSetting(default, name, category, subcategory, description, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}

