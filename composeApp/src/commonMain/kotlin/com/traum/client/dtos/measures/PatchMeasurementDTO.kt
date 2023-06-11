package com.traum.client.dtos.measures

import kotlinx.serialization.Serializable

@Serializable
class PatchMeasurementDTO (
    var name: String? = null,
    var isDeleted: Boolean
)