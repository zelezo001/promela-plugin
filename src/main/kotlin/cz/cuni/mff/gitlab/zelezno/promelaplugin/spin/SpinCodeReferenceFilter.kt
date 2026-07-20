package cz.cuni.mff.gitlab.zelezno.promelaplugin.spin

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.HyperlinkInfoFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import java.io.File

/**
 * Filter for handling code references from Spin output
 */
class SpinCodeReferenceFilter(private val project: Project) : Filter {

    private companion object {
        private val REGEX = "(/.*|[A-Za-z]:\\\\.*):(\\d+).*".toRegex()
    }

    /**
     * Tries to detect code references
     * @return [Filter.Result] if code references are present or null
     */
    override fun applyFilter(
        line: String, entireLength: Int
    ): Filter.Result? {
        val match = REGEX.find(line) ?: return null
        val offset = entireLength - line.length + match.range.first
        val (rawFile, rawLine) = REGEX.find(line)?.destructured ?: return null
        val file = VfsUtil.findFileByIoFile(File(rawFile), true) ?: return null
        val line = (rawLine.toIntOrNull() ?: return null) - 1 // offset between IDE and spin

        return Filter.Result(
            offset,
            offset + rawFile.length + 1 + rawLine.length,
            HyperlinkInfoFactory.getInstance().createMultipleFilesHyperlinkInfo(listOf(file), line, project)
        )
    }
}