package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components

import com.intellij.ui.JBSplitter
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import java.awt.BorderLayout
import javax.swing.JPanel
import kotlin.properties.Delegates

internal class HeaderlessColumnInfo : ColumnInfo<String, String>("") {
    override fun valueOf(item: String): String = item
}

/**
 * Model for tracked variable and channel values during simulation.
 */
interface TrackedValuesModel {
    /**
     * Represents a tracked variable.
     * @param name the name of the variable
     * @param value the string representation of the variable's value
     */
    data class Variable(val name: String, val value: String)

    /**
     * Represents tracked channel data.
     * @param id the unique identifier of the channel
     * @param name the name of the channel
     * @param values the string representation of the values currently in the channel
     */
    data class ChannelData(val id: String, val name: String, val values: String)

    /**
     * Tracked variable values
     */
    var values: List<Variable>

    /**
     * Tracked content of channels
     */
    var channels: List<ChannelData>
}

/**
 * Bridge model for showing values/channels inside tables.
 */
private class TrackedValuesModelTableBridge(
) : TrackedValuesModel {

    /**
     * [values] converted to the table model
     */
    val valuesTableModel = ListTableModel<String>(HeaderlessColumnInfo())

    /**
     * [channels] converted to the table model
     */
    val channelsTableModel = ListTableModel<String>(HeaderlessColumnInfo())

    override var values: List<TrackedValuesModel.Variable> by Delegates.observable(emptyList()) { _, _, newValue ->
        valuesTableModel.items = newValue.map {
            "${it.name} = ${it.value}"
        }
    }

    override var channels: List<TrackedValuesModel.ChannelData> by Delegates.observable(
        emptyList()
    ) { _, _, newValue ->
        valuesTableModel.items = newValue.map {
            "${it.name}(${it.id}): ${it.values}"
        }
    }
}

/**
 * Component for showing tracked values of variables and channels (queues)
 */
class TrackedValuesComponent : JBSplitter(true) {
    private val typedModel = TrackedValuesModelTableBridge()
    val model: TrackedValuesModel get() = typedModel

    init {
        val values = JPanel(BorderLayout())
        values.add(JBLabel(PromelaBundle.message("simulation.console.values")), BorderLayout.PAGE_START)
        JBTable(typedModel.valuesTableModel).also {
            values.add(
                ScrollPaneFactory.createScrollPane(it),
                BorderLayout.CENTER
            )
            it.setShowColumns(false)
        }

        val channels = JPanel(BorderLayout())
        channels.add(JBLabel(PromelaBundle.message("simulation.console.channels")), BorderLayout.PAGE_START)
        JBTable(typedModel.channelsTableModel).also {
            channels.add(
                ScrollPaneFactory.createScrollPane(it),
                BorderLayout.CENTER
            )
            it.setShowColumns(false)
        }

        firstComponent = values
        secondComponent = channels
        proportion = 0.5f
        setAndLoadSplitterProportionKey("simulation-tracked-values-splitter")
    }
}