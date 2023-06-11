package com.traum.models

import kotlinx.datetime.LocalDateTime


/**
 * Цена товара
 * @property id Идентификатор строки в таблице
 * @property price Цена товара
 * @property dateOfChange Дата изменения цены на товар
 */
class PriceOfItem {
    var price: Double = 0.0
    var dateOfChange: LocalDateTime? = null
}
