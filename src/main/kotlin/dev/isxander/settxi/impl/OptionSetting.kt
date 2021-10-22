package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor

class OptionSetting internal constructor(
    default: OptionContainer.Option,
    override val name: String,
    override val category: String,
    override val subcategory: String? = null,
    override val description: String,
    override val shouldSave: Boolean = true,
    lambda: SettingAdapter<OptionContainer.Option>.() -> Unit = {},
) : Setting<OptionContainer.Option>(default, lambda) {
    val options = value.values

    override var serializedValue: Any
        get() = value.id
        set(new) { value = options.find { it.id == new as String }!! }

    override val defaultSerializedValue: String = default.id
}

fun ConfigProcessor.option(
    default: OptionContainer.Option,
    name: String,
    category: String,
    subcategory: String? = null,
    description: String,
    shouldSave: Boolean = true,
    lambda: SettingAdapter<OptionContainer.Option>.() -> Unit = {},
): OptionSetting {
    val setting = OptionSetting(default, name, category, subcategory, description, shouldSave, lambda)
    this.settings.add(setting)
    return setting
}

abstract class OptionContainer {
    val options = arrayListOf<Option>()
    protected fun option(name: String, description: String? = null): Option = Option(this, name, description)

    class Option internal constructor(private val container: OptionContainer, val name: String, val description: String?) {
        init {
            container.options.add(this)
        }

        val id = name
            .lowercase()
            .replace(Regex("[^\\w]+"), "_")
            .trim { it == '_' || it.isWhitespace() }

        val ordinal = container.options.indexOf(this)
        val values get() = container.options
    }

}