package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.util.elementType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ScalarType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypename
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Mixin for typename implementation.
 * @param node the AST node
 */
abstract class TypenameImplMixin(node: ASTNode) : ASTWrapperPsiElement(node), PromelaTypename {
    override fun getType(): PsiType? {
        if (customTypename != null) {
            return customTypename?.getType()
        }
        if (mtypename != null) {
            return mtypename?.getType()
        }
        return when (firstChild.elementType) {
            PromelaTypes.CHAN_TYPE_KW -> ScalarType.CHANNEL_TYPE
            PromelaTypes.BIT_TYPE_KW -> ScalarType.BIT_TYPE
            PromelaTypes.BOOL_TYPE_KW -> ScalarType.BOOL_TYPE
            PromelaTypes.BYTE_TYPE_KW -> ScalarType.BYTE_TYPE
            PromelaTypes.SHORT_TYPE_KW -> ScalarType.SHORT_TYPE
            else -> ScalarType.INTEGER_TYPE
        }
    }
}