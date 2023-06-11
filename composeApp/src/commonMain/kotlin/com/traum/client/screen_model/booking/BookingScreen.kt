package com.traum.client.screen_model.booking

import AppThemeSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.traum.client.widgets.DialogWrap
import com.traum.client.widgets.MaskVisualTransformation

class BookingScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { BookingScreenModel() }

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(label = { Text("Дата") },
                    value = "",
                    onValueChange = {},
                    visualTransformation = MaskVisualTransformation("####-##-##")
                )
                Button({}) {
                    Text("Показать")
                }
            }
            bookings(model)
        }
    }


    @Composable
    private fun bookings(model: BookingScreenModel) {
        val addDialogModel = model.addDialog.collectAsState().value
        val isOpenAddDialog = addDialogModel?.isOpen?.collectAsState()?.value ?: false
        if (isOpenAddDialog) AddDialog(addDialogModel!!)

        val removeDialogModel = model.removeDialog.collectAsState().value
        val isOpenRemoveDialog = removeDialogModel?.isOpen?.collectAsState()?.value ?: false
        if (isOpenRemoveDialog) RemoveDialog(removeDialogModel!!)

        val freeTables = model.free.collectAsState().value
        LazyVerticalGrid(columns = GridCells.Fixed(25)) {
            item {
                Row(
                    Modifier.fillMaxSize().background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("#", color = AppThemeSettings.CurrentColorScheme.background)
                }
            }
            items(24) {
                Row(
                    Modifier.fillMaxSize().background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("${it + 1}", color = AppThemeSettings.CurrentColorScheme.background)
                }
            }
            freeTables.forEach {
                item {
                    Row(
                        Modifier.fillMaxSize().background(AppThemeSettings.CurrentColorScheme.onSurface)
                            .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("${it.key}", color = AppThemeSettings.CurrentColorScheme.background)
                    }
                }
                it.value.forEachIndexed { i, field ->
                    item {
                        Row(
                            Modifier.fillMaxSize().background(
                                when (field.state) {
                                    State.BUSY -> Color.Red
                                    State.FREE -> Color.Green
                                    else -> Color.Yellow
                                }
                            ).clickable {
                                when (field.state) {
                                    State.BUSY -> model.openBusyDialog(field.id)
                                    State.FREE -> model.openAddDialog(it.key!!, i)
                                    else -> {}
                                }
                            }.border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("", color = AppThemeSettings.CurrentColorScheme.background)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AddDialog(model: BookingAddDialogModel) {
        DialogWrap(onDismissRequest = { model.isOpen.value = false }, buttons = {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    model.send()
                }, enabled = !model.isBlocked.collectAsState().value) {
                    Text("Добавить")
                }
                Spacer(Modifier.weight(1.5f))
                Button(onClick = { model.isOpen.value = false }) {
                    Text("Отмена")
                }
            }
            Spacer(Modifier.height(12.dp))
        }, title = null, text = {
            Column {
                Text("Имя:")
                TextField(model.data.name.collectAsState().value, onValueChange = { model.data.name.value = it })
                Text("Номер телефона:")
                TextField(model.data.phoneNumber.collectAsState().value,
                    onValueChange = { model.data.phoneNumber.value = it })
                Text("Часов:")
                TextField(model.hour.toString(), onValueChange = { })
                Text("Минут:")
                TextField(
                    model.minute.collectAsState().value, onValueChange = model::onMinuteChange
                )
            }
        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun RemoveDialog(model: BookingRemoveDialogModel) {
        DialogWrap(onDismissRequest = { model.isOpen.value = false }, buttons = {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    model.remove()
                }) {
                    Text("Удалить")
                }
                Spacer(Modifier.weight(1.5f))
                Button(onClick = { model.isOpen.value = false }) {
                    Text("Отмена")
                }
            }
            Spacer(Modifier.height(12.dp))
        }, title = null, text = {
            Column {
                Text("Имя:")
                TextField(model.data.name.collectAsState().value, onValueChange = {})
                Text("Номер телефона:")
                TextField(model.data.phoneNumber.collectAsState().value, onValueChange = { })
                Text("Часов:")
                TextField(model.hour.toString(), onValueChange = { })
                Text("Минут:")
                TextField(model.minute.toString(), onValueChange = {})
            }
        })
    }
}