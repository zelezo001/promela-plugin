package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.startOffset
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ScalarType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInlineArg

/**
 * Mixin for inline argument implementation.
 * @param node the AST node
 */
abstract class InlineArgImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaInlineArg {
    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override val isGlobalVariable: Boolean
        get() = false

    override fun getPresentation(): ItemPresentation {
        val name = name
        return object : ItemPresentation {
            override fun getPresentableText() = name

            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Parameter
        }
    }

    override fun getType() = ScalarType.INLINED_EXPRESSION

    override fun getName() = identifier.name

    override fun getTextOffset() = identifier.textOffset

    override fun setName(newName: String): PromelaInlineArg {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }
}