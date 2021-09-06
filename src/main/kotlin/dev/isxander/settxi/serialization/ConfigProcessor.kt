package dev.isxander.settxi.serialization

import com.electronwill.nightconfig.core.Config
import dev.isxander.settxi.Setting

interface ConfigProcessor {
    val settings: MutableList<Setting<*>>
    var conf: Config

    fun addSettingToConfig(setting: Setting<*>, data: Config): Config {
        var key = "${setting.category}."
        if (setting.subcategory != "") key += "${setting.subcategory}."
        key += setting.nameSerializedKey

        data.add(setting.nameSerializedCategoryAndKey, setting.serializedValue)
        return data
    }

    fun setSettingFromConfig(data: Config, setting: Setting<*>) {
        var key = "${setting.category}."
        if (setting.subcategory != "") key += "${setting.subcategory}."
        key += setting.nameSerializedKey

        setting.serializedValue = data.get(setting.nameSerializedCategoryAndKey)
    }
}