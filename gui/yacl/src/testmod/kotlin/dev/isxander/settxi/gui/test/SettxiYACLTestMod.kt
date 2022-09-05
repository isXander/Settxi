package dev.isxander.settxi.gui.test

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.settxi.SettxiConfig
import dev.isxander.settxi.gui.yocl.*
import dev.isxander.settxi.impl.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.toast.SystemToast
import net.minecraft.text.Text

object SettxiYACLTestMod {
    object ModMenuIntegration : ModMenuApi {
        override fun getModConfigScreenFactory() = ConfigScreenFactory {
            TestSettings.yetAnotherConfigLib(Text.of("Test GUI"), it)
        }
    }

    object TestSettings : SettxiConfig() {
        override fun import() = println("Settxi YACL: Fake Importing")
        override fun export() = println("Settxi YACL: Fake Exporting")

        private val booleanGroup = Group(Text.of("Booleans"))

        var testBool by boolean(true) {
            name = "Test Boolean"
            category = "Primitives"
            yaclGroup = booleanGroup
            description = "Example of a boolean option."
            yaclColouredText = true
        }

        var testTickBox by boolean(true) {
            name = "Test TickBox"
            category = "Primitives"
            yaclGroup = booleanGroup
            description = "Example of a boolean option using a tick box controller."
            yaclUseTickBox = true
        }

        var testEnum by enum(Alphabet.A) {
            name = "Test Enum"
            category = "Primitives"
        }

        private val floatingPointGroup = Group(Text.of("Floating Point"))

        var testDouble by double(0.5) {
            name = "Test Double"
            category = "Primitives"
            yaclGroup = floatingPointGroup
            range = 0.0..1.0
            yaclSliderInterval = 0.05
        }

        var testFloat by float(0.5f) {
            name = "Test Float"
            category = "Primitives"
            yaclGroup = floatingPointGroup
            range = 0f..1f
            yaclSliderInterval = 0.05f
        }

        private val integerPointGroup = Group(Text.of("Integer Point"))

        var testInt by int(50) {
            name = "Test Int"
            category = "Primitives"
            yaclGroup = integerPointGroup
            range = 0..100
            yaclSliderInterval = 1
        }

        var testLong by long(50) {
            name = "Test Long"
            category = "Primitives"
            yaclGroup = integerPointGroup
            range = 0L..100L
            yaclSliderInterval = 1
        }

        var testString by string("Hello, World!") {
            name = "Test String"
            category = "Primitives"
        }

        var testButton by yaclButton({
            SystemToast.add(MinecraftClient.getInstance().toastManager, SystemToast.Type.TUTORIAL_HINT, Text.of("Settxi"), Text.of("Button invoked!"))
        }) {
            name = "Test Button"
            category = "Primitives"
        }

        enum class Alphabet {
            A, B, C
        }
    }
}