package com.shadowrelay.core.transport

import com.shadowrelay.core.ShadowRelayResult

interface RelayTransport {
    fun sendMessage(
        channelId: String,
        encryptedPayload: String,
        onComplete: (ShadowRelayResult) -> Unit
    )

    fun observeMessages(
        channelId: String,
        onMessage: (String) -> Unit
    )
}