package com.traum.client.dtos.order

import kotlinx.serialization.Serializable

@Serializable
data class PatchOrderDTO(
    val tableId: Long?,
    val employeeId: Long?
)
