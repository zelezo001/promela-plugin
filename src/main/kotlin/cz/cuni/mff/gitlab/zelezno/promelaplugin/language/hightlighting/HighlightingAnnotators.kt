package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.hightlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaMacroTypes
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*

/**
 * Handles highlighting of code not directly processed by PSI implementation (DISABLED_CODE, PREPROCESSOR_DEFINE_CALL, C_CODE)
 */
class IgnoredCodeHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element.elementType) {
            PromelaTypes.DISABLED_CODE -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element)
                    .textAttributes(PromelaSyntaxHighlighter.BLOCK_COMMENT)
                    .create()
            }

            PromelaMacroTypes.PREPROCESSOR_DEFINE_CALL -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element)
                    .textAttributes(PromelaSyntaxHighlighter.MACRO).create()
            }

            PromelaTypes.C_CODE -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element)
                    .textAttributes(PromelaSyntaxHighlighter.C_CODE).create()
            }
        }
    }
}

/**
 * Annotator for highlighting proctype identifiers.
 */
class ProctypeHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is PromelaProctype && element.identifier != null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.identifier!!)
                .textAttributes(PromelaSyntaxHighlighter.PROCTYPE_DECLARATION)
                .create()
        }
        if (element is PromelaRun && element.identifier !== null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.identifier!!)
                .textAttributes(PromelaSyntaxHighlighter.PROCTYPE_RUN)
                .create()
        }
    }
}

/**
 * Annotator for highlighting inline definitions and calls.
 */
class InlineHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is PromelaInline && element.identifier !== null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.identifier!!)
                .textAttributes(PromelaSyntaxHighlighter.PROCTYPE_DECLARATION)
                .create()
        }
        if (element is PromelaInlineCall && element.firstChild !== null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.firstChild!!)
                .textAttributes(PromelaSyntaxHighlighter.INLINE_CALL)
                .create()
        }
    }
}