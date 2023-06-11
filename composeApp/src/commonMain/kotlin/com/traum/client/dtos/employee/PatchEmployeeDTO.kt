package com.traum.client.dtos.employee

import kotlinx.serialization.Serializable

@Serializable
data class PatchEmployeeDTO(
    var name: String? = null,
    var phoneNumber: String? = null,
    var isDeleted: Boolean? = null
)
