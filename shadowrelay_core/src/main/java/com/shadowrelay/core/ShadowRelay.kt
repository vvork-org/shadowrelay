package com.shadowrelay.core

import java.util.UUID
import com.shadowrelay.core.transport.RelayTransport

class ShadowRelay(
    private val transport: RelayTransport,
    private val cipher: ShadowCipher = ShadowCipherImpl()
) {

    companion object {
        fun createChannelKey(): String {
            return ShadowRelayKeys.generateSharedKey()
        }
    }

    fun send(
        channelId: String,
        channelKey: String,
        payload: String,
        type: String = "text",
        onComplete: (ShadowRelayResult) -> Unit
    ) {
        val message = ShadowMessage(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            type = type,
            payload = payload
        )

        val encodedMessage = ShadowMessageCodec.encode(message)
        val encrypted = cipher.encrypt(encodedMessage, channelKey)

        transport.sendMessage(channelId, encrypted, onComplete)
    }

    fun observe(
        channelId: String,
        channelKey: String,
        onMessage: (ShadowMessage) -> Unit
    ) {
        transport.observeMessages(channelId) { encrypted ->
            try {
                val decrypted = cipher.decrypt(encrypted, channelKey)
                val message = ShadowMessageCodec.decode(decrypted)
                onMessage(message)
            } catch (e: Exception) {
                // Ignore messages that cannot be decrypted with this shared key
            }
        }
    }
}