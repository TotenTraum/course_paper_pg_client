package com.traum.client.screen_model.item

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.category.GetCategoryDTO
import com.traum.client.dtos.item.GetItemDTO
import com.traum.client.dtos.item.PatchItemDTO
import com.traum.client.dtos.item.PostItemDTO
import com.traum.client.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ItemScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(true)

    val fields = listOf("№", "Идентификатор", "Название", "Описание", "Не для продажи")

    val items = MutableStateFlow<MutableMap<Long?, List<GetItemDTO>>>(mutableMapOf())
    val categories = MutableStateFlow<List<GetCategoryDTO>>(listOf())

    val selectedCategory = MutableStateFlow<GetCategoryDTO?>(null)

    var categoriesComboBoxState = MutableStateFlow(false)


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            fetchCategories(client)
            fetchItems(client)
            isLoading.value = false
        }
    }

    private suspend fun fetchItems(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}items")
        items.value = response.body<List<GetItemDTO>>().groupBy { it.categoryId }.toMutableMap()
    }

    private suspend fun fetchCategories(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}categories/all")
        categories.value = response.body()
        if (categories.value.any())
            selectedCategory.value = categories.value.first()
    }

    fun remove(id: Long) = CoroutineScope(Dispatchers.Default).launch {
        isLoading.value = true
        val client = httpClient(token = UserToken.token)
        client.delete("${Config.get("baseUrl")}items/$id")
        fetchItems(client)
        isLoading.value = false
    }

    val addDialog = MutableStateFlow<ItemAddScreenModel?>(null)
    val editDialog = MutableStateFlow<ItemEditScreenModel?>(null)

    fun openAddDialog() {
        addDialog.value = ItemAddScreenModel(::fetchItems, selectedCategory.value?.id!!)
    }

    fun openEditDialog(obj: GetItemDTO) {
        editDialog.value = ItemEditScreenModel(::fetchItems, obj.id!!, obj)
    }

}

class ItemAddScreenModel(val onFinish: suspend (HttpClient) -> Unit, val selectedCategory: Long) : ScreenModel {

    var name: MutableStateFlow<String> = MutableStateFlow("")
    var description: MutableStateFlow<String> = MutableStateFlow("")
    var isNotForSale: MutableStateFlow<Boolean> = MutableStateFlow(false)

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PostItemDTO(
            name = name.value,
            description = description.value,
            isNotForSale = isNotForSale.value,
            categoryId = selectedCategory
        )
        val response = client.post("${Config.get("baseUrl")}items") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}

class ItemEditScreenModel(val onFinish: suspend (HttpClient) -> Unit, var id: Long, model: GetItemDTO) :
    ScreenModel {
    var name: MutableStateFlow<String> = MutableStateFlow("")
    var description: MutableStateFlow<String> = MutableStateFlow("")
    var isNotForSale: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        name.value = model.name!!
        description.value = model.description!!
        isNotForSale.value = model.isNotForSale!!
    }

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PatchItemDTO(name = name.value, description = description.value, isNotForSale = isNotForSale.value)
        val response = client.patch("${Config.get("baseUrl")}items/$id") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}