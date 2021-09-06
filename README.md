# `Settxi`
*Delegates-based settings library.*

```kotlin
class MyClass : ConfigProcessor {
    override val settings: MutableList<Setting<*>> = mutableListOf()
    
    val myColor by color(
        default = Color(255, 255, 255),
        name = "My Awesome Color",
        description = "An example color for Settxi.",
        category = "Examples",
        subcategory = "Colors", // optional
        allowTransparency = false,
    ) {
        set {
            // modify colors so they have to be 100 alpha or more
            Color(it.red, it.green, it.blue, it.alpha.coerceAtLeast(100))
        }
    }
    
    var config: Config
        get() { // export settings to night-config
            val cfg = Config.of(JsonFormat.instance())
            for (s in settings) addSettingToConfig(cfg, s)
            return cfg
        }
        set(cfg) {
            for (s in settings) setSettingFromConfig(cfg, s)
        }
}
```
