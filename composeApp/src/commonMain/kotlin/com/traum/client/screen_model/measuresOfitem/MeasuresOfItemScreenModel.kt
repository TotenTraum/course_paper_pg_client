package com.traum.client.screen_model.measuresOfitem

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.UserToken
import com.traum.client.dtos.item.GetItemDTO
import com.traum.client.dtos.measure_of_item.GetMeasureOfItemDTO
import com.traum.client.dtos.measure_of_item.PatchMeasureOfItemDTO
import com.traum.client.dtos.measure_of_item.PostMeasureOfItemDTO
import com.traum.client.dtos.measures.GetMeasurementDTO
import com.traum.client.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MeasuresOfItemScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(true)

    val fields = listOf("№", "Идентификатор", "Название", "Количество")

    val measuresOfItem = MutableStateFlow<MutableMap<Long?, List<GetMeasureOfItemDTO>>>(mutableMapOf())
    val items = MutableStateFlow<List<GetItemDTO>>(listOf())

    val selectedItem = MutableStateFlow<GetItemDTO?>(null)
    val measures = MutableStateFlow<List<GetMeasurementDTO>>(listOf())

    var itemComboBoxState = MutableStateFlow(false)


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            fetchItems(client)
            fetchMeasures(client)
            fetchMeasuresOfItem(client)
            isLoading.value = false
        }
    }

    private suspend fun fetchMeasuresOfItem(client: HttpClient) {
        items.value.forEach {
            val response = client.get("http://localhost:8081/items/${it.id}/measures")
            measuresOfItem.value[it.id!!] = response.body()
        }
    }

    private suspend fun fetchItems(client: HttpClient) {
        val response = client.get("http://localhost:8081/items")
        items.value = response.body()
        if (items.value.any()) selectedItem.value = items.value.first()
    }

    private suspend fun fetchMeasures(client: HttpClient) {
        val response = client.get("http://localhost:8081/measurements/all")
        measures.value = response.body()
    }

    fun remove(id: Long) = CoroutineScope(Dispatchers.Default).launch {
        if (selectedItem.value != null) {
            isLoading.value = true
            val client = httpClient(token = UserToken.token)
            client.delete("http://localhost:8081/items/${selectedItem.value?.id}/measures")
            fetchMeasures(client)
            isLoading.value = false
        }
    }

    val addDialog = MutableStateFlow<MeasuresOfItemAddScreenModel?>(null)
    val editDialog = MutableStateFlow<MeasuresOfItemEditScreenModel?>(null)

    fun openAddDialog() {
        addDialog.value = MeasuresOfItemAddScreenModel(::fetchMeasures, selectedItem.value?.id!!, measures.value)
    }

    fun openEditDialog(obj: GetMeasureOfItemDTO) {
        editDialog.value =
            MeasuresOfItemEditScreenModel(::fetchMeasures, selectedItem.value!!.id!!, obj.id!!, obj, measures.value)
    }
}

class MeasuresOfItemAddScreenModel(
    val onFinish: suspend (HttpClient) -> Unit, val selectedItem: Long,
    var measures: List<GetMeasurementDTO>
) : ScreenModel {
    var amount: MutableStateFlow<Double> = MutableStateFlow(0.0)

    val selectedMeasure = MutableStateFlow<GetMeasurementDTO?>(null)

    var itemComboBoxState = MutableStateFlow(false)


    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        if (selectedMeasure.value != null) {
            val client = httpClient(token = UserToken.token)
            val dto = PostMeasureOfItemDTO(
                measurementId = selectedMeasure.value?.id!!,
                amount = amount.value
            )
            val response = client.post("http://localhost:8081/items/$selectedItem/measures") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            onFinish(client)
            isOpen.value = false
        }
    }

    fun onAmountChange(value: String) {
        if (value.isEmpty())
            amount.value = 0.0

        val result = value.toDoubleOrNull()
        if (result != null)
            amount.value = result
    }
}

class MeasuresOfItemEditScreenModel(
    val onFinish: suspend (HttpClient) -> Unit,
    var itemId: Long,
    var id: Long,
    model: GetMeasureOfItemDTO,
    var measures: List<GetMeasurementDTO>
) : ScreenModel {

    val selectedMeasure = MutableStateFlow<GetMeasurementDTO?>(null)

    var amount: MutableStateFlow<Double> = MutableStateFlow(0.0)
    var itemComboBoxState = MutableStateFlow(false)


    init {
        amount.value = model.amount!!
    }

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        if (selectedMeasure.value != null) {
            val client = httpClient(token = UserToken.token)
            val dto = PatchMeasureOfItemDTO(amount = amount.value, measurementId = selectedMeasure.value!!.id)
            val response = client.patch("http://localhost:8081/items/$itemId/measures/$id") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            onFinish(client)
            isOpen.value = false
        }
    }

    fun onAmountChange(value: String) {
        if (value.isEmpty())
            amount.value = 0.0

        val result = value.toDoubleOrNull()
        if (result != null)
            amount.value = result
    }
}