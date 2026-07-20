package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.NlsSafe

/**
 * Facet for storing module level Promela configuration (for now compiler/runtime options).
 * @param module the module for the facet
 * @param name the facet name
 * @param configuration the facet configuration
 * @param underlyingFacet the underlying facet, if any
 */
class PromelaFacet(
    module: Module,
    name: @NlsSafe String,
    configuration: PromelaFacetConfiguration,
    underlyingFacet: Facet<*>?
) : Facet<PromelaFacetConfiguration>(
    PromelaFacetType(),
    module,
    name,
    configuration,
    underlyingFacet
) {
    companion object {
        /**
         * Returns the Promela facet for the given module.
         * @param module the module to check
         * @return the Promela facet, or null if not found
         */
        fun getForModule(module: Module): PromelaFacet? =
            FacetManager.getInstance(module).getFacetByType(PROMELA_FACE_ID)
    }
}