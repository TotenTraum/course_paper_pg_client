package com.traum.client

import AppTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.traum.client.screen_model.main.MainScreen
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

@Composable
internal fun App(modifier: Modifier, titleBar: (@Composable () -> Unit)? = null) {
    AppTheme {
        titleBar?.invoke()
    }
    AppTheme(modifier = modifier) {
        Navigator(MainScreen())
    }
}

expect fun httpClient(
    config: HttpClientConfig<*>.() -> Unit = {},
    token: String? = null
): HttpClient