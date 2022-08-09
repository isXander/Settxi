package dev.isxander.settxi.serialization

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.isxander.settxi.Setting
import java.util.stream.Collectors

/**
 * Gets [com.google.gson] helper functions to
 * serialize and deserialize settings.
 */
val <T : Setting<*>> List<T>.gson: GsonSerializationHelper
    get() = GsonSerializationHelper(this)

class GsonSerializationHelper internal constructor(private val settings: List<Setting<*>>) {
    /**
     * Exports all settings into a [JsonObject] to be saved.
     */
    fun asJson(): JsonObject {
        val jsonObject = JsonObject()
        for (setting in settings) {
            try {
                if (!setting.shouldSave) continue

                if (!jsonObject.has(setting.categorySerializedKey))
                    jsonObject.add(setting.categorySerializedKey, JsonObject())
                val category = jsonObject.getAsJsonObject(setting.categorySerializedKey)

                category.add(setting.nameSerializedKey, setting.serializedValue.toJsonElement())
            } catch (e: Exception) {
                SettxiSerializationException("Failed to serialize setting \"${setting.name}\"", e).printStackTrace()
            }
        }

        return jsonObject
    }

    /**
     * Import values from an exported [JsonObject]
     */
    fun importFromJson(json: JsonObject) {
        val rootObject = json.toObjectType()
        for (setting in settings) {
            try {
                if (!setting.shouldSave) continue

                val category = rootObject[setting.categorySerializedKey]?.`object`
                setting.setSerialized(
                    category?.get(setting.nameSerializedKey) ?: setting.defaultSerializedValue(rootObject, category)
                )
            } catch (e: Exception) {
                SettxiSerializationException("Failed to import setting \"${setting.name}\"", e).printStackTrace()
            }
        }
    }

    companion object {
        fun JsonElement.toSerializedType(): SerializedType =
            when (this) {
                is JsonObject -> toObjectType()
                is JsonArray -> toArrayType()
                is JsonPrimitive -> toPrimitiveType()
                is JsonNull -> error("Can't serialize null!")
                else -> error("Unknown type")
            }

        fun JsonObject.toObjectType(): ObjectType =
            ObjectType(
                this.entrySet().stream().collect(
                    Collectors.toMap(
                        MutableMap.MutableEntry<String, JsonElement>::key,
                        MutableMap.MutableEntry<String, JsonElement>::value
                    )
                ).mapValues { (_, v) -> v.toSerializedType() }.toMutableMap()
            )

        fun JsonArray.toArrayType(): ArrayType =
            ArrayType(this.map { it.toSerializedType() }.toMutableList())

        fun JsonPrimitive.toPrimitiveType(): PrimitiveType =
            when {
                isString -> PrimitiveType.of(asString)
                isNumber -> PrimitiveType.of(asNumber)
                isBoolean -> PrimitiveType.of(asBoolean)
                else -> error("Couldn't convert JsonPrimitive -> PrimitiveType")
            }


        fun SerializedType.toJsonElement(): JsonElement =
            when (this) {
                is ObjectType -> toJsonObject()
                is ArrayType -> toJsonArray()
                is PrimitiveType -> toJsonPrimitive()
            }

        fun ObjectType.toJsonObject(): JsonObject =
            JsonObject().apply { this@toJsonObject.forEach { this.add(it.key, it.value.toJsonElement()) } }

        fun ArrayType.toJsonArray(): JsonArray =
            JsonArray(size).apply { this@toJsonArray.forEach { this.add(it.toJsonElement()) } }

        fun PrimitiveType.toJsonPrimitive(): JsonPrimitive =
            when {
                isString -> JsonPrimitive(string)
                isNumber -> JsonPrimitive(number)
                isBoolean -> JsonPrimitive(boolean)
                else -> error("Couldn't convert PrimitiveType -> JsonPrimitive")
            }
    }
}