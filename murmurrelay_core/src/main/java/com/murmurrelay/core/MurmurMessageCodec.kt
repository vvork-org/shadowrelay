package com.murmurrelay.core

import kotlinx.serialization.json.Json

object MurmurMessageCodec {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun encode(message: MurmurMessage): String {
        return json.encodeToString(MurmurMessage.serializer(), message)
    }

    fun decode(value: String): MurmurMessage {
        return json.decodeFromString(MurmurMessage.serializer(), value)
    }
}