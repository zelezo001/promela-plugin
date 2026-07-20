package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.sdk

import com.intellij.configurationStore.deserialize
import com.intellij.configurationStore.serialize
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectBundle
import com.intellij.openapi.projectRoots.*
import com.intellij.openapi.projectRoots.impl.SdkAdditionalDataBase
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.SpinSdkProperties
import cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.model.SpinSdkSerializer
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import org.jdom.Element
import org.jetbrains.annotations.Unmodifiable
import java.io.File

/**
 * Additional tools that are not directly related to Spin program.
 * @param CCompilerPath path to C compiler used to compile Spin-generated models. cc is used when empty.
 */
internal class SpinSdkAdditionalData(var CCompilerPath: String? = null) : SdkAdditionalDataBase() {
    override fun markInternalsAsCommited(commitStackTrace: Throwable) {}
}

/**
 * SDK containing tools needed for compiling models written in Promela
 * Home path must point to Spin binary
 */
class SpinSDK : SdkType(SpinSdkSerializer.SPIN_SDK_TYPE_ID) {

    override fun suggestHomePaths(project: Project?): @Unmodifiable Collection<String?> {
        return SpinDetector.detectSpin()
    }

    /**
     * Detects the version of Spin located at sdkHome.
     * @param sdkHome path to Spin executable
     * @return the version string, or null if detection fails
     */
    override fun getVersionString(sdkHome: String): String? {
        return SpinDetector.detectVersion(sdkHome)
    }

    /**
     * Returns the file chooser descriptor for selecting the Spin executable.
     * @return the file chooser descriptor
     */
    override fun getHomeChooserDescriptor(): FileChooserDescriptor {
        // this is copied from the parent as we want to select files and not folders
        val descriptor: FileChooserDescriptor =
            object : FileChooserDescriptor(true, false, false, false, false, false) {
                @Throws(Exception::class)
                override fun validateSelectedFiles(files: Array<out VirtualFile>) {
                    if (files.isNotEmpty()) {
                        val selectedPath = files[0].path
                        var valid = isValidSdkHome(selectedPath)
                        if (!valid) {
                            valid = isValidSdkHome(adjustSelectedSdkHome(selectedPath))
                            if (!valid) {
                                val message = getInvalidHomeMessage(selectedPath)
                                throw Exception(message)
                            }
                        }
                    }
                }
            }
        descriptor.setTitle(ProjectBundle.message("sdk.configure.home.title", getPresentableName()))
        return descriptor
    }

    @Deprecated("Deprecated in Java")
    override fun suggestHomePath(): String? {
        return if (SystemInfo.isUnix) "/usr/bin" else null
    }

    /**
     * Validates that the path points to a runnable program.
     * It is not checked if it's actually Spin.
     * @param path the path to validate
     * @return true if the path is a valid SDK home, false otherwise
     */
    override fun isValidSdkHome(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.isFile && file.canExecute()
    }

    override fun suggestSdkName(currentSdkName: String?, sdkHome: String): String {
        return PromelaBundle.message("module.sdk.name")
    }

    override fun createAdditionalDataConfigurable(
        sdkModel: SdkModel,
        sdkModificator: SdkModificator
    ): AdditionalDataConfigurable {
        return SpinSDKConfigurable()
    }

    override fun getPresentableName(): String {
        return PromelaBundle.message("module.sdk.name")
    }

    private companion object {
        const val ADDITIONAL_DATA_ELEMENT = "sdk-properties"
    }

    /**
     * Loads additional data from a given element.
     * Inverse to [saveAdditionalData]
     * @return Loaded data
     */
    override fun loadAdditionalData(additional: Element): SdkAdditionalData {
        val promelaSdkProperties = additional.getChild(ADDITIONAL_DATA_ELEMENT)
            ?.deserialize(SpinSdkProperties::class.java)
        return SpinSdkAdditionalData(promelaSdkProperties?.cCompiler)
    }

    /**
     * Stores additional data to the given element
     * @param additional target element in which data will be stored
     * @param additionalData must be instance of [SpinSdkAdditionalData]
     */
    override fun saveAdditionalData(
        additionalData: SdkAdditionalData,
        additional: Element
    ) {
        additionalData as SpinSdkAdditionalData
        val element = serialize(SpinSdkProperties(additionalData.CCompilerPath ?: "ccc"), null, true)
        element!!.setName(ADDITIONAL_DATA_ELEMENT)
        additional.addContent(element)
    }

}