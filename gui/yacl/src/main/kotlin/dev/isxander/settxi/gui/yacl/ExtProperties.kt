package dev.isxander.settxi.gui.yacl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.BooleanSetting
import dev.isxander.settxi.impl.NumberSetting
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

var Setting<*>.yaclGroup: Group?
    get() = customProperties["yacl_group"] as Group?
    set(value) {
        if (value != null) customProperties["yacl_group"] = value
        else customProperties.remove("yacl_group")
    }

var Setting<*>.yaclRequireRestart: Boolean
    get() = customProperties["yacl_requireRestart"] as Boolean? == true
    set(value) { customProperties["yacl_requireRestart"] = value }

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

data class Group(val name: Text, val tooltip: Text = Text.empty(), val collapsed: Boolean = false)
