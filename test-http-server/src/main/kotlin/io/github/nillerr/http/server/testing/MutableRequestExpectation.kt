package io.github.nillerr.http.server.testing

import io.github.nillerr.http.server.testing.internal.buildRequestString

class MutableRequestExpectation(
    override val method: String,
    override val path: String,
) : RequestExpectation {
    override val parameters = mutableListOf<Pair<String, String>>()
    override val headers = mutableListOf<Pair<String, String>>()

    override var body: RequestBodyExpectation = AnyRequestBodyExpectation
        private set

    override var response: suspend () -> PreparedResponse = { MutablePreparedResponse() }
        private set

    fun parameter(name: String, value: Any): MutableRequestExpectation {
        parameters.add(name to value.toString())
        return this
    }

    fun header(name: String, value: Any): MutableRequestExpectation {
        headers.add(name to value.toString())
        return this
    }

    fun body(body: RequestBodyExpectation): MutableRequestExpectation {
        this.body = body
        return this
    }

    fun respond(status: Int, block: suspend MutablePreparedResponse.() -> Unit = {}) {
        this.response = {
            val response = MutablePreparedResponse()
            response.status = status
            block(response)
            response
        }
    }

    override fun toString(): String {
        return buildRequestString(method, path, parameters, headers, body.toString())
    }
}
