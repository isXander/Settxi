package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

class LongSetting internal constructor(
    default: Long,
    lambda: LongSetting.() -> Unit = {},
) : Setting<Long>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    lateinit var range: LongRange
    override var shouldSave: Boolean = true

    override var value: Long = default
        set(value) {
            field = value.coerceIn(range)
        }

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.long }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("longSetting")
fun ConfigProcessor.long(default: Long, lambda: LongSetting.() -> Unit): LongSetting {
    return LongSetting(default, lambda).also { settings.add(it) }
}
