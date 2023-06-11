package com.traum.client.dtos.order

import kotlinx.serialization.Serializable

@Serializable
data class PostOrderDTO(
    val tableId: Long,
    val employeeId: Long
)
