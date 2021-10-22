package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor

class DoubleSetting internal constructor(
    default: Double,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    val min: Double,
    val max: Double,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<Double>.() -> Unit = {},
) : Setting<Double>(default, lambda) {
    override var value: Double = default
        set(value) {
            field = value.coerceIn(min..max)
        }

    override var serializedValue: Any
        get() = value
        set(new) { value = (new as Number).toDouble() }

    override val defaultSerializedValue: Double = default
}

fun ConfigProcessor.double(
    default: Double,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    min: Double,
    max: Double,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<Double>.() -> Unit = {},
): DoubleSetting {
    val setting = DoubleSetting(default, name, category, subcategory, description, min, max, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}