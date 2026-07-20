package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi

import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiElement

/**
 * Class representing types supported in Promela. Beware that string is not a type (strings are only used in printf calls).
 */
sealed class PsiType {
    /**
     * Retrieves the name representation of the type displayed to the user.
     */
    abstract fun getName(): @NlsSafe String
}

/**
 * Represents a fixed-size array of type itemType.
 * @param itemType the type of items in the array
 * @param size the size of the array
 */
class ArrayType(val itemType: PsiType, val size: Long) : PsiType() {
    override fun getName() = "${itemType.getName()}[$size]"
}

/**
 * Type representing build-in scalar/chan types.
 * Use constants from companion objects.
 */
class ScalarType : PsiType {

    private val name: String

    private constructor(name: String) {
        this.name = name
    }

    override fun getName(): String = name

    companion object {
        // Build-in types
        val INTEGER_TYPE = ScalarType("int")
        val SHORT_TYPE = ScalarType("short")
        val BYTE_TYPE = ScalarType("byte")
        val BIT_TYPE = ScalarType("bit")
        val PID_TYPE = BYTE_TYPE // pid is an alias for byte
        val BOOL_TYPE = ScalarType("bool")
        val CHANNEL_TYPE = ScalarType("chan")

        // "Special" type for arguments inside inline definition
        val INLINED_EXPRESSION = ScalarType("inlined_expression")
    }
}

/**
 * Type representing "mtype" numeric constants.
 * @param name the name of the mtype
 */
class MType(private val name: String) : PsiType() {
    companion object {
        // used when subtype name is not present
        const val GENERIC_NAME = "mtype"
    }

    override fun getName(): String {
        if (name == GENERIC_NAME) return name // standard mtype
        return "mtype:$name" // subtyped mtype
    }
}

/**
 * Type representing an unsigned number with variable bit length.
 * @param bitSize the size of the unsigned number in bits
 */
class UnsignedType(val bitSize: Long) : PsiType() {
    override fun getName() = "unsigned: $bitSize"
}

/**
 * Type representing a user-defined type (typedef).
 * @param name the name of the user type
 * @param declarationElement the element where the type is declared
 */
class UserType(private val name: String?, val declarationElement: PsiElement) : PsiType() {
    override fun getName(): String {
        val name = name ?: hashCode().toString()
        return name
    }
}