package com.traum.client.screen_model.measure

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
import com.traum.client.widgets.DialogWrap
import com.traum.client.widgets.Loading
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.Pen
import compose.icons.fontawesomeicons.solid.Plus

class MeasureScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { MeasureScreenModel() }
        if (model.isLoading.collectAsState().value)
            Loading()
        else {
            Table(model)
        }
    }

    @Composable
    private fun Table(model: MeasureScreenModel) {
        val addDialog = model.addDialog.collectAsState().value
        val editDialog = model.editDialog.collectAsState().value
        val isOpenEditDialog = editDialog?.isOpen?.collectAsState()?.value ?: false
        val isOpenAddDialog = addDialog?.isOpen?.collectAsState()?.value ?: false
        if (isOpenAddDialog)
            addDialog(model.addDialog.value!!)
        if (isOpenEditDialog)
            editDialog(model.editDialog.value!!)


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
            measures.forEachIndexed { number, measure ->
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
                        Text(measure.id.toString())
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(measure.name.toString())
                    }
                }
                item {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(if (measure.isDeleted!!) "Да" else "Нет")
                    }
                }
                item {
                    IconButton({
                        model.openEditDialog(measure)
                    }) {
                        Icon(
                            modifier = Modifier.width(12.dp),
                            imageVector = FontAwesomeIcons.Solid.Pen,
                            contentDescription = null
                        )
                    }
                }

                item {
                    IconButton({ model.remove(measure.id!!) }) {
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
    private fun addDialog(model: MeasureAddScreenModel) {
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
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun editDialog(model: MeasureEditScreenModel) {
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
                    Text("удалён:")
                    Checkbox(model.isDeleted.collectAsState().value, onCheckedChange = { model.isDeleted.value = it })
                }
            }
        )
    }
}