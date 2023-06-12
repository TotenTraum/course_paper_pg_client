package com.traum.client.screen_model.table

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.table.GetTableDTO
import com.traum.client.dtos.table.PatchTableDTO
import com.traum.client.dtos.table.PostTableDTO
import com.traum.client.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TableScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(true)

    val fields = listOf("№", "Идентификатор", "Номер стола", "Удален")

    val tables = MutableStateFlow<List<GetTableDTO>>(listOf())


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            fetchTables(client)
            isLoading.value = false
        }
    }

    private suspend fun fetchTables(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}tables/all")
        tables.value = response.body()
    }

    fun remove(id: Long) = CoroutineScope(Dispatchers.Default).launch {
        isLoading.value = true
        val client = httpClient(token = UserToken.token)
        client.delete("${Config.get("baseUrl")}tables/$id")
        fetchTables(client)
        isLoading.value = false
    }

    val addDialog = MutableStateFlow<TableAddScreenModel?>(null)
    val editDialog = MutableStateFlow<TableEditScreenModel?>(null)

    fun openAddDialog() {
        addDialog.value = TableAddScreenModel(::fetchTables)
    }

    fun openEditDialog(obj: GetTableDTO) {
        editDialog.value = TableEditScreenModel(::fetchTables, obj.id!!, obj)
    }

}

class TableAddScreenModel(val onFinish: suspend (HttpClient) -> Unit) : ScreenModel {

    var tableNumber: MutableStateFlow<Int> = MutableStateFlow(0)
    var isOpen = MutableStateFlow(true)

    fun onNumberChange(value: String) {
        if (value.isEmpty())
            tableNumber.value = 0

        val result = value.toIntOrNull()
        if(result != null)
            tableNumber.value = result
    }

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PostTableDTO(tableNumber = tableNumber.value)
        val response = client.post("${Config.get("baseUrl")}tables") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}

class TableEditScreenModel(val onFinish: suspend (HttpClient) -> Unit, var id: Long, model: GetTableDTO) :
    ScreenModel {
    var tableNumber: MutableStateFlow<Int> = MutableStateFlow(0)
    var isDeleted: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        tableNumber.value = model.tableNumber!!
        isDeleted.value = model.isDeleted!!
    }

    fun onNumberChange(value: String) {
        if (value.isEmpty())
            tableNumber.value = 0

        val result = value.toIntOrNull()
        if(result != null)
            tableNumber.value = result
    }

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PatchTableDTO(tableNumber = tableNumber.value, isDeleted.value)
        val response = client.patch("${Config.get("baseUrl")}tables/$id") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}