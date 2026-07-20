package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaNumberRule

/**
 * Mixin for number rule implementation.
 * @param node the AST node
 */
abstract class PromelaNumberRuleImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaNumberRule {
    override fun calculateConstantValue(): Long? {
        return this.firstChild?.text?.let {
            parseNumber(it)
        }
    }

    private fun parseNumber(tokenText: String): Long? {
        if (tokenText.startsWith('\'')) {
            if (!tokenText.endsWith('\'')) {
                return null
            }
            return when (val charValue = tokenText.substring(1, tokenText.length - 1)) {
                "\\n" -> '\n'.code
                "\\t" -> '\t'.code
                "\\r" -> '\r'.code
                else -> {
                    if (charValue.count() == 1) {
                        charValue[0].code
                    } else {
                        null
                    }
                }
            }?.toLong()
        }
        return tokenText.toLongOrNull()
    }
}