package cz.cuni.mff.gitlab.zelezno.promelaplugin.verification

import com.intellij.execution.BeforeRunTask
import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionTarget
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.NotNullLazyValue
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.project.sdk.SpinSDK
import cz.cuni.mff.gitlab.zelezno.promelaplugin.spin.SpinCodeReferenceFilter
import org.jetbrains.annotations.Unmodifiable
import org.jetbrains.jps.util.SystemInfo
import java.io.File


/**
 * Type of the run configuration for Promela verification runs.
 */
class VerificationConfigurationType : SimpleConfigurationType(
    NAME,
    PromelaBundle.message("verification.configuration.name"),
    PromelaBundle.message("verification.configuration.description"),
    NotNullLazyValue.createValue {
        AllIcons.Nodes.Console
    }) {
    companion object {
        const val NAME = "promela.verification"
    }

    override fun getOptionsClass(): Class<out BaseState?>? {
        return VerificationConfigurationOptions::class.java
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return VerificationRunConfiguration(RunConfigurationModule(project), this, name)
    }
}

/**
 * Run configuration for running compiled models intended for model verification.
 * Model compilation is handled by [cz.cuni.mff.gitlab.zelezno.promelaplugin.build.PromelaBuildTargetScopeProvider]
 * and build dependency from [RunProfileWithCompileBeforeLaunchOption].
 * @param configurationModule the module for the configuration
 * @param factory the configuration factory
 * @param name the name of the configuration
 */
