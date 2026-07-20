package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor

import com.github.weisj.jsvg.parser.SVGLoader
import com.github.weisj.jsvg.parser.impl.MutableLoaderContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.compiler.CompilerManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Attachment
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.build.buildStateTableBuildScope
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.spin.DiagramRenderer
import cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.spin.SpinModel
import cz.cuni.mff.gitlab.zelezno.promelaplugin.message_bundle.PromelaBundle
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

/**
 * This service's main purpose is to encapsulate interaction with a compiled model
 * and converting DOT diagrams obtained from it to SVGs.
 */
@Service(Service.Level.PROJECT)
class DiagramResolvingService(
    private val project: Project,
    private val coroutineScope: CoroutineScope,
) {
    /**
     * Creates a new [DiagramResolver] for the given file.
     * @param file the Promela file
     * @param submit the callback to call when diagrams are resolved
     * @return the created [DiagramResolver]
     */
    fun createForFile(file: VirtualFile, submit: suspend (Result) -> Unit): DiagramResolver {
        return DiagramResolverImpl(coroutineScope, project, file, submit)
    }

    sealed class Result

    /**
     * [Result] signaling that somenthing (userfixable) has failed
     * @property message Error message showable to the user
     */
    data class Error(
        val message: String
    ) : Result()

    /**
     * [Result] with sucessfully obtained diagrams
     */
    data class Diagrams(
        val diagrams: List<Diagram>
    ) : Result()

    private class DiagramResolverImpl(
        coroutineScope: CoroutineScope,
        private val project: Project,
        private val file: VirtualFile,
        private val submitCallback: suspend (Result) -> Unit,
    ) : DiagramResolver {

        private val flow = MutableSharedFlow<Boolean>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

        companion object {

            private val LOG = Logger.getInstance(DiagramResolverImpl::class.java)
        }

        private suspend fun run(file: VirtualFile): Result {
            val model = SpinModel.buildForFile(file)
                ?: return Error(PromelaBundle.message("editor.AutomatonView.message.model-not-found"))

            val diagrams = withContext(Dispatchers.IO) { model.getStateDiagrams() }

            val loader = SVGLoader()
            val renderer = DiagramRenderer()

            val computed = withContext(Dispatchers.Default) {
                diagrams.mapNotNull { diagram ->
                    renderer.renderSVG(diagram).let {
                        loader.load(it.byteInputStream(), null, MutableLoaderContext())
                    }?.let { Diagram(diagram.name, it) } ?: run {
                        LOG.error("Failed converting diagram to SVG", Attachment("diagram", diagram.dotDefinition))
                        null
                    }
                }
            }

            return if (computed.isEmpty()) {
                Error(PromelaBundle.message("editor.AutomatonView.message.empty-model"))
            } else {
                Diagrams(computed)
            }
        }

        private val flowContext = coroutineScope.launch(CoroutineName("")) {
            flow.collectLatest { aborted ->
                val result = if (aborted) {
                    Error(PromelaBundle.message("editor.AutomatonView.message.build-aborted"))
                } else {
                    run(file)
                }

                submitCallback(result)
            }
        }

        /**
         * Triggers build for the connected model and then trigger internal [kotlinx.coroutines.flow.Flow] to
         * handle its parsing and propagation to [submitCallback]
         */
        override fun resolve() {
            val scope = buildStateTableBuildScope(project, file)
            CompilerManager.getInstance(project).make(scope) { aborted, _, _, _ ->
                flow.tryEmit(aborted)
            }
        }

        /**
         * Cancels internal flowContext, subsequent calls [resolve] won't be properly resolved
         */
        override fun dispose() {
            flowContext.cancel()
        }
    }
}

/**
 * Wrapping interface for (asynchronous) logic of resolving state automaton diagrams
 */
interface DiagramResolver : Disposable {

    /**
     * Triggers resolution of state automaton diagrams
     */
    fun resolve()
}