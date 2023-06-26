package io.github.nillerr.http.server.testing

import java.nio.charset.Charset

fun MutablePreparedResponse.body(str: String, charset: Charset = Charsets.UTF_8): MutablePreparedResponse {
    return body(str.toByteArray(charset))
}
