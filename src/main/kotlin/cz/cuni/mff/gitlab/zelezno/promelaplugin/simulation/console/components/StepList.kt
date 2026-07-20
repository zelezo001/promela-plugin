package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components

import com.intellij.openapi.observable.util.whenItemSelected
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.CollectionListModel
import com.intellij.ui.MutableCollectionComboBoxModel
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.SimulationStep
import java.awt.BorderLayout
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.util.function.Function
import javax.swing.*
import kotlin.math.min

/**
 * Component showing/selecting all steps in the simulation.
 * The step list is paged due to performance reasons.
 * @param model Model of the whole simulation
 */
class StepList(
    val model: SimulationModel,
) : JPanel(BorderLayout()) {

    private fun renderStep(step: SimulationStep): JComponent {
        return JPanel(GridBagLayout()).apply {
            val constraints = GridBagConstraints().apply {
                gridy = 0
                gridx = 0
                weightx = 1.0
                gridheight = GridBagConstraints.REMAINDER
                anchor = GridBagConstraints.LINE_START
            }
            add(JBLabel(step.step), constraints)
            step.stateChanges.forEachIndexed { index, change ->
                val constraints = GridBagConstraints().apply {
                    gridy = index
                    weightx = 1.0
                    gridx = 1
                    anchor = GridBagConstraints.LINE_START
                    fill = GridBagConstraints.HORIZONTAL
                    gridheight = 1
                }
                add(
                    JBLabel(
                        "${change.process.name}:${change.process.pid} (state ${change.text}) [${change.statement}]",
                        SwingConstants.LEFT
                    ),
                    constraints
                )
            }
        }
    }

    /**
     * Current selected step in format Pair<page,indexInPage>
     */
    private val currentPageIndex: Pair<Int, Int>?
        get() = model.selectedStepIndex?.let {
            it / PAGE_SIZE to it % PAGE_SIZE
        }

    private companion object {
        const val PAGE_SIZE = 1000
    }

    private fun getPageRange(page: Int): IntRange {
        val pageFromIndex = page * PAGE_SIZE
        val pageToIndex = min(pageFromIndex + PAGE_SIZE, model.steps.size)
        return pageFromIndex..<pageToIndex
    }

    private fun createPageModel(page: Int): ListModel<SimulationStep> {
        val range = getPageRange(page)
        return CollectionListModel(model.steps.subList(range.first, range.last + 1))
    }

    init {
        val header = JPanel(BorderLayout())
        var pageModel = MutableCollectionComboBoxModel<Int>()
        header.add(JBLabel(PromelaBundle.message("simulation.console.steps")), BorderLayout.LINE_START)
        var pageList = ComboBox(pageModel)
        header.add(pageList, BorderLayout.PAGE_END)
        add(header, BorderLayout.PAGE_START)
        add(JBLabel(PromelaBundle.message("simulation.console.steps")))

        // render pages as "firstStep-lastStep"
        pageList.renderer = object : ListCellRenderer<Int> {
            override fun getListCellRendererComponent(
                list: JList<out Int?>?,
                value: Int?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                value ?: return JBLabel("")
                // pages are always non-empty
                val page = getPageRange(value)
                var firstStep = model.steps[page.first]
                var lastStep = model.steps[page.last]
                return JBLabel("${firstStep.step}-${lastStep.step}")
            }
        }

        val stepList = JBList(DefaultListModel<SimulationStep>())
        stepList.selectionModel.addListSelectionListener {
            if (stepList.selectedIndex == -1) return@addListSelectionListener
            if (stepList.selectedIndex == currentPageIndex?.second) return@addListSelectionListener
            model.selectedStepIndex = pageModel.selected!! * PAGE_SIZE + stepList.selectedIndex
        }

        val renderer: Function<SimulationStep, JComponent> = { renderStep(it) }
        stepList.installCellRenderer(renderer)


        // changing list step when page changes
        pageList.whenItemSelected {
            stepList.selectedIndex = -1
            stepList.model = createPageModel(it)
            if (currentPageIndex?.first == pageList.item) {
                stepList.selectedIndex = currentPageIndex!!.second
            }
        }

        model.addListener(object : SimulationModel.SimulationUpdatedListener {
            override fun stepSelected(step: SimulationStep?) {
                currentPageIndex ?: return // only update step list if step is selected
                if (pageList.selectedItem != currentPageIndex!!.first) {
                    pageList.selectedItem = currentPageIndex!!.first
                    stepList.model = createPageModel(currentPageIndex!!.first)
                }
                stepList.selectedIndex = currentPageIndex!!.second
            }

            override fun stepsAdded(range: IntRange) {
                // update page count
                val pages = model.steps.size / PAGE_SIZE + // full pages
                        min(1, model.steps.size % PAGE_SIZE) // last partially filled page
                for (page in pageModel.size..<pages) {
                    pageModel.add(page)
                }
                // if step was added to the current page, update list model
                pageList.item ?: return
                val page = getPageRange(pageList.item)
                if (
                    !page.between(range.first) &&
                    !page.between(range.last)
                ) {
                    return
                }

                stepList.model = createPageModel(pageList.item)
            }

        })

        add(
            ScrollPaneFactory.createScrollPane(stepList).apply {
                createHorizontalScrollBar()
            },
            BorderLayout.CENTER
        )
    }
}

private fun IntRange.between(i: Int) = this.first <= i && i <= this.last
