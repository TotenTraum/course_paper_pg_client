package com.traum.client.dtos.category

import kotlinx.serialization.Serializable

@Serializable
class PatchCategoryDTO (
    var name: String? = null,
    val isDeleted: Boolean
)