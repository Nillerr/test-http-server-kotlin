package io.github.nillerr.http.server.testing

interface ExpectationMode {
    fun find(expectations: List<RequestExpectation>, request: RecordedRequest): RequestExpectation
}
