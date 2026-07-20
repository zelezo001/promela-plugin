package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.hightlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.util.NlsContexts
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFileType
import javax.swing.Icon


// this would be normally in companion object, but that violates Intellij companion object guidelines for DI loaded objects
internal val DESCRIPTORS: Array<AttributesDescriptor> = arrayOf(
    AttributesDescriptor("Preprocessor", PromelaSyntaxHighlighter.MACRO),
    AttributesDescriptor("Keyword", PromelaSyntaxHighlighter.KEYWORD),
    AttributesDescriptor("Line comment", PromelaSyntaxHighlighter.LINE_COMMENT),
    AttributesDescriptor("Block comment", PromelaSyntaxHighlighter.BLOCK_COMMENT),
    AttributesDescriptor("Parentheses", PromelaSyntaxHighlighter.PARENTHESES),
    AttributesDescriptor("Braces", PromelaSyntaxHighlighter.BRACES),
    AttributesDescriptor("Semicolon/arrow", PromelaSyntaxHighlighter.SEMICOLON),
    AttributesDescriptor("String", PromelaSyntaxHighlighter.STRING),
    AttributesDescriptor("Number", PromelaSyntaxHighlighter.NUMBER),
    AttributesDescriptor("Operation", PromelaSyntaxHighlighter.OPERATION_SIGN),
    AttributesDescriptor("Boolean value", PromelaSyntaxHighlighter.TRUTH_VALUE),
    AttributesDescriptor("Brackets", PromelaSyntaxHighlighter.BRACKETS),

    AttributesDescriptor("Proctype declaration", PromelaSyntaxHighlighter.PROCTYPE_DECLARATION),
    AttributesDescriptor("Proctype run", PromelaSyntaxHighlighter.PROCTYPE_RUN),
    AttributesDescriptor("Inline declaration", PromelaSyntaxHighlighter.INLINE_DECLARATION),
    AttributesDescriptor("Inline call", PromelaSyntaxHighlighter.INLINE_CALL),
    AttributesDescriptor("Embedded C code", PromelaSyntaxHighlighter.C_CODE),
)

/**
 * [ColorSettingsPage] for configuring Promela color schemes.
 */
class PromelaColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon = PromelaFileType.icon

    override fun getHighlighter(): SyntaxHighlighter = PromelaSyntaxHighlighter()

    override fun getDemoText(): String = """
// global chan
chan global_output = [0] of { int }

#define ARRAY_SIZE 14+32

#define PRINT_END (-1)

typedef int_tuple
{
	int first, second
}

typedef defined_type
{
	int size;
// struct comment
	int_tuple range;
}

defined_type defined_type_array[ARRAY_SIZE]

unsigned value: 1 = 1

#define DISABLED 1
#undef DISABLED

#ifdef DISABLED
//this is dissabled code
int disabled_value
#if DISABLED == 2
int nested_disabled_value
#else
// but this is enabled
int enabled_value;
#endif
#endif

// mtype documentation
mtype = { generic_mtype_1, generic_mtype_2 }

// mtype:mtype_name documentation
mtype:mtype_name = { enumeration_value_1, enumeration_value_1 }

// argument must be int comparable
inline inlined_code(argument) {
	if
	:: argument == 2 -> printf("inlined code called\n")
	fi
}

// prints ints from input
proctype print_channel(chan input) {
	int output_value // value read from input
	mtype mtype_instance = enumeration_value_1
	do
	:: input?output_value;
	if
	:: output_value == -1 -> break
	:: else -> printf("printing value: %d", output)
	fi
	od
	inlined_code(output_value)
}

active proctype main() priority 1 provided(1) {
	int x;
	run print_channel(global_output)
	for (x : 1 .. 10)
	{
		global_output !x
	}
	// struct field access
	defined_type_array[10].range.first
	global_output !-1
}
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String?, TextAttributesKey?>? = null

    override fun getAttributeDescriptors(): Array<out AttributesDescriptor?> = DESCRIPTORS

    override fun getColorDescriptors(): Array<out ColorDescriptor?> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): @NlsContexts.ConfigurableName String = "Promela"
}