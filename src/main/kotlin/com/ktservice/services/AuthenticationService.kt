package com.ktservice.services

import com.google.inject.Inject
import com.ktservice.application.HttpException
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

class AuthenticationService @Inject constructor() {
    suspend fun authenticate(call: ApplicationCall) {
        val authenticationDetails: AuthenticationDetails = call.receive()
        call.response.cookies.append(
            Cookie(
                name = "auth",
                value = authenticationDetails.username
            )
        )
        call.respond(
            AuthenticationDetails(
                username = authenticationDetails.username,
                password = "*".repeat(authenticationDetails.password.length)
            )
        )
    }

    fun getAuthToken(call: ApplicationCall): AuthToken {
        val cookie: String? = call.request.cookies["auth"]

        if (cookie == null) {
            throw HttpException(HttpStatusCode.Unauthorized)
        } else {
            return AuthToken(cookie, "user")
        }
    }
}

@Serializable
data class AuthenticationDetails(val username: String, val password: String)

@Serializable
data class AuthToken(val username: String, val role: String)
