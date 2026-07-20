package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.*
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaPropertyAccess
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaVarref

/**
 * Mixin for [PromelaVarref] implementation.
 * @param node the AST node
 */
abstract class VarRefImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaVarref {
    override fun getReferences(): Array<out PsiReference?> {
        var previousReference: PsiElementWithTypeReference = `var`.reference ?: return emptyArray()
        var previousVariableIsArrayAccessed = indexer != null
        return propertyAccessList.map { propertyAccess ->
            PropertyReference(
                el = this,
                part = propertyAccess,
                chained = previousReference,
                chainedShouldBeArray = previousVariableIsArrayAccessed
            ).also {
                previousReference = it
                previousVariableIsArrayAccessed = propertyAccess.indexer != null
            }
        }.toTypedArray()
    }
}

/**
 * Reference to a property (field) in a user-defined type or array.
 * @param el the element containing the reference
 * @param part the property access element
 * @param chained the preceding reference in the chain
 * @param chainedShouldBeArray true if the preceding element should be an array
 */
class PropertyReference(
    el: VarRefImplMixin,
    val part: PromelaPropertyAccess,
    val chained: PsiElementWithTypeReference,
    val chainedShouldBeArray: Boolean
) : PsiReferenceBase<VarRefImplMixin>(
    el,
    part.identifier.textRangeInParent.shiftRight(part.startOffsetInParent),
    false
), PsiVariableReference {

    override fun handleElementRename(newElementName: String): PsiElement {
        ElementFactory.replaceIdentifier(part.identifier, newElementName)
        return part
    }

    private fun resolveVariablesContext(): PsiElement? {
        if (part.identifier.name.isNullOrEmpty()) return null
        var previousType = chained.resolve()?.getType() ?: return null
        if (chainedShouldBeArray) {
            if (previousType !is ArrayType) return null
            previousType = previousType.itemType
        }
        if (previousType is UserType) return previousType.declarationElement
        return null
    }

    /**
     * Resolves the property reference.
     * @return the resolved variable (field), or null if not found
     */
    override fun resolve(): PsiVariable? {
        var lookingFor: PsiVariable? = null
        val processor = PsiScopeProcessor { declaration, _ ->
            if (declaration is PsiVariable && declaration.name == part.identifier.name) {
                lookingFor = declaration; false
            } else true
        }
        resolveVariablesContext()?.processDeclarations(
            processor,
            ResolveState.initial(),
            null,
            element
        )

        return lookingFor
    }

    /**
     * Returns all available properties for completion.
     * @return an array of lookup elements
     */
    override fun getVariants(): Array<out Any> {
        val variables = mutableMapOf<String, Any>()
        val processor = PsiScopeProcessor { element, _ ->
            if (element is PsiVariable && !element.name.isNullOrEmpty()) {
                variables.computeIfAbsent(element.name!!) {
                    buildLookupElement(element)
                }
            }
            true
        }
        resolveVariablesContext()?.processDeclarations(
            processor,
            ResolveState.initial(),
            null,
            element
        )

        return variables.values.toTypedArray()
    }

    private fun buildLookupElement(el: PsiVariable): LookupElement {
        return LookupElementBuilder
            .create(el)
            .withTypeText(el.getType()?.getName()).withIcon(AllIcons.Nodes.Field)
    }
}

