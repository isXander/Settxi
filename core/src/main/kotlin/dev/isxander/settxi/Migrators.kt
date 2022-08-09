package dev.isxander.settxi

import dev.isxander.settxi.serialization.PrimitiveType
import dev.isxander.settxi.serialization.SerializedType

typealias Migrator = (SerializedType) -> SerializedType

/**
 * Predefined migrators to clean up settings classes
 */
object Migrators {
    inline fun <reified T : Enum<T>> optionToEnum(): Migrator = {
        if (it.isPrimitive && it.primitive.isString) {
            PrimitiveType.of(enumValueOf<T>(it.primitive.string).ordinal)
        } else {
            it
        }
    }
}