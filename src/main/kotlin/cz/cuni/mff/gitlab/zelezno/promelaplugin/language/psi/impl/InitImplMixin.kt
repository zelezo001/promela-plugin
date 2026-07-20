package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInit

/**
 * Mixin for init block implementation.
 * @param node the AST node
 */
abstract class InitImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaInit {

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "init"
            override fun getIcon(unused: Boolean) = AllIcons.Json.Object // maybe something different
        }
    }
}