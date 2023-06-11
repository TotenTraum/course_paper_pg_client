package com.traum.client.dtos.booking

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PostBookingDTO(
    var contactData: String,
    var start: LocalDateTime,
    var tableId: Long,
)