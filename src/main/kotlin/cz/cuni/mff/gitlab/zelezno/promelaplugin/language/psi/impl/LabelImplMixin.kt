package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiElement
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaLabel

/**
 * Mixin for label implementation.
 * @param node the AST node
 */
abstract class LabelImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaLabel {
    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override fun getPresentation(): ItemPresentation {
        val name = name
        return object : ItemPresentation {
            override fun getPresentableText() = name

            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Tag
        }
    }

    override fun getName(): String? {
        return identifier.name
    }

    override fun setName(newName: @NlsSafe String): PsiElement? {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }
}