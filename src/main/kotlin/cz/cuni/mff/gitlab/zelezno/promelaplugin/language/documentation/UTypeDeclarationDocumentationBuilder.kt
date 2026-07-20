package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaUtype

internal object UTypeDeclarationDocumentationBuilder : DeclarationDocumentationBuilder<PromelaUtype> {
    override fun build(
        declarationBuilder: StringBuilder, el: PromelaUtype
    ) {
        /*
        Builds `typedef structName {
                    int x;
                    int y;
                }`
         */
        val builder = StringBuilder()

        // builds "header"
        builder.append("typedef ")
        el.name?.let {
            builder.append(it)
            builder.append(" ")
        }

        // builds "body"
        builder.append("{")
        writeVariableDeclarations(builder, el)
        builder.append("}")

        HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
            declarationBuilder, el.project, PromelaLanguage, builder.toString(), 1f
        )
    }
}