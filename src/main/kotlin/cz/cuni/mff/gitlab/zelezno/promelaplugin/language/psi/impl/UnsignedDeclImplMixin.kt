package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiElement
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.UnsignedType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaModules
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaUnsignedDecl

/**
 * Mixin for unsigned variable declaration implementation.
 * @param node the AST node
 */
abstract class UnsignedDeclImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaUnsignedDecl {
    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override val isGlobalVariable: Boolean
        get() = parent.parent is PromelaModules

    override fun getName(): String? {
        return identifier?.name
    }

    override fun getTextOffset(): Int {
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun getPresentation(): ItemPresentation = VariablePresentation(this)

    override fun setName(newName: @NlsSafe String): PsiElement? {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }

    override fun getType(): PsiType {
        val bitSize = const?.numberRule?.calculateConstantValue() ?: 0
        return UnsignedType(bitSize)
    }
}