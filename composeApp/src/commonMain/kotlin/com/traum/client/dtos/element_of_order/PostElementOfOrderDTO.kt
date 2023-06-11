package com.traum.dtos.element_of_order

import kotlinx.serialization.Serializable

@Serializable
data class PostElementOfOrderDTO(
    var amount: Int,
    var orderId: Long,
    var itemId: Long,
)
