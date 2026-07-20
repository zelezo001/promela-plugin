package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.parser

import com.intellij.lang.ITokenTypeRemapper
import com.intellij.lang.PsiBuilder
import com.intellij.lang.TokenWrapper
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.removeUserData
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes
import java.util.*

object PromelaParserUtil : GeneratedParserUtilBase() {

    /**
     * Uname captured during inline/utype declarations. Prevents symbol recognition inside the body of inline/utype. (e.g., nested utypes).
     */
    private val PROMELA_CAPTURED_UNAME = Key<String>("captured_uname.key");

    /**
     * Known inlines. Used for deciding if uname is a inline call or not.
     */
    private val PROMELA_USER_TYPES = Key<HashSet<String>>("promela_types.key");

    private fun getTypes(builder: PsiBuilder): MutableSet<String> {
        builder.getUserData(PROMELA_USER_TYPES)?.let {
            return it
        }

        return HashSet<String>().also { builder.putUserData(PROMELA_USER_TYPES, it) }
    }

    /**
     * Known inlines. Used for deciding if uname is an inline call or not.
     */
    private val PROMELA_INLINES = Key<HashSet<String>>("promela_inlines.key");

    private fun getInlines(builder: PsiBuilder): MutableSet<String> {
        builder.getUserData(PROMELA_INLINES)?.let {
            return it
        }

        return HashSet<String>().also { builder.putUserData(PROMELA_INLINES, it) }
    }

    private val PROMELA_TOKEN_REMAPPER_STACK =
        Key<Stack<ITokenTypeRemapper?>>("promela_token_remapper.key");

    /**
     * Hook for popping the last set remapper.
     */
    @JvmField
    val POP_REMAPPER: Hook<Boolean?> = Hook { builder: PsiBuilder?, marker: PsiBuilder.Marker?, _: Boolean? ->
        builder?.let { popRemapperRule(it, 0) }
        marker
    }

    /**
     * Reverts the last remapper.
     */
    @JvmStatic
    fun popRemapperRule(builder: PsiBuilder, level: Int): Boolean {
        builder.getUserData(PROMELA_TOKEN_REMAPPER_STACK)?.let { stack ->
            if (stack.empty()) return@let
            stack.pop()
            if (stack.empty()) return@let
            builder.setTokenTypeRemapper(stack.peek())
        }
        return true
    }

    private val EOL_OR_SEMI_AS_SEMI = ITokenTypeRemapper { el, _, _, _ ->
        if (el == PromelaTypes.EOL_OR_SEMI) PromelaTypes.SEMI else el
    }
    private val EOL_OR_SEMI_AS_EOL =
        ITokenTypeRemapper { el: IElementType, _: Int, _: Int, _: CharSequence -> if (el == PromelaTypes.EOL_OR_SEMI) PromelaTypes.EOL else el }

    private fun setRemapper(builder: PsiBuilder, remapper: ITokenTypeRemapper?) {
        if (builder.getUserData(PROMELA_TOKEN_REMAPPER_STACK) == null) {
            builder.putUserData(PROMELA_TOKEN_REMAPPER_STACK, Stack<ITokenTypeRemapper?>())
        }
        val final = ITokenTypeRemapper { el, a, b, chars ->
            when (el) {
                // unwrap all tokens that are not specially handled
                is TokenWrapper -> when (el.delegate) {
                    PromelaTypes.UNAME, PromelaTypes.NUMBER, in BINARY_OPERATIONS, in UNARY_OPERATIONS -> el
                    TokenType.ERROR_ELEMENT -> {
                        el
                    }

                    else -> el.delegate
                }

                else -> remapper?.filter(el, a, b, chars) ?: el
            }
        }
        builder.getUserData(PROMELA_TOKEN_REMAPPER_STACK)!!.push(final)
        builder.setTokenTypeRemapper(final)
    }

    /**
     * Set remapper to remap EOL_OR_SEMI tokens as SEMI
     */
    @JvmStatic
    fun eolOrSemiAsSemi(builder: PsiBuilder, level: Int): Boolean {
        setRemapper(builder, EOL_OR_SEMI_AS_SEMI)
        return true
    }

    /**
     * Disables remapping of EOL_OR_SEMI
     */
    @JvmStatic
    fun eolOrSemiRaw(builder: PsiBuilder, level: Int): Boolean {
        setRemapper(builder, null)
        return true
    }

