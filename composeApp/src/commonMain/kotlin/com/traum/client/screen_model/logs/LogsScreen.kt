package com.traum.client.screen_model.logs

import AppThemeSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.traum.client.widgets.Loading

class LogsScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { LogsScreenModel() }
        if (model.isLoading.collectAsState().value)
            Loading()
        else
            Table(model)
    }

    @Composable
    private fun Table(model: LogsScreenModel) {
        val logs = model.logs.collectAsState().value
        LazyVerticalGrid(columns = GridCells.Fixed(model.fields.size)) {
            this.items(model.fields) {
                Row(
                    Modifier.fillMaxSize().background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(it, color = AppThemeSettings.CurrentColorScheme.background)
                }
            }
            logs.forEachIndexed { number, log ->
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(number.toString())
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(log.id.toString())
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(log.source.toString())
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(log.created.toString())
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(log.roleCreated.toString())
                    }
                }
            }
        }
    }
}