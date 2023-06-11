package com.traum.client.screen_model.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import cafe.adriel.voyager.navigator.LocalNavigator
import com.traum.client.screen_model.start_menu.StartMenuScreen

class AuthScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current!!

        val model = rememberScreenModel { AuthScreenModel() }

        val username = model.username.collectAsState()
        val password = model.password.collectAsState()
        val isCorrect = model.isCorrect.collectAsState()
        val errorText = model.errorText.collectAsState()

        Column {
            Row(Modifier.fillMaxHeight(0.9f)) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {}
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        username.value,
                        label = { Text("Логин") },
                        onValueChange = { model.username.value = it })
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        password.value,
                        label = { Text("Пароль") },
                        onValueChange = { model.password.value = it })
                    Spacer(Modifier.height(10.dp))
                    if (!isCorrect.value)
                        Text(errorText.value)
                    Button(onClick = { model.login { navigator.push(StartMenuScreen()) } }) { Text("Войти") }
                }
            }
        }
    }
}