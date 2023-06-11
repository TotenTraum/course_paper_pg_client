package com.traum.client

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object UserToken {
    var token by mutableStateOf<String?>(null)
}