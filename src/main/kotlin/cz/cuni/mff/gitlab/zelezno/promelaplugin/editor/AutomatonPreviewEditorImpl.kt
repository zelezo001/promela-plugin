package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor

import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.concurrency.annotations.RequiresEdt
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components.AutomatonPreviewComponent
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components.ReloadingDiagramsState
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components.ViewDiagramsState
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components.ViewMessageState
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.beans.PropertyChangeListener
import javax.swing.JComponent

internal class AutomatonPreviewEditorImpl(file: VirtualFile, project: Project) : UserDataHolderBase(), FileEditor,
    AutomatonPreviewEditor {

    private val component = AutomatonPreviewComponent(this).apply {
        Disposer.register(this@AutomatonPreviewEditorImpl, this)
    }

    private val diagramResolver: DiagramResolver

    init {
        component.setState(
            ViewMessageState(
                PromelaBundle.message("editor.AutomatonView.message.reload-to-display"),
                false
            )
        )

        diagramResolver = project.getService(DiagramResolvingService::class.java).createForFile(file) { result ->
            withContext(Dispatchers.EDT) {
                val state = when (result) {
                    is DiagramResolvingService.Diagrams -> ViewDiagramsState(result.diagrams)
                    is DiagramResolvingService.Error -> ViewMessageState(result.message, true)
                }
                component.setState(state)
            }
        }
    }

    private class EmptyState : FileEditorState {
        override fun canBeMergedWith(
            otherState: FileEditorState, level: FileEditorStateLevel
        ): Boolean {
            return otherState is EmptyState
        }
    }

    override fun getComponent(): JComponent {
        return component
    }

    @RequiresEdt
    override fun reload() {
        component.setState(ReloadingDiagramsState())
        diagramResolver.resolve()
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return component
    }

    override fun getName() = PromelaBundle.message("editor.AutomatonView.name")
    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false // we never modify the shown file

    override fun isValid(): Boolean = true


    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}
    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun dispose() {}
}