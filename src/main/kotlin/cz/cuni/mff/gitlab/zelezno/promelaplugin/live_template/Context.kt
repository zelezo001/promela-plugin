package cz.cuni.mff.gitlab.zelezno.promelaplugin.live_template

import com.intellij.codeInsight.template.FileTypeBasedContextType
import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.psi.util.findParentInFile
import com.intellij.psi.util.findParentOfType
import com.intellij.psi.util.startOffset
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFileType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaAnyExpression
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaBaseExpression
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaStep

/**
 * Base live templates context for Promela related contexts.
 * Should be used as a parent of all other Promela live template contexts.
 */
abstract class PromelaContext(name: String) : FileTypeBasedContextType(name, PromelaFileType);

/**
 * Live templates context anywhere inside a Promela file
 */
class PromelaFileContext : PromelaContext(
    PromelaBundle.message("liveTemplates.context.file")
)

/**
 * Live templates context among top-level definitions
 */
class PromelaTopLevelContext : PromelaContext(
    PromelaBundle.message("liveTemplates.context.topLevel")
) {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        if (!super.isInContext(templateActionContext)) return false
        val context =
            templateActionContext.file.findElementAt(templateActionContext.startOffset)?.parent // parsing error
                ?.parent // hopefully PromelaFile
        // or we matched valid identifier

        return context is PromelaFile
    }
}

/**
 * Live templates context inside sequences (proctype bodies, inline bodies, for loops, if branches...)
 */
class PromelaSequenceContext : PromelaContext(
    PromelaBundle.message("liveTemplates.context.sequence")
) {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        if (!super.isInContext(templateActionContext)) return false
        val context = templateActionContext.file.findElementAt(templateActionContext.startOffset)
        val parent = context?.findParentOfType<PromelaStep>() ?: return false

        return parent.startOffset == context.startOffset // we are at the start of step in sequence

    }
}

/**
 * Live templates context inside non-constant expressions
 */
class PromelaExpressionContext : PromelaContext(
    PromelaBundle.message("liveTemplates.context.expression")) {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        if (!super.isInContext(templateActionContext)) return false
        val context = templateActionContext.file.findElementAt(templateActionContext.startOffset)
        val parent =
            context?.findParentInFile { it is PromelaAnyExpression || it is PromelaBaseExpression } ?: return false
        return parent.startOffset == context.startOffset
    }
}
