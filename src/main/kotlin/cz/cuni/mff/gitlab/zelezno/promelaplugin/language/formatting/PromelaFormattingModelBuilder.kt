package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.tree.TokenSet
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaLanguage
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes

/**
 * [FormattingModelBuilder] intended for formatting Promela files
 */
class PromelaFormattingModelBuilder : FormattingModelBuilder {

    /**
     * Creates a formatting model for a formatting Promela code.
     *
     * @param formattingContext The context containing the PSI element, code style settings,
     *                          and other information required for creating the formatting model.
     * @return Formatting model for Promela code
     */
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val root: ASTNode = formattingContext.psiElement.containingFile.node
        val codestyle = formattingContext.codeStyleSettings.getCustomSettings(PromelaCodeStyleSettings::class.java)
        val containingFile = formattingContext.containingFile
        val spaceBuilder = createBuilder(formattingContext.codeStyleSettings)
        return FormattingModelProvider.createFormattingModelForPsiFile(
            containingFile, PromelaBlock(root, spaceBuilder, codestyle), formattingContext.codeStyleSettings
        )
    }

    private fun SpacingBuilder.RuleBuilder.setOpenBraceSpacing(style: Int): SpacingBuilder {
        return when (style) {
            CommonCodeStyleSettings.END_OF_LINE -> spacing(1, 1, 0, false, 0)
            CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED -> parentDependentLFSpacing(1, 1, false, 0)
            // handle all other the same way
            else -> spacing(0, 0, 1, true, 0)
        }
    }

    private fun SpacingBuilder.RuleBuilder.setClosedBraceSpacing(style: Int): SpacingBuilder {
        return when (style) {
            // handle all other options as single '}' on the next line
            CommonCodeStyleSettings.END_OF_LINE -> spacing(0, 0, 1, true, 0)
            // allow wrapping on one line
            else -> parentDependentLFSpacing(1, 1, true, 0)
        }
    }


    private fun SpacingBuilder.RuleBuilder.spaceWithoutLfIf(space: Boolean): SpacingBuilder {
        val spaces = if (space) 1 else 0
        return this.spacing(spaces, spaces, 0, false, 0)
    }

    private fun SpacingBuilder.expressionRules(codestyle: CommonCodeStyleSettings): SpacingBuilder {
        before(PromelaTokenSets.OPERATIONS).spaceWithoutLfIf(true)
        after(TokenSet.andNot(PromelaTokenSets.OPERATIONS, PromelaTokenSets.UNARY_OPERATIONS)).spaces(1)
        after(PromelaTokenSets.UNARY_OPERATIONS).spaces(0)

        return this
    }

    private fun SpacingBuilder.spacingRules(codestyle: CommonCodeStyleSettings): SpacingBuilder {
        val methods = TokenSet.create(PromelaTypes.INLINE, PromelaTypes.PROCTYPE, PromelaTypes.ENABLER)
        val methodCalls = TokenSet.create(PromelaTypes.RUN, PromelaTypes.INLINE_CALL)

        return this.before(PromelaTypes.COMMA).spaceWithoutLfIf(codestyle.SPACE_BEFORE_COMMA)
            .after(PromelaTypes.COMMA).spaceWithoutLfIf(codestyle.SPACE_AFTER_COMMA)
//            .after(PromelaTypes.SEMI).spaceIf(codestyle.SPACE_AFTER_SEMICOLON)

            .beforeInside(PromelaTypes.PARENTHESIS, methodCalls)
            .spaceWithoutLfIf(codestyle.SPACE_BEFORE_METHOD_CALL_PARENTHESES)
            .beforeInside(PromelaTypes.PARENTHESIS, methods)
            .spaceWithoutLfIf(codestyle.SPACE_BEFORE_METHOD_PARENTHESES)

            .beforeInside(PromelaTypes.PARENTHESIS, PromelaTypes.FOR_STMNT)
            .spaceWithoutLfIf(codestyle.SPACE_BEFORE_FOR_PARENTHESES)

            .afterInside(PromelaTypes.OPENBRACKET, PromelaTypes.PARENTHESIS)
            .spaceWithoutLfIf(codestyle.SPACE_WITHIN_BRACKETS)
            .beforeInside(PromelaTypes.CLOSEBRACKET, PromelaTypes.PARENTHESIS)
            .spaceWithoutLfIf(codestyle.SPACE_WITHIN_BRACKETS)
    }

    private fun SpacingBuilder.promelaBraceRules(codestyle: CommonCodeStyleSettings): SpacingBuilder {
        return this

            // utypes
            .beforeInside(PromelaTypes.OPENCURLYBRACKET, PromelaTypes.UTYPE)
            .setOpenBraceSpacing(codestyle.CLASS_BRACE_STYLE)
            .beforeInside(PromelaTypes.CLOSECURLYBRACKET, PromelaTypes.UTYPE)
            .setClosedBraceSpacing(codestyle.CLASS_BRACE_STYLE)

            .beforeInside(
                PromelaTypes.SEQUENCE_BLOCK,
                TokenSet.create(PromelaTypes.INLINE, PromelaTypes.PROCTYPE, PromelaTypes.INIT, PromelaTypes.NEVER)
            ).setOpenBraceSpacing(codestyle.METHOD_BRACE_STYLE).beforeInside(
                PromelaTypes.CLOSECURLYBRACKET, PromelaTypes.SEQUENCE_BLOCK
            ).setClosedBraceSpacing(codestyle.METHOD_BRACE_STYLE)

//            // other cases cannot separate other
            .before(PromelaTypes.OPENCURLYBRACKET).setOpenBraceSpacing(codestyle.BRACE_STYLE)
            .before(PromelaTypes.SEQUENCE_BLOCK).setOpenBraceSpacing(codestyle.BRACE_STYLE)
            .before(PromelaTypes.CLOSECURLYBRACKET).setClosedBraceSpacing(codestyle.BRACE_STYLE)

//            .between(PromelaTypes.OPENCURLYBRACKET, PromelaTokenSets.NORMAL_COMMENTS)
//            .spacing(0, 1, 0, true, Int.MAX_VALUE)
            .after(PromelaTypes.OPENCURLYBRACKET).parentDependentLFSpacing(1, 1, true, 0)
    }

    private fun createBuilder(codestyle: CodeStyleSettings): SpacingBuilder {
        val promelaCodestyle = codestyle.getCommonSettings(PromelaLanguage)
        return SpacingBuilder(codestyle, PromelaLanguage)
            .expressionRules(promelaCodestyle)

            .before(PromelaTypes.LINE_COMMENT).spacing(0, Int.MAX_VALUE, 0, true, 1)
            // keep LF after comment
            .after(PromelaTypes.LINE_COMMENT).spacing(0, 0, 1, true, 1)

            // add LF after block comment
            .after(PromelaTypes.BLOCK_COMMENT)
            .spacing(0, Int.MAX_VALUE, 1, true, 1).


                // semi always ends a line
            between(PromelaTypes.SEMI, PromelaTypes.STEP).spacing(0, 0, 1, true, 1).
                // space before semis
            before(PromelaTypes.SEMI).spacing(0, 0, 0, false, 0)

            // options in :: if/do
            .between(PromelaTypes.OPTION, PromelaTypes.OPTION).spacing(0, 0, 1, true, 1)

            // curly bracket rules
            .promelaBraceRules(promelaCodestyle)

            .spacingRules(promelaCodestyle)

            // proctypes
            .afterInside(PromelaTypes.SEMI, PromelaTypes.PROCTYPE).spacing(1, 1, 0, true, 0)
            .betweenInside(PromelaTypes.IDENTIFIER, PromelaTypes.OPENBRACKET, PromelaTypes.PROCTYPE)
            .spacing(0, 0, 0, false, 0).betweenInside(
                TokenSet.ANY, TokenSet.ANY, PromelaTypes.PROCTYPE_HEADER
            ).spacing(1, 1, 0, false, 0).betweenInside(
                TokenSet.create(
                    PromelaTypes.PRIORITY,
                    PromelaTypes.ENABLER,
                    PromelaTypes.PROCTYPE_HEADER,
                    PromelaTypes.IDENTIFIER,
                    PromelaTypes.PARENTHESIS,
                ), TokenSet.create(
                    PromelaTypes.PRIORITY,
                    PromelaTypes.ENABLER,
                    PromelaTypes.PROCTYPE_HEADER,
                    PromelaTypes.IDENTIFIER,
                    PromelaTypes.PARENTHESIS,
                ), PromelaTypes.PROCTYPE
            ).spacing(1, 1, 0, false, 0)

            // inlines
            .beforeInside(PromelaTypes.IDENTIFIER, PromelaTypes.INLINE).spacing(1, 1, 0, false, 0)
            .beforeInside(PromelaTypes.IDENTIFIER, PromelaTypes.NEVER).spacing(1, 1, 0, false, 0)

            .betweenInside(PromelaTypes.UNAME, PromelaTypes.OPENBRACKET, PromelaTypes.PROCTYPE)
            .spacing(1, 1, 0, true, 0)

            // comma
            .after(PromelaTypes.COMMA).spacing(1, 1, 0, true, 0)

            // label definition
            .after(PromelaTypes.LABEL).spacing(0, 0, 1, true, 0)

            //arrow
            .around(PromelaTypes.ARROW).spacing(1, 1, 0, true, 1)

            .aroundInside(TokenSet.ANY, PromelaTypes.MODULES).blankLines(promelaCodestyle.BLANK_LINES_AROUND_METHOD)
    }
}