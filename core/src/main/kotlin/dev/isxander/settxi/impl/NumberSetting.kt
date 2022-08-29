package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting

abstract class NumberSetting<N : Number>(default: N) : Setting<N>(default)