package com.traum.client.screen_model.analytic_panel.report.stonks_report

import AppThemeSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.traum.client.widgets.Loading
import com.traum.client.widgets.MaskVisualTransformation

class StonksReportScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { StonksReportScreenModel() }
        if (model.isLoading.collectAsState().value)
            Loading()
        else
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        label = { Text("start") },
                        value = model.startStr.collectAsState().value,
                        onValueChange = model::onStartChanged,
                        visualTransformation = MaskVisualTransformation("##-##-####")
                    )
                    OutlinedTextField(
                        label = { Text("end") },
                        value = model.endStr.collectAsState().value,
                        onValueChange = model::onEndChanged,
                        visualTransformation = MaskVisualTransformation("##-##-####")
                    )
                    Button({ model.update() }, enabled = !model.errorDate.value) {
                        Text("Показать")
                    }
                }
                Table(model)
            }
    }

    @Composable
    private fun Table(model: StonksReportScreenModel) {

        val stonks = model.stonks.collectAsState().value
        LazyVerticalGrid(columns = GridCells.Fixed(model.fields.size)) {
            items(model.fields) {
                Row(
                    Modifier.fillMaxSize().background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(it, color = AppThemeSettings.CurrentColorScheme.background)
                }
            }
            stonks.forEachIndexed { number, stonk ->
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
                        Text("${stonk.date} | ${stonk.date?.dayOfWeek?.name}")
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(stonk.count.toString())
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(stonk.sum.toString())
                    }
                }
            }
        }
    }
}