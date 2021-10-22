package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor

class BooleanSetting internal constructor(
    lambda: BooleanSetting.() -> Unit = {},
) : Setting<Boolean>() {
    override var default: Boolean = true
    override lateinit var name: String
    override lateinit var category: String
    override var subcategory: String? = null
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: Any
        get() = value
        set(new) { value = new as Boolean }

    override val defaultSerializedValue: Boolean = default

    init {
        this.apply(lambda)
    }
}

fun ConfigProcessor.boolean(lambda: BooleanSetting.() -> Unit): BooleanSetting {
    val setting = BooleanSetting()
    setting.apply(lambda)
    this.settings.add(setting)
    return setting
}

