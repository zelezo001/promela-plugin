package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor

import com.intellij.psi.tree.IElementType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenWrapper
import kotlinx.collections.immutable.toImmutableList

internal class MacroBuilder {
    private val content = mutableListOf<IElementType>()
    private val arguments = mutableMapOf<String, Int>()

    /**
     * true if any argument was added by [addArgument]
     */
    val hasArguments get() = arguments.isNotEmpty()

    /**
     * Name of the macro, see [Macro.name]
     */
    var name: String? = null

    /**
     * Name of the macro, see [Macro.isFunctional]
     */
    var isFunctional = false

    /**
     * @param name added new argument to macro
     * @return false for duplicate argument name
     */
    fun addArgument(name: String): Boolean {
        if (arguments.containsKey(name)) {
            return false
        }
        arguments[name] = arguments.size
        return true
    }

    /**
     * Adds token as a macro.
     * @param elType Token type must not be wrapper type.
     * @param sequence Token text content, it is kept due to nested macro calls.
     */
    fun addToken(elType: IElementType, sequence: CharSequence) {
        val argumentIndex = arguments[sequence]
        val substituteToken = if (argumentIndex != null) MacroArgument(argumentIndex) else PromelaTokenWrapper(
            elType,
            sequence,
        )
        content.add(substituteToken)
    }

    /**
     * Builds macro from collected tokens and arguments.
     * @return Macro or null if a macro construction is invalid
     */
    fun tryBuild(): Macro? {
        if (name == null) {
            return null
        }
        return Macro(name!!, isFunctional, arguments.size, content.toImmutableList())
    }
}