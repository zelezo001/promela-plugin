package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider
import com.intellij.psi.PsiElement
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*

/**
 * [PsiDocumentationTargetProvider] for Promela elements.
 * Only referencable Promela [PsiElement]s are supported
 */
class PromelaElementDocumentationProvider : PsiDocumentationTargetProvider {
    override fun documentationTarget(element: PsiElement, originalElement: PsiElement?): DocumentationTarget? {
        return when (element) {
            is PromelaLabel -> PromelaDocumentationTarget(element) { builder, el ->
                builder.append("label ${el.name}")
            }

            is PromelaMtype -> PromelaDocumentationTarget(element, MTypeDeclarationDocumentationBuilder)
            is PromelaUtype -> PromelaDocumentationTarget(element, UTypeDeclarationDocumentationBuilder)
            is PsiVariable -> PromelaDocumentationTarget(element, VariablesDeclarationDocumentationBuilder)
            is PromelaInline -> PromelaDocumentationTarget(element, InlineDeclarationDocumentationBuilder)
            is PromelaProctype -> PromelaDocumentationTarget(element, ProctypeDeclarationDocumentationBuilder)

            else -> null
        }
    }
}