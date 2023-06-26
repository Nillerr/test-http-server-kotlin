package io.github.nillerr.http.server.testing.jackson

import io.github.nillerr.http.server.testing.stringValuesOf
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonRequestBodyExpectationTests {
    @Test
    fun `explicit, charset, match`() {
        // Given
        val expectedRequestBody = """{"name": "Nicklas"}"""
        val charset = Charsets.ISO_8859_1
        val expectation = JsonRequestBodyExpectation(expectedRequestBody, true, charset)

        val headers = stringValuesOf("Content-Type" to "application/json; charset=iso-8859-1")

        // When
        val result = expectation.matches(headers, expectedRequestBody.toByteArray(charset))

        // Then
        assertTrue(result)
    }

    @Test
    fun `explicit, charset, no match due to incorrect charset`() {
        // Given
        val expectedRequestBody = """{"name": "Nicklas"}"""
        val charset = Charsets.ISO_8859_1
        val expectation = JsonRequestBodyExpectation(expectedRequestBody, true, charset)

        val headers = stringValuesOf("Content-Type" to "application/json; charset=utf-8")

        // When
        val result = expectation.matches(headers, expectedRequestBody.toByteArray(charset))

        // Then
        assertFalse(result)
    }

    @Test
    fun `explicit, charset, no match due to incorrect media type`() {
        // Given
        val expectedRequestBody = """{"name": "Nicklas"}"""
        val charset = Charsets.ISO_8859_1
        val expectation = JsonRequestBodyExpectation(expectedRequestBody, true, charset)

        val headers = stringValuesOf("Content-Type" to "application/xml; charset=iso-8859-1")

        // When
        val result = expectation.matches(headers, expectedRequestBody.toByteArray(charset))

        // Then
        assertFalse(result)
    }

    @Test
    fun `explicit, no match when different charsets`() {
        // Given
        val expectedRequestBody = """{"name": "Nicklas"}"""
        val charset = Charsets.ISO_8859_1
        val expectation = JsonRequestBodyExpectation(expectedRequestBody, true, charset)

        val headers = stringValuesOf("Content-Type" to "application/xml; charset=utf-8")

        // When
        val result = expectation.matches(headers, expectedRequestBody.toByteArray(charset))

        // Then
        assertFalse(result)
    }

    @Test
    fun `toString returns expected`() {
        // Given
        val expected = """{"name": "Nicklas"}"""
        val expectation = JsonRequestBodyExpectation(expected, explicit = false, charset = null)

        // When
        val result = expectation.toString()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `explicit, charset, toString returns expected`() {
        // Given
        val expected = """{"name": "Nicklas"}"""
        val expectation = JsonRequestBodyExpectation(expected, explicit = true, charset = Charsets.ISO_8859_1)

        // When
        val result = expectation.toString()

        // Then
        assertEquals("Content-Type: application/json; charset=iso-8859-1\n\n$expected", result)
    }

    @Test
    fun `explicit, toString returns expected`() {
        // Given
        val expected = """{"name": "Nicklas"}"""
        val expectation = JsonRequestBodyExpectation(expected, explicit = true, charset = null)

        // When
        val result = expectation.toString()

        // Then
        assertEquals("Content-Type: application/json\n\n$expected", result)
    }
}
