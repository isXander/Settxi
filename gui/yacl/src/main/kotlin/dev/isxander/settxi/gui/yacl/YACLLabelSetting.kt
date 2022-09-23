package dev.isxander.settxi.gui.yacl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.ObjectType
import dev.isxander.settxi.serialization.SerializedType
import net.minecraft.text.Text

class YACLLabelSetting internal constructor(
    label: Text,
    lambda: YACLLabelSetting.() -> Unit = {},
) : Setting<Text>(label) {
    override var name: String = ""
    override lateinit var category: String
    override var description: String? = null
    override val shouldSave: Boolean = false

    override var serializedValue: SerializedType
        get() = ObjectType()
        set(new) {}

    override var defaultSerializedValue: (root: ObjectType, category: ObjectType?) -> SerializedType = { _, _ -> ObjectType() }

    init {
        this.apply(lambda)
    }
}

/**
 * Constructs and registers a [YACLLabelSetting]
 */
@JvmName("yaclLabelSetting")
fun ConfigProcessor.yaclLabel(label: Text, lambda: YACLLabelSetting.() -> Unit): YACLLabelSetting {
    return YACLLabelSetting(label, lambda).also { settings.add(it) }
}