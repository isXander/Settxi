package dev.isxander.settxi

import dev.isxander.settxi.serialization.PrimitiveType

typealias Migrator = (PrimitiveType) -> PrimitiveType

/**
 * Predefined migrators to clean up settings classes
 */
object Migrators {
    inline fun <reified T : Enum<T>> optionToEnum(): Migrator = {
        if (it.isString) {
            PrimitiveType.of(enumValueOf<T>(it.string).ordinal)
        } else {
            it
        }
    }
}