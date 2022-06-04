package dev.isxander.settxi.serialization

import dev.isxander.settxi.Setting
import kotlinx.serialization.json.*

fun List<Setting<*>>.asJson(): JsonObject {
    val settings = mutableMapOf<String, MutableMap<String, PrimitiveType>>()
    for (setting in this) {
        if (!setting.shouldSave) continue

        val category = settings.computeIfAbsent(setting.categorySerializedKey) { mutableMapOf() }
        category[setting.nameSerializedKey] = setting.serializedValue
    }

    return JsonObject(settings.mapValues { (_, content) -> JsonObject(content.mapValues { it.value.toJsonPrimitive() }) })
}

fun List<Setting<*>>.populateFromJson(json: JsonObject) {
    for (setting in this) {
        if (!setting.shouldSave) continue

        val category = json[setting.categorySerializedKey]?.jsonObject ?: continue
        setting.setSerialized(category[setting.nameSerializedKey]?.jsonPrimitive?.toPrimitiveType() ?: setting.defaultSerializedValue)
    }
}

fun JsonPrimitive.toPrimitiveType(): PrimitiveType =
    when {
        isString -> PrimitiveType.of(content)
        doubleOrNull != null -> PrimitiveType.of(double)
        booleanOrNull != null -> PrimitiveType.of(boolean)
        else -> error("Couldn't convert JsonPrimitive -> PrimitiveType")
    }

fun PrimitiveType.toJsonPrimitive(): JsonPrimitive =
    when {
        isString -> JsonPrimitive(string)
        isNumber -> JsonPrimitive(number)
        isBoolean -> JsonPrimitive(boolean)
        else -> error("Couldn't convert PrimitiveType -> JsonPrimitive")
    }