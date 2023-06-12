package com.traum.client.dtos

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class DateRangeDTO {
    var start: LocalDate? = null
    var end: LocalDate? = null
}