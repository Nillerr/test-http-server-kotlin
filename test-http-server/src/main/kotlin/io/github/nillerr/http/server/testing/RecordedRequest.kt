package io.github.nillerr.http.server.testing

import io.github.nillerr.http.server.testing.internal.buildRequestString
import io.github.nillerr.http.server.testing.internal.getCharset

class RecordedRequest(
    val method: String,
    val path: String,
    val parameters: StringValues,
    val headers: StringValues,
    val body: ByteArray,
) {
    override fun toString(): String {
        val charset = getCharset(headers)
        return buildRequestString(method, path, parameters, headers, body.toString(charset))
    }
}
