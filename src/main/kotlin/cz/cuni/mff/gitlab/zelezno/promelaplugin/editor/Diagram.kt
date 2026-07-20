package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor

import com.github.weisj.jsvg.SVGDocument

/**
 * Represents a diagram with a name and its SVG document.
 * @param name the name of the diagram
 * @param svg the SVG document of the diagram
 */
data class Diagram(val name: String, val svg: SVGDocument) {
    override fun toString(): String {
        return name
    }
}