package com.traum.client.screen_model.analytic_panel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.traum.client.screen_model.admin_panel.AdminPanelScreenModel
import com.traum.client.screen_model.analytic_panel.report.items_report.ItemsReportScreen
import com.traum.client.screen_model.analytic_panel.report.stonks_report.StonksReportScreen
import com.traum.client.widgets.ErrorMessage
import com.traum.client.widgets.Loading

class AnalyticPanelScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { AdminPanelScreenModel() }
        val navigator = LocalNavigator.current!!
        val buttonModifier = Modifier.fillMaxWidth(0.7f)
        val fontSize = 36.sp
        if (model.isLoading.collectAsState().value)
            Loading()
        else if (model.isError.collectAsState().value)
            ErrorMessage(model.errorMsg.collectAsState().value)
        else
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = buttonModifier, onClick = { navigator.push(ItemsReportScreen()) }) {
                    Text("Отчёт по продажам товаров", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(StonksReportScreen()) }) {
                    Text("Отчёт по выручке", fontSize = fontSize)
                }
            }
    }
}