package io.github.nillerr.http.server.testing

import io.github.nillerr.http.server.testing.internal.buildRequestString
import io.github.nillerr.http.server.testing.internal.getCharset

class RecordedRequest(
    val method: String,
    val path: String,
    val parameters: List<Pair<String, String>>,
    val headers: List<Pair<String, String>>,
    val body: ByteArray,
) {
    override fun toString(): String {
        val charset = getCharset(headers)
        return buildRequestString(method, path, parameters, headers, body.toString(charset))
    }
}
