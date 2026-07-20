package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor

import com.intellij.lexer.LexerBase
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaMacroTypes
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenWrapper
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes
import kotlinx.collections.immutable.toImmutableList
import java.util.*


/**
 * Lexer that handles Promela macros and preprocessor directives.
 * @param inner the base lexer to wrap
 */
class MacroAwareLexer(val inner: LexerBase) : LexerBase() {

    private companion object {
        val LOG = Logger.getInstance(MacroAwareLexer::class.java)
        const val DEFINED_MACRO_NAME = "defined"
    }

    /**
     * Defined macro for checking the existence of other macros inside if/elif
     */
    private val definedMacro =
        object : Macro(
            DEFINED_MACRO_NAME, true, 1,
            listOf<IElementType>(MacroArgument(0)).toImmutableList()
        ) {
            override fun expandMacro(arguments: List<List<IElementType>>): List<IElementType> {
                if (arguments.size != 1 && arguments[0].size != 1) {
                    return listOf(
                        PromelaTokenWrapper(
                            TokenType.ERROR_ELEMENT,
                            PromelaBundle.message("language.lexer.preprocessor.macro.invalidArgCount")
                        )
                    )
                }
                val value = (arguments[0][0] as? PromelaTokenWrapper)?.text.let {
                    if (knownMacros.contains(it)) {
                        "1"
                    } else {
                        "0"
                    }
                }

                val arguments = listOf(
                    listOf<IElementType>(
                        PromelaTokenWrapper(PromelaTypes.NUMBER, value)
                    )
                )
                return super.expandMacro(arguments)
            }
        }

    /**
     * All known macros, even if they are temporary disables
     */
    private val knownMacros: MutableSet<String> = HashSet()
    private val macros: HashMap<String, Macro> = HashMap()

    /**
     * Tokens expanded from macros introduced with #define
     */
    private val expandedMacroTokens: Stack<IElementType> = Stack()


    private data class Token(val type: IElementType?, val start: Int, val end: Int)

    /**
     * Current token showed by this lexer
     */
    private lateinit var currentToken: Token

    private fun buildTokenFromInner(): Token {
        if (this.currentBranchTaken || inner.tokenType == null) return Token(
            inner.tokenType,
            inner.tokenStart,
            inner.tokenEnd
        )
        return Token(PromelaTypes.DISABLED_CODE, inner.tokenStart, inner.tokenEnd)
    }

    /**
     * Starts the macro-aware lexer
     * Must always start at offset 0
     * @see [LexerBase.start]
     */
    override fun start(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
        if (p1 != 0) throw IllegalArgumentException("macro aware lexer must always lex the whole file")
        inner.start(p0, p1, p2)
        currentToken = processAdvancedInner()
    }

    override fun getState(): Int = inner.state
    override fun getTokenStart() = currentToken.start
    override fun getTokenEnd() = currentToken.end

    override fun getTokenType() = currentToken.type

    /**
     * Returns token text, can differ from text range if token is [PromelaTokenWrapper]
     */
    override fun getTokenText() = when (currentToken.type) {
        is PromelaTokenWrapper -> (currentToken.type as PromelaTokenWrapper).text
        else -> super.getTokenText()
    }

    /**
     * [tokenType] which is unwrapped to internal token for [PromelaTokenWrapper]
     */
    internal val underlyingTokenType
        get() = when (currentToken.type) {
            is PromelaTokenWrapper -> (currentToken.type as PromelaTokenWrapper).delegate
            else -> currentToken.type
        }

