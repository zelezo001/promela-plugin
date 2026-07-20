package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.configuration

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationModule
import com.intellij.execution.configurations.SimpleConfigurationType
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NotNullLazyValue
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle

/**
 * Type of the run configuration for Promela simulations.
 */
class SimulationConfigurationType : SimpleConfigurationType(
    NAME,
    PromelaBundle.message("simulation.configuration.name"),
    PromelaBundle.message("simulation.configuration.description"),
    NotNullLazyValue.createValue {
        AllIcons.Nodes.Console
    }) {
    companion object {
        const val NAME = "promela.simulation"
    }

    override fun getOptionsClass(): Class<out BaseState?>? {
        return SimulationConfigurationOptions::class.java
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return SimulationRunConfiguration(RunConfigurationModule(project), this, name)
    }
}