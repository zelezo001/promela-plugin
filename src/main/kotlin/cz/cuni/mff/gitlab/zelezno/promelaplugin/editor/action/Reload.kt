package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.AutomatonPreviewEditor


private val AnActionEvent.editor get() = this.getData(AutomatonPreviewEditor.DATA_KEY)

/**
 * Action for reloading the shown diagrams in the [AutomatonPreviewEditor].
 */
class ReloadAction() : AnAction(
    AllIcons.Actions.Refresh
) {
    override fun actionPerformed(e: AnActionEvent) {
        e.editor?.reload()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.editor != null
    }
}