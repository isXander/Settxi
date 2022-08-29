package dev.isxander.settxi.gui

import dev.isxander.settxi.Setting
import kotlin.reflect.KClass

abstract class GuiSettingRegistry<B, S> {
    val settingHandlers = hashMapOf<KClass<out Setting<*>>, B.(Setting<*>) -> S>()

    @Suppress("unchecked_cast")
    inline fun <reified T : Setting<*>> registerType(noinline factory: B.(setting: T) -> S) {
        settingHandlers[T::class] = factory as B.(Setting<*>) -> S
    }

    inline fun <reified T : Setting<*>> buildEntryForSetting(builder: B, setting: T): S {
        for ((k, v) in settingHandlers) {
            if (k.isInstance(setting)) {
                return v(builder, setting)
            }
        }

        throw NullPointerException("Config entry factory not found for ${setting::class.simpleName}")
    }
}