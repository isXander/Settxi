package dev.isxander.settxi.gui.test

import dev.isxander.settxi.gui.clothGui
import dev.isxander.settxi.impl.*
import dev.isxander.settxi.serialization.SettxiConfig
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.text.Text
import java.io.File
import java.nio.file.Path

object SettxiClothTestMod : ClientModInitializer {
    private var displayGui = false

    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(ClientCommandManager.literal("settxi-cloth-test").executes {
                displayGui = true
                0
            })
        }

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (displayGui) {
                displayGui = false
                client.setScreen(TestSettings.clothGui(Text.literal("Test GUI")))
            }
        }
    }

    object TestSettings : SettxiConfig() {
        override fun import() = println("Settxi Cloth: Fake Importing")
        override fun export() = println("Settxi Cloth: Fake Exporting")

        var testBool by boolean(true) {
            name = "Test Boolean"
            category = "Types"
        }

        var testDouble by double(0.5) {
            name = "Test Double"
            category = "Types"
            range = 0.0..1.0
        }

        var testEnum by enum(Alphabet.A) {
            name = "Test Enum"
            category = "Types"
        }

        var testFile by file(File(".")) {
            name = "Test File"
            category = "Types"
        }

        var testFloat by float(0.5f) {
            name = "Test Float"
            category = "Types"
            range = 0f..1f
        }

        var testInt by int(50) {
            name = "Test Int"
            category = "Types"
            range = 0..100
        }

        var testLong by long(50L) {
            name = "Test Long"
            category = "Types"
            range = 0L..100L
        }

        var testPath by path(Path.of(".")) {
            name = "Test Path"
            category = "Types"
        }

        var testString by string("Hello, World!") {
            name = "Test String"
            category = "Types"
        }

        enum class Alphabet {
            A, B, C
        }
    }
}