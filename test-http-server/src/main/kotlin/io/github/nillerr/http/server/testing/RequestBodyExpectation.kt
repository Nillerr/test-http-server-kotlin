package io.github.nillerr.http.server.testing

interface RequestBodyExpectation {
    fun matches(headers: List<Pair<String, String>>, body: ByteArray): Boolean
}
