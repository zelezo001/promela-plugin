package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiScopeActionExecutor
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiTypeDeclaration
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaInline
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaModules
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * Completion contributor for the Promela language.
 * Handles keyword-based completion and completion for custom inline and type identifiers,
 * as they cannot be resolved via references due to identifier retyping in [cz.cuni.mff.gitlab.zelezno.promelaplugin.parser.PromelaParser].
 */
class PromelaCompletionContributor : CompletionContributor() {

    /**
     * A completion provider that enables inline completion suggestions for elements of type `PromelaInline`.
     * This cannot be done via references as inline names are retyped in the parser and therefore standard
     * resolving techniques do not work on them.
     */
    private object InlineCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            params: CompletionParameters, ctx: ProcessingContext, resultSet: CompletionResultSet
        ) {
            val processor = PsiScopeProcessor { element, _ ->
                if (element is PromelaInline) {
                    resultSet.addElement(buildForInline(element))
                }
                true
            }

            PsiScopeActionExecutor.executeFromBottomUp(params.position, processor)
        }

        private fun buildForInline(el: PromelaInline): LookupElement {
            val tailedTextBuilder = StringBuilder()
            tailedTextBuilder.append('(');
            // collect declarations
            run {
                var addCol = false
                val processor = PsiScopeProcessor { declaration, _ ->
                    if (declaration is PsiVariable) {
                        if (addCol) {
                            tailedTextBuilder.append(", ")
                        }
                        tailedTextBuilder.append("${declaration.name}")
                        addCol = true
                    }
                    true
                }
                el.processDeclarations(processor, ResolveState.initial(), el, el)
            }
            tailedTextBuilder.append(')')

            return LookupElementBuilder.create(el).withIcon(AllIcons.Nodes.Function)
                .withTailText(tailedTextBuilder.toString(), true)
                .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
        }
    }

    /**
     * Completion provider for conditional language features.
     * @param insertHandler the handler for inserting the completion element
     * @param tokenStrings the list of token strings to suggest
     */
    private class ConditionalLanguageFeatures(
        insertHandler: InsertHandler<LookupElement>?,
        vararg tokenStrings: String,
    ) : CompletionProvider<CompletionParameters>() {

        private val suggestions = tokenStrings.map { LookupElementBuilder.create(it).withInsertHandler(insertHandler) }

        override fun addCompletions(
            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
        ) {
            result.addAllElements(suggestions)
        }
    }

    /**
     * Completion provider for a single conditional language feature.
     * @param tokenString the token string to suggest
     * @param insertHandler the handler for inserting the completion element
     */
    class ConditionalLanguageFeature(
        private val tokenString: String,
        private val insertHandler: InsertHandler<LookupElement>? = null,
    ) : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
        ) {
            result.addElement(LookupElementBuilder.create(tokenString).withInsertHandler(insertHandler))
        }
    }

    private object ModulesLanguageFeaturesProvider : CompletionProvider<CompletionParameters>() {
        private val keywords =
            listOf("never", "trace", "notrace", "active", "typedef", "init", "inline", "proctype", "D_proctype").map {
                LookupElementBuilder.create(it).withIcon(AllIcons.Nodes.Type)
                    .withInsertHandler(AddSpaceInsertHandler.INSTANCE)
            }

        override fun addCompletions(
            params: CompletionParameters, ctx: ProcessingContext, resultSet: CompletionResultSet
        ) {
            resultSet.addAllElements(keywords)
        }
    }

    private object ProctypeKeywordInDefinitionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
        ) {
            result.addElement(LookupElementBuilder.create("proctype").withInsertHandler(AddSpaceInsertHandler.INSTANCE))
            result.addElement(
                LookupElementBuilder.create("D_proctype").withInsertHandler(AddSpaceInsertHandler.INSTANCE)
            )
        }
    }

    private object StatementLanguageFeaturesProvider : CompletionProvider<CompletionParameters>() {
        private val keywords = listOf("for", "select", "do", "if", "else", "skip", "xs", "xr").map {
            LookupElementBuilder.create(it)
        }
        private val keywordsWithSpace = listOf("d_step", "assert", "atomic").map {
            LookupElementBuilder.create(it).withInsertHandler(AddSpaceInsertHandler.INSTANCE)
        }
        private val keywordsWithPopup = listOf("goto", "run").map {
            LookupElementBuilder.create(it).withInsertHandler(AddSpaceInsertHandler.INSTANCE_WITH_AUTO_POPUP)
        }

        override fun addCompletions(
            params: CompletionParameters, ctx: ProcessingContext, resultSet: CompletionResultSet
        ) {
            resultSet.addElement(
                LookupElementBuilder.create("printf").withIcon(AllIcons.Nodes.Function)

                    .withTailText(("(string message, ...)"), true)
                    .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
            )
            resultSet.addElement(
                LookupElementBuilder.create("printm").withIcon(AllIcons.Nodes.Function).withTailText(("(var)"), true)
                    .withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
            )

            resultSet.addAllElements(keywords)
            resultSet.addAllElements(keywordsWithSpace)
            resultSet.addAllElements(keywordsWithPopup)
        }
    }

    /**
     * Completion provider for suggesting type declarations and built-in Promela types during code completion.
     * Works both for build-in and custom types.
     * Custom types cannot be resolved via references as their names are retyped in the parser and therefore standard
     */
    private open class TypeCompletionProvider : CompletionProvider<CompletionParameters>() {
        private val buildInTypes = listOf("unsigned", "bit", "bool", "pid", "byte", "short", "int", "chan").map {
            LookupElementBuilder.create(it).withIcon(AllIcons.Nodes.Type)
                .withInsertHandler(AddSpaceInsertHandler.INSTANCE)
        }

        private fun addBuildInTypes(resultSet: CompletionResultSet) {
            for (buildInType in buildInTypes) {
                resultSet.addElement(buildInType)
            }
        }

        final override fun addCompletions(
            params: CompletionParameters, ctx: ProcessingContext, resultSet: CompletionResultSet
        ) {
            addBuildInTypes(resultSet)
            addVisibleTypes(params, resultSet)
        }

        private fun addVisibleTypes(
            params: CompletionParameters, resultSet: CompletionResultSet
        ) {
            val contextElement = getContextElement(params) ?: return

            val processor = PsiScopeProcessor { element, _ ->
                if (element is PsiTypeDeclaration) {
                    LookupElementBuilder.create(element).withIcon(AllIcons.Nodes.Type)
                        .withInsertHandler(AddSpaceInsertHandler.INSTANCE).also {
                            resultSet.addElement(it)
                        }
                }
                true
            }
            PsiScopeActionExecutor.executeFromBottomUp(contextElement, processor)
        }

        protected open fun getContextElement(params: CompletionParameters): PsiElement? {
            return params.position
        }
    }

    private object GlobalVariableTypeCompletionProvider : TypeCompletionProvider() {
        override fun getContextElement(params: CompletionParameters): PsiElement? {
            return (params.position.containingFile as PromelaFile).findChildByClass(PromelaModules::class.java)
        }
    }

    init {
        var modulesStart = PlatformPatterns.psiElement()
            .insideStarting(PlatformPatterns.psiElement().withParent(PlatformPatterns.psiFile(PromelaFile::class.java)))

        // looking for PromelaTypes.UNAME, because Intellij inserts an identifier when resolving completion suggestions,
        // which is then parsed by our parser as a VARREF

        val stepStart = PlatformPatterns.psiElement(PromelaTypes.UNAME)
            .insideStarting(PlatformPatterns.psiElement(PromelaTypes.STEP))
        val baseExpressionStart = PlatformPatterns.psiElement(PromelaTypes.UNAME).insideStarting(
            PlatformPatterns.psiElement().withElementType(TokenSet.create(PromelaTypes.VARREF_BASE_EXPRESSION))
        )
        val expressionStart = PlatformPatterns.psiElement(PromelaTypes.UNAME).insideStarting(
            PlatformPatterns.psiElement().withElementType(
                TokenSet.create(
                    PromelaTypes.VARREF_BASE_EXPRESSION, PromelaTypes.VARREF_ANY_EXPRESSION
                )
            )
        )

        val insideTypedef = PlatformPatterns.psiElement(PromelaTypes.UNAME).afterLeaf(
            PlatformPatterns.or(
                PlatformPatterns.psiElement(PromelaTypes.SEMI),
                PlatformPatterns.psiElement(PromelaTypes.OPENCURLYBRACKET)
            )
        ).inside(true, PlatformPatterns.psiElement(PromelaTypes.UTYPE))

        val insideProctype = PlatformPatterns.psiElement(PromelaTypes.UNAME).afterLeaf(
            PlatformPatterns.or(
                PlatformPatterns.psiElement(PromelaTypes.SEMI), PlatformPatterns.psiElement(PromelaTypes.OPENBRACKET)
            )
        ).inside(true, PlatformPatterns.psiElement(PromelaTypes.PROCTYPE))

        // inserting (D_)proctype keywords after active in proctype definition
        extend(
            null,
            PlatformPatterns.psiElement()
                .andNot(PlatformPatterns.psiElement().inside(PlatformPatterns.psiElement(PromelaTypes.ACTIVE_PRIORITY)))
                .inside(PlatformPatterns.psiElement(PromelaTypes.ACTIVE)),
            ProctypeKeywordInDefinitionProvider
        )

        extend(null, stepStart, StatementLanguageFeaturesProvider) // step-level keywords


        // type suggestions for global variables
        extend(null, modulesStart, GlobalVariableTypeCompletionProvider)
        extend(null, modulesStart, ModulesLanguageFeaturesProvider) // top-level keywords

        // suggesting appropriate keywords do/if constructs
        extend(
            null,
            PlatformPatterns.psiElement().inside(PlatformPatterns.psiElement(PromelaTypes.IF_STMNT)),
            ConditionalLanguageFeature("fi")
        )
        extend(
            null,
            PlatformPatterns.psiElement().inside(PlatformPatterns.psiElement(PromelaTypes.DO_STMNT)),
            ConditionalLanguageFeature("od")
        )
        extend(
            null,
            PlatformPatterns.psiElement().inside(PlatformPatterns.psiElement(PromelaTypes.OPTION)),
            ConditionalLanguageFeature("break")
        )

        // priority of init proctype
        extend(
            null, PlatformPatterns.psiElement().withParent(
                PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(PromelaTypes.INIT)).afterLeaf(
                    PlatformPatterns.psiElement(PromelaTypes.INIT_KW)
                )
            ), ConditionalLanguageFeature("priority", AddSpaceInsertHandler.INSTANCE)
        )

        // priority of proctype
        extend(
            null, PlatformPatterns.psiElement().withParent(
                PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(PromelaTypes.PROCTYPE))
                    .afterSibling(
                        PlatformPatterns.psiElement(PromelaTypes.IDENTIFIER)
                    )
            ), ConditionalLanguageFeature("priority", AddSpaceInsertHandler.INSTANCE)
        )
        // provided clause in proctype
        extend(
            null, PlatformPatterns.psiElement().withParent(
                PlatformPatterns.psiElement().withParent(PlatformPatterns.psiElement(PromelaTypes.PROCTYPE))
                    .afterSibling(
                        PlatformPatterns.psiElement()
                            .withElementType(TokenSet.create(PromelaTypes.IDENTIFIER, PromelaTypes.PRIORITY))
                    )
            ), ConditionalLanguageFeature("provided", ParenthesesInsertHandler.WITH_PARAMETERS)
        )

        // expression suggestions
        extend(
            null,
            expressionStart,
            ConditionalLanguageFeatures(
                ParenthesesInsertHandler.WITH_PARAMETERS,
                "len",
                "enabled",
                "pc_value",
                "get_priority",
                "set_priority"
            )
        )
        extend(null, expressionStart, ConditionalLanguageFeatures(null, "np_", "timeout"))

        // suggestions for "functions" available only in the "base expressions"
        extend(
            null,
            baseExpressionStart,
            ConditionalLanguageFeatures(
                ParenthesesInsertHandler.WITH_PARAMETERS,
                "full",
                "get_empty",
                "nfull",
                "nempty"
            )
        )

        // type completion inside channel type definition
        extend(
            null, PlatformPatterns.or(
                stepStart, insideTypedef, insideProctype, PlatformPatterns.psiElement(PromelaTypes.UNAME).afterLeaf(
                    PlatformPatterns.or(
                        PlatformPatterns.psiElement(PromelaTypes.COMMA),
                        PlatformPatterns.psiElement(PromelaTypes.OPENBRACKET)
                    )
                ).inside(PlatformPatterns.psiElement(PromelaTypes.CH_INIT_TYPES))
            ), TypeCompletionProvider()
        )

        // inline calls completion
        extend(
            null, stepStart, InlineCompletionProvider
        )
    }
}