package com.shadowrelay.core

import kotlinx.serialization.json.Json

object ShadowMessageCodec {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun encode(message: ShadowMessage): String {
        return json.encodeToString(ShadowMessage.serializer(), message)
    }

    fun decode(value: String): ShadowMessage {
        return json.decodeFromString(ShadowMessage.serializer(), value)
    }
}