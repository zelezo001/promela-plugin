package cz.cuni.mff.gitlab.zelezno.promelaplugin.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.FileViewProvider
import cz.cuni.mff.gitlab.zelezno.promelaplugin.icons.PromelaIcons
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import javax.swing.Icon

/**
 * The [Language] definition for Promela.
 */
object PromelaLanguage : Language("PROMELA")

/**
 * The [LanguageFileType] for Promela files.
 */
object PromelaFileType : LanguageFileType(
    PromelaLanguage,
) {
    override fun getName(): String = PromelaBundle.message("language.fileType.name")
    override fun getDescription(): String = PromelaBundle.message("language.fileType.description")
    override fun getDefaultExtension(): String = "pml"
    override fun getIcon(): Icon = PromelaIcons.PromelaFile
}

/**
 * The [PsiFileBase] implementation for Promela files.
 * @param viewProvider the file view provider
 */
class PromelaFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PromelaLanguage) {
    override fun getFileType(): FileType = PromelaFileType
    override fun toString(): String = PromelaBundle.message("language.file.name")
}