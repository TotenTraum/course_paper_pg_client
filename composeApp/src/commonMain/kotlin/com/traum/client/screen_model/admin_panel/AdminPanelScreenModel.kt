package com.traum.client.screen_model.admin_panel

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.UserToken
import com.traum.client.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AdminPanelScreenModel : ScreenModel {
    init {
        CoroutineScope(Dispatchers.Default).launch {
            isLoading.value = false
        }
    }

    var isLoading = MutableStateFlow(true)
    var isError = MutableStateFlow(false)
    var errorMsg = MutableStateFlow("")
}