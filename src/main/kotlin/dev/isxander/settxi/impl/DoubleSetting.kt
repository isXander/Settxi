package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettingAdapter
import dev.isxander.settxi.serialization.ConfigProcessor

class DoubleSetting internal constructor(
    default: Double,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<Double>.() -> Unit = {},
) : Setting<Double>(default, lambda) {
    override var serializedValue: Any
        get() = value
        set(new) { value = new as Double }

    override val defaultSerializedValue: Double = default
}

fun ConfigProcessor.double(
    default: Double,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<Double>.() -> Unit = {},
): DoubleSetting {
    val setting = DoubleSetting(default, name, category, subcategory, description, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}