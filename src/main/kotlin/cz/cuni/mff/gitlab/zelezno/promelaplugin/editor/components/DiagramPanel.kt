package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components

import com.github.weisj.jsvg.SVGDocument
import com.intellij.openapi.Disposable
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.Magnificator
import java.awt.*
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import javax.swing.JPanel
import javax.swing.JScrollPane
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

/**
 * Panel for displaying Promela automaton diagrams.
 */
class DiagramPanel : JPanel(BorderLayout()), Disposable, ZoomableComponent {

    private class ZoomModelImpl(
        private val zoomChanged: () -> Unit,
    ) : ZoomModel {

        override var isZoomEnabled = true

        private companion object {
            const val INIT_VALUE = 1.0
            const val MIN_VALUE = 0.25
            const val MAX_VALUE = Double.MAX_VALUE
            const val ZOOM_COEFFICIENT = 1.25
        }

        override fun zoomIn() {
            if (!canZoomIn()) return
            zoom = min(MAX_VALUE, zoom * ZOOM_COEFFICIENT)
        }

        override fun zoomOut() {
            if (!canZoomOut()) return
            zoom = max(MIN_VALUE, zoom * (1 / ZOOM_COEFFICIENT))
        }

        override fun canZoomIn(): Boolean {
            return isZoomEnabled && zoom < MAX_VALUE
        }

        override fun canZoomOut(): Boolean {
            return isZoomEnabled && zoom > MIN_VALUE
        }

        override fun resetZoom() {
            if (!isZoomEnabled) return
            zoom = INIT_VALUE
        }

        private var _zoom: Double by Delegates.observable(INIT_VALUE) { _, _, _ ->
            zoomChanged()
        }
        override var zoom: Double
            get() = _zoom
            set(value) {
                _zoom = max(min(value, MAX_VALUE), MIN_VALUE)
            }
    }

    private class SvgModelImpl(
        private val svgUpdated: () -> Unit
    ) :
        SvgModel {
        override var svg: SVGDocument? by Delegates.observable(null) { _, _, _ ->
            svgUpdated()
        }
    }

    private val image: SVGComponent = SVGComponent(
        SvgModelImpl { svgChanged() },
        ZoomModelImpl { zoomChanged() },
    )

    private val scrollPane: JScrollPane
    private val imageContainer = JPanel() // container for handling image alignment

    private val wheelAdapter = MouseWheelAdapter(image.zoomModel)
    private val focusRequester = FocusRequester(this)

    val svgModel: SvgModel get() = image.svgModel
    override val zoomModel: ZoomModel get() = image.zoomModel

    init {
        imageContainer.add(image)
        imageContainer.layout = object : LayoutManager {
            override fun addLayoutComponent(name: String?, comp: Component?) {
            }

            override fun removeLayoutComponent(comp: Component?) {
            }

            override fun preferredLayoutSize(parent: Container?): Dimension? {
                return image.preferredSize
            }

            override fun minimumLayoutSize(parent: Container?): Dimension? {
                return image.minimumSize
            }

            override fun layoutContainer(parent: Container?) {
                center()
            }
        }

        scrollPane = ScrollPaneFactory.createScrollPane(imageContainer, true)
        add(scrollPane, BorderLayout.CENTER)
        scrollPane.addMouseWheelListener(wheelAdapter) // for handling zoom
        scrollPane.addMouseListener(focusRequester) // grab focus so actions work correctly

        // setup magnificator (IDE level zoom) support
        putClientProperty(Magnificator.CLIENT_PROPERTY_KEY, object : Magnificator {
            override fun magnify(scale: Double, at: Point): Point {
                val locationBefore = image.location
                val factor = zoomModel.zoom
                zoomModel.zoom = scale * factor
                return Point(
                    (((at.x - max(if (scale > 1.0) locationBefore.x else 0, 0)) * scale).toInt()),
                    (((at.y - max(if (scale > 1.0) locationBefore.y else 0, 0)) * scale).toInt())
                )
            }
        })
    }

    override fun dispose() {
        scrollPane.removeMouseWheelListener(wheelAdapter)
        scrollPane.removeMouseListener(focusRequester)
        removeAll()
    }

    /**
     * wheel adapter for zooming in/out when holding CTRL
     */
    private class MouseWheelAdapter(val model: ZoomModel) : MouseWheelListener {
        override fun mouseWheelMoved(e: MouseWheelEvent?) {
            e ?: return
            if (e.isControlDown) {
                if (e.wheelRotation < 0) {
                    model.zoomIn()
                } else if (e.wheelRotation > 0) {
                    model.zoomOut()
                }
                e.consume()
            }
        }
    }

    /**
     * Resets state when different svg is selected.
     */
    private fun svgChanged() {
        zoomModel.resetZoom()
        imageUpdated()
    }

    /**
     * Recalculates position after zooming so the user always stays on the same position
     */
    private fun zoomChanged() {
        val oldSize = imageContainer.size
        val old = scrollPane.viewport.viewPosition
        val extend = scrollPane.viewport.extentSize

        imageUpdated()


        // check both sides in case only one changed (because the other is larger than zoomed SVG)
        // if both changed, they should be ~same
        val coefficient = max(
            imageContainer.size.width.toDouble() / oldSize.width.toDouble(),
            imageContainer.size.height.toDouble() / oldSize.height.toDouble()
        )

        scrollPane.viewport.viewPosition = Point(
            ((old.x + extend.width / 2) * coefficient - extend.width / 2).toInt(),
            ((old.y + extend.height / 2) * coefficient - extend.height / 2).toInt(),
        )
    }

    /**
     * Centers image if the panel wider then it
     */
    private fun center() {
        val bounds = imageContainer.bounds
        val point = image.location
        // in embedded mode images should be left-side aligned
        point.x = (bounds.width - image.size.width) / 2
        point.y = (bounds.height - image.size.height) / 2
        image.location = point
    }

    /**
     * Manually updates the viewport of the scroll pane as it doesn't handle resizing automatically
     */
    private fun updateViewport() {
        val view = scrollPane.viewport.view
        scrollPane.viewport = null
        scrollPane.setViewportView(view)
    }

    /**
     * Resizes image and self after image is updated
     */
    private fun imageUpdated() {
        image.resize()
        imageContainer.size = image.size
        center()
        repaint()
        updateViewport()
    }
}
