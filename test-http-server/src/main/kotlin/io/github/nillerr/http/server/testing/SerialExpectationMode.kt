package io.github.nillerr.http.server.testing

class SerialExpectationMode : ExpectationMode {
    override fun find(expectations: List<RequestExpectation>, request: RecordedRequest): RequestExpectation {
        val expectation = expectations.firstOrNull()
        if (expectation == null) {
            throw MissingExpectationException(request)
        }

        if (!expectation.matches(request)) {
            throw MissingExpectationException(request)
        }

        return expectation
    }
}
