package com.shadowrelay.core

object PayloadCodec {
    fun encode(payload: EncryptedPayload): String {
        return listOf(
            payload.version,
            payload.saltBase64,
            payload.ivBase64,
            payload.cipherTextBase64
        ).joinToString(":")
    }

    fun decode(value: String): EncryptedPayload {
        val parts = value.split(":")
        require(parts.size == 4) { "Invalid encrypted payload format" }

        return EncryptedPayload(
            version = parts[0].toInt(),
            saltBase64 = parts[1],
            ivBase64 = parts[2],
            cipherTextBase64 = parts[3]
        )
    }
}