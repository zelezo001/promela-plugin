package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.msc

import com.intellij.ui.ScrollPaneFactory
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.SimulationModel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.SimulationStep
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane

/**
 * Main Swing component representing MSC inside the simulation UI.
 * It handles correct binding of [model] to displayed chart
 * @param model Model of the displayed simulation
 * @param showStatements if true, the shown diagram contains statements even if they do not interact with channels
 */
class MessageSequenceChart(
    showStatements: Boolean,
    val model: SimulationModel,
) : JPanel(BorderLayout()) {

    private val scrollPane: JScrollPane

    init {
        val chartModel = ChartModel(showStatements)
        val messageSequenceChart = MessageSequenceChartComponent(chartModel)
        val builder = MessageSequenceChartBuilder(chartModel)
        scrollPane = ScrollPaneFactory.createScrollPane(messageSequenceChart)

        model.addListener(object : SimulationModel.SimulationUpdatedListener {
            override fun stepsAdded(range: IntRange) {
                range.forEach { builder.stepAdded(model.steps[it]) }
                updateScrollPaneViewport()
                messageSequenceChart.repaint()
            }

            override fun stepSelected(step: SimulationStep?) {
                chartModel.focusOnRowsFromStep(step?.step)
                messageSequenceChart.scrollToFocusedRows()
                messageSequenceChart.repaint()
            }
        })

        messageSequenceChart.addStepSelectedListener { step ->
            model.selectedStepIndex = model.steps.indexOfFirst { it.step == step }
        }

        add(scrollPane)
    }

    /**
     * Resets scroll pane viewport and thus resetting known dimensions.
     * Must be called be when the contained component changes its size, otherwise scrollPane would not update scrollbars.
     */
    private fun updateScrollPaneViewport() {
        val view = scrollPane.viewport.view
        scrollPane.viewport = null
        scrollPane.setViewportView(view)
    }

}

