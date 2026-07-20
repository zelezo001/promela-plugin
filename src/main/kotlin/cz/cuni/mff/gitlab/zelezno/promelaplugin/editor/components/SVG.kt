package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components

import com.github.weisj.jsvg.SVGDocument
import com.github.weisj.jsvg.view.ViewBox
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JComponent

/**
 * Model for an SVG document.
 */
interface SvgModel {
    /**
     * The current SVG document.
     */
    var svg: SVGDocument?
}

/**
 * Component for dynamically rendered SVG.
 * @param svgModel the model for the SVG document
 * @param zoomModel the zoom model for the component
 */
class SVGComponent(
    val svgModel: SvgModel,
    override val zoomModel: ZoomModel
) : ZoomableComponent,
    JComponent() {

    init {
        // so we don't render on every paint
        isDoubleBuffered = true
    }

    fun resize() {
        size = svgModel.svg?.size()?.let {
            val scale = zoomModel.zoom
            Dimension((scale * it.width).toInt(), (scale * it.height).toInt())
        } ?: Dimension()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        g ?: return

        val clip = g.clipBounds
        val svg = svgModel.svg ?: return
        val svgDimensions = svg.size()

        val pg = g.create(clip.x, clip.y, clip.width, clip.height)
        val target = (pg as Graphics2D)
        target.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        target.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        target.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

        svg.render(
            this, target, ViewBox(
                // move view-box to correct position
                -clip.x.toFloat(),
                -clip.y.toFloat(),
                // scale the SVG
                svgDimensions.width * zoomModel.zoom.toFloat(),
                svgDimensions.height * zoomModel.zoom.toFloat(),
            )
        )
    }

    override fun getPreferredSize(): Dimension? {
        val size = Dimension()
        svgModel.svg?.size()?.also {
            size.setSize(zoomModel.zoom * it.width, zoomModel.zoom * it.height)
        }
        return size // report whole size of SVG, so our parent can generate scrollbars if needed
    }
}