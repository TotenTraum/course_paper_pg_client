package com.traum.client.models

import com.traum.models.Item
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Элемент заказа
 * @property id Идентификатор строки в таблице
 * @property sum Общая сумма элемента заказа
 * @property amount Количество
 * @property orderId идентификатор заказа
 * @property order Заказ
 * @property itemId идентификатор товара
 * @property item Товар
 */
class ElementOfOrder {
    var id: Long = 0
    var amount: MutableStateFlow<Int> = MutableStateFlow(0)
    var sum: Double = 0.0
    var orderId: Long = 0
    var itemId: Long = 0
    var item: Item? = null
}
