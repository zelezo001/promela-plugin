package cz.cuni.mff.gitlab.zelezno.promelaplugin.verification

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
 * A settings editor for managing and configuring verification settings [VerificationRunConfiguration].
 * @param project used for file selection
 */
class VerificationSettingsEditor(project: Project) : SettingsEditor<VerificationRunConfiguration>() {
    private val myPanel: DialogPanel
    private var myProject: Project = project

    // helper class allowing binding of properties to form elements
    private data class Properties(
        var modelName: String = "",

        var modelFile: String = "",

        var safety: Boolean = true,
        var invalidEndStates: Boolean = true,
        var assertionViolations: Boolean = true,
        var xrXsAssertions: Boolean = true,

        var nonProgressCycles: Boolean = false,
        var acceptanceCycles: Boolean = false,
        var enforceWeakFairnessConstraint: Boolean = false,

        var computeVariableRanges: Boolean = false,

        var addComplexityProfiling: Boolean = false,

        var saveAllErrorTrails: Boolean = false,

        var stopAtErrors: Boolean = true,
        var stopAtNthError: Int = 1,

        var useNeverClaim: Boolean = false,
        var claimName: String = "",

        var exhaustive: Boolean = true,
        var hashCompact: Boolean = false,
        var bitstate: Boolean = false,

        var minimizedAutomata: Boolean = false,
        var collapseCompression: Boolean = false,

        /**
         * true for DFS
         * false for BFS
         */
        var searchMode: Boolean = true,

        var boundedContextSwitching: Boolean = false,
        var iterativeSearchForShortTrail: Boolean = false,
        var boundedContextSwitchingBound: Int = 0,

        var partialOrderReduction: Boolean = true,

        var reportUnreachableCode: Boolean = true,

        /**
         * True if a full channel should block
         */
        var fullChannelBlocksOrLoses: Boolean = true,

        var physicalMemoryInMegabytes: Int = 1024,

        var estimatedSpaceSize: Int = 1000,

        var maximumSearchDepth: Int = 10_000,

        var bitstateModeHashFunctionsNumber: Int = 3,

        var minimizedAutomatonSize: Int = 100,

        var modelGenerationOptions: String = "",

        var modelCompilationOptions: String = "",

        var extraRuntimeOptions: String = "",

        var inheritGlobalSpinOptions: Boolean = true,

        var inheritGlobalCompilationOptions: Boolean = true,
    )

    private val modules = MutableCollectionComboBoxModel<Module>()

    private val properties = Properties()

