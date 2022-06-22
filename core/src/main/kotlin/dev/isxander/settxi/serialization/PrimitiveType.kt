package dev.isxander.settxi.serialization

/**
 * Abstraction over a "primitive" type commonly found in data-interchange formats (e.g. JSON)
 *
 * This can easily be converted to a library's equivalent or vice-versa for saving/loading.
 */
sealed class PrimitiveType {
    open val string: String
        get() = throw UnsupportedOperationException("Type not a string")

    open val stringOrNull: String?
        get() = null

    open val isString: Boolean
        get() = false

    open val int: Int
        get() = throw UnsupportedOperationException("Type not an int")

    open val intOrNull: Int?
        get() = null

    open val long: Long
        get() = throw UnsupportedOperationException("Type not a long")

    open val longOrNull: Long?
        get() = null

    open val float: Float
        get() = throw UnsupportedOperationException("Type not a float")

    open val floatOrNull: Float?
        get() = null

    open val double: Double
        get() = throw UnsupportedOperationException("Type not a double")

    open val doubleOrNull: Double?
        get() = null

    open val number: Number
        get() = throw UnsupportedOperationException("Type not a number")

    open val numberOrNull: Number?
        get() = null

    open val isNumber: Boolean
        get() = false

    open val isInt: Boolean
        get() = false

    open val isLong: Boolean
        get() = false

    open val isFloat: Boolean
        get() = false

    open val isDouble: Boolean
        get() = false

    open val boolean: Boolean
        get() = throw UnsupportedOperationException("Type not a boolean")

    open val booleanOrNull: Boolean?
        get() = null

    open val isBoolean: Boolean
        get() = false

    companion object {
        /**
         * Constructs a [PrimitiveType] from a number.
         */
        fun of(value: Number): PrimitiveType =
            PrimitiveNumberType(value)

        /**
         * Constructs a [PrimitiveType] from a string.
         */
        fun of(value: String): PrimitiveType =
            PrimitiveStringType(value)

        /**
         * Constructs a [PrimitiveType] from a boolean.
         */
        fun of(value: Boolean): PrimitiveType =
            PrimitiveBooleanType(value)
    }
}

class PrimitiveNumberType internal constructor(val value: Number) : PrimitiveType() {
    override val int: Int
        get() = value.toInt()

    override val intOrNull: Int?
        get() = value.toInt()

    override val long: Long
        get() = value.toLong()

    override val longOrNull: Long?
        get() = value.toLong()

    override val float: Float
        get() = value.toFloat()

    override val floatOrNull: Float?
        get() = value.toFloat()

    override val double: Double
        get() = value.toDouble()

    override val doubleOrNull: Double?
        get() = value.toDouble()

    override val number: Number
        get() = value

    override val numberOrNull: Number?
        get() = value

    override val isNumber: Boolean
        get() = true

    override val isInt: Boolean
        get() = number is Int

    override val isLong: Boolean
        get() = number is Long

    override val isFloat: Boolean
        get() = number is Float

    override val isDouble: Boolean
        get() = number is Double
}

class PrimitiveStringType internal constructor(val value: String) : PrimitiveType() {
    override val string: String
        get() = value

    override val stringOrNull: String?
        get() = value

    override val isString: Boolean
        get() = true
}

class PrimitiveBooleanType internal constructor(val value: Boolean) : PrimitiveType() {
    override val boolean: Boolean
        get() = value

    override val booleanOrNull: Boolean?
        get() = value

    override val isBoolean: Boolean
        get() = true
}