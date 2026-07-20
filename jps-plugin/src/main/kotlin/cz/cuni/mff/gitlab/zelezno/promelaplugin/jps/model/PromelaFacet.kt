package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model

import com.intellij.util.xmlb.XmlSerializer
import com.intellij.util.xmlb.annotations.Tag
import com.intellij.util.xmlb.annotations.XCollection
import org.jdom.Element
import org.jetbrains.jps.model.JpsElement
import org.jetbrains.jps.model.JpsElementChildRole
import org.jetbrains.jps.model.ex.JpsElementBase
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase
import org.jetbrains.jps.model.module.JpsModule
import org.jetbrains.jps.model.serialization.facet.JpsFacetConfigurationSerializer

/**
 * Spin/C compiler options sharable in one module.
 */
data class PromelaFacetState(
    @JvmField @Tag("spinCompilerOptions") @XCollection(
        elementName = "arg", elementTypes = [String::class]
    ) val spinOptions: List<String> = emptyList(),

    @JvmField @Tag("cCompilerOptions") @XCollection(
        elementName = "arg", elementTypes = [String::class]
    ) val cCompilerOptions: List<String> = emptyList(),
)

/**
 * Extension element for the Promela facet in JPS.
 * @param options the facet options
 */
class PromelaFacetElement(val options: PromelaFacetState) : JpsElementBase<PromelaFacetElement>()

/**
 * Serializer for the Promela facet configuration in JPS.
 */
class PromelaFacetJpsFacetConfigurationSerializer :
    JpsFacetConfigurationSerializer<PromelaFacetElement>(
        ROLE,
        "promela-facet",
        "Promela"
    ) {
    companion object {
        val ROLE: JpsElementChildRole<PromelaFacetElement?> =
            JpsElementChildRoleBase.create<PromelaFacetElement?>("PromelaFacet")
    }

    override fun loadExtension(
        p0: Element,
        p1: String?,
        p2: JpsElement?,
        p3: JpsModule?
    ): PromelaFacetElement {
        val state: PromelaFacetState =
            XmlSerializer.deserialize(p0, PromelaFacetState::class.java)

        return PromelaFacetElement(state)
    }
}