package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.configuration

import com.intellij.execution.configurations.ModuleBasedConfigurationOptions
import com.intellij.util.xmlb.annotations.Tag
import com.intellij.util.xmlb.annotations.XCollection

/**
 * Different modes of Promela simulation.
 */
enum class SimulationMode {
    /**
     * Random simulation mode.
     */
    RANDOM,

    /**
     * Interactive simulation mode.
     */
    INTERACTIVE,

    /**
     * Guided simulation mode using a trail file.
     */
    GUIDED
}

/**
 * Options for Promela simulation run configuration.
 */
class SimulationConfigurationOptions : ModuleBasedConfigurationOptions() {
    @get:Tag("modelFile")
    var modelFile by string("")

    @get:Tag("mode")
    var mode by enum<SimulationMode>(SimulationMode.RANDOM)

    @get:Tag("randomSeed")
    var randomSeed by string("123")

    @get:Tag("trailFile")
    var trailFile by string("")


    @get:Tag("initialStepsSkipped")
    var initialStepsSkipped by property(0)

    @get:Tag("maxSteps")
    var maxSteps by property(0)

    @get:Tag("trackDataValues")
    var trackDataValues by property(false)

    @get:Tag("diagramContainsStatements")
    var diagramContainsStatements by property(false)

    /**
     * True if a full channel should block
     */
    @get:Tag("fullChannelBlocksOrLoses")
    var fullChannelBlocksOrLoses by property(true)


    @get:Tag("extraSimulationOptions")
    @get:XCollection(
        elementName = "arg", elementTypes = [String::class]
    )
    var extraSimulationOptions by list<String>()

    @get:Tag("inheritGlobalSpinOptions")
    var inheritGlobalSpinOptions by property(true)
}