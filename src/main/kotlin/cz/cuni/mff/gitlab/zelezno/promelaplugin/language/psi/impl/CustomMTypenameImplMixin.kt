package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.MType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiScopeActionExecutor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaMtype
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaMtypename

/**
 * Mixin for mtype name implementation.
 * @param node the AST node
 */
abstract class MtypenameImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaMtypename, NavigatablePsiElement {

    override fun getName(): String = identifier?.name ?: MType.GENERIC_NAME

    override fun getReference(): CustomMTypenameReference {
        return CustomMTypenameReference(this)
    }

    override fun getType(): PsiType = MType(name)

}

/**
 * Reference for [PromelaMtypename].
 * @param element the mtype name element
 */
class CustomMTypenameReference(private val element: MtypenameImplMixin) : PsiPolyVariantReference {

    override fun getElement(): PsiElement = element

    /**
     * Resolves the mtype name reference.
     * @return the resolved element, or null if not found
     */
    override fun resolve(): PsiElement? {
        return multiResolve(false).takeIf { it.size == 1 }?.get(0)?.element
    }

    override fun getCanonicalText(): @NlsSafe String {
        return element.getType().getName()
    }

    override fun handleElementRename(newElementName: String): PsiElement? {
        val name: String = if (MType.GENERIC_NAME == newElementName) {
            ""
        } else {
            newElementName
        }
        val newElement = ElementFactory.createMTypename(element.project, name) ?: return null
        return element.replace(newElement)
    }

    override fun bindToElement(element: PsiElement): PsiElement? {
        if (element !is PromelaMtype) throw IncorrectOperationException(PromelaBundle.message("language.psi.error.mustBeMtype"))
        val name = element.name ?: return null
        return handleElementRename(name)
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return element is PromelaMtype && element.name == this.element.name
    }

    override fun isSoft(): Boolean = true

    /**
     * Returns all available mtype variants for completion.
     * @return an array of lookup elements
     */
    override fun getVariants(): Array<out Any> {
        val variants = mutableSetOf<LookupElement>()
        PsiScopeActionExecutor.executeForFile(element) { element, _ ->
            if (element is PromelaMtype && !element.name.isNullOrEmpty()) {
                if (element.name != MType.GENERIC_NAME) {
                    variants.add(
                        LookupElementBuilder.create(element).withIcon(AllIcons.Nodes.Type)
                    )
                }
            }
            true
        }

        return variants.toTypedArray()
    }

    override fun getRangeInElement(): TextRange {
        return element.identifier?.textRangeInParent ?: TextRange(0, element.textLength)
    }

    /**
     * Resolves to all declaration of [PromelaMtype] which matches name of reference owner
     */
    override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult?> {
        val mtypes = mutableSetOf<PsiElementResolveResult>()
        PsiScopeActionExecutor.executeForFile(element) { visited, _ ->
            if (visited is PromelaMtype && element.getName() == visited.name) {
                mtypes.add(PsiElementResolveResult(visited))
            }
            true
        }

        return mtypes.toTypedArray()
    }
}