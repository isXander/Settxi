import dev.isxander.settxi.ConfigProcessor
import dev.isxander.settxi.Setting
import dev.isxander.settxi.impl.enum
import dev.isxander.settxi.serialization.kotlinxSerializer
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
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

        val serialized = kotlinxSerializer().asObject(settingsSample.settings)
        val expected = buildJsonObject {
            put("settings_sample", buildJsonObject {
                put("enum_setting", JsonPrimitive(SettingsSample.EnumSample.ONE.ordinal))
            })
        }

        assertEquals(serialized, expected)
    }

    @Test
    fun testSettingsPopulation() {
        val settingsSample = SettingsSample()

        val modified = buildJsonObject {
            put("settings_sample", buildJsonObject {
                put("enum_setting", JsonPrimitive(SettingsSample.EnumSample.TWO.ordinal))
            })
        }

        kotlinxSerializer().importFromObject(modified, settingsSample.settings)

        assertEquals(settingsSample.enumSetting, SettingsSample.EnumSample.TWO)
    }
}