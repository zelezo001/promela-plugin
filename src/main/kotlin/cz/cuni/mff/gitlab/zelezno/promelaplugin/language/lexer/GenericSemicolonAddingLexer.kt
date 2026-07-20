package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer

import com.intellij.lexer.Lexer
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Lexer handling semicolon around all problematic elements that are not '}' or else
 * @param inner previous lexer
 */
class GenericSemicolonAddingLexer(inner: Lexer) : SemicolonAddingLexer(
    delegate = inner,
    targetElementTypes = TokenSet.create(
        PromelaTypes.CLOSEBRACKET,
        PromelaTypes.CLOSESQUAREBRACKET,
        PromelaTypes.DO_END_KW,
        PromelaTypes.IF_END_KW,
        PromelaTypes.BREAK_KW,
        PromelaTypes.C_CODE_KW,
        PromelaTypes.C_EXPR_KW,
        PromelaTypes.C_DECL_KW,
        PromelaTypes.UNAME,
        PromelaTypes.NUMBER,
        PromelaTypes.CONST_FALSE,
        PromelaTypes.CONST_TRUE,
        PromelaTypes.CONST_SKIP,
        PromelaTypes.INCDEC,
    ),
    doNotAddSemicolonBefore = TokenSet.ANY,
    doNotAddEOLSemicolonBefore = TokenSet.EMPTY,
)