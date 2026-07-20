package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.parser

import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.*

/**
 * Parser for individual steps of Spin simulation output.
 * @param handler the handler to call when a step is parsed
 */
class StepParser(
    private val handler: StepHandler,
) : LineProcessor() {

    /**
     * Handler for parsed simulation steps.
     */
    fun interface StepHandler {
        /**
         * Called when a simulation step has been parsed.
         * @param step the parsed simulation step
         */
        fun handle(step: SimulationStep)
    }

    private var inProgress: InProgressSimulationStep? = null

    private data class InProgressSimulationStep(
        val step: String = "", // maybe int
        val stateChanges: MutableList<StateChange> = mutableListOf(),
        val processStarts: MutableList<ProcessStart> = mutableListOf(),
        val queueOperations: MutableList<ChannelOperation> = mutableListOf(),
        val variables: MutableList<Variable> = mutableListOf(),
        val queues: MutableList<Channel> = mutableListOf()
    ) {
        fun toSimulationStep(): SimulationStep {
            return SimulationStep(
                step,
                stateChanges.toList(),
                processStarts.toList(),
                queueOperations.toList(),
                variables.toList(),
                queues.toList(),
            )
        }
    }

    private companion object {
        val PROC_CREATED_REGEX = "\\s*(\\d+):\\s+proc\\s+(-|\\d+)\\s+\\((.+)\\)\\s+creates proc.+".toRegex()
        val STEP_OR_QUEUE_RECORD_REGEX =
            "\\s*(\\d+):\\s+proc\\s+(\\d+)\\s+\\((.+)\\)\\s+(\\S.+):(\\d+)\\s+\\(state (.+)\\)\\s+\\[(.+)]".toRegex()
        val QUEUE_REGEX = "\t\tqueue\\s+(\\d+)\\s+\\((.+)\\):\\s*(.*)".toRegex()
        val VALUE_REGEX = "\t\t(.*)\\s+=\\s+(.*)".toRegex()
        val QUEUE_CHANGE_STATEMENT_PREFIX = "values: "
    }

    override fun handle(line: String) {

        // Spin outputs separated by an empty line
        // we must be strict with line format of parsed output as Spin also outputs
        // choices for interactive mode into stdout (hence regexes instead of )

        // Expected format is:
        // optional created processes
        // state changes/queue operations
        // tracked values (queues/variables)
        // an empty line

        if (line == "" && inProgress != null) {
            // steps are separated by an empty line
            handler.handle(inProgress!!.toSimulationStep())
            inProgress = null
            return
        }

        if (inProgress != null) {
            parseStateChangeOrQueueOperation(line) || parseProcCreated(line) || parseQueueValues(line) || parseVariableValues(
                line
            )
        } else {
            parseStateChangeOrQueueOperation(line) || parseProcCreated(line)
        }
    }

    private fun parseVariableValues(line: String): Boolean {
        val (key, value) = VALUE_REGEX.matchEntire(line)?.destructured ?: return false
        inProgress!!.variables.add(Variable(name = key, value = value))
        return true
    }

    private fun startStepIfNecessary(step: String) {
        if (inProgress == null) {
            inProgress = InProgressSimulationStep(step = step)
        }
    }

    private fun parseQueueValues(line: String): Boolean {
        // either value or queue
        val (queueId, queueName, values) = QUEUE_REGEX.matchEntire(line)?.destructured ?: return false
        inProgress!!.queues.add(Channel(name = queueName, id = queueId, values = values))
        return true
    }

    private fun parseStateChangeOrQueueOperation(line: String): Boolean {
        val (step, rawProcNumber, procName, file, line, state, statement) = STEP_OR_QUEUE_RECORD_REGEX.matchEntire(line)?.destructured
            ?: return false

        startStepIfNecessary(step)

        val process = Process(procName, parseProcNumber(rawProcNumber))

        val inFile = InFile(file, line.toIntOrNull())

        if (state == "-") {
            inProgress!!.queueOperations.add(
                ChannelOperation(
                    process = process,
                    data = queueResolveValue(statement),
                    type = resolveType(statement),
                    file = inFile
                )
            )
        } else {
            inProgress!!.stateChanges.add(
                StateChange(
                    text = line, statement = statement, process = process, file = inFile,
                )
            )
        }
        return true
    }

    private fun parseProcNumber(raw: String): Int {
        return if (raw != "-") {
            raw.toInt()
        } else {
            0
        }
    }

    private fun parseProcCreated(line: String): Boolean {
        val (step, rawProcNumber, procName) = PROC_CREATED_REGEX.matchEntire(line)?.destructured ?: return false

        startStepIfNecessary(step)

        inProgress!!.processStarts.add(
            ProcessStart(
                line, Process(name = procName, pid = parseProcNumber(rawProcNumber))
            )
        )

        return true
    }

    private fun queueResolveValue(statement: String) = statement.removePrefix(QUEUE_CHANGE_STATEMENT_PREFIX)

    private fun resolveType(statement: String): ChannelOperation.Type {
        if (statement.contains("!")) return ChannelOperation.Type.SEND
        if (statement.startsWith(QUEUE_CHANGE_STATEMENT_PREFIX)) return ChannelOperation.Type.RECEIVE
        return ChannelOperation.Type.POLL // either poll or non-consuming receive with arguments in <>
    }
}