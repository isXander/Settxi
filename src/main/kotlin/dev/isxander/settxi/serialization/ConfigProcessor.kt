package dev.isxander.settxi.serialization

import dev.isxander.settxi.Setting

interface ConfigProcessor {
    val settings: MutableList<Setting<*>>
}