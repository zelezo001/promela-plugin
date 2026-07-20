package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.jetbrains.rd.generator.nova.GenerationSpec.Companion.nullIfEmpty
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaNever

/**
 * Mixin for never claim implementation.
 * @param node the AST node
 */
abstract class NeverImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaNever {

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = identifier?.name?.nullIfEmpty()?.let { "never $it" } ?: "never"
            override fun getIcon(unused: Boolean) = AllIcons.Json.Object
        }
    }
}