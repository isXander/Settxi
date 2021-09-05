/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.providers.IValueProvider
import dev.isxander.settxi.utils.DataTypes

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class OptionSetting(val name: String, val category: String, val subcategory: String = "", val description: String, val save: Boolean = true)

class OptionSettingWrapped(annotation: OptionSetting, provider: IValueProvider<OptionContainer.Option>) : Setting<OptionContainer.Option, OptionSetting>(annotation, provider) {
    override val name: String = annotation.name
    override val category: String = annotation.category
    override val subcategory: String = annotation.subcategory
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save
    val options = value.values

    override var serializedValue: Any
        get() = value.id
        set(new) { value = options.find { it.id == new as String }!! }

    override val defaultSerializedValue: String = default.id

    override val dataType: DataTypes = DataTypes.String
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