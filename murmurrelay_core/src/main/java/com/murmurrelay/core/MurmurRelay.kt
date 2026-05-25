package com.murmurrelay.core

import java.util.UUID
import com.murmurrelay.core.transport.RelayTransport

class MurmurRelay(
    private val transport: RelayTransport,
    private val cipher: MurmurCipher = MurmurCipherImpl()
) {

    companion object {
        fun createChannelKey(): String {
            return MurmurRelayKeys.generateSharedKey()
        }
    }

    fun send(
        channelId: String,
        channelKey: String,
        payload: String,
        type: String = "text",
        onComplete: (MurmurRelayResult) -> Unit
    ) {
        val message = MurmurMessage(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            type = type,
            payload = payload
        )

        val encodedMessage = MurmurMessageCodec.encode(message)
        val encrypted = cipher.encrypt(encodedMessage, channelKey)

        transport.sendMessage(channelId, encrypted, onComplete)
    }

    fun observe(
        channelId: String,
        channelKey: String,
        onMessage: (MurmurMessage) -> Unit
    ) {
        transport.observeMessages(channelId) { encrypted ->
            try {
                val decrypted = cipher.decrypt(encrypted, channelKey)
                val message = MurmurMessageCodec.decode(decrypted)
                onMessage(message)
            } catch (e: Exception) {
                // Ignore messages that cannot be decrypted with this shared key
            }
        }
    }
}