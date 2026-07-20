package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.components

import com.intellij.icons.AllIcons
import com.intellij.ui.JBColor
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBLabel
import org.jetbrains.annotations.Nls
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.ScrollPane
import javax.swing.JPanel
import javax.swing.JScrollPane


/**
 * Model for a message displayed in a [MessagePanel].
 */
interface MessageModel {
    /**
     * The message text.
     */
    @get:Nls
    val message: String

    /**
     * True if the message represents an error.
     */
    val isError: Boolean

    /**
     * Sets the message and error status.
     * @param newMessage the new message text
     * @param error true if the message is an error
     */
    fun setMessage(newMessage: String, error: Boolean)
}

/**
 * Panel for displaying error/info messages
 */
class MessagePanel : JPanel(GridLayout()) {

    val model: MessageModel = object : MessageModel {
        override var message: String = ""
            private set
        override var isError: Boolean = false
            private set

        override fun setMessage(newMessage: String, error: Boolean) {
            message = newMessage
            isError = error
            update()
        }
    }

    private val messageLabel = JBLabel()
    private val scrollPane: JScrollPane

    init {
        val panel = JPanel(GridBagLayout())
        panel.add(messageLabel, GridBagConstraints())

        scrollPane = ScrollPaneFactory.createScrollPane(panel)
        add(
            scrollPane,
            BorderLayout.CENTER
        )
    }

    private fun update() {
        messageLabel.text = model.message
        messageLabel.icon = if (model.isError) AllIcons.General.Error else AllIcons.General.Information

        // reset scroll after text change
        val view = scrollPane.viewport.view
        scrollPane.viewport = null
        scrollPane.setViewportView(view)
    }
}