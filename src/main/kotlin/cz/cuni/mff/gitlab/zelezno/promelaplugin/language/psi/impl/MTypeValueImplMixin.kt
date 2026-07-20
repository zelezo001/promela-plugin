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
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaMtypeValue
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl.PromelaMtypeImpl

/**
 * Mixin for mtype value implementation.
 * @param node the AST node
 */
abstract class MTypeValueImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaMtypeValue {
    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override val isGlobalVariable: Boolean
        get() = true

    override fun getPresentation(): ItemPresentation {
        val name = name
        return object : ItemPresentation {
            override fun getPresentableText() = name
            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Enum
        }
    }

    override fun getName(): String? {
        return identifier.name
    }

    override fun getTextOffset(): Int {
        return identifier.textOffset
    }

    override fun setName(newName: String): PromelaMtypeValue {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }

    override fun getType(): PsiType? {
        return (parent as? PromelaMtypeImpl)?.getType()
    }
}