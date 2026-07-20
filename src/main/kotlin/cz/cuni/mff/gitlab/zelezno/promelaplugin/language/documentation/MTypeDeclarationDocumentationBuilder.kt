package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaMtype

internal object MTypeDeclarationDocumentationBuilder : DeclarationDocumentationBuilder<PromelaMtype> {
    override fun build(declarationBuilder: StringBuilder, el: PromelaMtype) {
        val codeBuilder = StringBuilder()
        el.name?.let {
            codeBuilder.append(it)
        }
        codeBuilder.append("{")
        var addSemi = false
        val processor = PsiScopeProcessor { declaration, _ ->
            if (declaration is PsiVariable) {
                if (addSemi) {
                    codeBuilder.append(",")
                }
                codeBuilder.append("\n    ${declaration.name}")
                addSemi = true
            }
            true
        }
        el.processDeclarations(
            processor, ResolveState.initial(), el, el
        )
        if (addSemi) {
            codeBuilder.append("\n")
        }
        codeBuilder.append("}")

        HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
            declarationBuilder, el.project, PromelaLanguage, codeBuilder.toString(), 1f
        )
    }
}