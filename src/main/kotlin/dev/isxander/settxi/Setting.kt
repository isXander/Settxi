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

package dev.isxander.settxi

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Setting<T>(val default: T, lambda: SettingAdapter<T>.() -> Unit = {}) : SettingAdapter<T>(lambda), ReadWriteProperty<Any, T> {
    abstract val name: String
    abstract val category: String
    abstract val subcategory: String?
    abstract val description: String
    abstract val shouldSave: Boolean

    protected var value: T = default

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return getter(value)
    }
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = setter(value)
    }

    abstract var serializedValue: Any
    abstract val defaultSerializedValue: Any
    val nameSerializedKey: String by lazy {
        name
            .lowercase()
            .replace(Regex("[^\\w]+"), "_")
            .trim { it == '_' || it.isWhitespace() }
    }
    val nameSerializedCategoryAndKey: String by lazy {
        "$category.${if (subcategory != null) "$subcategory." else ""}$nameSerializedKey"
    }

    val hidden: Boolean
        get() = !depends.all { it(value) }

    fun reset() {
        value = default
    }
}





