package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReference
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope

/**
 * NavigatablePsiElement with mandatory value from getPresentation
 */
interface NavigablePsiElement : NavigatablePsiElement {
    override fun getPresentation(): ItemPresentation
}

/**
 * Base interface for all PSI elements with a name
 */
interface PromelaNamedElement : PsiNameIdentifierOwner, NavigablePsiElement

/**
 * Interface for type declarations: mtype, user types (typedef)...
 */
interface PsiTypeDeclaration : PromelaNamedElement, PsiElementWithType

/**
 * Interface for typed objects
 */
interface PsiElementWithType : PsiElement {
    /**
     * Returns the type of this element.
     * @return the type, or null if it cannot be determined
     */
    fun getType(): PsiType?
}

interface NamedPsiElement : PsiElement {
    fun getName(): String?
}

/**
 * Base interface for variables.
 */
interface PsiVariable : PromelaNamedElement, PsiElementWithType {
    /**
     * Should be true for variables declared in top-level scope (and mtypes)
     */
    val isGlobalVariable: Boolean

    /**
     * Formats variable as "type name[1]"
     */
    fun formatNameWithType(): String {
        val type = getType() ?: return name.orEmpty()
        if (type is ArrayType) {
            return "${type.itemType.getName()} ${name}[${type.size}]"
        }
        if (type is UnsignedType) {
            return "unsigned $name:${type.bitSize}"
        }

        return "${type.getName()} $name"
    }
}

interface ConstantExpression : PsiElement {
    /**
     * Calculates the constant value of this expression.
     * @return the constant value, or null if it's not a constant
     */
    fun calculateConstantValue(): Long?
}

/**
 * References referencing typed objects: types, variables...
 */
interface PsiElementWithTypeReference : PsiReference {
    override fun resolve(): PsiElementWithType?
}

/**
 * Reference referencing [PsiVariable].
 */
interface PsiVariableReference : PsiElementWithTypeReference {
    override fun resolve(): PsiVariable?
}

/**
 * Element referencing a [PsiVariable]
 */
interface PsiVariableReferencingElement : PsiElement, NamedPsiElement {
    override fun getReference(): PsiVariableReference?
}
