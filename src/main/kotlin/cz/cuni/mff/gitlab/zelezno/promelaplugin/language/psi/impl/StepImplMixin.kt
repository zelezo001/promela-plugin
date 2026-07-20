package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaStep

/**
 * Mixin for step implementation.
 * @param node the AST node
 */
abstract class StepImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaStep {
    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        return oneDeclList.all { it.processDeclarations(processor, state, null, place) }
    }
}