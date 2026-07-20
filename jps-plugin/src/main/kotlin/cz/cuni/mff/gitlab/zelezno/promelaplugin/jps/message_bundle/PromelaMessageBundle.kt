package cz.cuni.mff.gitlab.zelezno.promelaplugin.jps.message_bundle

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.promela-jps"

/**
 * Message bundle for the Promela JPS plugin.
 */
object PromelaJPSBundle {
    private val INSTANCE = DynamicBundle(PromelaJPSBundle::class.java, BUNDLE)

    /**
     * Returns a localized message from the bundle.
     * @param key the message key
     * @param params parameters for the message
     * @return the localized message
     */
    fun message(
        key: @PropertyKey(resourceBundle = BUNDLE) String,
        vararg params: Any
    ): String {
        return INSTANCE.getMessage(key, *params)
    }
}