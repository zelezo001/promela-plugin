package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.PsiTreeUtil
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaOneDecl
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaProctype

/**
 * Mixin for [PromelaProctype] implementation.
 * @param node the AST node
 */
abstract class ProctypeImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaProctype {
    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        if (lastParent == null) return true // local variables are scoped and invisible from parent
        return PsiTreeUtil.getChildrenOfType(parenthesis, PromelaOneDecl::class.java)
            ?.all { it.processDeclarations(processor, state, null, place) } ?: true
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): @NlsSafe String? {
                if (name == null) return null
                return "$name()"
            }

            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Function
        }
    }

    override fun getName(): String? {
        return identifier?.name
    }

    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun setName(newName: String): PsiElement? {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }

    override fun getTextOffset(): Int {
        return identifier?.textOffset ?: proctypeHeader.textOffset
    }
}