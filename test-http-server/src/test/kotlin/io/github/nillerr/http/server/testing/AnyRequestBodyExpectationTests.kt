package io.github.nillerr.http.server.testing

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AnyRequestBodyExpectationTests {
    @Test
    fun `matches no body`() {
        // Given
        val expectation = AnyRequestBodyExpectation

        // When
        val result = expectation.matches(emptyList(), ByteArray(325))

        // Then
        assertTrue(result)
    }

    @Test
    fun `matches long body`() {
        // Given
        val expectation = AnyRequestBodyExpectation

        // When
        val result = expectation.matches(listOf("Content-Type" to "text/plain"), "Hello, World!".toByteArray())

        // Then
        assertTrue(result)
    }

    @Test
    fun string() {
        // Given
        val expectation = AnyRequestBodyExpectation

        // When
        val result = expectation.toString()

        // Then
        assertEquals("<any>", result)
    }
}
