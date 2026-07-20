package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditorWithPreviewProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFileType

/**
 * [FileEditorProvider] for creating [AutomatonPreviewEditorImpl] editor
 */
internal class PromelaAutomatonPreviewEditorProvider : FileEditorProvider {
    override fun accept(
        project: Project, file: VirtualFile
    ): Boolean {
        return file.fileType is PromelaFileType && file.isInLocalFileSystem // otherwise we cannot call Spin on it
    }

    override fun createEditor(
        project: Project, file: VirtualFile
    ): FileEditor {
        return AutomatonPreviewEditorImpl(file, project)
    }

    override fun getEditorTypeId(): String {
        return "promelaAutomatonPreviewEditor"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR
    }
}

/**
 * Main editor provider for adding [AutomatonPreviewEditor] as a hidable part of the text editor
 */
class PromelaFileEditorProvider : TextEditorWithPreviewProvider(PromelaAutomatonPreviewEditorProvider())