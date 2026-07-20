package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi

import com.intellij.psi.tree.IElementType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import org.jetbrains.annotations.NonNls

/**
 * [IElementType] for Promela elements.
 * @param debugName the name of the element for debugging
 */
class PromelaElementType(@NonNls debugName: String) : IElementType(debugName, PromelaLanguage)