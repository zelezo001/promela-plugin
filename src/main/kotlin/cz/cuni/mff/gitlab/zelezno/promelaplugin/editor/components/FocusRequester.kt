package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components

import com.intellij.openapi.wm.IdeFocusManager
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JComponent

/**
 * Helper class for getting focus on mouse entrance.
 * Useful for situations where we need the focus
 * to enable actions without clicking on the components (so keyboard shortcuts work)
 */
internal class FocusRequester(private val component: JComponent) : MouseAdapter() {
    override fun mouseEntered(e: MouseEvent?) {
        IdeFocusManager.getGlobalInstance().requestFocus(component, true)
    }
}