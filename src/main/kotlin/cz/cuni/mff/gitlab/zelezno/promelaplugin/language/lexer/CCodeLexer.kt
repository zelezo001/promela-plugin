package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer

import com.intellij.lang.TokenWrapper
import com.intellij.lexer.DelegateLexer
import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Lexer handling embedded C code. C detection is not done in Flex
 * as it would require keeping a complex state (e.g., counting brackets), which would
 * clash with the usage of Flex lexer in highlighting.
 *
 * All C embeds are expected to be in format `keyword [ expression ] { statement }`
 * Logic of this code was inspired by lexer/parser inside Spin.
 */
class CCodeLexer(delegate: Lexer) : DelegateLexer(delegate) {
    private enum class State {
        START, C_EXPRESSION, C_STATEMENT,
    }

    private var recursion: Int = 0
    private var myState: State = State.START
    private var myTokenStart: Int? = null
    private var myTokenEnd: Int? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        super.start(buffer, startOffset, endOffset, initialState)
        myState = State.START
        myTokenStart = null
        myTokenEnd = null
        recursion = 0
        tokenAdvanced()
    }

    private companion object {
        val CCODE_TOKEN_START = TokenSet.create(
            PromelaTypes.C_CODE_KW,
            PromelaTypes.C_EXPR_KW,
            PromelaTypes.C_DECL_KW,
        )

        // tokens that can be inside C code
        val IGNORED_TOKENS = TokenSet.orSet(
            PromelaTokenSets.COMMENTS, TokenSet.WHITE_SPACE, TokenSet.create(
                TokenType.ERROR_ELEMENT,
                PromelaTypes.EOL,
                PromelaTypes.EOL_OR_SEMI,
                TokenType.WHITE_SPACE,
            )
        )
    }

    override fun getTokenType(): IElementType? {
        return if (myTokenStart != null) {
            PromelaTypes.C_CODE
        } else super.getTokenType()
    }

    override fun getTokenEnd(): Int {
        return myTokenEnd ?: super.getTokenEnd()
    }

    override fun restore(position: LexerPosition) {
        val position = position as Position
        super.restore(position.delegate)
        myState = position.state
        myTokenStart = position.start
        myTokenEnd = position.end
        recursion = position.recursion
    }

    override fun getCurrentPosition(): LexerPosition {
        return Position(tokenStart, tokenEnd, recursion, myState, super.currentPosition)
    }

    private class Position(
        val start: Int?,
        val end: Int?,
        val recursion: Int,
        val state: State,
        val delegate: LexerPosition,
    ) : LexerPosition {
        override fun getOffset(): Int {
            return start ?: delegate.offset
        }

        override fun getState(): Int {
            return state.ordinal
        }
    }

    override fun getTokenStart(): Int {
        return myTokenStart ?: super.getTokenStart()
    }

    /**
     * Collects all tokens enclosed in recursion, stops if an ignored token is encountered
     */
    private fun collectTokens(
        recursionIn: IElementType,
        recursionOut: IElementType,
        nextState: State,
    ) {
        while (unwrappedTokenType !in IGNORED_TOKENS && unwrappedTokenType != null) {
            when (unwrappedTokenType) {
                recursionIn -> recursion++
                recursionOut -> {
                    recursion--
                    if (recursion == 0) {
                        myState = nextState
                        break
                    }
                }
            }
            delegate.advance()
            unwrapToken()
        }
        // set all eaten as a C token content
        myTokenEnd = delegate.tokenStart
    }

    private var unwrappedTokenType: IElementType? = null
    private fun unwrapToken() {
        unwrappedTokenType = (delegate.tokenType as? TokenWrapper)?.delegate ?: delegate.tokenType
    }

    private fun tokenAdvanced() {
        unwrapToken()
        if (unwrappedTokenType in IGNORED_TOKENS || unwrappedTokenType == null) return

        if (recursion == 0) {
            if (unwrappedTokenType in CCODE_TOKEN_START) {
                myState = State.C_EXPRESSION
            } else if (unwrappedTokenType == PromelaTypes.OPENCURLYBRACKET && (myState == State.C_EXPRESSION || myState == State.C_STATEMENT)) {
                // C_EXPRESSION are optional
                recursion++
                myState = State.C_STATEMENT
            } else if (myState == State.C_EXPRESSION && unwrappedTokenType == PromelaTypes.OPENSQUAREBRACKET) {
                recursion++
            } else {
                // reset, no c code encountered
                myState = State.START
            }
        } else if (recursion > 0) {
            myTokenStart = delegate.tokenStart
            if (myState == State.C_EXPRESSION) {
                collectTokens(PromelaTypes.OPENSQUAREBRACKET, PromelaTypes.CLOSESQUAREBRACKET, State.C_STATEMENT)
            } else if (myState == State.C_STATEMENT) {
                collectTokens(PromelaTypes.OPENCURLYBRACKET, PromelaTypes.CLOSECURLYBRACKET, State.START)
            }
        }
    }

    override fun advance() {
        if (myTokenStart != null) {
            myTokenStart = null
            myTokenEnd = null
            return
        }
        super.advance()

        tokenAdvanced()
    }
}