package com.ktservice.api

import com.google.inject.Inject
import com.ktservice.application.ApiController
import com.ktservice.application.ApiRouter
import com.ktservice.application.handle
import com.ktservice.filters.AuthenticationFilter
import io.ktor.server.application.Application
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

@ApiController
class AuthenticationController @Inject constructor(
    private val authFilter: AuthenticationFilter,
): ApiRouter {

    override fun register(app: Application): Routing = app.routing {

        post("/auth") {
            call.handle<Unit, Unit> {
                authFilter.authenticate(call)
            }
        }
    }
}
