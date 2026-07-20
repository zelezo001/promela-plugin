package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.model.Pointer
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.platform.backend.documentation.DocumentationResult
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.createSmartPointer
import com.intellij.psi.scope.PsiScopeProcessor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.icons.PromelaIcons
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ArrayType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaNamedElement
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable

/**
 * Represents a documentation target for a Promela element. The class constructs and provides
 * detailed documentation for a specified Promela named element.
 *
 * @param T The type of the Promela element for which this documentation target is defined.
 *
 * @property el The Promela named element for which this documentation target is created.
 * @property pointer A smart pointer to the current documentation target.
 * @property declarationDocumentationBuilder Builder for constructing the element's definition in
 * the documentation.
 */
internal class PromelaDocumentationTarget<T : PromelaNamedElement> private constructor(
    private val el: T,
    private val pointer: Ptr<T>,
    private val declarationDocumentationBuilder: DeclarationDocumentationBuilder<T>
) : DocumentationTarget {

    class Ptr<T : PromelaNamedElement>(
        private val el: Pointer<T>,
        private val declarationDocumentationBuilder: DeclarationDocumentationBuilder<T>,
    ) : Pointer<PromelaDocumentationTarget<T>> {
        override fun dereference(): PromelaDocumentationTarget<T>? {
            val dereferencedEl = el.dereference() ?: return null
            return PromelaDocumentationTarget(dereferencedEl, this, declarationDocumentationBuilder)
        }
    }

    constructor(el: T, declarationDocumentationBuilder: DeclarationDocumentationBuilder<T>) : this(
        el, Ptr(el.createSmartPointer(), declarationDocumentationBuilder), declarationDocumentationBuilder
    )

    private fun writeDocFromComments(descriptionBuilder: StringBuilder) {
        val documentation = StringBuilder()
        if (PromelaDocumentationCommentsCollector.findDocumentationText(el, documentation)) {
            descriptionBuilder.append(DocumentationMarkup.CONTENT_START)
            descriptionBuilder.append(documentation)
            descriptionBuilder.append(DocumentationMarkup.CONTENT_END)
        }
    }

    private fun writeLocation(descriptionBuilder: StringBuilder) {
        if (!shouldDisplayFileName()) return

        val icon = HtmlChunk.tag("icon").attr("src", PromelaIcons.PromelaFile.toString());

        descriptionBuilder.append(DocumentationMarkup.CONTENT_START)
        descriptionBuilder.append(
            DocumentationMarkup.BOTTOM_ELEMENT.child(icon).addText(el.containingFile.name)
        )
        descriptionBuilder.append(DocumentationMarkup.CONTENT_END)
    }

    override fun computeDocumentation(): DocumentationResult {
        val descriptionBuilder = StringBuilder()

        descriptionBuilder.append(DocumentationMarkup.DEFINITION_START)
        declarationDocumentationBuilder.build(descriptionBuilder, el)
        descriptionBuilder.append(DocumentationMarkup.DEFINITION_END)

        writeDocFromComments(descriptionBuilder)

        writeLocation(descriptionBuilder)

        return DocumentationResult.documentation(descriptionBuilder.toString())
    }

    private fun shouldDisplayFileName(): Boolean {
        if (el !is PsiVariable) return true
        // it doesn't make sense to display file name for local variables
        return el.isGlobalVariable
    }

    override fun computeDocumentationHint(): @NlsContexts.HintText String {
        val descriptionBuilder = StringBuilder()
        descriptionBuilder.append(DocumentationMarkup.DEFINITION_START)
        declarationDocumentationBuilder.build(descriptionBuilder, el)
        descriptionBuilder.append(DocumentationMarkup.DEFINITION_END)
        return descriptionBuilder.toString()
    }

    override val navigatable: Navigatable?
        get() = el as? Navigatable

    override fun computePresentation(): TargetPresentation {
        return TargetPresentation.builder(el.name!!).presentation()
    }

    override fun createPointer(): Pointer<out DocumentationTarget> = pointer
}

/**
 * Builder for building declaration of an internal object shown in documentation
 */
internal fun interface DeclarationDocumentationBuilder<T : PromelaNamedElement> {
    fun build(declarationBuilder: StringBuilder, el: T)
}

internal fun writeVariableDeclarations(builder: StringBuilder, el: PsiElement) {
    var addSemi = false
    val processor = PsiScopeProcessor { declaration, _ ->
        if (declaration is PsiVariable) {
            if (addSemi) {
                builder.append(";")
            }
            builder.append("\n   ")
            builder.append(declaration.formatNameWithType())
            addSemi = true
        }
        true
    }
    el.processDeclarations(
        processor, ResolveState.initial(), el, el
    )
    if (addSemi) {
        builder.append("\n")
    }
}

