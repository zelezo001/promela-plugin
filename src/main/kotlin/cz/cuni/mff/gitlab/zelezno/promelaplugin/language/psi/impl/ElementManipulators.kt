package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl.*

/**
 * Element manipulator for [PromelaCustomTypenameImpl].
 */
class PromelaCustomTypenameManipulator : AbstractElementManipulator<PromelaCustomTypenameImpl>() {
    override fun handleContentChange(
        element: PromelaCustomTypenameImpl,
        range: TextRange,
        newContent: String?
    ): PromelaCustomTypenameImpl {
        val oldText: String = element.text
        val newText = oldText.substring(0, range.startOffset) + newContent + oldText.substring(range.endOffset)
        return element.updateText(newText)
    }
}

/**
 * Element manipulator for [PromelaVarImpl].
 */
class VarManipulator : AbstractElementManipulator<PromelaVarImpl>() {
    override fun handleContentChange(
        element: PromelaVarImpl,
        range: TextRange,
        newContent: String?
    ): PromelaVarImpl {
        val oldText: String = element.text
        val newText = oldText.substring(0, range.startOffset) + newContent + oldText.substring(range.endOffset)
        return element.updateText(newText)
    }
}

/**
 * Element manipulator for [PromelaIdentifierImpl].
 */
class IdentifierManipulator : AbstractElementManipulator<PromelaIdentifierImpl>() {
    override fun handleContentChange(
        element: PromelaIdentifierImpl,
        range: TextRange,
        newContent: String?
    ): PromelaIdentifierImpl {
        val oldText: String = element.text
        val newText = oldText.substring(0, range.startOffset) + newContent + oldText.substring(range.endOffset)
        val newIdentifier = ElementFactory.createIdentifier(element.project, newText)
        return element.replace(newIdentifier) as PromelaIdentifierImpl
    }
}

/**
 * Element manipulator for [PromelaRunImpl].
 */
class PromelaRunManipulator : AbstractElementManipulator<PromelaRunImpl>() {
    override fun handleContentChange(
        element: PromelaRunImpl,
        range: TextRange,
        newContent: String?
    ): PromelaRunImpl {
        val oldText: String = element.text
        val newText = oldText.substring(0, range.startOffset) + newContent + oldText.substring(range.endOffset)
        return element.updateText(newText)
    }
}