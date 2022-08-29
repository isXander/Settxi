package dev.isxander.settxi.gui.spruce

import dev.isxander.settxi.SettxiConfig
import dev.isxander.settxi.gui.GuiSettingRegistry
import dev.isxander.settxi.impl.*
import dev.lambdaurora.spruceui.background.Background
import dev.lambdaurora.spruceui.background.EmptyBackground
import dev.lambdaurora.spruceui.option.*
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import kotlin.io.path.exists

/**
 * Constructs a SpruceUI config screen
 *
 * @param title GUI Title, displayed at the top of the [Screen]
 * @param parent [Screen] to open once the Cloth Config screen is closed
 * @param background Background to display behind the settings, default none.
 * @param factory Allows adding more custom SpruceUI entries and adding custom drawables when paired with Fabric API
 */
fun SettxiConfig.spruceUI(
    title: Text,
    parent: Screen? = null,
    background: Background = EmptyBackground.EMPTY_BACKGROUND,
    factory: SettxiSpruceScreen.(SpruceOptionListWidget) -> Unit = {}
): Screen = SettxiSpruceScreen(title, parent, this, background, factory)

object SettxiSpruceUIGui : GuiSettingRegistry<Unit, SpruceOption>() {
    init {
        val decimalFormatter = DecimalFormat("0.00")

        registerType<BooleanSetting> { setting ->
            if (setting.spruceUIUseCheckbox)
                SpruceCheckboxBooleanOption(
                    setting.name,
                    { setting.get(false) },
                    { setting.set(it, false) },
                    setting.description?.let { Text.translatable(it) },
                    setting.spruceUIColoured
                )
            else
                SpruceBooleanOption(
                    setting.name,
                    { setting.get(false) },
                    { setting.set(it, false) },
                    setting.description?.let { Text.translatable(it) },
                    setting.spruceUIColoured
                )
        }
        registerType<DoubleSetting> { setting ->
            if (setting.range == null)
                SpruceDoubleInputOption(
                    setting.name,
                    { setting.get(false) },
                    { setting.set(it, false) },
                    setting.description?.let { Text.translatable(it) }
                )
            else
                SpruceDoubleOption(
                    setting.name,
                    setting.range!!.start,
                    setting.range!!.endInclusive,
                    setting.spruceUISliderStep?.toFloat() ?: 0.1f,
                    { setting.get(false) },
                    { setting.set(it, false) },
                    setting.spruceUITextGetter?.let {
                        { option: SpruceDoubleOption ->
                            option.getDisplayText(it(option.get()))
                        }
                    } ?: { it.getDisplayText(Text.literal(decimalFormatter.format(it.get()))) },
                    setting.description?.let { Text.translatable(it) }
                )
        }
        registerType<FloatSetting> { setting ->
            if (setting.range == null)
                SpruceFloatInputOption(
                    setting.name,
                    { setting.get(false) },
                    { setting.set(it, false) },
                    setting.description?.let { Text.translatable(it) }
                )
            else
                SpruceDoubleOption(
                    setting.name,
                    setting.range!!.start.toDouble(),
                    setting.range!!.endInclusive.toDouble(),
                    setting.spruceUISliderStep ?: 0.1f,
                    { setting.get(false).toDouble() },
                    { setting.set(it.toFloat(), false) },
                    setting.spruceUITextGetter?.let {
                        { option: SpruceDoubleOption ->
                            option.getDisplayText(it(option.get().toFloat()))
                        }
                    } ?: { it.getDisplayText(Text.literal(decimalFormatter.format(it.get()))) },
                    setting.description?.let { Text.translatable(it) }
                )
        }
        registerType<IntSetting> { setting ->
            if (setting.range == null)
                SpruceIntegerInputOption(
                    setting.name,
                    { setting.get(false) },
                    { setting.set(it, false) },
                    setting.description?.let { Text.translatable(it) }
                )
            else
                SpruceDoubleOption(
                    setting.name,
                    setting.range!!.first.toDouble(),
                    setting.range!!.last.toDouble(),
                    setting.spruceUISliderStep?.toFloat() ?: 1f,
                    { setting.get(false).toDouble() },
                    { setting.set(it.toInt(), false) },
                    setting.spruceUITextGetter?.let {
                        { option: SpruceDoubleOption ->
                            option.getDisplayText(it(option.get().toInt()))
                        }
                    } ?: { it.getDisplayText(Text.literal("${it.get().toInt()}")) },
                    setting.description?.let { Text.translatable(it) }
                )
        }
        registerType<EnumSetting<*>> { setting ->
            setting.toCyclingOption()
        }
        registerType<StringSetting> { setting ->
            SpruceStringOption(
                setting.name,
                { setting.get(false) },
                { setting.set(it, false) },
                setting.spruceUITextPredicate,
                setting.description?.let { Text.translatable(it) }
            )
        }
        registerType<FileSetting> { setting ->
            SpruceStringOption(
                setting.name,
                { setting.get(false).absolutePath },
                { setting.set(File(it), false) },
                { isValidPath(it) },
                setting.description?.let { Text.translatable(it) }
            )
        }
        registerType<PathSetting> { setting ->
            SpruceStringOption(
                setting.name,
                { setting.get(false).toAbsolutePath().toString() },
                { setting.set(Path.of(it), false) },
                { isValidPath(it) },
                setting.description?.let { Text.translatable(it) }
            )
        }
    }

    private fun <T : Enum<T>> EnumSetting<T>.toCyclingOption(): SpruceCyclingOption =
        SpruceCyclingOption(
            name,
            {
                var ordinal = get(false).ordinal + it

                if (ordinal > values.lastIndex)
                    ordinal -= values.size
                else if (ordinal < 0)
                    ordinal += values.lastIndex

                set(values[ordinal], false)
            },
            { it.getDisplayText(Text.translatable(nameProvider(get(false)))) },
            description?.let { Text.translatable(it) }
        )

    private fun isValidPath(path: String): Boolean {
        try {
            Paths.get(path)
        } catch (e: InvalidPathException) {
            return false
        }
        return true
    }
}