package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.usage

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.NavigablePsiElement
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiTypeDeclaration
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInline
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaLabel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaProctype

/**
 * [FindUsagesProvider] for the Promela language.
 * Works for elements which can be referenced [PsiVariable], [PromelaLabel], [PromelaProctype], [PsiTypeDeclaration], [PromelaInline]
 */
class PromelaFindUsagesProvider : FindUsagesProvider {
    override fun getHelpId(psiElement: PsiElement): String? = null
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return getType(psiElement).isNotEmpty()
    }

    override fun getType(element: PsiElement): String {
        return when (element) {
            is PsiVariable -> PromelaBundle.message("language.psi.type.variable")
            is PromelaLabel -> PromelaBundle.message("language.psi.type.label")
            is PromelaProctype -> PromelaBundle.message("language.psi.type.proctype")
            is PsiTypeDeclaration -> PromelaBundle.message("language.psi.type.typedef")
            is PromelaInline -> PromelaBundle.message("language.psi.type.inline")
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return (element as NavigablePsiElement).presentation.presentableText ?: getType(element)
    }

    override fun getNodeText(
        element: PsiElement,
        useFullName: Boolean
    ): String {
        return element.parent.toString()
    }
}