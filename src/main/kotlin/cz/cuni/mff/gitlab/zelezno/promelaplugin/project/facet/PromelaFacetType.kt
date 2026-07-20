package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetType
import com.intellij.facet.FacetTypeId
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.util.NlsSafe
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle

val PROMELA_FACE_ID = FacetTypeId<PromelaFacet>("promela-facet")

/**
 * Facet type for [PromelaFacet]
 */
class PromelaFacetType :
    FacetType<PromelaFacet, PromelaFacetConfiguration>(
        PROMELA_FACE_ID,
        "promela-facet",
        PromelaBundle.message("module.facet.name")
    ) {
    override fun createDefaultConfiguration() = PromelaFacetConfiguration()

    override fun createFacet(
        module: Module,
        name: @NlsSafe String?,
        configuration: PromelaFacetConfiguration,
        underlyingFacet: Facet<*>?
    ): PromelaFacet {
        return PromelaFacet(module, name ?: "Promela", configuration, underlyingFacet)
    }

    override fun isSuitableModuleType(moduleType: ModuleType<*>?) = true // Spin support is SDK-based
}