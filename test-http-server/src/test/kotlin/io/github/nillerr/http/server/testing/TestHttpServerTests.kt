package io.github.nillerr.http.server.testing

import org.junit.jupiter.api.Test
import kotlin.test.assertIs

class TestHttpServerTests {
    @Test
    fun `given verification mode is automatic, then close calls verify`() {
        // Given
        val server = TestHttpServer()
        server.start()

        try {
            server.expect("POST", "/users") {
                body("Hello Nicklas")
            }.respond(200) {
                body("Hello")
            }
        } catch (e: Throwable) {
            server.close()
            throw e
        }

        // When
        val result = runCatching { server.close() }

        // Then
        assertIs<UnnecessaryExpectationsError>(result.exceptionOrNull())
    }
}
