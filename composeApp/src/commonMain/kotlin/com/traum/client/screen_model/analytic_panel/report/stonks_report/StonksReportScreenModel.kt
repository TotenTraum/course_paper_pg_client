package com.traum.client.screen_model.analytic_panel.report.stonks_report

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.DateRangeDTO
import com.traum.client.dtos.reports.Stonks
import com.traum.client.httpClient
import com.traum.client.widgets.toLocalDate
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate

class StonksReportScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(false)
    val fields = listOf("№", "Дата", "Количество заказов", "Сумма выручки")

    val stonks = MutableStateFlow<List<Stonks>>(listOf())

    var startStr: MutableStateFlow<String> = MutableStateFlow("")
    var endStr: MutableStateFlow<String> = MutableStateFlow("")
    var errorDate = mutableStateOf(false)

    fun onStartChanged(value: String) {
        if (value.length <= 8) {
            startStr.value = value
            errorDate.value = true
        }

        if (value.length == 8) {
            try {
                toLocalDate(value)
                val start = toLocalDate(value)
                val end = toLocalDate(endStr.value)
                errorDate.value = start > end
            } catch (except: Exception) {
                errorDate.value = true
            }
        }
    }

    fun onEndChanged(value: String) {
        if (value.length <= 8) {
            endStr.value = value
            errorDate.value = true
        }

        if (value.length == 8) {
            try {
                val end = toLocalDate(value)
                val start = toLocalDate(startStr.value)
                errorDate.value = start > end

            } catch (except: Exception) {
                errorDate.value = true
            }
        }
    }

    init {
        val date = LocalDate.now().toKotlinLocalDate()
        var days = date.dayOfMonth.toString()
        if (days.length == 1) days = "0$days"

        var month = date.monthNumber.toString()
        if (month.length == 1) month = "0$month"
        startStr.value = "${days}${month}${date.year}"
        endStr.value = "${days}${month}${date.year}"
        errorDate.value = false
    }

    fun update() = CoroutineScope(Dispatchers.Default).launch {
        isLoading.value = true
        val client = httpClient(token = UserToken.token)
        fetchStonks(client)
        isLoading.value = false
    }

    private suspend fun fetchStonks(client: HttpClient) {
        val dto = DateRangeDTO()
        dto.start = toLocalDate(startStr.value)
        dto.end = toLocalDate(endStr.value)
        val response = client.get("${Config.get("baseUrl")}report/stonks") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        stonks.value = response.body()
    }
}