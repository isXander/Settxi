package dev.isxander.settxi.serialization

sealed class SerializedType {
    val isArray: Boolean
        get() = this is ArrayType

    val array: ArrayType
        get() = this as ArrayType

    val isObject: Boolean
        get() = this is ObjectType

    val `object`: ObjectType
        get() = this as ObjectType

    val isPrimitive: Boolean
        get() = this is PrimitiveType

    val primitive: PrimitiveType
        get() = this as PrimitiveType

    abstract val typeString: String
}