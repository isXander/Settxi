package dev.isxander.settxi.serialization

import dev.isxander.settxi.Setting
import kotlinx.serialization.json.*

/**
 * Gets [kotlinx.serialization] helper functions to
 * serialize and deserialize settings.
 */
val <T : Setting<*>> List<T>.kotlinx: KotlinxSerializationHelper
    get() = KotlinxSerializationHelper(this)

class KotlinxSerializationHelper internal constructor(private val settings: List<Setting<*>>) {
    /**
     * Exports all settings into a [JsonObject] to be saved.
     */
    fun asJson(): JsonObject {
        val settings = mutableMapOf<String, MutableMap<String, SerializedType>>()
        for (setting in this.settings) {
            try {
                if (!setting.shouldSave) continue

                val category = settings.computeIfAbsent(setting.categorySerializedKey) { mutableMapOf() }
                category[setting.nameSerializedKey] = setting.serializedValue
            } catch (e: Exception) {
                SettxiSerializationException("Failed to serialize setting \"${setting.name}\"", e).printStackTrace()
            }
        }

        return JsonObject(settings.mapValues { (_, content) -> JsonObject(content.mapValues { (_, v) -> v.toJsonElement() }) })
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
                is JsonPrimitive -> toPrimitiveType()
                is JsonArray -> toArrayType()
                is JsonNull -> error("Can't serialize null!")
            }

        fun JsonObject.toObjectType(): ObjectType =
            ObjectType(this.mapValues { (_, v) -> v.toSerializedType() }.toMutableMap())

        fun JsonPrimitive.toPrimitiveType(): PrimitiveType =
            when {
                isString -> PrimitiveType.of(content)
                doubleOrNull != null -> PrimitiveType.of(double)
                booleanOrNull != null -> PrimitiveType.of(boolean)
                else -> error("Couldn't convert JsonPrimitive -> PrimitiveType")
            }

        fun JsonArray.toArrayType(): ArrayType =
            ArrayType(this.map { it.toSerializedType() }.toMutableList())

        fun SerializedType.toJsonElement(): JsonElement =
            when (this) {
                is ObjectType -> toJsonObject()
                is ArrayType -> toJsonArray()
                is PrimitiveType -> toJsonPrimitive()
            }

        fun ObjectType.toJsonObject(): JsonObject =
            JsonObject(this.mapValues { (_, v) -> v.toJsonElement() })

        fun ArrayType.toJsonArray(): JsonArray =
            JsonArray(this.map { it.toJsonElement() })

        fun PrimitiveType.toJsonPrimitive(): JsonPrimitive =
            when {
                isString -> JsonPrimitive(string)
                isNumber -> JsonPrimitive(number)
                isBoolean -> JsonPrimitive(boolean)
                else -> error("Couldn't convert PrimitiveType -> JsonPrimitive")
            }
    }
}