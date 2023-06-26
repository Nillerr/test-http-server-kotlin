package io.github.nillerr.http.server.testing

interface PreparedResponse {
    val status: Int
    val headers: StringValues
    val body: ByteArray
}
