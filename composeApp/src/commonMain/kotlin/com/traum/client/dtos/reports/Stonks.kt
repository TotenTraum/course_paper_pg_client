package com.traum.client.dtos.reports

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class Stonks {
    /**
     * День продаж
     */
    var date: LocalDate? = null

    /**
     * Количество заказов
     */
    var count: Long? = null

    /**
     * Выручка
     */
    var sum: Long? = null
}