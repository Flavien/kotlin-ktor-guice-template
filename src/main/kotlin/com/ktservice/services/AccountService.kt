package com.ktservice.services

import com.ktservice.api.AccountDetails
import com.ktservice.api.CheckoutRequest
import com.ktservice.api.CheckoutResponse
import com.google.inject.Inject
import com.google.inject.name.Named
import java.time.Clock
import java.time.temporal.ChronoUnit

class AccountService @Inject constructor(
    private val clock: Clock,
    @Named("server.location") private val location: String,
    @Named("inventory.itemPrice") private val itemPrice: Int,
) {
    fun account(userToken: AuthToken): AccountDetails {
        return AccountDetails(
            username = userToken.username,
            role = userToken.role,
            time = clock.instant().toString(),
            serverLocation = location
        )
    }

    fun checkout(userToken: AuthToken, request: CheckoutRequest): CheckoutResponse {
        return CheckoutResponse(
            totalAmount = itemPrice.toFloat() * request.quantity,
            expectedDelivery = clock.instant().plus(1, ChronoUnit.DAYS).toString(),
            address = "Home address of ${userToken.username}"
        )
    }
}
