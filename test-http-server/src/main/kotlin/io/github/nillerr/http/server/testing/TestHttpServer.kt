package io.github.nillerr.http.server.testing

import io.github.nillerr.http.server.testing.internal.toRecordedRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.net.ServerSocket
import java.net.URI

@Suppress("ExtractKtorModule")
class TestHttpServer(
    private val expectationMode: ExpectationMode = SerialExpectationMode(),
    private val verificationMode: VerificationMode = VerificationMode.VERIFY_ON_CLOSE,
) : AutoCloseable {
    data class ServerWrapper(private val server: EmbeddedServer<*, *>, val host: String, val port: Int) {
        fun start() {
            server.start(wait = false)
        }

        fun stop() {
            server.stop()
        }
    }

    private var server: ServerWrapper? = null

    private val expectations = mutableListOf<RequestExpectation>()

    val url: URI
        get() {
            val server = init()

            val host = server.host
            val port = server.port
            return URI("http://$host:$port")
        }

    private fun init(): ServerWrapper {
        return server ?: startInternal()
    }

    fun start() {
        startInternal()
    }

    fun stop() {
        server?.stop()
        server = null
    }

    fun reset() {
        expectations.clear()
    }

    private fun startInternal(): ServerWrapper {
        val host = "localhost"
        val port = ServerSocket(0).use { it.localPort }

        val server = embeddedServer(Netty, host = host, port = port) {
            routing {
                route("{...}") {
                    handle {
                        handleCall(call)
                    }
                }
            }
        }

        val wrapper = ServerWrapper(server, host, port)
        wrapper.start()

        this.server = wrapper
        return wrapper
    }

    private suspend fun handleCall(call: ApplicationCall) {
        val recordedRequest = call.request.toRecordedRequest()

        val expectation = try {
            expectationMode.find(expectations, recordedRequest)
        } catch (e: MissingExpectationException) {
            call.response.status(HttpStatusCode.NotImplemented)
            call.respond("An expectation could not be found for the following request:\n${e.request}")
            return
        }

        expectations.remove(expectation)

        val response = expectation.response()
        call.response.status(HttpStatusCode.fromValue(response.status))

        response.headers.entries.forEach { (name, values) ->
            values.forEach { value ->
                call.response.header(name, value)
            }
        }

        call.respond(response.body)
    }

    override fun close() {
        stop()

        if (verificationMode == VerificationMode.VERIFY_ON_CLOSE) {
            verify()
        }
    }

    fun expect(expectation: RequestExpectation) {
        expectations.add(expectation)
    }

    fun expect(method: String, path: String, builder: MutableRequestExpectation.(MutableRequestExpectation) -> Unit = {}): MutableRequestExpectation {
        val expectation = MutableRequestExpectation(method, path)
        builder(expectation, expectation)
        expect(expectation)
        return expectation
    }

    fun verify() {
        if (expectations.isNotEmpty()) {
            throw UnnecessaryExpectationsError(expectations)
        }
    }
}
