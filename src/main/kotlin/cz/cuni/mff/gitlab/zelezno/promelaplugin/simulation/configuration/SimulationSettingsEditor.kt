package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.configuration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.MutableCollectionComboBoxModel
import com.intellij.ui.RawCommandLineEditor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.dsl.builder.*
import com.intellij.util.execution.ParametersListUtil
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFileType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import javax.swing.JPanel

/**
 * Settings editor for Promela simulation run configurations [SimulationRunConfiguration].
 * @param project the current project, used for file selectors
 */
class SimulationSettingsEditor(project: Project) : SettingsEditor<SimulationRunConfiguration>() {
    private val myPanel: DialogPanel
    private var myProject: Project = project

    private data class Properties(
        var modelFile: String = "",

        var mode: SimulationMode = SimulationMode.RANDOM,

        var randomSeed: String = "123",
        var trailFile: String = "",
        var initialStepsSkipped: Int = 0,
        var maxSteps: Int = 0,
        var trackDataValues: Boolean = false,
        var diagramContainsStatements: Boolean = false,

        /**
         * True if a full channel should block
         */
        var fullChannelBlocksOrLoses: Boolean = true,

        var inheritGlobalSpinOptions: Boolean = true,

        var extraSimulationOptions: String = "",
    )

    private val modules = MutableCollectionComboBoxModel<Module>()

    private var myProperties = Properties()

    init {
        myPanel = panel {
            row {
                comboBox(modules).resizableColumn()
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.singleFile().withExtensionFilter(PromelaFileType), myProject
                ).bindText(myProperties::modelFile).resizableColumn()
            }
            buttonsGroup(PromelaBundle.message("simulation.configuration.options.mode")) {
                row {
                    radioButton(
                        PromelaBundle.message("simulation.configuration.options.mode.random"),
                        SimulationMode.RANDOM
                    )
                    textField().bindText(myProperties::randomSeed)
                        .label(PromelaBundle.message("simulation.configuration.options.mode.random.seed"))
                }
                row {
                    radioButton(
                        PromelaBundle.message("simulation.configuration.options.mode.guided"),
                        SimulationMode.GUIDED
                    )
                    textFieldWithBrowseButton(
                        FileChooserDescriptorFactory.singleFile().withExtensionFilter("trail"), myProject
                    ).bindText(myProperties::trailFile)
                        .label(PromelaBundle.message("simulation.configuration.options.mode.guided.trail"))
                }
                row {
                    radioButton(
                        PromelaBundle.message("simulation.configuration.options.mode.interactive"),
                        SimulationMode.INTERACTIVE
                    )
                }
            }.bind(myProperties::mode)
            row {
                checkBox(PromelaBundle.message("simulation.configuration.options.trackDataValues")).bindSelected(
                    myProperties::trackDataValues
                )
            }
            row {
                checkBox(PromelaBundle.message("simulation.configuration.options.diagramContainsStatements")).bindSelected(
                    myProperties::diagramContainsStatements
                )
            }
            row {
                spinner(0.rangeTo(Int.MAX_VALUE)).bindIntValue(myProperties::initialStepsSkipped)
                    .label(PromelaBundle.message("simulation.configuration.options.initialStepsSkipped"))
            }
            row {
                spinner(1.rangeTo(Int.MAX_VALUE)).bindIntValue(myProperties::maxSteps)
                    .label(PromelaBundle.message("simulation.configuration.options.maxSteps"))
            }
            row {
                checkBox(PromelaBundle.message("simulation.configuration.options.inheritGlobalSpinOptions")).bindSelected(
                    myProperties::inheritGlobalSpinOptions
                )
            }
            row {
                cell(RawCommandLineEditor()).apply {
                    label(JBLabel(PromelaBundle.message("simulation.configuration.options.extraRuntimeOptions")))
                }.bind(
                    { it.text }, { it, v -> it.text = v }, myProperties::extraSimulationOptions.toMutableProperty()
                )
            }

            collapsibleGroup(PromelaBundle.message("simulation.configuration.options.fullChannel")) {
                buttonsGroup {
                    row {
                        radioButton(PromelaBundle.message("simulation.configuration.options.fullChannelBlocks"), true)
                    }
                    row {
                        radioButton(PromelaBundle.message("simulation.configuration.options.fullChannelLoses"), false)
                    }
                }.bind(myProperties::fullChannelBlocksOrLoses)
            }
        }
    }

    override fun resetEditorFrom(configuration: SimulationRunConfiguration) {
        myProperties.modelFile = configuration.modelFile
        myProperties.trailFile = configuration.trailFile
        myProperties.extraSimulationOptions = ParametersListUtil.join(configuration.extraSimulationOptions)
        myProperties.inheritGlobalSpinOptions = configuration.inheritGlobalSpinOptions
        myProperties.mode = configuration.mode
        myProperties.trackDataValues = configuration.trackDataValues
        myProperties.diagramContainsStatements = configuration.diagramContainsStatements
        myProperties.randomSeed = configuration.randomSeed
        myProperties.initialStepsSkipped = configuration.initialStepsSkipped
        myProperties.maxSteps = configuration.maxSteps
        myProperties.fullChannelBlocksOrLoses = configuration.fullChannelBlocksOrLoses
        modules.removeAll()
        modules.add(configuration.validModules.toList())
        modules.selectedItem = configuration.configurationModule.module
        myPanel.reset()
    }

    override fun applyEditorTo(configuration: SimulationRunConfiguration) {
        myPanel.apply()
        configuration.modelFile = myProperties.modelFile
        configuration.trailFile = myProperties.trailFile
        configuration.extraSimulationOptions = ParametersListUtil.parse(myProperties.extraSimulationOptions)
        configuration.inheritGlobalSpinOptions = myProperties.inheritGlobalSpinOptions
        configuration.mode = myProperties.mode
        configuration.trackDataValues = myProperties.trackDataValues
        configuration.diagramContainsStatements = myProperties.diagramContainsStatements
        configuration.randomSeed = myProperties.randomSeed
        configuration.initialStepsSkipped = myProperties.initialStepsSkipped
        configuration.maxSteps = myProperties.maxSteps
        configuration.fullChannelBlocksOrLoses = myProperties.fullChannelBlocksOrLoses
        configuration.setModule(modules.selected)
    }

    override fun createEditor(): JPanel {
        return myPanel
    }
}