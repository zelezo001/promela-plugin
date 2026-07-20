package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.NlsSafe
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*
import javax.swing.Icon

/**
 * Handles Presentation of the [PsiVariable].
 * @param el the variable element to present
 */
class VariablePresentation(private val el: PsiVariable) : ItemPresentation {

    override fun getPresentableText(): @NlsSafe String = el.formatNameWithType()

    override fun getIcon(unused: Boolean): Icon? {
        return when (el.parent) {
            is PromelaOneDecl -> {
                when (el.parent.parent) {
                    is PromelaUtype -> AllIcons.Nodes.Field
                    else -> AllIcons.Nodes.Variable
                }
            }

            is PromelaMtype -> AllIcons.Nodes.Variable
            is PromelaInline, is PromelaProctype -> AllIcons.Nodes.Parameter
            else -> AllIcons.Nodes.Variable
        }
    }
}