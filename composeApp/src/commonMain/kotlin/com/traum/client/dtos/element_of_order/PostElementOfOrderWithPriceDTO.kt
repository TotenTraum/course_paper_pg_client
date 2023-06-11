package com.traum.dtos.element_of_order

import kotlinx.serialization.Serializable

@Serializable
data class PostElementOfOrderWithPriceDTO(
    var amount: Int,
    var orderId: Long,
    var itemId: Long,
    var priceOfItemId: Long
)
