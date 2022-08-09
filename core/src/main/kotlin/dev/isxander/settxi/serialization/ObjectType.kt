package dev.isxander.settxi.serialization

class ObjectType(
    private val backedMap: MutableMap<String, SerializedType> = linkedMapOf()
) : SerializedType(), MutableMap<String, SerializedType> by backedMap {
    override val typeString: String
        get() = "object"
}