package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import com.intellij.openapi.util.io.FileUtilRt
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.message_bundle.PromelaJPSBundle
import org.jetbrains.jps.incremental.CompileContext
import org.jetbrains.jps.incremental.ProjectBuildException
import org.jetbrains.jps.incremental.messages.BuildMessage
import org.jetbrains.jps.incremental.messages.CompilerMessage
import java.io.File
import java.io.IOException

/**
 * Takes care of compiling a Promela model by generating C code with spin and running C compiler on it.
 */
class SpinModelCompilationRun(
    private val spin: Compiler,
    private val cCompiler: Compiler,
    private val context: CompileContext,
) {
    /**
     * Settings for the compiler
     * @param path path to the executable
     * @param arguments default arguments for the compiler
     */
    data class Compiler(val path: String, val arguments: List<String>)

    /**
     * Starts the run of the compiler process
     * @return Compiler process or null if start failed (message is already reported)
     * @param name compiler name sent to IDE
     * @param workingDir work directory of started process
     * @param compiler compiler to run
     * @param additionalArguments arguments passed to compiler
     */
    private fun runCompilation(
        name: String, workingDir: File, compiler: Compiler, vararg additionalArguments: String
    ): Process? {
        context.processMessage(
            CompilerMessage(
                name,
                BuildMessage.Kind.INFO,
                "${compiler.path} ${compiler.arguments.joinToString(" ")} ${additionalArguments.joinToString(" ")}"
            )
        )
        try {
            return ProcessBuilder(compiler.path, *compiler.arguments.toTypedArray(), *additionalArguments).directory(
                workingDir
            ).redirectErrorStream(true) // spin does not really use error stream and instead prints everything to
                .start()
        } catch (ex: IOException) {
            context.processMessage(
                CompilerMessage(
                    name,
                    BuildMessage.Kind.ERROR,
                    PromelaJPSBundle.message("build.error.compiler.io", name, ex.message ?: ex.javaClass.name)
                )
            )
            return null
        }
    }

    companion object {
        private const val SPIN_ASSEMBLE_FLAG = "-a"
        private const val C_CODE_TARGET = "pan.c"
        private const val C_COMPILER_OUTPUT_PARAMETER = "-o"
        private const val STATUS_OK = 0
    }

    private fun generateCCode(tempFolder: File, file: File) {
        // we will generate C code with spin using -a flag
        val spin = runCompilation(
            PromelaJPSBundle.message("build.spin"),
            tempFolder, spin, SPIN_ASSEMBLE_FLAG, file.absolutePath
        ) ?: return
        val status = spin.waitFor()
        val output = spin.inputReader().readText()
        // relay any output from spin to the user
        SpinModuleCompilationOutputParser.parse(output)
            .forEach { message ->
                context.processMessage(
                    message.toCompilerMessage(
                        PromelaJPSBundle.message("build.spin"), file.absolutePath
                    )
                )
            }
        if (status != STATUS_OK) {
            throw ProjectBuildException(PromelaJPSBundle.message("build.error.spinFailed").format(status))
        }
    }

    private fun compileCCode(tempFolder: File, file: File, finalFile: File) {
        val cCompiler = runCompilation(
            PromelaJPSBundle.message("build.c-compiler"),
            tempFolder,
            cCompiler,
            C_CODE_TARGET,
            C_COMPILER_OUTPUT_PARAMETER,
            finalFile.absolutePath
        ) ?: return
        val status = cCompiler.waitFor()
        val output = cCompiler.inputReader().readText()

        if (output != "") {
            // c compiler printed out something, as our goal is not to understand/support various C compilers
            // so we will just relay whole compiler output

            // if compilation didn't fail but produced some output (e.g., some warnings), we will relay
            // output as warning so build is not seen as failed
            val kind = if (status == STATUS_OK) BuildMessage.Kind.WARNING else BuildMessage.Kind.ERROR
            context.processMessage(
                CompilerMessage(

                    PromelaJPSBundle.message("build.c-compiler"), kind, output, file.absolutePath
                )
            )
        }
        if (status != STATUS_OK) {
            if (output == "") {
                context.processMessage(
                    CompilerMessage(
                        PromelaJPSBundle.message("build.c-compiler"),
                        BuildMessage.Kind.ERROR,
                        PromelaJPSBundle.message("build.error.cCompilerUnknownError"),
                        file.absolutePath
                    )
                )
            }
            throw ProjectBuildException(PromelaJPSBundle.message("build.error.cCompilerFailed").format(status))
        }
    }

    /**
     * Compiles a Promela file by first generating C code with Spin and then compiling it with a C compiler.
     * @param file the Promela file to compile
     * @param finalFile the path where the resulting executable should be placed
     * @throws ProjectBuildException if the Spin or C compiler fails
     */
    fun compilePromelaFile(file: File, finalFile: File) {
        // we want to run spin in isolated folder as it always generate files named pan.*
        val tempFolder = FileUtilRt.createTempDirectory("spin-compile-", null, true)
        try {
            generateCCode(tempFolder, file)
            compileCCode(tempFolder, file, finalFile)
        } finally {
            tempFolder.delete()
        }
    }
}