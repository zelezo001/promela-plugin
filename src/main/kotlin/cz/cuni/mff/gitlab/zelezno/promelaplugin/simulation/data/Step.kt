package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data

// This file defines data classes representing one step of simulation

/**
 * Represents a process in the simulation.
 * @param name the name of the process
 * @param pid the process ID
 */
data class Process(
    val name: String,
    val pid: Int,
)

/**
 * Represents a state change in a model.
 * @param text the new state description
 * @param statement the statement that caused the change
 * @param process the process that changed state
 * @param file the location in the source file
 */
data class StateChange(
    val text: String,
    val statement: String,
    val process: Process,
    val file: InFile,
)

/**
 * Represents the start of a new process.
 * @param text description of the start
 * @param startedBy the process that started the new one
 */
data class ProcessStart(
    val text: String,
    val startedBy: Process
)

/**
 * Represents a location in a source file.
 * @param path the path to the file
 * @param line the line number
 */
data class InFile(
    val path: String,
    val line: Int?,
)

/**
 * Represents a channel operation.
 * @param process the process performing the operation
 * @param data the data being sent or received
 * @param type the type of operation (SEND, RECEIVE, POLL)
 * @param file the location in the source file
 */
data class ChannelOperation(
    val process: Process,
    val data: String,
    val type: Type,
    val file: InFile,
) {
    enum class Type { RECEIVE, POLL, SEND } // POLL is either poll or non-consuming receive
}

/**
 * Represents a single step in the simulation.
 * @param step the step identifier
 * @param stateChanges list of state changes in this step
 * @param processStarts list of process starts in this step
 * @param channelOperations list of channel operations in this step
 * @param variables list of variable values in this step
 * @param queues list of channel states in this step
 */
data class SimulationStep(
    val step: String, // maybe int
    val stateChanges: List<StateChange>,
    val processStarts: List<ProcessStart>,
    val channelOperations: List<ChannelOperation>,
    val variables: List<Variable>,
    val queues: List<Channel>
)

/**
 * Represents the state of a channel (queue).
 * @param name the name of the channel
 * @param id the unique identifier of the channel
 * @param values the values currently in the channel
 */
data class Channel(val name: String, val id: String, val values: String)

/**
 * Represents the value of a variable.
 * @param name the name of the variable
 * @param value the string representation of the variable's value
 */
data class Variable(val name: String, val value: String)