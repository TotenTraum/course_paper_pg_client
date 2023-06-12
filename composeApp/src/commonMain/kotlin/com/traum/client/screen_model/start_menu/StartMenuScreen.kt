package com.traum.client.screen_model.start_menu

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
import com.traum.client.screen_model.admin_panel.AdminPanelScreen
import com.traum.client.screen_model.analytic_panel.AnalyticPanelScreen
import com.traum.client.screen_model.booking.BookingScreen
import com.traum.client.screen_model.order_screen.OrderScreen
import com.traum.client.widgets.ErrorMessage
import com.traum.client.widgets.Loading

class StartMenuScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { StartMenuScreenModel() }
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
                if(model.isEmployee.collectAsState().value){
                    Button(modifier = buttonModifier, onClick = { navigator.push(OrderScreen()) }) {
                        Text("Новый заказ", fontSize = fontSize)
                    }
                    Spacer(Modifier.requiredHeight(24.dp))
                    Button(modifier = buttonModifier, onClick = { navigator.push(BookingScreen()) }) {
                        Text("Забронировать", fontSize = fontSize)
                    }
                    Spacer(Modifier.requiredHeight(24.dp))
                }
                if(model.isAnalytic.collectAsState().value) {
                    Button(modifier = buttonModifier, onClick = { navigator.push(AnalyticPanelScreen()) }) {
                        Text("Аналитика", fontSize = fontSize)
                    }
                    Spacer(Modifier.requiredHeight(24.dp))
                }
                if (model.isAdmin.collectAsState().value)
                    Button(modifier = buttonModifier, onClick = { navigator.push(AdminPanelScreen()) }) {
                        Text("Админ-панель", fontSize = fontSize)
                    }
            }
    }
}