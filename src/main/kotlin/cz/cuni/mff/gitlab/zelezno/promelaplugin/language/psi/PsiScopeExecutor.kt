package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaModules

object PsiScopeActionExecutor {
    /**
     * Processes all declarations present on the walk to the PSI tree root.
     * @param start the element where to start the walk
     * @param processor the scope processor to use
     */
    fun executeFromBottomUp(start: PsiElement, processor: PsiScopeProcessor) {
        var element: PsiElement? = start
        var lastParent: PsiElement? = null
        val state = ResolveState.initial()
        while (element != null) {
            if (!element.processDeclarations(processor, state, lastParent, start)) {
                return
            }
            lastParent = element
            element = element.parent
        }
    }

    /**
     * Processes all declarations present in the file as if they were requested from a child after the last child of the file.
     * @param element the element whose file to process
     * @param processor the scope processor to use
     */
    fun executeForFile(element: PsiElement, processor: PsiScopeProcessor) {
        val state = ResolveState.initial()
        PsiTreeUtil.getChildOfType(element.containingFile, PromelaModules::class.java)
            ?.processDeclarations(processor, state, null, element)
    }
}