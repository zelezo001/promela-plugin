package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor

import com.intellij.openapi.util.TextRange
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaMacroTypes
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Parser for expressions used inside #if/#elif preprocessor directives
 */
internal class ExpressionParser(
    val lexer: MacroAwareLexer
) {

    /**
     * Range with error in [errorMessage]
     */
    var errorRange: TextRange? = null
        private set

    /**
     * Message for error located in [errorRange], must be user-readable
     */
    var errorMessage: String? = null
        private set

    /**
     * True if there is an error in [errorRange]
     */
    val hasError: Boolean get() = errorRange != null

    private fun advanceLexer() {
        lexer.advance()
        removeIgnoredChars()
    }

    private fun removeIgnoredChars() {
        while (
            lexer.tokenType in TokenSet.WHITE_SPACE ||
            lexer.tokenType == PromelaTypes.EOL ||
            lexer.tokenType in PromelaTokenSets.COMMENTS &&
            lexer.tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END
        ) {
            lexer.advance()
        }
    }

    /**
     * Records error. Only the first encountered error is reported.
     * @param message user-readable message
     * @param range If null, current token range is taken from [lexer]
     */
    private fun error(message: String, range: TextRange? = null) {
        if (errorRange == null) {
            errorMessage = message
            errorRange = range ?: TextRange(lexer.tokenStart, lexer.tokenEnd)
        }
    }

    companion object {
        private fun boolToInt(a: Boolean) = if (a) 1 else 0
    }

    private class InvalidOperationException(message: String) : Exception(message)

    private var highestOperation: OperationPriority =
        OperationPriority.BinaryOperation(
            operations = mapOf(
                PromelaTypes.OP_OR to { a: Int, b: Int ->
                    if (a == 0 && b == 0) {
                        0
                    } else {
                        1
                    }
                },
            ),
            inner = OperationPriority.BinaryOperation(
                operations = mapOf(
                    PromelaTypes.OP_AND to { a: Int, b: Int ->
                        if (a == 0 || b == 0) {
                            0
                        } else {
                            1
                        }
                    },
                ),
                inner = OperationPriority.BinaryOperation(
                    operations = mapOf(
                        PromelaTypes.OP_BOR to Int::or,
                    ),
                    inner = OperationPriority.BinaryOperation(
                        operations = mapOf(
                            PromelaTypes.OP_XOR to Int::xor,
                        ),
                        inner = OperationPriority.BinaryOperation(
                            operations = mapOf(
                                PromelaTypes.OP_BAND to Int::and,
                            ),
                            inner = OperationPriority.BinaryOperation(
                                operations = mapOf(
                                    PromelaTypes.OP_NEQ to { a: Int, b: Int -> boolToInt(a != b) },
                                    PromelaTypes.OP_EQ to { a: Int, b: Int -> boolToInt(a == b) },
                                ),
                                inner = OperationPriority.BinaryOperation(
                                    operations = mapOf(
                                        PromelaTypes.OP_LE to { a: Int, b: Int -> boolToInt(a <= b) },
                                        PromelaTypes.OP_LT to { a: Int, b: Int -> boolToInt(a < b) },
                                        PromelaTypes.OP_GE to { a: Int, b: Int -> boolToInt(a >= b) },
                                        PromelaTypes.OP_GT to { a: Int, b: Int -> boolToInt(a > b) },
                                    ),
                                    inner = OperationPriority.BinaryOperation(
                                        operations = mapOf(
                                            PromelaTypes.OP_SLEFT to Int::shl,
                                            PromelaTypes.OP_SRIGHT to Int::shr,
                                        ),
                                        inner = OperationPriority.BinaryOperation(
                                            operations = mapOf(
                                                PromelaTypes.OP_PLUS to Int::plus,
                                                PromelaTypes.OP_MINUS to Int::minus,
                                            ),
                                            inner = OperationPriority.BinaryOperation(
                                                operations = mapOf(
                                                    PromelaTypes.OP_STAR to Int::times,
                                                    PromelaTypes.OP_DIV to { a: Int, b: Int ->
                                                        if (b == 0) throw InvalidOperationException(
                                                            PromelaBundle.message("language.lexer.preprocessor.expression.invalid.divisionByZero")
                                                        )
                                                        a / b
                                                    },
                                                    PromelaTypes.OP_MOD to Int::rem,
                                                ),
                                                inner = OperationPriority.UnaryPrefixOperations(
                                                    operations = mapOf(
                                                        PromelaTypes.OP_BANG to { a: Int -> boolToInt(a == 0) },
                                                        PromelaTypes.OP_PLUS to Int::unaryPlus,
                                                        PromelaTypes.OP_MINUS to Int::unaryMinus,
                                                        PromelaTypes.OP_NEG to Int::inv,
                                                    ),
                                                    inner = OperationPriority.IdentifierOrNumber()
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

    private interface OperationPriority {
        fun eval(parser: ExpressionParser): Int

        class UnaryPrefixOperations(
            val operations: Map<IElementType, Int.() -> Int>,
            val inner: OperationPriority,
        ) : OperationPriority {
            override fun eval(parser: ExpressionParser): Int {
                val opType = parser.lexer.underlyingTokenType

                val op = opType?.let {
                    operations[it]
                }
                val right = if (op != null) {
                    parser.advanceLexer() // consume operation
                    eval(parser)
                } else {
                    inner.eval(parser)
                }
                if (parser.hasError) {
                    return 0
                }
                return op?.invoke(right) ?: right
            }
        }

        /**
         * @param operations operation should throw [InvalidOperationException] on error
         */
        class BinaryOperation(
            private val operations: Map<IElementType, Int.(Int) -> Int>,
            private val inner: OperationPriority,
        ) : OperationPriority {
            override fun eval(parser: ExpressionParser): Int {
                val left = inner.eval(parser)
                if (parser.hasError) {
                    return 0
                }
                val opType = parser.lexer.underlyingTokenType ?: return left
                val op = operations[opType] ?: return left // unknown op
                val opRange = TextRange(parser.lexer.tokenStart, parser.lexer.tokenEnd)
                parser.advanceLexer() // eat op
                val right = eval(parser)
                if (parser.hasError) {
                    return 0
                }

                return try {
                    left.op(right)
                } catch (exception: InvalidOperationException) {
                    parser.error(
                        exception.message
                            ?: PromelaBundle.message("language.lexer.preprocessor.expression.invalid.operation"),
                        opRange
                    )
                    0
                }
            }
        }

        class IdentifierOrNumber : OperationPriority {
            override fun eval(parser: ExpressionParser): Int {
                return when (parser.lexer.underlyingTokenType) {
                    PromelaTypes.OPENBRACKET -> {
                        parser.advanceLexer() //eat )
                        val inner = parser.highestOperation.eval(parser)
                        if (parser.hasError) {
                            return 0
                        }
                        if (parser.lexer.tokenType != PromelaTypes.CLOSEBRACKET) {
                            parser.error(
                                PromelaBundle.message("language.lexer.preprocessor.expression.invalid.unclosed")
                            )
                            0
                        } else {
                            parser.advanceLexer() // eat )
                            inner
                        }
                    }

                    PromelaTypes.CONST_FALSE -> {
                        parser.advanceLexer() // eat false
                        0
                    }

                    PromelaTypes.CONST_TRUE -> {
                        parser.advanceLexer() // eat true
                        1
                    }

                    PromelaTypes.NUMBER -> {
                        parseNumber(parser).also {
                            parser.advanceLexer() // eat number
                        }
                    }

                    PromelaTypes.UNAME -> {
                        // unknown identifier, expand as 0
                        parser.advanceLexer() // eat uname
                        0
                    }

                    PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END, null -> {
                        parser.error(PromelaBundle.message("language.lexer.preprocessor.expression.invalid.unclosed"))
                        0
                    }

                    else -> {
                        parser.error(PromelaBundle.message("language.lexer.preprocessor.expression.invalid.token"))
                        0
                    }
                }
            }

            private fun parseNumber(parser: ExpressionParser): Int {
                val tokenText = parser.lexer.tokenText
                if (tokenText.startsWith('\'')) {
                    if (!tokenText.endsWith('\'')) {
                        parser.error(PromelaBundle.message("language.lexer.preprocessor.expression.invalid.char"))
                        return 0
                    }
                    return when (val charValue = tokenText.substring(1, tokenText.length - 1)) {
                        "\\n" -> '\n'.code
                        "\\t" -> '\t'.code
                        "\\r" -> '\r'.code
                        else -> {
                            if (charValue.count() == 1) {
                                charValue[0].code
                            } else {
                                parser.error(PromelaBundle.message("language.lexer.preprocessor.expression.invalid.char"))
                                0
                            }
                        }
                    }
                }
                val parsed = tokenText.toIntOrNull()
                if (parsed == null) {
                    parser.error(PromelaBundle.message("language.lexer.preprocessor.expression.invalid.number"))
                    return 0
                }
                return parsed
            }
        }
    }

    fun parse(): Int {
        removeIgnoredChars()
        return this.highestOperation.eval(this)
    }
}