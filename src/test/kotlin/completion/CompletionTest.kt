package completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase


class CompletionTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/resources/completion"

    fun testInlineCompletion() {
        myFixture.configureByFiles("inline.pml")
        myFixture.type("inlined")
        myFixture.complete(CompletionType.BASIC)
        val elements = myFixture.lookupElementStrings

        assertSameElements(elements!!, "inlined_before", "inlined_before2")
    }

    fun testCustomMTypeCompletion() {
        myFixture.configureByFiles("mtype.pml")
        myFixture.complete(CompletionType.BASIC)

        assertSameElements(myFixture.lookupElementStrings!!, "before", "after")
    }

    fun testCustomTypeCompletion() {
        myFixture.configureByFiles("custom_type.pml")
        myFixture.type("struct_")
        myFixture.complete(CompletionType.BASIC)

        assertSameElements(myFixture.lookupElementStrings!!, "struct_t", "struct2_t")
    }

    fun testVariableCompletion() {
        myFixture.configureByFiles("variable.pml")
        myFixture.type("x_")
        myFixture.complete(CompletionType.BASIC)

        assertSameElements(myFixture.lookupElementStrings!!, "x_yes1", "x_yes2", "x_yes3")
    }


    fun testNoVariableCompletion() {
        myFixture.configureByFiles("variable.pml")
        myFixture.type("x_no")
        myFixture.complete(CompletionType.BASIC)

        assertEmpty(myFixture.lookupElementStrings!!)
    }

    fun testProctypeParameterCompletion() {
        myFixture.configureByFiles("proctype_parameters.pml")
        myFixture.type("pr_")
        myFixture.complete(CompletionType.BASIC)

        assertSameElements(myFixture.lookupElementStrings!!, "pr_1", "pr_2")
    }


    fun testInlineParameterCompletion() {
        myFixture.configureByFiles("inline_parameters.pml")
        myFixture.type("pr_")
        myFixture.complete(CompletionType.BASIC)

        assertSameElements(myFixture.lookupElementStrings!!, "pr_1", "pr_2")
    }

    fun testLabelCompletion() {
        myFixture.configureByFiles("label.pml")
        myFixture.complete(CompletionType.BASIC)

        assertSameElements(myFixture.lookupElementStrings!!, "LN1", "L1", "L3")
    }

    fun testProctypeCompletion() {
        myFixture.configureByFiles("proctype.pml")
        myFixture.complete(CompletionType.BASIC)

        assertSameElements(myFixture.lookupElementStrings!!, "pc1", "pc2")
    }
}