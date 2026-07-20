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
 * JPS target builder for generating verification models from Promela files.
 */
class PromelaVerificationTargetBuilder() : TargetBuilder<PromelaRootDescriptor, PromelaVerificationBuildTarget>(
    listOf(PromelaVerificationBuildTargetType.INSTANCE)
) {

    private fun getPromelaFacetState(module: JpsModule): PromelaFacetState {
        val facet = module.container.getChild(PromelaFacetJpsFacetConfigurationSerializer.ROLE)
        return facet?.options ?: PromelaFacetState()
    }

    private fun composeCompilationOptions(target: PromelaVerificationBuildTarget): List<String> {
        val options = mutableListOf<String>()
        val add = { b: Boolean, flag: String -> if (b) options.add(flag) }

        target.runConfiguration.properties.apply {
            add(!searchMode, "-DBFS")
            add(collapseCompression, "-DCOLLAPSE")
            add(safety, "-DSAFETY")
            add(!xrXsAssertions, "-DXUSAFE")
            add(nonProgressCycles, "-DNP")
            add(minimizedAutomata, "-DMA=$minimizedAutomatonSize")
            add(bitstate, "-DBITSTATE")
            add(hashCompact, "-DHC4")
            add(boundedContextSwitching, "-DBCS")
            add(iterativeSearchForShortTrail, "-DREACH")
            add(!partialOrderReduction, "-DNOREDUCE")
            add(!useNeverClaim, "-DNOCLAIM")
            add(addComplexityProfiling, "-DPEG")
            add(computeVariableRanges, "-DVAR_RANGES")
            options.add("-DMEMLIM=$physicalMemoryInMegabytes")
            options.addAll(modelCompilationOptions)

            // -w is added in iSpin, we should copy the behavior
            options.add("-w")
        }

        return options
    }

    private fun composeGenerationOptions(target: PromelaVerificationBuildTarget): MutableList<String> {
        val options = mutableListOf<String>()
        target.runConfiguration.properties.apply {
            if (addComplexityProfiling) {
                options.add("-o3")
            }
            if (!fullChannelBlocksOrLoses) {
                options.add("-m")
            }
        }
        return options
    }

    private fun createCompilerRunForTarget(
        target: PromelaVerificationBuildTarget, context: CompileContext
    ): SpinModelCompilationRun {
        target.runConfiguration
        val sdk = target.module.getSdk(JpsPromelaSdkType) ?: throw ProjectBuildException("Unsupported SDK selected")
        val promelaFacetState = getPromelaFacetState(target.module)

        val cCompilerOptions = mutableListOf<String>()
        if (target.runConfiguration.properties.inheritGlobalCompilationOptions) {
            cCompilerOptions.addAll(promelaFacetState.cCompilerOptions)
        }
        cCompilerOptions.addAll(composeCompilationOptions(target))

        val spinOptions = composeGenerationOptions(target)
        if (target.runConfiguration.properties.inheritGlobalSpinOptions) {
            spinOptions.addAll(promelaFacetState.spinOptions)
        }

        return SpinModelCompilationRun(
            spin = SpinModelCompilationRun.Compiler(sdk.homePath, spinOptions),
            SpinModelCompilationRun.Compiler(sdk.sdkProperties.cCompiler, cCompilerOptions),
            context
        )
    }

    private fun finalFileName(target: PromelaVerificationBuildTarget): File {
        val base = File(
            target.myFile.absoluteFile.parent,
            "${target.myFile.absoluteFile.nameWithoutExtension}-${
                target.runConfiguration.name.toByteArray().toHexString()
            }-pan"
        )
        if (SystemInfo.isWindows) {
            return File("${base.absolutePath}.exe")
        }
        return base
    }

    /**
     * Builds a Promela state table by compiling the specified target file.
     * Build is NOT incremental (as configuration might have changed)
     *  and only [PromelaStateTableBuildTarget.myFile] is compiled.
     *
     * For param description see [TargetBuilder.build]
     * */
    override fun build(
        target: PromelaVerificationBuildTarget,
        holder: DirtyFilesHolder<PromelaRootDescriptor?, PromelaVerificationBuildTarget?>,
        outputConsumer: BuildOutputConsumer,
        context: CompileContext
    ) {
        val finalFile = finalFileName(target)
        val compilerRun = createCompilerRunForTarget(target, context)
        compilerRun.compilePromelaFile(file = target.myFile, finalFile = finalFile)
        outputConsumer.registerOutputFile(finalFile, listOf(target.myFile.path))
    }

    override fun getPresentableName(): String {
        return PromelaJPSBundle.message("builder.verificationModel.name")
    }
}