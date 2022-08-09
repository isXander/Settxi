package dev.isxander.settxi.serialization

import java.util.LinkedList

class ArrayType(
    private val backedArray: MutableList<SerializedType> = LinkedList()
) : SerializedType(), MutableList<SerializedType> by backedArray {
    constructor(vararg elements: SerializedType) : this(LinkedList<SerializedType>().apply { addAll(elements) })

    override val typeString: String
        get() = "array"
}