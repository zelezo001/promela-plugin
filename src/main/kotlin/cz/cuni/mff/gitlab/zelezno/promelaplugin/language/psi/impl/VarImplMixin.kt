package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReferenceBase
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiScopeActionExecutor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariableReference
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaMtypeValue
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaVar
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl.PromelaVarImpl

/**
 * Mixin for [PromelaVar] implementation.
 * @param node the AST node
 */
abstract class VarImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaVar {
    override fun getReference(): PsiVariableReference? {
        return VarReference(this)
    }

    override fun getName(): String? {
        return identifier.name
    }

    /**
     * Updates the identifier text of the variable.
     * @param text the new identifier text
     * @return the updated element
     */
    fun updateText(text: String): PromelaVarImpl {
        val updated = ElementFactory.createIdentifier(this.project, text)
        this.identifier.replace(updated)
        return this as PromelaVarImpl
    }
}

/**
 * Reference to a [PsiVariable].
 * @param el the element containing the reference
 */
class VarReference(el: VarImplMixin) : PsiReferenceBase<PromelaVar>(el, false), PsiVariableReference {

    /**
     * Resolves the variable reference.
     * @return the resolved variable, or null if not found
     */
    override fun resolve(): PsiVariable? {
        var variable: PsiVariable? = null
        val name = element.getName() ?: return null
        PsiScopeActionExecutor.executeFromBottomUp(element) { visited, _ ->
            if (visited is PsiVariable && name == visited.name) {
                variable = visited
                false
            } else {
                true
            }
        }

        return variable
    }

    /**
     * Returns all visible variables for completion.
     * @return an array of lookup elements
     */
    override fun getVariants(): Array<out Any> {
        val variables = mutableMapOf<String, Any>()
        PsiScopeActionExecutor.executeFromBottomUp(element) { element, _ ->
            if (element is PsiVariable && !element.name.isNullOrEmpty()) {
                variables.computeIfAbsent(element.name!!) { buildLookupElement(element) }
            }
            true
        }

        return variables.values.toTypedArray()
    }

    private fun buildLookupElement(el: PsiVariable): LookupElement {
        val builder = LookupElementBuilder
            .create(el)
            .withTypeText(el.getType()?.getName())
        return if (el is PromelaMtypeValue) {
            builder.withIcon(AllIcons.Nodes.Enum)
        } else {
            builder.withIcon(AllIcons.Nodes.Variable)
        }
    }
}