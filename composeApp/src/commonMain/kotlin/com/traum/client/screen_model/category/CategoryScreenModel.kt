package com.traum.client.screen_model.category

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.category.GetCategoryDTO
import com.traum.client.dtos.category.PatchCategoryDTO
import com.traum.client.dtos.category.PostCategoryDTO
import com.traum.client.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CategoryScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(true)

    val fields = listOf("№", "Идентификатор", "Название", "Удален")

    val categories = MutableStateFlow<List<GetCategoryDTO>>(listOf())


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            fetchCategories(client)
            isLoading.value = false
        }
    }

    private suspend fun fetchCategories(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}categories/all")
        categories.value = response.body()
    }

    fun remove(id: Long) = CoroutineScope(Dispatchers.Default).launch {
        isLoading.value = true
        val client = httpClient(token = UserToken.token)
        client.delete("${Config.get("baseUrl")}categories/$id")
        fetchCategories(client)
        isLoading.value = false
    }

    val addDialog = MutableStateFlow<CategoryAddScreenModel?>(null)
    val editDialog = MutableStateFlow<CategoryEditScreenModel?>(null)

    fun openAddDialog() {
        addDialog.value = CategoryAddScreenModel(::fetchCategories)
    }

    fun openEditDialog(obj: GetCategoryDTO) {
        editDialog.value = CategoryEditScreenModel(::fetchCategories, obj.id!!, obj)
    }

}

class CategoryAddScreenModel(val onFinish: suspend (HttpClient) -> Unit) : ScreenModel {

    var name = MutableStateFlow("")

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PostCategoryDTO(name = name.value)
        val response = client.post("${Config.get("baseUrl")}categories") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}

class CategoryEditScreenModel(val onFinish: suspend (HttpClient) -> Unit, var id: Long, model: GetCategoryDTO) :
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
        val dto = PatchCategoryDTO(name = name.value,  isDeleted.value)
        val response = client.patch("${Config.get("baseUrl")}categories/$id") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}