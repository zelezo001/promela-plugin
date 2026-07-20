package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*

/**
 * Mixin for the top-level modules element.
 * @param node the AST node
 */
abstract class ModulesImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaModules {
    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        var skip = false
        for (child in children) {
            if (skip) return true // skip all remaining declarations as they are not visible from last parent
            if (child == lastParent) {
                skip = true
                if (child !is PromelaProctype) continue // proctype sees itself
            }
            val shouldContinue = when (child) {
                is PromelaUtype,
                is PromelaProctype,
                is PromelaInline ->
                    processor.execute(child, state)

                is PromelaOneDecl -> child.processDeclarations(processor, state, null, place)

                is PromelaMtype -> processor.execute(child, state) &&
                        child.processDeclarations(processor, state, null, place)

                else -> true
            }
            if (!shouldContinue) return false
        }
        return true
    }
}