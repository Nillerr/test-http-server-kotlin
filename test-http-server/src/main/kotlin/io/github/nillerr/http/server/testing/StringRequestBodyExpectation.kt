package io.github.nillerr.http.server.testing

import io.github.nillerr.http.server.testing.internal.getCharset

class StringRequestBodyExpectation(private val expectation: String) : RequestBodyExpectation {
    override fun matches(headers: List<Pair<String, String>>, body: ByteArray): Boolean {
        val charset = getCharset(headers)
        return expectation == body.toString(charset)
    }

    override fun toString(): String {
        return expectation
    }
}

fun MutableRequestExpectation.body(str: String): MutableRequestExpectation {
    return body(StringRequestBodyExpectation(str))
}