    init {
        // Grouping taken from iSpin
        myPanel = panel {
            row {
                comboBox(modules).resizableColumn()
                textFieldWithBrowseButton(
                    FileChooserDescriptorFactory.singleFile().withExtensionFilter(PromelaFileType), myProject
                ).bindText(properties::modelFile).resizableColumn()
            }
            buttonsGroup {
                collapsibleGroup(PromelaBundle.message("verification.configuration.options.safety")) {
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.safety")).bindSelected(
                            properties::safety
                        )
                    }
                    row {
                        checkBox(PromelaBundle.message("verification.configuration.options.invalidEndStates")).bindSelected(
                            properties::invalidEndStates
                        )
                    }
                    row {
                        checkBox(PromelaBundle.message("verification.configuration.options.assertionViolations")).bindSelected(
                            properties::assertionViolations
                        )
                    }
                    row {
                        checkBox(PromelaBundle.message("verification.configuration.options.xrXsAssertions")).bindSelected(
                            properties::xrXsAssertions
                        )
                    }
                }
                collapsibleGroup(PromelaBundle.message("verification.configuration.options.liveness")) {
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.nonProgressCycles")).bindSelected(
                            properties::nonProgressCycles
                        )
                    }
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.acceptanceCycles")).bindSelected(
                            properties::acceptanceCycles
                        )
                    }
                    row {
                        checkBox(PromelaBundle.message("verification.configuration.options.enforceWeakFairnessConstraint")).bindSelected(
                            properties::enforceWeakFairnessConstraint
                        )
                    }
                }

            }
            collapsibleGroup(PromelaBundle.message("verification.configuration.options.errorTrappingOptions")) {
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.computeVariableRanges")).bindSelected(
                        properties::computeVariableRanges
                    )
                }
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.addComplexityProfiling")).bindSelected(
                        properties::addComplexityProfiling
                    )
                }
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.saveAllErrorTrails")).bindSelected(
                        properties::saveAllErrorTrails
                    )
                }
                separator()
                buttonsGroup {
                    row {
                        radioButton(
                            PromelaBundle.message("verification.configuration.options.stopAtErrors.zero"),
                            false
                        )
                    }
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.stopAtErrors.some"), true)
                        spinner(1.rangeTo(Int.MAX_VALUE))
                    }
                }.bind(properties::stopAtErrors)
            }
            collapsibleGroup(PromelaBundle.message("verification.configuration.options.storageMode")) {
                buttonsGroup {
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.exhaustive")).bindSelected(
                            properties::exhaustive
                        )
                    }
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.hashCompact")).bindSelected(
                            properties::hashCompact
                        )
                    }
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.bitstate")).bindSelected(
                            properties::bitstate
                        )
                    }
                }
                separator()
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.minimizedAutomata")).bindSelected(
                        properties::minimizedAutomata
                    )
                }
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.collapseCompression")).bindSelected(
                        properties::collapseCompression
                    )
                }

            }

            collapsibleGroup(PromelaBundle.message("verification.configuration.options.neverClaims")) {
                buttonsGroup {
                    row {
                        radioButton(
                            PromelaBundle.message("verification.configuration.options.useNeverClaim.dontUse"),
                            false
                        )
                    }
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.useNeverClaim.use"), true)
                    }
                    indent {
                        row {
                            textField().label(PromelaBundle.message("verification.configuration.options.claimName"))
                                .bindText(properties::claimName)
                        }
                    }
                }.bind(properties::useNeverClaim)
            }


            collapsibleGroup(PromelaBundle.message("verification.configuration.options.searchMode")) {
                buttonsGroup {
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.searchMode.dfs"), true)
                    }
                    indent {
                        row {
                            checkBox(PromelaBundle.message("verification.configuration.options.boundedContextSwitching")).bindSelected(
                                properties::boundedContextSwitching
                            )
                            spinner(0.rangeTo(Int.MAX_VALUE)).apply {
                                label(JBLabel(PromelaBundle.message("verification.configuration.options.boundedContextSwitchingBound")))
                            }.bindIntValue(properties::boundedContextSwitchingBound)
                        }
                        row {
                            checkBox(PromelaBundle.message("verification.configuration.options.iterativeSearchForShortTrail")).bindSelected(
                                properties::iterativeSearchForShortTrail
                            )
                        }
                    }
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.searchMode.bfs"), false)
                    }
                }.bind(properties::searchMode)
                separator()
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.partialOrderReduction")).bindSelected(
                        properties::partialOrderReduction
                    )
                }
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.reportUnreachableCode")).bindSelected(
                        properties::reportUnreachableCode
                    )
                }
            }
            collapsibleGroup(PromelaBundle.message("verification.configuration.options.fullChannel")) {
                buttonsGroup {
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.fullChannelBlocks"), true)
                    }
                    row {
                        radioButton(PromelaBundle.message("verification.configuration.options.fullChannelLoses"), false)
                    }
                }.bind(properties::fullChannelBlocksOrLoses)
            }
            collapsibleGroup(PromelaBundle.message("verification.configuration.options.advancedParameters")) {
                row {
                    spinner(1.rangeTo(Int.MAX_VALUE)).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.memory")).apply {
                            comment(
                                PromelaBundle.message("verification.configuration.options.memory.hint"),
                            )
                        })
                    }.gap(RightGap.SMALL).bindIntValue(properties::physicalMemoryInMegabytes)
                    label(PromelaBundle.message("verification.configuration.options.memory.unit"))
                }
                row {
                    spinner(1.rangeTo(Int.MAX_VALUE)).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.estimatedStateSpace")).apply {
                            comment(
                                PromelaBundle.message("verification.configuration.options.estimatedStateSpace.hint")
                            )
                        })
                    }.bindIntValue(properties::estimatedSpaceSize)
                }
                row {
                    spinner(1.rangeTo(Int.MAX_VALUE)).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.maxSearchDepth")))
                            .comment(
                                PromelaBundle.message("verification.configuration.options.maxSearchDepth.hint")
                        )
                    }.bindIntValue(properties::maximumSearchDepth)
                }
                row {
                    spinner(1.rangeTo(Int.MAX_VALUE)).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.numberOfHashFunctionInBitstateMode"))).comment(
                            PromelaBundle.message("verification.configuration.options.numberOfHashFunctionInBitstateMode.hint")
                        ).bindIntValue(properties::bitstateModeHashFunctionsNumber)
                    }
                }
                row {
                    spinner(1.rangeTo(Int.MAX_VALUE)).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.automatonSize"))).comment(
                            PromelaBundle.message("verification.configuration.options.automatonSize.hint")
                        ).bindIntValue(properties::minimizedAutomatonSize)
                    }
                }
                row {
                    cell(RawCommandLineEditor()).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.modelGenerationOptions")))
                    }
                        .bind(
                            { it.text },
                            { it, v -> it.text = v },
                            properties::modelGenerationOptions.toMutableProperty()
                        )
                }
                row {
                    cell(RawCommandLineEditor()).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.modelCompilationOptions")))
                    }
                        .bind(
                            { it.text },
                            { it, v -> it.text = v },
                            properties::modelCompilationOptions.toMutableProperty()
                        )
                }
                row {
                    cell(RawCommandLineEditor()).apply {
                        label(JBLabel(PromelaBundle.message("verification.configuration.options.extraRuntimeOptions")))
                    }.bind(
                        { it.text }, { it, v -> it.text = v }, properties::extraRuntimeOptions.toMutableProperty()
                    )
                }
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.inheritGlobalSpinOptions")).bindSelected(
                        properties::inheritGlobalSpinOptions
                    )
                }
                row {
                    checkBox(PromelaBundle.message("verification.configuration.options.inheritGlobalCompilationOptions")).bindSelected(
                        properties::inheritGlobalCompilationOptions
                    )
                }
            }
        }
    }

    override fun resetEditorFrom(configuration: VerificationRunConfiguration) {
        properties.modelFile = configuration.modelFile
        properties.safety = configuration.safety
        properties.invalidEndStates = configuration.invalidEndStates
        properties.assertionViolations = configuration.assertionViolations
        properties.xrXsAssertions = configuration.xrXsAssertions
        properties.nonProgressCycles = configuration.nonProgressCycles
        properties.acceptanceCycles = configuration.acceptanceCycles
        properties.enforceWeakFairnessConstraint = configuration.enforceWeakFairnessConstraint
        properties.computeVariableRanges = configuration.computeVariableRanges
        properties.addComplexityProfiling = configuration.addComplexityProfiling
        properties.saveAllErrorTrails = configuration.saveAllErrorTrails
        properties.stopAtErrors = configuration.stopAtErrors
        properties.stopAtNthError = configuration.stopAtNthError
        properties.useNeverClaim = configuration.useNeverClaim
        properties.claimName = configuration.claimName
        properties.exhaustive = configuration.exhaustive
        properties.hashCompact = configuration.hashCompact
        properties.bitstate = configuration.bitstate
        properties.minimizedAutomata = configuration.minimizedAutomata
        properties.collapseCompression = configuration.collapseCompression
        properties.searchMode = configuration.searchMode
        properties.boundedContextSwitching = configuration.boundedContextSwitching
        properties.iterativeSearchForShortTrail = configuration.iterativeSearchForShortTrail
        properties.boundedContextSwitchingBound = configuration.boundedContextSwitchingBound
        properties.partialOrderReduction = configuration.partialOrderReduction
        properties.reportUnreachableCode = configuration.reportUnreachableCode
        properties.fullChannelBlocksOrLoses = configuration.fullChannelBlocksOrLoses
        properties.physicalMemoryInMegabytes = configuration.physicalMemoryInMegabytes
        properties.estimatedSpaceSize = configuration.estimatedSpaceSize
        properties.maximumSearchDepth = configuration.maximumSearchDepth
        properties.bitstateModeHashFunctionsNumber = configuration.bitstateModeHashFunctionsNumber
        properties.minimizedAutomatonSize = configuration.minimizedAutomatonSize
        properties.modelGenerationOptions = ParametersListUtil.join(configuration.modelGenerationOptions)
        properties.modelCompilationOptions = ParametersListUtil.join(configuration.modelCompilationOptions)
        properties.extraRuntimeOptions = ParametersListUtil.join(configuration.extraRuntimeOptions)
        properties.inheritGlobalSpinOptions = configuration.inheritGlobalSpinOptions
        properties.inheritGlobalCompilationOptions = configuration.inheritGlobalCompilationOptions
        modules.removeAll()
        modules.add(configuration.validModules.toList())
        modules.selectedItem = configuration.configurationModule.module
        myPanel.reset()
    }

    override fun applyEditorTo(configuration: VerificationRunConfiguration) {
        myPanel.apply()
        configuration.modelFile = properties.modelFile
        configuration.safety = properties.safety
        configuration.invalidEndStates = properties.invalidEndStates
        configuration.assertionViolations = properties.assertionViolations
        configuration.xrXsAssertions = properties.xrXsAssertions
        configuration.nonProgressCycles = properties.nonProgressCycles
        configuration.acceptanceCycles = properties.acceptanceCycles
        configuration.enforceWeakFairnessConstraint = properties.enforceWeakFairnessConstraint
        configuration.computeVariableRanges = properties.computeVariableRanges
        configuration.addComplexityProfiling = properties.addComplexityProfiling
        configuration.saveAllErrorTrails = properties.saveAllErrorTrails
        configuration.stopAtErrors = properties.stopAtErrors
        configuration.stopAtNthError = properties.stopAtNthError
        configuration.useNeverClaim = properties.useNeverClaim
        configuration.claimName = properties.claimName.trim()
        configuration.exhaustive = properties.exhaustive
        configuration.hashCompact = properties.hashCompact
        configuration.bitstate = properties.bitstate
        configuration.minimizedAutomata = properties.minimizedAutomata
        configuration.collapseCompression = properties.collapseCompression
        configuration.searchMode = properties.searchMode
        configuration.boundedContextSwitching = properties.boundedContextSwitching
        configuration.iterativeSearchForShortTrail = properties.iterativeSearchForShortTrail
        configuration.boundedContextSwitchingBound = properties.boundedContextSwitchingBound
        configuration.partialOrderReduction = properties.partialOrderReduction
        configuration.reportUnreachableCode = properties.reportUnreachableCode
        configuration.fullChannelBlocksOrLoses = properties.fullChannelBlocksOrLoses
        configuration.physicalMemoryInMegabytes = properties.physicalMemoryInMegabytes
        configuration.estimatedSpaceSize = properties.estimatedSpaceSize
        configuration.maximumSearchDepth = properties.maximumSearchDepth
        configuration.bitstateModeHashFunctionsNumber = properties.bitstateModeHashFunctionsNumber
        configuration.minimizedAutomatonSize = properties.minimizedAutomatonSize
        configuration.modelGenerationOptions = ParametersListUtil.parse(properties.modelGenerationOptions)
        configuration.modelCompilationOptions = ParametersListUtil.parse(properties.modelCompilationOptions)
        configuration.extraRuntimeOptions = ParametersListUtil.parse(properties.extraRuntimeOptions)
        configuration.inheritGlobalSpinOptions = properties.inheritGlobalSpinOptions
        configuration.inheritGlobalCompilationOptions = properties.inheritGlobalCompilationOptions
        configuration.setModule(modules.selected)
    }

    override fun createEditor(): JPanel {
        return myPanel
    }
}