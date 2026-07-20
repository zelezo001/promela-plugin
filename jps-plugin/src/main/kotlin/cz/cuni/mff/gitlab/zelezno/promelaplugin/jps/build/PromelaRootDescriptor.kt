package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.build

import com.intellij.openapi.util.io.FileUtilRt
import org.jetbrains.jps.builders.BuildRootDescriptor
import org.jetbrains.jps.builders.BuildTarget
import java.io.File
import java.io.FileFilter

/**
 * RootDescriptor for Promela files. Contains a file instead of a directory, as we always target a single file.
 * @param file the Promela file
 * @param myTarget the build target
 */
class PromelaRootDescriptor(private val file: File, private val myTarget: BuildTarget<*>) : BuildRootDescriptor() {
    override fun getRootId(): String = rootFile.path

    override fun getRootFile(): File {
        return file
    }

    override fun createFileFilter(): FileFilter {
        return FileFilter { FileUtilRt.filesEqual(file, it) }
    }

    override fun getTarget(): BuildTarget<*> {
        return myTarget
    }
}