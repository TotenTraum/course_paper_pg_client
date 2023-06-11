package com.traum.client.dtos

import kotlinx.serialization.Serializable

@Serializable
class ErrorDTO(val reason: String) {
    val description: String = ""
}