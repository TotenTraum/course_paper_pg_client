package com.traum.client.dtos.log

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class GetLogDTO {
    var id: Long? = null
    var source: String? = null
    var created: LocalDateTime? = null
    var roleCreated: String? = null
}