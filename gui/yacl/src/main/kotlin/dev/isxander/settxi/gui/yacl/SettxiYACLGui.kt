package dev.isxander.settxi.gui.yacl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettxiConfig
import dev.isxander.settxi.gui.GuiSettingRegistry
import dev.isxander.settxi.impl.*
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.CyclingListControllerBuilder
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.LongSliderControllerBuilder
import dev.isxander.yacl3.api.controller.StringControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.gui.controllers.*
import dev.isxander.yacl3.gui.controllers.cycling.EnumController
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController
import dev.isxander.yacl3.gui.controllers.slider.LongSliderController
import dev.isxander.yacl3.gui.controllers.string.StringController
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

fun SettxiConfig.yetAnotherConfigLib(title: Text, parent: Screen?, block: YetAnotherConfigLib.Builder.() -> Unit = {}): Screen =
    YetAnotherConfigLib.createBuilder().apply {
        title(title)
        save { export() }

        val builders = mutableMapOf<String, Pair<ConfigCategory.Builder, MutableMap<YaclGroup, OptionGroup.Builder>>>()
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
                        .description(OptionDescription.of(group.tooltip))
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
    }
        .apply(block)
        .build()
        .generateScreen(parent)

object SettxiYACLGui : GuiSettingRegistry<Unit, Option<*>>() {
    init {
        registerType<BooleanSetting> { setting ->
            Option.createBuilder<Boolean>().apply {
                applyGenericSetting(setting)
                controller { opt ->
                    if (setting.yaclUseTickBox)
                        TickBoxControllerBuilder.create(opt)
                    else
                        BooleanControllerBuilder.create(opt).apply {
                            setting.yaclValueFormatter?.let { valueFormatter(it) } ?: onOffFormatter()
                            coloured(setting.yaclColouredText)
                        }
                }
            }.build()
        }
        registerType<IntSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder<Int>().apply {
                applyGenericSetting(setting)
                controller { opt ->
                    IntegerSliderControllerBuilder.create(opt).apply {
                        range(setting.range!!.first, setting.range!!.last)
                        step(setting.yaclSliderInterval!!)
                        setting.yaclValueFormatter?.let { valueFormatter(it) }
                    }
                }
            }.build()
        }
        registerType<FloatSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder<Float>().apply {
                applyGenericSetting(setting)
                controller { opt ->
                    FloatSliderControllerBuilder.create(opt).apply {
                        range(setting.range!!.start, setting.range!!.endInclusive)
                        step(setting.yaclSliderInterval!!)
                        setting.yaclValueFormatter?.let { valueFormatter(it) }
                    }
                }
            }.build()
        }
        registerType<DoubleSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder<Double>().apply {
                applyGenericSetting(setting)
                controller { opt ->
                    DoubleSliderControllerBuilder.create(opt).apply {
                        range(setting.range!!.start, setting.range!!.endInclusive)
                        step(setting.yaclSliderInterval!!)
                        setting.yaclValueFormatter?.let { valueFormatter(it) }
                    }
                }
            }.build()
        }
        registerType<LongSetting> { setting ->
            setting.range ?: error("YetAnotherConfigLib requires `range` to be defined.")
            setting.yaclSliderInterval ?: error("YetAnotherConfigLib requires `yoclSliderInterval` to be defined")

            Option.createBuilder<Long>().apply {
                applyGenericSetting(setting)
                controller { opt ->
                    LongSliderControllerBuilder.create(opt).apply {
                        range(setting.range!!.first, setting.range!!.last)
                        step(setting.yaclSliderInterval!!)
                        setting.yaclValueFormatter?.let { valueFormatter(it) }
                    }
                }
            }.build()
        }
        registerType<EnumSetting<*>> { setting ->
            setting.toOption()
        }
        registerType<StringSetting> { setting ->
            Option.createBuilder<String>().apply {
                applyGenericSetting(setting)
                controller(StringControllerBuilder::create)
            }.build()
        }
        registerType<YACLButtonSetting> { setting ->
            ButtonOption.createBuilder().apply {
                name(Text.translatable(setting.name))
                setting.description?.let { description(OptionDescription.of(Text.translatable(it))) }
                action { screen, _ -> setting.get(false).invoke(screen) }
            }.build()
        }
        registerType<YACLLabelSetting> { setting ->
            LabelOption.create(setting.get())
        }
    }

    fun <T : Any> Option.Builder<T>.applyGenericSetting(setting: Setting<T>) {
        name(Text.translatable(setting.name))
        setting.description?.let { description(OptionDescription.of(Text.translatable(it))) }
        binding(
            setting.default,
            { setting.get(false) },
            { setting.set(it, false) }
        )
        flags(setting.yaclFlags)
        instant(setting.yaclInstant)
        available(setting.yaclAvailable)
    }

    private inline fun <reified T : Enum<T>> EnumSetting<T>.toOption(): Option<T> {
        return Option.createBuilder<T>().apply {
            applyGenericSetting(this@toOption)
            controller { opt ->
                if (yaclAvailableConstants != null) {
                    CyclingListControllerBuilder.create(opt).apply {
                        values(yaclAvailableConstants ?: enumClass.enumConstants.toList())
                        yaclValueFormatter?.let { valueFormatter(it) }
                    }
                } else {
                    EnumControllerBuilder.create(opt).apply {
                        enumClass(enumClass)
                        yaclValueFormatter?.let { valueFormatter(it) }
                    }
                }
            }
        }.build()
    }
}