    private fun collectMacroArgument(): List<IElementType> {
        var braces = 0
        val argumentTokens = mutableListOf<IElementType>()
        while (tokenType != null) {
            when (tokenType) {
                PromelaTypes.LINE_COMMENT, PromelaTypes.BLOCK_COMMENT, TokenType.WHITE_SPACE, PromelaTypes.PREPROCESSOR_COMMENT, PromelaMacroTypes.PREPROCESSOR_DEFINE_CALL -> {
                }

                PromelaTypes.OPENBRACKET -> {
                    braces++
                    argumentTokens.add(tokenType!!)
                }

                PromelaTypes.CLOSEBRACKET -> {
                    if (braces > 0) {
                        braces--
                        argumentTokens.add(tokenType!!)
                    } else {
                        // end of list
                        return argumentTokens
                    }
                }

                PromelaTypes.COMMA -> {
                    if (braces == 0) {
                        return argumentTokens // end of this argument
                    }

                    argumentTokens.add(PromelaTokenWrapper(tokenType!!, tokenText))
                }

                PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END -> return argumentTokens
                is PromelaTokenWrapper -> argumentTokens.add(tokenType!!)
                else -> argumentTokens.add(PromelaTokenWrapper(tokenType!!, tokenText))
            }
            advance()
        }

        return argumentTokens
    }

    private val processedTokens: LinkedList<Token> = LinkedList<Token>()

    private fun collectWhitespacesAndComments(eatenTokens: Queue<Token>?) {
        while (true) {
            when (tokenType) {
                TokenType.WHITE_SPACE, PromelaTypes.BLOCK_COMMENT, PromelaTypes.LINE_COMMENT, PromelaTypes.EOL -> {
                    eatenTokens?.add(currentToken)
                    advance()
                }

                else -> return
            }
        }
    }

    private fun consumeCommentsAndWhitespacesFromInner() {
        while (true) {
            when (inner.tokenType) {
                TokenType.WHITE_SPACE, PromelaTypes.BLOCK_COMMENT, PromelaTypes.LINE_COMMENT, PromelaTypes.EOL -> inner.advance()

                else -> return
            }
        }
    }

    private fun handleMacros() {
        when (tokenType) {
            PromelaTypes.DISABLED_CODE, PromelaTypes.PREPROCESSOR_COMMENT -> return
        }
        val macro = macros[tokenText] ?: return

        // prevent recursive macro calls
        if (!macro.isFunctional) {
            // macro was defined as #define X y

            macros.remove(tokenText)
            expandMacro(macro, emptyList())

            currentToken = currentToken.copy(type = PromelaMacroTypes.PREPROCESSOR_DEFINE_CALL)
            return
        }
        val expandedToken = currentToken
        advance() // consume current token

        // keep consumed (and processed by this lexer) tokens so we can return them when needed
        val consumedProcessedTokens = LinkedList<Token>()
        collectWhitespacesAndComments(consumedProcessedTokens) // collect "ignored" tokens after macro name

        var startFrom = tokenStart
        if (currentToken.type != PromelaTypes.OPENBRACKET) {
            // it was not a macro call, return tokens as nothing happened
            processedTokens.add(expandedToken)
            processedTokens.addAll(consumedProcessedTokens)
            if (currentToken.type != null) {
                processedTokens.add(currentToken)
            }
            advance() // so we reset lexer to expandedToken
            return
        }

        // it is macro call, let's process arguments from `macroName(arg1,arg2)
        consumedProcessedTokens.addFirst(expandedToken.copy(type = PromelaMacroTypes.PREPROCESSOR_DEFINE_CALL))
        advance() // consume (
        val arguments = mutableListOf<List<IElementType>>()
        collectWhitespacesAndComments(null)
        while (currentToken.type != null
            // do not use TokenSet as that does not respect WrappedTokens
            && currentToken.type !in listOf(
                PromelaTypes.CLOSEBRACKET, PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END
            ) // unclosed call from macro if directive
        ) {
            if (arguments.isNotEmpty()) {
                if (currentToken.type != PromelaTypes.COMMA) {
                    // bug, arguments must collect until ',', ')' or EOF
                    LOG.error("unexpected token ${currentToken.type} occurred after consuming macro argument at offset ${inner.tokenStart}")
                } else {
                    advance()
                }
            }
            val argument = collectMacroArgument()
            if (argument.isEmpty()) {
                // handle error by inserting error element
                consumedProcessedTokens.add(Token(PromelaTypes.PREPROCESSOR_COMMENT, startFrom, tokenEnd))
                startFrom = tokenEnd
                consumedProcessedTokens.add(
                    lexerError(tokenEnd, PromelaBundle.message("language.lexer.preprocessor.macro.emptyArgument"))
                )
            }
            arguments.add(argument)
        }

        if (currentToken.type != PromelaTypes.CLOSEBRACKET) {
            // handle error by inserting error element
            consumedProcessedTokens.add(Token(PromelaTypes.PREPROCESSOR_COMMENT, startFrom, tokenEnd))
            startFrom = tokenEnd
            consumedProcessedTokens.add(
                lexerError(tokenEnd, PromelaBundle.message("language.lexer.preprocessor.macro.unclosed"))
            )
        }

        consumedProcessedTokens.add(Token(PromelaTypes.PREPROCESSOR_COMMENT, startFrom, tokenEnd))
        processedTokens.addAll(consumedProcessedTokens)

        expandMacro(macro, arguments)

        advance() // consume ')' and start reading consumed tokens
    }

