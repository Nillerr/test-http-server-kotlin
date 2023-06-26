package io.github.nillerr.http.server.testing

interface RequestBodyExpectation {
    fun matches(headers: StringValues, body: ByteArray): Boolean
}
