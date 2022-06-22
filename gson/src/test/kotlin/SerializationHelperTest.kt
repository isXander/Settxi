import com.google.gson.JsonObject
import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.enum
import dev.isxander.settxi.serialization.gson
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

internal class SerializationHelperTest {
    class SettingsSample : ConfigProcessor {
        override val settings = mutableListOf<Setting<*>>()

        val enumSetting by enum(EnumSample.ONE) {
            name = "Enum Setting"
            category = "Settings Sample"
        }

        enum class EnumSample {
            ONE,
            TWO,
        }
    }

    @Test
    fun testSerialize() {
        val settingsSample = SettingsSample()

        val serialized = settingsSample.settings.gson.asJson()

        val expected = JsonObject()
        val category = JsonObject()
        category.addProperty("enum_setting", SettingsSample.EnumSample.ONE.ordinal)
        expected.add("settings_sample", category)

        assertEquals(serialized, expected)
    }

    @Test
    fun testSettingsPopulation() {
        val settingsSample = SettingsSample()

        val modified = JsonObject()
        val category = JsonObject()
        category.addProperty("enum_setting", SettingsSample.EnumSample.TWO.ordinal)
        modified.add("settings_sample", category)

        settingsSample.settings.gson.importFromJson(modified)

        assertEquals(settingsSample.enumSetting, SettingsSample.EnumSample.TWO)
    }
}