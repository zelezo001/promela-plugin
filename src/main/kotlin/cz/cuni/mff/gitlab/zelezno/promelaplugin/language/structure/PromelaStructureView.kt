package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.structure

import com.intellij.ide.structureView.*
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFile

/**
 * [PsiStructureViewFactory] for the Promela language.
 */
class PromelaStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return PromelaFileStructureViewModel(psiFile as PromelaFile)
            }
        }
    }
}

/**
 * [TextEditorBasedStructureViewModel] describing the Promela File
 * @param psiFile described Promela file
 */
private class PromelaFileStructureViewModel(private val psiFile: PromelaFile) :
    TextEditorBasedStructureViewModel(psiFile), StructureViewModel.ElementInfoProvider {

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
        // only PromelaStructureViewTreeTypeDeclarationElement and PromelaStructureViewTreeRootElement contains subtrees
        return element !is PromelaStructureViewTreeTypeDeclarationElement &&
                element !is PromelaStructureViewTreeRootElement
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?) = false

    override fun getRoot(): StructureViewTreeElement = PromelaStructureViewTreeRootElement(psiFile)
}