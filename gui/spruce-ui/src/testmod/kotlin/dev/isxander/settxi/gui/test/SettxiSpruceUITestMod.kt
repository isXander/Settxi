package dev.isxander.settxi.gui.test

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.settxi.SettxiConfig
import dev.isxander.settxi.gui.spruce.*
import dev.isxander.settxi.impl.*
import net.minecraft.text.Text
import java.io.File
import java.nio.file.Path

object SettxiSpruceUITestMod {
    object ModMenuIntegration : ModMenuApi {
        override fun getModConfigScreenFactory() = ConfigScreenFactory { parent ->
            TestSettings.spruceUI(Text.literal("Test GUI"), parent)
        }
    }

    object TestSettings : SettxiConfig() {
        override fun import() = println("Settxi SpruceUI: Fake Importing")
        override fun export() = println("Settxi SpruceUI: Fake Exporting")

        var testBool by boolean(true) {
            name = "Test Boolean"
            category = "Primitives"
            description = "Test description"
            spruceUIColoured = true
        }

        var testBoolCheckbox by boolean(true) {
            name = "Test Boolean Checkbox"
            category = "Primitives"
            spruceUIUseCheckbox = true
        }

        var testDouble by double(0.5) {
            name = "Test Double"
            category = "Primitives"
        }

        var testEnum by enum(Alphabet.A) {
            name = "Test Enum"
            category = "Primitives"
        }

        var testFloat by float(0.5f) {
            name = "Test Float"
            category = "Primitives"
            range = 0f..1f
            spruceUIHalfWidth = true
        }

        var testInt by int(50) {
            name = "Test Int"
            category = "Primitives"
            range = 0..100
            spruceUIHalfWidth = true
        }

        var testFile by file(File(".")) {
            name = "Test File"
            category = "Files"
        }

        var testPath by path(Path.of(".")) {
            name = "Test Path"
            category = "Files"
        }

        var testString by string("Hello, World!") {
            name = "Test String"
            category = "Files"
        }

        enum class Alphabet {
            A, B, C
        }
    }
}