package io.github.nillerr.http.server.testing.jackson

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.nillerr.http.server.testing.MutablePreparedResponse
import io.github.nillerr.http.server.testing.MutableRequestExpectation
import io.github.nillerr.http.server.testing.RequestBodyExpectation
import io.github.nillerr.http.server.testing.StringValues
import io.github.nillerr.http.server.testing.internal.getCharsetOrNull
import io.github.nillerr.http.server.testing.internal.getMediaTypeOrNull
import org.intellij.lang.annotations.Language
import java.nio.charset.Charset

class JsonRequestBodyExpectation(
    @Language("json") val expected: String,
    val explicit: Boolean,
    val charset: Charset?
) : RequestBodyExpectation {
    private val expectedTree = json.readTree(expected)

    override fun matches(headers: StringValues, body: ByteArray): Boolean {
        val charset = getCharsetOrNull(headers)

        if (explicit) {
            val mediaType = getMediaTypeOrNull(headers)
            if (mediaType != "application/json") {
                return false
            }

            if (this.charset != null) {
                if (this.charset != charset) {
                    return false
                }
            }
        }

        val string = body.toString(charset ?: Charsets.UTF_8)
        val requestTree = json.readTree(string)
        return expectedTree == requestTree
    }

    override fun toString(): String {
        return buildString {
            if (explicit) {
                if (charset != null) {
                    appendLine("Content-Type: application/json; charset=${charset.name().lowercase()}")
                } else {
                    appendLine("Content-Type: application/json")
                }

                appendLine()
            }

            append(expected)
        }
    }

    companion object {
        private val json = JsonMapper()
    }
}

fun MutableRequestExpectation.json(
    @Language("json") expected: String,
    explicit: Boolean = false,
    charset: Charset? = null,
): MutableRequestExpectation {
    return body(JsonRequestBodyExpectation(expected, explicit, charset))
}

fun MutablePreparedResponse.json(
    @Language("json") body: String,
    explicit: Boolean = true,
    charset: Charset? = null,
): MutablePreparedResponse {
    if (explicit && headers.contains("Content-Type")) {
        if (charset != null) {
            header("Content-Type", "application/json; charset=${charset.name().lowercase()}")
        } else {
            header("Content-Type", "application/json")
        }
    }

    return body(body.toByteArray(charset ?: Charsets.UTF_8))
}
