package dev.isxander.settxi

/**
 * This interface has many extension functions that
 * allow you to automatically register and construct settings.
 * (You cannot construct settings outside this interface)
 *
 * ```
 * val mySetting by boolean(true) {
 *     // ...
 * }
 * ```
 */
interface ConfigProcessor {
    /**
     * List that is automatically managed that
     * contains all registered settings you create.
     *
     * Settings are registered automatically when you construct them.
     */
    val settings: MutableList<Setting<*>>
}