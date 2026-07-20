package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ArrayType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaIvar
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaModules
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaOneDecl

/**
 * Mixin for variable (ivar) implementation.
 * @param node the AST node
 */
abstract class IVarImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaIvar, NavigatablePsiElement {
    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override val isGlobalVariable: Boolean
        get() = parent.parent is PromelaModules

    override fun getPresentation() = VariablePresentation(this)

    override fun getName(): String? {
        return identifier.name
    }

    override fun setName(newName: @NlsSafe String): PsiElement? {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }

    override fun getType(): PsiType? {
        val type = (this.parent as PromelaOneDecl?)?.typename?.getType() ?: return null
        return if (sizeInitializer != null) {
            val size = sizeInitializer!!.constExpression.calculateConstantValue() ?: 0.toLong()
            ArrayType(type, size)
        } else {
            type
        }
    }
}