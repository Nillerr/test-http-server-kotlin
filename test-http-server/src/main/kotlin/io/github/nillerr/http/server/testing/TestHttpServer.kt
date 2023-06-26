package io.github.nillerr.http.server.testing

import io.github.nillerr.http.server.testing.internal.toRecordedRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.ServerSocket
import java.net.URI

@Suppress("ExtractKtorModule")
class TestHttpServer(
    private val expectationMode: ExpectationMode = SerialExpectationMode(),
    private val verificationMode: VerificationMode = VerificationMode.VERIFY_ON_CLOSE,
) : AutoCloseable {
    private var server: ApplicationEngine? = null

    private val expectations = mutableListOf<RequestExpectation>()

    private val connectors: List<EngineConnectorConfig>
        get() = init().environment.connectors

    val url: URI
        get() {
            val connector = connectors.single()
            val scheme = connector.type.name.lowercase()
            val host = connector.host
            val port = connector.port
            return URI("$scheme://$host:$port")
        }

    private fun init(): ApplicationEngine {
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

    private fun startInternal(): ApplicationEngine {
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

        server.start()

        this.server = server

        return server
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
