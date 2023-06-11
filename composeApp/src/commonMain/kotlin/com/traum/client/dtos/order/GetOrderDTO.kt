package com.traum.client.dtos.order

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class GetOrderDTO {
    var id: Long? = null
    var tableId: Long? = null
    var employeeId: Long? = null
    var sum: Double? = null
    var date: LocalDateTime? = null
}
