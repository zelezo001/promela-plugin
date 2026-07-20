package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiScopeActionExecutor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInline
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInlineCall

/**
 * Mixin for inline call implementation.
 * @param node the AST node
 */
abstract class InlineCallImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaInlineCall {
    override fun getReference(): PsiReference? {
        return InlineReference(this)
    }

    override fun getName(): String {
        return firstChild!!.text
    }
}

/**
 * Reference to a [PromelaInline] definition.
 * @param el the inline call element containing the reference
 */
class InlineReference(
    el: InlineCallImplMixin,
) : PsiReferenceBase<InlineCallImplMixin>(
    el, el.firstChild.textRangeInParent,
) {

    /**
     * Resolves the inline reference.
     * @return the resolved inline definition, or null if not found
     */
    override fun resolve(): PromelaInline? {
        var lookingFor: PromelaInline? = null
        val name = element.name

        PsiScopeActionExecutor.executeFromBottomUp(element) { declaration, _ ->
            if (declaration is PromelaInline && declaration.name == name) {
                lookingFor = declaration; false
            } else true
        }

        return lookingFor
    }

    /**
     * Returns all visible inline definitions for completion.
     * @return an array of visible inline definitions
     */
    override fun getVariants(): Array<out Any> {
        val variables = mutableSetOf<PromelaInline>()
        PsiScopeActionExecutor.executeFromBottomUp(element) { declaration, _ ->
            if (declaration is PromelaInline) {
                variables.add(declaration)
            }
            true
        }

        return variables.toTypedArray()
    }
}

