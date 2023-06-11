package com.traum.client.dtos.price_of_item

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class GetPriceOfItemDTO {
    var id: Long? = null
    var price: Double? = null
    var dateOfChange: LocalDateTime? = null
    var itemId: Long? = null
}
