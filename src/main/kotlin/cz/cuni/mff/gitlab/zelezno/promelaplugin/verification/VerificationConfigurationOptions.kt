package cz.cuni.mff.gitlab.zelezno.promelaplugin.verification

import com.intellij.execution.configurations.ModuleBasedConfigurationOptions
import com.intellij.util.xmlb.annotations.Tag
import com.intellij.util.xmlb.annotations.XCollection

/**
 * Options for Promela verification run configuration.
 */
class VerificationConfigurationOptions() : ModuleBasedConfigurationOptions() {
    @get:Tag("modelFile")
    var modelFile by string("")

    @get:Tag("safety")
    var safety by property(true)

    @get:Tag("invalidEndStates")
    var invalidEndStates by property(true)

    @get:Tag("assertionViolations")
    var testAssertionViolations by property(true)

    @get:Tag("xrXsAssertions")
    var xrXsAssertions by property(true)

    @get:Tag("nonProgressCycles")
    var nonProgressCycles by property(false)

    @get:Tag("acceptanceCycles")
    var acceptanceCycles by property(false)

    @get:Tag("enforceWeakFairnessConstraint")
    var enforceWeakFairnessConstraint by property(false)


    @get:Tag("computeVariableRanges")
    var computeVariableRanges by property(false)

    @get:Tag("addComplexityProfiling")
    var addComplexityProfiling by property(false)

    @get:Tag("saveAllErrorTrails")
    var saveAllErrorTrails by property(false)

    @get:Tag("stopAtErrors")
    var stopAtErrors by property(true)

    @get:Tag("stopAtNthError")
    var stopAtNthError by property(1)

    @get:Tag("useNeverClaim")
    var useNeverClaim by property(false)

    @get:Tag("claimName")
    var claimName by string("")

    @get:Tag("exhaustive")
    var exhaustive by property(true)

    @get:Tag("hashCompact")
    var hashCompact by property(false)

    @get:Tag("bitstate")
    var bitstate by property(false)

    @get:Tag("minimizedAutomata")
    var minimizedAutomata by property(false)

    @get:Tag("collapseCompression")
    var collapseCompression by property(false)

    /**
     * true for DFS
     * false for BFS
     */
    @get:Tag("searchMode")
    var searchMode by property(true)

    @get:Tag("boundedContextSwitching")
    var boundedContextSwitching by property(false)

    @get:Tag("iterativeSearchForShortTrail")
    var iterativeSearchForShortTrail by property(false)

    @get:Tag("boundedContextSwitchingBound")
    var boundedContextSwitchingBound by property(0)

    @get:Tag("partialOrderReduction")
    var partialOrderReduction by property(true)

    @get:Tag("reportUnreachableCode")
    var reportUnreachableCode by property(true)

    /**
     * True if a full channel should block
     */
    @get:Tag("fullChannelBlocksOrLoses")
    var fullChannelBlocksOrLoses by property(true)

    @get:Tag("physicalMemoryInMegabytes")
    var physicalMemoryInMegabytes by property(1024)

    @get:Tag("estimatedSpaceSize")
    var estimatedSpaceSize by property(1000)

    @get:Tag("maximumSearchDepth")
    var maximumSearchDepth by property(10_000)

    @get:Tag("bitstateModeHashFunctionsNumber")
    var bitstateModeHashFunctionsNumber by property(3)

    @get:Tag("minimizedAutomatonSize")
    var minimizedAutomatonSize by property(100)

    @get:Tag("modelGenerationOptions")
    @get:XCollection(
        elementName = "arg", elementTypes = [String::class]
    )
    var modelGenerationOptions by list<String>()

    @get:Tag("modelCompilationOptions")
    @get:XCollection(
        elementName = "arg", elementTypes = [String::class]
    )
    var modelCompilationOptions by list<String>()

    @get:Tag("extraRuntimeOptions")
    @get:XCollection(
        elementName = "arg", elementTypes = [String::class]
    )
    var extraRuntimeOptions by list<String>()

    @get:Tag("inheritGlobalSpinOptions")
    var inheritGlobalSpinOptions by property(true)

    @get:Tag("inheritGlobalCompilationOptions")
    var inheritGlobalCompilationOptions by property(true)
}