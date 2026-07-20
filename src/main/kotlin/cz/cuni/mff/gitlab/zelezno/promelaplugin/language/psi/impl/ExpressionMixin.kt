package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.siblings
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*

/**
 * Mixin for [PromelaUnitConstExpression] implementation.
 * @param node the AST node
 */
abstract class UnitConstExpressionImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaUnitConstExpression {

    override fun calculateConstantValue(): Long? {
        if (const !== null) {
            return const!!.numberRule?.calculateConstantValue()
        }
        return parenthesis!!.childrenOfType<PromelaConstExpression>()
            .firstOrNull()?.calculateConstantValue()
    }
}

/**
 * Mixin for [PromelaUnaryConstExpression] implementation.
 * @param node the AST node
 */
abstract class UnaryConstExpressionImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaUnaryConstExpression {
    override fun calculateConstantValue(): Long? {
        return unitConstExpression?.calculateConstantValue()?.unaryMinus()
    }
}

/**
 * Mixin for [PromelaMultiplicationConstExpression] implementation.
 * @param node the AST node
 */
abstract class MultiplicationConstExpressionImplMixin(node: ASTNode) : ASTWrapperPsiElement(node),
    PromelaMultiplicationConstExpression {

    private companion object {
        val LOG = Logger.getInstance(AdditionConstExpressionImplMixin::class.java)
    }

    override fun calculateConstantValue(): Long? {
        val children = constExpressionList.toList()
        if (children.size != 2) {
            return null
        }
        val a = children[0].calculateConstantValue() ?: return null
        val b = children[1].calculateConstantValue() ?: return null
        val op = children[0].getNextSiblingIgnoringWhitespaceAndComments()
        return when (op.elementType) {
            PromelaTypes.OP_STAR -> a * b
            PromelaTypes.OP_MOD -> a % b
            PromelaTypes.OP_DIV -> if (b == 0.toLong()) null else a / b
            else -> {
                val e = NotImplementedError("unsupported operation $op")
                LOG.error(e)
                throw e
            }
        }
    }
}

/**
 * Mixin for [PromelaAdditionConstExpression] implementation.
 * @param node the AST node
 */
abstract class AdditionConstExpressionImplMixin(node: ASTNode) : ASTWrapperPsiElement(node),
    PromelaAdditionConstExpression {

    private companion object {
        val LOG = Logger.getInstance(AdditionConstExpressionImplMixin::class.java)
    }

    override fun calculateConstantValue(): Long? {
        val children = constExpressionList.toList()
        if (children.size != 2) {
            return null
        }
        val a = children[0].calculateConstantValue() ?: return null
        val b = children[1].calculateConstantValue() ?: return null
        val op = children[0].getNextSiblingIgnoringWhitespaceAndComments()
        return when (op.elementType) {
            PromelaTypes.OP_PLUS -> a + b
            PromelaTypes.OP_MINUS -> a - b
            else -> {
                val e = NotImplementedError("unsupported operation $op")
                LOG.error(e)
                throw e
            }
        }
    }
}

private val IGNORED_TOKENS =
    TokenSet.orSet(PromelaTokenSets.COMMENTS, TokenSet.WHITE_SPACE, TokenSet.create(PromelaTypes.EOL))

private fun PsiElement.getNextSiblingIgnoringWhitespaceAndComments(): PsiElement? {
    return this.siblings(withSelf = false).firstOrNull { sibling -> sibling.elementType !in IGNORED_TOKENS }
}