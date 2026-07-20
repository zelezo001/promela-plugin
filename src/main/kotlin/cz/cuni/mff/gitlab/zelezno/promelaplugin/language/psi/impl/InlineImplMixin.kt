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
import com.intellij.psi.util.startOffset
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInline
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInlineArg
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaLabel
import javax.swing.Icon

/**
 * Mixin for inline definition implementation.
 * @param node the AST node
 */
abstract class InlineImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaInline {
    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override fun getPresentation(): ItemPresentation {
        val name = name
        return object : ItemPresentation {
            override fun getPresentableText(): @NlsSafe String? {
                if (name == null) return null
                return "$name()"
            }

            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Function
        }
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        if (lastParent == null) return true
        return PsiTreeUtil.getChildrenOfType(parenthesis, PromelaInlineArg::class.java)?.all { processor.execute(it, state) }.also {
            findChildrenByClass(PromelaLabel::class.java)
                .all { processor.execute(it, state) }
        } ?: true
    }

    override fun getName(): String? {
        return identifier?.name
    }

    override fun getTextOffset(): Int {
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun setName(newName: String): PromelaInline {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }
}