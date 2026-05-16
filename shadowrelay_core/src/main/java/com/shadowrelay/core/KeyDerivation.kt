package com.shadowrelay.core

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object KeyDerivation {
    fun deriveAesKey(password: String, salt: ByteArray): SecretKeySpec {
        val spec = PBEKeySpec(
            password.toCharArray(),
            salt,
            120_000,
            256
        )

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = factory.generateSecret(spec).encoded

        return SecretKeySpec(keyBytes, "AES")
    }
}