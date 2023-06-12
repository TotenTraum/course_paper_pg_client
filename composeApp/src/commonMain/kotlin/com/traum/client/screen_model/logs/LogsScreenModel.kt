package com.traum.client.screen_model.logs

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.log.GetLogDTO
import com.traum.client.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LogsScreenModel : ScreenModel {
    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            val response = client.get("${Config.get("baseUrl")}logs")
            logs.value = response.body()
            isLoading.value = false
        }
    }

    val fields = listOf("№", "Идентификатор", "Источник", "Дата создания", "Роль")

    val logs = MutableStateFlow<List<GetLogDTO>>(listOf())

    var isLoading = MutableStateFlow(true)

}