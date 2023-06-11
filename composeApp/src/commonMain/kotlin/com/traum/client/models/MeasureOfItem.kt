package com.traum.client.models

/**
 * Мера товара в одном из измерений
 * @property id Идентификатор строки в таблице
 * @property amount Количество товара в данном измерении
 * @property measurementId идентификатор меры измерения
 */
class MeasureOfItem {
    var id: Long = 0
    var amount: Double = 0.0
    var measurementId: Long = 0
}