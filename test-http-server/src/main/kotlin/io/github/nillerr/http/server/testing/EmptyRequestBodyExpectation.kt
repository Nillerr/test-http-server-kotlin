package io.github.nillerr.http.server.testing

object EmptyRequestBodyExpectation : RequestBodyExpectation {
    override fun matches(headers: StringValues, body: ByteArray): Boolean {
        return body.isEmpty()
    }

    override fun toString(): String {
        return "<empty>"
    }
}
