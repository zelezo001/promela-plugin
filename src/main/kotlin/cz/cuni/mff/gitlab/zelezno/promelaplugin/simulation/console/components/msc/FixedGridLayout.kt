package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.components.msc

import java.awt.*
import kotlin.math.max

/**
 * Layout for a grid with fixed cell size used for laying out [MessageSequenceChartComponent]
 *@param cellDimensions dimension of a cell (without [vGap] and [hGap])
 * @param vGap gap between rows
 * @param hGap gap between columns
 */
internal class FixedGridLayout(
    var cellDimensions: Dimension,
    var hGap: Int = 0,
    var vGap: Int = 0,
) : LayoutManager2 {

    /**
     * Position in the fixed grid
     */
    class Position(val x: Int = 0, val y: Int = 0)

    /**
     * Number of rows in the grid. Can be larger than the bounds of a populated grid
     */
    var rows = 0

    /**
     * Number of columns in the grid. Can be larger than the bounds of a populated grid
     */
    var columns = 0

    private val columnWidth: Int get() = max(0, cellPositionX(columns) - hGap)
    private var originalParentWidth = 0
    private val width: Int get() = max(originalParentWidth, columnWidth)
    private val height: Int get() = max(0, cellPositionY(rows) - vGap)

    /**
     * Returns line parallel to the row located just above it
     */
    fun calculateLineBeforeRow(row: Int): Pair<Point, Point> {
        val y = cellPositionY(row) - vGap / 2
        return Point(0, y) to Point(width, y)
    }

    /**
     * Returns line parallel to the row located just below it
     */
    fun calculateLineAfterRow(row: Int): Pair<Point, Point> {
        return calculateLineBeforeRow(row + 1)
    }

    /**
     * Returns position of the column start
     */
    fun calculateColumnStart(column: Int): Int = cellPositionX(column)

    /**
     * Calculates a line segment between two cells starting from/to center of facing sides
     */
    fun calculateLinkBetweenCells(
        from: Position,
        to: Position,
    ): Pair<Point, Point> {
        val startY = cellPositionY(from.y) + cellDimensions.height / 2
        val endY = cellPositionY(to.y) + cellDimensions.height / 2
        var startX = cellPositionX(from.x)
        var endX = cellPositionX(to.x)

        // link starts at the end of the left cell and ends at the start of right cell
        if (from.x > to.x) {
            // start is on the left
            endX += cellDimensions.width
        } else if (from.x < to.x) {
            // start is on the right
            startX += cellDimensions.width
        } else {
            // both start and end are in the same column, draw link on the right
            endX += cellDimensions.width
            startX += cellDimensions.width
        }
        return Point(startX, startY) to Point(endX, endY)
    }

    private fun cellPositionX(column: Int) = (cellDimensions.width + hGap) * column
    private fun cellPositionY(row: Int) = (cellDimensions.height + vGap) * row

    /**
     * Adds component to the grid.
     * Repetitive calls with the same component update component's position.
     * @param constraints must not be null and must be of type [Position]
     */
    override fun addLayoutComponent(comp: Component?, constraints: Any?) {
        comp ?: return
        constraints as Position
        comp.size = cellDimensions.size
        comp.location = Point(cellPositionX(constraints.x), cellPositionY(constraints.y))
        comp.location = comp.location
    }

    /**
     * @throws UnsupportedOperationException
     *  Always call [FixedGridLayout.addLayoutComponent] with parameters Component?, Any?
     */
    override fun addLayoutComponent(name: String?, comp: Component?) {
        throw UnsupportedOperationException("must be called with constraints")
    }

    /**
     * @return null
     */
    override fun maximumLayoutSize(target: Container?) = null

    /**
     * @return 0
     */
    override fun getLayoutAlignmentX(target: Container?) = 0f

    /**
     * @return 0
     */
    override fun getLayoutAlignmentY(target: Container?) = 0f

    /**
     * Does nothing
     */
    override fun invalidateLayout(target: Container?) {
    }

    /**
     * Does nothing
     */
    override fun removeLayoutComponent(comp: Component?) {}

    /**
     * Same as [minimumLayoutSize]
     */
    override fun preferredLayoutSize(parent: Container?) = minimumLayoutSize(parent)

    /**
     * Always returns a value calculated from the number of rows/columns and set cell dimensions.
     */
    override fun minimumLayoutSize(parent: Container?) = Dimension(columnWidth, height)

    /**
     * Just updates the parent component, all laying out is done in [addLayoutComponent]
     */
    override fun layoutContainer(parent: Container?) {
        parent ?: return
        originalParentWidth = parent.width
        parent.size = Dimension(width, height)
    }

    /**
     * Calculates row in which point lies, x-axis is ignored.
     * Bottom [vGap] is taken as a part of the row.
     * @return row or null if the point is below the last row
     */
    fun getRowForCoordinates(point: Point): Int? {
        if (point.y >= height) {
            return null
        }

        return point.y / (vGap + cellDimensions.height)
    }
}