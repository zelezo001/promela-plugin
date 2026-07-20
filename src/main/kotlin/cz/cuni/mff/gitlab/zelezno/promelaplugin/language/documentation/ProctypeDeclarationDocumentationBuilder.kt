package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaProctype

internal object ProctypeDeclarationDocumentationBuilder : DeclarationDocumentationBuilder<PromelaProctype> {
    override fun build(
        declarationBuilder: StringBuilder, el: PromelaProctype
    ) {
        /*
        Builds `proctype  structName {
                    int x;
                    int y;
                }`
         */
        val builder = StringBuilder()

        el.proctypeHeader.active?.let {
            builder.append("active ")
        }
        if (el.proctypeHeader.dProctype != null) {
            builder.append("D_proctype ")
        } else {
            builder.append("proctype ")
        }
        el.name?.let {
            builder.append(it)
        }
        builder.append("(")
        writeVariableDeclarations(builder, el)
        builder.append(")")
        HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
            declarationBuilder, el.project, PromelaLanguage, builder.toString(), 1f
        )
    }
}