    /**
     * Set remapper to remap EOL_OR_SEMI tokens as EOL
     */
    @JvmStatic
    fun eolOrSemiAsEOL(builder: PsiBuilder, level: Int): Boolean {
        setRemapper(builder, EOL_OR_SEMI_AS_EOL)
        return true
    }

    /**
     * Hook which should be called after a inline is added. Inline name is read from PROMELA_CAPTURED_UNAME.
     */
    @JvmField
    val INLINE_ADDED: Hook<Boolean?> = Hook { builder: PsiBuilder?, marker: PsiBuilder.Marker?, _: Boolean? ->
        builder?.let {
            builder.getUserData(PROMELA_CAPTURED_UNAME)?.run {
                getInlines(builder).add(this)
            }
            builder.removeUserData(PROMELA_CAPTURED_UNAME)
        }
        marker
    }

    /**
     * Hook which should be called after an utype (via typedef) is added. Utype name is read from PROMELA_CAPTURED_UNAME.
     */
    @JvmField
    val USER_TYPE_ADDED: Hook<Boolean?> = Hook { builder: PsiBuilder?, marker: PsiBuilder.Marker?, _: Boolean? ->
        builder?.let {
            builder.getUserData(PROMELA_CAPTURED_UNAME)?.run {
                getTypes(builder).add(this)
            }
            builder.removeUserData(PROMELA_CAPTURED_UNAME)
        }
        marker
    }

    /**
     * Consumes uname which is not known inline/utype.
     */
    @JvmStatic
    fun consumeNonSpecialIdentifier(builder: PsiBuilder, level: Int) =
        !getTypes(builder).contains(builder.tokenText) &&
                !getInlines(builder).contains(builder.tokenText)
                && consumeUname(builder, level)

    /**
     * Use this instead of uname token as that does not respect wrapped tokens (returned by preprocessor).
     */
    @JvmStatic
    fun consumeUname(builder: PsiBuilder, level: Int): Boolean {
        if (builder.tokenType == PromelaTypes.UNAME) {
            addVariant(builder, builder.tokenText)
            builder.advanceLexer()
            return true
        }
        return false
    }


    /**
     * Use this instead of a number token as that does not respect wrapped tokens (returned by preprocessor).
     */
    @JvmStatic
    fun consumeNumber(builder: PsiBuilder, level: Int): Boolean {
        if (builder.tokenType == PromelaTypes.NUMBER) {
            addVariant(builder, builder.tokenText)
            builder.advanceLexer()
            return true
        }
        return false
    }

    private val BINARY_OPERATIONS = TokenSet.create(
        PromelaTypes.OP_AND,
        PromelaTypes.OP_BAND,
        PromelaTypes.OP_BOR,
        PromelaTypes.OP_DIV,
        PromelaTypes.OP_EQ,
        PromelaTypes.OP_GE,
        PromelaTypes.OP_GT,
        PromelaTypes.OP_LE,
        PromelaTypes.OP_LT,
        PromelaTypes.OP_MINUS,
        PromelaTypes.OP_MOD,
        PromelaTypes.OP_NEQ,
        PromelaTypes.OP_OR,
        PromelaTypes.OP_PLUS,
        PromelaTypes.OP_SLEFT,
        PromelaTypes.OP_SRIGHT,
        PromelaTypes.OP_STAR,
        PromelaTypes.OP_XOR
    )

    /**
     * Consume binary operation (*,+,/,-...). Use this instead of a number token as
     * that does not respect wrapped tokens (returned by preprocessor).
     */
    @JvmStatic
    fun consumeBinOperation(builder: PsiBuilder, level: Int): Boolean {
        if (builder.unwrappedTokenType() in BINARY_OPERATIONS) {
            addVariant(builder, builder.tokenText)
            builder.advanceLexer()
            return true
        }
        return false
    }

    private val MULTIPLICATIVE_OPERATIONS = TokenSet.create(
        PromelaTypes.OP_STAR,
        PromelaTypes.OP_DIV,
        PromelaTypes.OP_MOD,
    )

    /**
     * Consume multiplicative operation (*, /, %). Use this instead of a number token as
     * that does not respect wrapped tokens (returned by preprocessor).
     */
    @JvmStatic
    fun consumeMultiplicativeOperation(builder: PsiBuilder, level: Int): Boolean {
        if (builder.unwrappedTokenType() in MULTIPLICATIVE_OPERATIONS) {
            addVariant(builder, builder.tokenText)
            builder.advanceLexer()
            return true
        }
        return false
    }

    private val ADDITIVE_OPERATIONS = TokenSet.create(
        PromelaTypes.OP_PLUS,
        PromelaTypes.OP_MINUS,
    )

