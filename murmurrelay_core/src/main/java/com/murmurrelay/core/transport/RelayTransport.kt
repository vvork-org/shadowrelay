package com.murmurrelay.core.transport

import com.murmurrelay.core.MurmurRelayResult

interface RelayTransport {
    fun sendMessage(
        channelId: String,
        encryptedPayload: String,
        onComplete: (MurmurRelayResult) -> Unit
    )

    fun observeMessages(
        channelId: String,
        onMessage: (String) -> Unit
    )
}