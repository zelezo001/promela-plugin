package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer

import com.intellij.lexer.Lexer
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Lexer handling semicolon around else keyword
 * @param inner previous lexer
 */
class SemiColonAfterElseAddingLexer(inner: Lexer) : SemicolonAddingLexer(
    delegate = inner,
    targetElementTypes = TokenSet.create(PromelaTypes.ELSE_KW),
    doNotAddSemicolonBefore = TokenSet.create(
        PromelaTypes.IF_END_KW,
        PromelaTypes.ARROW,
        PromelaTypes.EOL_OR_SEMI,
        PromelaTypes.SEMI
    ),
    doNotAddEOLSemicolonBefore = TokenSet.EMPTY,
)