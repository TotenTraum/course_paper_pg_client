package com.traum.client.dtos.measure_of_item

import kotlinx.serialization.Serializable

@Serializable
class GetMeasureOfItemDTO {
    var id: Long? = null
    var amount: Double? = null
    var itemId: Long? = null
    var measurementId: Long? = null
}
