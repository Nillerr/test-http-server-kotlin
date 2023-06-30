package io.github.nillerr.http.server.testing

import io.github.nillerr.autocleanup.junit5.AutoCleanup
import io.github.nillerr.autocleanup.junit5.AutoCleanupTest
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@AutoCleanupTest
class AutoCleanupTestHttpServerTests {
    @AutoCleanup
    val server = TestHttpServer(verificationMode = VerificationMode.MANUAL)

    @AutoCleanup
    val client = HttpClient(CIO) {
        defaultRequest {
            url(server.url.toString())
        }
    }

    @Test
    fun `server url`() {
        // Given
        server.expect("POST", "/users") {
            body("Hello Nicklas")
        }.respond(200) {
            body("Hello")
        }

        // When
        val url = server.url

        // Then
        assertStartsWith("http://localhost:", url.toString())
    }

    @Test
    fun `given expectations but no calls, then verify throws AssertionError`() {
        // Given
        server.expect("POST", "/users") {
            body("Hello Nicklas")
        }.respond(200) {
            body("Hello")
        }

        // When
        val result = runCatching { server.verify() }

        // Then
        assertIs<UnnecessaryExpectationsError>(result.exceptionOrNull())
    }

    @Test
    fun `given all expectations called, then verify succeeds`() {
        // Given
        server.expect("POST", "/users") {
            body("Hello Nicklas")
        }.respond(200) {
            body("Hello")
        }

        runBlocking {
            client.post("/users") {
                setBody("Hello Nicklas")
            }
        }

        // When
        val result = server.verify()

        // Then
        assertEquals(Unit, result)
    }

    @Test
    fun `given missing expectation, then 501 Not Implemented is returned`() {
        // When
        val response = runBlocking {
            client.post("/users") {
                setBody("Hello Nicklas")
            }
        }

        // Then
        val body = runBlocking { response.bodyAsText() }
        assertStartsWith("An expectation could not be found for the following request:", body)
        assertEquals(HttpStatusCode.NotImplemented, response.status)
    }

    @Test
    fun `given expectation, then response is returned`() {
        // Given
        server.expect("POST", "/users") {
            parameter("id", 5)
            parameter("foo", null)
            header("Authorization", "Bearer [jwt]")
            header("Accept", null)
            body("Nicklas")
        }.respond(200) {
            header("Content-Type", "text/html")
            body("Hello Nicklas")
        }

        // When
        val response = runBlocking {
            client.post("/users") {
                parameter("id", 5)
                header("Authorization", "Bearer [jwt]")
                setBody("Nicklas")
            }
        }

        // Then
        val body = runBlocking { response.bodyAsText() }
        assertEquals("Hello Nicklas", body)
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
