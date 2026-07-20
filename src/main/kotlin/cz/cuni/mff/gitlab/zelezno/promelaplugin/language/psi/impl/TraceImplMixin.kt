package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTrace

/**
 * Mixin for trace/notrace implementation.
 * @param node the AST node
 */
abstract class TraceImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaTrace {

    override fun getPresentation(): ItemPresentation {
        val name = if (noTrace == null) "trace" else "notrace"
        return object : ItemPresentation {
            override fun getPresentableText() = name

            override fun getIcon(unused: Boolean) = AllIcons.Json.Object
        }
    }
}