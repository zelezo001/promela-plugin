package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaOneDecl

/**
 * Mixin for variable declaration implementation.
 * @param node the AST node
 */
abstract class OneDeclImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaOneDecl {
    override fun processDeclarations(
        processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement
    ): Boolean {
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        for (ivar in ivarList) {
            if (ivar.name == null) continue
            if (!processor.execute(ivar, state)) return false
        }
        if (unsignedDecl?.name != null) {
            return processor.execute(unsignedDecl!!, state)
        }
        return true
    }
}