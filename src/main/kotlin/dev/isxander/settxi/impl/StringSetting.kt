package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettingAdapter
import dev.isxander.settxi.serialization.ConfigProcessor

class StringSetting internal constructor(
    default: String,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<String>.() -> Unit = {},
) : Setting<String>(default, lambda) {
    override var serializedValue: Any
        get() = value
        set(new) { value = new as String }

    override val defaultSerializedValue: String = default
}

fun ConfigProcessor.string(
    default: String,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<String>.() -> Unit = {},
): StringSetting {
    val setting = StringSetting(default, name, category, subcategory, description, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}
