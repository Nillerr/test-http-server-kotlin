package io.github.nillerr.http.server.testing

open class UnnecessaryExpectationsError(val expectations: List<RequestExpectation>) : MockHttpServerAssertionError(
    buildString {
        appendLine("The mock server has unnecessary request expectations:")

        val expectationsPart = expectations.joinToString(separator)
        append(expectationsPart)
    }
) {
    companion object {
        private val separator =
            "${System.lineSeparator()}--------------------------------------------------------------------------------${System.lineSeparator()}"
    }
}
