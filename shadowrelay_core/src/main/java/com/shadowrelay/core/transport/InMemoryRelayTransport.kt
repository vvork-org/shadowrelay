package com.shadowrelay.core.transport

import com.shadowrelay.core.ShadowRelayResult

class InMemoryRelayTransport : RelayTransport {
    private val listeners = mutableMapOf<String, MutableList<(String) -> Unit>>()

    override fun sendMessage(
        roomId: String,
        encryptedPayload: String,
        onComplete: (ShadowRelayResult) -> Unit
    ) {
        listeners[roomId]?.forEach { listener ->
            listener(encryptedPayload)
        }
        onComplete(ShadowRelayResult.Success)
    }

    override fun observeMessages(
        roomId: String,
        onMessage: (String) -> Unit
    ) {
        val roomListeners = listeners.getOrPut(roomId) { mutableListOf() }
        roomListeners.add(onMessage)
    }
}