package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenWrapper
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import kotlinx.collections.immutable.ImmutableList

/**
 * User-defined macro created by #define directive
 * @param name name of the macro (replaced identifier)
 * @param isFunctional if macro requires arguments
 * @param argumentCount number of arguments in a functional macro. Should be zero for [isFunctional] == false
 */
internal open class Macro(
    val name: String, val isFunctional: Boolean, val argumentCount: Int, private val tokens: ImmutableList<IElementType>
) {

    /**
     * Expands macro with given arguments.
     * If size of arguments is less than
     * @param arguments for argument replacement, must be
     * @return expanded macro
     */
    open fun expandMacro(arguments: List<List<IElementType>>): List<IElementType> {
        if (arguments.size != argumentCount) {
            return listOf(
                PromelaTokenWrapper(
                    TokenType.ERROR_ELEMENT, PromelaBundle.message("language.lexer.preprocessor.macro.invalidArgCount")
                )
            )
        }

        val expandedMacro = ArrayList<IElementType>(tokens.size)
        for (element in tokens) {
            if (element is MacroArgument) {
                expandedMacro.addAll(arguments[element.index])
            } else {
                expandedMacro.add(element)
            }
        }

        return expandedMacro
    }
}