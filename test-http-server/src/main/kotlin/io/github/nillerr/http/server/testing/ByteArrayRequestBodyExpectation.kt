package io.github.nillerr.http.server.testing

class ByteArrayRequestBodyExpectation(private val expectation: ByteArray) : RequestBodyExpectation {
    override fun matches(headers: List<Pair<String, String>>, body: ByteArray): Boolean {
        return expectation.contentEquals(body)
    }

    override fun toString(): String {
        return "<${expectation.size} bytes>"
    }
}

fun MutableRequestExpectation.body(bytes: ByteArray): MutableRequestExpectation {
    return body(ByteArrayRequestBodyExpectation(bytes))
}
