package formatting

import com.intellij.psi.formatter.FormatterTestCase


class FormattingTest :
    FormatterTestCase() {

    override fun getTestDataPath(): String = "src/test/resources"
    override fun getBasePath(): String {
        return "formatting"
    }

    override fun getFileExtension() = "pml"

    fun testInline() {
        doTest()
    }

    fun testProctype() {
        doTest()
    }

    fun testNever() {
        doTest()
    }

    fun testNested() {
        doTest()
    }

    fun testExpression() {
        doTest()
    }

    fun testComplex() {
        doTest()
    }
}