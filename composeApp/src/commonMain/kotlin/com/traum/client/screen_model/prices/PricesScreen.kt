package com.traum.client.screen_model.prices

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
import compose.icons.fontawesomeicons.solid.Plus

class PricesScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { PricesScreenModel() }
        val selectedItem = model.selectedItem.collectAsState().value
        val items = model.items.collectAsState().value
        if (model.isLoading.collectAsState().value)
            Loading()
        else {
            ComboBox(
                Modifier.width(220.dp),
                expanded = model.pricesComboBoxState.collectAsState().value,
                onAccept = { model.pricesComboBoxState.value = true },
                onDismiss = { model.pricesComboBoxState.value = false },
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
    private fun Table(model: PricesScreenModel) {
        val selectedItem = model.selectedItem.collectAsState().value
        val addDialog = model.addDialog.collectAsState().value
        val isOpenAddDialog = addDialog?.isOpen?.collectAsState()?.value ?: false
        if (isOpenAddDialog)
            addDialog(model.addDialog.value!!)

        val pricesOfItem = model.prices.collectAsState().value
        LazyVerticalGrid(columns = GridCells.Fixed(model.fields.size + 1)) {
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
                    Modifier.clickable {
                        model.openAddDialog()
                    }.height(24.dp)
                        .background(AppThemeSettings.CurrentColorScheme.onSurface)
                        .border(1.dp, AppThemeSettings.CurrentColorScheme.background),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    if (selectedItem != null) {
                        IconButton({ model.openAddDialog() }) {
                            Icon(
                                modifier = Modifier.width(12.dp),
                                imageVector = FontAwesomeIcons.Solid.Plus,
                                contentDescription = null,
                                tint = AppThemeSettings.CurrentColorScheme.background
                            )
                        }
                    } else
                        Text("")
                }
            }
            if (selectedItem != null)
                pricesOfItem[selectedItem.id!!]?.forEachIndexed { number, prices ->
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
                            Text(prices.id.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(prices.price.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(prices.dateOfChange.toString())
                        }
                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("")
                        }
                    }
                }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun addDialog(model: PricesAddScreenModel) {
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
                    Text("Новая цена:")
                    TextField(model.price.collectAsState().value.toString(), onValueChange = model::onPriceChange)
                }
            }
        )
    }
}