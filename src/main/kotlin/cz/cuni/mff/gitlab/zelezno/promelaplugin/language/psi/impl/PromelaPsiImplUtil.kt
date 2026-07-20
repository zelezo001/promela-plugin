package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaIdentifier

/**
 * Utility class for Promela PSI implementation.
 */
object PromelaPsiImplUtil {
    /**
     * Gets the name of the given identifier element.
     * @param el the identifier element
     * @return the name of the identifier
     */
    @JvmStatic
    fun getName(el: PromelaIdentifier): String? {
        return el.firstChild?.text
    }
}
