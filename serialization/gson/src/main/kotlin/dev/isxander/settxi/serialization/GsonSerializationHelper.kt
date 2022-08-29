package dev.isxander.settxi.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import dev.isxander.settxi.Setting
import java.util.stream.Collectors

fun gsonSerializer(gsonBuilder: GsonBuilder.() -> Unit = {}) =
    gsonSerializer(GsonBuilder().apply(gsonBuilder).create())

fun gsonSerializer(gson: Gson) =
    GsonSerializationHelper(gson)

class GsonSerializationHelper internal constructor(private val gson: Gson) : SerializationHelper<JsonObject> {
    /**
     * Exports all settings into a [JsonObject] to be saved.
     */
    override fun asObject(settings: List<Setting<*>>): JsonObject {
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
     * @return if all values were present and imported
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
        gson.toJson(asObject(settings)).encodeToByteArray()

    override fun importFromBytes(bytes: ByteArray, settings: List<Setting<*>>): Boolean =
        importFromObject(gson.toJsonTree(bytes.decodeToString()).asJsonObject, settings)

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