package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ArrayType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiScopeActionExecutor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaProctype
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaRun
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl.PromelaRunImpl

/**
 * Mixin for run expression implementation.
 * @param node the AST node
 */
abstract class RunImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaRun {
    override fun getName(): String? = identifier?.name

    override fun getReference(): RunReference? {
        return this.name?.let { RunReference(this, it) }
    }

    override fun getNavigationElement(): PsiElement {
        return this
    }

    override fun getTextOffset(): Int {
        return identifier?.textOffset ?: super.getTextOffset()
    }

    /**
     * Updates the identifier text of the run expression.
     * @param text the new identifier text
     * @return the updated element
     */
    fun updateText(text: String): PromelaRunImpl {
        ElementFactory.replaceIdentifier(identifier, text)
        return this as PromelaRunImpl
    }
}

/**
 * Reference to a [PromelaProctype] definition from a run expression.
 * @param el the run expression element containing the reference
 * @param name the name of the proctype to resolve
 */
class RunReference(el: RunImplMixin, private val name: String) :
    PsiReferenceBase<RunImplMixin>(el, el.identifier!!.textRangeInParent, false) {
    /**
     * Resolves the proctype reference.
     * @return the resolved proctype definition, or null if not found
     */
    override fun resolve(): PsiElement? {
        var proctype: PromelaProctype? = null
        PsiScopeActionExecutor.executeFromBottomUp(element) { element, _ ->
            if (element is PromelaProctype && element.name == name) {
                proctype = element
                false
            } else {
                true
            }
        }

        return proctype
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        return element.updateText(newElementName)
    }

    /**
     * Returns all visible proctypes for completion.
     * @return an array of lookup elements
     */
    override fun getVariants(): Array<out Any?> {
        val proctypes = mutableSetOf<Any>()
        PsiScopeActionExecutor.executeFromBottomUp(element) { element, _ ->
            if (element is PromelaProctype) {
                proctypes.add(buildProctypeLookupElement(element))
            }
            true
        }

        return proctypes.toTypedArray()
    }

    private fun buildProctypeLookupElement(el: PromelaProctype): LookupElement {
        // builds name as "name(int arg1;int arg2[1])"
        val tailedTextBuilder = StringBuilder()

        tailedTextBuilder.append('(')

        var addSemi = false
        val argumentCollector = PsiScopeProcessor { declaration, _ ->
            if (declaration is PsiVariable) {
                if (addSemi) {
                    tailedTextBuilder.append("; ") // arguments in declaration are separated by ;
                }
                tailedTextBuilder.append(declaration.formatNameWithType())
                addSemi = true
            }
            true
        }
        el.processDeclarations(argumentCollector, ResolveState.initial(), el, el)

        tailedTextBuilder.append(')')

        return LookupElementBuilder.create(el)
            .withIcon(AllIcons.Nodes.Function)
            .withTailText(tailedTextBuilder.toString(), true)
            .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
    }
}