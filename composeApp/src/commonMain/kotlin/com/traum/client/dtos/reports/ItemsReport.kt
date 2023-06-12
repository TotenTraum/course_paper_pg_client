package com.traum.client.dtos.reports

import kotlinx.serialization.Serializable

@Serializable
class ItemsReport {
    var itemName: String = ""
    var count: Long = 0
}