package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.jsonPrimitive

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

    override var serializedValue: JsonElement
        get() = JsonPrimitive(value)
        set(new) { value = new.jsonPrimitive.long }

    override val defaultSerializedValue: JsonElement = JsonPrimitive(default)

    init {
        this.apply(lambda)
    }
}

@JvmName("longSetting")
fun ConfigProcessor.long(default: Long, lambda: LongSetting.() -> Unit): LongSetting {
    return LongSetting(default, lambda).also { settings.add(it) }
}
