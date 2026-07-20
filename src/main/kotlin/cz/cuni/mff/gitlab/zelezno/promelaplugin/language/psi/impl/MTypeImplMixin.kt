package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.startOffset
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.MType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaNamedElement
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiTypeDeclaration
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaMtype

/**
 * Handles presentation of a [PsiType].
 * @param type the type to present
 */
class TypePresentation(private val type: PsiType) : ItemPresentation {
    override fun getPresentableText() = type.getName()

    override fun getIcon(unused: Boolean) = AllIcons.Nodes.Type
}

/**
 * Mixin for mtype declaration implementation.
 * @param node the AST node
 */
abstract class MTypeImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaMtype, PsiTypeDeclaration {

    override fun getType() = MType(name)

    override fun getPresentation() = TypePresentation(getType())

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        processor.handleEvent(PsiScopeProcessor.Event.SET_DECLARATION_HOLDER, this)
        return mtypeValueList.all { processor.execute(it, state) }
    }

    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getTextOffset(): Int {
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun getName(): String {
        return identifier?.name ?: MType.GENERIC_NAME
    }

    override fun setName(newName: String): PromelaMtype? {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }
}