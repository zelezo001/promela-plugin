package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.*
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor.MacroAwareLexer
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.parser.PromelaParser
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

private val FILE = IFileElementType(PromelaLanguage)

/**
 * [ParserDefinition] for the Promela language.
 */
class PromelaParserDefinition : ParserDefinition {

    override fun createLexer(p0: Project?): Lexer = SemiColonAfterElseAddingLexer(
        SemiColonAfterCurlyBracketAddingLexer(
            GenericSemicolonAddingLexer(
                CCodeLexer(MacroAwareLexer(PromelaLexerAdapter()))
            )
        )
    )

    override fun createParser(p0: Project?): PsiParser = object : PromelaParser() {
        override fun parse(t: IElementType, b: PsiBuilder): ASTNode {
            val builder = LexerErrorHandlingPsiBuilder(b)
            return super.parse(t, builder)
        }
    }

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getWhitespaceTokens(): TokenSet {
        return TokenSet.orSet(
            super.getWhitespaceTokens(),
            TokenSet.create(PromelaTypes.EOL),
        )
    }

    override fun getCommentTokens(): TokenSet = PromelaTokenSets.COMMENTS

    override fun getStringLiteralElements(): TokenSet = PromelaTokenSets.STRINGS

    override fun createElement(node: ASTNode?) = PromelaTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider) = PromelaFile(viewProvider)

}