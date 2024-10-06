package com.ktservice.application

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import java.lang.RuntimeException

class HttpException(val statusCode: HttpStatusCode) : RuntimeException()

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
        respond(exception.statusCode)
    }
}
