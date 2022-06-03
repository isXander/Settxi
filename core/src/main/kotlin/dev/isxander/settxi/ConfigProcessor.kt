package dev.isxander.settxi

interface ConfigProcessor {
    val settings: MutableList<Setting<*>>
}