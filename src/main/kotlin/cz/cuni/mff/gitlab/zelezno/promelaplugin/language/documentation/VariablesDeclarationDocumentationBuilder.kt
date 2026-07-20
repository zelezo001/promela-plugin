package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.jetbrains.rd.generator.nova.GenerationSpec.Companion.nullIfEmpty
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*

internal object VariablesDeclarationDocumentationBuilder : DeclarationDocumentationBuilder<PsiVariable> {
    private fun kind(el: PsiVariable): String {
        return when (el.parent) {
            // variables declared via one decl (struct fields, variable declarations)
            is PromelaOneDecl -> {
                when {
                    el.parent.parent is PromelaUtype -> "struct field"
                    el.isGlobalVariable -> "global variable"
                    else -> "local variable"
                }
            }
            // other
            is PromelaMtype -> "numeric constant"
            is PromelaInline, is PromelaProctype -> "parameter"
            else -> ""
        }
    }

    override fun build(
        declarationBuilder: StringBuilder, el: PsiVariable
    ) {
        kind(el).nullIfEmpty()?.let { declarationBuilder.append("$it\n") }

        HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
            declarationBuilder, el.project, PromelaLanguage, el.formatNameWithType(), 1f
        )

        val parentParent = el.parent.parent
        if (parentParent is PromelaUtype) {
            parentParent.getType()?.let {
                declarationBuilder.append("\n    in ")
                HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
                    declarationBuilder, el.project, PromelaLanguage, it.getName(), 1f
                )
                declarationBuilder.append("\n")
            }
        }
    }
}