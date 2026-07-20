package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.sdk

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import com.jetbrains.rd.generator.nova.GenerationSpec.Companion.nullIfEmpty
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.isExecutable

/**
 * Object for detecting Spin and information about id
 */
internal object SpinDetector {

    private const val SPIN_UNIX_PATH = "/usr/bin/spin"

    /**
     * Tries to detect Spin from PATH. Works only on Unix where which program is present.
     * @return List of possible Spin installations.
     */
    @RequiresBackgroundThread(generateAssertion = true)
    fun detectSpin(): List<String> {
        if (!SystemInfo.isUnix) {
            return emptyList()
        }
        val spins = mutableSetOf(SPIN_UNIX_PATH)
        try {
            val process = ProcessBuilder("which", "spin").start()
            process.waitFor()
            process.inputReader().readText().trim().nullIfEmpty()?.let {
                val spin = Path(it)
                if (spin.isExecutable()) {
                    spins.add(it)
                }
            }
        } catch (e: Throwable) {
            LOG.info("Spin detection failed", e)
        }
        return spins.toList()
    }

    private val LOG = Logger.getInstance(SpinDetector::class.java)
    private const val PREFIX = "Spin Version "

    private fun parseOutput(output: String): String? {
        if (output.startsWith(PREFIX)) {
            // expected format: "Spin Version 6.5.2 -- 6 December 2019"
            // we cant to detect also the date as authors changes only the date when adding bug fixes
            return "Spin " + output.removePrefix(PREFIX).trim()
        }
        return null
    }

    /**
     * Tries to detect the version of Spin
     * @param spin path to spin
     * @return Version if a path leads to Spin, null otherwise.
     * It's possible this works only for newer Spin versions.
     */
    @RequiresBackgroundThread(generateAssertion = true)
    fun detectVersion(spin: String): String? {
        val executable = File(spin)
        if (!executable.canExecute() || !executable.isFile) {
            return null
        }
        try {
            val process = ProcessBuilder(executable.path, "-V").redirectErrorStream(true).start()
            process.waitFor()
            return parseOutput(process.inputReader().readText())
        } catch (e: Throwable) {
            LOG.info("Spin $spin version detection failed", e)
        }
        return null
    }
}