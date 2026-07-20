package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer

import com.intellij.lang.TokenWrapper
import com.intellij.lexer.DelegateLexer
import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * In Spin 6.3.0 semicolons where made (mostly) optional. As we don't want to rewrite grammar to be dependent on EOLs
 * nor can we just remove semicolons from it (`a\n+n` and `a+b` would not be distinguishable) and
 * we possibly want to support multiple Spin versions, we need to handle this on a lexer level.
 * As Spin does not bother itself with lexer-compiler separation and uses compiler context when deciding
 * if semicolons should be inserted, in cases where is ambiguity, EOL_OR_SEMI is inserted instead of SEMI. EOL_OR_SEMIs
 * are then converted to SEMIs if Spin would do so.
 *
 * This class should NOT be used outside for highlighting as it does not support state recovering.
 *
 * @param delegate internal lexer from which this reads tokens
 * @param targetElementTypes Types after which SEMIs/SEMI_OR_EOLs are inserted
 * @param doNotAddSemicolonBefore Types before which SEMIs are not (and SEMI_OR_EOLs are) inserted
 * @param doNotAddEOLSemicolonBefore Types before which SEMI_OR_EOLs/SEMIs are not inserted
 */
open class SemicolonAddingLexer(
    delegate: Lexer,
    private val targetElementTypes: TokenSet,
    private val doNotAddSemicolonBefore: TokenSet,
    private val doNotAddEOLSemicolonBefore: TokenSet,
) : DelegateLexer(delegate) {
    private var insertedType: IElementType? = null
    private var innerTokenType: IElementType? = null // unwrapped type

    private var checkIfAddSemicolon: Boolean = false
    private var newLineEncountered: Boolean = false

    companion object {
        // These tokens are ignored (or rather removed before seen) by spin lexer
        private val IGNORED_TOKENS =
            TokenSet.orSet(PromelaTokenSets.COMMENTS, TokenSet.WHITE_SPACE, TokenSet.create(PromelaTypes.EOL))
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        super.start(buffer, startOffset, endOffset, initialState)
        unwrapInnerTokenType()
    }

    override fun getTokenType(): IElementType? {
        return insertedType ?: super.tokenType
    }

    override fun getTokenStart(): Int {
        return super.tokenStart
    }

    override fun getTokenEnd(): Int {
        return if (insertedType != null) super.tokenStart else super.tokenEnd
    }

    override fun advance() {
        if (insertedType != null) {
            insertedType = null
            return
        }
        if (innerTokenType in targetElementTypes) {
            checkIfAddSemicolon = true
            newLineEncountered = false
        }
        if (innerTokenType == PromelaTypes.EOL) {
            // newLineEncountered is always reset when target token is encountered so
            // no need to check for checkIfAddSemicolon
            newLineEncountered = true
        }
        super.advance()
        innerAdvanced()
    }

    private fun updateInsertedType() {
        if (innerTokenType !in doNotAddSemicolonBefore) {
            insertedType = PromelaTypes.SEMI
            return
        }
        if (newLineEncountered && innerTokenType !in doNotAddEOLSemicolonBefore) {
            insertedType = PromelaTypes.EOL_OR_SEMI
        }
    }

    private fun unwrapInnerTokenType() {
        val unwrapped = super.tokenType as? TokenWrapper
        this.innerTokenType = if (unwrapped === null) super.tokenType else unwrapped.delegate
    }

    private fun innerAdvanced() {
        unwrapInnerTokenType()
        if (checkIfAddSemicolon && innerTokenType !in IGNORED_TOKENS) {
            insertedType = null
            if (innerTokenType != null) {
                updateInsertedType()
            }
            checkIfAddSemicolon = false
            newLineEncountered = false
        }
    }
}