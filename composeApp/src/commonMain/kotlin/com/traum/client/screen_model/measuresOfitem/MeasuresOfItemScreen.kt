package com.traum.client.screen_model.measuresOfitem

import AppThemeSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.traum.client.widgets.ComboBox
import com.traum.client.widgets.DialogWrap
import com.traum.client.widgets.DropdownMenuItemWrap
import com.traum.client.widgets.Loading
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.Pen
import compose.icons.fontawesomeicons.solid.Plus

class MeasuresOfItemScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { MeasuresOfItemScreenModel() }
        val selectedItem = model.selectedItem.collectAsState().value
        val items = model.items.collectAsState().value

        if (model.isLoading.collectAsState().value)
            Loading()
        else {
            ComboBox(
                Modifier.width(220.dp),
                expanded = model.itemComboBoxState.collectAsState().value,
                onAccept = { model.itemComboBoxState.value = true },
                onDismiss = { model.itemComboBoxState.value = false },
                selectedText = {
                    Text(selectedItem?.name ?: "Выберите товар")
                })
            {
                items.forEach { item ->
                    DropdownMenuItemWrap(onClick = { model.selectedItem.value = item },
                        text = { Text(item.name!!) }
                    )
                }
            }
            Table(model)
        }
    }

    @Composable
    private fun Table(model: MeasuresOfItemScreenModel) {
        val selectedItem = model.selectedItem.collectAsState().value
        val addDialog = model.addDialog.collectAsState().value
        val editDialog = model.editDialog.collectAsState().value
        val isOpenEditDialog = editDialog?.isOpen?.collectAsState()?.value ?: false
        val isOpenAddDialog = addDialog?.isOpen?.collectAsState()?.value ?: false
        if (isOpenAddDialog)
            addDialog(model.addDialog.value!!)
        if (isOpenEditDialog)
            editDialog(model.editDialog.value!!)


        val measuresOfItem = model.measuresOfItem.collectAsState().value
        val measures = model.measures.collectAsState().value
        LazyVerticalGrid(columns = GridCells.Fixed(model.fields.size + 2)) {
            items(model.fields) {
                Row(
                    Modifier.fillMaxSize().background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(it, color = AppThemeSettings.CurrentColorScheme.background)
                }
            }
            item {
                Row(
                    Modifier.height(24.dp).background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("")
                }
            }
            item {
                Row(
                    Modifier.clickable {
                        model.openAddDialog()
                    }.height(24.dp)
                        .background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton({}) {
                        Icon(
                            modifier = Modifier.width(12.dp),
                            imageVector = FontAwesomeIcons.Solid.Plus,
                            contentDescription = null,
                            tint = AppThemeSettings.CurrentColorScheme.background
                        )
                    }
                }
            }
            if (selectedItem != null)
                measuresOfItem[selectedItem.id]?.forEachIndexed { number, measureOfItem ->
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
                            Text(measureOfItem.id.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(measures.filter{it.id == measureOfItem.measurementId}.first().name.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(measureOfItem.amount.toString())
                        }
                    }
                    item {
                        IconButton({
                            model.openEditDialog(measureOfItem)
                        }) {
                            Icon(
                                modifier = Modifier.width(12.dp),
                                imageVector = FontAwesomeIcons.Solid.Pen,
                                contentDescription = null
                            )
                        }
                    }

                    item {
                        IconButton({ model.remove(measureOfItem.id!!) }) {
                            Icon(
                                modifier = Modifier.width(12.dp),
                                imageVector = FontAwesomeIcons.Solid.Minus,
                                contentDescription = null
                            )
                        }
                    }
                }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun addDialog(model: MeasuresOfItemAddScreenModel) {
        DialogWrap(
            onDismissRequest = { model.isOpen.value = false },
            buttons = {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        model.send()
                    }) {
                        Text("Добавить")
                    }
                    Spacer(Modifier.weight(1.5f))
                    Button(
                        onClick = { model.isOpen.value = false }
                    ) {
                        Text("Отмена")
                    }
                }
                Spacer(Modifier.height(12.dp))
            },
            title = null,
            text = {
                Column {
                    Text("Название:")
                    ComboBox(
                        Modifier.width(220.dp),
                        expanded = model.itemComboBoxState.collectAsState().value,
                        onAccept = { model.itemComboBoxState.value = true },
                        onDismiss = { model.itemComboBoxState.value = false },
                        selectedText = {
                            Text(model.selectedMeasure.value?.name ?: "Выберите товар")
                        })
                    {
                        model.measures.forEach { measure ->
                            DropdownMenuItemWrap(onClick = { model.selectedMeasure.value = measure },
                                text = { Text(measure.name!!) }
                            )
                        }
                    }
                    Text("Количество:")
                    TextField(
                        model.amount.collectAsState().value.toString(),
                        onValueChange = model::onAmountChange
                    )
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun editDialog(model: MeasuresOfItemEditScreenModel) {
        DialogWrap(
            onDismissRequest = { model.isOpen.value = false },
            buttons = {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        model.send()
                    }) {
                        Text("Изменить")
                    }
                    Spacer(Modifier.weight(1.5f))
                    Button(
                        onClick = { model.isOpen.value = false }
                    ) {
                        Text("Отмена")
                    }
                }
                Spacer(Modifier.height(12.dp))
            },
            title = null,
            text = {
                Column {
                    Text("Название:")
                    ComboBox(
                        Modifier.width(220.dp),
                        expanded = model.itemComboBoxState.collectAsState().value,
                        onAccept = { model.itemComboBoxState.value = true },
                        onDismiss = { model.itemComboBoxState.value = false },
                        selectedText = {
                            Text(model.selectedMeasure.value?.name ?: "Выберите товар")
                        })
                    {
                        model.measures.forEach { measure ->
                            DropdownMenuItemWrap(onClick = { model.selectedMeasure.value = measure },
                                text = { Text(measure.name!!) }
                            )
                        }
                    }
                    Text("Количество:")
                    TextField(
                        model.amount.collectAsState().value.toString(),
                        onValueChange = model::onAmountChange
                    )
                }
            }
        )
    }
}