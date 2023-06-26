package io.github.nillerr.http.server.testing.internal

import io.github.nillerr.http.server.testing.StringValues
import io.ktor.http.encodeURLParameter
import io.ktor.http.encodeURLPath

internal fun buildRequestString(
    method: String,
    path: String,
    parameters: StringValues,
    headers: StringValues,
    body: String,
): String {
    return buildString {
        val encodedPath = buildString {
            append(path.encodeURLPath())

            if (parameters.entries.isNotEmpty()) {
                append('?')

                val queryString = buildString {
                    for ((name, values) in parameters.entries) {
                        for (value in values) {
                            append(name.encodeURLParameter())
                            append('=')
                            append(value.encodeURLParameter(spaceToPlus = true))
                            append('&')
                        }
                    }
                }.dropLast(1)

                append(queryString)
            }
        }

        appendLine("$method $encodedPath HTTP/1.1")

        for ((name, values) in headers.entries) {
            appendLine("$name: ${values.joinToString(",")}")
        }

        appendLine()

        appendLine(body)
    }
}
