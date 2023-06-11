package com.traum.client.screen_model.order_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.traum.client.screen_model.start_menu.StartMenuScreen
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Heartbeat
import compose.icons.fontawesomeicons.solid.Smile

class SuccessOrderScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current!!
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.width(240.dp),
                imageVector = FontAwesomeIcons.Solid.Smile,
                contentDescription = null
            )
            Spacer(Modifier.requiredHeight(30.dp))
            Text("Заказ принят", fontSize = 48.sp)
            Button({ navigator.popUntil { screen -> screen is StartMenuScreen } }) {
                Text("Вернуться к главному экрану", fontSize = 24.sp)
            }
        }
    }
}