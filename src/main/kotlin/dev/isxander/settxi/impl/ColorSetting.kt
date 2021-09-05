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
import java.awt.Color

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class ColorSetting(val name: String, val category: String, val subcategory: String = "", val description: String, val save: Boolean = true, val transparency: Boolean = true)

class ColorSettingWrapped(annotation: ColorSetting, provider: IValueProvider<Color>) : Setting<Color, ColorSetting>(annotation, provider) {
    override val name: String = annotation.name
    override val category: String = annotation.category
    override val subcategory: String = annotation.subcategory
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save
    val transparency: Boolean = annotation.transparency

    override var serializedValue: Any
        get() = value.rgb
        set(new) { value = Color(new as Int) }

    override val dataType: DataTypes = DataTypes.Int

    override val defaultSerializedValue: Int = default.rgb
}
