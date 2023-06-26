package io.github.nillerr.http.server.testing.jackson

import io.github.nillerr.http.server.testing.MutablePreparedResponse
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertSame

class MutablePreparedResponseTests {
    @Test
    fun `json returns same as receiver`() {
        // Given
        val response = MutablePreparedResponse()

        val expected = """{"name": "Nicklas"}"""
        val explicit = true
        val charset = Charsets.ISO_8859_1

        // When
        val result = response.json(expected, explicit, charset)

        // Then
        assertSame(response, result)
    }

    @Test
    fun `json sets body to encoded bytes`() {
        // Given
        val response = MutablePreparedResponse()

        val expected = """{"name": "Nicklas"}"""
        val explicit = true
        val charset = Charsets.ISO_8859_1

        // When
        val result = response.json(expected, explicit, charset)

        // Then
        assertContentEquals(expected.toByteArray(charset), result.body)
    }

    @Test
    fun `json with default arguments`() {
        // Given
        val response = MutablePreparedResponse()

        val expected = """{"name": "Nicklas"}"""

        // When
        val result = response.json(expected)

        // Then
        assertContentEquals(expected.toByteArray(Charsets.UTF_8), result.body)
    }
}
