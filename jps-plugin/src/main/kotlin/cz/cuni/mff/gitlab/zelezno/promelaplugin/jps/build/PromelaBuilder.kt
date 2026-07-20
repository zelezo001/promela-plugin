package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.JpsPromelaSdkType
import org.jetbrains.jps.builders.BuildTargetType
import org.jetbrains.jps.incremental.BuilderService
import org.jetbrains.jps.incremental.TargetBuilder
import org.jetbrains.jps.incremental.resources.ResourcesBuilder

/**
 * JPS builder service for adding support for building of Promela sources
 *
 */
class PromelaBuilder : BuilderService() {

    override fun getTargetTypes(): List<BuildTargetType<*>?> {
        return listOf(PromelaStateTableBuildTargetType.INSTANCE, PromelaVerificationBuildTargetType.INSTANCE)
    }

    /**
     * Provides the list of supported [TargetBuilder]s also registers enabler into [ResourcesBuilder]
     * which disables resource copying of resources inside modules using Promela
     */
    override fun createBuilders(): List<TargetBuilder<*, *>?> {
        ResourcesBuilder.registerEnabler {
            // disable resource builder for spin SDK, otherwise IDEA always copies whole src to Java build dir
                m ->
            val sdk = m.getSdk(JpsPromelaSdkType)
            sdk == null
        }
        return listOf(PromelaPromelaStateTableTargetBuilder(), PromelaVerificationTargetBuilder())
    }
}


