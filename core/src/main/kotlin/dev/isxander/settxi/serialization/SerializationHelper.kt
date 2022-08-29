package dev.isxander.settxi.serialization

import dev.isxander.settxi.Setting

/**
 * Abstraction API for serializing and deserializing settings
 * Used by serialization modules
 *
 * @see [SettxiFileConfig]
 */
interface SerializationHelper<T> {
    /**
     * Converts a settings list into the respected library's object type
     */
    fun asObject(settings: List<Setting<*>>): T

    /**
     * Expresses the library's object type as bytes, could be a string.
     */
    fun asBytes(settings: List<Setting<*>>): ByteArray


    /**
     * Attempts to deserialize into the library's object type and apply it to the settings list.
     */
    fun importFromObject(obj: T, settings: List<Setting<*>>): Boolean

    /**
     * Attempts to deserialize bytes and apply it to the settings list.
     */
    fun importFromBytes(bytes: ByteArray, settings: List<Setting<*>>): Boolean
}