package dev.isxander.settxi.gui.spruce

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.SerializedType

class SpruceButtonSetting internal constructor(
    action: () -> Unit,
    lambda: SpruceButtonSetting.() -> Unit = {},
) : Setting<() -> Unit>(action) {
    override lateinit var name: String
    override lateinit var category: String
    override var description: String? = null
    override val shouldSave: Boolean = true

    override var serializedValue: SerializedType
        get() = ObjectType()
        set(new) {}

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> ObjectType() }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [SpruceButtonSetting]
 */
@JvmName("spruceButtonSetting")
fun ConfigProcessor.spruceButton(action: () -> Unit, lambda: SpruceButtonSetting.() -> Unit): SpruceButtonSetting {
    return SpruceButtonSetting(action, lambda).also { settings.add(it) }
}