package dev.isxander.settxi.exception

import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.serialization.ConfigProcessor

class SettxiException(message: String, e: Exception? = null) : RuntimeException(message, e), ConfigProcessor {
    var booleanSetting by boolean {
        default = true
        name = ""
    }
}