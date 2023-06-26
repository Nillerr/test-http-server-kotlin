package io.github.nillerr.http.server.testing.internal

import io.github.nillerr.http.server.testing.StringValues
import java.nio.charset.Charset

fun parseHeader(value: String): List<Pair<String, String>> {
    return value.split(";").map { parseHeaderPart(it.trim()) }
}

fun parseHeaderPart(value: String): Pair<String, String> {
    val parts = value.split('=', limit = 2)
    if (parts.size == 2) {
        return parts[0] to parts[1]
    }
    return "" to parts[0]
}

fun getContentTypeOrNull(headers: StringValues): String? {
    return headers.get("Content-Type").singleOrNull()
}

fun getCharsetOrNull(value: String): Charset? {
    return getFirstHeaderPartOrNull(value, "charset")
        ?.let { Charset.forName(it) }
}

fun getCharsetOrNull(headers: StringValues): Charset? {
    return getContentTypeOrNull(headers)?.let { getCharsetOrNull(it) }
}

fun getCharset(headers: StringValues): Charset {
    return getCharsetOrNull(headers) ?: Charsets.UTF_8
}

fun getFirstHeaderPartOrNull(value: String, name: String): String? {
    return parseHeader(value).filter { (n, _) -> n == name }.map { (_, v) -> v }.firstOrNull()
}

fun getMediaTypeOrNull(headers: StringValues): String? {
    return getContentTypeOrNull(headers)?.let { getFirstHeaderPartOrNull(it, "") }
}
