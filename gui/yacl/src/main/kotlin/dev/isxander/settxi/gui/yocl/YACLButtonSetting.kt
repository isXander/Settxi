package dev.isxander.settxi.gui.yocl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.SerializedType
import dev.isxander.yacl.gui.YACLScreen

class YACLButtonSetting internal constructor(
    action: (YACLScreen) -> Unit,
    lambda: YACLButtonSetting.() -> Unit = {},
) : Setting<(YACLScreen) -> Unit>(action) {
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
 * Constructs and registers a [YACLButtonSetting]
 */
@JvmName("yaclButtonSetting")
fun ConfigProcessor.yaclButton(action: (YACLScreen) -> Unit, lambda: YACLButtonSetting.() -> Unit): YACLButtonSetting {
    return YACLButtonSetting(action, lambda).also { settings.add(it) }
}