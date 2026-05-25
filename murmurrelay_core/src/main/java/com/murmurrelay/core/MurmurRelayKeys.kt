package com.murmurrelay.core

import java.security.SecureRandom
import java.util.Base64

object MurmurRelayKeys {
    fun generateSharedKey(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)

        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }
}