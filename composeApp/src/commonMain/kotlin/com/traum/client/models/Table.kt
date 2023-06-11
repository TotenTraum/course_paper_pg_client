package com.traum.models

/**
 * Стол в кафе
 * @property id Идентификатор строки в таблице
 * @property tableNumber Номер стола в кафе
 */
class Table {
    var id: Long = 0
    var tableNumber: Int = 0
    var isDeleted: Boolean = false
}
