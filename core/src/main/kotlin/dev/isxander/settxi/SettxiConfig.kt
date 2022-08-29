package dev.isxander.settxi

/**
 * Abstraction layer for serialization over [ConfigProcessor]
 *
 * You can either create your own serialization system or use
 * ones provided in the serialization modules.
 */
abstract class SettxiConfig : ConfigProcessor {
    override val settings = mutableListOf<Setting<*>>()

    /**
     * Imports all values for settings, usually from a file.
     *
     * Similar to loading but only "loads" the actual value of the setting, not the whole setting.
     */
    abstract fun import()

    /**
     * Exports all values for settings, usually to a file.
     *
     * Similar to saving but only "saves" the actual value
     * (mapped to [Setting.name] modified for a key-like pattern).
     */
    abstract fun export()
}