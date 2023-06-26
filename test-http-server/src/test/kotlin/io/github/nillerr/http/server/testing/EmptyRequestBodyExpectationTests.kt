package io.github.nillerr.http.server.testing

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmptyRequestBodyExpectationTests {
    @Test
    fun matches() {
        // Given
        val expectation = EmptyRequestBodyExpectation

        // When
        val result = expectation.matches(emptyStringValues(), ByteArray(0))

        // Then
        assertTrue(result)
    }

    @Test
    fun string() {
        // Given
        val expectation = EmptyRequestBodyExpectation

        // When
        val result = expectation.toString()

        // Then
        assertEquals("<empty>", result)
    }
}
