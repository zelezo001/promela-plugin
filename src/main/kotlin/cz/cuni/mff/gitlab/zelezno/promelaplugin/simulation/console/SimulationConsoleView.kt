package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console

import com.intellij.execution.console.ConsoleViewWrapperBase
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.observable.util.whenListChanged
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JBSplitter
import com.intellij.ui.MutableCollectionComboBoxModel
import com.intellij.ui.components.JBTabbedPane
import com.intellij.util.ui.components.BorderLayoutPanel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.*
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.msc.MessageSequenceChart
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.InteractiveSimulationOption
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.SimulationStep
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.parser.InteractiveSimulationOptionsParser
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.parser.LineProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.parser.StepParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.swing.JComponent
import kotlin.time.Duration.Companion.seconds

/**
 * Custom console view for Promela simulations, providing interactive options,
 * tracked values, and Message Sequence Charts.
 * @param interactive true if the simulation is interactive
 * @param valuesTracked true if data values are tracked
 * @param diagramContainsStatements true if the diagram should contain statements
 * @param project the current project
 * @param delegate the base console view to wrap
 */
class SimulationConsoleView(
    private val interactive: Boolean,
    private val valuesTracked: Boolean,
    private val diagramContainsStatements: Boolean,
    project: Project,
    delegate: ConsoleView,
) : ConsoleViewWrapperBase(delegate) {

    private val model = SimulationModelImpl()
    private val interactiveOptions = MutableCollectionComboBoxModel<InteractiveSimulationOption>()

    private val p: Bridge = project.getService(SpinBridgeService::class.java).createBridge(
        if (interactive) 1 else 10_000, // interactive must always be with 1, otherwise user would never get his results
    ) {
        withContext(Dispatchers.EDT) { model.addSteps(it) }
        delay(1.seconds) //  delay the processing so we don't overwhelm the EDT dispatcher
    }

    init {
        Disposer.register(this, p)
    }

    /**
     * Attaches listeners for handling parsing of simulation output
     */
    override fun attachToProcess(processHandler: ProcessHandler) {
        super.attachToProcess(processHandler)

        if (interactive) {
            processHandler.addProcessListener(LineProcessor.StdoutProcessListener(InteractiveSimulationOptionsParser() { options ->
                // this can be a bit slow, but that doesn't matter as we expect little traffic here
                ApplicationManager.getApplication().invokeAndWait {
                    this@SimulationConsoleView.interactiveOptions.removeAll()
                    this@SimulationConsoleView.interactiveOptions.add(options)
                }
            }), this)
        }

        processHandler.addProcessListener(LineProcessor.StdoutProcessListener(StepParser { step ->
            p.publish(step)
        }), this)

        processHandler.addProcessListener(object : ProcessListener {
            override fun processTerminated(event: ProcessEvent) {
                p.close()
            }
        }, this)
    }

    private fun buildStepsView(): JComponent {
        return StepList(model)
    }

    private fun wrapWithTrackedValues(stepsView: JComponent): JComponent {
        if (!valuesTracked) {
            return stepsView
        }

        val trackedValues = TrackedValuesComponent()

        model.addListener(object : SimulationModel.SimulationUpdatedListener {
            override fun stepSelected(step: SimulationStep?) {
                val selected = model.selectedStepIndex
                if (selected == null) {
                    trackedValues.model.channels = emptyList()
                    trackedValues.model.values = emptyList()
                    return
                }

                trackedValues.model.channels = model.steps.asSequence()
                    .take(selected + 1).flatMap { it.queues }
                    .associate { it.id to (it.name to it.values) }
                    .map { TrackedValuesModel.ChannelData(it.key, it.value.first, it.value.second) }
                trackedValues.model.values = model.steps.asSequence()
                    .take(selected + 1).flatMap { it.variables }.associate { it.name to it.value }
                    .map { TrackedValuesModel.Variable(it.key, it.value) }
            }
        })

        return JBSplitter().apply {
            firstComponent = trackedValues
            secondComponent = stepsView
            proportion = 0.5f
            setAndLoadSplitterProportionKey("simulation-tracked-values-steps-splitter")
        }
    }

    private fun wrapWithInteractive(view: JComponent): JComponent {
        if (!interactive) return view

        interactiveOptions.whenListChanged(this) {
            if (interactiveOptions.selected == null) return@whenListChanged
            // relay user choice
            delegate.print("${interactiveOptions.selected!!.choice}\r\n", ConsoleViewContentType.USER_INPUT)
            interactiveOptions.removeAll()
        }

        return JBSplitter().apply {
            firstComponent = InteractiveComponent(interactiveOptions)
            secondComponent = view
            proportion = 0.33f
            setAndLoadSplitterProportionKey("simulation-interactive-splitter")
        }
    }

    private fun wrapWithMSC(view: JComponent): JComponent {
        return JBSplitter(true).apply {
            firstComponent = MessageSequenceChart(diagramContainsStatements, model)
            secondComponent = view
        }.apply {
            setAndLoadSplitterProportionKey("simulation-diagram-splitter")
        }
    }

    private val myComponent: JComponent by lazy {
        val console = super.component

        var component = buildStepsView()
        component = wrapWithTrackedValues(component)
        component = wrapWithInteractive(component)
        component = wrapWithMSC(component)
        component = BorderLayoutPanel().apply { add(component) }

        JBTabbedPane().apply {
            this.addTab(PromelaBundle.message("simulation.console.tab.simulation"), component)
            this.addTab(PromelaBundle.message("simulation.console.tab.console"), console)
        }
    }

    override fun createConsoleActions(): Array<out AnAction?> {
        return emptyArray()
    }

    override fun getComponent(): JComponent {
        return myComponent
    }
}