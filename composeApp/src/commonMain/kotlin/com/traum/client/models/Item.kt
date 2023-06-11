package com.traum.models

import com.traum.client.models.MeasureOfItem

/**
 * Товар
 * @property id Идентификатор строки в таблице
 * @property name Название товара
 * @property description Описание товара
 * @property isNotForSale Флаг, который указывает на то, что поступил в продажу товар или нет
 * @property categoryId идентификатор категории
 * @property category Категория
 * @property measures Список мер измерений
 */
class Item {
    var id: Long = 0
    var name: String = ""
    var description: String = ""
    var isNotForSale: Boolean = false
    var categoryId: Long = 0
    var category: Category? = null
    var measures: List<MeasureOfItem> = listOf()
    var priceOfItemId: Long = 0
    var priceOfItem: PriceOfItem? = null
}
