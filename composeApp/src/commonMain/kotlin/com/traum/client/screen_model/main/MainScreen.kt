package com.traum.client.screen_model.main

import AppThemeSettings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.traum.client.screen_model.auth.AuthScreen
import com.traum.client.screen_model.start_menu.StartMenuScreen
import com.traum.client.widgets.ComboIconBox
import com.traum.client.widgets.DropdownMenuItemWrap
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowLeft
import compose.icons.fontawesomeicons.solid.Bars
import compose.icons.fontawesomeicons.solid.Moon
import compose.icons.fontawesomeicons.solid.Sun
import swapAppColor

class MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val model = rememberScreenModel { MainScreenModel() }

        var onLogOut: (() -> Unit)? = null
        var onBack: (() -> Unit)? = null

        Scaffold(topBar = {
            TopAppBar(title = { Text("") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = AppThemeSettings.CurrentColorScheme.surfaceColorAtElevation(
                        3.dp
                    )
                ),
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = { onBack?.invoke() }) {
                            Icon(imageVector = FontAwesomeIcons.Solid.ArrowLeft, null)
                        }
                    }
                },
                actions = {
                    ComboIconBox(
                        modifier = Modifier.width(60.dp),
                        expanded = model.settingsExpanded.value,
                        onAccept = { model.settingsExpanded.value = true },
                        onDismiss = { model.settingsExpanded.value = false },
                        selectedIcon = {
                            Icon(
                                modifier = Modifier.width(40.dp).height(40.dp).clickable {
                                    model.settingsExpanded.value = true
                                },
                                imageVector = FontAwesomeIcons.Solid.Bars,
                                contentDescription = null
                            )
                        }
                    ) {
                        DropdownMenuItemWrap(onClick = {},
                            text = { Text("Настройки") },
                            leadingIcon = {
                                Icon(
                                    modifier = Modifier.width(24.dp),
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = ""
                                )
                            }
                        )
                        DropdownMenuItemWrap(
                            onClick = ::swapAppColor,
                            text = { Text("Смена тему") },
                            leadingIcon = {
                                Icon(
                                    modifier = Modifier.width(24.dp),
                                    imageVector = if (AppThemeSettings.isDark.value)
                                        FontAwesomeIcons.Solid.Moon
                                    else
                                        FontAwesomeIcons.Solid.Sun,
                                    contentDescription = ""
                                )
                            }
                        )
                        if (onLogOut != null)
                            DropdownMenuItemWrap(
                                onClick = {
                                    onLogOut?.invoke()
                                },
                                text = { Text("Выйти в меню авторизации") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.ExitToApp,
                                        contentDescription = "Вернуться обратно"
                                    )
                                }
                            )
                    }
                    Spacer(Modifier.width(4.dp))

                })
        }) {
            Column(Modifier.padding(it)) {
                Navigator(screen = AuthScreen()) { navigator ->
                    onLogOut = if (navigator.lastItem is AuthScreen)
                        null
                    else {
                        { navigator.popUntil { screen -> screen is AuthScreen } }
                    }
                    onBack = if (navigator.lastItem is AuthScreen || navigator.lastItem is StartMenuScreen)
                        null
                    else {
                        { navigator.pop() }
                    }
                    CurrentScreen()
                }
            }
        }
    }
}