package cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.console

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import cz.cuni.mff.gitlab.zelezno.promelaplugin.simulation.data.SimulationStep
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

/**
 * Interface for sending information from a running process to UI
 */
interface Bridge : Disposable {
    /**
     * Closes the underlying connection
     */
    fun close()

    /**
     * Publishes the simulation step into the UI, must not be called after calling [close]
     */
    fun publish(step: SimulationStep)
}

/**
 * Service for connecting UI and BE threads when running simulation.
 * Handles coroutine scooping and data buffering
 */
@Service(Service.Level.PROJECT)
class SpinBridgeService(
    private val coroutineScope: CoroutineScope,
) {
    /**
     * Creates new [Bridge] for sending simulation data. Collector must handle data access rules (locks etc.) itself.
     * Internally the [Bridge] is connected to this service's [CoroutineScope].
     * @param chunkSize the number of steps to buffer before sending them to the collector
     * @param collector the callback to receive buffered simulation steps
     * @return the created [Bridge]
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun createBridge(chunkSize: Int  = 10_000, collector: suspend (List<SimulationStep>) -> Unit): Bridge {
        return object : Bridge {
            private var closed = false
            private val flow = MutableSharedFlow<SimulationStep?>(onBufferOverflow = BufferOverflow.SUSPEND)
            private val flowContext = coroutineScope.launch(CoroutineName("simulation read bridge")) {
                flow.takeWhile { it != null } // null stops flow
                    .map { it!! }.chunked(chunkSize) // so we don't lock for every step as Spin itself is quite fast
                    .buffer(10) // if collector is slow
                    .collect {
                        collector(it)
                    }
            }

            /**
             * Forces underlying flow to flush.
             * Successive calls do nothing.
             */
            override fun close() {
                if (closed) return
                coroutineScope.launch { flow.emit(null) }
                closed = true
            }

            /**
             * Closes underlying shared underlying flow context
             */
            override fun dispose() {
                flowContext.cancel()
            }

            /**
             * This method blocks to preserve the order of publishing.
             */
            override fun publish(step: SimulationStep) {
                // we require inorder execution here
                runBlocking { coroutineScope.launch { flow.emit(step) } }
            }
        }
    }
}