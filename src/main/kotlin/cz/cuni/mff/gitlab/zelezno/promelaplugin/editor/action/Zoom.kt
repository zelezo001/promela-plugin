package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components.ZoomableComponent

/**
 * For easier accessing of the zoom model
 */
private val AnActionEvent.zoomModel get() = this.getData(ZoomableComponent.DATA_KEY)?.zoomModel

/**
 * Action to reset the zoom level to the default.
 */
class ResetZoomAction : AnAction(
    AllIcons.Actions.Restart
) {
    override fun actionPerformed(e: AnActionEvent) {
        e.zoomModel?.resetZoom()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.zoomModel?.isZoomEnabled ?: false
    }
}

/**
 * Action to zoom in on the diagram.
 */
class ZoomInAction : AnAction(
    AllIcons.Graph.ZoomIn
) {
    override fun actionPerformed(e: AnActionEvent) {
        e.zoomModel?.zoomIn()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.zoomModel?.canZoomIn() ?: false
    }
}

/**
 * Action to zoom out of the diagram.
 */
class ZoomOutAction : AnAction(
    AllIcons.Graph.ZoomOut
) {
    override fun actionPerformed(e: AnActionEvent) {
        e.zoomModel?.zoomOut()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.zoomModel?.canZoomOut() ?: false
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}