package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.configuration

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.roots.ModuleRootManager
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.project.facet.PromelaFacet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.project.sdk.SpinSDK
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.SimulationState
import org.jetbrains.annotations.Unmodifiable
import java.io.File


/**
 * Run configuration for Promela simulations.
 * @param configurationModule the module for the configuration
 * @param factory the configuration factory
 * @param name the name of the configuration
 */
class SimulationRunConfiguration(
    configurationModule: RunConfigurationModule, factory: ConfigurationFactory, name: String?
) : ModuleBasedConfiguration<RunConfigurationModule, SimulationConfigurationOptions>(
    name, configurationModule, factory
) {
    override fun getValidModules(): @Unmodifiable Collection<Module> {
        return ModuleManager.getInstance(project).modules.toList()
            .filter { ModuleRootManager.getInstance(it).sdk?.sdkType is SpinSDK }
    }

    private fun resolveSpinPath(): String? {
        val module = configurationModule.module ?: return null
        val sdk = ModuleRootManager.getInstance(module).sdk ?: return null
        if (sdk.sdkType !is SpinSDK) return null
        return sdk.homePath
    }

    private fun resolveRunnableFile(): File? {
        val path = opts.modelFile ?: return null
        if (path.isEmpty()) return null
        return File(path).takeIf { it.exists() }
    }

    private fun simulationArguments(): List<String> {
        val options = mutableListOf("-n${randomSeed}", "-u${maxSteps}", "-r", "-p", "-s", "-v")
        options.add("-b") // suppresses printf to make it less fragile (spin does not distinguish between user and model output)
        options.add("-X") // more parsable format
        val add = { b: Boolean, flag: String -> if (b) options.add(flag) }

        add(trackDataValues, "-g")
        add(trackDataValues, "-l")
        add(initialStepsSkipped > 0, "-j${initialStepsSkipped}")
        add(mode == SimulationMode.GUIDED, "-k")
        add(mode == SimulationMode.GUIDED, trailFile)
        add(mode == SimulationMode.INTERACTIVE, "-i")

        add(!fullChannelBlocksOrLoses, "-m")

        if (inheritGlobalSpinOptions && this.configurationModule.module != null) {
            PromelaFacet.getForModule(this.configurationModule.module!!)
                ?.let { facet -> options.addAll(facet.configuration.spinOptions) }
        }

        options.addAll(extraSimulationOptions)


        return options
    }

    /**
     * @return [SimulationState] representing defined simulation
     * @throws ExecutionException if Promela model or spin cannot be resolved
     */
    override fun getState(
        executor: Executor, environment: ExecutionEnvironment
    ): SimulationState {
        val file = resolveRunnableFile() ?: throw ExecutionException(
            PromelaBundle.message("simulation.exception.missing-file")
        )
        val spin = resolveSpinPath() ?: throw ExecutionException(
            PromelaBundle.message("simulation.exception.missing-spin")
        )
        val verificationArguments = simulationArguments()
        return SimulationState(
            mode == SimulationMode.INTERACTIVE,
            valuesTracked = trackDataValues,
            diagramContainsStatements = diagramContainsStatements,
            file,
            spin,
            verificationArguments,
            environment
        )
    }

    private val opts: SimulationConfigurationOptions get() = this.options as SimulationConfigurationOptions

    var modelFile
        get() = opts.modelFile ?: ""
        set(value) {
            opts.modelFile = value
        }
    var trailFile
        get() = opts.trailFile ?: ""
        set(value) {
            opts.trailFile = value
        }
    var mode
        get() = opts.mode
        set(value) {
            opts.mode = value
        }
    var trackDataValues
        get() = opts.trackDataValues
        set(value) {
            opts.trackDataValues = value
        }
    var diagramContainsStatements
        get() = opts.diagramContainsStatements
        set(value) {
            opts.diagramContainsStatements = value
        }
    var randomSeed
        get() = opts.randomSeed ?: ""
        set(value) {
            opts.randomSeed = value
        }
    var initialStepsSkipped
        get() = opts.initialStepsSkipped
        set(value) {
            opts.initialStepsSkipped = value
        }
    var maxSteps
        get() = opts.maxSteps
        set(value) {
            opts.maxSteps = value
        }
    var fullChannelBlocksOrLoses
        get() = opts.fullChannelBlocksOrLoses
        set(value) {
            opts.fullChannelBlocksOrLoses = value
        }
    var extraSimulationOptions: List<String>
        get() = opts.extraSimulationOptions
        set(value) {
            opts.extraSimulationOptions = value.toMutableList()
        }
    var inheritGlobalSpinOptions
        get() = opts.inheritGlobalSpinOptions
        set(value) {
            opts.inheritGlobalSpinOptions = value
        }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration?> {
        return SimulationSettingsEditor(project)
    }
}

