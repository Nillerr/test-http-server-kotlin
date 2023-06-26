package io.github.nillerr.http.server.testing

import io.github.nillerr.http.server.testing.internal.getCharset
import io.ktor.http.*

class MutablePreparedResponse : PreparedResponse {
    override var status: Int = 501
    override val headers = mutableListOf<Pair<String, String>>()
    override var body: ByteArray = ByteArray(0)

    fun status(status: Int): MutablePreparedResponse {
        this.status = status
        return this
    }

    fun header(name: String, value: Any): MutablePreparedResponse {
        headers.add(name to value.toString())
        return this
    }

    fun body(body: ByteArray): MutablePreparedResponse {
        this.body = body
        return this
    }

    override fun toString(): String {
        return buildString {
            val httpStatus = HttpStatusCode.fromValue(status)
            val description = httpStatus.description
            appendLine("HTTP/1.1 $status $description")

            for ((name, value) in headers) {
                appendLine("$name: $value")
            }

            appendLine()

            val charset = getCharset(headers)
            val bodyString = body.toString(charset)
            appendLine(bodyString)
        }
    }
}
