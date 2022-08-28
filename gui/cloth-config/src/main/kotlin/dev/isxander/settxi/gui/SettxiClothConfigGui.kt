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
import java.util.function.Supplier
import kotlin.io.path.notExists
import kotlin.reflect.KClass

/**
 * Constructs a Cloth Config gui
 */
fun SettxiConfig.clothGui(title: Text, parent: Screen? = null, builder: ConfigBuilder.() -> Unit = {}): Screen =
    ConfigBuilder.create().apply {
        this.parentScreen = parent
        this.title = title

        for (setting in settings) {
            if (setting.hidden) continue

            val category = getOrCreateCategory(Text.translatable(setting.category))
            category.addEntry(SettxiClothConfigGui.buildEntryForSetting(this, setting))
        }

        setSavingRunnable { export() }

        builder()
    }.build()

var Setting<*>.clothRequireRestart: Boolean
    get() = customProperties["cloth_requireRestart"] as Boolean? == true
    set(value) { customProperties["cloth_requireRestart"] = value }

var BooleanSetting.clothYesNoText: ((Boolean) -> Text)?
    get() = customProperties["cloth_yesNoText"] as ((Boolean) -> Text)?
    set(value) {
        if (value != null)
            customProperties["cloth_yesNoText"] = value
        else
            customProperties.remove("cloth_yesNoText")
    }

var IntSetting.clothTextGetter: ((Int) -> Text)?
    get() = customProperties["cloth_textGetter"] as ((Int) -> Text)?
    set(value) {
        if (value != null) {
            if (range == null) throw IllegalStateException("`clothTextGetter` only works when `range` is defined")
            customProperties["cloth_textGetter"] = value
        } else {
            customProperties.remove("cloth_textGetter")
        }
    }

var LongSetting.clothTextGetter: ((Long) -> Text)?
    get() = customProperties["cloth_textGetter"] as ((Long) -> Text)?
    set(value) {
        if (value != null) {
            if (range == null) throw IllegalStateException("`clothTextGetter` only works when `range` is defined")
            customProperties["cloth_textGetter"] = value
        } else {
            customProperties.remove("cloth_textGetter")
        }
    }

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
            entryBuilder().startBooleanToggle(Text.translatable(setting.name), setting.get(false)).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it, false) }
                requireRestart(setting.clothRequireRestart)
                setting.clothYesNoText?.let { setYesNoTextSupplier(it) }
            }.build()
        }
        registerType<DoubleSetting> { setting ->
            entryBuilder().startDoubleField(Text.translatable(setting.name), setting.get(false)).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let { Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it, false) }
                requireRestart(setting.clothRequireRestart)
                if (setting.range != null) {
                    setMin(setting.range!!.start)
                    setMax(setting.range!!.endInclusive)
                }
            }.build()
        }
        registerType<FloatSetting> { setting ->
            entryBuilder().startFloatField(Text.translatable(setting.name), setting.get(false)).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it, false) }
                requireRestart(setting.clothRequireRestart)
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
                    setting.get(false),
                    setting.range!!.first,
                    setting.range!!.last
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it, false) }
                    requireRestart(setting.clothRequireRestart)
                }.build().apply {
                    setting.clothTextGetter?.let { setTextGetter(it) }
                }
            } else {
                entryBuilder().startLongField(
                    Text.translatable(setting.name),
                    setting.get(false),
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it, false) }
                    requireRestart(setting.clothRequireRestart)
                }.build()
            }
        }
        registerType<IntSetting> { setting ->
            if (setting.range != null) {
                entryBuilder().startIntSlider(
                    Text.translatable(setting.name),
                    setting.get(false),
                    setting.range!!.first,
                    setting.range!!.last
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it, false) }
                    requireRestart(setting.clothRequireRestart)
                }.build().apply {
                    setting.clothTextGetter?.let { setTextGetter(it) }
                }
            } else {
                entryBuilder().startIntField(
                    Text.translatable(setting.name),
                    setting.get(false),
                ).apply {
                    defaultValue = Supplier { setting.default }
                    setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                    setSaveConsumer { setting.set(it, false) }
                    requireRestart(setting.clothRequireRestart)
                }.build()
            }
        }
        registerType<StringSetting> { setting ->
            entryBuilder().startStrField(Text.translatable(setting.name), setting.get(false)).apply {
                defaultValue = Supplier { setting.default }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(it, false) }
                requireRestart(setting.clothRequireRestart)
            }.build()
        }
        registerType<EnumSetting<*>> { setting ->
            setting.toEnumSelector(entryBuilder()).apply {
                requireRestart(setting.clothRequireRestart)
            }.build()
        }
        registerType<FileSetting> { setting ->
            entryBuilder().startStrField(Text.translatable(setting.name), setting.get(false).absolutePath).apply {
                defaultValue = Supplier { setting.default.absolutePath }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(File(it).absoluteFile, false) }
                requireRestart(setting.clothRequireRestart)
                setErrorSupplier {
                    if (!File(it).exists())
                        Optional.of(Text.translatable("settxi.cloth.file_not_exists"))
                    else
                        Optional.empty()
                }
            }.build()
        }
        registerType<PathSetting> { setting ->
            entryBuilder().startStrField(Text.translatable(setting.name), setting.get(false).toAbsolutePath().toString()).apply {
                defaultValue = Supplier { setting.default.toAbsolutePath().toString() }
                setTooltip(setting.description?.let{ Text.translatable(it) } ?: Text.empty())
                setSaveConsumer { setting.set(Paths.get(it), false) }
                requireRestart(setting.clothRequireRestart)
                setErrorSupplier {
                    if (Paths.get(it).notExists())
                        Optional.of(Text.translatable("settxi.cloth.file_not_exists"))
                    else
                        Optional.empty()
                }
            }.build()
        }
    }

    @Suppress("unchecked_cast")
    private fun <T : Enum<T>> EnumSetting<T>.toEnumSelector(entryBuilder: ConfigEntryBuilder): EnumSelectorBuilder<T> =
        entryBuilder.startEnumSelector(Text.translatable(name), enumClass, get(false)).apply {
            defaultValue = Supplier { default }
            setTooltip(description?.let{ Text.translatable(it) } ?: Text.empty())
            setSaveConsumer { set(it, false) }
            setEnumNameProvider { Text.translatable(nameProvider(it as T)) }
        }
}