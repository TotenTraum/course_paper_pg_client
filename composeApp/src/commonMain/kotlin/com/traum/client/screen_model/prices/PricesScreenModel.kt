package com.traum.client.screen_model.prices

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.item.GetItemDTO
import com.traum.client.dtos.price_of_item.GetPriceOfItemDTO
import com.traum.client.dtos.price_of_item.PostPriceOfItemDTO
import com.traum.client.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PricesScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(true)

    val fields = listOf("№", "Идентификатор", "Цена", "Дата изменения")

    val selectedItem = MutableStateFlow<GetItemDTO?>(null)

    val prices = MutableStateFlow<MutableMap<Long, List<GetPriceOfItemDTO>>>(mutableMapOf())
    val items = MutableStateFlow<List<GetItemDTO>>(listOf())

    var pricesComboBoxState = MutableStateFlow(false)

    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            fetchItems(client)
            fetchPrices(client)
            isLoading.value = false
        }
    }

    private suspend fun fetchPrices(client: HttpClient) {
        items.value.forEach {
            val response = client.get("${Config.get("baseUrl")}items/${it.id}/prices")
            prices.value[it.id!!] = response.body()
        }
    }

    private suspend fun fetchItems(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}items")
        items.value = response.body()
        if (items.value.any())
            selectedItem.value = items.value.first()
    }

    val addDialog = MutableStateFlow<PricesAddScreenModel?>(null)

    fun openAddDialog() {
        addDialog.value = PricesAddScreenModel(::fetchPrices, selectedItem.value?.id!!)
    }


}

class PricesAddScreenModel(val onFinish: suspend (HttpClient) -> Unit, val id: Long) : ScreenModel {

    var price = MutableStateFlow(0.0)

    var isOpen = MutableStateFlow(true)

    fun onPriceChange(value: String) {
        if (value.isEmpty())
            price.value = 0.0

        val result = value.toDoubleOrNull()
        if(result != null)
            price.value = result
    }

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PostPriceOfItemDTO(price = price.value)
        val response = client.post("${Config.get("baseUrl")}items/$id/prices") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}