package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.msc

import com.intellij.ide.setToolTipText
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.ui.components.JBLabel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.border.StrokeBorder
import kotlin.math.max
import kotlin.math.min

/**
 * Component containing and handling rendering/clicking the MSC
 * @param model Data source for this component
 */
internal class MessageSequenceChartComponent(
    val model: ChartModel
) : JPanel() {

    companion object {
        private val SELECTED_LINE_STROKE = BasicStroke(
            1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, floatArrayOf(10.0f), 0.0f
        )
    }

    private val grid = FixedGridLayout(Dimension(100, 50), hGap = 20, vGap = 5)

    fun interface StepSelectedListener {
        fun selected(step: String)
    }

    private val stepSelectedListeners = mutableSetOf<StepSelectedListener>()

    fun addStepSelectedListener(stepSelectedListener: StepSelectedListener) {
        stepSelectedListeners.add(stepSelectedListener)
    }

    private val headerLabels = mutableListOf<JBLabel>()

    init {
        // add "Step" column header
        this.add(
            JBLabel(
                PromelaBundle.message("simulation.console.step")
            ), FixedGridLayout.Position(0, 0)
        )

        model.addListener(object : ChartModel.UpdateListener {
            override fun focusChanged() {
                // we must update selection indicator around focused rows
                repaint()
            }

            override fun processAdded() {
                refreshLabels()
                refreshDimensions()
            }

            override fun rowAdded(index: Int) {
                refreshDimensions() // update dimensions
                if (rowVisible(index)) { // added row is currently shown, we must refresh the visible content
                    setVisibleContent(true)
                }
            }
        })
        this.layout = grid

        // handler of row selection outside of labels
        addMouseListener(object : MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                e ?: return
                pointInGridSelected(e.point)
            }

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {}
        })

        refreshLabels()
        refreshDimensions()
    }

    fun scrollToFocusedRows() {
        val y = model.focusedRows?.first?.let { grid.calculateLineBeforeRow(it + 1).first.y } ?: return
        val x = model.focusedRows?.first?.let { grid.calculateColumnStart(model.rows[it].column + 1) } ?: return
        val rectangle = Rectangle(x, y, grid.cellDimensions.width, grid.cellDimensions.height)
        scrollRectToVisible(rectangle)
    }

    /**
     * Function handling selection of a row
     */
    private fun pointInGridSelected(point: Point) {
        grid.getRowForCoordinates(point)
            ?.takeIf { it != 0 } // skip header
            ?.minus(1) // to model rows
            ?.takeIf { it < model.rows.size }
            ?.let { row ->
                stepSelectedListeners.forEach { it.selected(model.rows[row].step) }
            }
    }

    /**
     * Mouse listener for handling selection of row when clicking on its labels
     */
    private val contentLabelMouseEventHandler = object : MouseListener {
        override fun mouseClicked(e: MouseEvent?) {
            e ?: return
            if (e.component.parent == this@MessageSequenceChartComponent) {
                val inParent = e.component.location
                val pointInParent = e.point.location
                pointInParent.translate(inParent.x, inParent.y)
                pointInGridSelected(pointInParent)
            }
        }

        override fun mousePressed(e: MouseEvent?) {}

        override fun mouseReleased(e: MouseEvent?) {}

        override fun mouseEntered(e: MouseEvent?) {}

        override fun mouseExited(e: MouseEvent?) {}
    }

    private fun refreshLabels() {
        // add missing labels
        repeat(model.processes.size - headerLabels.size) {
            val label = JBLabel()
            headerLabels.add(label)
            this.add(
                label, FixedGridLayout.Position(headerLabels.size, 0)
            )
        }
        // update label texts
        model.processes.forEachIndexed { index, string -> headerLabels[index].text = string }
    }

    private fun refreshDimensions() {
        grid.columns = model.processes.size + 1 // 1 for "Step" column
        grid.rows = model.rows.size + 1 // 1 for header
    }

    /**
     * List of labels that are used for rendering actually shown rows
     * format: pair<stepLabel, contentLabel>
     */
    private val contentLabels = mutableListOf<Pair<JBLabel, JBLabel>>()

    private fun addContentLabel() {
        val stepLabel = JBLabel()
        stepLabel.addMouseListener(contentLabelMouseEventHandler)

        val label = JBLabel("", SwingConstants.CENTER)
        label.addMouseListener(contentLabelMouseEventHandler)
        label.isAllowAutoWrapping = true
        label.verticalTextPosition = SwingConstants.CENTER
        label.horizontalTextPosition = SwingConstants.CENTER

        contentLabels.add(stepLabel to label)
    }

    private fun hideContentLabel(index: Int) {
        val content = contentLabels[index]
        content.first.isVisible = false
        content.second.isVisible = false
    }

    private fun setupContentLabel(index: Int, rowIndex: Int) {
        val content = contentLabels[index]
        content.first.isVisible = true
        content.second.isVisible = true
        val row = model.rows[rowIndex]
        val gridRow = rowIndex + 1 // model rows start at index 1

        content.first.text = row.step
        content.second.text = row.label
        content.second.setToolTipText(HtmlChunk.text(row.label))
        content.second.border = if (row.isChannelInteraction) {
            StrokeBorder(BasicStroke())
        } else {
            null
        }
        this.add(content.first, FixedGridLayout.Position(0, gridRow))
        this.add(
            content.second,
            FixedGridLayout.Position(row.column + 1, gridRow)
        ) // +1 for step header column
    }

    private fun drawLine(g: Graphics2D, line: Pair<Point, Point>, stroke: Stroke = BasicStroke()) {
        val (from, to) = line
        g.create().let {
            it as Graphics2D
            it.stroke = stroke
            it.drawLine(from.x, from.y, to.x, to.y)
        }
    }

    /**
     * Draw communication/selection lines onto the rendered MSC
     */
    private fun drawLines(g: Graphics) {
        val drawingG = g.create() as Graphics2D
        drawingG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        drawingG.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        drawingG.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

        model.paths.forEach {
            drawLine(
                drawingG, grid.calculateLinkBetweenCells(
                    // +1 because model addresses without "header" column/row
                    FixedGridLayout.Position(it.from.column + 1, it.from.row + 1),
                    FixedGridLayout.Position(it.to.column + 1, it.to.row + 1),
                )
            )
        }

        model.focusedRows?.let {
            drawLine(drawingG, grid.calculateLineBeforeRow(it.first + 1), SELECTED_LINE_STROKE)
            drawLine(drawingG, grid.calculateLineAfterRow(it.last + 1), SELECTED_LINE_STROKE)
        }
    }

    private var oldBound: Rectangle? = null

    private fun rowVisible(row: Int): Boolean {
        if (row < (firstRenderedRow() ?: Int.MAX_VALUE)) return false // before rendered rows
        if (row > lastRenderedRow()) return false // before rendered rows

        return true
    }

    private fun firstRenderedRow(): Int? {
        if (model.rows.isEmpty()) return null
        val firstRow = grid.getRowForCoordinates(visibleRect.location) ?: return null
        return max(0, firstRow - 1) // -1 as rows in grid are shifted by 0
    }

    private fun lastRenderedRow(): Int {
        val point = Point(visibleRect.location)
        point.translate(0, visibleRect.height)
        val gridRowIndex = grid.getRowForCoordinates(point)?.plus(1)
            ?: model.rows.size // + 1 so we render ahead a bit for smoothness

        return min(model.rows.size, gridRowIndex) - 1
    }

    /**
     * Updates [contentLabels] representing MSC content so they are in the visible area containing correct values
     * @param force true if recomputation must be done. If false, recomputation is done only when [visibleRect] changes.
     */
    private fun setVisibleContent(force: Boolean = false) {
        if (visibleRect.equals(oldBound) && !force) return
        oldBound = visibleRect
        val firstRow = firstRenderedRow() ?: return

        val lastRow = lastRenderedRow()
        if (lastRow < 0) return
        if (firstRow >= lastRow) return // no rows to show
        val rowsToShow = (lastRow + 1) - firstRow

        // add labels if needed
        repeat(rowsToShow - contentLabels.size) {
            addContentLabel()
        }

        // setup visible labels
        (firstRow..lastRow).forEachIndexed { i, row ->
            setupContentLabel(i, row)
        }

        // hide remaining labels
        (rowsToShow..<contentLabels.size).forEach { hideContentLabel(it) }
    }

    /**
     * Paints recomputes content of [contentLabels] if necessary and draws the MSC
     */
    override fun paint(g: Graphics?) {
        setVisibleContent()
        super.paint(g)
        g ?: return
        drawLines(g)
    }
}