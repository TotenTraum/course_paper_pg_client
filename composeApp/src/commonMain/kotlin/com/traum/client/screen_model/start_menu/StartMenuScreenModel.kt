package com.traum.client.screen_model.start_menu

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StartMenuScreenModel : ScreenModel {
    init {
        CoroutineScope(Dispatchers.Default).launch {
            val response =
                httpClient(token = UserToken.token).get("${Config.get("baseUrl")}groups")
            if (response.status.value in 200..299) {
//                val map = response.body<HashMap<String, Boolean>>()
                val list = response.body<List<String>>()
                isAdmin.value = list.contains("admins")
                isAnalytic.value = list.contains("analytics")
                isEmployee.value = list.contains("employees")
            } else {
                errorMsg.value = response.status.description
                isError.value = true
            }
            isLoading.value = false
        }
    }

    var isAdmin = MutableStateFlow(false)
    var isAnalytic = MutableStateFlow(false)
    var isEmployee = MutableStateFlow(false)
    var isLoading = MutableStateFlow(true)
    var isError = MutableStateFlow(false)
    var errorMsg = MutableStateFlow("")
}