package dev.isxander.settxi.gui.spruce

import dev.isxander.settxi.Setting
import dev.isxander.settxi.SettxiConfig
import dev.lambdaurora.spruceui.Position
import dev.lambdaurora.spruceui.SpruceTexts
import dev.lambdaurora.spruceui.background.Background
import dev.lambdaurora.spruceui.option.SpruceSeparatorOption
import dev.lambdaurora.spruceui.option.SpruceSimpleActionOption
import dev.lambdaurora.spruceui.screen.SpruceScreen
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

class SettxiSpruceScreen internal constructor(
    title: Text,
    private val parent: Screen?,
    private val settxi: SettxiConfig,
    private val background: Background,
    private val factory: SettxiSpruceScreen.(SpruceOptionListWidget) -> Unit
) : SpruceScreen(title) {
    private lateinit var optionWidget: SpruceOptionListWidget

    override fun init() {
        super.init()

        optionWidget = SpruceOptionListWidget(Position.of(0, 22), width, height - 35 - 22)
        var position = 0
        val categoryPositions = settxi.settings.map { it.category }.toSet().associateWith { position++ }.toMutableMap()

        val iterator = settxi.settings.sortedBy { categoryPositions[it.category] }.listIterator()
        while (iterator.hasNext()) {
            val setting = iterator.next()

            if (categoryPositions.containsKey(setting.category)) {
                optionWidget.addSingleOptionEntry(SpruceSeparatorOption(setting.category, true, null))
                categoryPositions.remove(setting.category)
            }
            val entry = SettxiSpruceUIGui.buildEntryForSetting(Unit, setting)

            if (setting.spruceUIHalfWidth) {
                var next: Setting<*>? = null
                if (iterator.hasNext() && run { next = iterator.next(); next }!!.spruceUIHalfWidth && next!!.category.contentEquals(setting.category)) {
                    optionWidget.addOptionEntry(entry, SettxiSpruceUIGui.buildEntryForSetting(Unit, next!!))
                } else {
                    if (next != null)
                        iterator.previous()

                    optionWidget.addSmallSingleOptionEntry(entry)
                }
            } else {
                optionWidget.addSingleOptionEntry(entry)
            }
        }
        optionWidget.background = background
        factory(this, optionWidget)

        addDrawableChild(optionWidget)

        addDrawableChild(
            SpruceButtonWidget(
                Position.of(this, width / 2 - 155, height - 29), 150, 20, Text.translatable("gui.cancel")
            ) {
                settxi.settings.forEach(Setting<*>::reset)
                settxi.import()

                client!!.setScreen(parent)
            }
        )
        addDrawableChild(
            SpruceButtonWidget(
                Position.of(this, width / 2 - 155 + 160, height - 29), 150, 20, SpruceTexts.GUI_DONE
            ) {
                settxi.export()
                client!!.setScreen(parent)
            }.asVanilla()
        )
    }

    fun reinitialize() = init(client, width, height)

    override fun renderTitle(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        drawCenteredText(matrices, textRenderer, title, width / 2, 8, 16777215)
    }

    override fun shouldCloseOnEsc() = false
}