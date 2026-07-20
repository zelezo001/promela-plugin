package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi

import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * [TokenSet] definitions for the Promela language.
 */
class PromelaTokenSets {
    companion object {
        /** Tokens representing preprocessor directives. */
        val PREPROCESSOR_DIRECTIVES: TokenSet = TokenSet.create(
            PromelaTypes.PREPROCESSOR_COMMENT,
            PromelaTypes.DISABLED_CODE,
            PromelaMacroTypes.PREPROCESSOR_DEFINE_CALL,
            PromelaMacroTypes.PREPROCESSOR_DEFINE_NAME,
            PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END,
            PromelaMacroTypes.PREPROCESSOR_DEFINE,
            PromelaMacroTypes.PREPROCESSOR_UNDEFINE,

            PromelaMacroTypes.PREPROCESSOR_IF,
            PromelaMacroTypes.PREPROCESSOR_IFDEF,
            PromelaMacroTypes.PREPROCESSOR_IFNDEF,
            PromelaMacroTypes.PREPROCESSOR_ELSEIF,
            PromelaMacroTypes.PREPROCESSOR_ELSE,
            PromelaMacroTypes.PREPROCESSOR_ENDIF,
        )

        /** Normal comments (line and block). */
        val NORMAL_COMMENTS = TokenSet.create(
            PromelaTypes.LINE_COMMENT,
            PromelaTypes.BLOCK_COMMENT,
        )

        /** All comments, including preprocessor directives. */
        val COMMENTS: TokenSet = TokenSet.orSet(
            PREPROCESSOR_DIRECTIVES,
            NORMAL_COMMENTS
        )

        /** String literals. */
        val STRINGS: TokenSet = TokenSet.create(PromelaTypes.STRING)

        /** All operations (assignment, comparison, arithmetic, etc.). */
        val OPERATIONS = TokenSet.create(
            PromelaTypes.OP_AND,
            PromelaTypes.OP_ASGN,
            PromelaTypes.OP_BAND,
            PromelaTypes.OP_BANG,
            PromelaTypes.OP_BOR,
            PromelaTypes.OP_DIV,
            PromelaTypes.OP_EQ,
            PromelaTypes.OP_GE,
            PromelaTypes.OP_GT,
            PromelaTypes.OP_LE,
            PromelaTypes.OP_LT,
            PromelaTypes.OP_MINUS,
            PromelaTypes.OP_MOD,
            PromelaTypes.OP_NEG,
            PromelaTypes.OP_NEQ,
            PromelaTypes.OP_OR,
            PromelaTypes.OP_PLUS,
            PromelaTypes.OP_SLEFT,
            PromelaTypes.OP_SRIGHT,
            PromelaTypes.OP_STAR,
            PromelaTypes.OP_XOR
        )
        val UNARY_OPERATIONS = TokenSet.create(
            PromelaTypes.OP_BANG,
            PromelaTypes.OP_MINUS,
            PromelaTypes.OP_NEG,
        )
        val KEYWORDS: TokenSet = TokenSet.create(
            PromelaTypes.RETURN_KW,
            PromelaTypes.ACTIVE_KW,
            PromelaTypes.INLINE_KW,
            PromelaTypes.ASSERT_KW,
            PromelaTypes.ATOMIC_KW,
            PromelaTypes.BIT_TYPE_KW,
            PromelaTypes.BOOL_TYPE_KW,
            PromelaTypes.BREAK_KW,
            PromelaTypes.BYTE_TYPE_KW,
            PromelaTypes.CHAN_TYPE_KW,
            PromelaTypes.C_CODE_KW,
            PromelaTypes.C_DECL_KW,
            PromelaTypes.C_EXPR_KW,
            PromelaTypes.C_STATE_KW,
            PromelaTypes.C_TRACK_KW,
            PromelaTypes.DO_END_KW,
            PromelaTypes.DO_KW,
            PromelaTypes.D_STEP_KW,
            PromelaTypes.ELSE_KW,
            PromelaTypes.ENABLED_KW,
            PromelaTypes.EVAL_KW,
            PromelaTypes.FOR_KW,
            PromelaTypes.GET_PRIORITY_KW,
            PromelaTypes.GOTO_KW,
            PromelaTypes.HIDDEN_KW,
            PromelaTypes.IF_END_KW,
            PromelaTypes.IF_KW,
            PromelaTypes.INIT_KW,
            PromelaTypes.INT_TYPE_KW,
            PromelaTypes.IN_KW,
            PromelaTypes.LEN_KW,
            PromelaTypes.LTL_KW,
            PromelaTypes.MTYPE_KW,
            PromelaTypes.NEVER_KW,
            PromelaTypes.NOTRACE_KW,
            PromelaTypes.NP_KW,
            PromelaTypes.OF_KW,
            PromelaTypes.PC_VALUE_KW,
            PromelaTypes.PRINTF_KW,
            PromelaTypes.PRINTM_KW,
            PromelaTypes.PRIORITY_KW,
            PromelaTypes.PROCTYPE_D_KW,
            PromelaTypes.PROCTYPE_KW,
            PromelaTypes.PROVIDED_KW,
            PromelaTypes.RUN_KW,
            PromelaTypes.SELECT_KW,
            PromelaTypes.SET_PRIORITY_KW,
            PromelaTypes.SHORT_TYPE_KW,
            PromelaTypes.SHOW_KW,
            PromelaTypes.TIMEOUT_KW,
            PromelaTypes.TRACE_KW,
            PromelaTypes.TYPEDEF_KW,
            PromelaTypes.UNLESS_KW,
            PromelaTypes.UNSIGNED_KW,
            PromelaTypes.XR_KW,
            PromelaTypes.XS_KW,
            PromelaTypes.LTL,
            PromelaTypes.CONST_FALSE,
            PromelaTypes.CONST_TRUE,
            PromelaTypes.CONST_SKIP,
            PromelaTypes.CHANPOLL_FULL,
            PromelaTypes.CHANPOLL_NFULL,
            PromelaTypes.CHANPOLL_EMPTY,
            PromelaTypes.CHANPOLL_NEMPTY,
        )
    }
}