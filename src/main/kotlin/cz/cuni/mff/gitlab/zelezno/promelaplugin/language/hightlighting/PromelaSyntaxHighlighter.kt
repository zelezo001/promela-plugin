package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.hightlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.PromelaLexerAdapter
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets.Companion.PREPROCESSOR_DIRECTIVES
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Syntax highlighter for the Promela language.
 */
class PromelaSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val KEYWORD: TextAttributesKey =
            createTextAttributesKey("PROMELA_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val LINE_COMMENT: TextAttributesKey = createTextAttributesKey(
            "PROMELA_LINE_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        val BLOCK_COMMENT: TextAttributesKey = createTextAttributesKey(
            "PROMELA_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )
        val PARENTHESES: TextAttributesKey =
            createTextAttributesKey("PROMELA_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES: TextAttributesKey =
            createTextAttributesKey("PROMELA_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val SEMICOLON: TextAttributesKey =
            createTextAttributesKey("PROMELA_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val STRING: TextAttributesKey =
            createTextAttributesKey("PROMELA_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER: TextAttributesKey =
            createTextAttributesKey("PROMELA_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val OPERATION_SIGN: TextAttributesKey =
            createTextAttributesKey("OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val TRUTH_VALUE: TextAttributesKey =
            createTextAttributesKey("PROMELA_TRUTH_VALUE", DefaultLanguageHighlighterColors.KEYWORD)
        val BRACKETS: TextAttributesKey =
            createTextAttributesKey("PROMELA_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val MACRO: TextAttributesKey =
            createTextAttributesKey("PROMELA_MACRO", DefaultLanguageHighlighterColors.METADATA)

        val PROCTYPE_DECLARATION: TextAttributesKey =
            createTextAttributesKey("PROCTYPE_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val PROCTYPE_RUN: TextAttributesKey =
            createTextAttributesKey("PROCTYPE_RUN", DefaultLanguageHighlighterColors.FUNCTION_CALL)

        val INLINE_DECLARATION: TextAttributesKey =
            createTextAttributesKey("PROCTYPE_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val INLINE_CALL: TextAttributesKey =
            createTextAttributesKey("INLINE_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL)

        val C_CODE: TextAttributesKey =
            createTextAttributesKey("C_CODE")
    }

    override fun getHighlightingLexer(): Lexer = PromelaLexerAdapter()

    override fun getTokenHighlights(type: IElementType?): Array<out TextAttributesKey?> {

        if (PromelaTokenSets.KEYWORDS.contains(type)) {
            return arrayOf(KEYWORD)
        }

        return when (type) {
            PromelaTypes.C_CODE -> arrayOf(C_CODE)
            PromelaTypes.BLOCK_COMMENT -> arrayOf(BLOCK_COMMENT)
            PromelaTypes.LINE_COMMENT -> arrayOf(LINE_COMMENT)
            PromelaTypes.STRING -> arrayOf(STRING)
            PromelaTypes.SEMI, PromelaTypes.ARROW -> arrayOf(SEMICOLON)
            PromelaTypes.OPENCURLYBRACKET, PromelaTypes.CLOSECURLYBRACKET -> arrayOf(BRACES)
            PromelaTypes.OPENBRACKET, PromelaTypes.CLOSEBRACKET -> arrayOf(PARENTHESES)
            PromelaTypes.OPENSQUAREBRACKET, PromelaTypes.CLOSESQUAREBRACKET -> arrayOf(BRACKETS)

            PromelaTypes.CONST_FALSE, PromelaTypes.CONST_TRUE -> arrayOf(TRUTH_VALUE)

            PromelaTypes.NUMBER -> arrayOf(NUMBER)
            in PREPROCESSOR_DIRECTIVES -> arrayOf(MACRO)

            in PromelaTokenSets.OPERATIONS -> arrayOf(OPERATION_SIGN)

            else -> emptyArray()
        }

    }
}

/**
 * Factory for creating [PromelaSyntaxHighlighter] instances.
 */
class PromelaSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(
        p0: Project?,
        p1: VirtualFile?
    ): SyntaxHighlighter = PromelaSyntaxHighlighter()
}