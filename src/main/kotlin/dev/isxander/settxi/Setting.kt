package dev.isxander.settxi

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Setting<T>(val default: T, lambda: SettingAdapter<T>.() -> Unit = {}) : SettingAdapter<T>(lambda), ReadWriteProperty<Any, T> {
    abstract val name: String
    abstract val category: String
    abstract val subcategory: String?
    abstract val description: String
    abstract val shouldSave: Boolean

    protected open var value: T = default

    fun get() = getter(value)
    fun set(value: T) { this.value = setter(value) }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = get()
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = set(value)

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





