package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.msc

import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.ChannelOperation
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.Process
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.SimulationStep
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.StateChange
import java.util.*


/**
 * This class handles transformation from flow of steps to a message sequence chart.
 * Visualization itself is then separated inside [Renderer].
 * It's separated from [ChartModel] so we can in the future render to different targets (eg. exporting SVG)
 */
internal class MessageSequenceChartBuilder<T>(private val renderer: Renderer<T>) {

    interface Renderer<T> {

        fun drawPassedMessage(from: T, to: T, consumesMessage: Boolean)
        fun drawChanInteraction(step: String, process: Process, text: String): T
        fun drawStatement(step: String, process: Process, text: String)
    }

    // channel value -> sending block
    private val chanValues: MutableMap<String, Queue<T>> = mutableMapOf()

    /**
     * Processes messages done in the given step.
     * Method expected that the order of steps matches their actual order
     */
    fun stepAdded(step: SimulationStep) {
        // state changes always mirror channel operations
        if (step.channelOperations.isNotEmpty()) {
            channelOperations(step.step, step.channelOperations)
        } else {
            statements(step.step, step.stateChanges)
        }
    }

    private fun statements(step: String, opts: List<StateChange>) {
        opts.forEach {
            renderer.drawStatement(step, it.process, it.statement)
        }
    }

    private fun channelOperations(step: String, ops: List<ChannelOperation>) {
        ops.forEach { operation ->
            val rendered = renderer.drawChanInteraction(step, operation.process, operation.data)
            when (operation.type) {
                ChannelOperation.Type.POLL, ChannelOperation.Type.RECEIVE -> {
                    val key = operation.data.replace("?", "!")
                    val sentBy = chanValues[key]?.poll() ?: return
                    renderer.drawPassedMessage(
                        sentBy, rendered,
                        operation.type == ChannelOperation.Type.RECEIVE
                    )
                }

                ChannelOperation.Type.SEND -> {
                    chanValues.getOrPut(operation.data) { LinkedList() }.add(rendered)
                }
            }
        }
    }
}