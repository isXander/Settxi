package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

class IntSetting internal constructor(
    default: Int,
    lambda: IntSetting.() -> Unit = {},
) : Setting<Int>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    lateinit var range: IntRange
    override var shouldSave: Boolean = true

    override var value: Int = default
        set(value) {
            field = value.coerceIn(range)
        }

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.int }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("intSetting")
fun ConfigProcessor.int(default: Int, lambda: IntSetting.() -> Unit): IntSetting {
    return IntSetting(default, lambda).also { settings.add(it) }
}
