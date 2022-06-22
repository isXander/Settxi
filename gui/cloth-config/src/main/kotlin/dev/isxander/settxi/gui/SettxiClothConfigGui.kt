package dev.isxander.settxi.gui

import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.settxi.serialization.SettxiConfig
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.util.function.Supplier

/**
 * Constructs a Cloth Config gui
 */
fun SettxiConfig.clothGui(title: Text, parent: Screen? = null): Screen =
    ConfigBuilder.create().apply {
        this.parentScreen = parent
        this.title = title

        for (setting in settings) {
            val category = getOrCreateCategory(Text.translatable(setting.category))
            category.addEntry(entry(setting))
        }

        setSavingRunnable { export() }
    }.build()


private fun ConfigBuilder.entry(setting: Setting<*>) =
    when (setting) {
        is BooleanSetting ->
            entryBuilder().startBooleanToggle(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(Text.translatable(setting.description))
                setSaveConsumer { setting.set(it) }
            }
        is DoubleSetting ->
            entryBuilder().startDoubleField(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(Text.translatable(setting.description))
                setSaveConsumer { setting.set(it) }
                setMin(setting.range.start)
                setMax(setting.range.endInclusive)
            }
        is FloatSetting ->
            entryBuilder().startFloatField(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(Text.translatable(setting.description))
                setSaveConsumer { setting.set(it) }
                setMin(setting.range.start)
                setMax(setting.range.endInclusive)
            }
        is LongSetting ->
            entryBuilder().startLongSlider(
                Text.translatable(setting.name),
                setting.get(),
                setting.range.first,
                setting.range.last
            ).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(Text.translatable(setting.description))
                setSaveConsumer { setting.set(it) }
            }
        is IntSetting ->
            entryBuilder().startIntSlider(
                Text.translatable(setting.name),
                setting.get(),
                setting.range.first,
                setting.range.last
            ).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(Text.translatable(setting.description))
                setSaveConsumer { setting.set(it) }
            }
        is StringSetting ->
            entryBuilder().startStrField(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(Text.translatable(setting.description))
                setSaveConsumer { setting.set(it) }
            }
        is OptionSetting ->
            entryBuilder().startStringDropdownMenu(
                Text.translatable(setting.name),
                setting.get().name
            ) { Text.translatable(it) }.apply {
                defaultValue = Supplier { setting.default.name }
                setTooltip(Text.translatable(setting.description))
                isSuggestionMode = false
                setSelections(setting.options.map { Text.translatable(it.name).string })
                setSaveConsumer {
                    setting.set(setting.options.first { option ->
                        Text.translatable(option.name).string == it
                    })
                }
            }
        is EnumSetting<*> ->
            setting.toEnumSelector(entryBuilder())
        is FileSetting ->
            throw UnsupportedOperationException("Cloth config does not support file settings")

        else -> throw IllegalArgumentException("Unknown setting type: ${setting.javaClass}")
    }.build()

@Suppress("unchecked_cast")
private fun <T : Enum<T>> EnumSetting<T>.toEnumSelector(entryBuilder: ConfigEntryBuilder): EnumSelectorBuilder<T> =
    entryBuilder.startEnumSelector(Text.translatable(name), enumClass, get()).apply {
        defaultValue = Supplier { default }
        setTooltip(Text.translatable(description))
        setSaveConsumer { set(it) }
        setEnumNameProvider { Text.translatable(nameProvider(it as T)) }
    }