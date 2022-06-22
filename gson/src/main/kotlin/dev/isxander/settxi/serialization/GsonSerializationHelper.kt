package dev.isxander.settxi.serialization

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.isxander.settxi.Setting

/**
 * Gets [com.google.gson] helper functions to
 * serialize and deserialize settings.
 */
val List<Setting<*>>.gson: GsonSerializationHelper
    get() = GsonSerializationHelper(this)

class GsonSerializationHelper internal constructor(private val settings: List<Setting<*>>) {
    /**
     * Exports all settings into a [JsonObject] to be saved.
     */
    fun asJson(): JsonObject {
        val jsonObject = JsonObject()
        for (setting in settings) {
            if (!setting.shouldSave) continue

            if (!jsonObject.has(setting.categorySerializedKey))
                jsonObject.add(setting.categorySerializedKey, JsonObject())
            val category = jsonObject.getAsJsonObject(setting.categorySerializedKey)

            category.add(setting.nameSerializedKey, setting.serializedValue.toJsonPrimitive())
        }

        return jsonObject
    }

    /**
     * Import values from an exported [JsonObject]
     */
    fun importFromJson(json: JsonObject) {
        for (setting in settings) {
            if (!setting.shouldSave) continue

            val category = json[setting.categorySerializedKey]?.asJsonObject ?: continue
            setting.setSerialized(
                category[setting.nameSerializedKey]?.asJsonPrimitive?.toPrimitiveType() ?: setting.defaultSerializedValue
            )
        }
    }

    companion object {
        fun JsonPrimitive.toPrimitiveType(): PrimitiveType =
            when {
                isString -> PrimitiveType.of(asString)
                isNumber -> PrimitiveType.of(asNumber)
                isBoolean -> PrimitiveType.of(asBoolean)
                else -> error("Couldn't convert JsonPrimitive -> PrimitiveType")
            }

        fun PrimitiveType.toJsonPrimitive(): JsonPrimitive =
            when {
                isString -> JsonPrimitive(string)
                isNumber -> JsonPrimitive(number)
                isBoolean -> JsonPrimitive(boolean)
                else -> error("Couldn't convert PrimitiveType -> JsonPrimitive")
            }
    }
}