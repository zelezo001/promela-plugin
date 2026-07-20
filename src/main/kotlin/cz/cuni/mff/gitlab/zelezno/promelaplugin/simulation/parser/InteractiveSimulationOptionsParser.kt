package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.parser

import com.intellij.openapi.diagnostic.Logger
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.InteractiveSimulationOption

/**
 * Parser for interactive simulation options from Spin output.
 * @param handler the handler to call when options are parsed
 */
class InteractiveSimulationOptionsParser(private val handler: OptionsParsedHandler) : LineProcessor() {

    /**
     * Handler for parsed interactive simulation options.
     */
    fun interface OptionsParsedHandler {
        /**
         * Called when a set of options has been parsed.
         * @param options the list of parsed options
         */
        fun handle(options: List<InteractiveSimulationOption>)
    }

    private companion object {
        const val BEGIN = "Select a statement"
        val END_REGEX = "Make Selection \\d+".toRegex()
        val LOG = Logger.getInstance(InteractiveSimulationOptionsParser::class.java)
        val STATEMENT_REGEX =
            "\\s+choice\\s+(\\d+):\\s+proc\\s+(\\d+)\\s+\\((.+)\\).+\\(state (\\d+)\\)\\s+(\\S.+)".toRegex()
    }

    private var parsingOptions = false

    private val options = mutableListOf<InteractiveSimulationOption>()

    private fun optionsParsed() {
        parsingOptions = false
        val list = options.toList()
        handler.handle(list)
        options.clear()
    }

    override fun bufferWritten() {
        if (!parsingOptions) return
        if (END_REGEX.matches(buffer)) {
            optionsParsed()
            buffer.clear() // treat this as a line
        }
    }

    override fun handle(line: String) {
        if (line == BEGIN) {
            if (parsingOptions) {
                LOG.error("Spin send statement select twice")
            }
            parsingOptions = true
            return
        }
        if (!parsingOptions) return
        if (END_REGEX.matches(line)) {
            optionsParsed()
            return
        }
        parseStatement(line)
    }

    private fun parseStatement(line: String) {
        val (choice, procNumber, procName, state, statement) = STATEMENT_REGEX.matchEntire(line)?.destructured
            ?: run {
                LOG.error("Invalid option line '$line'")
                return
            }
        options.add(InteractiveSimulationOption(choice, procNumber, procName, state, statement))
    }

}
