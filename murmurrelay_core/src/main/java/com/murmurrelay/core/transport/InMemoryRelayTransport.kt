package com.murmurrelay.core.transport

import com.murmurrelay.core.MurmurRelayResult

class InMemoryRelayTransport : RelayTransport {
    private val listeners = mutableMapOf<String, MutableList<(String) -> Unit>>()

    override fun sendMessage(
        roomId: String,
        encryptedPayload: String,
        onComplete: (MurmurRelayResult) -> Unit
    ) {
        listeners[roomId]?.forEach { listener ->
            listener(encryptedPayload)
        }
        onComplete(MurmurRelayResult.Success)
    }

    override fun observeMessages(
        roomId: String,
        onMessage: (String) -> Unit
    ) {
        val roomListeners = listeners.getOrPut(roomId) { mutableListOf() }
        roomListeners.add(onMessage)
    }
}