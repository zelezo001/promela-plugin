package cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.promela"

/**
 * Message bundle for the Promela plugin.
 */
object PromelaBundle {
    private val INSTANCE = DynamicBundle(PromelaBundle::class.java, BUNDLE)

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

//    fun lazyMessage(
//        @PropertyKey(resourceBundle = BUNDLE) key: String,
//        vararg params: Any
//    ): Supplier<@Nls String> {
//        return INSTANCE.getLazyMessage(key, *params)
//    }
}