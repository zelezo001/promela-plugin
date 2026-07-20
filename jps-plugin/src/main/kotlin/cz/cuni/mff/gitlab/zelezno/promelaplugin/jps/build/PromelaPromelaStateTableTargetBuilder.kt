package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.message_bundle.PromelaJPSBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.JpsPromelaSdkType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.PromelaFacetJpsFacetConfigurationSerializer
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.PromelaFacetState
import org.jetbrains.jps.builders.BuildOutputConsumer
import org.jetbrains.jps.builders.DirtyFilesHolder
import org.jetbrains.jps.incremental.CompileContext
import org.jetbrains.jps.incremental.ProjectBuildException
import org.jetbrains.jps.incremental.TargetBuilder
import org.jetbrains.jps.model.module.JpsModule
import org.jetbrains.jps.util.SystemInfo
import java.io.File

/**
 * JPS target builder for generating state table models from Promela files.
 */
class PromelaPromelaStateTableTargetBuilder() : TargetBuilder<PromelaRootDescriptor, PromelaStateTableBuildTarget>(
    listOf(PromelaStateTableBuildTargetType.INSTANCE)
) {

    private fun getCompilerOptions(module: JpsModule): PromelaFacetState {
        val facet = module.container.getChild(PromelaFacetJpsFacetConfigurationSerializer.ROLE)
        return facet?.options ?: PromelaFacetState()
    }

    private fun createCompilerRunForTarget(
        target: PromelaStateTableBuildTarget, context: CompileContext
    ): SpinModelCompilationRun {
        val sdk = target.module.getSdk(JpsPromelaSdkType) ?: throw ProjectBuildException("Unsupported SDK selected")

        // load shared options
        val options = getCompilerOptions(target.module)

        return SpinModelCompilationRun(
            SpinModelCompilationRun.Compiler(sdk.homePath, options.spinOptions),
            SpinModelCompilationRun.Compiler(sdk.sdkProperties.cCompiler, options.cCompilerOptions),
            context
        )
    }

    private fun finalFileName(file: File): File {
        val base = File(file.absoluteFile.parent, "${file.absoluteFile.nameWithoutExtension}-pan")
        if (SystemInfo.isWindows) {
            return File("${base.absoluteFile}.exe")
        }
        return base
    }

    /**
     * Builds a Promela state table by compiling the specified target file.
     * Build is incremental (as configuration might have changed) and only [PromelaStateTableBuildTarget.myFile] is compiled.
     *
     * For param description see [TargetBuilder.build]
     * */
    override fun build(
        target: PromelaStateTableBuildTarget,
        holder: DirtyFilesHolder<PromelaRootDescriptor?, PromelaStateTableBuildTarget?>,
        outputConsumer: BuildOutputConsumer,
        context: CompileContext
    ) {
        val compilerRun = createCompilerRunForTarget(target, context)

        val finalFile = finalFileName(target.myFile)
        compilerRun.compilePromelaFile(file = target.myFile, finalFile = finalFile)
        outputConsumer.registerOutputFile(finalFile, listOf(target.myFile.path))
    }

    override fun getPresentableName(): String {
        return PromelaJPSBundle.message("builder.stateTableModel.name")
    }
}