package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components

import com.intellij.openapi.actionSystem.DataKey

/**
 * Component whose content can be zoomed
 */
interface ZoomableComponent {
    companion object {
        val DATA_KEY = DataKey.create<ZoomableComponent>(ZoomableComponent::class.java.name)
    }

    val zoomModel: ZoomModel
}