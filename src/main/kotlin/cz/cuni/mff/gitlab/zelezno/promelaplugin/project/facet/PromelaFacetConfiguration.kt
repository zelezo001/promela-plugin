package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.facet

import com.intellij.facet.FacetConfiguration
import com.intellij.facet.ui.FacetEditorContext
import com.intellij.facet.ui.FacetEditorTab
import com.intellij.facet.ui.FacetValidatorsManager
import com.intellij.openapi.components.SerializablePersistentStateComponent
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.PromelaFacetState

/**
 * Configuration for the [PromelaFacet].
 */
class PromelaFacetConfiguration :
    SerializablePersistentStateComponent<PromelaFacetState>(PromelaFacetState()),
    FacetConfiguration {
    override fun createEditorTabs(
        editorContext: FacetEditorContext?,
        validatorsManager: FacetValidatorsManager?
    ): Array<out FacetEditorTab?>? {
        return arrayOf(PromelaFacetEditorTab(this))
    }

    /**
     * The C compiler options.
     */
    var cCompilerOptions: List<String>
        get() = state.cCompilerOptions
        set(value) {
            updateState {
                it.copy(cCompilerOptions = value)
            }
        }

    /**
     * The Spin tool options.
     */
    var spinOptions: List<String>
        get() = state.spinOptions
        set(value) {
            updateState {
                it.copy(spinOptions = value)
            }
        }
}