package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.serialization.PrimitiveType

class OptionSetting internal constructor(
    default: OptionContainer.Option,
    lambda: OptionSetting.() -> Unit = {},
) : Setting<OptionContainer.Option>(default) {
    override lateinit var name: String
    override lateinit var category: String
    override lateinit var description: String
    override var shouldSave: Boolean = true

    val container by default::container
    val options by container::options

    override var value: OptionContainer.Option = default
        set(value) {
            check(value.container === container) { "Option must be in the same container" }
            field = value
        }


    override var serializedValue: PrimitiveType
        get() = PrimitiveType.of(value.id)
        set(new) { value = options.find { it.id == new.string }!! }

    override val defaultSerializedValue: PrimitiveType = PrimitiveType.of(default.id)

    init {
        this.apply(lambda)
    }
}

@JvmName("optionSetting")
fun ConfigProcessor.option(default: OptionContainer.Option, lambda: OptionSetting.() -> Unit): OptionSetting {
    return OptionSetting(default, lambda).also { settings.add(it) }
}

abstract class OptionContainer {
    val options = arrayListOf<Option>()
    protected fun option(name: String, description: String? = null): Option = Option(this, name, description)

    class Option internal constructor(val container: OptionContainer, val name: String, val description: String?) {
        init {
            container.options.add(this)
        }

        val id = name
            .lowercase()
            .replace(Regex("[^\\w]+"), "_")
            .trim { it == '_' || it.isWhitespace() }

        val ordinal = container.options.indexOf(this)
    }

}