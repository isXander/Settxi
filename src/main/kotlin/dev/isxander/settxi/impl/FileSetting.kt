package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.serialization.ConfigProcessor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

class FileSetting internal constructor(
    default: File,
    lambda: FileSetting.() -> Unit = {},
) : Setting<File>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true

    override var serializedValue: JsonElement
        get() = JsonPrimitive(value.path)
        set(new) { value = File(new.jsonPrimitive.content) }

    override val defaultSerializedValue: JsonElement = JsonPrimitive(default.path)

    init {
        this.apply(lambda)
    }
}

@JvmName("fileSetting")
fun ConfigProcessor.file(default: File, lambda: FileSetting.() -> Unit): FileSetting {
    return FileSetting(default, lambda).also { settings.add(it) }
}
