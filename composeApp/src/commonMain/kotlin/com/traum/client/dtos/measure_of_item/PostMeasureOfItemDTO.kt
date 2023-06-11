package com.traum.client.dtos.measure_of_item

import kotlinx.serialization.Serializable

@Serializable
class PostMeasureOfItemDTO(
    var amount: Double,
    var measurementId: Long
)
