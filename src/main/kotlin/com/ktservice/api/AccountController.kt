package com.ktservice.api

import com.ktservice.application.ApiRouter
import com.ktservice.application.handle
import com.ktservice.services.AccountService
import com.ktservice.services.AuthToken
import com.ktservice.services.AuthenticationService
import com.google.inject.Inject
import com.ktservice.application.ApiController
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

@ApiController
class AccountController @Inject constructor(
    private val authService: AuthenticationService,
    private val accountService: AccountService,
): ApiRouter {

    override fun register(app: Application): Routing = app.routing {

        get("/account") {
            call.handle<Unit, AccountDetails> {
                val userToken: AuthToken = authService.getAuthToken(call)
                accountService.account(userToken)
            }
        }

        post("/checkout") {
            call.handle { request: CheckoutRequest ->
                val userToken: AuthToken = authService.getAuthToken(call)
                accountService.checkout(userToken, request)
            }
        }
    }
}

@Serializable
data class AccountDetails(
    val username: String,
    val role: String,
    val time: String,
    val serverLocation: String
)

@Serializable
data class CheckoutRequest(
    val item: String,
    val quantity: Int
)

@Serializable
data class CheckoutResponse(
    val totalAmount: Float,
    val expectedDelivery: String,
    val address: String
)
