package io.github.nillerr.http.server.testing

interface PreparedResponse {
    val status: Int
    val headers: List<Pair<String, String>>
    val body: ByteArray
}
