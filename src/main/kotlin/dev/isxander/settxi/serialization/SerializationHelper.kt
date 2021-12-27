package dev.isxander.settxi.serialization

import dev.isxander.settxi.Setting
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

fun List<Setting<*>>.asJson(): JsonObject {
    val settings = mutableMapOf<String, MutableMap<String, JsonElement>>()
    for (setting in this) {
        if (!setting.shouldSave) continue

        val category = settings.computeIfAbsent(setting.category) { mutableMapOf() }
        category[setting.nameSerializedKey] = setting.serializedValue
    }

    return JsonObject(settings.mapValues { (_, content) -> JsonObject(content) })
}

fun List<Setting<*>>.populateFromJson(json: JsonObject) {
    for (setting in this) {
        if (!setting.shouldSave) continue

        val category = json[setting.category]!!.jsonObject
        setting.serializedValue = category[setting.nameSerializedKey] ?: setting.defaultSerializedValue
    }
}