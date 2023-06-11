package com.traum.models

/**
 * Категория товара
 * @property id Идентификатор строки в таблице
 * @property [name] Категория товара(Напитки, супы и т.д.)
 */
class Category {
    var id: Long = 0
    var name: String = ""
    var isDeleted: Boolean = false
}
