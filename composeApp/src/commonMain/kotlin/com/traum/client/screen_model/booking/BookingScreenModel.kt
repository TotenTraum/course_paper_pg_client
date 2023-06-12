package com.traum.client.screen_model.booking

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.traum.client.Config
import com.traum.client.UserToken
import com.traum.client.dtos.BookingContactData
import com.traum.client.dtos.BookingContactDataDTO
import com.traum.client.dtos.DateDTO
import com.traum.client.dtos.booking.GetBookingDTO
import com.traum.client.dtos.booking.PostBookingDTO
import com.traum.client.dtos.table.GetTableDTO
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.ext.clearQuotes
import kotlin.collections.set
import java.time.LocalDate as JavaDate

enum class State {
    FREE, BUSY, UNDEFINED
}

class Field(var state: State = State.FREE, var id: Long = 0) {}

class BookingScreenModel : ScreenModel {
    var lastUsedDate: LocalDate = JavaDate.now().toKotlinLocalDate()
    var dateStr: MutableStateFlow<String> = MutableStateFlow("")
    var errorDate = mutableStateOf(false)

    fun onDateChanged(value: String) {
        if (value.length <= 8) {
            dateStr.value = value
            errorDate.value = true
        }

        if (value.length == 8) {
            try {
                toLocalDate(value)
                errorDate.value = false
            } catch (except: Exception) {
                errorDate.value = true
            }
        }
    }

    val free: MutableStateFlow<MutableMap<Long?, Array<Field>>> = MutableStateFlow(mutableMapOf())
    val tables: MutableStateFlow<List<GetTableDTO>> = MutableStateFlow(listOf())
    var bookings: MutableStateFlow<List<GetBookingDTO>> = MutableStateFlow(listOf())

    init {
        var days = lastUsedDate.dayOfMonth.toString()
        if(days.length == 1) days = "0$days"

        var month = lastUsedDate.monthNumber.toString()
        if(month.length == 1) month = "0$month"
        dateStr.value = "${days}${month}${lastUsedDate.year}"
        CoroutineScope(Dispatchers.Default).launch {
            update()
        }
    }

    fun update() = CoroutineScope(Dispatchers.Default).launch {
        lastUsedDate = toLocalDate(dateStr.value)
        val client = httpClient(token = UserToken.token)
        fetchSuspend(client)
    }

    private suspend fun fetchSuspend(client: HttpClient) {
        val freeTables: MutableMap<Long?, Array<Field>> = mutableMapOf()
        var response = client.get("${Config.get("baseUrl")}tables/all")
        tables.value = response.body()
        tables.value.forEach {
            if (it.isDeleted == false) freeTables[it.id!!] = Array(24) {
                Field()
            }
        }
        response = client.post("${Config.get("baseUrl")}bookings/date") {
            contentType(ContentType.Application.Json)
            setBody(DateDTO(lastUsedDate))
        }
        bookings.value = response.body()
        val groupedByTable = bookings.value.groupBy { it.tableId }

        groupedByTable.forEach {
            if (tables.value.any { x -> x.id == it.key && x.isDeleted == false }) freeTables[it.key] =
                timelineToField(it.value)
        }
        free.value = freeTables
    }

    private suspend fun timelineToField(bookings: List<GetBookingDTO>): Array<Field> {
        val arr = Array(24) { Field() }
        bookings.forEach { booking ->
            if (booking.isCanceled != true) {
                val nums = timelineHoursToList(booking.start!!, booking.end!!)
                nums.forEach { num ->
                    arr[num].state = State.BUSY
                    arr[num].id = booking.id!!
                }
            }
        }
        for (i in 0..22) {
            if (arr[i].state == State.FREE && arr[i + 1].state == State.BUSY) {
                arr[i].state = State.UNDEFINED
            }
        }
        return arr
    }

    private suspend fun timelineHoursToList(startDate: LocalDateTime, endDate: LocalDateTime): List<Int> {
        val list = mutableListOf<Int>()
        val pointDate = if (startDate.date == lastUsedDate) startDate else endDate
        val diff = if (startDate.date == lastUsedDate) 0 else -2
        val addition = if (pointDate.minute != 0) 1 else 0
        repeat(2 + addition) {
            list.add(pointDate.hour + diff + it)
        }
        return list.filter { it in 0..23 }
    }

    fun openAddDialog(tableId: Long, hour: Int) {
        addDialog.value = BookingAddDialogModel(lastUsedDate, hour, tableId, ::fetchSuspend)
    }

    fun openBusyDialog(bookingId: Long) {
        removeDialog.value = BookingRemoveDialogModel(bookings.value.first { it.id == bookingId }, ::fetchSuspend)
    }

    val addDialog = MutableStateFlow<BookingAddDialogModel?>(null)
    val removeDialog = MutableStateFlow<BookingRemoveDialogModel?>(null)

}

class BookingAddDialogModel(
    val date: LocalDate, val hour: Int, private val tableId: Long, val onFinish: suspend (HttpClient) -> Unit
) {
    val data = BookingContactData()

    val minute = MutableStateFlow("")

    var isOpen = MutableStateFlow(true)
    var isBlocked = MutableStateFlow(true)

    fun onMinuteChange(value: String) {
        val int = value.toIntOrNull()
        if (int == null) isBlocked.value = true
        else isBlocked.value = int !in 0..59
        minute.value = value

    }

    fun send() = CoroutineScope(Dispatchers.Default).launch {
        val contactDataDTO = BookingContactDataDTO()
        contactDataDTO.name = data.name.value
        contactDataDTO.phoneNumber = data.phoneNumber.value
        val time = LocalDateTime(date, kotlinx.datetime.LocalTime(hour, minute.value.toInt(), 0))
        val client = httpClient(token = UserToken.token)
        val dto = PostBookingDTO(
            contactData = Json.encodeToString(contactDataDTO), start = time, tableId = tableId
        )
        val response = client.post("${Config.get("baseUrl")}bookings") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
        onFinish(client)
        isOpen.value = false
    }
}

class BookingRemoveDialogModel(
    val obj: GetBookingDTO, val onFinish: suspend (HttpClient) -> Unit
) {
    val data = BookingContactData()
    val hour = obj.start?.hour
    val minute = obj.start?.minute

    var isOpen = MutableStateFlow(true)

    init {
        val dto = Json.decodeFromString<BookingContactDataDTO>(cleanup(obj.contactData!!))
        data.name.value = dto.name
        data.phoneNumber.value = dto.phoneNumber
    }

    private fun cleanup(string: String): String {
        return string.clearQuotes().replace("\\\"", "\"")
    }

    fun remove() = CoroutineScope(Dispatchers.Default).launch {
        val client = httpClient(token = UserToken.token)
        val response = client.delete("${Config.get("baseUrl")}bookings/${obj.id}")
        onFinish(client)
        isOpen.value = false
    }
}