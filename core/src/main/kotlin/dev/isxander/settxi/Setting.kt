package dev.isxander.settxi

import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.SerializedType
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Setting<T>(val default: T) : ReadWriteProperty<Any, T> {
    abstract val name: String
    abstract val category: String
    abstract val description: String?
    abstract val shouldSave: Boolean
    open var hidden = false

    protected open var value: T = default

    /**
     * Gets the current value
     *
     * @param useListener use the custom getter from [Setting.modifyGet]
     */
    fun get(useListener: Boolean = true): T {
        return if (useListener) getter(this.value)
            else this.value
    }

    /**
     * Sets the current value
     *
     * @param useListener use the custom setter from [Setting.modifySet]
     */
    fun set(value: T, useListener: Boolean = true) {
        if (useListener) this.value = setter(value)
        else this.value = value
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = get()
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = set(value)

    abstract var serializedValue: SerializedType
        protected set
    abstract val defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType

    fun setSerialized(type: SerializedType) {
        serializedValue = migrator(type)
    }

    val nameSerializedKey: String by lazy { name.toJsonKey() }
    val categorySerializedKey: String by lazy { category.toJsonKey() }

    fun reset() {
        value = default
    }

    protected var getter: (T) -> T = { it }
    protected var setter: (T) -> T = { it }

    /**
     * Modifies the delegate [Setting.get] getter
     */
    fun modifyGet(lambda: (T) -> T) { getter = lambda }

    /**
     * Modifies the delegate [Setting.set] setter
     */
    fun modifySet(lambda: (T) -> T) { setter = lambda }

    @Deprecated("Use clearer name", ReplaceWith("modifyGet(lambda)"))
    fun get(lambda: (T) -> T) { getter = lambda }

    @Deprecated("Use clearer name", ReplaceWith("modifySet(lambda)"))
    fun set(lambda: (T) -> T) { setter = lambda }

    protected var migrator: Migrator = { it }

    /**
     * Migrates the serialized setting to another type
     * if you changed the setting type
     */
    fun migrator(lambda: Migrator) { migrator = lambda }

    private fun String.toJsonKey() =
        this
            .lowercase()
            .replace(Regex("[^\\w]+"), "_")
            .trim { it == '_' || it.isWhitespace() }
}





