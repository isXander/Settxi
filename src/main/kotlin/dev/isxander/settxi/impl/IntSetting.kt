package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor

class IntSetting internal constructor(
    default: Int,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    val min: Int,
    val max: Int,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<Int>.() -> Unit = {},
) : Setting<Int>(default, lambda) {
    override var value: Int = default
        set(value) {
            field = value.coerceIn(min..max)
        }

    override var serializedValue: Any
        get() = value
        set(new) { value = (new as Number).toInt() }

    override val defaultSerializedValue: Int = default
}

fun ConfigProcessor.int(
    default: Int,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    min: Int,
    max: Int,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<Int>.() -> Unit = {},
): IntSetting {
    val setting = IntSetting(default, name, category, subcategory, description, min, max, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}