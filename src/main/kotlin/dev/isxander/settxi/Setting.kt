package dev.isxander.settxi

import kotlinx.serialization.json.JsonElement
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Setting<T>(val default: T) : ReadWriteProperty<Any, T> {
    abstract val name: String
    abstract val category: String
    abstract val description: String
    abstract val shouldSave: Boolean

    protected open var value: T = default

    fun get(useListener: Boolean = true): T {
        return if (useListener) getter(this.value)
            else this.value
    }
    fun set(value: T, useListener: Boolean = true) {
        if (useListener) this.value = setter(value)
        else this.value = value
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = get()
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = set(value)

    abstract var serializedValue: JsonElement
    abstract val defaultSerializedValue: JsonElement
    val nameSerializedKey: String by lazy {
        name
            .lowercase()
            .replace(Regex("[^\\w]+"), "_")
            .trim { it == '_' || it.isWhitespace() }
    }

    val hidden: Boolean
        get() = !depends.all { it(value) }

    fun reset() {
        value = default
    }

    protected var getter: (T) -> T = { it }
    protected var setter: (T) -> T = { it }
    protected var depends: MutableList<(T) -> Boolean> = mutableListOf()

    fun get(lambda: (T) -> T) { getter = lambda }
    fun set(lambda: (T) -> T) { setter = lambda }
    fun depends(lambda: (T) -> Boolean) = depends.add(lambda)
}





