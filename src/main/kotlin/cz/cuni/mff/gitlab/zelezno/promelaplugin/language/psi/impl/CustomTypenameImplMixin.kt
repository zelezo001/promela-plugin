package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReferenceBase
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiScopeActionExecutor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiTypeDeclaration
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaCustomTypename
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaUtype
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl.PromelaCustomTypenameImpl

/**
 * Mixin for custom typename elements (typedef usages).
 * @param node the AST node
 */
abstract class CustomTypenameImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaCustomTypename {

    override fun getName(): String? = firstChild?.text

    override fun getReference(): CustomTypenameReference {
        return CustomTypenameReference(this)
    }

    /**
     * Updates the text of the typename.
     * @param text the new text
     * @return the updated element
     */
    fun updateText(text: String): PromelaCustomTypenameImpl {
        val updated = ElementFactory.createTypename(this.project, text)
        return this.replace(updated) as PromelaCustomTypenameImpl
    }

    override fun getType(): PsiType? {
        return reference.resolve()?.getType()
    }
}

/**
 * Reference for [PromelaCustomTypename].
 * @param ctype the custom typename element
 */
class CustomTypenameReference(ctype: PromelaCustomTypename) : PsiReferenceBase<PromelaCustomTypename>(ctype, false) {
    /**
     * Resolves the custom typename reference.
     * @return the resolved type declaration, or null if not found
     */
    override fun resolve(): PsiTypeDeclaration? {
        var type: PsiTypeDeclaration? = null
        val typeName = element.getName() ?: return null

        PsiScopeActionExecutor.executeFromBottomUp(element) { declaration, _ ->
            if (declaration is PromelaUtype && typeName == declaration.name) {
                type = declaration
                false
            } else {
                true
            }
        }

        return type
    }

    /**
     * Returns all available custom typenames for completion.
     * @return an array of available user types
     */
    override fun getVariants(): Array<out Any> {
        val utypes = mutableSetOf<PromelaUtype>()
        PsiScopeActionExecutor.executeFromBottomUp(element) { element, _ ->
            if (element is PromelaUtype) {
                utypes.add(element)
            }
            true
        }

        return utypes.toTypedArray()
    }
}