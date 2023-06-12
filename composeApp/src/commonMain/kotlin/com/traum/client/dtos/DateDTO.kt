package com.traum.client.dtos

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class DateDTO(val date: LocalDate)