package com.traum.client.screen_model.order_screen

import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.category.GetCategoryDTO
import com.traum.client.dtos.employee.GetEmployeeDTO
import com.traum.client.dtos.item.GetItemDTO
import com.traum.client.dtos.measure_of_item.GetMeasureOfItemDTO
import com.traum.client.dtos.measures.GetMeasurementDTO
import com.traum.client.dtos.order.PostOrderDTO
import com.traum.client.dtos.price_of_item.GetPriceOfItemDTO
import com.traum.client.dtos.table.GetTableDTO
import com.traum.client.httpClient
import com.traum.client.models.ElementOfOrder
import com.traum.client.models.MeasureOfItem
import com.traum.dtos.element_of_order.PostElementOfOrderWithPriceDTO
import com.traum.models.Employee
import com.traum.models.Item
import com.traum.models.PriceOfItem
import com.traum.models.Table
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrderScreenModel() : ScreenModel {
    var categories: MutableStateFlow<List<GetCategoryDTO>> = MutableStateFlow(listOf())
    var itemsGroupedByCategory: MutableStateFlow<Map<Long?, List<Item>>> = MutableStateFlow(mapOf())
    private var items: MutableStateFlow<List<Item>> = MutableStateFlow(listOf())

    var measures: MutableStateFlow<List<GetMeasurementDTO>> =
        MutableStateFlow(mutableListOf())
    var tables: MutableStateFlow<List<Table>> =
        MutableStateFlow(mutableListOf())
    var employees: MutableStateFlow<List<Employee>> =
        MutableStateFlow(mutableListOf())


    var selectedCategory = MutableStateFlow(GetCategoryDTO())
    var selectedItem = MutableStateFlow(Item())
    var selectedTable: MutableStateFlow<Table?> = MutableStateFlow(null)
    var selectedEmployee: MutableStateFlow<Employee?> = MutableStateFlow(null)

    var tableComboBoxState = MutableStateFlow(false)
    var employeeComboBoxState = MutableStateFlow(false)

    val accessToFinishOrder: Boolean
        get() = selectedTable.value != null && selectedEmployee.value != null

    var isLoading = MutableStateFlow(true)
    var isError = MutableStateFlow(false)
    var msgError = MutableStateFlow("")

    var isVisibleItemDialog = MutableStateFlow(false)
    var amountDialog = MutableStateFlow(0)

    var isVisibleFinishDialog = MutableStateFlow(false)


    class SendState {
        var orderCreated = false
        var elementSended = false
        var orderID: Long? = null
    }

    val sendState = SendState()

    var itemsInCart: MutableStateFlow<List<ElementOfOrder>> = MutableStateFlow(listOf())


    init {
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            downloadCategories(client)
            downloadItems(client)
            downloadPrices(client)
            downloadMeasuresOfItem(client)
            downloadMeasures(client)
            downloadTables(client)
            downloadEmployees(client)
            isLoading.value = false
        }
    }

    fun selectItem(item: Item) = CoroutineScope(Dispatchers.Default).launch {
        selectedItem.value = item
        amountDialog.value = 1
        isVisibleItemDialog.value = true
    }

    private suspend fun downloadCategories(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}categories")
        responseHandler(response) {
            categories.value = response.body()
            if (categories.value.any())
                selectedCategory.value = categories.value.first()
        }
    }

    private suspend fun downloadItems(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}items/priced")
        responseHandler(response) {
            items.value =
                response.body<List<GetItemDTO>>().map {
                    val item = Item()
                    item.id = it.id!!
                    item.isNotForSale = it.isNotForSale!!
                    item.name = it.name!!
                    item.description = it.description!!
                    item.categoryId = it.categoryId!!
                    return@map item
                }
            itemsGroupedByCategory.value = items.value.groupBy { it.categoryId }
        }
    }

    private suspend fun downloadPrices(client: HttpClient) {
        items.value.forEach {
            val response = client.get("${Config.get("baseUrl")}items/${it.id}/prices/latest")
            responseHandler(response) {
                val priceDTO = response.body<GetPriceOfItemDTO>()
                val priceOfItem = PriceOfItem()
                priceOfItem.price = priceDTO.price!!
                priceOfItem.dateOfChange = priceDTO.dateOfChange!!
                it.priceOfItemId = priceDTO.id!!
                it.priceOfItem = priceOfItem
            }
        }
    }

    private suspend fun downloadMeasuresOfItem(client: HttpClient) {
        items.value.forEach {
            val response = client.get("${Config.get("baseUrl")}items/${it.id}/measures")
            responseHandler(response) {
                val measure = response.body<List<GetMeasureOfItemDTO>>().map {
                    val measureOfItem = MeasureOfItem()
                    measureOfItem.id = it.id!!
                    measureOfItem.measurementId = it.measurementId!!
                    measureOfItem.amount = it.amount!!
                    measureOfItem
                }
                it.measures = measure
            }
        }
    }

    private suspend fun downloadMeasures(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}measurements")
        responseHandler(response) {
            measures.value = response.body<List<GetMeasurementDTO>>()
        }
    }

    private suspend fun downloadTables(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}tables")
        responseHandler(response) {
            tables.value = response.body<List<GetTableDTO>>().map {
                val table = Table()
                table.id = it.id!!
                table.tableNumber = it.tableNumber!!
                table
            }
        }
    }

    private suspend fun downloadEmployees(client: HttpClient) {
        val response = client.get("${Config.get("baseUrl")}employees")
        responseHandler(response) {
            employees.value = response.body<List<GetEmployeeDTO>>().map {
                val employee = Employee()
                employee.name = it.name!!
                employee.id = it.id!!
                employee.phoneNumber = it.phoneNumber!!
                employee
            }
        }
    }

    fun addElement() {
        val elementOfOrder = ElementOfOrder()
        elementOfOrder.itemId = selectedItem.value.id
        elementOfOrder.item = selectedItem.value
        elementOfOrder.amount.value = amountDialog.value
        itemsInCart.value += elementOfOrder
    }

    fun removeElement(elementOfOrder: ElementOfOrder) {
        itemsInCart.value -= elementOfOrder
    }

    fun endOrder(onError: ((String) -> Unit)? = null, onSuccess: () -> Unit) =
        CoroutineScope(Dispatchers.Default).launch {
            val client = httpClient(token = UserToken.token)
            if (!sendState.orderCreated) {
                createOrder(client, onError)
            }
            if(sendState.orderCreated) {
                sendElements(client, onSuccess, onError)
            }
        }

    private suspend fun createOrder(client: HttpClient, onError: ((String) -> Unit)? = null) {
        val response = client.post("${Config.get("baseUrl")}orders") {
            contentType(ContentType.Application.Json)
            setBody(PostOrderDTO(selectedTable.value?.id!!, selectedEmployee.value?.id!!))
        }
        responseHandler(response,
            errorContent = {
                onError?.invoke("Произошла ошибка, попробуйте чуть позже")
            }) {
            sendState.orderID = response.body<HashMap<String, Long>>().get("id")
            sendState.orderCreated = true
        }
    }

    private suspend fun sendElements(client: HttpClient, onSuccess: () -> Unit, onError: ((String) -> Unit)? = null) {
        val response = client.post("${Config.get("baseUrl")}orders/${sendState.orderID}/elements/list") {
            contentType(ContentType.Application.Json)
            setBody(itemsInCart.value.map {
                PostElementOfOrderWithPriceDTO(
                    it.amount.value,
                    sendState.orderID!!,
                    it.itemId,
                    it.item!!.priceOfItemId
                )
            })
        }
        responseHandler(response,
            errorContent = {
                onError?.invoke("Произошла ошибка, попробуйте чуть позже")
            }) {
            onSuccess()
        }
    }

    private suspend fun responseHandler(
        response: HttpResponse,
        errorContent: (suspend () -> Unit)? = null,
        successContent: suspend () -> Unit
    ) {
        if (response.status.value in 200..299) {
            successContent()
        } else {
            if (errorContent == null) {
                msgError.value = "${response.status.description}\n${response.bodyAsText()}"
                isError.value = true
            } else
                errorContent()
        }
    }
}