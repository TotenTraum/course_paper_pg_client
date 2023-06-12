package com.traum.client

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

@Serializable
object Config {
    var defaultFile = "Application.json"

    var map: MutableMap<String, String> = mutableMapOf()

    init {
        fetch()
        if (!map.containsKey("baseUrl")) {
            map["baseUrl"] = "http://localhost:8081/"
        }
        send()
    }

    fun get(key: String): String = map[key]!!
    fun set(key: String, value: String) {
        map[key] = value
    }

    private fun fetch() {
        val config = File(defaultFile)
        if (!config.exists())
            createConfigFile(config)

        val content = config.readText()
        map = Json.decodeFromString(content)
    }

    fun send() {
        val config = File(defaultFile)
        if (config.exists())
            config.writeText(Json.encodeToString(map))
    }

    private fun createConfigFile(config: File) {
        if (!config.createNewFile())
            throw FileNotFoundException()
        config.writeText("{}")
    }
}