package dev.isxander.settxi.gui.yocl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettxiConfig
import dev.isxander.settxi.gui.GuiSettingRegistry
import dev.isxander.settxi.impl.*
import dev.isxander.yacl.api.ButtonOption
import dev.isxander.yacl.api.ConfigCategory
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.api.YetAnotherConfigLib
import dev.isxander.yacl.gui.YACLScreen
import dev.isxander.yacl.gui.controllers.*
import dev.isxander.yacl.gui.controllers.slider.DoubleSliderController
import dev.isxander.yacl.gui.controllers.slider.FloatSliderController
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController
import dev.isxander.yacl.gui.controllers.slider.LongSliderController
import dev.isxander.yacl.gui.controllers.string.StringController
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

fun SettxiConfig.yetAnotherConfigLib(title: Text, parent: Screen?, initConsumer: (YACLScreen) -> Unit = {}): Screen =
    YetAnotherConfigLib.createBuilder().apply {
        title(title)
        save { export() }
        screenInit(initConsumer)

        val builders = mutableMapOf<String, Pair<ConfigCategory.Builder, MutableMap<Group, OptionGroup.Builder>>>()
        for (setting in settings) {
            val builder = builders.getOrPut(setting.category) {
                ConfigCategory.createBuilder()
                    .name(Text.translatable(setting.category))
                    .tooltip(setting.yaclCategoryTooltip) to mutableMapOf()
            }
            val option = SettxiYACLGui.buildEntryForSetting(Unit, setting)

            val group = setting.yaclGroup
            if (group != null) {
                builder.second.getOrPut(group) {
                    OptionGroup.createBuilder()
                        .name(group.name)
                        .tooltip(group.tooltip)
                        .collapsed(group.collapsed)
                }.option(option)
            } else {
                builder.first.option(option)
            }
        }

        categories(builders.map { builder ->
            val groups = builder.value.second.map { group -> group.value.build() }
            if (groups.isNotEmpty()) builder.value.first.groups(groups)
            builder.value.first.build()
        })
    }.build().generateScreen(parent)

object SettxiYACLGui : GuiSettingRegistry<Unit, Option<*>>() {
    init {
        registerType<BooleanSetting> { setting ->
            Option.createBuilder(Boolean::class.java).apply {
                applyGenericSetting(setting)
                controller {
                    if (setting.yaclUseTickBox)
                        TickBoxController(it)
                    else
                        BooleanController(
                            it,
                            setting.yaclValueFormatter
                                ?: { value -> BooleanController.ON_OFF_FORMATTER.apply(value) },
                            setting.yaclColouredText
                        )
                }
            }.build()
        }
        registerType<IntSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder(Int::class.java).apply {
                applyGenericSetting(setting)
                controller { IntegerSliderController(it, setting.range!!.first, setting.range!!.last, setting.yaclSliderInterval!!) }
            }.build()
        }
        registerType<FloatSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder(Float::class.java).apply {
                applyGenericSetting(setting)
                controller { FloatSliderController(it, setting.range!!.start, setting.range!!.endInclusive, setting.yaclSliderInterval!!) }
            }.build()
        }
        registerType<DoubleSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder(Double::class.java).apply {
                applyGenericSetting(setting)
                controller { DoubleSliderController(it, setting.range!!.start, setting.range!!.endInclusive, setting.yaclSliderInterval!!) }
            }.build()
        }
        registerType<LongSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder(Long::class.java).apply {
                applyGenericSetting(setting)
                controller { LongSliderController(it, setting.range!!.first, setting.range!!.last, setting.yaclSliderInterval!!) }
            }.build()
        }
        registerType<EnumSetting<*>> { setting ->
            setting.toOption()
        }
        registerType<StringSetting> { setting ->
            Option.createBuilder(String::class.java).apply {
                applyGenericSetting(setting)
                controller(::StringController)
            }.build()
        }
        registerType<YACLButtonSetting> { setting ->
            ButtonOption.createBuilder().apply {
                name(Text.translatable(setting.name))
                setting.description?.let { tooltip(Text.translatable(it)) }
                action { setting.get(false).invoke(it) }
                if (setting.yaclButtonText == null)
                    controller(::ActionController)
                else
                    controller { opt -> ActionController(opt, setting.yaclButtonText) }
            }.build()
        }
        registerType<YACLLabelSetting> { setting ->
            Option.createBuilder(Text::class.java).apply {
                applyGenericSetting(setting)
                controller(::LabelController)
            }.build()
        }
    }

    private fun <T : Any> Option.Builder<T>.applyGenericSetting(setting: Setting<T>) {
        name(Text.translatable(setting.name))
        setting.description?.let { tooltip(Text.translatable(it)) }
        binding(
            setting.default,
            { setting.get(false) },
            { setting.set(it, false) }
        )
        requiresRestart(setting.yaclRequireRestart)
    }

    private fun <T : Enum<T>> EnumSetting<T>.toOption(): Option<T> {
        return Option.createBuilder(enumClass).apply {
            applyGenericSetting(this@toOption)
            controller {
                if (yaclValueFormatter != null)
                    EnumController(it, yaclValueFormatter)
                else
                    EnumController(it)
            }
        }.build()
    }
}
