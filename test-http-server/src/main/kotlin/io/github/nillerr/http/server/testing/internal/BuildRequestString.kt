package io.github.nillerr.http.server.testing.internal

import io.ktor.http.*

internal fun buildRequestString(
    method: String,
    path: String,
    parameters: List<Pair<String, String>>,
    headers: List<Pair<String, String>>,
    body: String,
): String {
    return buildString {
        val encodedPath = buildString {
            append(path.encodeURLPath())

            if (parameters.isNotEmpty()) {
                append('=')

                for ((name, value) in parameters) {
                    append(name.encodeURLParameter())
                    append('=')
                    append(value.encodeURLParameter(spaceToPlus = true))
                }
            }
        }

        appendLine("$method $encodedPath HTTP/1.1")

        for ((name, value) in headers) {
            appendLine("$name: $value")
        }

        appendLine()

        appendLine(body)
    }
}
