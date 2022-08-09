package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType

/**
 * Setting backed by a [Int] value.
 *
 * A range option is present which coerces all set values into it.
 *
 * ```
 * var myDouble by int(0) {
 *     range = 0..100
 *     // ...
 * }
 * ```
 */
class IntSetting internal constructor(
    default: Int,
    lambda: IntSetting.() -> Unit = {},
) : Setting<Int>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    var range: IntRange? = null
    override var shouldSave: Boolean = true

    override var value: Int = default
        set(value) {
            field = range?.let { value.coerceIn(it) } ?: value
        }

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value)
        set(new) { value = new.primitive.int }

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default) }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers am [IntSetting]
 */
@JvmName("intSetting")
fun ConfigProcessor.int(default: Int, lambda: IntSetting.() -> Unit): IntSetting {
    return IntSetting(default, lambda).also { settings.add(it) }
}
