package com.traum.models

/**
 * Сотрудник кафе
 * @property id Идентификатор строки в таблице
 * @property name ФИО сотрудника
 * @property phoneNumber номер телефона сотрудника
 */
class Employee {
    var id: Long = 0
    var name: String = ""
    var phoneNumber: String = ""
    var isDeleted: Boolean = false
}
