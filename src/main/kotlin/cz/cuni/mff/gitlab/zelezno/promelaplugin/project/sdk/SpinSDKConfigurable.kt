package cz.cuni.mff.gitlab.zelezno.promelaplugin.project.sdk

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.util.ui.FormBuilder
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Configurable for editing [SpinSdkAdditionalData] of [SpinSDK]
 */
internal class SpinSDKConfigurable : AdditionalDataConfigurable {

    /**
     * Form component for additional data
     */
    private class Component {

        private var cCompilerPathComponent = TextFieldWithBrowseButton().also {
            it.addBrowseFolderListener(null, FileChooserDescriptorFactory.singleFile())
        }
        var cCompilerPath: String
            get() = cCompilerPathComponent.text
            set(value) {
                cCompilerPathComponent.text = value
            }

        val mainPanel: JPanel = FormBuilder.createFormBuilder().addLabeledComponent(
            PromelaBundle.message("module.sdk.options.cCompiler"),
            cCompilerPathComponent,
        ).panel
    }

    private lateinit var component: Component

    private var sdk: Sdk? = null

    override fun setSdk(sdk: Sdk?) {
        this.sdk = sdk
    }

    private fun getAdditionalData(): SpinSdkAdditionalData? {
        return sdk?.sdkAdditionalData as? SpinSdkAdditionalData
    }

    override fun createComponent(): JComponent? {
        component = Component()
        return component.mainPanel
    }

    override fun reset() {
        component.cCompilerPath = getAdditionalData()?.CCompilerPath ?: return
        component.mainPanel.repaint();
    }

    override fun isModified(): Boolean {
        val data = getAdditionalData() ?: return component.cCompilerPath.isNotEmpty()
        return data.CCompilerPath != component.cCompilerPath
    }

    override fun apply() {
        val data = SpinSdkAdditionalData(component.cCompilerPath)
        val modifier = sdk!!.sdkModificator
        modifier.sdkAdditionalData = data
        ApplicationManager.getApplication().runWriteAction(modifier::commitChanges)
    }
}
