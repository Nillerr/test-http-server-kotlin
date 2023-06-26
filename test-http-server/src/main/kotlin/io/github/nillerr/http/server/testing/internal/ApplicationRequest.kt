package io.github.nillerr.http.server.testing.internal

import io.github.nillerr.http.server.testing.RecordedRequest
import io.ktor.server.request.*
import io.ktor.util.*
import io.ktor.utils.io.core.*

internal suspend fun ApplicationRequest.toRecordedRequest(): RecordedRequest {
    return RecordedRequest(
        method = httpMethod.value,
        path = path(),
        parameters = queryParameters.flattenEntries(),
        headers = headers.flattenEntries(),
        body = receiveChannel().readRemaining().readBytes(),
    )
}
