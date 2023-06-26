package io.github.nillerr.http.server.testing

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ByteArrayRequestBodyExpectationTests {
    @Test
    fun match() {
        // Given
        val content = byteArrayOf(5, 6, 8, 123, 127)
        val expectation = ByteArrayRequestBodyExpectation(content)

        // When
        val result = expectation.matches(emptyStringValues(), content)

        // Then
        assertTrue(result)
    }

    @Test
    fun `no match`() {
        // Given
        val content = byteArrayOf(5, 6, 8, 123, 127)
        val expectation = ByteArrayRequestBodyExpectation(content)

        // When
        val result = expectation.matches(emptyStringValues(), byteArrayOf(50, 60, 80, 123, 127))

        // Then
        assertFalse(result)
    }

    @Test
    fun string() {
        // Given
        val content = byteArrayOf(5, 6, 8, 123, 127)
        val expectation = ByteArrayRequestBodyExpectation(content)

        // When
        val result = expectation.toString()

        // Then
        assertEquals("<5 bytes>", result)
    }
}
