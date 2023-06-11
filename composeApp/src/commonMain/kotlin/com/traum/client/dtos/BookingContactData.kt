package com.traum.client.dtos

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

class BookingContactData {
    var name = MutableStateFlow("")
    var phoneNumber = MutableStateFlow("")
}

@Serializable
class BookingContactDataDTO {
    var name: String = ""
    var phoneNumber: String = ""
}