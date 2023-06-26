package io.github.nillerr.http.server.testing.internal

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

fun getLastHeaderOrNull(headers: List<Pair<String, String>>, name: String): String? {
    return headers.filter { (n, _) -> n == name }.map { (_, v) -> v }.lastOrNull()
}

fun getContentTypeOrNull(headers: List<Pair<String, String>>): String? {
    return getLastHeaderOrNull(headers, "Content-Type")
}

fun getCharsetOrNull(value: String): Charset? {
    return getFirstHeaderPartOrNull(value, "charset")
        ?.let { Charset.forName(it) }
}

fun getCharsetOrNull(headers: List<Pair<String, String>>): Charset? {
    return getContentTypeOrNull(headers)?.let { getCharsetOrNull(it) }
}

fun getCharset(headers: List<Pair<String, String>>): Charset {
    return getCharsetOrNull(headers) ?: Charsets.UTF_8
}

fun getFirstHeaderPartOrNull(value: String, name: String): String? {
    return parseHeader(value).filter { (n, _) -> n == name }.map { (_, v) -> v }.firstOrNull()
}

fun getMediaTypeOrNull(headers: List<Pair<String, String>>): String? {
    return getContentTypeOrNull(headers)?.let { getFirstHeaderPartOrNull(it, "") }
}