    /**
     * Add expanded macro and macro reenabling token to the expandedMacroTokens in the correct order
     */
    private fun expandMacro(macro: Macro, arguments: List<List<IElementType>>) {
        // put macro back before expanded content
        expandedMacroTokens.add(EnableMacroElementType(macro))
        // expand macro
        macro.expandMacro(arguments).asReversed().forEach { expandedMacroTokens.add(it) }
    }

    private fun advanceInnerTillEndOfPreprocessorDirective() {
        while (true) {
            when (inner.tokenType) {
                PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END, null -> return
            }
            inner.advance()
        }
    }

    /**
     * Creates zero-length error token of type [TokenType.ERROR_ELEMENT] intercepted by
     *  [cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.parser.LexerErrorHandlingPsiBuilder]
     *  @param at position of the token
     *  @param message message shown to the user
     */
    private fun lexerError(at: Int, message: String): Token {
        return Token(
            PromelaTokenWrapper(TokenType.ERROR_ELEMENT, message), at, at
        )
    }

    /**
     * Session of parsing one directive (e.g., #define). Handles correct error placement and
     * token propagation
     */
    private class DirectiveParsingSession(private val lexer: MacroAwareLexer) {
        private val inner = lexer.inner
        private var macroTokenStart = inner.tokenStart

        private fun coverRangeInPreprocessorComment(end: Int): Token {
            val start = macroTokenStart
            macroTokenStart = end

            return Token(PromelaTypes.PREPROCESSOR_COMMENT, start, end)
        }

        /**
         * Context-aware function for creates parts of macro as [PromelaTypes.PREPROCESSOR_COMMENT]
         * Always creates token in [previousTokenEnd, inner.tokenEnd) or [previousTokenEnd, inner.tokenStart)
         *
         * @return [Token] of type [PromelaTypes.PREPROCESSOR_COMMENT] spanning over [previousTokenEnd, inner.tokenEnd)
         */
        fun coverInPreprocessorComment(): Token {
            return coverRangeInPreprocessorComment(inner.tokenEnd)
        }

        /**
         * Places error at [errorAt] and consumes the rest of the declaration as [PromelaTypes.PREPROCESSOR_COMMENT]
         * and stores both in [MacroAwareLexer.processedTokens]
         * @return [PromelaTypes.PREPROCESSOR_COMMENT] for the part predecessing the error.
         *
         * @param error error message
         * @param errorAt position of the error
         * @throws IllegalArgumentException if position is not in the processed but uncovered text range
         */
        fun finishWithErrorAt(error: String, errorAt: Int): Token {
            if (macroTokenStart > errorAt || errorAt > inner.tokenEnd) {
                throw IllegalArgumentException("error must be in currently processed text range")
            }
            // create prefix
            val finalToken = coverRangeInPreprocessorComment(errorAt)

            // collect rest of the #define
            lexer.advanceInnerTillEndOfPreprocessorDirective()
            lexer.processedTokens.addFirst(coverInPreprocessorComment())

            // report error
            lexer.processedTokens.addFirst(lexer.lexerError(errorAt, error))
            return finalToken
        }

