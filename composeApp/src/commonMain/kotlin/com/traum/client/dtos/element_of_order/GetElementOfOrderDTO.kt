package com.traum.client.dtos.element_of_order

import kotlinx.serialization.Serializable

@Serializable
class GetElementOfOrderDTO {
    var id: Long? = null
    var amount: Int? = null
    var sum: Double? = null
    var priceOfItemId: Long? = null
    var orderId: Long? = null
    var itemId: Long? = null
}
