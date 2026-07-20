package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model

import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension
import org.jetbrains.jps.model.serialization.facet.JpsFacetConfigurationSerializer
import org.jetbrains.jps.model.serialization.library.JpsSdkPropertiesSerializer
import org.jetbrains.jps.model.serialization.runConfigurations.JpsRunConfigurationPropertiesSerializer

/**
 * Class for registering all serializers needed by this JPS plugin
 */
class JpsPromelaModelSerializerExtension : JpsModelSerializerExtension() {

    override fun getSdkPropertiesSerializers(): MutableList<out JpsSdkPropertiesSerializer<*>?> {
        return mutableListOf(SpinSdkSerializer())
    }

    override fun getFacetConfigurationSerializers(): List<JpsFacetConfigurationSerializer<*>?> {
        return listOf(PromelaFacetJpsFacetConfigurationSerializer())
    }

    override fun getRunConfigurationPropertiesSerializers(): List<JpsRunConfigurationPropertiesSerializer<*>?> {
        return listOf(VerificationRunSerializer())
    }
}