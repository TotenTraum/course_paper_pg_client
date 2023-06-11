package com.traum.client.screen_model.auth

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.UserToken
import com.traum.client.dtos.ErrorDTO
import com.traum.client.dtos.auth.LoginDTO
import com.traum.client.httpClient
import io.ktor.client.call.*
import io.ktor.client.network.sockets.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthScreenModel : ScreenModel {
    val username = MutableStateFlow("root")
    val password = MutableStateFlow("r")
    val isCorrect = MutableStateFlow(true)
    val errorText = MutableStateFlow("")

    val scope = CoroutineScope(Dispatchers.Default)

    fun login(onSuccess: () -> Unit) = scope.launch {
        isCorrect.value = true
        errorText.value = ""
        try {
            val response = httpClient().post("http://localhost:8081/login") {
                contentType(ContentType.Application.Json)
                setBody(
                    LoginDTO(
                        username = username.value,
                        password = password.value
                    )
                )
            }

            if (response.status.value in 200..299) {
                val map = response.body<HashMap<String, String>>()
                if (map.containsKey("token")) {
                    UserToken.token = map["token"]!!
                    onSuccess()
                } else {
                    isCorrect.value = false
                    errorText.value = "Проблема со стороны сервера"
                }
            } else if (response.status == HttpStatusCode.Forbidden) {
                isCorrect.value = false
                errorText.value = response.body<ErrorDTO>().reason
            } else if (response.status == HttpStatusCode.NotFound) {
                isCorrect.value = false
                errorText.value = "Not found"
            } else {
                isCorrect.value = false
                errorText.value = "Неизвестная ошибка"
            }
        }
        catch (exception: Exception){
            isCorrect.value = false
            errorText.value = "Возможно проблемы с интернетом..."
        }
    }
}
