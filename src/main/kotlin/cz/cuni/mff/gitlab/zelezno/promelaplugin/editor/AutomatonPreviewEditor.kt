package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor

import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditorWithPreviewProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFileType

/**
 * Editor for previewing automaton diagrams for Promela files.
 */
interface AutomatonPreviewEditor : FileEditor {
    companion object {
        val DATA_KEY = DataKey.create<AutomatonPreviewEditor>(AutomatonPreviewEditor::class.java.name)
    }

    /**
     * Reloads the displayed automaton diagrams
     */
    fun reload()
}