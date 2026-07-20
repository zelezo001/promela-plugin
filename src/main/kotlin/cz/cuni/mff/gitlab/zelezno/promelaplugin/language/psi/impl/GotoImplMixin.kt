package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.startOffset
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiScopeActionExecutor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaGoto
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaLabel

/**
 * Mixin for goto statement implementation.
 * @param node the AST node
 */
abstract class GotoImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaGoto {

    override fun getName(): String? = identifier?.name

    override fun getReference(): LabelReference? {
        val name = name ?: return null
        // name being not null implies [identifier] being not null
        return LabelReference(this, name)
    }

    override fun getTextOffset(): Int {
        return identifier?.startOffset ?: (super.getTextOffset() + 5) // |goto | = 4
    }
}

/**
 * Reference to a [PromelaLabel].
 * @param element the goto element containing the reference. [GotoImplMixin.getIdentifier] must be not-null
 * @param name name of the label
 */
class LabelReference(private val element: GotoImplMixin, private val name: String) : PsiReference {
    override fun getElement(): PsiElement {
        return element
    }

    override fun getRangeInElement(): TextRange {
        return element.identifier!!.textRangeInParent
    }

    /**
     * Resolves the label reference.
     * @return the resolved label, or null if not found
     */
    override fun resolve(): PromelaLabel? {
        var label: PromelaLabel? = null
        PsiScopeActionExecutor.executeFromBottomUp(element) { visited, _ ->
            if (visited is PromelaLabel && element.getName() == visited.name) {
                label = visited
                false
            } else {
                true
            }
        }

        return label
    }

    override fun getCanonicalText(): @NlsSafe String = name

    override fun handleElementRename(labelName: String): PsiElement? {
        ElementFactory.replaceIdentifier(element.identifier, labelName)
        return element
    }

    override fun bindToElement(element: PsiElement): PsiElement? {
        if (element !is PromelaLabel) throw IncorrectOperationException(PromelaBundle.message("language.psi.error.mustBeLabel"))
        val name = element.name ?: return null
        return handleElementRename(name)
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return element is PromelaLabel && name == element.name
    }

    /**
     * Returns all available labels for completion.
     * @return an array of available labels
     */
    override fun getVariants(): Array<out Any> {
        val labels = mutableSetOf<PromelaLabel>()
        PsiScopeActionExecutor.executeFromBottomUp(element) { element, _ ->
            if (element is PromelaLabel) {
                labels.add(element)
            }
            true
        }

        return labels.toTypedArray()
    }

    override fun isSoft(): Boolean = true // all our references are soft
}