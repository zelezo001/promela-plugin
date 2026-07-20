package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import com.intellij.openapi.diagnostic.Logger
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.PromelaVerificationJpsRunConfigurationType
import org.jetbrains.jps.builders.BuildTargetLoader
import org.jetbrains.jps.builders.BuildTargetType
import org.jetbrains.jps.model.JpsModel
import java.io.File

/**
 * Build target type for building models intended for verification runs.
 */
class PromelaVerificationBuildTargetType private constructor() :
    BuildTargetType<PromelaVerificationBuildTarget>("promela-verification", false) {

    companion object {
        val INSTANCE = PromelaVerificationBuildTargetType()
        val LOG = Logger.getInstance(PromelaVerificationBuildTargetType::class.java)
    }

    /**
     * Computes all targets from [PromelaVerificationJpsRunConfigurationType] contained in the target project
     * @param model target model
     */
    override fun computeAllTargets(model: JpsModel): List<PromelaVerificationBuildTarget?> {
        // build targets from known run configurations
        val configurations = model.project.getRunConfigurations(PromelaVerificationJpsRunConfigurationType)
        val targets = configurations.filter { it.properties.modelFile != "" && it.properties.module != "" }.map {
            val module = model.project.modules.firstOrNull { m -> m.name == it.properties.module }
            if (module == null) {
                LOG.warn("Spin model verification configuration ${it.name} has unknown module set")
                return@map null
            }
            PromelaVerificationBuildTarget(it, File(it.properties.modelFile), module)
        }.filter { true }

        return targets
    }

    /**
     * @return Simple loader that always tries to find searched target by looking among all known targets in the model
     */
    override fun createLoader(model: JpsModel): BuildTargetLoader<PromelaVerificationBuildTarget?> {
        val targets = computeAllTargets(model)
        return object : BuildTargetLoader<PromelaVerificationBuildTarget?>() {
            override fun createTarget(targetId: String): PromelaVerificationBuildTarget? {
                return targets.firstOrNull { it?.id == targetId }
            }
        }
    }
}