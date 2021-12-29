# `Settxi`
*Delegates-based settings library.*

```kotlin
class MyClass : ConfigProcessor {
    override val settings: MutableList<Setting<*>> = mutableListOf()
    
    var myColor by color(Color.white) {
        name = "My Awesome Color"
        description = "An example color for Settxi."
        category = "Examples"
        allowTransparency = false

        set {
            // modify colors so they have to be 100 alpha or more
            Color(it.red, it.green, it.blue, it.alpha.coerceAtLeast(100))
        }
    }
    
    var config: JsonObject
        get() { // export settings to kotlinx.serialization
            return settings.asJson()
        }
        set(json) {
            settings.populateFromJson(json)
        }
}
```
