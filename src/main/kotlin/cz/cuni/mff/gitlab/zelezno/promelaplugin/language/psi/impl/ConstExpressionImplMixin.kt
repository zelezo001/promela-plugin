package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ConstantExpression
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaConstExpression

/**
 * Mixin for [PromelaConstExpression] implementation.
 * @param node the AST node
 */
abstract class ConstExpressionImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaConstExpression {
    override fun calculateConstantValue(): Long? {
        return (firstChild as? ConstantExpression)?.calculateConstantValue()
    }
}