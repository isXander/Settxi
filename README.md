# `Settxi`
*Delegates-based settings library.*

```kt
var settxiIsAwesome by boolean(true) {
    name = "Awesome"
    description = "Is Settxi awesome or not."
    category = "Settxi"
}
```

## Usage
Add https://maven.isxander.dev to your repositories list:
```kts
repositories {
    maven("https://maven.isxander.dev/releases")
}
```

You can then depend on any Settxi module you want:
```kts
dependencies {
    implementation("dev.isxander.settxi:settxi-core:2.4.0")
}
```

## Not just a settings library
Settxi provides serialization and GUI implementations for various libraries.

### Serialization
#### kotlinx.serialization
Depend on the `settxi-kotlinx-serialization` module in your gradle buildscript.

```kts
implementation("dev.isxander.settxi:settxi-kotlinx-serialization:2.4.0")
```

Now you can save and load from a file using JSON!

```kt
 import java.nio.file.Path

 object SettxiSettings : SettxiConfigKotlinx(Path.of("~/.config/settxi.json")) {
     // the power of settxi is at your fingertips!
     
     // make sure to import from the file at the end of your class!
     init {
         import()
     }
 }
```

#### Gson
Depend on the `settxi-gson` module in your gradle buildscript.

```kts
implementation("dev.isxander.settxi:settxi-gson:2.4.0")
```

Now you can save and load from a file using JSON!

```kt
 import java.nio.file.Path

 object SettxiSettings : SettxiConfigGson(Path.of("~/.config/settxi.json")) {
     // the power of settxi is at your fingertips!
     
     // make sure to import from the file at the end of your class!
     init {
         import()
     }
 }
```

### GUI
#### [Cloth Config](https://github.com/shedaniel/cloth-config) (Minecraft)
Depend on the `settxi-gui-cloth-config` module in your gradle buildscript.

```kts
implementation("dev.isxander.settxi:settxi-gui-cloth-config:2.4.0")
```

Now you can easily create a cloth config screen to be displayed in Minecraft!
Requires you use `SettxiConfig` or implementations

```kt
// you could use SettxiConfigKotlinx etc
object SettxiSettings : SettxiConfig() {
    // ... the power of settxi is at your fingertips!
}

SettxiSettings.clothGui(title = Text.empty(), parent = null) // now you have a Screen
```

You can register factories for custom `Setting` classes as well.
Learn how [on the wiki](https://github.com/isXander/Settxi/wiki/Usage#cloth-config-minecraft)
