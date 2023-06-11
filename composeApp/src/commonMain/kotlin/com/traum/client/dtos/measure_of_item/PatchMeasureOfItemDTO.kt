package com.traum.client.dtos.measure_of_item

import kotlinx.serialization.Serializable

@Serializable
class PatchMeasureOfItemDTO(
    var amount: Double? = null,
    var measurementId: Long? = null
)
