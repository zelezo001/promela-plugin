package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components

import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.SimulationStep
import kotlin.properties.Delegates

/**
 * Model for storing and managing Promela simulation steps.
 */
interface SimulationModel {
    /**
     * Listener for updates to the simulation model.
     */
    interface SimulationUpdatedListener {
        /**
         * Called when new steps are added to the model.
         * @param range the range of indices of the added steps
         */
        fun stepsAdded(range: IntRange) {}

        /**
         * Called when a step is selected.
         * @param step the selected step, or null if no step is selected
         */
        fun stepSelected(step: SimulationStep?) {}
    }

    /**
     * The list of simulation steps.
     */
    val steps: List<SimulationStep>

    /**
     * The currently selected simulation step.
     */
    val selectedStep: SimulationStep?

    /**
     * The index of the currently selected simulation step.
     */
    var selectedStepIndex: Int?

    /**
     * Adds new steps to the model.
     * @param steps the steps to add
     */
    fun addSteps(steps: List<SimulationStep>)

    /**
     * Adds a listener for model updates.
     * @param listener the listener to add
     */
    fun addListener(listener: SimulationUpdatedListener)

    /**
     * Removes a listener for model updates.
     * @param listener the listener to remove
     */
    fun removeListener(listener: SimulationUpdatedListener)
}

/**
 * Implementation of [SimulationModel].
 */
class SimulationModelImpl : SimulationModel {

    private val listeners: MutableSet<SimulationModel.SimulationUpdatedListener> = mutableSetOf()

    /**
     * Backing field of [selectedStepIndex] that is always a valid value
     */
    private var _selectedStepIndex by Delegates.observable<Int?>(null) { _, _, newValue ->
        val step = newValue?.let { _steps[it] }
        listeners.forEach { it.stepSelected(step) }
    }

    override var selectedStepIndex: Int?
        get() = _selectedStepIndex
        set(value) {
            if (value != null && steps.size <= value) {
                throw IndexOutOfBoundsException("selected step index is outside of step array")
            }
            _selectedStepIndex = value
        }

    override val selectedStep get() = _selectedStepIndex?.let { _steps[it] }

    override val steps: List<SimulationStep> get() = _steps

    private val _steps: MutableList<SimulationStep> = mutableListOf()

    override fun addSteps(steps: List<SimulationStep>) {
        val from = _steps.size
        val to = from + steps.size
        _steps.addAll(steps)
        listeners.forEach { it.stepsAdded(from..<to) }
    }

    override fun addListener(listener: SimulationModel.SimulationUpdatedListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: SimulationModel.SimulationUpdatedListener) {
        listeners.remove(listener)
    }
}