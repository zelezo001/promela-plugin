package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaLabel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaSequence

/**
 * Mixin for sequence implementation.
 * @param node the AST node
 */
abstract class SequenceImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaSequence {
    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        for (child in children) {
            if (child == lastParent) break // skip all remaining declarations as they are not visible from the last parent
            if (!child.processDeclarations(processor, state, null, place)) return false
        }
        return PsiTreeUtil.findChildrenOfType(
            this,
            PromelaLabel::class.java,
        ).all { processor.execute(it, state) }
    }
}