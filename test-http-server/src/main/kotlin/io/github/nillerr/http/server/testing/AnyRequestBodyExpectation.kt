package io.github.nillerr.http.server.testing

object AnyRequestBodyExpectation : RequestBodyExpectation {
    override fun matches(headers: StringValues, body: ByteArray): Boolean {
        return true
    }

    override fun toString(): String {
        return "<any>"
    }
}
