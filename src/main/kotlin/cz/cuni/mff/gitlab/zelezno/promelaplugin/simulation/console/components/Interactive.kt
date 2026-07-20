package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.InteractiveSimulationOption
import java.awt.BorderLayout
import java.util.function.Function
import javax.swing.ComboBoxModel
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Component for displaying and selecting interactive simulation options.
 * @param model the model containing simulation options
 */
class InteractiveComponent(
    val model: ComboBoxModel<InteractiveSimulationOption>
) : JPanel(BorderLayout()) {
    init {
        add(JBLabel(PromelaBundle.message("simulation.console.interactive")), BorderLayout.PAGE_START)
        val interactiveList = JBList(model)
        add(interactiveList)
        val renderer: Function<InteractiveSimulationOption, JComponent> = {
            JBLabel("proc ${it.procNumber} (${it.procName}) (state ${it.state}) ${it.statement}")
        }
        interactiveList.installCellRenderer(renderer)
        interactiveList.selectionModel.addListSelectionListener {
            if (it.valueIsAdjusting) return@addListSelectionListener
            model.selectedItem = interactiveList.selectedValue
        }
    }
}