        /**
         * Places error at the start of the current token end consumes the rest of the declaration as [PromelaTypes.PREPROCESSOR_COMMENT]
         * and stores both in [MacroAwareLexer.processedTokens]
         * Returns [PromelaTypes.PREPROCESSOR_COMMENT] for the part predecessing the error.
         */
        fun finishWithError(error: String): Token {
            return finishWithErrorAt(error, inner.tokenStart)
        }
    }

    private fun buildMacro(): Token {
        val session = DirectiveParsingSession(this)

        inner.advance() // eat #define
        consumeCommentsAndWhitespacesFromInner() // eat whitespaces between define directive and name
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DEFINE_NAME) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.define.invalid"))
        }

        val name = inner.tokenText
        if (name.isEmpty()) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.define.invalid"))
        }
        if (macros.containsKey(name) || name == DEFINED_MACRO_NAME) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.define.duplicate", name))
        }
        val builder = MacroBuilder()
        builder.name = name

        inner.advance() // consume the name token
        // do not call consumeWhitespacesFromInner here, as functional macro name must be immediately succeeded by (

        if (inner.tokenType == PromelaTypes.OPENBRACKET) {
            inner.advance() //consume (
            builder.isFunctional = true
            consumeCommentsAndWhitespacesFromInner()
            while (inner.tokenType != PromelaTypes.CLOSEBRACKET && inner.tokenType != null) {
                if (builder.hasArguments) {
                    if (inner.tokenType != PromelaTypes.COMMA) {
                        return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.define.args.separator"))
                    }

                    inner.advance() // consume comma
                    consumeCommentsAndWhitespacesFromInner()
                }
                if (inner.tokenType != PromelaTypes.UNAME) {
                    return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.define.args.identifier"))
                }

                if (!builder.addArgument(inner.tokenText)) {
                    return session.finishWithError(
                        PromelaBundle.message(
                            "language.lexer.preprocessor.define.args.duplicate",
                            inner.tokenText
                        )
                    )
                }
                inner.advance() // consume uname
                consumeCommentsAndWhitespacesFromInner()
            }

            if (inner.tokenType != PromelaTypes.CLOSEBRACKET) {
                return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.define.args.missingBracket"))
            }
            inner.advance() // eat )
        }

        while (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END && inner.tokenType != null) {
            when (inner.tokenType) {
                TokenType.WHITE_SPACE, PromelaTypes.BLOCK_COMMENT, PromelaTypes.LINE_COMMENT -> {} // ignore comments
                else -> builder.addToken(inner.tokenType!!, inner.tokenSequence)
            }
            inner.advance()
        }
        val macro = builder.tryBuild()!! // must be not null as we set the name when creating the builder
        macros[macro.name] = macro
        knownMacros.add(macro.name)

        return session.coverInPreprocessorComment()
    }

    private data class IfContext(val branchAlreadyTaken: Boolean, val currentBranchTaken: Boolean) {}

    private fun undefineMacro(): Token {
        val session = DirectiveParsingSession(this)

        inner.advance() // consume undef
        consumeCommentsAndWhitespacesFromInner() // consume till name
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DEFINE_NAME) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.undefine.invalid"))
        }
        val macroName = inner.tokenText
        inner.advance() // consume name
        consumeCommentsAndWhitespacesFromInner() // consume whitespaces after macro name
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END && inner.tokenType != null) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.undefine.invalid"))
        }
        macros.remove(macroName)
        knownMacros.remove(macroName)

        return session.coverInPreprocessorComment()
    }

    private val ifStack: Stack<IfContext> = Stack()
    private fun handleIfDef(defined: Boolean): Token {
        val session = DirectiveParsingSession(this)

        inner.advance() // consume ifdef/ifndef
        consumeCommentsAndWhitespacesFromInner() // consume till
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DEFINE_NAME) {
            advanceInnerTillEndOfPreprocessorDirective()
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.invalid"))
        }
        val macroName = inner.tokenText
        inner.advance() // consume name
        consumeCommentsAndWhitespacesFromInner() // consume whitespaces after macro name
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END && inner.tokenType != null) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.invalid"))
        }
        val branchTaken = (macroName in macros) == defined
        ifStack.push(IfContext(branchAlreadyTaken = branchTaken, currentBranchTaken = branchTaken))

        return session.coverInPreprocessorComment()
    }

    private fun handleIf(): Token {
        val session = DirectiveParsingSession(this)

        if (ifStack.isNotEmpty() && !ifStack.peek().currentBranchTaken) {
            // if inside not taken branch
            // create context for following else/elif/endif but setup it so it will never collect tokens
            ifStack.push(IfContext(branchAlreadyTaken = true, currentBranchTaken = false))
            advanceInnerTillEndOfPreprocessorDirective()
            return Token(PromelaTypes.PREPROCESSOR_COMMENT, inner.tokenStart, inner.tokenEnd)
        }

        return handleCondition(session)
    }

    private fun handleCondition(session: DirectiveParsingSession): Token {
        inner.advance() // consume #if

        assert(processedTokens.isEmpty())
        assert(expandedMacroTokens.isEmpty())

        consumeCommentsAndWhitespacesFromInner()
        var token: Token? = null // token to return
        var branchTaken = false
        macros[definedMacro.name] = definedMacro // register macro
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END && inner.tokenType != null) {
            // manually handle token setup (used by ExpressionParser)
            currentToken = buildTokenFromInner()
            handleMacros()

            val constantExpressionParser = ExpressionParser(this)
            branchTaken = constantExpressionParser.parse() != 0
            var i = 0
            while (tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END && tokenType != null) {
                if (tokenType !in PromelaTokenSets.COMMENTS) {
                    i++
                }
                advance()
            }
            assert(processedTokens.isEmpty())
            assert(expandedMacroTokens.isEmpty())
            if (constantExpressionParser.hasError) {
                branchTaken = false
                val errorRange = constantExpressionParser.errorRange!!
                token = session.finishWithErrorAt(constantExpressionParser.errorMessage!!, errorRange.startOffset)
            } else if (i > 0) {
                token = session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.invalid"))
            }
        }

        ifStack.push(IfContext(branchTaken, branchTaken))

        macros.remove(definedMacro.name) // remove defined macro as its only available in conditions

        return token ?: session.coverInPreprocessorComment()
    }

    private fun handleElseIf(): Token {
        val session = DirectiveParsingSession(this)

        if (ifStack.empty()) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.elif.orphan"))
        }

        val context = ifStack.pop()
        if (context.branchAlreadyTaken) {
            // some previous branch already taken, skip this one
            ifStack.push(context.copy(currentBranchTaken = false))

            return session.coverInPreprocessorComment()
        }
        // handle as normal if
        return handleCondition(session)
    }

    private fun handleElse(): Token {
        val session = DirectiveParsingSession(this)

        if (ifStack.empty()) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.else.orphan"))
        }

        val context = ifStack.pop()
        if (context.branchAlreadyTaken) {
            // some previous branch already taken, skip this one
            ifStack.push(context.copy(currentBranchTaken = false))
        } else {
            ifStack.push(context.copy(currentBranchTaken = true, branchAlreadyTaken = true))
        }

        inner.advance() // eat else
        advanceInnerTillEndOfPreprocessorDirective()
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END && inner.tokenType != null) {
            return session.finishWithError(PromelaBundle.message("language.lexer.preprocessor.else.invalid"))
        }

        return session.coverInPreprocessorComment()
    }

    /**
     * Closes the current conditional compilation scope
     */
    private fun handleEndIf(): Token {
        val session = DirectiveParsingSession(this)
        inner.advance() // eat endif
        if (ifStack.empty()) {
            return session.finishWithErrorAt(
                PromelaBundle.message("language.lexer.preprocessor.endif.orphan"),
                inner.tokenEnd
            )
        }
        ifStack.pop()
        consumeCommentsAndWhitespacesFromInner()
        if (inner.tokenType != PromelaMacroTypes.PREPROCESSOR_DIRECTIVE_END && inner.tokenType != null) {
            return session.finishWithErrorAt(
                PromelaBundle.message("language.lexer.preprocessor.endif.invalid"),
                inner.tokenEnd
            )
        }

        return session.coverInPreprocessorComment()
    }

    /**
     * Bool if current tokens from inner should be taken as given or if they should be recasted to [PromelaTypes.DISABLED_CODE]
     */
    private val currentBranchTaken: Boolean
        get() = ifStack.isEmpty() || ifStack.peek().currentBranchTaken

    /**
     * Handles preprocessor directives advanced token from inner
     */
    private fun processAdvancedInner(): Token {
        return when (inner.tokenType) {
            PromelaMacroTypes.PREPROCESSOR_IF -> handleIf()
            PromelaMacroTypes.PREPROCESSOR_IFDEF -> handleIfDef(true)
            PromelaMacroTypes.PREPROCESSOR_IFNDEF -> handleIfDef(false)
            PromelaMacroTypes.PREPROCESSOR_ELSEIF -> handleElseIf()
            PromelaMacroTypes.PREPROCESSOR_ELSE -> handleElse()
            PromelaMacroTypes.PREPROCESSOR_ENDIF -> handleEndIf()
            PromelaMacroTypes.PREPROCESSOR_UNDEFINE -> {
                if (currentBranchTaken) {
                    undefineMacro()
                } else {
                    val start = inner.tokenStart
                    advanceInnerTillEndOfPreprocessorDirective()
                    Token(PromelaTypes.DISABLED_CODE, start, inner.tokenEnd)
                }
            }

            PromelaMacroTypes.PREPROCESSOR_DEFINE -> {
                if (currentBranchTaken) {
                    buildMacro()
                } else {
                    val start = inner.tokenStart
                    advanceInnerTillEndOfPreprocessorDirective()
                    Token(PromelaTypes.DISABLED_CODE, start, inner.tokenEnd)
                }
            }

            else -> buildTokenFromInner()
        }
    }

    /**
     * Advances inner lexer and processes any returned preprocessor directives
     */
    private fun advanceInner(): Token {
        if (inner.tokenType == null) {
            return buildTokenFromInner()
        }
        inner.advance()
        return processAdvancedInner()
    }

    /**
     * Resolves next token which should be processed
     */
    private fun nextToken(): Token {
        // first we will return all expanded tokens
        while (expandedMacroTokens.isNotEmpty()) {
            val token = expandedMacroTokens.pop()
            if (token is EnableMacroElementType) {
                // token signals, that macro can be safely interpreted without fear of recursion
                macros[token.macro.name] = token.macro
                continue
            }
            // expanded tokens are always zero-length
            return Token(token, inner.tokenEnd, inner.tokenEnd)
        }

        return advanceInner()
    }

    override fun advance() {
        if (processedTokens.isNotEmpty()) {
            currentToken = processedTokens.pop()
        } else {
            currentToken = nextToken()
            handleMacros()
        }
    }

    override fun getBufferSequence() = inner.bufferSequence

    override fun getBufferEnd() = inner.bufferEnd
}

/**
 * Placeholder [IElementType] used inside [Macro] for argument substitution
 */
internal class MacroArgument(val index: Int) : IElementType("name", null, false)

/**
 * Internal [IElementType] used for disabling macros inside their expansion (protection against recursive calls)
 */
private class EnableMacroElementType(val macro: Macro) : IElementType("macro ${macro.name}", null, false)