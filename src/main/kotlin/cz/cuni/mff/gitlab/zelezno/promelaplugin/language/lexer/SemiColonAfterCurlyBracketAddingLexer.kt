package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer

import com.intellij.lexer.Lexer
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Lexer handling semicolon around '}'
 * @param inner previous lexer
 */
class SemiColonAfterCurlyBracketAddingLexer(inner: Lexer) : SemicolonAddingLexer(
    delegate = inner,
    targetElementTypes = TokenSet.create(PromelaTypes.CLOSECURLYBRACKET),
    doNotAddSemicolonBefore = TokenSet.create(
        PromelaTypes.PROCTYPE_KW,
        PromelaTypes.INIT_KW,
        PromelaTypes.NEVER_KW,
        PromelaTypes.SEMI,
        PromelaTypes.SEP,
        PromelaTypes.IF_END_KW,
        PromelaTypes.DO_END_KW,
        PromelaTypes.CLOSECURLYBRACKET,
        PromelaTypes.UNLESS_KW,
        PromelaTypes.SEMI,
        PromelaTypes.ARROW
    ),
    doNotAddEOLSemicolonBefore = TokenSet.create(PromelaTypes.UNLESS_KW),
)

