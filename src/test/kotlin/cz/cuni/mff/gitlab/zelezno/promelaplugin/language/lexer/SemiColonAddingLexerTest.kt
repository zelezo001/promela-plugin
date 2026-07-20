package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer

import com.intellij.lang.TokenWrapper
import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SemiColonAddingLexerTest(
    private val inner: Lexer,
    private val advances: Int,
    private val expectedTokenType: IElementType?,
    private val expectedTokenStartOffset: Int,
    private val expectedTokenEndOffset: Int,
) {

    private lateinit var subject: SemicolonAddingLexer

    companion object {
        val BUFFER = "NOT_REALLY_USED"
        val TARGET_TYPE: IElementType = PromelaTypes.CHAN_TYPE_KW
        val DO_NOT_ADD_SEMI_AFTER = TokenSet.create(PromelaTypes.C_CODE, PromelaTypes.NUMBER)
        val DO_NOT_ADD_EOL_SEMI_AFTER = TokenSet.create(PromelaTypes.NUMBER)

        @JvmStatic
        @Parameterized.Parameters
        public fun parameters(): Collection<Array<Any?>> {
            return listOf(
                // no tokens
                arrayOf(
                    LexerMock(listOf()), 0, null, BUFFER.length, BUFFER.length,
                ),

                // no tokens with advances
                arrayOf(
                    LexerMock(listOf()), 2, null, BUFFER.length, BUFFER.length,
                ),

                // should not add after different token
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(PromelaTypes.STRING), LexerMock.MockToken(PromelaTypes.C_CODE, 1, 2)
                        )
                    ),
                    1, PromelaTypes.C_CODE, 1, 2,
                ),

                // should add SEMI
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TARGET_TYPE, 0, 1),
                            LexerMock.MockToken(PromelaTypes.STRING, 1, 2)
                        )
                    ),
                    1, PromelaTypes.SEMI, 1, 1,
                ),

                // should advance past SEMI
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TARGET_TYPE, 0, 1),
                            LexerMock.MockToken(PromelaTypes.STRING, 1, 2)
                        )
                    ),
                    2, PromelaTypes.STRING, 1, 2,
                ),

                // should add semi even when there are ignored tokens after target
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TARGET_TYPE, 0, 1),
                            LexerMock.MockToken(PromelaTypes.BLOCK_COMMENT),
                            LexerMock.MockToken(PromelaTypes.LINE_COMMENT),
                            LexerMock.MockToken(PromelaTypes.EOL),
                            LexerMock.MockToken(TokenType.WHITE_SPACE),
                            LexerMock.MockToken(PromelaTypes.STRING, 1, 2)
                        )
                    ),
                    5, PromelaTypes.SEMI, 1, 1,
                ),

                // should work with wrapped tokens
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TokenWrapper(TARGET_TYPE, ""), 0, 1),
                            LexerMock.MockToken(TokenWrapper(PromelaTypes.BLOCK_COMMENT, "")),
                            LexerMock.MockToken(TokenWrapper(PromelaTypes.LINE_COMMENT, "")),
                            LexerMock.MockToken(TokenWrapper(PromelaTypes.EOL, "")),
                            LexerMock.MockToken(TokenWrapper(TokenType.WHITE_SPACE, "")),
                            LexerMock.MockToken(TokenWrapper(PromelaTypes.STRING, ""), 1, 2)
                        )
                    ),
                    5, PromelaTypes.SEMI, 1, 1,
                ),

                // should add EOL_OR_SEMI
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TARGET_TYPE, 0, 1),
                            LexerMock.MockToken(PromelaTypes.EOL, 1, 2),
                            LexerMock.MockToken(PromelaTypes.C_CODE, 2, 3)
                        )
                    ),
                    2, PromelaTypes.EOL_OR_SEMI, 2, 2,
                ),

                // should add EOL_OR_SEMI even when there are ignored tokens after target
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TARGET_TYPE, 0, 1),
                            LexerMock.MockToken(PromelaTypes.BLOCK_COMMENT),
                            LexerMock.MockToken(PromelaTypes.LINE_COMMENT),
                            LexerMock.MockToken(PromelaTypes.EOL),
                            LexerMock.MockToken(TokenType.WHITE_SPACE),
                            LexerMock.MockToken(PromelaTypes.C_CODE, 1, 2)
                        )
                    ),
                    5, PromelaTypes.EOL_OR_SEMI, 1, 1,
                ),

                // should advance past EOL_OR_SEMI
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TARGET_TYPE, 0, 1),
                            LexerMock.MockToken(PromelaTypes.EOL, 1, 2),
                            LexerMock.MockToken(PromelaTypes.C_CODE, 2, 3)
                        )
                    ),
                    3, PromelaTypes.C_CODE, 2, 3,
                ),

                // should NOT add SEMI nor EOL_OR_SEMI before ignored
                arrayOf(
                    LexerMock(
                        listOf(
                            LexerMock.MockToken(TARGET_TYPE, 0, 1),
                            LexerMock.MockToken(PromelaTypes.NUMBER, 1, 2)
                        )
                    ),
                    1, PromelaTypes.NUMBER, 1, 2,
                )
            )
        }
    }

    @Before
    fun init() {
        subject = SemicolonAddingLexer(
            inner,
            TokenSet.create(TARGET_TYPE),
            DO_NOT_ADD_SEMI_AFTER,
            DO_NOT_ADD_EOL_SEMI_AFTER,
        )
        subject.start(BUFFER)
        for (i in 1..advances) {
            subject.advance()
        }
    }

    @Test
    fun `should match token type`() {
        Assert.assertEquals(expectedTokenType, subject.tokenType)
    }

    @Test
    fun `should match token start`() {
        Assert.assertEquals(expectedTokenStartOffset, subject.tokenStart)
    }

    @Test
    fun `should match token end`() {
        Assert.assertEquals(expectedTokenEndOffset, subject.tokenEnd)
    }
}

class LexerMock(
    private val tokens: List<MockToken>,
    private val state: Int = 0,
) : LexerBase() {
    private var endOffset = 0
    private var tokenIndex = 0
    private lateinit var buffer: CharSequence

    data class MockToken(val type: IElementType, val startOffset: Int = 0, val endOffset: Int = 0)

    override fun start(
        buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int
    ) {
        this.buffer = buffer
        tokenIndex = 0
        this.endOffset = endOffset
    }

    override fun getState(): Int {
        return state
    }

    override fun getTokenType(): IElementType? {
        if (tokenIndex >= tokens.size) return null
        return tokens[tokenIndex].type
    }

    override fun getTokenStart(): Int {
        if (tokenIndex >= tokens.size) return endOffset
        return tokens[tokenIndex].startOffset
    }

    override fun getTokenEnd(): Int {
        if (tokenIndex >= tokens.size) return endOffset
        return tokens[tokenIndex].endOffset
    }

    override fun advance() {
        tokenIndex++
    }

    override fun getBufferSequence(): CharSequence {
        return buffer
    }

    override fun getBufferEnd(): Int {
        return endOffset
    }
}