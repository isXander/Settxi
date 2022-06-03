package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

class StringSetting internal constructor(
    default: String,
    lambda: StringSetting.() -> Unit = {},
) : Setting<String>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value)
        set(new) { value = new.string }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("stringSetting")
fun ConfigProcessor.string(default: String, lambda: StringSetting.() -> Unit): StringSetting {
    return StringSetting(default, lambda).also { settings.add(it) }
}

