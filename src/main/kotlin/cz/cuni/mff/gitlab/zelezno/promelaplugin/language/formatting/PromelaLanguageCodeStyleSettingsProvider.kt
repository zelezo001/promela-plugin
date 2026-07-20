package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.formatting

import com.intellij.application.options.*
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.codeStyle.*
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle

/**
 * Settings used for configuring Promela code a formatting model built by [PromelaFormattingModelBuilder]
 * @param container Container for persisting the settings
 */
class PromelaCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings(PromelaLanguage.id, container)

/**
 * Promela setting page for configuring [CodeStyleSettings]
 */
class PromelaLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getConfigurableDisplayName(): @NlsContexts.ConfigurableName String = "Promela"

    override fun getLanguageName(): @NlsContexts.Label String = "Promela"

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return PromelaCodeStyleSettings(settings)
    }

    override fun createConfigurable(
        settings: CodeStyleSettings, modelSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, modelSettings, configurableDisplayName) {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                return PromelaCodeStyleMainPanel(currentSettings, settings)
            }
        }
    }

    private class PromelaCodeStyleMainPanel(currentSettings: CodeStyleSettings, settings: CodeStyleSettings) :
        TabbedLanguageCodeStylePanel(PromelaLanguage, currentSettings, settings)

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
            consumer.showStandardOptions(
                CodeStyleSettingsCustomizable.WrappingOrBraceOption.BRACE_STYLE.name,
                CodeStyleSettingsCustomizable.WrappingOrBraceOption.METHOD_BRACE_STYLE.name,
                CodeStyleSettingsCustomizable.WrappingOrBraceOption.CLASS_BRACE_STYLE.name,
            )
            consumer.renameStandardOption(
                CodeStyleSettingsCustomizable.WrappingOrBraceOption.CLASS_BRACE_STYLE.name,
                PromelaBundle.message("language.formatting.typedefBraceStyle")
            )
        }

        if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions(
                CodeStyleSettingsCustomizable.BlankLinesOption.KEEP_BLANK_LINES_IN_DECLARATIONS.name,

                )
            consumer.showStandardOptions(
                CodeStyleSettingsCustomizable.BlankLinesOption.BLANK_LINES_AROUND_METHOD.name,

                )
            consumer.renameStandardOption(
                CodeStyleSettingsCustomizable.BlankLinesOption.BLANK_LINES_AROUND_METHOD.name,
                PromelaBundle.message("language.formatting.blankLinesTopLevel"),
            )
        }

        if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions(

                CodeStyleSettingsCustomizable.SpacingOption.SPACE_BEFORE_METHOD_CALL_PARENTHESES.name,
                CodeStyleSettingsCustomizable.SpacingOption.SPACE_BEFORE_METHOD_PARENTHESES.name,

                CodeStyleSettingsCustomizable.SpacingOption.SPACE_BEFORE_FOR_PARENTHESES.name,

                CodeStyleSettingsCustomizable.SpacingOption.SPACE_BEFORE_COMMA.name,
                CodeStyleSettingsCustomizable.SpacingOption.SPACE_AFTER_COMMA.name,
            )
        }

        if (settingsType == SettingsType.INDENT_SETTINGS) {
            consumer.showStandardOptions(
                CodeStyleSettingsCustomizable.IndentOption.INDENT_SIZE.name,
                CodeStyleSettingsCustomizable.IndentOption.CONTINUATION_INDENT_SIZE.name,
                CodeStyleSettingsCustomizable.IndentOption.LABEL_INDENT_SIZE.name,
                CodeStyleSettingsCustomizable.IndentOption.LABEL_INDENT_ABSOLUTE.name,
            )
        }
    }

    override fun getIndentOptionsEditor(): IndentOptionsEditor {
        return SmartIndentOptionsEditor()
    }

    override fun getLanguage() = PromelaLanguage

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
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
        """
    }
}