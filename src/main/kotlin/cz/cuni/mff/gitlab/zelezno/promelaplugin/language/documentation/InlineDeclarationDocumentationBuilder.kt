package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ScalarType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInline

internal object InlineDeclarationDocumentationBuilder : DeclarationDocumentationBuilder<PromelaInline> {
    override fun build(
        declarationBuilder: StringBuilder, el: PromelaInline
    ) {
        val builder = StringBuilder()
        builder.append("inline ")
        el.name?.let {
            builder.append(it)
        }
        builder.append("(")
        var addSemi = false
        val processor = PsiScopeProcessor { declaration, _ ->
            if (declaration is PsiVariable) {
                if (addSemi) {
                    builder.append(",")
                }
                builder.append("\n    ${ScalarType.INLINED_EXPRESSION.getName()} ${declaration.name}")
                addSemi = true
            }
            true
        }
        el.processDeclarations(
            processor, ResolveState.initial(), el, el
        )
        if (addSemi) {
            builder.append("\n")
        }
        builder.append(")")
        HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
            declarationBuilder, el.project, PromelaLanguage, builder.toString(), 1f
        )
    }
}