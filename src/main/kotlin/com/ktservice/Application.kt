package com.ktservice

import com.ktservice.application.configureRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@ExperimentalSerializationApi
fun Application.module() {
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                namingStrategy = JsonNamingStrategy.SnakeCase
            }
        )
    }

    configureRouting(this)
}
