package dev.isxander.settxi.gui.yacl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.BooleanSetting
import dev.isxander.settxi.impl.EnumSetting
import dev.isxander.settxi.impl.NumberSetting
import dev.isxander.yacl3.api.OptionFlag
import net.minecraft.text.Text

var <T> Setting<T>.yaclValueFormatter: ((T) -> Text)?
    get() = customProperties["yacl_valueFormatter"] as ((T) -> Text)?
    set(value) {
        if (value != null) customProperties["yacl_valueFormatter"] = value
        else customProperties.remove("yacl_valueFormatter")
    }

var Setting<*>.yaclCategoryTooltip: Text
    get() = customProperties["yacl_categoryTooltip"] as Text? ?: Text.empty()
    set(value) { customProperties["yacl_categoryTooltip"] = value }

var Setting<*>.yaclCategoryTooltipStr: String
    get() = yaclCategoryTooltip.string
    set(value) { yaclCategoryTooltip = Text.of(value) }

var Setting<*>.yaclGroup: YaclGroup?
    get() = customProperties["yacl_group"] as YaclGroup?
    set(value) {
        if (value != null) customProperties["yacl_group"] = value
        else customProperties.remove("yacl_group")
    }

var Setting<*>.yaclFlags: Set<OptionFlag>
    get() = customProperties["yacl_flags"] as Set<OptionFlag>? ?: emptySet()
    set(value) { customProperties["yacl_flags"] = value }

var Setting<*>.yaclInstant: Boolean
    get() = customProperties["yacl_instant"] as Boolean? == true
    set(value) { customProperties["yacl_instant"] = value }

@Deprecated("Replaced with YACL's flags")
var Setting<*>.yaclRequireRestart: Boolean
    get() = yaclFlags.contains(OptionFlag.GAME_RESTART)
    set(value) {
        yaclFlags = if (value)
            yaclFlags + OptionFlag.GAME_RESTART
        else
            yaclFlags - OptionFlag.GAME_RESTART
    }

var Setting<*>.yaclAvailable: Boolean
    get() = customProperties["yacl_available"] as Boolean? ?: true
    set(value) { customProperties["yacl_available"] = value }

var BooleanSetting.yaclUseTickBox: Boolean
    get() = customProperties["yacl_useTickBox"] as Boolean? == true
    set(value) { customProperties["yacl_useTickBox"] = value }

var BooleanSetting.yaclColouredText: Boolean
    get() = customProperties["yacl_colouredText"] as Boolean? == true
    set(value) {
        if (yaclUseTickBox) error("`yaclColouredText` does not support tick box!")
        customProperties["yacl_colouredText"] = value
    }

var <T : Number> NumberSetting<T>.yaclSliderInterval: T?
    get() = customProperties["yacl_sliderInterval"] as T?
    set(value) {
        if (value != null) customProperties["yacl_sliderInterval"] = value
        else customProperties.remove("yacl_sliderInterval")
    }

var YACLButtonSetting.yaclButtonText: Text?
    get() = customProperties["yacl_buttonText"] as Text?
    set(value) {
        if (value != null) customProperties["yacl_buttonText"] = value
        else customProperties.remove("yacl_buttonText")
    }

inline var <reified T : Enum<T>> EnumSetting<T>.yaclAvailableConstants: Set<T>?
    get() = customProperties["yacl_enumAvailableConsts"] as Set<T>?
    set(value) { customProperties["yacl_enumAvailableConsts"] = value!! }

data class YaclGroup(val name: Text, val tooltip: Text = Text.empty(), val collapsed: Boolean = false)
@Deprecated(message = "Name changed!", ReplaceWith("YaclGroup"))
typealias Group = YaclGroup
