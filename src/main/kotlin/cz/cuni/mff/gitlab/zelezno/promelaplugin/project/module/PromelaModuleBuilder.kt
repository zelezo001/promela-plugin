package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.module

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.ProjectJdkForModuleStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.module.GeneralModuleType
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.io.NioFiles
import com.intellij.openapi.vfs.LocalFileSystem
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.project.sdk.SpinSDK
import java.io.File
import java.io.IOException
import java.nio.file.Path

/**
 * A custom module builder for creating Promela project modules.
 * This module builder sets up a project with Promela-specific configurations.
 */
class PromelaModuleBuilder : ModuleBuilder() {

    private companion object {
        val LOG = Logger.getInstance(PromelaModuleBuilder::class.java)
    }

    init {
        name = "promela-module"
    }

    override fun getPresentableName(): String {
        return PromelaBundle.message("module.builder.name")
    }

    override fun getModuleType(): ModuleType<*>? {
        return GeneralModuleType.INSTANCE
    }

    override fun createWizardSteps(
        wizardContext: WizardContext, modulesProvider: ModulesProvider
    ): Array<out ModuleWizardStep?> {
        return arrayOf(
            ProjectJdkForModuleStep(wizardContext, SpinSDK()),
        )
    }

    /**
     * Creates src source root directory inside the content entry.

     */
    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val contentEntry = doAddContentEntry(modifiableRootModel)
        if (contentEntry != null) {
            val contentEntryPath = getContentEntryPath() ?: return
            val path = Path.of(contentEntryPath).resolve("src")
            try {
                NioFiles.createDirectories(path)
            } catch (e: IOException) {
                LOG.error(e)
                File(path.toString()).mkdirs() // maybe this will succeed...
            }
            val sourceRoot = LocalFileSystem.getInstance()
                .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(path.toString()))
            if (sourceRoot != null) {
                contentEntry.addSourceFolder(sourceRoot, false, "")
            }
        }
    }

    /**
     * Only [SpinSDK] are allowed
     */
    override fun isSuitableSdkType(sdkType: SdkTypeId): Boolean {
        return sdkType is SpinSDK
    }
}

