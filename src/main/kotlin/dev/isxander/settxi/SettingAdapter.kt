package dev.isxander.settxi

abstract class SettingAdapter<T> protected constructor(lambda: SettingAdapter<T>.() -> Unit = {}) {
    protected var getter: (T) -> T = { it }
    protected var setter: (T) -> T = { it }
    protected var depends: MutableList<(T) -> Boolean> = mutableListOf()

    init {
        this.apply(lambda)
    }

    fun get(lambda: (T) -> T) { getter = lambda }
    fun set(lambda: (T) -> T) { setter = lambda }
    fun depends(lambda: (T) -> Boolean) = depends.add(lambda)
}