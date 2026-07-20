package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model

import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.annotations.Tag
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.JpsPromelaSdkType.DEFAULT_C_COMPILER
import org.jdom.Element
import org.jetbrains.jps.model.JpsElementTypeWithDefaultProperties
import org.jetbrains.jps.model.ex.JpsElementBase
import org.jetbrains.jps.model.library.sdk.JpsSdkType
import org.jetbrains.jps.model.serialization.library.JpsSdkPropertiesSerializer

/**
 * Properties of the Spin SDK used during JPS build
 * @param cCompiler the C compiler executable name or path
 */
data class SpinSdkProperties(
    @JvmField @Tag("cCompiler") val cCompiler: String = ""
) : JpsElementBase<SpinSdkProperties>();

/**
 * The type of the Spin SDK in JPS
 */
object JpsPromelaSdkType : JpsSdkType<SpinSdkProperties>(),
    JpsElementTypeWithDefaultProperties<SpinSdkProperties> {

    const val DEFAULT_C_COMPILER = "cc"

    /**
     * Returns default [SpinSdkProperties] with c compiler set to [DEFAULT_C_COMPILER]
     */
    override fun createDefaultProperties(): SpinSdkProperties {
        return SpinSdkProperties(DEFAULT_C_COMPILER)
    }
}

/**
 * Serializer for the Spin SDK properties in JPS
 */
class SpinSdkSerializer : JpsSdkPropertiesSerializer<SpinSdkProperties?>(
    SPIN_SDK_TYPE_ID, JpsPromelaSdkType
) {
    companion object {
        const val SPIN_SDK_TYPE_ID: String = "spin-sdk"
    }

    override fun loadProperties(propertiesElement: Element?): SpinSdkProperties {
        if (propertiesElement == null) {
            return JpsPromelaSdkType.createDefaultProperties()
        }
        var properties = XmlSerializer.deserialize(
            propertiesElement.getOrCreateChild("sdk-properties"), SpinSdkProperties::class.java
        )

        // if c compiler is empty, set default value
        properties = properties.copy(cCompiler = properties.cCompiler.ifEmpty { JpsPromelaSdkType.DEFAULT_C_COMPILER })

        return properties
    }
}