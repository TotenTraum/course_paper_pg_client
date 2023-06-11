package com.traum.client.dtos.booking

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class GetBookingDTO {
    var id: Long? = null
    var contactData: String? = null
    var start: LocalDateTime? = null
    var end: LocalDateTime? = null
    var isCanceled: Boolean? = null
    var tableId: Long? = null
}
