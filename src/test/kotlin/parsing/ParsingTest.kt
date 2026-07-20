package parsing

import com.intellij.testFramework.ParsingTestCase
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.parser.PromelaParserDefinition

class ParsingTest :
    ParsingTestCase("", "pml", PromelaParserDefinition()) {

    override fun getTestDataPath(): String = "src/test/resources/parsing"

    fun testComplex() {
        doTest(true, true)
    }

    fun testConditionalCompilation() {
        doTest(true, true)
    }

    fun testDefine() {
        doTest(true, true)
    }

    fun testInline() {
        doTest(true, true)
    }

    fun testTypedef() {
        doTest(true, true)
    }

    fun testCCode() {
        doTest(true, true)
    }
}