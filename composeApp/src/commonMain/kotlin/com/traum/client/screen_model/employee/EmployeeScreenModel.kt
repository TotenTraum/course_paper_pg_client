package com.traum.client.screen_model.employee

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.UserToken
import com.traum.client.dtos.employee.GetEmployeeDTO
import com.traum.client.dtos.employee.PatchEmployeeDTO
import com.traum.client.dtos.employee.PostEmployeeDTO
import com.traum.client.httpClient
import com.traum.client.screen_model.measure.MeasureAddScreenModel
import com.traum.client.screen_model.measure.MeasureEditScreenModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EmployeeScreenModel : ScreenModel {
    var isLoading = MutableStateFlow(true)

    val fields = listOf("№", "Идентификатор", "ФИО", "Номер телефона", "Удален")

    val employees = MutableStateFlow<List<GetEmployeeDTO>>(listOf())


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            fetchEmployees(client)
            isLoading.value = false
        }
    }

    private suspend fun fetchEmployees(client: HttpClient) {
        val response = client.get("http://localhost:8081/employees/all")
        employees.value = response.body()
    }

    fun remove(id: Long) = CoroutineScope(Dispatchers.Default).launch {
        isLoading.value = true
        val client = httpClient(token = UserToken.token)
        client.delete("http://localhost:8081/employees/$id")
        fetchEmployees(client)
        isLoading.value = false
    }

    val addDialog = MutableStateFlow<EmployeeAddScreenModel?>(null)
    val editDialog = MutableStateFlow<EmployeeEditScreenModel?>(null)

    fun openAddDialog() {
        addDialog.value = EmployeeAddScreenModel(::fetchEmployees)
    }

    fun openEditDialog(obj: GetEmployeeDTO) {
        editDialog.value = EmployeeEditScreenModel(::fetchEmployees, obj.id!!, obj)
    }

}

class EmployeeAddScreenModel(val onFinish: suspend (HttpClient) -> Unit) : ScreenModel {

    var name = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PostEmployeeDTO(name = name.value, phoneNumber = phoneNumber.value)
        val response = client.post("http://localhost:8081/employees") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}

class EmployeeEditScreenModel(val onFinish: suspend (HttpClient) -> Unit, var id: Long, model: GetEmployeeDTO) :
    ScreenModel {
    var name: MutableStateFlow<String> = MutableStateFlow("")
    var phoneNumber: MutableStateFlow<String> = MutableStateFlow("")
    var isDeleted: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        name.value = model.name!!
        phoneNumber.value = model.phoneNumber!!
        isDeleted.value = model.isDeleted!!
    }

    var isOpen = MutableStateFlow(true)

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val dto = PatchEmployeeDTO(name = name.value, phoneNumber = phoneNumber.value, isDeleted.value)
        val response = client.patch("http://localhost:8081/employees/$id") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}