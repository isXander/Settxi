package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettingAdapter
import dev.isxander.settxi.serialization.ConfigProcessor

class IntSetting internal constructor(
    default: Int,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<Int>.() -> Unit = {},
) : Setting<Int>(default, lambda) {
    override var serializedValue: Any
        get() = value
        set(new) { value = new as Int }

    override val defaultSerializedValue: Int = default
}

fun ConfigProcessor.int(
    default: Int,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<Int>.() -> Unit = {},
): IntSetting {
    val setting = IntSetting(default, name, category, subcategory, description, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}