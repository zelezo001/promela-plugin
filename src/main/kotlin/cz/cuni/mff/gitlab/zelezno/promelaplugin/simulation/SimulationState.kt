package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.ConsoleView
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console.SimulationConsoleView
import cz.cuni.mff.gitlab.zelezno.promelaplugin.spin.SpinCodeReferenceFilter
import java.io.File

/**
 * Represents the state of a Promela simulation run.
 * @param interactive true if the simulation is interactive
 * @param valuesTracked true if data values are tracked
 * @param diagramContainsStatements true if the diagram should contain statements
 * @param file the Promela file to simulate
 * @param spin the path to the Spin executable
 * @param verificationArguments arguments for the Spin simulation
 * @param environment the execution environment
 */
class SimulationState(
    val interactive: Boolean,
    val valuesTracked: Boolean,
    val diagramContainsStatements: Boolean,
    val file: File,
    val spin: String,
    val verificationArguments: List<String>,
    environment: ExecutionEnvironment
) : CommandLineState(environment) {

    override fun execute(executor: Executor, runner: ProgramRunner<*>): ExecutionResult {
        return super.execute(executor, runner)
    }

    override fun createConsole(executor: Executor): ConsoleView? {
        val console = super.createConsole(executor) ?: return null
        return SimulationConsoleView(
            interactive,
            valuesTracked,
            diagramContainsStatements,
            environment.project,
            console
        )
    }

    init {
        addConsoleFilters(SpinCodeReferenceFilter(project = environment.project))
    }

    override fun startProcess(): ProcessHandler {
        return OSProcessHandler(
            GeneralCommandLine(
                spin, *verificationArguments.toTypedArray(), file.absolutePath
            ).withWorkDirectory(file.parent)
        )
    }
}