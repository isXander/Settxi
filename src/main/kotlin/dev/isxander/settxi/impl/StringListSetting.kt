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
import dev.isxander.settxi.utils.levenshtein

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class StringListSetting(val name: String, val category: String, val subcategory: String = "", val description: String, val options: Array<String>, val save: Boolean = true)

class StringListSettingWrapped(annotation: StringListSetting, provider: IValueProvider<String>) : Setting<String, StringListSetting>(annotation, provider) {
    override val name: String = annotation.name
    override val category: String = annotation.category
    override val subcategory: String = annotation.subcategory
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save
    val options: Array<String> = annotation.options

    override var serializedValue: Any
        get() = value
        set(new) { value = options.sortedBy { it.levenshtein(new as String) }[0] }

    override val defaultSerializedValue: String = default

    override val dataType: DataTypes = DataTypes.String
}