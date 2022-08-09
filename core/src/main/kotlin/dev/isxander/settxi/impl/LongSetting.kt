package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType

/**
 * Setting backed by a [Long] value.
 *
 * A range option is present which coerces all set values into it.
 *
 * ```
 * var myDouble by long(0L) {
 *     range = 0L..100L
 *     // ...
 * }
 * ```
 */
class LongSetting internal constructor(
    default: Long,
    lambda: LongSetting.() -> Unit = {},
) : Setting<Long>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    var range: LongRange? = null
    override var shouldSave: Boolean = true

    override var value: Long = default
        set(value) {
            field = range?.let { value.coerceIn(it) } ?: value
        }

    override var serializedValue: SerializedType
        get() = PrimitiveType.of(value)
        set(new) { value = new.primitive.long }

    override val defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> PrimitiveType.of(default) }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [LongSetting]
 */
@JvmName("longSetting")
fun ConfigProcessor.long(default: Long, lambda: LongSetting.() -> Unit): LongSetting {
    return LongSetting(default, lambda).also { settings.add(it) }
}
