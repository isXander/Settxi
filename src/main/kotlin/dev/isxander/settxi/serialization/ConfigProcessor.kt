package dev.isxander.settxi.serialization

import com.electronwill.nightconfig.core.Config
import dev.isxander.settxi.Setting
import dev.isxander.settxi.exception.SettxiException

interface ConfigProcessor {
    val settings: MutableList<Setting<*>>

    fun addSettingToConfig(setting: Setting<*>, data: Config): Config {
        try {
            data.add(setting.nameSerializedCategoryAndKey, setting.serializedValue)
        } catch (e: Exception) {
            throw SettxiException("Failed to add setting ${setting.name} to config.", e)
        }

        return data
    }

    fun setSettingFromConfig(data: Config, setting: Setting<*>) {
        try {
            setting.serializedValue = data.get(setting.nameSerializedCategoryAndKey)
        } catch (e: Exception) {
            throw SettxiException("Failed to parse setting ${setting.name} from config.", e)
        }

    }
}