package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

class BooleanSetting internal constructor(
    default: Boolean,
    lambda: BooleanSetting.() -> Unit = {},
) : Setting<Boolean>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.boolean }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("booleanSetting")
fun ConfigProcessor.boolean(default: Boolean, lambda: BooleanSetting.() -> Unit): BooleanSetting {
    return BooleanSetting(default, lambda).also { settings.add(it) }
}

