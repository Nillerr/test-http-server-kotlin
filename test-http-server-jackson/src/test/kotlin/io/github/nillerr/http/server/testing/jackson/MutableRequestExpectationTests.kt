package io.github.nillerr.http.server.testing.jackson

import io.github.nillerr.http.server.testing.MutableRequestExpectation
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class MutableRequestExpectationTests {
    @Test
    fun `json returns same as receiver`() {
        // Given
        val request = MutableRequestExpectation("POST", "/users")

        val expected = """{"name": "Nicklas"}"""
        val explicit = true
        val charset = Charsets.ISO_8859_1

        // When
        val result = request.json(expected, explicit, charset)

        // Then
        assertSame(request, result)
    }

    @Test
    fun `json sets body to JsonRequestBodyExpectation`() {
        // Given
        val request = MutableRequestExpectation("POST", "/users")

        val expected = """{"name": "Nicklas"}"""
        val explicit = true
        val charset = Charsets.ISO_8859_1

        // When
        val result = request.json(expected, explicit, charset)

        // Then
        val jsonBody = assertIs<JsonRequestBodyExpectation>(result.body)
        assertEquals(expected, jsonBody.expected)
        assertEquals(explicit, jsonBody.explicit)
        assertEquals(charset, jsonBody.charset)
    }

    @Test
    fun `json with default arguments`() {
        // Given
        val request = MutableRequestExpectation("POST", "/users")

        val expected = """{"name": "Nicklas"}"""

        // When
        val result = request.json(expected)

        // Then
        val jsonBody = assertIs<JsonRequestBodyExpectation>(result.body)
        assertEquals(expected, jsonBody.expected)
        assertEquals(false, jsonBody.explicit)
        assertEquals(null, jsonBody.charset)
    }
}
