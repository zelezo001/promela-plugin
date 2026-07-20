package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.util.IncorrectOperationException
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.PromelaFileType
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaCustomTypename
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaIdentifier
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaModules
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl.PromelaMtypenameImpl

private fun Char.isAsciiLetter(): Boolean {
    return this in 'a'..'z' ||
            this in 'A'..'Z'
}

private fun Char.isAsciiDigit(): Boolean {
    return this in '0'..'9'
}

/**
 * Factory singleton for creating elements as we cannot create them directly via constructors.
 */
object ElementFactory {

    private val LOG = Logger.getInstance(ElementFactory::class.java)

    /**
     * Validates that an identifier is a valid Promela identifier.
     * @param identifier the identifier string to validate
     * @throws IncorrectOperationException if the identifier is not valid
     */
    fun validateIdentifier(identifier: String) {
        if (identifier.isNotEmpty() &&
            !identifier[0].isAsciiDigit() && // identifier cannot start with a digit
            identifier.all { it.isAsciiDigit() || it.isAsciiLetter() || it == '_' }
        ) {
            return
        }

        throw IncorrectOperationException(
            PromelaBundle.message("language.psi.error.invalidIdentifier", identifier)
        )
    }

    /**
     * Creates a new [PromelaIdentifier] with the given name.
     * @param project the current project
     * @param name the name for the identifier
     * @return the created identifier
     * @throws IncorrectOperationException if the identifier name is not valid
     */
    fun createIdentifier(project: Project, name: String): PromelaIdentifier {
        validateIdentifier(name)

        val content = "int ${name};"
        val file = createPromelaFile(project, content)
        val modules = file?.findChildByClass(PromelaModules::class.java)
        val decl = modules?.oneDeclList?.firstOrNull()
        val ivar = decl?.ivarList?.firstOrNull()
        return ivar?.identifier ?: run {
            throw IncorrectOperationException(
                // probably a keyword
                PromelaBundle.message("language.psi.error.invalidIdentifier", name)
            )
        }
    }

    /**
     * Creates a new [PromelaMtypenameImpl] for an mtype with an optional subtype.
     * @param project the current project
     * @param subtype the subtype name
     * @return the created mtype name element
     * @throws IncorrectOperationException if the subtype name is not valid
     */
    fun createMTypename(project: Project, subtype: String): PromelaMtypenameImpl {
        if (!subtype.isEmpty()) validateIdentifier(subtype)

        val content = if (subtype.isEmpty()) "mtype ${subtype}x;" else "mtype:${subtype} x${subtype}; "
        return run {
            val file = createPromelaFile(project, content) ?: return@run null
            val modules = file.findChildByClass(PromelaModules::class.java) ?: return@run null
            val decl = modules.oneDeclList.firstOrNull() ?: return@run null
            decl.typename?.mtypename as PromelaMtypenameImpl?
        } ?: run {
            throw IncorrectOperationException(
                // probably a keyword
                PromelaBundle.message("language.psi.error.invalidIdentifier", subtype)
            )
        }
    }

    /**
     * Creates a new [PromelaCustomTypename] with the given name.
     * @param project the current project
     * @param name the name for the custom type
     * @return the created custom type name element
     * @throws IncorrectOperationException if the name is not valid
     */
    fun createTypename(project: Project, name: String): PromelaCustomTypename {
        validateIdentifier(name)

        val content = "typedef $name {int ${name}x;}; $name ${name}x;"
        return run {
            val file = createPromelaFile(project, content) ?: return@run null
            val modules = file.findChildByClass(PromelaModules::class.java) ?: return@run null
            val decl = modules.oneDeclList.firstOrNull() ?: return@run null
            decl.typename?.customTypename
        } ?: run {
            throw IncorrectOperationException(
                // probably a keyword
                PromelaBundle.message("language.psi.error.invalidIdentifier", name)
            )
        }
    }

    /**
     * Replaces an existing identifier with a new one having the specified name.
     * @param identifier the identifier to replace
     * @param name the new name
     * @return the new identifier element
     * @throws IncorrectOperationException if the name is not valid or the identifier cannot be replaced
     */
    fun replaceIdentifier(identifier: PromelaIdentifier?, name: String): PromelaIdentifier {
        if (identifier == null) throw IncorrectOperationException(PromelaBundle.message("language.psi.error.renamingMissingIdentifier"))
        if (identifier.textRange.isEmpty) throw IncorrectOperationException(PromelaBundle.message("language.psi.error.renamingMacroIdentifier"))
        return identifier.replace(createIdentifier(identifier.project, name)) as PromelaIdentifier
    }

    private fun createPromelaFile(project: Project, text: CharSequence): PromelaFile? {
        val name = "dummy.pml"
        return PsiFileFactory.getInstance(project).createFileFromText(name, PromelaFileType, text) as PromelaFile?
    }
}