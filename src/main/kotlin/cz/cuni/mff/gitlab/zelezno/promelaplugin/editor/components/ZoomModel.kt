package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components

/**
 * Model for controlling zoom of the component
 */
interface ZoomModel {

    /**
     * Increases zoom
     */
    fun zoomIn()

    /**
     * Decreases zoom
     */
    fun zoomOut()

    /**
     * If calling [zoomIn] would change the zoom or not
     */
    fun canZoomIn(): Boolean

    /**
     * If calling [zoomOut] would change the zoom or not
     */
    fun canZoomOut(): Boolean

    /**
     * Restores [zoom] to its initial value
     */
    fun resetZoom()

    /**
     * True if component's zoom can be altered
     */
    var isZoomEnabled: Boolean

    /**
     * Zoom on the component. 1.0 mean no zoom
     */
    var zoom: Double
}