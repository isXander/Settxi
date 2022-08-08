package dev.isxander.settxi.gui

import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.*
import dev.isxander.settxi.serialization.SettxiConfig
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.io.File
import java.nio.file.Paths
import java.util.Optional
import java.util.function.Function
import java.util.function.Supplier
import kotlin.io.path.notExists
import kotlin.reflect.KClass

/**
 * Constructs a Cloth Config gui
 */
fun SettxiConfig.clothGui(title: Text, parent: Screen? = null): Screen =
    ConfigBuilder.create().apply {
        this.parentScreen = parent
        this.title = title

        for (setting in settings) {
            if (setting.hidden) continue

            val category = getOrCreateCategory(Text.translatable(setting.category))
            category.addEntry(SettxiClothConfigGui.buildEntryForSetting(this, setting))
        }

        setSavingRunnable { export() }
    }.build()

object SettxiClothConfigGui {
    val settingHandlers = hashMapOf<KClass<out Setting<*>>, ConfigBuilder.(Setting<*>) -> TooltipListEntry<out Any>>()

    @Suppress("unchecked_cast")
    inline fun <reified T : Setting<*>> registerType(noinline factory: ConfigBuilder.(setting: T) -> TooltipListEntry<out Any>) {
        settingHandlers[T::class] = factory as ConfigBuilder.(Setting<*>) -> TooltipListEntry<out Any>
    }

    inline fun <reified T : Setting<*>> buildEntryForSetting(builder: ConfigBuilder, setting: T): TooltipListEntry<out Any> {
        for ((k, v) in settingHandlers) {
            if (k.isInstance(setting)) {
                return v(builder, setting)
            }
        }

        throw NullPointerException("Config entry factory not found for ${setting::class.simpleName}")
    }

    init {
        registerType<BooleanSetting> { setting ->
            entryBuilder().startBooleanToggle(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it) }
            }.build()
        }
        registerType<DoubleSetting> { setting ->
            entryBuilder().startDoubleField(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let { Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it) }
                if (setting.range != null) {
                    setMin(setting.range!!.start)
                    setMax(setting.range!!.endInclusive)
                }
            }.build()
        }
        registerType<FloatSetting> { setting ->
            entryBuilder().startFloatField(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it) }
                if (setting.range != null) {
                    setMin(setting.range!!.start)
                    setMax(setting.range!!.endInclusive)
                }
            }.build()
        }
        registerType<LongSetting> { setting ->
            if (setting.range != null) {
                entryBuilder().startLongSlider(
                    Text.translatable(setting.name),
                    setting.get(),
                    setting.range!!.first,
                    setting.range!!.last
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it) }
                }
            } else {
                entryBuilder().startLongField(
                    Text.translatable(setting.name),
                    setting.get(),
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it) }
                }
            }.build()
        }
        registerType<IntSetting> { setting ->
            if (setting.range != null) {
                entryBuilder().startIntSlider(
                    Text.translatable(setting.name),
                    setting.get(),
                    setting.range!!.first,
                    setting.range!!.last
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it) }
                }
            } else {
                entryBuilder().startIntField(
                    Text.translatable(setting.name),
                    setting.get(),
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it) }
                }
            }.build()
        }
        registerType<StringSetting> { setting ->
            entryBuilder().startStrField(Text.translatable(setting.name), setting.get()).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it) }
            }.build()
        }
        registerType<OptionSetting> { setting ->
            entryBuilder().startStringDropdownMenu(
                Text.translatable(setting.name),
                setting.get().name
            ) { Text.translatable(it) }.apply {
                defaultValue = Supplier { setting.default.name }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                isSuggestionMode = false
                setSelections(setting.options.map { Text.translatable(it.name).string })
                setSaveConsumer {
                    setting.set(setting.options.first { option ->
                        Text.translatable(option.name).string == it
                    })
                }
            }.build()
        }
        registerType<EnumSetting<*>> { setting ->
            setting.toEnumSelector(entryBuilder()).build()
        }
        registerType<FileSetting> { setting ->
            entryBuilder().startStrField(Text.translatable(setting.name), setting.get().absolutePath).apply {
                defaultValue = Supplier { setting.default.absolutePath }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(File(it).absoluteFile) }
                setErrorSupplier {
                    if (!File(it).exists())
                        Optional.of(Text.translatable("settxi.cloth.file_not_exists"))
                    else
                        Optional.empty()
                }
            }.build()
        }
        registerType<PathSetting> { setting ->
            entryBuilder().startStrField(Text.translatable(setting.name), setting.get().toAbsolutePath().toString()).apply {
                defaultValue = Supplier { setting.default.toAbsolutePath().toString() }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(Paths.get(it)) }
                setErrorSupplier {
                    if (Paths.get(it).notExists())
                        Optional.of(Text.translatable("settxi.cloth.file_not_exists"))
                    else
                        Optional.empty()
                }
            }.build()
        }
    }
}

@Suppress("unchecked_cast")
private fun <T : Enum<T>> EnumSetting<T>.toEnumSelector(entryBuilder: ConfigEntryBuilder): EnumSelectorBuilder<T> =
    entryBuilder.startEnumSelector(Text.translatable(name), enumClass, get()).apply {
        defaultValue = Supplier { default }
        setTooltip(description?.let{ Text.translatable(it) } ?: Text.empty())
        setSaveConsumer { set(it) }
        setEnumNameProvider { Text.translatable(nameProvider(it as T)) }
    }