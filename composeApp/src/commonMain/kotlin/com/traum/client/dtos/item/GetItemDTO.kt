package com.traum.client.dtos.item

import kotlinx.serialization.Serializable

@Serializable
class GetItemDTO {
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    var isNotForSale: Boolean? = null
    var categoryId: Long? = null
}
