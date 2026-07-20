package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi

import com.intellij.lang.ASTNode
import com.intellij.lang.ForeignLeafType
import com.intellij.psi.impl.source.tree.ForeignLeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.ILeafElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.startOffset
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes
import org.jetbrains.annotations.NonNls

/**
 * Class handling correct insertion of empty tokens into PSI tree (generated mainly by [cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor.MacroAwareLexer]).
 * @param type the leaf type
 * @param text the text of the leaf
 */
class PromelaForeignLeafPsiElement(type: ForeignLeafType, text: CharSequence) : ForeignLeafPsiElement(type, text) {
    override fun getStartOffset(): Int {
        // Promela ForeignLeafPsiElement are tokens inserted by the preprocessor, and they must behave like normal tokens
        return startOffsetInParent + (parent?.startOffset ?: 0);
    }
}

/**
 * Wrapper class for passing around text with empty tokens.
 * Used mainly by [cz.cuni.mff.gitlab.zelezno.promelaplugin.language.lexer.preprocessor.MacroAwareLexer].
 * @param delegate the original element type
 * @param text the text content
 */
class PromelaTokenWrapper(delegate: IElementType, text: CharSequence) :
    com.intellij.lang.ForeignLeafType(delegate, text),
    ILeafElementType {
    @Override
    override fun equals(other: Any?): Boolean {
        return delegate == other
    }

    override fun hashCode(): Int {
        return delegate.hashCode()
    }

    override fun createLeafNode(leafText: CharSequence): ASTNode {
        return PromelaForeignLeafPsiElement(this, text)
    }
}

/**
 * [IElementType] for Promela tokens.
 * @param debugName the name of the token for debugging
 */
class PromelaTokenType(@NonNls debugName: String) : IElementType(debugName, PromelaLanguage) {
    override fun toString(): String {
        return "PromelaTokenType." + super.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is PromelaTokenWrapper) {
            return this == other.delegate
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}