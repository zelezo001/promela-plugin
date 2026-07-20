package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.msc

import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.Process
import kotlin.math.max

/**
 * Represents a position in the MSC grid.
 * @param row the row index
 * @param column the column index
 */
data class RowColumn(val row: Int, val column: Int)

/**
 * Model representing MSC rendered as a grid used by [MessageSequenceChartComponent]
 * @param showStatements if MSC should contain statements or not (statement-only processes are always shown in the header)
 */
class ChartModel(
    val showStatements: Boolean,
) : MessageSequenceChartBuilder.Renderer<RowColumn> {

    interface UpdateListener {
        fun processAdded() {}
        fun rowAdded(index: Int) {}
        fun focusChanged() {}
    }

    private val listeners = mutableSetOf<UpdateListener>()

    /**
     * Registers [listener] to receive events from this model
     */
    fun addListener(listener: UpdateListener) {
        listeners.add(listener)
    }

    private fun fireEvent(call: (UpdateListener) -> Unit) {
        listeners.forEach(call)
    }

    class Row(val column: Int, val label: String, val step: String, val isChannelInteraction: Boolean)
    class Path(val from: RowColumn, val to: RowColumn)

    private var maxPid = 0

    val mutablePaths = mutableListOf<Path>()

    /**
     * Message paths between the channel interactions, coordinate of interaction in the first step by pid = 1 is [0,0]
     */
    val paths: List<Path> get() = mutablePaths

    /**
     * Updates [focusedRows] to match the first sequence of rows matching given step.
     * For unknown step, [focusedRows] is set to null
     */
    fun focusOnRowsFromStep(step: String?) {
        focusedRows = null
        step ?: return

        var first: Int? = null
        var last: Int? = null
        for (indexed in rows.withIndex()) {
            if (step != indexed.value.step) {
                // if we already encountered the step, end iteration
                if (first == null) continue else break
            }
            first = first ?: indexed.index
            last = indexed.index
        }
        focusedRows = first?.let { it..last!! }
        fireEvent { it.focusChanged() }
    }

    /**
     * Rows focused by the user
     */
    var focusedRows: IntRange? = null
        private set

    private val mutableProcesses = mutableListOf<String>()
    private val mutableRows = mutableListOf<Row>()

    /**
     * Rows of the MSC, only "content" rows are present.
     * [Row.column] corresponds to the performing process in [processes]
     */
    val rows: List<Row> = mutableRows

    /**
     * All processes known by this model. Can contain gaps.
     */
    val processes: List<String> get() = mutableProcesses


    /**
     * Stores the process into [processes] and triggers [UpdateListener.processAdded]
     */
    private fun recordProcess(process: Process) {
        maxPid = max(process.pid, maxPid)
        var added = false

        val index = process.pid
        val pidCount = process.pid + 1 // pids starts with 0
        if (mutableProcesses.size < pidCount) {
            repeat(pidCount - mutableProcesses.size) {
                mutableProcesses.add("")
                added = true
            }
        }

        val name = "${process.pid}:${process.name}"
        if (mutableProcesses[index] != name) {
            mutableProcesses[index] = name
            added = true
        }

        if (added) fireEvent { it.processAdded() }
    }

    /**
     * Draw the passed message as a [paths].
     * @param from start (write action) [RowColumn] returned from [drawChanInteraction]
     * @param to end (read action) [RowColumn] returned from [drawChanInteraction]
     * @param consumesMessage is ignored
     */
    override fun drawPassedMessage(
        from: RowColumn, to: RowColumn, consumesMessage: Boolean
    ) {
        mutablePaths.add(Path(from, to))
    }

    /**
     * Creates a new row with the channel interaction.
     * If the process is not known yet, it's added to the header.
     * Triggers [UpdateListener.rowAdded]
     */
    override fun drawChanInteraction(
        step: String, process: Process, text: String
    ): RowColumn {
        recordProcess(process)
        mutableRows.add(Row(process.pid, text, step, true))

        fireEvent { it.rowAdded(mutableRows.size - 1) }

        return RowColumn(mutableRows.size - 1, process.pid)
    }

    /**
     * Creates new row with the statement if statements are shown [showStatements]
     * If a process is not known yet, it's added to the header.
     * Triggers [UpdateListener.rowAdded]
     */
    override fun drawStatement(
        step: String, process: Process, text: String
    ) {
        recordProcess(process) // always record the process
        if (!showStatements) return
        mutableRows.add(Row(process.pid, text, step, false))

        fireEvent { it.rowAdded(mutableRows.size - 1) }
    }
}