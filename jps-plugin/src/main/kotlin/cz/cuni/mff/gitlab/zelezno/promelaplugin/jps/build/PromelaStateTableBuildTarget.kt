package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import com.intellij.openapi.util.io.FileUtilRt
import org.jetbrains.annotations.Unmodifiable
import org.jetbrains.jps.builders.BuildRootIndex
import org.jetbrains.jps.builders.BuildTarget
import org.jetbrains.jps.builders.BuildTargetRegistry
import org.jetbrains.jps.builders.TargetOutputIndex
import org.jetbrains.jps.builders.storage.BuildDataPaths
import org.jetbrains.jps.indices.IgnoredFileIndex
import org.jetbrains.jps.indices.ModuleExcludeIndex
import org.jetbrains.jps.model.JpsModel
import org.jetbrains.jps.model.module.JpsModule
import java.io.File

/**
 * Build target for building a plain model (without any special flags) intended for getting state tables/state automatons.
 * @param myFile the Promela file to build
 * @param module the JPS module
 */
class PromelaStateTableBuildTarget(val myFile: File, val module: JpsModule) : BuildTarget<PromelaRootDescriptor?>(
    PromelaStateTableBuildTargetType.INSTANCE
) {
    override fun getId(): String = "promela://" + myFile.path

    override fun computeDependencies(
        targetRegistry: BuildTargetRegistry, outputIndex: TargetOutputIndex
    ): Collection<BuildTarget<*>?> {
        return emptyList()
    }

    // implement quality otherwise incremental builds do not work

    override fun hashCode(): Int {
        var result = myFile.hashCode()
        result = 31 * result + module.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PromelaStateTableBuildTarget

        if (!FileUtilRt.filesEqual(myFile, other.myFile)) return false
        if (module != other.module) return false

        return true
    }

    /**
     * @return Always return the held file (or null if that file is ignored)
     */
    override fun computeRootDescriptors(
        model: JpsModel, index: ModuleExcludeIndex, ignoredFileIndex: IgnoredFileIndex, dataPaths: BuildDataPaths
    ): @Unmodifiable List<PromelaRootDescriptor?> {
        if (!ignoredFileIndex.isIgnored(myFile.path)) {
            return listOf(PromelaRootDescriptor(myFile, this))
        }
        return emptyList()
    }

    override fun findRootDescriptor(
        rootId: String, rootIndex: BuildRootIndex
    ): PromelaRootDescriptor? {
        for (descriptor in rootIndex.getTargetRoots<PromelaRootDescriptor?>(this, null)) {
            if (descriptor!!.getRootId() == rootId) {
                return descriptor
            }
        }

        return null
    }

    override fun getPresentableName(): String = "Promela compilation of " + myFile.path
}