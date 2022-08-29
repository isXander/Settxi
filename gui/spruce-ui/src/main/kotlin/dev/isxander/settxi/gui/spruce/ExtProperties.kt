package dev.isxander.settxi.gui.spruce

import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.BooleanSetting
import dev.isxander.settxi.impl.NumberSetting
import dev.isxander.settxi.impl.StringSetting
import net.minecraft.text.Text

/**
 * Colour the True or False text green and red respectively.
 */
var BooleanSetting.spruceUIColoured: Boolean
    get() = customProperties["spruceUI_coloured"] as Boolean? == true
    set(value) { customProperties["spruceUI_coloured"] = value }

/**
 * Use a checkbox rather than a button.
 */
var BooleanSetting.spruceUIUseCheckbox: Boolean
    get() = customProperties["spruceUI_useCheckbox"] as Boolean? == true
    set(value) { customProperties["spruceUI_useCheckbox"] = value }

/**
 * What increment the slider uses when dragging.
 */
var <N : Number> NumberSetting<N>.spruceUISliderStep: N?
    get() = customProperties["spruceUI_sliderStep"] as N?
    set(value) {
        if (value != null) customProperties["spruceUI_sliderStep"] = value
        else customProperties.remove("spruceUI_sliderStep")
    }

/**
 * Supplies [Text] for the slider to tell the user what the value is.
 */
var <N : Number> NumberSetting<N>.spruceUITextGetter: ((N) -> Text)?
    get() = customProperties["spruceUI_textGetter"] as ((N) -> Text)?
    set(value) {
        if (value != null) customProperties["spruceUI_textGetter"] = value
        else customProperties.remove("spruceUI_textGetter")
    }

/**
 * Displays the setting as half the width of a normal button.
 * Two of these in a row will be inlined into one row.
 */
var Setting<*>.spruceUIHalfWidth: Boolean
    get() = customProperties["spruceUI_halfWidth"] as Boolean? == true
    set(value) { customProperties["spruceUI_halfWidth"] = value }

/**
 * Predicate for the text box, when returning false, the key press is ignored.
 */
var StringSetting.spruceUITextPredicate: ((String) -> Boolean)?
    get() = customProperties["spruceUI_textPredicate"] as ((String) -> Boolean)?
    set(value) {
        if (value != null) customProperties["spruceUI_textPredicate"] = value
        else customProperties.remove("spruceUI_textPredicate")
    }
