package com.traum.dtos.element_of_order

import kotlinx.serialization.Serializable

@Serializable
class PatchElementOfOrderDTO {
    var amount: Int? = null
    var orderId: Long? = null
}
