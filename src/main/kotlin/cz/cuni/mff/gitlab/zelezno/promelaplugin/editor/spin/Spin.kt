package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.spin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.io.awaitExit
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.spin.DiagramParser.InvalidFormatException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFileType
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Reader
import java.io.StringReader

/**
 * Represents a diagram in DOT format.
 * @param name the name of the diagram
 * @param dotDefinition the DOT definition string
 */
data class DotDiagram(val name: String, val dotDefinition: String)

/**
 * Parser of dot digrams output by a compiled Promela model
 */
private class DiagramParser(
    private val input: Reader
) {
    private val diagrams: MutableList<DotDiagram> = mutableListOf()
    private val buffer = StringBuilder()

    class InvalidFormatException(reason: String) :
        Exception("Invalid format encountered while parsing diagram: $reason")


    /**
     * @throws InvalidFormatException
     */
    fun parse(): List<DotDiagram> {
        while (true) {
            /*
            Parse dot defined diagram in format
            digraph name {
                body content
            }
             */
            val digraph = parseWord()
            if (digraph == "") return diagrams
            if (digraph != "digraph") throw InvalidFormatException("not starting with digraph")

            val name = parseWord()
            if (name == "") throw InvalidFormatException("missing name")
            val body = parseBody()
            diagrams.add(DotDiagram(name, "$digraph $name $body"))
        }
    }

    private fun readNext(): Int {
        while (true) {
            val next = input.read()
            if (next != '\n'.code) return next
        }
    }

    private fun parseWord(): String {
        buffer.clear()
        var c = readNext()
        while (c != -1 && c != ' '.code) {
            buffer.append(c.toChar())
            c = readNext()
        }

        return buffer.toString()
    }

    private fun parseBody(): String {
        buffer.clear()
        var c = readNext()
        if (c != '{'.code) {
            // invalid format
            throw InvalidFormatException("body starts with '${c.toChar()}'")
        }
        buffer.append(c.toChar())
        var r = 1
        while (r != 0 && c != -1) {
            c = readNext()
            buffer.append(c.toChar())
            if (c == '}'.code) r--
            else if (c == '{'.code) r++
        }
        if (r != 0) {
            throw InvalidFormatException("body not closed")
        }

        return buffer.toString()
    }
}

/**
 * Renders DOT diagrams to SVG.
 */
class DiagramRenderer {
    /**
     * Renders a DOT diagram to an SVG string.
     * @param diagram the DOT diagram to render
     * @return the SVG string
     */
    public suspend fun renderSVG(diagram: DotDiagram): String {
        return withContext(Dispatchers.IO) {
            Graphviz.fromString(diagram.dotDefinition).render(
                Format.SVG_STANDALONE
            ).toString()
        }
    }
}


/**
 * Represents a compiled Spin model.
 * @param modelPath the path to the compiled model executable
 */
class SpinModel(private val modelPath: File) {

    /**
     * Returns state diagrams in DOT format.
     * @return a list of DOT diagrams
     */
    public suspend fun getStateDiagrams(): List<DotDiagram> {
        val out: String
        val status = withContext(Dispatchers.IO) {
            val execution = GeneralCommandLine(
                modelPath.absolutePath, "-D"
            ).withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE).createProcess()
            out = execution.inputReader().readText()
            val status = execution.awaitExit()
            status
        }

        if (status == 0) {
            return withContext(Dispatchers.Default) {
                try {
                    DiagramParser(StringReader(out)).parse()
                } catch (e: InvalidFormatException) {
                    LOG.error(e)
                    emptyList()
                }
            }
        }
        return emptyList()
    }

    companion object {

        private val LOG = Logger.getInstance(SpinModel::class.java)

        /**
         * Builds a [SpinModel] for the given file if possible.
         * @param file the Promela file
         * @return the Spin model, or null if it cannot be built
         */
        fun buildForFile(file: VirtualFile): SpinModel? {
            if (file.fileType != PromelaFileType) return null
            if (!file.isInLocalFileSystem) return null
            val path = file.toNioPath()
            val modelFile = resolveModelFile(path.toFile())
            return modelFile?.let { SpinModel(it) }
        }

        private fun resolveModelFile(file: File): File? {
            val base = File(file.absoluteFile.parent, "${file.absoluteFile.nameWithoutExtension}-pan")
            val panFile: File = if (SystemInfo.isWindows) {
                File("${base.absolutePath}.exe")
            } else {
                base
            }

            return if (panFile.exists() && panFile.isFile && panFile.canExecute()) panFile else null
        }
    }
}