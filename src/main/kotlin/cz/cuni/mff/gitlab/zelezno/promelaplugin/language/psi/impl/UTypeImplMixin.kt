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
import com.intellij.psi.util.startOffset
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiTypeDeclaration
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.UserType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*
import javax.swing.Icon


/**
 * Mixin for user-defined type (typedef) elements.
 * @param node the AST node
 */
abstract class UTypeImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaUtype {
    override fun getNameIdentifier(): PsiElement? {
        return identifier
    }

    override fun getUseScope(): SearchScope = LocalSearchScope(containingFile ?: this)

    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): @NlsSafe String? {
                return name
            }

            override fun getIcon(unused: Boolean): Icon? {
                return AllIcons.Nodes.Type
            }
        }
    }

    override fun getType(): PsiType {
        return UserType(name, this)
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        return oneDeclList.all { it.processDeclarations(processor, state, null, place) }
    }

    override fun getName(): String? {
        return identifier?.name
    }

    override fun getTextOffset(): Int {
        return identifier?.textOffset ?: super.getTextOffset()
    }

    override fun setName(newName: String): PromelaUtype {
        ElementFactory.replaceIdentifier(identifier, newName)
        return this
    }
}