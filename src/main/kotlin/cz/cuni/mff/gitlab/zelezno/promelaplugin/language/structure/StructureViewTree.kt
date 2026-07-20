package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.childrenOfType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.icons.PromelaIcons
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.NavigablePsiElement
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiTypeDeclaration
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaModules
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaOneDecl
import java.util.*
import javax.swing.Icon

/**
 * Structure element used for leaf elements
 */
internal open class PromelaStructureViewTreeElement(protected val el: NavigablePsiElement) : StructureViewTreeElement {
    override fun getValue(): Any? = el

    override fun getPresentation(): ItemPresentation = el.presentation

    override fun getChildren(): Array<out TreeElement?> = TreeElement.EMPTY_ARRAY

    override fun canNavigateToSource(): Boolean = el.canNavigateToSource()

    override fun canNavigate(): Boolean = el.canNavigate()

    override fun navigate(requestFocus: Boolean) = el.navigate(requestFocus)
}

/**
 * Structure element used for type declaration elements, which always contain some children
 */
internal class PromelaStructureViewTreeTypeDeclarationElement(el: PsiTypeDeclaration) :
    PromelaStructureViewTreeElement(el) {
    override fun getChildren(): Array<out TreeElement> {
        val children = LinkedList<TreeElement>()
        val processor = PsiScopeProcessor { declaration, _ ->
            if (declaration is PsiVariable) {
                children.add(PromelaStructureViewTreeElement(declaration))
            }
            true
        }
        el.processDeclarations(
            processor, ResolveState.initial(), null, el
        )

        return children.toTypedArray()
    }
}

/**
 * Root element of Promela structure tree.
 * @param psiFile the file which should be broken into tree structure
 */
internal class PromelaStructureViewTreeRootElement(private val psiFile: PromelaFile) : StructureViewTreeElement {
    override fun getValue() = psiFile

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): @NlsSafe String = psiFile.name
            override fun getIcon(unused: Boolean): Icon = PromelaIcons.PromelaFile
        }
    }

    /**
     * @return List of all children that are navigable through [NavigablePsiElement]
     */
    override fun getChildren(): Array<out TreeElement?> {
        // collect all direct children from PromelaModules (it is always only-child)
        val children = ArrayList<StructureViewTreeElement>()
        psiFile.findChildByClass(PromelaModules::class.java)
            ?.children
            ?.forEach { child ->
                when (child) {
                    is PromelaOneDecl -> child.childrenOfType<PsiVariable>()
                        .forEach { variable -> children.add(PromelaStructureViewTreeElement(variable)) }

                    is PsiTypeDeclaration -> children.add(PromelaStructureViewTreeTypeDeclarationElement(child))
                    is NavigablePsiElement -> children.add(PromelaStructureViewTreeElement(child))
                }
            }

        return children.toTypedArray()
    }
}