package com.traum.client.screen_model.order_screen

import AppThemeSettings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.traum.client.widgets.*
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.Plus
import kotlinx.coroutines.launch

class OrderScreen : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { OrderScreenModel() }
        if (model.isLoading.collectAsState().value)
            Loading()
        else if (model.isError.collectAsState().value)
            ErrorMessage(model.msgError.collectAsState().value)
        else
            loadedContent(model)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun showCategories(model: OrderScreenModel) {
        val categories = model.categories.collectAsState()
        val selected = model.selectedCategory.collectAsState()
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Категории", fontSize = 24.sp)
        }
        Divider()
        categories.value.forEach { category ->
            NavigationDrawerItem(
                label = { Text(category.name!!) },
                selected = category == selected.value,
                onClick = {
                    model.selectedCategory.value = category
                }
            )
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun loadedContent(model: OrderScreenModel) {
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val state = mutableStateOf(State.Categories)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    when (state.value) {
                        State.Categories -> showCategories(model)
                        State.Cart -> cartLayout(model)
                    }
                }
            })
        {
            Column {
                Row {
                    Row {
                        Button({
                            scope.launch {
                                state.value = State.Categories
                                drawerState.open()
                            }
                        }) {
                            Text("Открыть список категорий")
                        }
                        Button({
                            scope.launch {
                                state.value = State.Cart
                                drawerState.open()
                            }
                        }) {
                            Text("Открыть корзину")
                        }
                    }
                }
                showItems(model)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun showItems(model: OrderScreenModel) {
        val categoryId = model.selectedCategory.collectAsState().value.id

        val groupsOfItem = model.itemsGroupedByCategory.collectAsState()
        if (groupsOfItem.value.containsKey(categoryId)) {
            if (groupsOfItem.value[categoryId]?.isEmpty()!!) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("В данной категории нет товаров")
                }
            } else {
                groupsOfItem.value[categoryId]?.forEach {
                    ListItem(
                        modifier = Modifier.clickable { model.selectItem(it) },
                        headlineText = {
                            Text(it.name, fontSize = 24.sp)
                        },
                        supportingText = {
                            Text(it.description, fontSize = 16.sp)
                        },
                        trailingContent = {
                            val price = it.priceOfItem?.price
                            Text("$price Руб.", fontSize = 16.sp)
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = AppThemeSettings.CurrentColorScheme.surfaceColorAtElevation(
                                3.dp
                            )
                        )
                    )
                    Divider()
                }
                if (model.isVisibleItemDialog.collectAsState().value)
                    showItemDialog(model)
                if (model.isVisibleFinishDialog.collectAsState().value)
                    showFinishDialog(model)
            }
        } else {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Выберите категорию")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun showItemDialog(model: OrderScreenModel) {
        DialogWrap(
            onDismissRequest = { model.isVisibleItemDialog.value = false },
            buttons = {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            model.addElement()
                            model.isVisibleItemDialog.value = false
                        }
                    ) {
                        Text("Добавить")
                    }
                    Spacer(Modifier.weight(1.5f))
                    Button(
                        onClick = { model.isVisibleItemDialog.value = false }
                    ) {
                        Text("Отмена")
                    }
                }
                Spacer(Modifier.height(12.dp))
            },
            title = {},
            text = {
                val item = model.selectedItem.collectAsState().value
                val measures = model.measures.collectAsState().value
                val amount = model.amountDialog.collectAsState().value
                Column {
                    Text(item.name, fontSize = 24.sp)
                    Text(item.description, fontSize = 16.sp)
                    Row {
                        item.measures.forEach { measure ->
                            val filteredMeasures = measures.filter { it.id == measure.measurementId }
                            if (filteredMeasures.any())
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text("${measure.amount} ${filteredMeasures.single().name}") }
                                )
                        }
                    }
                    Text("Количество:")
                    Row {
                        Column {
                            IconButton({ model.amountDialog.value += 1 }) {
                                Icon(
                                    modifier = Modifier.width(24.dp),
                                    imageVector = FontAwesomeIcons.Solid.Plus,
                                    contentDescription = null
                                )
                            }
                            IconButton(
                                { model.amountDialog.value -= 1 },
                                enabled = (model.amountDialog.value > 1)
                            ) {
                                Icon(
                                    modifier = Modifier.width(24.dp),
                                    imageVector = FontAwesomeIcons.Solid.Minus,
                                    contentDescription = null
                                )
                            }
                        }
                        Text("$amount", fontSize = 64.sp)
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun cartList(model: OrderScreenModel) {
        val itemsInCart = model.itemsInCart.collectAsState().value

        if (!itemsInCart.any()) {
            Text("Пустовато что-то тут...")
            return
        }

        itemsInCart.forEach { itemInCart ->
            itemInCart.item
            val item = itemInCart.item!!
            val price = item.priceOfItem!!
            val amount = itemInCart.amount.collectAsState().value
            ListItem(
                headlineText = {
                    Text(item.name)
                },
                supportingText = {
                    Text("Стоимость: ${price.price * amount} руб. \nКоличество: $amount")
                },
                trailingContent = {
                    Row {
                        IconButton({ itemInCart.amount.value += 1 }) {
                            Icon(FontAwesomeIcons.Solid.Plus, null)
                        }
                        IconButton(
                            { itemInCart.amount.value -= 1 },
                            enabled = (itemInCart.amount.value > 1)
                        ) {
                            Icon(FontAwesomeIcons.Solid.Minus, null)
                        }
                        IconButton({ model.removeElement(itemInCart) }) {
                            Icon(Icons.Default.Delete, null)
                        }
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun cartLayout(model: OrderScreenModel) {
        Scaffold(
            topBar = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Корзина", fontSize = 24.sp)
                }
            },
            bottomBar = {
                Column {
                    Divider()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button({ model.isVisibleFinishDialog.value = true }) {
                            Text("Завершить заказ")
                        }
                    }
                }
            })
        {
            val scrollState = rememberScrollState()
            Column(Modifier.padding(it)) {
                Divider()
                Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
                    cartList(model)
                }
            }
        }
    }

    enum class State {
        Categories,
        Cart
    }

    @Composable
    private fun showFinishDialog(model: OrderScreenModel) {
        val navigator = LocalNavigator.current!!
        DialogWrap(
            onDismissRequest = { model.isVisibleFinishDialog.value = false },
            buttons = {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        model.endOrder(onError = { text ->
                            navigator.push(ErrorOrderScreen( { model.endOrder(onSuccess = it)}, text))
                        }) {
                            navigator.push(SuccessOrderScreen())
                        }
                        model.isVisibleFinishDialog.value = false
                        model.isLoading.value = true
                    }, enabled = model.accessToFinishOrder) {
                        Text("Закрыть заказ")
                    }
                    Spacer(Modifier.weight(1.5f))
                    Button(
                        onClick = { model.isVisibleFinishDialog.value = false }
                    ) {
                        Text("Отмена")
                    }
                }
                Spacer(Modifier.height(12.dp))
            },
            title = {},
            text = {

                val selectedTable = model.selectedTable.collectAsState().value
                val tables = model.tables.collectAsState().value

                Column {
                    Text("Стол:")
                    ComboBox(
                        Modifier.width(220.dp),
                        expanded = model.tableComboBoxState.value,
                        onAccept = { model.tableComboBoxState.value = true },
                        onDismiss = { model.tableComboBoxState.value = false },
                        selectedText = {
                            Text(selectedTable?.tableNumber?.toString() ?: "Выберите стол")
                        }) {
                        tables.forEach { table ->
                            DropdownMenuItemWrap(onClick = { model.selectedTable.value = table },
                                text = { Text("Стол ${table.tableNumber}") }
                            )
                        }
                    }

                    val selectedEmployee = model.selectedEmployee.collectAsState().value
                    val employees = model.employees.collectAsState().value

                    Text("Сотрудник:")
                    ComboBox(
                        Modifier.width(220.dp),
                        expanded = model.employeeComboBoxState.value,
                        onAccept = { model.employeeComboBoxState.value = true },
                        onDismiss = { model.employeeComboBoxState.value = false },
                        selectedText = {
                            Text(selectedEmployee?.name ?: "Выберите сотрудника")
                        }) {
                        employees.forEach { employee ->
                            DropdownMenuItemWrap(onClick = { model.selectedEmployee.value = employee },
                                text = { Text(employee.name) }
                            )
                        }
                    }
                }
            }
        )
    }
}