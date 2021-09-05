package dev.isxander.settxi.impl

import dev.isxander.settxi.Setting
import dev.isxander.settxi.providers.IValueProvider
import dev.isxander.settxi.utils.DataTypes

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class DoubleSetting(val name: String, val category: String, val subcategory: String = "", val description: String, val min: Double, val max: Double, val suffix: String = "", val save: Boolean = true)

class DoubleSettingWrapped(annotation: DoubleSetting, provider: IValueProvider<Double>) : Setting<Double, DoubleSetting>(annotation, provider) {
    override val name: String = annotation.name
    override val category: String = annotation.category
    override val subcategory: String = annotation.subcategory
    override val description: String = annotation.description
    override val shouldSave: Boolean = annotation.save

    override var serializedValue: Any
        get() = value
        set(new) { value = new as Double }

    override val defaultSerializedValue: Double = default

    override val dataType: DataTypes = DataTypes.Double

}