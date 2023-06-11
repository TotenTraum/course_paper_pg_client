package com.traum.client.dtos.item

import kotlinx.serialization.Serializable

@Serializable
data class PostItemDTO(
    var name: String,
    var description: String,
    var isNotForSale: Boolean,
    var categoryId: Long
)