package com.traum.client.screen_model.main

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel

class MainScreenModel : ScreenModel {
    var settingsExpanded = mutableStateOf(false)
}