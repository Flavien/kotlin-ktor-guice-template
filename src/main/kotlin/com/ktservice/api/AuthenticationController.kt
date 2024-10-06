package com.ktservice.api

import com.ktservice.application.ApiRouter
import com.ktservice.application.handle
import com.ktservice.services.AuthenticationService
import com.google.inject.Inject
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

class AuthenticationController @Inject constructor(
    private val authService: AuthenticationService,
): ApiRouter {

    override fun register(app: Application): Routing = app.routing {

        post("/auth") {
            call.handle<Unit, Unit> {
                authService.authenticate(call)
            }
        }
    }
}
