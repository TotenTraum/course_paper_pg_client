package com.traum.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit, token: String?) = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(Json)
    }

    if(token != null){
        install(Auth){
            bearer {
                loadTokens {
                    BearerTokens(token, "")
                }
            }
        }
    }
}