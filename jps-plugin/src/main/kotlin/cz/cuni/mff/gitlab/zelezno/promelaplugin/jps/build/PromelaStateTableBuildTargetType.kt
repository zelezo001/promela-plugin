package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import com.intellij.openapi.util.io.FileFilters
import com.intellij.platform.workspace.jps.entities.SourceRootTypeId
import org.jetbrains.jps.builders.BuildTargetLoader
import org.jetbrains.jps.builders.BuildTargetType
import org.jetbrains.jps.model.JpsModel
import org.jetbrains.jps.model.module.JpsModule
import java.io.File
import java.io.FileFilter

/**
 * Type of build target for state table generation.
 */
class PromelaStateTableBuildTargetType private constructor() :
    BuildTargetType<PromelaStateTableBuildTarget>("promela-state-table", true) {

    companion object {
        val INSTANCE = PromelaStateTableBuildTargetType()
        private const val EXTENSION = "pml"
        private val FILTER: FileFilter = FileFilters.withExtension(EXTENSION)
    }

    private fun collectTargets(targets: MutableList<PromelaStateTableBuildTarget?>, module: JpsModule, rootDir: File) {
        if (!rootDir.isDirectory) return
        rootDir.walkTopDown().filter { FILTER.accept(it) }.forEach {
            targets.add(PromelaStateTableBuildTarget(it, module))
        }
    }

    /**
     * Gathers all Promela files from the all source roots in the modules inside the project
     * @param model Target model
     */
    override fun computeAllTargets(model: JpsModel): List<PromelaStateTableBuildTarget?> {
        val list = mutableListOf<PromelaStateTableBuildTarget?>()
        model.project.modules.forEach {
                module ->
            module.sourceRoots.forEach { root ->
                collectTargets(list, module, root.file)
            }
        }
        return list
    }

    /**
     * @return Simple loader that always tries to find searched target by looking among all known targets in the model
     */
    override fun createLoader(model: JpsModel): BuildTargetLoader<PromelaStateTableBuildTarget?> {
        val all = computeAllTargets(model)
        return object : BuildTargetLoader<PromelaStateTableBuildTarget?>() {
            override fun createTarget(targetId: String): PromelaStateTableBuildTarget? {
                return all.firstOrNull({ it?.id == targetId })
            }
        }
    }
}