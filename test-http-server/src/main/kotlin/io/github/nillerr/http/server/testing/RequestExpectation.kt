package io.github.nillerr.http.server.testing

interface RequestExpectation {
    val method: String
    val path: String

    val parameters: StringValues
    val headers: StringValues

    val body: RequestBodyExpectation

    val response: suspend () -> PreparedResponse
}

fun RequestExpectation.matches(request: RecordedRequest): Boolean {
    if (method != request.method) {
        return false
    }

    if (path.removeSuffix("/") != request.path.removeSuffix("/")) {
        return false
    }

    if (parameters != request.parameters) {
        return false
    }

    if (!headers.containsAll(request.headers)) {
        return false
    }

    if (!body.matches(request.headers, request.body)) {
        return false
    }

    return true
}
