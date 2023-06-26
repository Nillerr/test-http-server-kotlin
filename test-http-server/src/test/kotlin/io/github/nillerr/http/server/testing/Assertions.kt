package io.github.nillerr.http.server.testing

import kotlin.test.assertTrue

fun assertStartsWith(expected: String, actual: String) {
    assertTrue("Expected the string `$actual` to start with `$expected`") { actual.startsWith(expected) }
}
