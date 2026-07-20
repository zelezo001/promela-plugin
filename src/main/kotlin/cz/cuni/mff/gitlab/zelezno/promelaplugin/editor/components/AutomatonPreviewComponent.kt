package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DataSink
import com.intellij.openapi.actionSystem.UiDataProvider
import com.intellij.openapi.observable.util.whenListChanged
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.dsl.builder.panel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.AutomatonPreviewEditor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.Diagram
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.action.getAutomatonActionGroup
import org.jetbrains.annotations.Nls
import java.awt.BorderLayout
import java.awt.CardLayout
import javax.swing.JPanel

/**
 * Class representing all possible states of [AutomatonPreviewComponent] component.
 */
sealed class AutomatonPreviewComponentState

/**
 * State representing that diagrams are being reloaded.
 */
class ReloadingDiagramsState : AutomatonPreviewComponentState()

/**
 * State representing that diagrams are being viewed.
 * @param diagrams the list of diagrams to view
 */
class ViewDiagramsState(
    val diagrams: List<Diagram>
) : AutomatonPreviewComponentState()

/**
 * State representing that a message is being displayed.
 * @param message the message to display
 * @param error true if the message represents an error
 */
class ViewMessageState(
    @param:Nls val message: String, val error: Boolean
) : AutomatonPreviewComponentState()

/**
 * Main Component for previewing state automaton diagrams.
 * Handles different states of the editor [AutomatonPreviewComponentState]
 * @param editor the editor this component is associated with
 */
class AutomatonPreviewComponent(val editor: AutomatonPreviewEditor) : JPanel(BorderLayout()), UiDataProvider,
    Disposable {
    companion object {
        private const val IMAGE_PANEL = "image"
        private const val MESSAGE_PANEL = "message"
    }

    override fun uiDataSnapshot(sink: DataSink) {
        sink.set(AutomatonPreviewEditor.DATA_KEY, editor)
        sink.set(ZoomableComponent.DATA_KEY, diagramPanel)
    }

    private val bodyLayout = CardLayout()
    private val bodyPanel = JPanel(bodyLayout)
    private val diagramPanel = DiagramPanel().apply {
        Disposer.register(this@AutomatonPreviewComponent, this)
    }
    private val loadingPanel = JBLoadingPanel(BorderLayout(), this, -1)
    private val messagePanel = MessagePanel()

    override fun dispose() {
        removeAll()
    }

    private val messageModel = messagePanel.model
    private val diagramsModel = CollectionComboBoxModel<Diagram>()

    /**
     * Sets the state of the component.
     * @param state the new state to set
     */
    fun setState(state: AutomatonPreviewComponentState) {
        diagramPanel.zoomModel.isZoomEnabled = false
        val panel = when (state) {
            is ViewDiagramsState -> {
                loadingPanel.stopLoading()
                diagramsModel.replaceAll(state.diagrams)
                diagramsModel.selectedItem = state.diagrams.firstOrNull()
                diagramPanel.zoomModel.isZoomEnabled = diagramsModel.selectedItem != null

                IMAGE_PANEL
            }

            is ReloadingDiagramsState -> {
                loadingPanel.startLoading()
                diagramsModel.selectedItem = null
                diagramsModel.removeAll()

                IMAGE_PANEL
            }

            is ViewMessageState -> {
                messageModel.setMessage(state.message, state.error)

                MESSAGE_PANEL
            }
        }
        bodyLayout.show(bodyPanel, panel)
    }

    private val toolbar = ActionManager.getInstance()
        .let { it.createActionToolbar(ActionPlaces.TOOLBAR, it.getAutomatonActionGroup(), true) }

    init {
        toolbar.targetComponent = this

        val control = panel {
            row {
                cell(
                    toolbar.component
                )
                comboBox(diagramsModel)
            }
        }

        // grab focus on hover so actions triggered by keyboard work correctly without clicking on this component
        addMouseListener(FocusRequester(this))

        diagramsModel.whenListChanged(this) { _ ->
            diagramPanel.svgModel.svg = run {
                diagramsModel.selectedItem?.let {
                    (diagramsModel.selectedItem as Diagram).svg
                }
            }
        }

        add(control, BorderLayout.NORTH)

        loadingPanel.add(diagramPanel, BorderLayout.CENTER)
        bodyPanel.add(loadingPanel, IMAGE_PANEL)
        bodyPanel.add(messagePanel, MESSAGE_PANEL)
        add(bodyPanel, BorderLayout.CENTER)
    }
}