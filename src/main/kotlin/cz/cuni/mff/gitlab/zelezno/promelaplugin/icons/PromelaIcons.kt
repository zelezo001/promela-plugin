package cz.cuni.mff.gitlab.zelezno.promelaplugin.icons

import com.intellij.openapi.util.IconLoader.getIcon
import javax.swing.Icon


/**
 * Icons used by the Promela plugin.
 */
object PromelaIcons {
    /**
     * Icon for Promela files.
     */
    @JvmField
    val PromelaFile: Icon = getIcon("/icons/pml.svg", PromelaIcons::class.java)
}