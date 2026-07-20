package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.impl.PsiBuilderAdapter
import com.intellij.psi.TokenType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor.MacroAwareLexer
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenWrapper

/**
 * Psi builder which interprets [PromelaTokenWrapper] tokens with delegate [TokenType.ERROR_ELEMENT] as errors
 * and reports them without interfering with the grammer. Mainly used for errors related to preprocessor handled by [MacroAwareLexer].
 * @param delegate the original PSI builder
 */
internal class LexerErrorHandlingPsiBuilder(delegate: PsiBuilder) : PsiBuilderAdapter(delegate) {

    /**
     * Handles and skips all relevant [com.intellij.psi.TokenType.ERROR_ELEMENT]
     */
    private fun reportMessage() {
        while (true) {
            val wrapped = super.tokenType as? PromelaTokenWrapper ?: return
            if (wrapped.delegate != TokenType.ERROR_ELEMENT) return
            this.error(wrapped.text)
            super.advanceLexer()
        }
    }

    override fun advanceLexer() {
        reportMessage()
        super.advanceLexer()
    }
}