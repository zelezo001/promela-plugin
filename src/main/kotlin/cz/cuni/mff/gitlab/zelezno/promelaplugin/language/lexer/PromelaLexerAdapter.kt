package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer

import com.intellij.lexer.FlexAdapter
import cz.cuni.mff.gitlab.zelezno.promelaplugin.parser.PromelaLexer

/**
 * Lexer adapter for base lexer implemented in JFlex
 */
class PromelaLexerAdapter : FlexAdapter(PromelaLexer(null))