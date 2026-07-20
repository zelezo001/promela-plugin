package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi

/**
 * Element types for Promela preprocessor macros.
 */
object PromelaMacroTypes {
    /** Token for the end of a preprocessor directive. */
    @JvmField
    val PREPROCESSOR_DIRECTIVE_END = PromelaTokenType("preprocessor_directive_end")

    /** Token for #define directive. */
    @JvmField
    val PREPROCESSOR_DEFINE = PromelaTokenType("preprocessor_define")

    /** Token for #undef directive. */
    @JvmField
    val PREPROCESSOR_UNDEFINE = PromelaTokenType("preprocessor_undefine")

    /** Token for the name being defined in #define. */
    @JvmField
    val PREPROCESSOR_DEFINE_NAME = PromelaTokenType("preprocessor_define_name")

    /** Token representing a call to a defined macro. */
    @JvmField
    val PREPROCESSOR_DEFINE_CALL = PromelaTokenType("preprocessor_define_call")

    /** Token for #include directive. */
    @JvmField
    val PREPROCESSOR_INCLUDE = PromelaTokenType("preprocessor_include")


    /** Token for #endif directive. */
    @JvmField
    val PREPROCESSOR_ENDIF = PromelaTokenType("preprocessor_endif")

    /** Token for #if directive. */
    @JvmField
    val PREPROCESSOR_IF = PromelaTokenType("preprocessor_if")

    /** Token for #ifdef directive. */
    @JvmField
    val PREPROCESSOR_IFDEF = PromelaTokenType("preprocessor_ifdef")

    /** Token for #ifndef directive. */
    @JvmField
    val PREPROCESSOR_IFNDEF = PromelaTokenType("preprocessor_ifndef")

    /** Token for #elif directive. */
    @JvmField
    val PREPROCESSOR_ELSEIF = PromelaTokenType("preprocessor_elseif")

    /** Token for #else directive. */
    @JvmField
    val PREPROCESSOR_ELSE = PromelaTokenType("preprocessor_else")
}