class VerificationRunConfiguration(
    configurationModule: RunConfigurationModule, factory: ConfigurationFactory, name: String?
) : ModuleBasedConfiguration<RunConfigurationModule, VerificationConfigurationOptions>(
    name, configurationModule, factory
), RunProfileWithCompileBeforeLaunchOption, TargetAwareRunProfile {

    override fun getValidModules(): @Unmodifiable Collection<Module> {
        return ModuleManager.getInstance(project).modules.toList()
            .filter { ModuleRootManager.getInstance(it).sdk?.sdkType is SpinSDK }
    }

    private fun resolveRunnableFile(): File? {
        val path = opts.modelFile ?: return null
        if (path.isEmpty()) return null
        val modelFile = File(path)
        val modelFileName = modelFile.nameWithoutExtension
        val parent = modelFile.absoluteFile.parentFile

        val withoutExtension = "${modelFileName}-${(name.toByteArray()).toHexString()}-pan"
        return if (SystemInfo.isWindows) {
            File(parent, "$withoutExtension.exe")
        } else {
            File(parent, withoutExtension)
        }
    }

    private fun verificationRuntimeArguments(): List<String> {
        val options = mutableListOf<String>()
        val add = { b: Boolean, flag: String -> if (b) options.add(flag) }

        add(nonProgressCycles, "-l")
        add(acceptanceCycles, "-a")

        add(!invalidEndStates, "-E")
        add(!assertionViolations, "-A")

        add(enforceWeakFairnessConstraint, "-f")

        add(claimName.isNotEmpty(), "-N")
        add(claimName.isNotEmpty(), claimName)

        add(bitstate, "-k${bitstateModeHashFunctionsNumber}")
        if (bitstate && estimatedSpaceSize != 0) {
            val highestBit = estimatedSpaceSize.takeHighestOneBit()
            val bitstateSize = highestBit.countTrailingZeroBits() + 1 // without +1 we would have last training zero
            options.add("-w${bitstateSize}")
        }
        add(boundedContextSwitching, "-L$boundedContextSwitchingBound")
        add(iterativeSearchForShortTrail, "-i")

        add(!reportUnreachableCode, "-n")

        add(!stopAtErrors, "-c0")
        add(stopAtErrors, "-c$stopAtNthError")
        add(saveAllErrorTrails, "-e")

        options.add("-m$maximumSearchDepth")

        options.addAll(extraRuntimeOptions)
        return options
    }

    /**
     * Creates and returns the execution state for the verification run.
     *
     * @param executor the executor instance responsible for initiating the run.
     * @param environment the execution environment containing relevant context and configuration.
     * @return the execution state as a [RunProfileState]
     * @throws ExecutionException if the runnable file cannot be resolved or is invalid.
     */
    override fun getState(
        executor: Executor, environment: ExecutionEnvironment
    ): RunProfileState {
        val file = resolveRunnableFile() ?: throw ExecutionException(
            PromelaBundle.message("verification.exception.missing-file")
        )
        val verificationArguments = verificationRuntimeArguments()
        return object : CommandLineState(environment) {
            init {
                addConsoleFilters(SpinCodeReferenceFilter(project))
            }

            override fun startProcess(): ProcessHandler {
                return OSProcessHandler(
                    GeneralCommandLine(
                        file.absolutePath, *verificationArguments.toTypedArray()
                    ).withWorkDirectory(file.parent)
                )
            }
        }
    }

    private val opts: VerificationConfigurationOptions get() = this.options as VerificationConfigurationOptions

    var modelFile
        get() = opts.modelFile ?: ""
        set(value) {
            opts.modelFile = value
        }
    var safety
        get() = opts.safety
        set(value) {
            opts.safety = value
        }
    var invalidEndStates
        get() = opts.invalidEndStates
        set(value) {
            opts.invalidEndStates = value
        }
    var assertionViolations
        get() = opts.testAssertionViolations
        set(value) {
            opts.testAssertionViolations = value
        }
    var xrXsAssertions
        get() = opts.xrXsAssertions
        set(value) {
            opts.xrXsAssertions = value
        }
    var nonProgressCycles
        get() = opts.nonProgressCycles
        set(value) {
            opts.nonProgressCycles = value
        }
    var acceptanceCycles
        get() = opts.acceptanceCycles
        set(value) {
            opts.acceptanceCycles = value
        }
    var enforceWeakFairnessConstraint
        get() = opts.enforceWeakFairnessConstraint
        set(value) {
            opts.enforceWeakFairnessConstraint = value
        }
    var computeVariableRanges
        get() = opts.computeVariableRanges
        set(value) {
            opts.computeVariableRanges = value
        }
    var addComplexityProfiling
        get() = opts.addComplexityProfiling
        set(value) {
            opts.addComplexityProfiling = value
        }
    var saveAllErrorTrails
        get() = opts.saveAllErrorTrails
        set(value) {
            opts.saveAllErrorTrails = value
        }
    var stopAtErrors
        get() = opts.stopAtErrors
        set(value) {
            opts.stopAtErrors = value
        }
    var stopAtNthError
        get() = opts.stopAtNthError
        set(value) {
            opts.stopAtNthError = value
        }
    var useNeverClaim
        get() = opts.useNeverClaim
        set(value) {
            opts.useNeverClaim = value
        }
    var claimName
        get() = opts.claimName ?: ""
        set(value) {
            opts.claimName = value
        }
    var exhaustive
        get() = opts.exhaustive
        set(value) {
            opts.exhaustive = value
        }
    var hashCompact
        get() = opts.hashCompact
        set(value) {
            opts.hashCompact = value
        }
    var bitstate
        get() = opts.bitstate
        set(value) {
            opts.bitstate = value
        }
    var minimizedAutomata
        get() = opts.minimizedAutomata
        set(value) {
            opts.minimizedAutomata = value
        }
    var collapseCompression
        get() = opts.collapseCompression
        set(value) {
            opts.collapseCompression = value
        }
    var searchMode
        get() = opts.searchMode
        set(value) {
            opts.searchMode = value
        }
    var boundedContextSwitching
        get() = opts.boundedContextSwitching
        set(value) {
            opts.boundedContextSwitching = value
        }
    var iterativeSearchForShortTrail
        get() = opts.iterativeSearchForShortTrail
        set(value) {
            opts.iterativeSearchForShortTrail = value
        }
    var boundedContextSwitchingBound
        get() = opts.boundedContextSwitchingBound
        set(value) {
            opts.boundedContextSwitchingBound = value
        }
    var partialOrderReduction
        get() = opts.partialOrderReduction
        set(value) {
            opts.partialOrderReduction = value
        }
    var reportUnreachableCode
        get() = opts.reportUnreachableCode
        set(value) {
            opts.reportUnreachableCode = value
        }
    var fullChannelBlocksOrLoses
        get() = opts.fullChannelBlocksOrLoses
        set(value) {
            opts.fullChannelBlocksOrLoses = value
        }
    var physicalMemoryInMegabytes
        get() = opts.physicalMemoryInMegabytes
        set(value) {
            opts.physicalMemoryInMegabytes = value
        }
    var estimatedSpaceSize
        get() = opts.estimatedSpaceSize
        set(value) {
            opts.estimatedSpaceSize = value
        }
    var maximumSearchDepth
        get() = opts.maximumSearchDepth
        set(value) {
            opts.maximumSearchDepth = value
        }
    var bitstateModeHashFunctionsNumber
        get() = opts.bitstateModeHashFunctionsNumber
        set(value) {
            opts.bitstateModeHashFunctionsNumber = value
        }
    var minimizedAutomatonSize
        get() = opts.minimizedAutomatonSize
        set(value) {
            opts.minimizedAutomatonSize = value
        }
    var modelGenerationOptions: List<String>
        get() = opts.modelGenerationOptions
        set(value) {
            opts.modelGenerationOptions = value.toMutableList()
        }

    var modelCompilationOptions: List<String>
        get() = opts.modelCompilationOptions
        set(value) {
            opts.modelCompilationOptions = value.toMutableList()
        }

    var extraRuntimeOptions: List<String>
        get() = opts.extraRuntimeOptions
        set(value) {
            opts.extraRuntimeOptions = value.toMutableList()
        }
    var inheritGlobalSpinOptions
        get() = opts.inheritGlobalSpinOptions
        set(value) {
            opts.inheritGlobalSpinOptions = value
        }
    var inheritGlobalCompilationOptions
        get() = opts.inheritGlobalCompilationOptions
        set(value) {
            opts.inheritGlobalCompilationOptions = value
        }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration?> {
        return VerificationSettingsEditor(project)
    }
}

