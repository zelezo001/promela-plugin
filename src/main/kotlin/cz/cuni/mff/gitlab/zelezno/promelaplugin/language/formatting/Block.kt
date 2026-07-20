package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.lang.tree.util.children
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Block representing all comments in [PromelaTokenSets.COMMENTS].
 * Handles troubles of formatting preprocessor stuff (that means not formatting around it)
 * @param node formatted node
 * @param spacingBuilder [SpacingBuilder] for getting spacing configuration
 * @param settings User-defined style settings
 */
class PromelaCommentBlock(
    node: ASTNode,
    spacingBuilder: SpacingBuilder,
    settings: PromelaCodeStyleSettings
) : PromelaAbstractBlock(node, spacingBuilder, settings) {

    /**
     * Always empty
     */
    override fun buildChildren(): List<Block?>? {
        return emptyList()
    }

    /**
     * @return always true
     */
    override fun isLeaf(): Boolean = true

    /**
     * True if the block can be moved (preprocessor directives are not movable)
     */
    val movable = node.elementType !in PromelaTokenSets.PREPROCESSOR_DIRECTIVES
}

/**
 * Block representing LTL formulas is treated as a leaf and not formatted as LTL can be tricky due to special operators
 * @param node formatted node
 * @param spacingBuilder [SpacingBuilder] for getting spacing configuration
 * @param settings User-defined style settings
 */
class PromelaLTLBlock(
    node: ASTNode,
    spacingBuilder: SpacingBuilder,
    settings: PromelaCodeStyleSettings
) : PromelaAbstractBlock(node, spacingBuilder, settings) {
    override fun buildChildren(): List<Block?>? {
        return emptyList()
    }

    override fun getIndent(): Indent? = Indent.getNoneIndent()
    override fun isLeaf(): Boolean = true
}

/**
 * Block representing Promela code which is not handled by any other special block types.
 * @param node formatted node
 * @param spacingBuilder [SpacingBuilder] for getting spacing configuration
 * @param settings User-defined style settings
 */
class PromelaBlock(node: ASTNode, spacingBuilder: SpacingBuilder, settings: PromelaCodeStyleSettings) :
    PromelaAbstractBlock(node, spacingBuilder, settings) {
    override fun isLeaf(): Boolean = false
}

/**
 * Base class for representing blocks of Promela code for formatting.
 * @param node formatted node
 * @param spacingBuilder [SpacingBuilder] for getting spacing configuration
 * @param settings User-defined style settings
 */
abstract class PromelaAbstractBlock(
    node: ASTNode,
    private val spacingBuilder: SpacingBuilder,
    protected val settings: PromelaCodeStyleSettings
) :
    AbstractBlock(node, null, null) {

    override fun buildChildren(): List<Block?>? {
        return node.children().filter { it.elementType != TokenType.WHITE_SPACE }

            .filter { it.textLength > 0 } // ignore "fake" tokens (inserted semicolons/macro inlines...)
            .map {
                when (it.elementType) {
                    in PromelaTokenSets.COMMENTS -> PromelaCommentBlock(it, spacingBuilder, settings)
                    PromelaTypes.LTL -> PromelaLTLBlock(it, spacingBuilder, settings)
                    else -> PromelaBlock(it, spacingBuilder, settings)
                }
            }.toList()
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        return super.getChildAttributes(newChildIndex)
    }

    override fun getChildIndent(): Indent? {
        when (node.elementType) {
            is IFileElementType,
            PromelaTypes.MODULES -> Indent.getNoneIndent()

            in expressionElements -> Indent.getContinuationWithoutFirstIndent()

            PromelaTypes.SEQUENCE_BLOCK,
            PromelaTypes.OPTION,
                -> return Indent.getNormalIndent()
        }

        return Indent.getNoneIndent()
    }

    override fun getIndent(): Indent? {
        if (node.elementType == PromelaTypes.LABEL) {
            return Indent.getAbsoluteLabelIndent()
        }
        if (inNestedExpression()) {
            return Indent.getContinuationWithoutFirstIndent()
        }
        if (inNestedBlock()) {
            return Indent.getNormalIndent()
        }
        if (inOptionBlock()) {
            return Indent.getIndent(
                Indent.Type.NONE,
                true,
                false
            )
        }

        return Indent.getNoneIndent()
    }

    private fun inNestedExpression(): Boolean {
        val parentType = node.treeParent?.elementType
        return when (parentType) {
            in expressionElements -> true

            else -> false
        }
    }

    private fun inOptionBlock(): Boolean {
        val parentType = node.treeParent?.elementType
        return when (parentType) {
            PromelaTypes.DO_STMNT,
            PromelaTypes.IF_STMNT -> true

            else -> false
        }
    }

    private fun inNestedBlock(): Boolean {

        val parentType = node.treeParent?.elementType
        return when (parentType) {
            PromelaTypes.SEQUENCE_BLOCK ->
                node.elementType != PromelaTypes.OPENCURLYBRACKET && node.elementType != PromelaTypes.CLOSECURLYBRACKET

            PromelaTypes.SEQUENCE ->  node.treeParent!!.treeParent.elementType == PromelaTypes.OPTION

            PromelaTypes.UTYPE ->
                node.elementType == PromelaTypes.ONE_DECL

            else -> false
        }
    }

    private companion object {
        val expressionElements = TokenSet.create(
//            PromelaTypes.ANY_EXPRESSION, PromelaTypes.BASE_EXPRESSION,
//            PromelaTypes.CONST_EXPRESSION,
            PromelaTypes.PARENTHESIS,

            PromelaTypes.BINARY_ANY_EXPRESSION,
            PromelaTypes.BINARY_BASE_EXPRESSION,
            PromelaTypes.ADDITION_CONST_EXPRESSION,
            PromelaTypes.MULTIPLICATION_CONST_EXPRESSION,
            PromelaTypes.MULTIPLICATION_CONST_EXPRESSION,
        )
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 is PromelaCommentBlock && !child1.movable || child2 is PromelaCommentBlock && !child2.movable) {
            return Spacing.getReadOnlySpacing()
        }
        return spacingBuilder.getSpacing(this, child1, child2)
    }
}