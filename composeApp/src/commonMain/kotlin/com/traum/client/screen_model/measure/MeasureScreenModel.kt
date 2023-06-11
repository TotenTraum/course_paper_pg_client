package com.traum.client.screen_model.measure

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.UserToken
import com.traum.client.dtos.employee.PostEmployeeDTO
import com.traum.client.dtos.measures.GetMeasurementDTO
import com.traum.client.dtos.measures.PatchMeasurementDTO
import com.traum.client.dtos.measures.PostMeasurementDTO
import com.traum.client.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MeasureScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(true)

    val fields = listOf("№", "Идентификатор", "Название", "Удален")

    val measures = MutableStateFlow<List<GetMeasurementDTO>>(listOf())


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            fetchMeasures(client)
            isLoading.value = false
        }
    }

    private suspend fun fetchMeasures(client: HttpClient) {
        val response = client.get("http://localhost:8081/measurements/all")
        measures.value = response.body()
    }

    fun remove(id: Long) = CoroutineScope(Dispatchers.Default).launch {
        isLoading.value = true
        val client = httpClient(token = UserToken.token)
        client.delete("http://localhost:8081/measurements/$id")
        fetchMeasures(client)
        isLoading.value = false
    }

    val addDialog = MutableStateFlow<MeasureAddScreenModel?>(null)
    val editDialog = MutableStateFlow<MeasureEditScreenModel?>(null)

    fun openAddDialog() {
        addDialog.value = MeasureAddScreenModel(::fetchMeasures)
    }

    fun openEditDialog(obj: GetMeasurementDTO) {
        editDialog.value = MeasureEditScreenModel(::fetchMeasures, obj.id!!, obj)
    }

}

class MeasureAddScreenModel(val onFinish: suspend (HttpClient) -> Unit) : ScreenModel {

    var name = MutableStateFlow("")

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PostMeasurementDTO(name = name.value)
        val response = client.post("http://localhost:8081/measurements") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}

class MeasureEditScreenModel(val onFinish: suspend (HttpClient) -> Unit, var id: Long, model: GetMeasurementDTO) :
    ScreenModel {
    var name: MutableStateFlow<String> = MutableStateFlow("")
    var isDeleted: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        name.value = model.name!!
        isDeleted.value = model.isDeleted!!
    }

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PatchMeasurementDTO(name = name.value, isDeleted = isDeleted.value)
        val response = client.patch("http://localhost:8081/measurements/$id") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}