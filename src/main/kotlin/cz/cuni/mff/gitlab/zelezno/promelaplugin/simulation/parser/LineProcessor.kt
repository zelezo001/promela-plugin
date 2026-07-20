package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.parser

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessOutputType
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.text.containsLineBreak

/**
 * LineProcessor accepts an arbitrary text stream and passes lines from it to a handler.
 * Lines are detected by CRLF|LF|CR
 */
abstract class LineProcessor {
    val buffer = StringBuilder()

    /**
     * Always call when calls to [write] are stopped as stream could contain last line without a line-end
     */
    fun flushRemainingLine() {
        prependFromBuffer("").takeIf { it.isNotEmpty() }?.let { handle(it) }
    }

    /**
     * Writes text to the processor, which may be buffered until a full line is formed.
     * @param text the text to write
     */
    fun write(text: String) {
        if (text.containsLineBreak()) {
            val lines = text.lines()
            lines.forEachIndexed { index, line ->
                if (index + 1 < lines.size)
                // terminated line, send it to handler
                    handle(prependFromBuffer(line))
                else
                // the last line is either an empty string (due to line break at the eol or the last line without it
                    writeUnfinishedLine(line)
            }
        } else writeUnfinishedLine(text)
    }

    private fun writeUnfinishedLine(line: String) {
        if (line.isEmpty()) return
        buffer.append(line)
        bufferWritten()
    }

    /**
     * Called when something is written to the buffer
     */
    protected open fun bufferWritten() {}

    /**
     * Called when a line-end is encountered
     */
    protected abstract fun handle(line: String)

    private fun prependFromBuffer(line: String): String {
        if (buffer.isEmpty()) return line
        return buffer.append(line).toString().also { buffer.clear() }
    }

    /**
     * [ProcessListener] that passes stdout to a [LineProcessor].
     * @param processor the line processor to use
     */
    class StdoutProcessListener(private val processor: LineProcessor) : ProcessListener {
        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
            if (!ProcessOutputType.isStdout(outputType)) return
            processor.write(event.text)
        }

        override fun processTerminated(event: ProcessEvent) {
            processor.flushRemainingLine()
        }
    }
}