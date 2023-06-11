package com.traum.client.screen_model.analytic_panel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AnalyticPanelScreenModel : ScreenModel {
    init {
        CoroutineScope(Dispatchers.Default).launch {
            isLoading.value = false
        }
    }

    var isLoading = MutableStateFlow(true)
    var isError = MutableStateFlow(false)
    var errorMsg = MutableStateFlow("")
}