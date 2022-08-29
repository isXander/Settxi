package dev.isxander.settxi.serialization

import dev.isxander.settxi.Setting
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

/**
 * Gets [kotlinx.serialization] helper functions to
 * serialize and deserialize settings.
 */
fun kotlinxSerializer(from: Json = Json.Default, jsonBuilder: JsonBuilder.() -> Unit = {}): KotlinxSerializationHelper =
    KotlinxSerializationHelper(Json(from, jsonBuilder))

class KotlinxSerializationHelper internal constructor(private val json: Json) : SerializationHelper<JsonObject> {
    /**
     * Exports all settings into a [JsonObject] to be saved.
     */
    override fun asObject(settings: List<Setting<*>>): JsonObject {
        val serializedSettings = mutableMapOf<String, MutableMap<String, SerializedType>>()
        for (setting in settings) {
            try {
                if (!setting.shouldSave) continue

                val category = serializedSettings.computeIfAbsent(setting.categorySerializedKey) { mutableMapOf() }
                category[setting.nameSerializedKey] = setting.serializedValue
            } catch (e: Exception) {
                SettxiSerializationException("Failed to serialize setting \"${setting.name}\"", e).printStackTrace()
            }
        }

        return JsonObject(serializedSettings.mapValues { (_, content) -> JsonObject(content.mapValues { (_, v) -> v.toJsonElement() }) })
    }

    /**
     * Import values from an exported [JsonObject]
     */
    override fun importFromObject(obj: JsonObject, settings: List<Setting<*>>): Boolean {
        val rootObject = obj.toObjectType()
        var shouldSave = false
        for (setting in settings) {
            try {
                if (!setting.shouldSave) continue

                val category = rootObject[setting.categorySerializedKey]?.`object`
                setting.setSerialized(
                    category?.get(setting.nameSerializedKey) ?: setting.defaultSerializedValue(rootObject, category).also { shouldSave = true }
                )
            } catch (e: Exception) {
                SettxiSerializationException("Failed to import setting \"${setting.name}\"", e).printStackTrace()
            }
        }
        return !shouldSave
    }

    override fun asBytes(settings: List<Setting<*>>): ByteArray =
        json.encodeToString(asObject(settings)).encodeToByteArray()

    override fun importFromBytes(bytes: ByteArray, settings: List<Setting<*>>): Boolean =
        importFromObject(json.decodeFromString(bytes.decodeToString()), settings)

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