    /**
     * Consume additive operation (+, -). Use this instead of a number token as
     * that does not respect wrapped tokens (returned by preprocessor).
     */
    @JvmStatic
    fun consumeAdditiveOperation(builder: PsiBuilder, level: Int): Boolean {
        if (builder.unwrappedTokenType() in ADDITIVE_OPERATIONS) {
            addVariant(builder, builder.tokenText)
            builder.advanceLexer()
            return true
        }
        return false
    }

    private val UNARY_OPERATIONS = TokenSet.create(
        PromelaTypes.OP_BANG,
        PromelaTypes.OP_MINUS,
        PromelaTypes.OP_NEG,
    )

    /**
     * Consume unary operation('~', '-', '!'). Use this instead of a number token as
     * that does not respect wrapped tokens (returned by preprocessor).
     */
    @JvmStatic
    fun consumeUnOperation(builder: PsiBuilder, level: Int): Boolean {
        if (builder.unwrappedTokenType() in UNARY_OPERATIONS) {
            addVariant(builder, builder.tokenText)
            builder.advanceLexer()
            return true
        }
        return false
    }


    private val LTL_MODE_ENABLED = Key<Boolean>("promela_ltl_mode.key");

    @JvmStatic
    fun ltlEnabled(builder: PsiBuilder, level: Int): Boolean {
        val v = builder.getUserData(LTL_MODE_ENABLED)
        return v == true
    }

    @JvmStatic
    fun enableLTL(builder: PsiBuilder, level: Int): Boolean {
        builder.putUserData(LTL_MODE_ENABLED, true)
        return true
    }

    @JvmStatic
    fun disableLTL(builder: PsiBuilder, level: Int): Boolean {
        builder.putUserData(LTL_MODE_ENABLED, false)
        return true
    }

    private val LTL_UNARY_OPS = Regex("eventually|always|X")

    @JvmStatic
    fun consumeLTLUnaryOps(builder: PsiBuilder, level: Int): Boolean {
        if (builder.tokenType != PromelaTypes.UNAME || builder.tokenText == null) {
            return false
        }
        if (!LTL_UNARY_OPS.matches(builder.tokenText!!)) {
            return false
        }
        addVariant(builder, builder.tokenText)
        builder.advanceLexer()
        return true
    }

    /**
     * Consumes two tokens in a row if they match the given types.
     * @param builder the PSI builder
     * @param level the current parsing level
     * @param first the first token type
     * @param second the second token type
     * @return true if both tokens were consumed
     */
    @JvmStatic
    fun consumeTokenPair(builder: PsiBuilder, level: Int, first: IElementType, second: IElementType): Boolean {
        val next = builder.rawLookup(1)
        if (builder.tokenType == first && next == second) {
            val r = consumeToken(builder, first) // eat first
            if (!r) {
                // should not happen
                return false
            }
            return consumeToken(builder, second)
        }
        return false
    }

    private val LTL_BINARY_OPS = Regex("implies|equivalent|stronguntil|until|weakuntil|release|U|W|V")

    @JvmStatic
    fun consumeLTLBinaryOp(builder: PsiBuilder, level: Int): Boolean {
        if (builder.tokenType != PromelaTypes.UNAME || builder.tokenText == null) {
            return false
        }
        if (!LTL_BINARY_OPS.matches(builder.tokenText!!)) {
            return false
        }
        addVariant(builder, builder.tokenText)
        builder.advanceLexer()
        return true
    }

    @JvmStatic
    fun captureUname(builder: PsiBuilder, level: Int): Boolean {
        if (builder.tokenType != PromelaTypes.UNAME) {
            return false
        }
        val text = builder.tokenText
        if (text.isNullOrEmpty()) {
            return false

        }
        builder.putUserData(PROMELA_CAPTURED_UNAME, text)
        return true
    }

    @JvmStatic
    fun consumeType(builder: PsiBuilder, level: Int): Boolean {
        return getTypes(builder).contains(builder.tokenText) && consumeUname(builder, level)
    }

    @JvmStatic
    fun consumeInline(builder: PsiBuilder, level: Int): Boolean {
        return getInlines(builder).contains(builder.tokenText) && consumeUname(builder, level)
    }
}

/**
 * Unwraps a token type if it's wrapped. Use this when working with token sets as 'in' does not respect wrapping.
 */
private fun PsiBuilder.unwrappedTokenType(): IElementType? {
    return (tokenType as? TokenWrapper)?.delegate ?: tokenType
}