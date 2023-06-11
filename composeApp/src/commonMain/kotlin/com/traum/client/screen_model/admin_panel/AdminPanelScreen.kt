package com.traum.client.screen_model.admin_panel

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
import com.traum.client.screen_model.category.CategoryScreen
import com.traum.client.screen_model.employee.EmployeeScreen
import com.traum.client.screen_model.item.ItemScreen
import com.traum.client.screen_model.logs.LogsScreen
import com.traum.client.screen_model.measure.MeasureScreen
import com.traum.client.screen_model.measuresOfitem.MeasuresOfItemScreen
import com.traum.client.screen_model.prices.PricesScreen
import com.traum.client.screen_model.table.TableScreen
import com.traum.client.widgets.ErrorMessage
import com.traum.client.widgets.Loading

class AdminPanelScreen : Screen {
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
                Button(modifier = buttonModifier, onClick = { navigator.push(LogsScreen()) }) {
                    Text("Просмотреть логи", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(TableScreen()) }) {
                    Text("Редактор столов", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(EmployeeScreen()) }) {
                    Text("Редактор сотрудников", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(CategoryScreen()) }) {
                    Text("Редактор Категорий", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(ItemScreen()) }) {
                    Text("Редактор товаров", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(PricesScreen()) }) {
                    Text("Редактор цен", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(MeasureScreen()) }) {
                    Text("Редактор мер измерений", fontSize = fontSize)
                }
                Spacer(Modifier.requiredHeight(24.dp))
                Button(modifier = buttonModifier, onClick = { navigator.push(MeasuresOfItemScreen()) }) {
                    Text("Редактор мер измерений на товаре", fontSize = fontSize)
                }
            }
    }
}