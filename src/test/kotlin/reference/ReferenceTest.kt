package reference

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*


class ReferenceTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String = "src/test/resources/reference"

    fun testInlineReference() {
        myFixture.configureByFiles("inline.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PromelaInline)
        assertEquals("inlined_before", (definition as PromelaInline).name)
    }

    fun testCustomMTypeReference() {
        myFixture.configureByFiles("submtype.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PromelaMtype)
        assertEquals("before", (definition as PromelaMtype).name)
    }

    fun testMTypeReference() {
        myFixture.configureByFiles("mtype.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PromelaMtype)
        assertEquals("mtype", (definition as PromelaMtype).name)
    }

    fun testCustomTypeReference() {
        myFixture.configureByFiles("custom_type.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PromelaUtype)
        assertEquals("struct_t", (definition as PromelaUtype).name)
    }

    fun testVariableReference() {
        myFixture.configureByFiles("variable.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PsiVariable)
        assertEquals("x_yes1", (definition as PsiVariable).name)
    }

    fun testProctypeParameterReference() {
        myFixture.configureByFiles("proctype_parameters.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PsiVariable)
        assertEquals("pr_1", (definition as PsiVariable).name)
    }

    fun testInlineParameterReference() {
        myFixture.configureByFiles("inline_parameters.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PsiVariable)
        assertEquals("pr_1", (definition as PsiVariable).name)
    }

    fun testLabelReference() {
        myFixture.configureByFiles("label.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PromelaLabel)
        assertEquals("LN1", (definition as PromelaLabel).name)
    }

    fun testProctypeReference() {
        myFixture.configureByFiles("proctype.pml")
        val definition = myFixture.getReferenceAtCaretPosition()!!.resolve()

        assertTrue(definition is PromelaProctype)
        assertEquals("pc2", (definition as PromelaProctype).name)
    }
}