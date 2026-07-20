package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model

import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.annotations.OptionTag
import com.intellij.util.xmlb.annotations.Tag
import com.intellij.util.xmlb.annotations.XCollection
import org.jdom.Element
import org.jetbrains.jps.model.JpsElementChildRole
import org.jetbrains.jps.model.ex.JpsElementBase
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase
import org.jetbrains.jps.model.runConfiguration.JpsRunConfigurationType
import org.jetbrains.jps.model.serialization.runConfigurations.JpsRunConfigurationPropertiesSerializer

/**
 * Serializer for the Promela verification run configuration in JPS.
 */
class VerificationRunSerializer : JpsRunConfigurationPropertiesSerializer<VerificationRunProperties>(
    PromelaVerificationJpsRunConfigurationType, "promela.verification"
) {
    override fun loadProperties(runConfigurationTag: Element?): VerificationRunProperties {
        if (runConfigurationTag == null) {
            return VerificationRunProperties()
        }

        val properties = VerificationRunProperties()
        XmlSerializer.deserializeInto(properties, runConfigurationTag)
        return properties
    }
}

/**
 * The type of the Promela verification run configuration in JPS.
 */
object PromelaVerificationJpsRunConfigurationType : JpsRunConfigurationType<VerificationRunProperties?> {
    val ROLE: JpsElementChildRole<VerificationRunProperties?> =
        JpsElementChildRoleBase.create<VerificationRunProperties?>("promela.verification")

    override fun getPropertiesRole(): JpsElementChildRole<VerificationRunProperties?> {
        return ROLE
    }
}

/**
 * Properties of the Promela verification run configuration in JPS.
 */
data class VerificationRunProperties(

    @Tag("modelFile") @JvmField val modelFile: String = "",

    @OptionTag(tag = "module", valueAttribute = "name", nameAttribute = "") @JvmField val module: String = "",

    @Tag("safety") @JvmField val safety: Boolean = true,

    @Tag("invalidEndStates") @JvmField val invalidEndStates: Boolean = true,

    @Tag("assertionViolations") @JvmField val assertionViolations: Boolean = true,

    @Tag("xrXsAssertions") @JvmField val xrXsAssertions: Boolean = true,

    @Tag("nonProgressCycles") @JvmField val nonProgressCycles: Boolean = false,
    @Tag("acceptanceCycles") @JvmField val acceptanceCycles: Boolean = false,
    @Tag("enforceWeakFairnessConstraint") @JvmField val enforceWeakFairnessConstraint: Boolean = false,

    @Tag("computeVariableRanges") @JvmField val computeVariableRanges: Boolean = false,

    @Tag("addComplexityProfiling") @JvmField val addComplexityProfiling: Boolean = false,

    @Tag("saveAllErrorTrails") @JvmField val saveAllErrorTrails: Boolean = false,

    @Tag("stopAtErrors") @JvmField val stopAtErrors: Boolean = true,
    @Tag("stopAtNthError") @JvmField val stopAtNthError: Int = 1,

    @Tag("useNeverClaim") @JvmField val useNeverClaim: Boolean = false,
    @Tag("claimName") @JvmField val claimName: String = "",

    @Tag("exhaustive") @JvmField val exhaustive: Boolean = true,
    @Tag("hashCompact") @JvmField val hashCompact: Boolean = false,
    @Tag("bitstate") @JvmField val bitstate: Boolean = false,

    @Tag("minimizedAutomata") @JvmField val minimizedAutomata: Boolean = false,
    @Tag("collapseCompression") @JvmField val collapseCompression: Boolean = false,

    /**
     * true for DFS
     * false for BFS
     */
    @Tag("searchMode") @JvmField val searchMode: Boolean = true,

    @Tag("boundedContextSwitching") @JvmField val boundedContextSwitching: Boolean = false,
    @Tag("iterativeSearchForShortTrail") @JvmField val iterativeSearchForShortTrail: Boolean = false,
    @Tag("boundedContextSwitchingBound") @JvmField val boundedContextSwitchingBound: Int = 0,

    @Tag("partialOrderReduction") @JvmField val partialOrderReduction: Boolean = true,

    @Tag("reportUnreachableCode") @JvmField val reportUnreachableCode: Boolean = true,

    /**
     * True if a full channel should block
     */
    @Tag("fullChannelBlocksOrLoses") @JvmField val fullChannelBlocksOrLoses: Boolean = true,

    @Tag("physicalMemoryInMegabytes") @JvmField val physicalMemoryInMegabytes: Int = 1024,

    @Tag("estimatedSpaceSize") @JvmField val estimatedSpaceSize: Int = 1000,

    @Tag("maximumSearchDepth") @JvmField val maximumSearchDepth: Int = 10_000,

    @Tag("bitstateModeHashFunctionsNumber") @JvmField val bitstateModeHashFunctionsNumber: Int = 3,

    @Tag("minimizedAutomatonSize") @JvmField val minimizedAutomatonSize: Int = 100,

    @JvmField @Tag("modelGenerationOptions") @XCollection(
        elementName = "arg", elementTypes = [String::class]
    ) var modelGenerationOptions: List<String> = emptyList(),

    @JvmField @Tag("modelCompilationOptions") @XCollection(
        elementName = "arg", elementTypes = [String::class]
    ) var modelCompilationOptions: List<String> = emptyList(),

    @JvmField @Tag("extraRuntimeOptions") @XCollection(
        elementName = "arg", elementTypes = [String::class]
    ) var extraRuntimeOptions: List<String> = emptyList(),

    @Tag("inheritGlobalSpinOptions") @JvmField val inheritGlobalSpinOptions: Boolean = true,

    @Tag("inheritGlobalCompilationOptions") @JvmField val inheritGlobalCompilationOptions: Boolean = true,
) : JpsElementBase<VerificationRunProperties>();