# Test HTTP Server for Kotlin

Provides an HTTP server for testing HTTP client implementations.

## Installation

```kotlin
dependencies {
    implementation("io.github.nillerr:test-http-server:1.0.1")
    implementation("io.github.nillerr:test-http-server-jackson:1.0.1")
}
```

## Usage

```kotlin
class UserServiceTests {
    // Mocks
    private val server = TestHttpServer()
    
    // SUT
    private val service = UserClient(server.url)
    
    @AfterEach
    fun afterEach() {
        server.close()
    }
    
    @Test
    fun test() {
        // Given
        val id = "5"
        server.expect("GET", "/users/$id") {
            header("Accept", "application/json")
        }.respond(200) {
            json("{\"name\":\"Nicklas\"}")
        }
        
        // When
        val result = service.getUser(id)
        
        // Then
        assertEquals(Unit, result)
    }
}
```
