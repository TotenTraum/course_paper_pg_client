package com.traum.client.dtos.category

import kotlinx.serialization.Serializable

@Serializable
class GetCategoryDTO {
    var id: Long? = null
    var name: String? = null
    var isDeleted: Boolean? = null
}