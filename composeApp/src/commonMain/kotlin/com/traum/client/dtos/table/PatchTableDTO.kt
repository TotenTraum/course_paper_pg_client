package com.traum.client.dtos.table

import kotlinx.serialization.Serializable

@Serializable
class PatchTableDTO(
    val tableNumber: Int? = null,
    val isDeleted: Boolean
)

