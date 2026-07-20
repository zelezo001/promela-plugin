package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.facet

import com.intellij.facet.ui.FacetEditorTab
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.RawCommandLineEditor
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.execution.ParametersListUtil
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * An editor tab for the Promela facet configuration.
 *
 * @param config the configuration to be edited.
 */
internal class PromelaFacetEditorTab(private val config: PromelaFacetConfiguration) :
    FacetEditorTab() {
    /**
     * A UI component for editing Promela compiler options.
     */
    class PromelaCompilerOptionsComponent {

        /**
         * The main panel of the component.
         */
        val mainPanel: JPanel

        private var cCompilerOptionsComponent =
            RawCommandLineEditor()

        /**
         * The C compiler options.
         */
        var cCompilerOptions: String
            get() = cCompilerOptionsComponent.text
            set(value) {
                cCompilerOptionsComponent.text = value
            }

        private var spinOptionsComponent =
            RawCommandLineEditor()

        /**
         * The Spin compiler options.
         */
        var spinCompilerOptions: String
            get() = spinOptionsComponent.text
            set(value) {
                spinOptionsComponent.text = value
            }

        init {
            mainPanel = panel {
                row {
                    cell(cCompilerOptionsComponent).apply { label("C compiler options") }
                }
                row {
                    cell(spinOptionsComponent).apply { label("Spin options") }
                }
            }
        }
    }

    private lateinit var component: PromelaCompilerOptionsComponent

    /**
     * Returns the display name of the facet editor tab.
     *
     * @return the display name.
     */
    override fun getDisplayName(): @NlsContexts.ConfigurableName String {
        return PromelaBundle.message("module.facet.name")
    }

    /**
     * Checks if the configuration has been modified.
     *
     * @return true if modified, false otherwise.
     */
    override fun isModified(): Boolean {
        return argumentsString(config.cCompilerOptions) != component.cCompilerOptions ||
                argumentsString(config.spinOptions) != component.spinCompilerOptions
    }

    /**
     * Creates the UI component for the facet editor tab.
     *
     * @return the created component.
     */
    override fun createComponent(): JComponent {
        component = PromelaCompilerOptionsComponent()
        component.cCompilerOptions = argumentsString(config.cCompilerOptions)
        component.spinCompilerOptions = argumentsString(config.spinOptions)

        return component.mainPanel
    }

    /**
     * Applies the changes made in the UI to the configuration.
     */
    override fun apply() {
        config.cCompilerOptions = arguments(component.cCompilerOptions)
        config.spinOptions = arguments(component.spinCompilerOptions)
    }
}

private fun argumentsString(arguments: List<String>): String {
    return ParametersListUtil.join(arguments)
}

private fun arguments(argumentsText: String): List<String> {
    return ParametersListUtil.parse(argumentsText)
}