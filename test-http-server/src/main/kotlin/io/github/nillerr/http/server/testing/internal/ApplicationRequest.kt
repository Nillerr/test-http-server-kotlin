package io.github.nillerr.http.server.testing.internal

import io.github.nillerr.http.server.testing.MutableStringValues
import io.github.nillerr.http.server.testing.RecordedRequest
import io.github.nillerr.http.server.testing.StringValues
import io.ktor.server.request.*
import io.ktor.util.*
import io.ktor.utils.io.core.*

internal suspend fun ApplicationRequest.toRecordedRequest(): RecordedRequest {
    return RecordedRequest(
        method = httpMethod.value,
        path = path(),
        parameters = queryParameters.toStringValues(),
        headers = headers.toStringValues(),
        body = call.receive<ByteArray>(),
    )
}

internal fun io.ktor.util.StringValues.toStringValues(): StringValues {
    val values = MutableStringValues()
    flattenForEach { name, value ->
        values.add(name, value)
    }
    return values
}
