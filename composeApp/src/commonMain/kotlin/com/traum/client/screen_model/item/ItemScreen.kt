package com.traum.client.screen_model.item

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

class ItemScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { ItemScreenModel() }
        val selectedCategory = model.selectedCategory.collectAsState().value
        val categories = model.categories.collectAsState().value

        if (model.isLoading.collectAsState().value)
            Loading()
        else {
            ComboBox(
                Modifier.width(220.dp),
                expanded = model.categoriesComboBoxState.collectAsState().value,
                onAccept = { model.categoriesComboBoxState.value = true },
                onDismiss = { model.categoriesComboBoxState.value = false },
                selectedText = {
                    Text(selectedCategory?.name ?: "Выберите товар")
                })
            {
                categories.forEach { item ->
                    DropdownMenuItemWrap(onClick = { model.selectedCategory.value = item },
                        text = { Text(item.name!!) }
                    )
                }
            }
            Table(model)
        }
    }

    @Composable
    private fun Table(model: ItemScreenModel) {
        val selectedCategory = model.selectedCategory.collectAsState().value
        val addDialog = model.addDialog.collectAsState().value
        val editDialog = model.editDialog.collectAsState().value
        val isOpenEditDialog = editDialog?.isOpen?.collectAsState()?.value ?: false
        val isOpenAddDialog = addDialog?.isOpen?.collectAsState()?.value ?: false
        if (isOpenAddDialog)
            addDialog(model.addDialog.value!!)
        if (isOpenEditDialog)
            editDialog(model.editDialog.value!!)


        val items = model.items.collectAsState().value
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
                    IconButton({model.openAddDialog()}) {
                        Icon(
                            modifier = Modifier.width(12.dp),
                            imageVector = FontAwesomeIcons.Solid.Plus,
                            contentDescription = null,
                            tint = AppThemeSettings.CurrentColorScheme.background
                        )
                    }
                }
            }
            if (selectedCategory != null)
                items[selectedCategory.id]?.forEachIndexed { number, item ->
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
                            Text(item.id.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(item.name.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(item.description.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(if (item.isNotForSale!!) "Да" else "Нет")
                        }
                    }
                    item {
                        IconButton({
                            model.openEditDialog(item)
                        }) {
                            Icon(
                                modifier = Modifier.width(12.dp),
                                imageVector = FontAwesomeIcons.Solid.Pen,
                                contentDescription = null
                            )
                        }
                    }

                    item {
                        IconButton({ model.remove(item.id!!) }) {
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
    private fun addDialog(model: ItemAddScreenModel) {
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
                    TextField(model.name.collectAsState().value, onValueChange = { model.name.value = it })
                    Text("Описание:")
                    TextField(
                        model.description.collectAsState().value,
                        onValueChange = { model.description.value = it })
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun editDialog(model: ItemEditScreenModel) {
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
                    TextField(model.name.collectAsState().value, onValueChange = { model.name.value = it })
                    Text("Описание:")
                    TextField(
                        model.description.collectAsState().value,
                        onValueChange = { model.description.value = it })
                    Text("Не для продажи:")
                    Checkbox(
                        model.isNotForSale.collectAsState().value,
                        onCheckedChange = { model.isNotForSale.value = it })
                }
            }
        )
    }
}