package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import org.jetbrains.jps.incremental.messages.BuildMessage
import org.jetbrains.jps.incremental.messages.CompilerMessage

/**
 * Message parsed from the Spin compiler output.
 * @param path the path to the file where the message originated
 * @param line the line number in the file
 * @param message the message text
 * @param error true if the message represents an error, false for a warning
 */
data class SpinModuleCompilationOutputMessage(
    val path: String?,
    val line: Long?,
    val message: String,
    val error: Boolean = false
) {
    /**
     * Converts [SpinModuleCompilationOutputMessage] to compiler message
     * @param compilerName passed to the [CompilerMessage] as compiler name
     * @param compiledFile passed to the [CompilerMessage] as source file
     * @return Converted message that points to place of the error/warning if [path] and [line] are present
     */
    fun toCompilerMessage(compilerName: String, compiledFile: String): CompilerMessage {
        val kind = if (error) BuildMessage.Kind.ERROR else BuildMessage.Kind.WARNING
        if (this.path != null) {
            val line = this.line ?: -1L
            return CompilerMessage(
                compilerName, kind, message, path, -1L, -1L, -1L, line, -1L
            )
        }
        return CompilerMessage(compilerName, kind, this.message, compiledFile)
    }
}

/**
 * Parser of output coming from Spin when run with -a (assemble) flag
 */
object SpinModuleCompilationOutputParser {

    private val ERROR_MESSAGE = "spin: (.*):(\\d+), (Error: .*)".toRegex()
    private val SYSTEM_FILES = listOf("nofilename")

    /**
     * Parses the output of the Spin compiler.
     * @param output the raw output string from Spin
     * @return a list of parsed messages
     */
    fun parse(output: String): List<SpinModuleCompilationOutputMessage> {
        // spin errors are (mostly) line based
        return output.split("\n").map { it.replace('\t', ' ') }.filter { it.isNotEmpty() }.map { line ->
            parseSpinError(line) ?: SpinModuleCompilationOutputMessage(null, null, line, false)
        }
    }

    private fun parseSpinError(line: String): SpinModuleCompilationOutputMessage? {
        val (file, line, error) = ERROR_MESSAGE.matchEntire(line)?.destructured ?: return null
        val lineNumber = line.toLongOrNull() ?: return null
        if (file in SYSTEM_FILES) return null
        return SpinModuleCompilationOutputMessage(file, lineNumber, error, true)
    }
}
