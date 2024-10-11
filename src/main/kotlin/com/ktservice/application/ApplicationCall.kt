@file:OptIn(ExperimentalSerializationApi::class)

package com.ktservice.application

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import java.lang.RuntimeException

class HttpException(
    val statusCode: HttpStatusCode,
    val errorCode: String? = null,
    val description: String? = null
) : RuntimeException()

suspend inline fun <reified Request: Any, reified Response: Any> ApplicationCall.handle(
    crossinline body: suspend (Request) -> Response
) {
    val request: Request = when (Request::class) {
        Unit::class -> Unit as Request
        else -> receive()
    }

    try {
        val response: Response = body(request)

        if (Response::class != Unit::class) {
            respond(response)
        }

    } catch (exception: HttpException) {
        val errorResponse = ErrorResponse(
            status = exception.statusCode.value.toInt(),
            errorCode = exception.errorCode,
            description = exception.description
        )

        response.header("Content-Type", "application/json")
        respond(exception.statusCode, errorResponse)
    }
}

@Serializable
data class ErrorResponse constructor(
    val status: Int,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val errorCode: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val description: String? = null
)
