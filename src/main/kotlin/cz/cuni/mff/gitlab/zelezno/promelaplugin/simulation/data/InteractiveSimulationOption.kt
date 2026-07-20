package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data

/**
 * Represents an option in an interactive Promela simulation.
 * @param choice the choice identifier
 * @param procNumber the process number
 * @param procName the name of the process
 * @param state the current state of the process
 * @param statement the statement to be executed
 */
data class InteractiveSimulationOption(
    val choice: String,
    val procNumber: String,
    val procName: String,
    val state: String,
    val statement: String,
)